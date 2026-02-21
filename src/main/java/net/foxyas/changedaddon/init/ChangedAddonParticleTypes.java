package net.foxyas.changedaddon.init;

import com.mojang.serialization.Codec;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.particle.*;
import net.foxyas.changedaddon.effect.particles.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.function.Function;


@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonParticleTypes {

    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ChangedAddonMod.MODID);

    public static final RegistryObject<ParticleType<SimpleParticleType>> SOLVENT_PARTICLE = REGISTRY.register("solvent_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<ThunderSparkOption>> THUNDER_SPARK = register("thunder_spark", ThunderSparkOption.DESERIALIZER, ThunderSparkOption::codec);
    public static final RegistryObject<ParticleType<SignalParticleOption>> SIGNAL_PARTICLE = register("signal_particle", SignalParticleOption.DESERIALIZER, SignalParticleOption::codec);
    public static final RegistryObject<ParticleType<LaserPointParticle.Option>> LASER_POINT = register("laser_point", LaserPointParticle.Option.DESERIALIZER, LaserPointParticle.Option::codec);

    public static final RegistryObject<ParticleType<EntityModelFadeParticleOptions>> ENTITY_MODEL_FADE = register("entity_model_fade", EntityModelFadeParticleOptions.DESERIALIZER, EntityModelFadeParticleOptions::codec);

    public static final RegistryObject<ParticleType<RibbonParticleOptions>> RIBBON = register("ribbon", RibbonParticleOptions.DESERIALIZER, RibbonParticleOptions::codec);
    public static final RegistryObject<ParticleType<AgeableRibbonParticleOptions>> AGEABLE_RIBBON = register("ageable_ribbon", AgeableRibbonParticleOptions.DESERIALIZER, AgeableRibbonParticleOptions::codec);
    public static final RegistryObject<ParticleType<MultiColorRibbonParticleOptions>> MULTICOLOR_RIBBON = register("multicolor_ribbon", MultiColorRibbonParticleOptions.DESERIALIZER, MultiColorRibbonParticleOptions::codec);

    public static ThunderSparkOption thunderSpark(int lifespan) {
        return new ThunderSparkOption(THUNDER_SPARK.get(), lifespan);
    }

    public static LaserPointParticle.Option laserPoint(Entity entity, Color color) {
        return new LaserPointParticle.Option(entity, color.getRGB(), color.getAlpha() / 255f);
    }

    public static SignalParticleOption signal(int strength, ItemStack blockingAgeItem) {
        return new SignalParticleOption(SIGNAL_PARTICLE.get(), strength, blockingAgeItem);
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
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;

        engine.register(THUNDER_SPARK.get(), ThunderSparkParticle.Provider::new);
        engine.register(LASER_POINT.get(), LaserPointParticle.Provider::new);
        engine.register(SIGNAL_PARTICLE.get(), SignalParticle.Provider::new);
        engine.register(SOLVENT_PARTICLE.get(), SolventParticleParticle::provider);

        engine.register(ENTITY_MODEL_FADE.get(), new EntityModelFadeParticle.Provider());
        engine.register(RIBBON.get(), new RibbonParticle.Provider());
        engine.register(AGEABLE_RIBBON.get(), new AgeableRibbonParticle.Provider());
        engine.register(MULTICOLOR_RIBBON.get(), new MultiColorRibbonParticle.Provider());
    }
}
