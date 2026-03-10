package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.event.TransfurEvents;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.network.packet.TransfurSoundsGuiButtonPacket;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.foxyas.changedaddon.variant.TransfurSoundsDetails;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TransfurSoundsGuiScreen extends Screen {

    /* ------------------------------------------------------------
     * Layout constants
     * ------------------------------------------------------------ */
    private static final int BUTTON_W = 30;
    private static final int BUTTON_H = 20;
    private static final int START_X = 4;
    private static final int START_Y = 6;
    private static final int GAP_Y = 22;
    private static final int imageWidth = 176;
    private static final int imageHeight = 150;
    /* ------------------------------------------------------------
     * Player
     * ------------------------------------------------------------ */
    private final Player player;
    protected int topPos;
    protected int leftPos;

    List<Button> buttons = new ArrayList<>();

    /* ------------------------------------------------------------
     * Constructor
     * ------------------------------------------------------------ */
    public TransfurSoundsGuiScreen() {
        super(Component.literal("TransfurSoundsGui"));
        this.player = Minecraft.getInstance().player;
    }

    public static MutableComponent joinWithSeparator(List<MutableComponent> components, String separator) {
        MutableComponent result = components.get(0);

        for (int i = 1; i < components.size(); i++) {
            result = result
                    .append(Component.literal(separator))
                    .append(components.get(i));
        }

        return result;
    }

    /* ------------------------------------------------------------
     * Init
     * ------------------------------------------------------------ */
    @Override
    public void init() {
        super.init();

        leftPos = (this.width - imageWidth) / 2;
        topPos = (this.height - imageHeight) / 2;

        int y = topPos + START_Y;

        buttons.clear();
        Button button;
        for (TransfurSoundsDetails.TransfurSoundType type : TransfurSoundsDetails.TransfurSoundType.values()) {
            if (!type.predicate.test(player)) continue;

            for (TransfurSoundsDetails.TransfurSoundAction action : type.actions) {
                button = createSoundButton(leftPos + START_X, y, action);

                buttons.add(button);
                addRenderableWidget(button);

                y += GAP_Y;
            }
        }
    }

    @Override
    public void tick() {
        boolean active = isNotOnCooldown();
        for (Button button : buttons) {
            button.active = active;
        }
    }

    /* ------------------------------------------------------------
     * Rendering
     * ------------------------------------------------------------ */
    @Override
    public void render(
            @NotNull GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTicks
    ) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);

        InventoryScreen.renderEntityInInventoryFollowsMouse(
                graphics,
                leftPos + 89,
                topPos + 133,
                30,
                (float) Math.atan((leftPos + 89 - mouseX) / 40.0),
                (float) Math.atan((topPos + 83 - mouseY) / 40.0),
                player
        );

        renderLabels(graphics, mouseX, mouseY);
    }

    protected void renderLabels(
            @NotNull GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {
        graphics.pose().pushPose();
        graphics.pose().translate(leftPos, topPos, 0);
        // -------------------------------
        // Title
        // -------------------------------
        graphics.drawCenteredString(
                font,
                Component.translatable("gui.changed_addon.transfur_sounds_gui.label_transfur_sounds"),
                imageWidth / 2,
                -24,
                0xFFFFFF
        );

        // -------------------------------
        // Subtitles (multi-line)
        // -------------------------------
        List<Component> subtitles = getPlayerSubtitle();

        int startY = -11;
        int lineHeight = font.lineHeight + 2;

        for (int i = 0; i < subtitles.size(); i++) {
            graphics.drawCenteredString(
                    font,
                    subtitles.get(i),
                    imageWidth / 2,
                    startY + (i * lineHeight),
                    0x3A3A3A
            );
        }
        graphics.pose().popPose();
    }

    /* ------------------------------------------------------------
     * Buttons
     * ------------------------------------------------------------ */
    protected Button createSoundButton(
            int x,
            int y,
            TransfurSoundsDetails.TransfurSoundAction action
    ) {
        return Button.builder(
                Component.translatable(
                        "gui.changed_addon.transfur_sounds_gui." +
                                action.name().toLowerCase()
                ),
                b -> {
                    if (isNotOnCooldown()) sendSoundPacket(action);
                }
        ).bounds(
                x, y,
                BUTTON_W, BUTTON_H
        ).build();
    }

    protected void sendSoundPacket(
            TransfurSoundsDetails.TransfurSoundAction action
    ) {
        ChangedAddonMod.PACKET_HANDLER.sendToServer(
                new TransfurSoundsGuiButtonPacket(action.ordinal())
        );
    }

    /* ------------------------------------------------------------
     * Titles & state
     * ------------------------------------------------------------ */
    protected List<Component> getPlayerSubtitle() {

        if (!ProcessTransfur.isPlayerTransfurred(player)) {
            return List.of(Component.literal("§7Not Transfurred"));
        }

        List<Component> subtitles = new ArrayList<>();

        // Prefixo base
        subtitles.add(Component.literal("§fYou are a"));

        // ===============================
        // Species / family
        // ===============================

        List<MutableComponent> species = new ArrayList<>();

        if (PlayerUtil.isCatTransfur(player)) {
            species.add(Component.literal("§fCat"));
        }

        if (PlayerUtil.isFoxTransfur(player)) {
            species.add(Component.literal("§fFox"));
        }

        if (PlayerUtil.isWolfTransfur(player)) {
            species.add(Component.literal("§fCanine"));
        }

        if (PlayerUtil.isDragonTransfur(player)) {
            species.add(Component.literal("§fDragon"));
        }

        if (PlayerUtil.isAquaticTransfur(player)) {
            species.add(Component.literal("§fFish"));
        }

        if (PlayerUtil.isSpiderTransfur(player)) {
            species.add(Component.literal("§fSpider"));
        }

        if (species.isEmpty()) {
            species.add(Component.literal("§7Unknown"));
        }

        subtitles.add(joinWithSeparator(species, "§7 / "));

        // ===============================
        // Special traits
        // ===============================

        if (isApexPredator()) {
            subtitles.add(Component.literal("§6Apex Predator"));
        }

        return subtitles;
    }

    protected boolean isNotOnCooldown() {
        return !ChangedAddonVariables.ofOrDefault(player).actCooldown;
    }

    protected boolean isApexPredator() {
        if (!ProcessTransfur.isPlayerTransfurred(player))
            return false;

        ResourceLocation id =
                ProcessTransfur.getPlayerTransfurVariant(player).getFormId();

        if (id == null)
            return false;

        String path = id.toString();

        return path.contains("lion")
                || path.contains("tiger")
                || path.startsWith("changed_addon:form_experiment009") || TransfurEvents.resolveChangedEntity(player).getType().is(ChangedAddonTags.EntityTypes.CAN_ROAR);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}