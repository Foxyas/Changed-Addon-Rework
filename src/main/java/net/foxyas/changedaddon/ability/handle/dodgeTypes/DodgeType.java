package net.foxyas.changedaddon.ability.handle.dodgeTypes;

import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.Nullable;

public class DodgeType {
    public DodgeType() {
        super();
    }

    public void runDodge(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, LivingEntity dodger, Entity attacker, LivingAttackEvent event, double distance, Vec3 dodgePosBehind, boolean causeExhaustion) {
    }

    public void runDodgeEffects(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, @Nullable LivingAttackEvent event, boolean causeExhaustion) {
        if (this.shouldApplyIframes(dodgeAbilityInstance, levelAccessor, dodger, attacker, dodgeType, event, causeExhaustion) && dodger != null) {
            dodger.invulnerableTime = 20 * 3;
            dodger.hurtDuration = 20 * 3;
            dodger.hurtTime = dodger.hurtDuration;
            dodger.hurtMarked = false;
        }
    }

    public boolean shouldApplyIframes(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, @Nullable LivingAttackEvent event, boolean causeExhaustion) {
        return false;
    }

    public boolean shouldPlayDodgeAnimation(LivingEntity dodger) {
        return false;
    }
}