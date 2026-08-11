package net.foxyas.changedaddon.init;

import com.mojang.serialization.Codec;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.particle.*;
import net.foxyas.changedaddon.effect.particles.*;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Function;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonParticleTypes {

    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ChangedAddonMod.MODID);

    public static final RegistryObject<ParticleType<SimpleParticleType>> SOLVENT_PARTICLE = REGISTRY.register("solvent_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<ThunderSparkOption>> THUNDER_SPARK = register("thunder_spark", ThunderSparkOption.DESERIALIZER, ThunderSparkOption::codec);
    public static final RegistryObject<ParticleType<SignalParticleOption>> SIGNAL_PARTICLE = register("signal_particle", SignalParticleOption.DESERIALIZER, SignalParticleOption::codec);
    public static final RegistryObject<ParticleType<LaserPointParticle.Option>> LASER_POINT = register("laser_point", LaserPointParticle.Option.DESERIALIZER, LaserPointParticle.Option::codec);
    public static final RegistryObject<ParticleType<FallingLeafParticleOptions>> FALLING_LEAVES = register("falling_leaves", FallingLeafParticleOptions.DESERIALIZER, FallingLeafParticleOptions::codec);

    public static final RegistryObject<ParticleType<EntityModelFadeParticleOptions>> ENTITY_MODEL_FADE = register("entity_model_fade", EntityModelFadeParticleOptions.DESERIALIZER, EntityModelFadeParticleOptions::codec);

    public static final RegistryObject<ParticleType<RibbonParticleOption>> RIBBON = register("ribbon", RibbonParticleOption.DESERIALIZER, RibbonParticleOption::codec);
    public static final RegistryObject<ParticleType<AgeableRibbonParticleOption>> AGEABLE_RIBBON = register("ageable_ribbon", AgeableRibbonParticleOption.DESERIALIZER, AgeableRibbonParticleOption::codec);
    public static final RegistryObject<ParticleType<MultiColorRibbonParticleOption>> MULTI_COLOR_RIBBON = register("multi_color_ribbon", MultiColorRibbonParticleOption.DESERIALIZER, MultiColorRibbonParticleOption::codec);
    public static final RegistryObject<ParticleType<ThunderParticleOptions>> THUNDER_PARTICLE = register("thunder_bolt", ThunderParticleOptions.DESERIALIZER, ThunderParticleOptions::codec);
    public static final RegistryObject<ParticleType<EntityLinkedThunderParticleOptions>> ENTITY_LINKED_THUNDER_PARTICLE = register("thunder_bolt_linked", EntityLinkedThunderParticleOptions.DESERIALIZER, EntityLinkedThunderParticleOptions::codec);

    /**
     * Creates a configured instance of {@link ThunderParticleOptions} for spawning custom lightning bolt particles.
     *
     * @param speed  the propagation speed of the bolt along its segment path
     * @param rooted {@code true} if the origin point remains fixed while rendering; {@code false} if the root follows moving nodes
     * @param shake  a 3D vector (X, Y, Z) multiplier controlling the jitter and offset intensity along each axis
     * @param color  an RGB color vector with values ranging from 0.0f to 1.0f
     * @param size   the thickness scaling factor for the rendered lightning quads
     * @return a fully configured {@link ThunderParticleOptions} instance ready to pass to particle spawning methods
     */
    public static ThunderParticleOptions thunderBolt(float speed,
                                                     boolean rooted,
                                                     Vector3f shake,
                                                     Vector3f color,
                                                     int lifetime,
                                                     float size) {
        return ThunderParticleOptionsBuilder.create()
                .speed(speed)
                .rooted(rooted)
                .shake(shake)
                .color(color)
                .lifeTime(lifetime)
                .size(size)
                .build();
    }

    /**
     * Creates a configured instance of {@link EntityLinkedThunderParticleOptions} for spawning custom lightning bolt particles.
     *
     * @param entity entity that the thunderbolt is linked
     * @param shouldUseTargetPosAsBaseForDeltas  if it should use a "entity pos+delta vec" instead of "entity pos to delta vec pos"
     * @param speed  the propagation speed of the bolt along its segment path
     * @param rooted {@code true} if the origin point remains fixed while rendering; {@code false} if the root follows moving nodes
     * @param shake  a 3D vector (X, Y, Z) multiplier controlling the jitter and offset intensity along each axis
     * @param color  an RGB color vector with values ranging from 0.0f to 1.0f
     * @param size   the thickness scaling factor for the rendered lightning quads
     * @return a fully configured {@link EntityLinkedThunderParticleOptions} instance ready to pass to particle spawning methods
     */
    public static EntityLinkedThunderParticleOptions thunderBoltLinkedTo(
            Entity entity,
            boolean shouldUseTargetPosAsBaseForDeltas,
            float speed,
            boolean rooted,
            Vector3f shake,
            Vector3f color,
            int lifetime,
            float size
    ) {
        return ThunderParticleOptionsBuilder.create()
                .speed(speed)
                .rooted(rooted)
                .shake(shake)
                .color(color)
                .lifeTime(lifetime)
                .size(size)
                .shouldUseTargetPosAsBaseForDeltas(shouldUseTargetPosAsBaseForDeltas)
                .withTarget(entity)
                .buildLinked();
    }

    public static ThunderSparkOption thunderSpark(int lifespan) {
        return new ThunderSparkOption(THUNDER_SPARK.get(), lifespan);
    }

    public static LaserPointParticle.Option laserPoint(Entity entity, Color color) {
        return new LaserPointParticle.Option(entity, color.getRGB(), color.getAlpha() / 255f);
    }

    public static SignalParticleOption signal(int strength, ItemStack blockingAgeItem) {
        return new SignalParticleOption(SIGNAL_PARTICLE.get(), strength, blockingAgeItem);
    }

    public static FallingLeafParticleOptions fallingLeaf(Color3 color3) {
        return new FallingLeafParticleOptions(FALLING_LEAVES.get(), color3, 0.07F, 10.0F, true, false, 2.0F, 0.021F);
    }

    public static FallingLeafParticleOptions fallingLeaf(Color3 color, float fallAcceleration, float sideAcceleration, boolean swirl, boolean flowAway, float scale, float startVelocity) {
        return new FallingLeafParticleOptions(FALLING_LEAVES.get(), color, fallAcceleration, sideAcceleration, swirl, flowAway, scale, startVelocity);
    }

    public static RibbonParticleOption ribbon(Entity target, int color, int segments, float length, float sizeY, float rotationRad) {
        return new RibbonParticleOption(target, color, segments, length, sizeY, rotationRad);
    }

    public static RibbonParticleOption ribbonWithOffset(Entity target, Vec3 positionOffset, int color, int segments, float length, float sizeY, float rotationRad) {
        return new RibbonParticleOption(target, color, segments, length, sizeY, rotationRad);
    }

    public static AgeableRibbonParticleOption ageableRibbon(Entity target, int color, int segments, float length, float sizeY, float rotationRad, int maxAge) {
        return new AgeableRibbonParticleOption(target, color, segments, length, sizeY, rotationRad, maxAge);
    }

    public static MultiColorRibbonParticleOption multiColorRibbon(Entity target, Color[] colors, int segments, float length, float sizeY, float rotationRad) {
        int[] array = Arrays.stream(colors).mapToInt(Color::getRGB).toArray();
        return new MultiColorRibbonParticleOption(target, array, segments, length, sizeY, rotationRad);
    }

    public static EntityModelFadeParticleOptions entityModelFade(Entity target, int color, float duration) {
        return new EntityModelFadeParticleOptions(target.getId(), color, duration);
    }

    private static <T extends ParticleOptions> RegistryObject<ParticleType<T>> register(String name, ParticleOptions.Deserializer<T> dec, final Function<ParticleType<T>, Codec<T>> fn) {
        return REGISTRY.register(name, () -> new ParticleType<>(false, dec) {
            public @NotNull Codec<T> codec() {
                return fn.apply(this);
            }
        });
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(THUNDER_SPARK.get(), ThunderSparkParticle.Provider::new);
        event.registerSpriteSet(LASER_POINT.get(), LaserPointParticle.Provider::new);
        event.registerSpriteSet(SOLVENT_PARTICLE.get(), SolventParticleParticle::provider);
        event.registerSpriteSet(SIGNAL_PARTICLE.get(), SignalParticle.Provider::new);
        event.registerSpriteSet(FALLING_LEAVES.get(), FallingLeafParticle.Provider::new);

        event.registerSpecial(ENTITY_MODEL_FADE.get(), new EntityModelFadeParticle.Provider());
        event.registerSpecial(THUNDER_PARTICLE.get(), new ThunderParticle.Provider());
        event.registerSpecial(ENTITY_LINKED_THUNDER_PARTICLE.get(), new EntityLinkedThunderParticle.Provider());

        event.registerSpecial(RIBBON.get(), new RibbonParticle.Provider());
        event.registerSpecial(AGEABLE_RIBBON.get(), new AgeableRibbonParticle.Provider());
        event.registerSpecial(MULTI_COLOR_RIBBON.get(), new MultiColorRibbonParticle.Provider());
    }
}
