package net.foxyas.changedaddon.effect.particles;

import com.mojang.serialization.Codec;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonParticles {
    private static final Map<ResourceLocation, ParticleType<?>> REGISTRY = new HashMap<>();
    public static final ParticleType<ThunderSparkOption> THUNDER_SPARK = register(ResourceLocation.parse(ChangedAddonMod.MODID, "thunder_spark"), ThunderSparkOption.DESERIALIZER, ThunderSparkOption::codec);
    public static final ParticleType<LaserPointParticle.Option> LAZER_POINT = register(ResourceLocation.parse(ChangedAddonMod.MODID, "laser_point"), LaserPointParticle.Option.DESERIALIZER, LaserPointParticle.Option::codec);

    public static ThunderSparkOption thunderSpark(int lifeSpam) {
        return new ThunderSparkOption(THUNDER_SPARK, lifeSpam);
    }

    public static LaserPointParticle.Option laserPoint(Entity entity, Color3 color, float alpha) {
        return new LaserPointParticle.Option(entity, color.toInt(), alpha);
    }

    public static LaserPointParticle.Option laserPoint(Entity entity, Color color) {
        return new LaserPointParticle.Option(entity, color.getRGB(), color.getAlpha());
    }


    private static <T extends ParticleOptions> ParticleType<T> register(ResourceLocation name, ParticleType<T> type) {
        type.setRegistryName(name);
        REGISTRY.put(name, type);
        return type;
    }

    private static <T extends ParticleOptions> ParticleType<T> register(ResourceLocation name, ParticleOptions.Deserializer<T> dec, final Function<ParticleType<T>, Codec<T>> fn) {
        var type = new ParticleType<T>(false, dec) {
            @Override
            public @NotNull Codec<T> codec() {
                return fn.apply(this);
            }
        };

        return register(name, type);
    }

    @SubscribeEvent
    public static void registerParticleTypes(RegistryEvent.Register<ParticleType<?>> event) {
        REGISTRY.forEach((name, entry) -> {
            event.getRegistry().register(entry);
        });
    }

    @SubscribeEvent
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        var engine = Minecraft.getInstance().particleEngine;
        engine.register(THUNDER_SPARK, ThunderSparkParticle.Provider::new);
        engine.register(LAZER_POINT, LaserPointParticle.Provider::new);
    }

}

