package net.foxyas.changedaddon.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.foxyas.changedaddon.client.model.api.IPublicRootModel;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.foxyas.changedaddon.mixins.client.renderer.LivingEntityRendererAccessor;
import net.ltxprogrammer.changed.client.ModelPartStem;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexHumanoidArmorLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexItemInHandLayer;
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
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
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
import java.util.ArrayList;
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
    protected LivingEntity frozenEntity;
    protected HashMap<ModelPart, PartPose> poses = new HashMap<>();
    private float frozenModelRot;

//    protected boolean armorSnapshotTaken;
//    protected HashMap<ModelPart, PartPose> armorPoses = new HashMap<>();

    public EntityModelFadeParticle(
            ClientLevel level,
            double x, double y, double z,
            Entity entity,
            int color,
            float duration) {
        super(level, x, y, z);
        this.entity = entity;
        this.color = color;

        this.lifetime = (int) (20 * duration);
        this.gravity = 0f;
    }

    public static Provider provider() {
        return new Provider();
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
        if (entity == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON
                && entity.distanceToSqr(x, y, z) < 3) return;//ignore if too close

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

        if (frozenEntity == null) frozenEntity = EntityUtil.maybeGetOverlaying(livingEntity);

        if (frozenEntity instanceof ChangedEntity frozenChangedEntity) {
            renderTransfur(partialTick, frozenChangedEntity, bufferSource, poseStack, fadeColor);
        } else {
            renderHumanoid(partialTick, frozenEntity, bufferSource, poseStack, fadeColor);
        }

        poseStack.popPose();
        bufferSource.endBatch();
    }

    protected void renderTransfur(float partialTick, ChangedEntity changedEntity, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, Color fadeColor) {
        EntityRenderer<? super ChangedEntity> rendererNormal = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(changedEntity);
        if (!(rendererNormal instanceof AdvancedHumanoidRenderer<? super ChangedEntity, ?> advancedHumanoidRenderer))
            return;
        AdvancedHumanoidModel<? super ChangedEntity> model = advancedHumanoidRenderer.getModel();
        ResourceLocation texture = advancedHumanoidRenderer.getTextureLocation(changedEntity);


        if (!(model instanceof IPublicRootModel iPublicRootModel)) return;
        ModelPart modelRoot = iPublicRootModel.getModelRoot();
        if (modelRoot == null) return;
        List<ModelPart> modelParts = new ArrayList<>(model.getRootLevelLimbs().toList());
        List<ModelPartStem> allParts = model.getAllParts().toList();
        for (ModelPartStem allPart : allParts) {
            modelParts.addAll(allPart.stem);
        }
        //modelParts.addAll(allParts.stream().map(ModelPartStem::getLeaf).toList());
        //modelParts.addAll(allParts.stream().map(ModelPartStem::getRoot).toList());


        if (!snapshotTaken) {
            frozenModelRot = Mth.lerp(partialTick, changedEntity.yBodyRotO, changedEntity.yBodyRot);
            frozenLimbSwing = changedEntity.walkAnimation.position();
            frozenLimbSwingAmount = changedEntity.walkAnimation.speed();
            frozenAgeInTicks = changedEntity.tickCount;

            frozenNetHeadYaw = Mth.lerp(partialTick, changedEntity.yHeadRotO, changedEntity.yHeadRot) -
                    Mth.lerp(partialTick, changedEntity.yBodyRotO, changedEntity.yBodyRot);

            frozenHeadPitch = changedEntity.getXRot();
            frozenBodyYaw = changedEntity.yBodyRot;
            frozenXRot = changedEntity.getXRot();

            float limbSwing = frozenLimbSwing;
            float limbSwingAmount = frozenLimbSwingAmount;
            float ageInTicks = frozenAgeInTicks;
            float netHeadYaw = frozenNetHeadYaw;
            float headPitch = frozenHeadPitch;

            model.prepareMobModel(changedEntity, limbSwing, limbSwingAmount, partialTick);
            model.setupAnim(changedEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

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

        int blockLight = changedEntity.level.getBrightness(LightLayer.BLOCK, changedEntity.blockPosition());
        int skyLight = changedEntity.level.getBrightness(LightLayer.SKY, changedEntity.blockPosition());
        int Light = LightTexture.pack(blockLight, skyLight);

        for (ModelPart modelPart : modelParts) {
            modelPart.loadPose(poses.get(modelPart));
        }
        model.renderToBuffer(poseStack, bufferSource.getBuffer(ChangedAddonRenderTypes.entityAdditiveTranslucent(texture, false)), Light, OverlayTexture.NO_OVERLAY, fadeColor.getRed() / 255f, fadeColor.getGreen() / 255f, fadeColor.getBlue() / 255f, this.alpha);
        if (advancedHumanoidRenderer instanceof LivingEntityRendererAccessor livingEntityRendererAccessor) {
            List<RenderLayer<LivingEntity, EntityModel<LivingEntity>>> layers = livingEntityRendererAccessor.getLayers();
            if (layers != null && !layers.isEmpty()) {
                for (RenderLayer<LivingEntity, EntityModel<LivingEntity>> layer : layers) {
                    if (layer instanceof LatexHumanoidArmorLayer<?, ?> || layer instanceof LatexItemInHandLayer<?, ?> || layer instanceof CustomEyesLayer<?, ?>) {
//                        if (layer instanceof LatexHumanoidArmorLayer armorLayer) {
//
//                            for (EquipmentSlot equipmentSlot: Arrays.stream(EquipmentSlot.values())
//                                    .filter(equipmentSlot -> equipmentSlot.getType() == EquipmentSlot.Type.ARMOR).toList()) {
//                                LatexHumanoidArmorModel<? super ChangedEntity, ?> armorModel = armorLayer.getArmorModel(changedEntity, EquipmentSlot.CHEST);
//
//                                if (!(armorModel instanceof IPublicRootModel armorModelAccessor)) return;
//                                ModelPart armorModelRoot = armorModelAccessor.getModelRoot();
//                                if (armorModelRoot == null) return;
//                                List<ModelPart> armorModelParts = new ArrayList<>(model.getRootLevelLimbs().toList());
//                                List<ModelPartStem> armorAllParts = armorModel.getAllParts().toList();
//                                for (ModelPartStem allPart : armorAllParts) {
//                                    armorModelParts.addAll(allPart.stem);
//                                }
//
//                                armorModel.prepareMobModel(changedEntity, limbSwing, limbSwingAmount, partialTick);
//                                armorModel.setupAnim(changedEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
//
//                                for (ModelPart modelPart : armorModelParts) {
//                                    armorPoses.putIfAbsent(modelPart, modelPart.storePose());
//                                }
//
//                            }
//                        }


                        continue;
                    }
                    layer.render(poseStack, bufferSource, Light, changedEntity, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
                    //TODO: filter this so it don't render the "hold item" layers :>
                }
            }
        }
        modelParts.forEach(ModelPart::resetPose);
    }

    protected void renderHumanoid(float partialTick, LivingEntity livingEntity, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, Color fadeColor) {
        EntityRenderer<? super LivingEntity> rendererNormal = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        if (!(rendererNormal instanceof LivingEntityRenderer<? super LivingEntity, ?> livingEntityRenderer)) return;
        EntityModel<? super LivingEntity> model = livingEntityRenderer.getModel();
        ResourceLocation texture = livingEntityRenderer.getTextureLocation(livingEntity);


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


        int blockLight = livingEntity.level.getBrightness(LightLayer.BLOCK, livingEntity.blockPosition());
        int skyLight = livingEntity.level.getBrightness(LightLayer.SKY, livingEntity.blockPosition());
        int Light = LightTexture.pack(blockLight, skyLight);

        for (ModelPart modelPart : modelParts) {
            modelPart.loadPose(poses.get(modelPart));
        }
        model.renderToBuffer(poseStack, bufferSource.getBuffer(ChangedAddonRenderTypes.entityAdditiveTranslucent(texture, false)), Light, OverlayTexture.NO_OVERLAY, fadeColor.getRed() / 255f, fadeColor.getGreen() / 255f, fadeColor.getBlue() / 255f, this.alpha);
        if (livingEntityRenderer instanceof LivingEntityRendererAccessor livingEntityRendererAccessor) {
            List<RenderLayer<LivingEntity, EntityModel<LivingEntity>>> layers = livingEntityRendererAccessor.getLayers();
            if (layers != null && !layers.isEmpty()) {
                for (RenderLayer<LivingEntity, EntityModel<LivingEntity>> layer : layers) {
                    if (layer instanceof HumanoidArmorLayer<?, ?, ?> || layer instanceof ItemInHandLayer<?, ?>) {
                        continue;
                    }
                    layer.render(poseStack, bufferSource, Light, livingEntity, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
                    //TODO: filter this so it don't render the "hold item" layers :>
                }
            }
        }
        modelParts.forEach(ModelPart::resetPose);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
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
            EntityModelFadeParticle entityModelFadeParticle = new EntityModelFadeParticle(level, x, y, z, level.getEntity(options.targetId()), options.color(), options.duration());
            entityModelFadeParticle.setParticleSpeed(xs, ys, zs);
            return entityModelFadeParticle;
        }
    }
}