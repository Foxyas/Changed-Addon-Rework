package net.foxyas.changedaddon.process;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.util.ModelPosUtil;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.commands.Commands;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

// Turn this off before posting it.
public final class ModelPosDebugger {
    private ModelPosDebugger() {}

    /*
    // ---- toggles (changed by /modeldebug) ----
    public static boolean enabled = SharedConstants.IS_RUNNING_IN_IDE || !FMLLoader.isProduction();
    public static boolean markers = true;     // line boxes (ground truth)
    public static boolean particles = true;   // dummy particles (util result)
    private static final int PARTICLE_INTERVAL = 2; // ticks

    /** A point to test: a model part + a local point in PIXELS (16 px = 1 block). */
    /*private record Probe(String name, Function<HumanoidModel<?>, ModelPart> part,
                         float px, float py, float pz, float r, float g, float b) {}

    // Approximate armor stand geometry, adjust to taste.
    private static final List<Probe> PROBES = List.of(
        new Probe("head top",   m -> m.head,     0f, -7f, 0f, 1f, 0f, 0f),
        new Probe("body",       m -> m.body,     0f,  1.5f, 0f, 0f, 1f, 0f),
        new Probe("rightArm",   m -> m.rightArm, 0f, 10f, 0f, 0f, 0.4f, 1f),
        new Probe("leftArm",    m -> m.leftArm,  0f, 10f, 0f, 1f, 1f, 0f),
        new Probe("rightLeg",   m -> m.rightLeg, 0f, 11f, 0f, 1f, 0f, 1f),
        new Probe("leftLeg",    m -> m.leftLeg,  0f, 11f, 0f, 0f, 1f, 1f)
    );

    private static final Map<Long, Long> LAST_SPAWN = new HashMap<>();

    // ================= LAYER =================
    public static class DebugLayer extends RenderLayer<ArmorStand, ArmorStandArmorModel> {
        public DebugLayer(RenderLayerParent<ArmorStand, ArmorStandArmorModel> parent) {
            super(parent);
        }

        @Override
        public void render(PoseStack pose, MultiBufferSource buf, int light, ArmorStand stand,
                           float limbSwing, float limbSwingAmount, float partialTick,
                           float ageInTicks, float netHeadYaw, float headPitch) {
            if (!enabled) return;
            ArmorStandArmorModel model = getParentModel();
            long gameTime = stand.level().getGameTime();

            for (int i = 0; i < PROBES.size(); i++) {
                Probe p = PROBES.get(i);

                pose.pushPose();
                p.part().apply(model).translateAndRotate(pose);

                // 1) the util result (must be called with the part transform applied)
                Vec3 world = ModelPosUtil.toWorldPx(pose, p.px(), p.py(), p.pz());

                // 2) ground truth: box drawn directly in this pose space (blocks)
                if (markers) {
                    float s = 1.5f / 16f;
                    float cx = p.px() / 16f, cy = p.py() / 16f, cz = p.pz() / 16f;
                    LevelRenderer.renderLineBox(pose, buf.getBuffer(RenderType.lines()),
                        cx - s, cy - s, cz - s, cx + s, cy + s, cz + s,
                        p.r(), p.g(), p.b(), 1f);
                }
                pose.popPose();

                // 3) dummy particle at the util's world position
                if (particles) {
                    long key = ((long) stand.getId() << 8) | i;
                    long last = LAST_SPAWN.getOrDefault(key, 0L);
                    if (gameTime - last >= PARTICLE_INTERVAL) {
                        LAST_SPAWN.put(key, gameTime);
                        Minecraft.getInstance().level.addParticle(
                            new DustParticleOptions(new Vector3f(p.r(), p.g(), p.b()), 0.6f),
                            world.x, world.y, world.z, 0, 0, 0);
                    }
                }
            }
        }
    }

    // ================= EVENTS =================
    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void addLayers(EntityRenderersEvent.AddLayers e) {
            ArmorStandRenderer r = e.getEntityRenderer(EntityType.ARMOR_STAND);
            if (r != null) r.addLayer(new DebugLayer(r));
        }
    }

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, value = Dist.CLIENT)
    public static class ForgeBusEvents {
        @SubscribeEvent
        public static void commands(RegisterClientCommandsEvent e) {
            e.getDispatcher().register(Commands.literal("modeldebug")
                .executes(c -> { enabled = !enabled; LAST_SPAWN.clear(); return msg(c.getSource(), "enabled", enabled); })
                .then(Commands.literal("markers").executes(c -> { markers = !markers; return msg(c.getSource(), "markers", markers); }))
                .then(Commands.literal("particles").executes(c -> { particles = !particles; return msg(c.getSource(), "particles", particles); })));
        }

        private static int msg(net.minecraft.commands.CommandSourceStack src, String name, boolean v) {
            src.sendSuccess(() -> Component.literal("modeldebug " + name + " = " + v), false);
            return 1;
        }
    }
    */
}