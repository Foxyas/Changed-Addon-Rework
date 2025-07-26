package net.foxyas.changedaddon.init;

import com.mojang.serialization.Codec;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.client.animations.AnimationDefinitions;
import net.ltxprogrammer.changed.entity.animation.AnimationEvent;
import net.ltxprogrammer.changed.entity.animation.AnimationParameters;
import net.ltxprogrammer.changed.entity.animation.NoParameters;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ChangedAddonAnimationEvents {
    public static DeferredRegister<AnimationEvent<?>> REGISTRY = ChangedRegistry.ANIMATION_EVENTS.createDeferred(ChangedAddonMod.MODID);

    public static RegistryObject<AnimationEvent<NoParameters>> DODGE_LEFT = register("dodge_left", AnimationEvent.NO_PARAMETERS);

    public static RegistryObject<AnimationEvent<NoParameters>> DODGE_RIGHT = register("dodge_right", AnimationEvent.NO_PARAMETERS);

    private static <T extends AnimationParameters> RegistryObject<AnimationEvent<T>> register(String name, Codec<T> parameters) {
        return REGISTRY.register(name, () -> new AnimationEvent<>(parameters));
    }
}
