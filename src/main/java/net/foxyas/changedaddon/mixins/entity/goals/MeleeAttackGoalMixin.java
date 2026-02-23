package net.foxyas.changedaddon.mixins.entity.goals;

import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.ability.GrabEntityAbility;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MeleeAttackGoal.class)
public class MeleeAttackGoalMixin {

    @Shadow
    @Final
    protected PathfinderMob mob;

    @Inject(at = @At("HEAD"), method = "canUse", cancellable = true)
    public void canUseHook(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob instanceof IGrabberEntity grabber) {
            GrabEntityAbilityInstance grabAbilityInstance = grabber.getGrabAbilityInstance();
            if (grabAbilityInstance != null && grabAbilityInstance.grabbedEntity != null) {
                cir.setReturnValue(false);
                return;
            }
            return;
        }

        LivingEntity target = this.mob.getTarget();
        IAbstractChangedEntity iGrabber = GrabEntityAbility.getGrabber(target);
        if (iGrabber != null) {
            LivingEntity grabber = iGrabber.getEntity();
            if (!(grabber instanceof Player)) {

                if (grabber.is(mob)) {
                    cir.setReturnValue(false);
                    return;
                }

                if (grabber.getType().is(ChangedAddonTags.EntityTypes.CAN_GRAB) || grabber instanceof IGrabberEntity) {
                    if (grabber instanceof IGrabberEntity grabberEntity) {
                        boolean value = grabberEntity.shouldRespectGrab(mob);
                        cir.setReturnValue(!value);
                        return;
                    }

                    boolean value = mob.getType().is(ChangedAddonTags.EntityTypes.IGNORE_GRABBED_TARGETS);
                    cir.setReturnValue(!value);
                }
            }
        }
    }
}
