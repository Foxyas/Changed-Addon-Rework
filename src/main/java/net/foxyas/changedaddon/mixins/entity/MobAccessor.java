package net.foxyas.changedaddon.mixins.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mob.class)
public interface MobAccessor {

    @Invoker("getAmbientSound")
    SoundEvent callGetAmbientSound();

    @Accessor("persistenceRequired")
    void setPersistenceRequired(boolean value);

    @Invoker("registerGoals")
    void registerAIGoals();

    @Accessor("bodyRotationControl")
    BodyRotationControl getBodyRotationControl();


}
