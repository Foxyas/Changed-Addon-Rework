package net.foxyas.changedaddon.mixins.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Accessor("jumping")
    boolean isJumping();

    @Invoker("dropEquipment")
    void _dropEquipment();

    @Invoker("dropCustomDeathLoot")
    void _dropCustomDeathLoot(DamageSource source, int looting, boolean hitByPlayer);
}
