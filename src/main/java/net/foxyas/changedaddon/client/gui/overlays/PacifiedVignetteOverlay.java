package net.foxyas.changedaddon.client.gui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.LuminaraBloomFlowerBlock;
import net.foxyas.changedaddon.entity.advanced.LuminaraFlowerBeastEntity;
import net.foxyas.changedaddon.entity.api.ChangedEntityExtension;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.util.ChangedEntityUtil;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.Color;
import java.util.List;


@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PacifiedVignetteOverlay {

    public static class FadeAnimationUtils {

        /**
         * Calculates the dynamic fade speed based on the dot product precision.
         *
         * @param dotProduct   The dot product of the direction vectors (typically ranges from -1.0 to 1.0).
         * @param defaultSpeed The baseline speed used when the dot product is zero or negative.
         * @param maxSpeed     The maximum speed used when looking directly at the target (dot = 1.0).
         * @return The calculated dynamic speed.
         */
        public static float calculateDynamicFadeSpeed(double dotProduct, float defaultSpeed, float maxSpeed) {
            // If the dot product is zero or negative, fallback to the default speed
            if (dotProduct <= 0.0) {
                return defaultSpeed;
            }

            // Scale the speed proportionally based on how precise the view angle is.
            // As the dot product approaches 1.0 (looking straight at it), the speed approaches maxSpeed.
            // If looking slightly off-center (lower positive dot), the speed scales down smoothly.
            return defaultSpeed + (float) ((maxSpeed - defaultSpeed) * dotProduct);
        }

        /**
         * Updates the current fade progress value over time based on the view precision.
         *
         * @param currentFade  The current fade progress (clamped between 0.0f and 1.0f).
         * @param dotProduct   The dot product between the look vector and target vector.
         * @param deltaTime    The time step (delta time or partial ticks).
         * @param defaultSpeed The default speed for fallback or decreasing the effect.
         * @param maxSpeed     The maximum speed when looking directly.
         * @return The updated and clamped fade progress.
         */
        public static float updateFadeProgress(float currentFade, double dotProduct, float deltaTime, float defaultSpeed, float maxSpeed) {
            // Retrieve the dynamic speed based on current dot product precision
            float dynamicSpeed = calculateDynamicFadeSpeed(dotProduct, defaultSpeed, maxSpeed);

            if (dotProduct > 0.0) {
                // If facing the target, advance the fade progress forward using the dynamic speed
                currentFade += dynamicSpeed * deltaTime;
            } else {
                // If the dot is negative or zero, decrease/revert the effect using the default speed
                currentFade -= defaultSpeed * deltaTime;
            }

            // Ensure the final value stays strictly within the 0.0f to 1.0f range
            return Math.max(0.0f, Math.min(1.0f, currentFade));
        }
    }

    private static final ResourceLocation VIGNETTE_LOCATION = ChangedAddonMod.textureLoc("textures/misc/alpha_vignette");
    private static final Color BORDER_COLOR = new Color(243, 146, 255);
    public static final float FLOWER_DETECTION_DISTANCE = 32.0f;

    // Fade variables
    private static float vignetteFadeProgress = 0.0f;
    private static final float DEFAULT_FADE_SPEED = 0.005f; // Baseline speed when looking away or out of focus
    private static final float MAX_FADE_SPEED = 0.025f;     // Maximum speed when looking directly at the target
    private static final float MAX_ALPHA = 0.8f;            // Target max alpha

    @SubscribeEvent
    public static void updateAlphaValue(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.END) {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;

            if (player == null) {
                return;
            }

            if (minecraft.isPaused()) return;

            // Verify base player conditions first

            boolean hasValidConditions = ProcessTransfur.getPlayerTransfurVariantSafe(player).map(variant -> variant.getHost().hasEffect(ChangedAddonMobEffects.PACIFIED.get()) && ChangedEntityUtil.isAffectedByPacifiedEffect(variant.getChangedEntity())).orElse(false);
//                    ProcessTransfur.isPlayerTransfurred(player) && player.hasEffect(ChangedAddonMobEffects.PACIFIED.get());

            double targetDot = -1.0;
            if (hasValidConditions) {
                targetDot = getLuminaraTargetDot(player);
            }

            // Update fade progress dynamically based on view dot product precision
            vignetteFadeProgress = FadeAnimationUtils.updateFadeProgress(
                    vignetteFadeProgress,
                    targetDot,
                    1.0f,
                    DEFAULT_FADE_SPEED,
                    MAX_FADE_SPEED
            );
        }
    }

    private static double getLuminaraTargetDot(Player player) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getViewVector(1.0F).normalize();

        double maxDistance = FLOWER_DETECTION_DISTANCE;
        double maxDistanceSqr = maxDistance * maxDistance;
        double bestDot = -1.0;

        // Minimum dot product threshold to consider something "in front" of the player
        // (e.g., 0.0 means 90 degrees to each side, 0.5 is 60 degrees, 0.707 is 45 degrees)
        double minDotThreshold = Math.cos(Math.toRadians(90 / 2.0));

        BlockPos playerPos = player.blockPosition();

        // 1. Check blocks in an area (Sphere around the player)
        int radius = (int) Math.ceil(maxDistance);
        for (BlockPos pos : FoxyasUtil.getBlockPositionsInSphere(playerPos, radius).toList()) {
            double distSqr = eyePos.distanceToSqr(Vec3.atCenterOf(pos));
            if (distSqr <= maxDistanceSqr) {
                BlockState state = player.level().getBlockState(pos);
                if (state.getBlock() instanceof LuminaraBloomFlowerBlock) {
                    Vec3 targetCenter = Vec3.atCenterOf(pos);
                    Vec3 toTarget = targetCenter.subtract(eyePos).normalize();
                    double dot = lookVec.dot(toTarget);

                    if (dot >= minDotThreshold && dot > bestDot) {
                        bestDot = dot;
                    }
                }
            }
        }

        // 2. Check entities in an area (BoundingBox search around the player)
        AABB searchBox = player.getBoundingBox().inflate(maxDistance);
        List<Entity> entities = player.level().getEntities(player, searchBox, entity ->
                !entity.isSpectator() && entity.isPickable() && entity instanceof LuminaraFlowerBeastEntity
        );

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity living)) {
                continue;
            }
            Vec3 targetPos = living.getPosition(Minecraft.getInstance().getPartialTick()).add(0, living.getBbHeight() / 2.0, 0);
            double distSqr = eyePos.distanceToSqr(targetPos);

            if (distSqr <= maxDistanceSqr) {
                Vec3 toTarget = targetPos.subtract(eyePos).normalize();
                double dot = lookVec.dot(toTarget);

                if (dot >= minDotThreshold && dot > bestDot) {
//                    if (FoxyasUtil.canEntitySeeOther(player, living, 90f)) {
                        bestDot = dot;
//                    }
                }
            }
        }

        return bestDot;
    }

    public static void renderPacifiedVignetteOverlay(ForgeGui forgeGui, GuiGraphics graphics, float partialTick, int width, int height) {

        // If progress is 0, we don't need to render anything
        if (vignetteFadeProgress <= 0.0f) {
            return;
        }

        forgeGui.setupOverlayRenderState(true, false);

        float r = BORDER_COLOR.getRed() / 255.0F;
        float g = BORDER_COLOR.getGreen() / 255.0F;
        float b = BORDER_COLOR.getBlue() / 255.0F;

        // Apply fade: Multiply the desired max alpha by the current fade progress
        float dynamicAlpha = Mth.lerp(partialTick, vignetteFadeProgress - DEFAULT_FADE_SPEED, vignetteFadeProgress) * MAX_ALPHA;
        dynamicAlpha = Mth.clamp(dynamicAlpha, 0.0f, MAX_ALPHA);

        graphics.setColor(r, g, b, dynamicAlpha);

        graphics.blit(VIGNETTE_LOCATION, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);

        // Reset state
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }
}