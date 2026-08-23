package net.foxyas.changedaddon.init;

import com.mojang.serialization.Codec;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.animations.parameters.DodgeAnimationParameters;
import net.foxyas.changedaddon.client.model.animations.parameters.PatReactionAnimationParameters;
import net.foxyas.changedaddon.network.packet.S2CPlayAnimationAfterParticleFade;
import net.ltxprogrammer.changed.entity.animation.AnimationEvent;
import net.ltxprogrammer.changed.entity.animation.AnimationParameters;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.awt.*;

public class ChangedAddonAnimationEvents {

    public static final DeferredRegister<AnimationEvent<?>> REGISTRY = ChangedRegistry.ANIMATION_EVENTS.createDeferred(ChangedAddonMod.MODID);

    public static final RegistryObject<AnimationEvent<DodgeAnimationParameters>> DODGE_LEFT = register("dodge_left", DodgeAnimationParameters.CODEC);
    public static final RegistryObject<AnimationEvent<DodgeAnimationParameters>> DODGE_RIGHT = register("dodge_right", DodgeAnimationParameters.CODEC);
    public static final RegistryObject<AnimationEvent<DodgeAnimationParameters>> DODGE_WEAVE_LEFT = register("dodge_weave_left", DodgeAnimationParameters.CODEC);
    public static final RegistryObject<AnimationEvent<DodgeAnimationParameters>> DODGE_WEAVE_RIGHT = register("dodge_weave_right", DodgeAnimationParameters.CODEC);
    public static final RegistryObject<AnimationEvent<DodgeAnimationParameters>> DODGE_DOWN_LEFT = register("dodge_down_left", DodgeAnimationParameters.CODEC);
    public static final RegistryObject<AnimationEvent<DodgeAnimationParameters>> DODGE_DOWN_RIGHT = register("dodge_down_right", DodgeAnimationParameters.CODEC);
    public static final RegistryObject<AnimationEvent<PatReactionAnimationParameters>> PAT_REACTION = register("pat_reaction", PatReactionAnimationParameters.CODEC);

    private static <T extends AnimationParameters> RegistryObject<AnimationEvent<T>> register(String name, Codec<T> parameters) {
        return REGISTRY.register(name, () -> new AnimationEvent<>(parameters));
    }

    public static <T extends AnimationParameters> void broadcastEntityAnimationWithFade(LivingEntity livingEntity, Color color, Vec3 pos, Vec3 motion, float speed, int count, AnimationEvent<T> event, @Nullable T parameters) {
        if (!livingEntity.level().isClientSide) {
            ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), new S2CPlayAnimationAfterParticleFade<T>(livingEntity, color, pos, motion, speed, count, event, null, parameters));
        }
    }
}
