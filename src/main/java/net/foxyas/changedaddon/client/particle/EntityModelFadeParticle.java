package net.foxyas.changedaddon.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.foxyas.changedaddon.client.model.api.IPublicRootModel;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.foxyas.changedaddon.mixins.client.renderer.LivingEntityRendererAccessor;
import net.minecraft.client.Camera;
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
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class EntityModelFadeParticle extends Particle {

    private final Entity entity;
    private final int color;
    protected boolean snapshotTaken;
    protected float frozenLimbSwing;
    protected float frozenLimbSwingAmount;
    protected int frozenAgeInTicks;
    protected float frozenNetHeadYaw;
    protected float frozenHeadPitch;
    protected float frozenBodyYaw;
    protected float frozenXRot;
    private float frozenModelRot;
    protected HashMap<ModelPart, PartPose> poses = new HashMap<>();

    public EntityModelFadeParticle(
            ClientLevel level,
            double x, double y, double z,
            Entity entity,
            int color,
            float fadeSpeed) {
        super(level, x, y, z);
        this.entity = entity;
        this.color = color;

        this.lifetime = (int) (20 * fadeSpeed);
        this.gravity = 0f;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 1 - ((float) this.getAge() / this.getLifetime());
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public void render(@NotNull VertexConsumer consumer, @NotNull Camera camera, float partialTick) {
        Color fadeColor = new Color(color);

        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        PoseStack poseStack = new PoseStack();
        Vec3 camPos = camera.getPosition();
        poseStack.translate(
                this.x - camPos.x,
                this.y - camPos.y,
                this.z - camPos.z
        );


        if (!(entity instanceof LivingEntity livingEntity)) return;
        EntityRenderer<? super LivingEntity> rendererNormal = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        if (!(rendererNormal instanceof LivingEntityRenderer<? super LivingEntity, ?> livingEntityRenderer)) return;
        EntityModel<? super LivingEntity> model = livingEntityRenderer.getModel();
        ResourceLocation texture = livingEntityRenderer.getTextureLocation(livingEntity);
        VertexConsumer buffer = bufferSource.getBuffer(ChangedAddonRenderTypes.entityTranslucent(texture));


        if (!(model instanceof IPublicRootModel iPublicRootModel)) return;
        ModelPart modelRoot = iPublicRootModel.getModelRoot();
        if (modelRoot == null) return;
        List<ModelPart> modelParts = modelRoot.getAllParts().toList();

        if (!snapshotTaken) {
            frozenModelRot = Mth.lerp(partialTick, livingEntity.yBodyRotO, livingEntity.yBodyRot);
            frozenLimbSwing = livingEntity.walkAnimation.position();
            frozenLimbSwingAmount = livingEntity.walkAnimation.speed();
            frozenAgeInTicks = livingEntity.tickCount;

            frozenNetHeadYaw = Mth.lerp(partialTick, livingEntity.yHeadRotO, livingEntity.yHeadRot) -
                    Mth.lerp(partialTick, livingEntity.yBodyRotO, livingEntity.yBodyRot);

            frozenHeadPitch = livingEntity.getXRot();
            frozenBodyYaw = livingEntity.yBodyRot;
            frozenXRot = livingEntity.getXRot();

            float limbSwing = frozenLimbSwing;
            float limbSwingAmount = frozenLimbSwingAmount;
            float ageInTicks = frozenAgeInTicks;
            float netHeadYaw = frozenNetHeadYaw;
            float headPitch = frozenHeadPitch;

            model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
            model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            for (ModelPart modelPart : modelParts) {
                poses.putIfAbsent(modelPart, modelPart.storePose());
            }

            snapshotTaken = true;
            return;
        }


        // Rotação do corpo (igual renderer normal)
        poseStack.mulPose(Axis.YP.rotationDegrees(-frozenModelRot));

        // Rotação X real da entity
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        poseStack.pushPose();

        float limbSwing = frozenLimbSwing;
        float limbSwingAmount = frozenLimbSwingAmount;
        float ageInTicks = frozenAgeInTicks;
        float netHeadYaw = frozenNetHeadYaw;
        float headPitch = frozenHeadPitch;

        //model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
        //model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        for (ModelPart modelPart : modelParts) {
            modelPart.loadPose(poses.get(modelPart));
        }

        int blockLight = livingEntity.level.getBrightness(LightLayer.BLOCK, livingEntity.blockPosition());
        int skyLight = livingEntity.level.getBrightness(LightLayer.SKY, livingEntity.blockPosition());
        int Light = LightTexture.pack(blockLight, skyLight);

        model.renderToBuffer(poseStack, buffer, Light, OverlayTexture.NO_OVERLAY, fadeColor.getRed() / 255f, fadeColor.getGreen() / 255f, fadeColor.getBlue() / 255f, this.alpha);
        if (livingEntityRenderer instanceof LivingEntityRendererAccessor livingEntityRendererAccessor) {
            List<RenderLayer<LivingEntity, EntityModel<LivingEntity>>> layers = livingEntityRendererAccessor.getLayers();
            if (layers != null && !layers.isEmpty()) {
                for (RenderLayer<LivingEntity, EntityModel<LivingEntity>> layer : layers) {
                    layer.render(poseStack, bufferSource, Light, livingEntity, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
                }
            }
        }

        poseStack.popPose();
        bufferSource.endBatch();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static Provider provider() {
        return new Provider();
    }


    /* ========================= PROVIDER ========================= */

    public static class Provider implements ParticleProvider<EntityModelFadeParticleOptions> {
        public Provider() {
        }

        @Override
        public @Nullable Particle createParticle(
                @NotNull EntityModelFadeParticleOptions options,
                @NotNull ClientLevel level,
                double x, double y, double z,
                double xs, double ys, double zs
        ) {
            EntityModelFadeParticle entityModelFadeParticle = new EntityModelFadeParticle(level, x, y, z, options.target(), options.color(), options.fadeSpeed());
            entityModelFadeParticle.setParticleSpeed(xs, ys, zs);
            return entityModelFadeParticle;
        }
    }
}