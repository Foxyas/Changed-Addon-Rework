package net.foxyas.changedaddon.ability.handle.dodgeTypes;

import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.EntityShape;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.Nullable;

public class WeaveDodgeType extends DodgeType {

    public static final WeaveDodgeType INSTANCE = new WeaveDodgeType();

    public WeaveDodgeType() {
        super();
    }

    @Override
    public boolean shouldPlayDodgeAnimation(LivingEntity dodger) {
        if (EntityUtil.maybeGetOverlaying(dodger) instanceof ChangedEntity changedEntity) {
            return changedEntity.getEntityShape() != EntityShape.FERAL;
        } else if (dodger instanceof ChangedEntity changedEntity) {
            return changedEntity.getEntityShape() != EntityShape.FERAL;
        }
        return true;
    }

    @Override
    public void runDodgeEffects(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, @Nullable LivingAttackEvent event, boolean causeExhaustion) {
        super.runDodgeEffects(dodgeAbilityInstance, levelAccessor, dodger, attacker, dodgeType, event, causeExhaustion);
        dodgeAbilityInstance.executeDodgeParticles(levelAccessor, dodger, attacker);
        dodgeAbilityInstance.executeDodgeAnimations(dodger);
    }

    @Override
    public boolean shouldApplyIframes(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, @Nullable LivingAttackEvent event, boolean causeExhaustion) {
        return true;
    }
}
