package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.util.ModelPosUtil;
import net.foxyas.changedaddon.util.ModelUtils;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DebugParticlesTrailsLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> implements FirstPersonLayer<T> {

    // ---- toggles de debug (mude em runtime, ex.: por comando) ----
    public static boolean DEBUG_MARKERS = true;     // caixas = posição "verdadeira"
    public static boolean DEBUG_PARTICLES = true;   // partículas = resultado do util

    /** Intervalo entre partículas, POR PARTE, em ticks (mínimo 1). */
    public static final int PART_INTERVAL_TICKS = 2;

    /** Espalhamento em blocos. 0 = posição exata. */
    public static final double SPREAD = 0.0;

    // Pontos locais em PIXELS (16 px = 1 bloco), relativos ao pivô da parte.
    private static final float[] HEAD_POINT  = {0f, -4f, 0f};
    private static final float[] TORSO_POINT = {0f,  6f, 0f};
    private static final float[] ARMS_POINT  = {1f,  6f, 0f};
    private static final float[] LEGS_POINT  = {0f,  6f, 0f};
    private static final float[] TAIL_POINT  = {0f,  0f, 0f};

    private final ParticleOptions[] particles;
    private float spawnProbability = 1f; // chance por parte, a cada intervalo

    private final Map<Entity, State> states = new WeakHashMap<>();
    private final Map<ModelPart, List<ModelPart>> pathCache = new IdentityHashMap<>();

    private static final class State {
        /** Último tick de spawn de cada parte (igual ao LAST_SPAWN do debugger). */
        final Map<ModelPart, Long> lastSpawn = new IdentityHashMap<>();
    }

    public DebugParticlesTrailsLayer(RenderLayerParent<T, M> parent, ParticleOptions... particle) {
        super(parent);
        this.particles = particle;
    }

    public DebugParticlesTrailsLayer(RenderLayerParent<T, M> parent, float spawnProbability, ParticleOptions... particle) {
        super(parent);
        this.particles = particle;
        this.spawnProbability = spawnProbability;
    }

    public float getSpawnProbability() { return spawnProbability; }
    public void setSpawnProbability(float p) { this.spawnProbability = p; }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        boolean paused = Minecraft.getInstance().isPaused();
        long now = entity.level().getGameTime();
        State state = states.computeIfAbsent(entity, e -> new State());
        RandomSource random = entity.getRandom();

        AdvancedHumanoidModel<?> model = this.getParentModel();

        for (ModelPart part : getRelevantParts(model)) {
            float[] p = getLocalPoint(model, part);
            List<ModelPart> chain = getPath(model, part);

            poseStack.pushPose();
            for (ModelPart node : chain) node.translateAndRotate(poseStack);

            // 1) util: posição no mundo
            Vec3 world = ModelPosUtil.toWorldPx(poseStack, p[0], p[1], p[2]);

            // 2) ground truth: caixa desenhada no mesmo espaço da parte
            if (DEBUG_MARKERS) {
                float s = 1.5f / 16f;
                float cx = p[0] / 16f, cy = p[1] / 16f, cz = p[2] / 16f;
                VertexConsumer vc = bufferSource.getBuffer(RenderType.lines());
                LevelRenderer.renderLineBox(poseStack, vc,
                        cx - s, cy - s, cz - s, cx + s, cy + s, cz + s,
                        1f, 0.2f, 0.2f, 1f);
            }
            poseStack.popPose();

            // 3) partícula, com timer por parte
            if (!DEBUG_PARTICLES || paused) continue;

            long last = state.lastSpawn.getOrDefault(part, Long.MIN_VALUE);
            if (now - last < Math.max(1, PART_INTERVAL_TICKS)) continue;

            // marca já aqui: o dado é rolado UMA vez por intervalo, não por frame
            state.lastSpawn.put(part, now);

            if (random.nextFloat() <= spawnProbability) {
                spawnParticle(entity, world);
            }
        }
    }

    // ---------------------------------------------------------------- partes

    protected List<ModelPart> getRelevantParts(AdvancedHumanoidModel<?> model) {
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

    protected float[] getLocalPoint(AdvancedHumanoidModel<?> model, ModelPart part) {
        if (part == model.getHead()) return HEAD_POINT;
        if (part == model.getTorso()) return TORSO_POINT;
        if (ModelUtils.getTailFromModelIfAny(model).contains(part)) return TAIL_POINT;
        List<ModelPart> arms = Arrays.stream(HumanoidArm.values()).map(model::getArm).toList();
        List<ModelPart> legs = Arrays.stream(HumanoidArm.values()).map(model::getLeg).toList();
        if (arms.contains(part)) {
            boolean right = part == model.getArm(HumanoidArm.RIGHT) || part == model.getLeg(HumanoidArm.RIGHT);
            return right ? new float[]{-ARMS_POINT[0], ARMS_POINT[1], ARMS_POINT[2]} : ARMS_POINT;
        } else if (legs.contains(part)) {
            // outward = +X for the left limbs, -X for the right limbs
            boolean right = part == model.getArm(HumanoidArm.RIGHT) || part == model.getLeg(HumanoidArm.RIGHT);
            return right ? new float[]{-LEGS_POINT[0], LEGS_POINT[1], LEGS_POINT[2]} : LEGS_POINT;
        }

        // outward = +X for the left limbs, -X for the right limbs
        boolean right = part == model.getArm(HumanoidArm.RIGHT) || part == model.getLeg(HumanoidArm.RIGHT);
        return right ? new float[]{-ARMS_POINT[0], ARMS_POINT[1], ARMS_POINT[2]} : ARMS_POINT;
    }

    // ------------------------------------------------------------ hierarquia

    private List<ModelPart> getPath(AdvancedHumanoidModel<?> model, ModelPart target) {
        List<ModelPart> cached = pathCache.get(target);
        if (cached != null) return cached;

        List<ModelPart> best = new ArrayList<>(List.of(target));
        for (ModelPart candidate : getRelevantParts(model)) {
            List<ModelPart> path = findPath(candidate, target);
            if (path != null && path.size() > best.size()) best = path;
        }
        pathCache.put(target, best);
        return best;
    }

    private static List<ModelPart> findPath(ModelPart root, ModelPart target) {
        if (root == target) return new ArrayList<>(List.of(root));
        for (ModelPart child : root.children.values()) {
            List<ModelPart> sub = findPath(child, target);
            if (sub != null) {
                sub.add(0, root);
                return sub;
            }
        }
        return null;
    }

    // ------------------------------------------------------------- partículas

    protected void spawnParticle(T entity, Vec3 pos) {
        for (ParticleOptions particle : particles) {
            double dx = SPREAD == 0 ? 0 : entity.getRandom().nextGaussian() * SPREAD;
            double dy = SPREAD == 0 ? 0 : entity.getRandom().nextGaussian() * SPREAD;
            double dz = SPREAD == 0 ? 0 : entity.getRandom().nextGaussian() * SPREAD;
            entity.level().addParticle(particle, pos.x() + dx, pos.y() + dy, pos.z() + dz, 0, 0, 0);
        }
    }

    public @NotNull RenderType renderType() {
        return RenderType.cutoutMipped();
    }

    @Override
    public void renderFirstPersonOnFace(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, Camera camera) {
        FirstPersonLayer.super.renderFirstPersonOnFace(stack, bufferSource, packedLight, entity, camera);
    }
}