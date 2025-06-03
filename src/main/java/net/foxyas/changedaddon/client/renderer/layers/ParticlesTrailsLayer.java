package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.process.util.FoxyasUtils;
import net.foxyas.changedaddon.process.util.ModelUtils;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.world.entity.Entity;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class ParticlesTrailsLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> implements FirstPersonLayer<T> {

    private static final Vector3f DEFAULT_OFFSET = new Vector3f(0, -0.10f, 0);
    private static final Vector3f WORLD_OFFSET = new Vector3f(0, 0.85f, 0);
    private static final Vec3 ENTITY_ROTATION = new Vec3(180, 0, 0);
    private float spawnProbability = 0.025f;

    private final ParticleOptions[] particles;

    private PoseStack poseStack = new PoseStack();

    public ParticlesTrailsLayer(RenderLayerParent<T, M> parent, ParticleOptions... particle) {
        super(parent);
        this.particles = particle;
    }

    public ParticlesTrailsLayer(RenderLayerParent<T, M> parent, float spawnProbability, ParticleOptions... particle) {
        super(parent);
        this.particles = particle;
        this.spawnProbability = spawnProbability;
    }

    public void setSpawnProbability(float spawnProbability) {
        this.spawnProbability = spawnProbability;
    }

    public float getSpawnProbability() {
        return spawnProbability;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        AdvancedHumanoidModel<?> model = this.getParentModel();

        List<ModelPart> parts = getRelevantParts(model);
        Vec3 worldPos;

        /*for (ModelPart selectedPart : parts) {
            worldPos = getWorldPositionFromPart(entity, model, poseStack, part);
        }*/

        ModelPart selectedPart = getRandomPart(parts, entity.getRandom());
        worldPos = getWorldPositionFromPart(entity, model, selectedPart);

        if (entity.getRandom().nextFloat() <= spawnProbability && !Minecraft.getInstance().isPaused()) {
            spawnParticle(entity, selectedPart == model.getHead(), worldPos);
        }
    }

    private List<ModelPart> getRelevantParts(AdvancedHumanoidModel<?> model) {
        List<ModelPart> parts = new ArrayList<>(List.of(model.getHead(), model.getTorso()));
        parts.addAll(ModelUtils.getTailFromModelIfAny(model));
        parts.addAll(List.of(
                model.getArm(HumanoidArm.RIGHT),
                model.getArm(HumanoidArm.LEFT),
                model.getLeg(HumanoidArm.RIGHT),
                model.getLeg(HumanoidArm.LEFT)
        ));
        return parts;
    }

    private ModelPart getRandomPart(List<ModelPart> parts, Random random) {
        return parts.get(random.nextInt(parts.size()));
    }

    private Vec3 getWorldPositionFromPart(T entity, AdvancedHumanoidModel<?> model, ModelPart part) {
        if (part == model.getHead()) {
            return FoxyasUtils.getRelativePositionEyes(entity, new Vec3(0, 0, 0.1f));
        } else if (ModelUtils.getTailFromModelIfAny(model).contains(part)) {
            Vector3f offset = DEFAULT_OFFSET.copy();
            offset.add(0, 0, 0.5f);
            return ModelUtils.getWorldSpaceFromModelPart(part, offset, WORLD_OFFSET, entity, Vec3.ZERO, ENTITY_ROTATION, poseStack, false);
        } else {
            return ModelUtils.getWorldSpaceFromModelPart(part, DEFAULT_OFFSET, WORLD_OFFSET, entity, Vec3.ZERO, ENTITY_ROTATION, poseStack, false);
        }
    }

    private void spawnParticle(T entity, boolean isHead, Vec3 pos) {
        for (ParticleOptions particle : particles) {
            double dx = entity.getRandom().nextDouble(isHead ? -0.5 : -0.25, isHead ? 0.5 : 0.25);
            double dy = entity.getRandom().nextDouble(isHead ? -0.5 : -0.25, isHead ? 0.5 : 0.25);
            double dz = entity.getRandom().nextDouble(isHead ? -0.5 : -0.25, isHead ? 0.5 : 0.25);

            entity.getLevel().addParticle(
                    particle,
                    pos.x() + dx, pos.y() + dy, pos.z() + dz,
                    0, 0, 0
            );
        }
    }

    public @NotNull RenderType renderType() {
        return RenderType.cutoutMipped();
    }

    @Override
    public void renderFirstPersonOnFace(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, Camera camera) {
        FirstPersonLayer.super.renderFirstPersonOnFace(stack, bufferSource, packedLight, entity, camera);
    }

    @Override
    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PoseStack stackCorrector, float partialTick) {
        FirstPersonLayer.super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, stackCorrector, partialTick);
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public void setPoseStack(PoseStack poseStack) {
        this.poseStack = poseStack;
    }
}
