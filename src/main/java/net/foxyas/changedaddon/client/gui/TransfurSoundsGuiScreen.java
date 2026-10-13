package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.network.packet.TransfurSoundsGuiButtonPacket;
import net.foxyas.changedaddon.variant.TransfurSoundsDetails;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.client.gui.AbstractRadialScreen;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.SingleRunnable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TransfurSoundsGuiScreen extends AbstractRadialScreen<AbstractContainerMenu> {

    private final Player player;
    private final List<Action> actions = new ArrayList<>();

    public TransfurSoundsGuiScreen(TransfurVariantInstance<?> variant) {
        super(
                new AbstractContainerMenu(MenuType.GENERIC_9x1, 0) {
                    @Override
                    public boolean stillValid(@NotNull Player player) {
                        return ProcessTransfur.isPlayerTransfurred(player) && playerHasAnyTransfurSoundType(player);
                    }

                    @Override
                    public ItemStack quickMoveStack(@NotNull Player player, int index) {
                        return ItemStack.EMPTY;
                    }
                },
                variant.getHost().getInventory(),
                Component.literal("TransfurSoundsGui"),
                getColors(variant).background(),
                getColors(variant).foreground(),
                variant.getHost()
        );
        this.player = variant.getHost();
        populateActions();
    }

    private void populateActions() {
        actions.clear();
        for (TransfurSoundsDetails.TransfurSoundType type : TransfurSoundsDetails.TransfurSoundType.values()) {
            if (!type.predicate.test(player)) continue;

            for (TransfurSoundsDetails.TransfurSoundAction action : type.actions) {
                actions.add(new Action(action.name(), getIconBasedOnTheSoundType(action), 16));
            }
        }
    }

    public static boolean playerHasAnyTransfurSoundType(Player player) {
        return Arrays.stream(TransfurSoundsDetails.TransfurSoundType.values())
                .anyMatch(type -> type.predicate.test(player));
    }

    /**
     * Retorna o ResourceLocation do ícone baseado na ação/tipo de som.
     */
    private static ResourceLocation getIconBasedOnTheSoundType(TransfurSoundsDetails.TransfurSoundAction action) {
        String path = switch (action) {
            case MEOW, PURREOW, PURR, HISS -> "emote/heart_gray"; // Exemplo de ícones reutilizados ou específicos
            case BARK, GROWL, HOWL, WHINE -> "emote/pause_gray";
            case YIP, CHATTER, FOX_SCREAM -> "emote/idea_gray";
            case DRAGON_ROAR, DRAGON_GROWL, ROAR -> "emote/deny_gray";
            default -> "emote/pause_gray";
        };
        // Altere "changed_addon" para o namespace correto dos seus ícones radiais se necessário
        return ChangedAddonMod.textureLoc("textures/gui/radial/icons/" + path);
    }

    protected ResourceLocation getTextureForSection(int section, boolean selected) {
        String style = ProcessTransfur.isPlayerNotLatex(player) ? "organic" : "goo";
        return Changed.modResource("textures/gui/radial/" + style + (selected ? "_selected/" : "/") + section + ".png");
    }

    protected int getSectionU(int section) {
        return (section % 4) * 80;
    }

    protected int getSectionV(int section) {
        return (section / 4) * 80;
    }

    @Override
    public int getCount() {
        return actions.size();
    }

    @Override
    public @Nullable List<Component> tooltipsFor(int section) {
        if (section >= 0 && section < actions.size()) {
            Action action = actions.get(section);
            return List.of(Component.translatable("gui.changed_addon.transfur_sounds_gui." + action.command().toLowerCase()));
        }
        return null;
    }

    @Override
    public void renderSectionBackground(GuiGraphics graphics, int section, double x, double y, float partialTicks, int mouseX, int mouseY, float red, float green, float blue) {
        boolean selected = this.getSectionAt(mouseX, mouseY).filter((index) -> index == section).isPresent();

        // Check if this specific action is on cooldown
        boolean onCooldown = false;
        if (section >= 0 && section < actions.size()) {
            try {
                var soundAction = TransfurSoundsDetails.TransfurSoundAction.valueOf(actions.get(section).command());
                onCooldown = isActionOnCooldown(soundAction);
            } catch (IllegalArgumentException ignored) {
            }
        }

        // Dim the color/alpha if on cooldown
        float alphaMultiplier = onCooldown ? 0.4F : 1.0F;
        graphics.setColor(red, green, blue, alphaMultiplier);

        graphics.blit(getTextureForSection(section, false), (int) x - 32 + this.leftPos, (int) y - 32 + this.topPos, 0.0F, 0.0F, 64, 64, 64, 64);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (!selected && !ChangedAddonVariables.ofOrDefault(player).isActInCooldown())
            return;

        if (section > this.actions.size() - 1) return;

        graphics.setColor(red, green, blue, alphaMultiplier);

        float maxCooldown = actions.stream()
                .mapToInt(action -> {
                    try {
                        return TransfurSoundsDetails.TransfurSoundAction.valueOf(action.command()).getCooldown();
                    } catch (IllegalArgumentException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(100); // Default fallback if actions list is empty

        // Calculate cooldown progress percentage (0.0 to 1.0)
        float actCooldownPercent = 1f - Mth.clamp((float) ChangedAddonVariables.ofOrDefault(player).actCooldown / maxCooldown, 0, 1);

        int dynamicHeight = (int) (64 * actCooldownPercent);
        int verticalOffset = 64 - dynamicHeight; // Shifts the render area to the bottom

        // Blit from bottom to top by modifying the source V offset and screen Y position
        graphics.blit(
                getTextureForSection(section, true),
                (int) x - 32 + this.leftPos,
                (int) y - 32 + this.topPos + verticalOffset, // Offset down so it fills upward
                0.0F,
                (float) verticalOffset,                      // Crop from the bottom of the source texture
                64,
                dynamicHeight,
                64,
                64
        );

        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }


    @Override
    public void renderSectionForeground(GuiGraphics graphics, int section, double x, double y, float partialTicks, int mouseX, int mouseY, float red, float green, float blue, float alpha) {
        if (section >= 0 && section < actions.size()) {
            Action action = actions.get(section);
            ResourceLocation icon = action.texture();
            int centerX = (int) (x - 2 + this.leftPos);
            int centerY = (int) (y - 2 + this.topPos);

            // Desenha o ícone dimensionado no centro do botão radial
            graphics.setColor(0.0F, 0.0F, 0.0F, 0.45F * alpha);
            blitScaledIcon(graphics, icon, centerX + 2, centerY + 2, 34, action.textureSize());
            graphics.setColor(red, green, blue, alpha);
            blitScaledIcon(graphics, icon, centerX, centerY, 32, action.textureSize());
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private static void blitScaledIcon(GuiGraphics graphics, ResourceLocation icon, int centerX, int centerY, int targetSize, int sourceSize) {
        float scale = (float) targetSize / (float) sourceSize;
        graphics.pose().pushPose();
        graphics.pose().translate((float) centerX - (float) targetSize * 0.5F, (float) centerY - (float) targetSize * 0.5F, 0.0F);
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.blit(icon, 0, 0, 0.0F, 0.0F, sourceSize, sourceSize, sourceSize, sourceSize);
        graphics.pose().popPose();
    }

    public boolean mouseClicked(double p_97748_, double p_97749_, int p_97750_) {
        Optional<Integer> section = this.getSectionAt((int) p_97748_, (int) p_97749_);
        if (section.isPresent()) {
            LocalPlayer localPlayer = (LocalPlayer) player;
            Objects.requireNonNull(localPlayer);
            SingleRunnable single = new SingleRunnable(() -> {
            });
            if (this.handleClicked(section.get(), single)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                single.run();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean handleClicked(int section, SingleRunnable close) {
        if (section >= 0 && section < actions.size()) {
            Action action = actions.get(section);

            try {
                TransfurSoundsDetails.TransfurSoundAction soundAction = TransfurSoundsDetails.TransfurSoundAction.valueOf(action.command());
                if (isActionOnCooldown(soundAction)) return false;

                sendSoundPacket(soundAction);
                return true;
            } catch (IllegalArgumentException ignored) {
            }
        }
        return false;
    }

    protected void sendSoundPacket(TransfurSoundsDetails.TransfurSoundAction action) {
        ChangedAddonMod.PACKET_HANDLER.sendToServer(
                new TransfurSoundsGuiButtonPacket(action.ordinal())
        );
    }

    protected boolean isActionOnCooldown(TransfurSoundsDetails.TransfurSoundAction action) {
        // You can check global action cooldown or specific logic if available
        return ChangedAddonVariables.ofOrDefault(player).isActInCooldown();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderLabels(graphics, mouseX, mouseY);
    }

    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.pose().pushPose();
        graphics.pose().translate(this.leftPos, this.topPos, 0);
        graphics.drawCenteredString(
                font,
                Component.translatable("gui.changed_addon.transfur_sounds_gui.label_transfur_sounds"),
                0,
                -120,
                0xFFFFFF
        );
        graphics.pose().popPose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public record Action(String command, ResourceLocation texture, int textureSize) {
    }
}