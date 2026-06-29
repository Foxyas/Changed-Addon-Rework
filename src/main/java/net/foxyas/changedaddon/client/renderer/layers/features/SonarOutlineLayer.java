package net.foxyas.changedaddon.client.renderer.layers.features;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

/**
 * Outline layer with customizable behavior per-entity and fade-in/out logic.
 */
public class SonarOutlineLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final RenderLayerParent<T, M> parent;

    public SonarOutlineLayer(RenderLayerParent<T, M> parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                       @NotNull T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        if (SonarClientState.ticksToRenderEntities <= 0) return;

        Minecraft minecraft = Minecraft.getInstance();
        Entity camera = minecraft.cameraEntity;
        if (camera == null) return;

        if (SonarClientState.getRenderMode() == RenderMode.KINETIC_SIGHT) {
            if (livingEntity.isSilent() || livingEntity.getDeltaMovement().length() <= 0.01f || livingEntity.isShiftKeyDown())
                return;
        }

        if (camera.distanceToSqr(livingEntity) > SonarOutlineLayer.SonarClientState.getMaxDistSqr()) {
            if (!FoxyasUtil.canEntitySeeOther(camera, livingEntity)) {
                return;
            }
        }

        Player player = minecraft.player;
        if (player == null) return;
        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (instance == null) return;
        if (!instance.hasAbility(ChangedAddonAbilities.SONAR.get())) return;

        float alpha = SonarClientState.getAlpha(partialTicks);
        if (alpha <= 0.01f) return;

        // If the entity itself provides custom sonar rendering
        if (livingEntity instanceof CustomSonarRenderable custom) {
            if (custom.handleSonarRender(this, poseStack, buffer, packedLight,
                    limbSwing, limbSwingAmount, partialTicks,
                    ageInTicks, netHeadYaw, headPitch, alpha)) {
                return; // custom handled → skip default
            }
        }

        // If the transformed entity (variant) provides custom sonar rendering
        if (instance.getChangedEntity() instanceof CustomSonarRenderable custom) {
            if (custom.handleSonarRenderForCamera(this, livingEntity, poseStack, buffer, packedLight,
                    limbSwing, limbSwingAmount, partialTicks,
                    ageInTicks, netHeadYaw, headPitch, alpha)) {
                return; // custom handled → skip default
            }
        }

        // Default colors: white (Sonar) or yellowish (Echo)
        float r = 1.0f, g = 1.0f, b = 1.0f;
        if (SonarClientState.renderMode == RenderMode.ECHO_LOCATION) {
            r = 1.0f;
            g = 1.0f;
            b = 0.2f;
        }

        RenderType outline = ChangedAddonRenderTypes.outlineWithTranslucencyCull(getTextureLocation(livingEntity));
        getParentModel().renderToBuffer(
                poseStack,
                buffer.getBuffer(outline),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                r, g, b, alpha
        );
    }

    public interface CustomSonarRenderable {
        /**
         * Called when rendering with sonar outline.
         * Implementors can override rendering (pose, color, etc.).
         * Return true if you fully handled rendering yourself (skips default).
         */
        boolean handleSonarRender(@NotNull SonarOutlineLayer<?, ?> sonarOutlineLayer, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                                  float limbSwing, float limbSwingAmount, float partialTicks,
                                  float ageInTicks, float netHeadYaw, float headPitch, float alpha);

        /**
         * Same as handleSonarRender but specifically for the camera entity.
         * Return true if you fully handled rendering yourself (skips default).
         */
        boolean handleSonarRenderForCamera(@NotNull SonarOutlineLayer<?, ?> sonarOutlineLayer, @NotNull LivingEntity livingEntity, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                                           float limbSwing, float limbSwingAmount, float partialTicks,
                                           float ageInTicks, float netHeadYaw, float headPitch, float alpha);
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT)
    public static class ClientHandle {

        @SubscribeEvent
        public static void renderSonarFailSafe(RenderLevelStageEvent event) { // Fail-safe for custom culling mods
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;
            if (!SonarOutlineLayer.SonarClientState.isActive()) return;

            Minecraft mc = Minecraft.getInstance();
            MultiBufferSource buffer = mc.renderBuffers().bufferSource();
            PoseStack pose = event.getPoseStack();

            Entity camera = mc.cameraEntity;
            if (!(camera instanceof LivingEntity)) return;

            if (mc.level == null) return;

            for (Entity e : mc.level.entitiesForRendering()) {
                if (!(e instanceof LivingEntity living)) continue;

                if (SonarClientState.getRenderMode() == RenderMode.KINETIC_SIGHT) {
                    if (living.isSilent() || living.getDeltaMovement().length() <= 0.01f || living.isShiftKeyDown())
                        continue;
                }

                if (camera.distanceToSqr(living) > SonarOutlineLayer.SonarClientState.getMaxDistSqr()) {
                    if (!FoxyasUtil.canEntitySeeOther(camera, living)) {
                        continue;
                    }
                }


                EntityRenderer<? super Entity> renderer =
                        mc.getEntityRenderDispatcher().getRenderer(living);
                if (!(renderer instanceof LivingEntityRenderer<? super LivingEntity, ?> livingEntityRenderer)) return;

                boolean canNormallyRender =
                        livingEntityRenderer.shouldRender(
                                living,
                                event.getFrustum(),
                                living.getX(), living.getY(), living.getZ()
                        );

                if (canNormallyRender) {
                    continue;
                }


                pose.pushPose();

                Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
                pose.translate(
                        living.getX() - camPos.x,
                        living.getY() - camPos.y,
                        living.getZ() - camPos.z
                );

                float partialTick = event.getPartialTick();
                float alpha = SonarOutlineLayer.SonarClientState.getAlpha(event.getPartialTick());
                if (alpha <= 0.01f) {
                    pose.popPose();
                    continue;
                }

                RenderType outline = ChangedAddonRenderTypes
                        .outlineWithTranslucencyCull(livingEntityRenderer.getTextureLocation(living));

                EntityModel<? super LivingEntity> model = livingEntityRenderer.getModel();
                // Moviment
                float limbSwing = living.walkAnimation.position(partialTick);
                float limbSwingAmount = living.walkAnimation.speed(partialTick);

                // Time
                float ageInTicks = living.tickCount + partialTick;

                // Head
                float netHeadYaw = Mth.rotLerp(
                        partialTick,
                        living.yHeadRotO,
                        living.yHeadRot
                ) - Mth.rotLerp(
                        partialTick,
                        living.yBodyRotO,
                        living.yBodyRot
                );

                float headPitch = Mth.lerp(
                        partialTick,
                        living.xRotO,
                        living.getXRot()
                );

                model.prepareMobModel(
                        living,
                        limbSwing,
                        limbSwingAmount,
                        partialTick
                );

                model.setupAnim(
                        living,
                        limbSwing,
                        limbSwingAmount,
                        ageInTicks,
                        netHeadYaw,
                        headPitch
                );

                model.renderToBuffer(
                        pose,
                        buffer.getBuffer(outline),
                        0xF000F0,
                        OverlayTexture.NO_OVERLAY,
                        1f, 1f, 1f, alpha
                );

                pose.popPose();
            }
        }
    }

    // estado global do cliente
    public static class SonarClientState {

        private static int ticksToRenderEntities = 0;
        private static int fadeInDuration = 10;   // duração do fade in
        private static int fadeOutDuration = 10;  // duração do fade out
        private static float maxDistSqr;
        private static RenderMode renderMode = RenderMode.SONAR;
        private static int lastTicks = 0;

        public static void setTicksToRenderEntities(int ticks, int iLastTicks, int fadeIn, int fadeOut, float maxDist, RenderMode mode) {
            ticksToRenderEntities = ticks;
            fadeInDuration = fadeIn;
            fadeOutDuration = fadeOut;
            maxDistSqr = maxDist * maxDist;
            renderMode = mode;
            lastTicks = iLastTicks;
        }

        public static void tick() {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            if (player == null) return;
            TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance == null) {
                ticksToRenderEntities = 0;
                return;
            } else if (!instance.hasAbility(ChangedAddonAbilities.SONAR.get())) {
                ticksToRenderEntities = 0;
                return;
            }


            if (ticksToRenderEntities > 0) ticksToRenderEntities--;
        }

        /**
         * Calcula o alpha (0f-1f) baseado no tempo restante.
         */
        public static float getAlpha(float partialTicks) {
            if (lastTicks <= 0) return 0f;
            int ticks = ticksToRenderEntities;

            // --- FADE IN ---
            if (ticks >= lastTicks - fadeInDuration) {
                float elapsed = (lastTicks - ticks) + partialTicks;
                return Mth.clamp(elapsed / fadeInDuration, 0f, 1f);
            }

            // --- FADE OUT ---
            if (ticks <= fadeOutDuration) {
                float remaining = (ticks - partialTicks);
                return Mth.clamp(remaining / fadeOutDuration, 0f, 1f);
            }

            // --- VISIBILIDADE TOTAL ---
            return 1f;
        }

        public static boolean isActive() {
            return ticksToRenderEntities > 0;
        }

        public static RenderMode getRenderMode() {
            return renderMode;
        }

        public static float getMaxDistSqr() {
            return maxDistSqr;
        }

        public static void resetData() {
            ticksToRenderEntities = 0;
            fadeInDuration = 0;
            fadeOutDuration = 0;
            maxDistSqr = 0;
            renderMode = RenderMode.SONAR;
            lastTicks = 0;
        }
    }

}
