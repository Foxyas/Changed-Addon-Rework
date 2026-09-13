package net.foxyas.changedaddon.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.model.api.IPublicRootModel;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.mixins.client.renderer.LivingEntityRendererAccessor;
import net.ltxprogrammer.changed.client.ModelPartStem;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityModelFadeParticle extends Particle {

    private final Entity entity;
    private final int color;
    private final int targetSnapshots;
    private final List<ModelSnapshot> snapshots = new ArrayList<>();

    private int snapshotInterval = 1; // Capture frequency (in ticks)
    private int ticksSinceLastSnapshot = 0;

    public EntityModelFadeParticle(
            ClientLevel level,
            double x, double y, double z,
            Entity entity,
            @NotNull EntityModelFadeParticleOptions options) {
        super(level, x, y, z);
        this.entity = entity;
        this.color = options.color();
        this.lifetime = (int) (20 * options.duration());
        this.targetSnapshots = Math.max(1, options.modelSnapshots());
        this.gravity = 0f;
    }

    public static Provider provider() {
        return new Provider();
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 1.0f - ((float) this.age / (float) this.lifetime);

        if (this.entity instanceof LivingEntity livingEntity && snapshots.size() < targetSnapshots) {
            ticksSinceLastSnapshot++;
            if (ticksSinceLastSnapshot >= snapshotInterval) {
                ticksSinceLastSnapshot = 0;
                captureSnapshot(livingEntity);
            }
        }
    }

    private void captureSnapshot(LivingEntity livingEntity) {
        LivingEntity targetEntity = EntityUtil.maybeGetOverlaying(livingEntity);
        if (targetEntity == null) targetEntity = livingEntity;

        if (targetEntity instanceof ChangedEntity changedEntity) {
            captureTransfurSnapshot(changedEntity);
        } else {
            captureHumanoidSnapshot(targetEntity);
        }
    }

    private void captureTransfurSnapshot(ChangedEntity changedEntity) {
        EntityRenderer<? super ChangedEntity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(changedEntity);
        if (!(renderer instanceof AdvancedHumanoidRenderer<? super ChangedEntity, ?> advancedHumanoidRenderer)) return;

        AdvancedHumanoidModel<? super ChangedEntity> model = advancedHumanoidRenderer.getModel();
        if (!(model instanceof IPublicRootModel publicRoot)) return;
        if (publicRoot.getModelRoot() == null) return;

        List<ModelPart> modelParts = new ArrayList<>(model.getRootLevelLimbs().toList());
        for (ModelPartStem stem : model.getAllParts().toList()) {
            modelParts.addAll(stem.stem);
        }

        float partialTicks = 1.0f;
        float limbSwing = changedEntity.walkAnimation.position();
        float limbSwingAmount = changedEntity.walkAnimation.speed();
        float ageInTicks = changedEntity.tickCount;
        float netHeadYaw = Mth.lerp(partialTicks, changedEntity.yHeadRotO, changedEntity.yHeadRot) -
                Mth.lerp(partialTicks, changedEntity.yBodyRotO, changedEntity.yBodyRot);
        float headPitch = changedEntity.getXRot();

        model.prepareMobModel(changedEntity, limbSwing, limbSwingAmount, partialTicks);
        model.setupAnim(changedEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        ModelSnapshot snapshot = new ModelSnapshot(
                changedEntity.position(),
                changedEntity.yBodyRot,
                changedEntity.tickCount,
                limbSwing,
                limbSwingAmount,
                netHeadYaw,
                headPitch,
                1.0f - ((float) snapshots.size() / (float) targetSnapshots)
        );

        for (ModelPart part : modelParts) {
            snapshot.poses.put(part, part.storePose());
        }

        snapshots.add(snapshot);
    }

    private void captureHumanoidSnapshot(LivingEntity livingEntity) {
        EntityRenderer<? super LivingEntity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);
        if (!(renderer instanceof LivingEntityRenderer<? super LivingEntity, ?> livingRenderer)) return;

        EntityModel<? super LivingEntity> model = livingRenderer.getModel();
        if (!(model instanceof IPublicRootModel publicRoot)) return;
        ModelPart root = publicRoot.getModelRoot();
        if (root == null) return;

        List<ModelPart> modelParts = root.getAllParts().toList();

        float partialTicks = 1.0f;
        float limbSwing = livingEntity.walkAnimation.position();
        float limbSwingAmount = livingEntity.walkAnimation.speed();
        float ageInTicks = livingEntity.tickCount;
        float netHeadYaw = Mth.lerp(partialTicks, livingEntity.yHeadRotO, livingEntity.yHeadRot) -
                Mth.lerp(partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot);
        float headPitch = livingEntity.getXRot();

        model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
        model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        ModelSnapshot snapshot = new ModelSnapshot(
                livingEntity.position(),
                livingEntity.yBodyRot,
                livingEntity.tickCount,
                limbSwing,
                limbSwingAmount,
                netHeadYaw,
                headPitch,
                1.0f - ((float) snapshots.size() / (float) targetSnapshots)
        );

        for (ModelPart part : modelParts) {
            snapshot.poses.put(part, part.storePose());
        }

        snapshots.add(snapshot);
    }

    @Override
    public void render(@NotNull VertexConsumer consumer, @NotNull Camera camera, float partialTick) {
        if (snapshots.isEmpty()) return;
        if (entity == Minecraft.getInstance().player
                && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON
                && entity.distanceToSqr(x, y, z) < 3) return;

        Color fadeColor = new Color(color);
        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        Vec3 camPos = camera.getPosition();

        if (!(entity instanceof LivingEntity livingEntity)) return;

        LivingEntity frozenEntity = EntityUtil.maybeGetOverlaying(livingEntity);
        if (frozenEntity == null) frozenEntity = livingEntity;

        for (ModelSnapshot snapshot : snapshots) {
            PoseStack poseStack = new PoseStack();
            poseStack.translate(
                    snapshot.position.x - camPos.x,
                    snapshot.position.y - camPos.y,
                    snapshot.position.z - camPos.z
            );

            if (frozenEntity instanceof ChangedEntity changedEntity) {
                renderTransfurSnapshot(snapshot, partialTick, changedEntity, bufferSource, poseStack, fadeColor);
            } else {
                renderHumanoidSnapshot(snapshot, partialTick, frozenEntity, bufferSource, poseStack, fadeColor);
            }
        }

        bufferSource.endBatch();
    }

    protected void renderTransfurSnapshot(ModelSnapshot snapshot, float partialTicks, ChangedEntity changedEntity, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, Color fadeColor) {
        EntityRenderer<? super ChangedEntity> rendererNormal = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(changedEntity);
        if (!(rendererNormal instanceof AdvancedHumanoidRenderer<? super ChangedEntity, ?> advancedHumanoidRenderer)) return;

        AdvancedHumanoidModel<? super ChangedEntity> model = advancedHumanoidRenderer.getModel();
        ResourceLocation texture = advancedHumanoidRenderer.getTextureLocation(changedEntity);

        if (!(model instanceof IPublicRootModel publicRoot)) return;
        if (publicRoot.getModelRoot() == null) return;

        List<ModelPart> modelParts = new ArrayList<>(model.getRootLevelLimbs().toList());
        for (ModelPartStem allPart : model.getAllParts().toList()) {
            modelParts.addAll(allPart.stem);
        }

        if (changedEntity.hasPose(Pose.SLEEPING)) {
            Direction direction = changedEntity.getBedOrientation();
            if (direction != null) {
                float f4 = changedEntity.getEyeHeight(Pose.STANDING) - 0.1F;
                poseStack.translate((float) (-direction.getStepX()) * f4, 0.0F, (float) (-direction.getStepZ()) * f4);
            }
        }

        if (advancedHumanoidRenderer instanceof LivingEntityRendererAccessor rendererAccessor) {
            rendererAccessor.callSetupRotations(changedEntity, poseStack, snapshot.frozenAgeInTicks, snapshot.bodyYaw, partialTicks);
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            rendererAccessor.callScale(changedEntity, poseStack, partialTicks);

            if (ChangedAddonClientConfiguration.ALPHA_COMPATIBILITY_MODE_RENDER.get()) {
                if (changedEntity instanceof IAlphaAbleEntity alphaAbleEntity && alphaAbleEntity.isAlpha()) {
                    poseStack.scale(alphaAbleEntity.alphaScaleForRender(), alphaAbleEntity.alphaScaleForRender(), alphaAbleEntity.alphaScaleForRender());
                }
            }
            poseStack.translate(0.0F, -1.501F, 0.0F);
        }

        poseStack.pushPose();

        int blockLight = changedEntity.level().getBrightness(LightLayer.BLOCK, changedEntity.blockPosition());
        int skyLight = changedEntity.level().getBrightness(LightLayer.SKY, changedEntity.blockPosition());
        int light = LightTexture.pack(blockLight, skyLight);

        for (ModelPart modelPart : modelParts) {
            PartPose pose = snapshot.poses.get(modelPart);
            if (pose != null) modelPart.loadPose(pose);
        }

        float renderAlpha = this.alpha * snapshot.alphaMultiplier;

        model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(
                        ChangedAddonClientConfiguration.USE_ADDITIVE_TRANSPARENCY_FOR_FADE_PARTICLES.get() ?
                                ChangedAddonRenderTypes.entityAdditiveTranslucent(texture, false) :
                                ChangedAddonRenderTypes.entityTranslucent(texture, false)
                ),
                light,
                OverlayTexture.NO_OVERLAY,
                fadeColor.getRed() / 255f,
                fadeColor.getGreen() / 255f,
                fadeColor.getBlue() / 255f,
                renderAlpha
        );

        if (advancedHumanoidRenderer instanceof LivingEntityRendererAccessor livingEntityRendererAccessor) {
            List<RenderLayer<LivingEntity, EntityModel<LivingEntity>>> layers = livingEntityRendererAccessor.getLayers();
            if (layers != null && !layers.isEmpty()) {
                for (RenderLayer<LivingEntity, EntityModel<LivingEntity>> layer : layers) {
                    if (layer instanceof LatexHumanoidArmorLayer<?, ?>
                            || layer instanceof LatexItemInHandLayer<?, ?>
                            || layer instanceof CustomEyesLayer<?, ?>
                            || layer instanceof LatexElytraLayer<?, ?>
                            || layer instanceof AccessoryLayer<?, ?>) {
                        continue;
                    }
                    layer.render(poseStack, bufferSource, light, changedEntity, snapshot.limbSwing, snapshot.limbSwingAmount, partialTicks, snapshot.frozenAgeInTicks, snapshot.netHeadYaw, snapshot.headPitch);
                }
            }
        }

        poseStack.popPose();
        modelParts.forEach(ModelPart::resetPose);
    }

    protected void renderHumanoidSnapshot(ModelSnapshot snapshot, float partialTicks, LivingEntity livingEntity, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, Color fadeColor) {
        EntityRenderer<? super LivingEntity> rendererNormal = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        if (!(rendererNormal instanceof LivingEntityRenderer<? super LivingEntity, ?> livingEntityRenderer)) return;

        EntityModel<? super LivingEntity> model = livingEntityRenderer.getModel();
        ResourceLocation texture = livingEntityRenderer.getTextureLocation(livingEntity);

        if (!(model instanceof IPublicRootModel publicRoot)) return;
        ModelPart modelRoot = publicRoot.getModelRoot();
        if (modelRoot == null) return;

        List<ModelPart> modelParts = modelRoot.getAllParts().toList();

        if (livingEntity.hasPose(Pose.SLEEPING)) {
            Direction direction = livingEntity.getBedOrientation();
            if (direction != null) {
                float f4 = livingEntity.getEyeHeight(Pose.STANDING) - 0.1F;
                poseStack.translate((float) (-direction.getStepX()) * f4, 0.0F, (float) (-direction.getStepZ()) * f4);
            }
        }

        if (rendererNormal instanceof LivingEntityRendererAccessor rendererAccessor) {
            rendererAccessor.callSetupRotations(livingEntity, poseStack, snapshot.frozenAgeInTicks, snapshot.bodyYaw, partialTicks);
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            rendererAccessor.callScale(livingEntity, poseStack, partialTicks);

            if (ChangedAddonClientConfiguration.ALPHA_COMPATIBILITY_MODE_RENDER.get()) {
                if (livingEntity instanceof IAlphaAbleEntity alphaAbleEntity && alphaAbleEntity.isAlpha()) {
                    poseStack.scale(alphaAbleEntity.alphaScaleForRender(), alphaAbleEntity.alphaScaleForRender(), alphaAbleEntity.alphaScaleForRender());
                }
            }
            poseStack.translate(0.0F, -1.501F, 0.0F);
        }

        poseStack.pushPose();

        int blockLight = livingEntity.level().getBrightness(LightLayer.BLOCK, livingEntity.blockPosition());
        int skyLight = livingEntity.level().getBrightness(LightLayer.SKY, livingEntity.blockPosition());
        int light = LightTexture.pack(blockLight, skyLight);

        for (ModelPart modelPart : modelParts) {
            PartPose pose = snapshot.poses.get(modelPart);
            if (pose != null) modelPart.loadPose(pose);
        }

        float renderAlpha = this.alpha * snapshot.alphaMultiplier;

        model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(
                        ChangedAddonClientConfiguration.USE_ADDITIVE_TRANSPARENCY_FOR_FADE_PARTICLES.get() ?
                                ChangedAddonRenderTypes.entityAdditiveTranslucent(texture, false) :
                                ChangedAddonRenderTypes.entityTranslucent(texture, false)
                ),
                light,
                OverlayTexture.NO_OVERLAY,
                fadeColor.getRed() / 255f,
                fadeColor.getGreen() / 255f,
                fadeColor.getBlue() / 255f,
                renderAlpha
        );

        if (livingEntityRenderer instanceof LivingEntityRendererAccessor livingEntityRendererAccessor) {
            List<RenderLayer<LivingEntity, EntityModel<LivingEntity>>> layers = livingEntityRendererAccessor.getLayers();
            if (layers != null && !layers.isEmpty()) {
                for (RenderLayer<LivingEntity, EntityModel<LivingEntity>> layer : layers) {
                    if (layer instanceof HumanoidArmorLayer<?, ?, ?>
                            || layer instanceof ItemInHandLayer<?, ?>
                            || layer instanceof ElytraLayer<?, ?>
                            || layer instanceof AccessoryLayer<?, ?>) {
                        continue;
                    }
                    layer.render(poseStack, bufferSource, light, livingEntity, snapshot.limbSwing, snapshot.limbSwingAmount, partialTicks, snapshot.frozenAgeInTicks, snapshot.netHeadYaw, snapshot.headPitch);
                }
            }
        }

        poseStack.popPose();
        modelParts.forEach(ModelPart::resetPose);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    /* ========================= SNAPSHOT CONTAINER ========================= */

    protected static class ModelSnapshot {
        protected final Map<ModelPart, PartPose> poses = new HashMap<>();
        protected final Vec3 position;
        protected final float bodyYaw;
        protected final int frozenAgeInTicks;
        protected final float limbSwing;
        protected final float limbSwingAmount;
        protected final float netHeadYaw;
        protected final float headPitch;
        protected final float alphaMultiplier;

        public ModelSnapshot(Vec3 position, float bodyYaw, int frozenAgeInTicks, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch, float alphaMultiplier) {
            this.position = position;
            this.bodyYaw = bodyYaw;
            this.frozenAgeInTicks = frozenAgeInTicks;
            this.limbSwing = limbSwing;
            this.limbSwingAmount = limbSwingAmount;
            this.netHeadYaw = netHeadYaw;
            this.headPitch = headPitch;
            this.alphaMultiplier = alphaMultiplier;
        }
    }

    /* ========================= PROVIDER ========================= */

    public static class Provider implements ParticleProvider<EntityModelFadeParticleOptions> {
        public Provider() {}

        @Override
        public @Nullable Particle createParticle(
                @NotNull EntityModelFadeParticleOptions options,
                @NotNull ClientLevel level,
                double x, double y, double z,
                double xs, double ys, double zs
        ) {
            EntityModelFadeParticle entityModelFadeParticle = new EntityModelFadeParticle(level, x, y, z, level.getEntity(options.targetId()), options);
            entityModelFadeParticle.setParticleSpeed(xs, ys, zs);
            return entityModelFadeParticle;
        }
    }
}