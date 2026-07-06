package net.foxyas.changedaddon.mobEffects;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;


public interface ICustomEffectInstance<T extends MobEffectInstance> {
    T load(MobEffectInstance mobEffectInstance, CompoundTag tag);

    void save(T instance, CompoundTag tag);
}