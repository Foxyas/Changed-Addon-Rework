package net.foxyas.changedaddon.ability.handle.dodgeTypes;

import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DodgeType {
    public DodgeType() {
        super();
    }

    public void applyDodgeMovement(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, LivingEntity dodger, Entity attacker, double distance, Vec3 dodgePosBehind, boolean causeExhaustion) {
    }

    public void applyDodgeEffects(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, boolean causeExhaustion) {
        if (this.willApplyIFrames(dodgeAbilityInstance, levelAccessor, dodger, attacker, dodgeType, causeExhaustion) && dodger != null) {
            dodger.invulnerableTime = 20 * 3;
            dodger.hurtDuration = 20 * 3;
            dodger.hurtTime = dodger.hurtDuration;
            dodger.hurtMarked = false;
        }
    }

    public void tickIdle(DodgeAbilityInstance dodgeAbilityInstance) {
    }

    public boolean willApplyIFrames(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, boolean causeExhaustion) {
        return false;
    }

    public boolean willPlayDodgeAnimation(LivingEntity dodger) {
        return false;
    }

    public boolean willDodge(DodgeAbilityInstance dodgeAbilityInstance, Entity entity) {
        return dodgeAbilityInstance.canUse() && dodgeAbilityInstance.canKeepUsing() && dodgeAbilityInstance.isDodgeActive();
    }
}