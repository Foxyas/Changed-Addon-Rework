package net.foxyas.changedaddon.mixins.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mob.class)
public interface MobAccessor {

    @Invoker("getAmbientSound")
    SoundEvent callGetAmbientSound();
}
