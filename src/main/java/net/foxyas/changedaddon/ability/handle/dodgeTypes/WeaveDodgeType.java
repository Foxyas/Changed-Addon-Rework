package net.foxyas.changedaddon.ability.handle.dodgeTypes;

import com.mojang.datafixers.util.Either;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.EntityShape;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;

public class WeaveDodgeType extends DodgeType {

    public static final WeaveDodgeType INSTANCE = new WeaveDodgeType();

    public WeaveDodgeType() {
        super();
    }

    @Override
    public boolean willPlayDodgeAnimation(LivingEntity dodger) {
        if (EntityUtil.maybeGetOverlaying(dodger) instanceof ChangedEntity changedEntity) {
            return changedEntity.getEntityShape() != EntityShape.FERAL;
        } else if (dodger instanceof ChangedEntity changedEntity) {
            return changedEntity.getEntityShape() != EntityShape.FERAL;
        }
        return true;
    }

    @Override
    public void applyDodgeEffects(DodgeAbilityInstance instance, @Nullable LivingEntity dodger, Either<DamageSource, Projectile> source, boolean causeExhaustion) {
        super.applyDodgeEffects(instance, dodger, source, causeExhaustion);
        instance.applyDodgeParticles(source);
        instance.applyDodgeAnimations(source);
    }

    @Override
    public boolean willApplyIFrames(DodgeAbilityInstance dodgeAbilityInstance, @Nullable LivingEntity dodger, Either<DamageSource, Projectile> sourceProjectileEither, boolean causeExhaustion) {
        return true;
    }

    @Override
    public float getDodgeUsageWithoutContext(DodgeAbilityInstance dodgeAbilityInstance) {
        return 0.1f;
    }
}
