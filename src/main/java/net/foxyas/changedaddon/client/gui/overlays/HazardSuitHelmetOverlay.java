package net.foxyas.changedaddon.client.gui.overlays;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.item.armor.HazardBodySuit;
import net.foxyas.changedaddon.process.sounds.HelmetBreathingSound;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.init.ChangedAccessorySlots;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class HazardSuitHelmetOverlay {

    public static final ResourceLocation OVERLAY_TEXTURE = ResourceLocation.parse("changed_addon:textures/screens/overlays/hazard_helmet_overlay.png");
    private static HelmetBreathingSound breathingSound;


    public static boolean shouldApplyOverlay(Player entity) {
        Minecraft minecraft = Minecraft.getInstance();
        if (entity == null) {
            return false;
        }

        if (!minecraft.options.getCameraType().isFirstPerson()) return false;
        LocalPlayer player = minecraft.player;
        assert player != null;
        if (ProcessTransfur.isPlayerTransfurred(player) && !ProcessTransfur.getPlayerTransfurVariant(player).is(ChangedTransfurVariants.LATEX_HUMAN.get()))
            return false;

        if (AccessorySlots.getForEntity(player).isPresent()) {
            AccessorySlots accessorySlots = AccessorySlots.getForEntity(player).get();
            Optional<ItemStack> item = accessorySlots.getItem(ChangedAccessorySlots.FULL_BODY.get());
            if (item.isPresent()) {
                ItemStack stack = item.get();
                if (stack.getItem() instanceof HazardBodySuit hazardBodySuit) {
                    return hazardBodySuit.getClothingState(stack).getValue(HazardBodySuit.HELMET);
                }
            }
        }


        return false;
    }

    public static boolean shouldPlayOverlaySound(Player entity) {
        Minecraft minecraft = Minecraft.getInstance();
        if (entity == null) {
            return false;
        }

        if (!minecraft.options.getCameraType().isFirstPerson()) return false;
        LocalPlayer player = minecraft.player;
        assert player != null;
        if (ProcessTransfur.isPlayerTransfurred(player)) return false;

        if (AccessorySlots.getForEntity(player).isPresent()) {
            AccessorySlots accessorySlots = AccessorySlots.getForEntity(player).get();
            Optional<ItemStack> item = accessorySlots.getItem(ChangedAccessorySlots.FULL_BODY.get());
            if (item.isPresent()) {
                ItemStack stack = item.get();
                if (stack.getItem() instanceof HazardBodySuit hazardBodySuit) {
                    return hazardBodySuit.getClothingState(stack).getValue(HazardBodySuit.HELMET);
                }
            }
        }


        return false;
    }

    public static void renderHelmetOverlay(ForgeGui forgeIngameGui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        boolean overlay = shouldApplyOverlay(player);

        if (overlay) {

            // --- Set the data of the RenderSystem
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            // --- I don't know if that is necessary but is always good to set an alpha friendly blend function
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            // --- Color and texture stuff
            guiGraphics.setColor(1, 1, 1, 1);

            // --- Render overlay
            guiGraphics.blit(OVERLAY_TEXTURE, 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);

            // --- Reset the data of the RenderSystem
            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            guiGraphics.setColor(1, 1, 1, 1);

            // --- Breath Sound
            if ((breathingSound == null || breathingSound.isStopped())) {
                breathingSound = new HelmetBreathingSound(SoundEvents.PLAYER_BREATH, player);
                Minecraft.getInstance().getSoundManager().play(breathingSound);
            }
        } else {
            if (breathingSound != null) {
                breathingSound.forceStop();
            }
        }
    }
}
