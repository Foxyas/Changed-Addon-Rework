package net.foxyas.changedaddon.init;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;

public class ChangedAddonDamageSources {
    public static final DamageSource SOLVENT = new DamageSource("latex_solvent");

    public static EntityDamageSource mobAttack(LivingEntity mob) {
        return new EntityDamageSource("latex_solvent", mob);
    }

}
