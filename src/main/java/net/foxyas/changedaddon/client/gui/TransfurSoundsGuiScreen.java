package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.event.TransfurEvents;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.network.packet.TransfurSoundsGuiButtonPacket;
import net.foxyas.changedaddon.util.PlayerUtil;
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
import net.minecraft.network.chat.MutableComponent;
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
            case MEOW, PURREOW, PURR, HISS -> "emote/heart_gray";
            case BARK, GROWL, HOWL, WHINE -> "emote/pause_gray";
            case YIP, CHATTER, FOX_SCREAM -> "emote/idea_gray";
            case DRAGON_ROAR, DRAGON_GROWL, ROAR -> "emote/deny_gray";
            default -> "emote/pause_gray";
        };
        return ChangedAddonMod.textureLoc("textures/gui/radial/icons/" + path);
    }

    protected ResourceLocation getTextureForSection(int section, boolean selected) {
        String style = ProcessTransfur.isPlayerNotLatex(player) ? "organic" : "goo";
        return Changed.modResource("textures/gui/radial/" + style + (selected ? "_selected/" : "/") + section + ".png");
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

        boolean onCooldown = false;
        if (section >= 0 && section < actions.size()) {
            try {
                var soundAction = TransfurSoundsDetails.TransfurSoundAction.valueOf(actions.get(section).command());
                onCooldown = isActionOnCooldown(soundAction);
            } catch (IllegalArgumentException ignored) {}
        }

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
                .orElse(100);

        float actCooldownPercent = 1f - Mth.clamp((float) ChangedAddonVariables.ofOrDefault(player).actCooldown / maxCooldown, 0, 1);

        int dynamicHeight = (int) (64 * actCooldownPercent);
        int verticalOffset = 64 - dynamicHeight;

        graphics.blit(
                getTextureForSection(section, true),
                (int) x - 32 + this.leftPos,
                (int) y - 32 + this.topPos + verticalOffset,
                0.0F,
                (float) verticalOffset,
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
            SingleRunnable single = new SingleRunnable(() -> {});
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
            } catch (IllegalArgumentException ignored) {}
        }
        return false;
    }

    protected void sendSoundPacket(TransfurSoundsDetails.TransfurSoundAction action) {
        ChangedAddonMod.PACKET_HANDLER.sendToServer(
                new TransfurSoundsGuiButtonPacket(action.ordinal())
        );
    }

    protected boolean isActionOnCooldown(TransfurSoundsDetails.TransfurSoundAction action) {
        return ChangedAddonVariables.ofOrDefault(player).isActInCooldown();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderLabels(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.pose().pushPose();
        graphics.pose().translate(this.leftPos, this.topPos, 0);

        // -------------------------------
        // Title at top center
        // -------------------------------
        graphics.drawCenteredString(
                font,
                Component.translatable("gui.changed_addon.transfur_sounds_gui.label_transfur_sounds"),
                0,
                -120,
                0xFFFFFF
        );

        graphics.pose().popPose();

        // -------------------------------
        // Adaptive Subtitles Info Box
        // -------------------------------
        List<Component> subtitles = getPlayerSubtitle();
        if (!subtitles.isEmpty()) {
            int panelWidth = 130;
            int lineHeight = font.lineHeight + 4;
            int panelHeight = subtitles.size() * lineHeight + 12;

            // 1. Calculate the ideal position (to the left of the radial menu)
            int idealX = this.leftPos - panelWidth - 120; // 120px gap from radial center
            int idealY = this.topPos - (panelHeight / 2) - 48; // Vertically centered with radial

            int margin = 12; // Minimum distance allowed from any screen edge

            // 2. Check if the ideal position overflows the left or top boundary of the screen
            int finalX = idealX;
            int finalY = idealY;

            if (idealX < margin) {
                // Screen is too narrow on the left!
                // Fallback: If there's space above/below, keep it readable at the top margin or left margin
                finalX = Math.max(margin, (this.width - panelWidth) / 2); // Center horizontally
                finalY = margin; // Anchor near the top
            } else {
                // Keep inside screen bounds for Y axis as well
                finalY = Mth.clamp(idealY, margin, Math.max(margin, this.height - panelHeight - margin));
            }

            // Semi-transparent background panel & outline
            graphics.fill(finalX, finalY, finalX + panelWidth, finalY + panelHeight, 0x80000000);
            graphics.renderOutline(finalX, finalY, panelWidth, panelHeight, 0x55FFFFFF);

            int textY = finalY + 6;
            for (Component subtitle : subtitles) {
                graphics.drawString(font, subtitle, finalX + 8, textY, 0xFFFFFF, true);
                textY += lineHeight;
            }
        }
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

    protected List<Component> getPlayerSubtitle() {
        if (!ProcessTransfur.isPlayerTransfurred(player)) {
            return List.of(Component.literal("§7Not Transfurred"));
        }

        List<Component> subtitles = new ArrayList<>();
        subtitles.add(Component.literal("§fYou are a"));

        List<MutableComponent> species = new ArrayList<>();
        if (PlayerUtil.isCatTransfur(player)) species.add(Component.literal("§fCat"));
        if (PlayerUtil.isFoxTransfur(player)) species.add(Component.literal("§fFox"));
        if (PlayerUtil.isWolfTransfur(player)) species.add(Component.literal("§fCanine"));
        if (PlayerUtil.isDragonTransfur(player)) species.add(Component.literal("§fDragon"));
        if (PlayerUtil.isAquaticTransfur(player)) species.add(Component.literal("§fFish"));
        if (PlayerUtil.isSpiderTransfur(player)) species.add(Component.literal("§fSpider"));

        if (species.isEmpty()) {
            species.add(Component.literal("§7Unknown"));
        }

        subtitles.add(joinWithSeparator(species, "§7 / "));

        if (isApexPredator()) {
            subtitles.add(Component.literal("§6Apex Predator"));
        }

        return subtitles;
    }

    protected boolean isApexPredator() {
        if (!ProcessTransfur.isPlayerTransfurred(player))
            return false;

        ResourceLocation id = ProcessTransfur.getPlayerTransfurVariant(player).getFormId();
        if (id == null) return false;

        String path = id.toString();
        return path.contains("lion")
                || path.contains("tiger")
                || path.startsWith("changed_addon:form_experiment009")
                || TransfurEvents.resolveChangedEntity(player).getType().is(ChangedAddonTags.EntityTypes.CAN_ROAR);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public record Action(String command, ResourceLocation texture, int textureSize) {}
}