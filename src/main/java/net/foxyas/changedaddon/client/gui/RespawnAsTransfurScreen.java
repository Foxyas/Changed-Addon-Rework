package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.gui.util.SuggestionHelper;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.network.packet.RespawnAsTransfurPacket;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RespawnAsTransfurScreen extends Screen {

    private static final ResourceLocation TEXTURE =
            ChangedAddonMod.resourceLoc("textures/gui/infected_spawn.png");

    private final DeathScreen previousDeathScreen;
    public List<ResourceLocation> possibleTransfurVariants;
    private EditBox typeBox;
    private boolean canChooseVariant = false;
    private List<String> allSuggestions = new ArrayList<>();
    private Map<String, List<TransfurVariant<?>>> nameToVariants = new HashMap<>();
    private SuggestionHelper suggestionHelper;

    public RespawnAsTransfurScreen(DeathScreen previousDeathScreen) {
        super(Component.literal("Spawn as Transfurred"));
        this.previousDeathScreen = previousDeathScreen;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        assert this.minecraft != null;
        Player player = this.minecraft.player;

        // -----------------------------------------------------
        // Determine if the player is allowed to choose a variant
        // -----------------------------------------------------
        canChooseVariant =
                player != null &&
                        (player.hasPermissions(2)
                                || ChangedAddonServerConfiguration.ALLOW_PLAYERS_TO_SELECT_RESPAWN_TRANSFUR.get());

        // -----------------------------------------------------
        // Load list of variants and set up the edit box
        // -----------------------------------------------------
        if (canChooseVariant) {

            // Load all display names for the suggestion list
            allSuggestions = TransfurVariant.getPublicTransfurVariants()
                    .map(v -> v.getEntityType().getDescription().getString())
                    .toList();

            // Map display name -> list of variants
            nameToVariants = TransfurVariant.getPublicTransfurVariants()
                    .collect(Collectors.groupingBy(
                            v -> v.getEntityType().getDescription().getString()
                    ));

            // Create the input box
            typeBox = new EditBox(
                    this.font,
                    centerX - 75,
                    centerY - 40,
                    150,
                    20,
                    Component.literal("Transfur Id")
            );

            typeBox.setMaxLength(128);
            this.addRenderableWidget(typeBox);

            // -----------------------------------------------------
            // Initialize the SuggestionHelper
            // -----------------------------------------------------
            suggestionHelper = new SuggestionHelper(typeBox, allSuggestions, chosen -> {
            });
            suggestionHelper.setRenderUpwards(true);    // display suggestions upwards
            suggestionHelper.setMaxSuggestions(6);

            typeBox.setResponder(s -> suggestionHelper.update());
        }

        // -----------------------------------------------------
        // Buttons
        // -----------------------------------------------------
        this.addRenderableWidget(Button.builder(Component.literal("Spawn as Transfurred"),
                        btn -> handleRespawnAsTransfur(true))
                .bounds(centerX - 75, centerY - 10, 150, 20)
                .build()
        );

        this.addRenderableWidget(Button.builder(Component.literal("Cancel"),
                        btn -> handleRespawnAsTransfur(false))
                .bounds(centerX - 75, centerY + 20, 150, 20)
                .build());
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);

        guiGraphics.blit(TEXTURE, (this.width - 176) / 2, (this.height - 166) / 2,
                0, 0, 176, 166);

        guiGraphics.drawCenteredString(this.font, "Choose Your Fate", this.width / 2, 20, 0xFFFFFF);

        // Render EditBox if player can choose
        if (canChooseVariant && typeBox != null)
            typeBox.render(guiGraphics, mouseX, mouseY, partialTicks);

        // Render suggestions using the helper
        if (suggestionHelper != null)
            suggestionHelper.render(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if (suggestionHelper != null && suggestionHelper.mouseClicked(mouseX, mouseY))
            return true;

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (suggestionHelper != null && suggestionHelper.keyPressed(keyCode, scanCode, modifiers))
            return true;

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // ============================================================
    // Respawn Logic
    // ============================================================
    public void handleRespawnAsTransfur(boolean accept) {
        if (minecraft == null) minecraft = Minecraft.getInstance();

        if (!accept) {
            minecraft.setScreen(previousDeathScreen);
            return;
        }

        Player player = minecraft.player;
        if (player == null) return;

        // -----------------------------------------------------
        // Case 1: The player is NOT allowed to select variants
        // -----------------------------------------------------
        if (!canChooseVariant || typeBox == null) {
            minecraft.setScreen(null);
            ChangedAddonMod.PACKET_HANDLER.sendToServer(new RespawnAsTransfurPacket((ResourceLocation) null));
            return;
        }

        // -----------------------------------------------------
        // Case 2: The player CAN choose
        // -----------------------------------------------------
        String chosen = typeBox.getValue();
        List<TransfurVariant<?>> variants = nameToVariants.get(chosen);

        if (variants == null || variants.isEmpty()) {
            // Player typed a formId manually
            try {
                possibleTransfurVariants = List.of(ResourceLocation.parse(chosen));
            } catch (Exception ignored) {
                return;
            }
        } else {
            // Player typed a public name -> convert to formId
            possibleTransfurVariants = variants.stream()
                    .map(TransfurVariant::getFormId)
                    .toList();
        }

        if (possibleTransfurVariants.isEmpty()) return;

        minecraft.setScreen(null);
        ChangedAddonMod.PACKET_HANDLER.sendToServer(new RespawnAsTransfurPacket(possibleTransfurVariants.get(0)));
    }
}
