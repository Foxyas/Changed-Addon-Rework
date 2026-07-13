package net.foxyas.changedaddon.ability.handle.dodgeTypes;

import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.EntityShape;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
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
    public void applyDodgeEffects(DodgeAbilityInstance instance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, boolean causeExhaustion) {
        super.applyDodgeEffects(instance, levelAccessor, dodger, attacker, dodgeType, causeExhaustion);
        instance.applyDodgeParticles(dodger, attacker);
        instance.applyDodgeAnimations(dodger);
    }

    @Override
    public boolean willApplyIFrames(DodgeAbilityInstance instance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, boolean causeExhaustion) {
        return true;
    }
}
