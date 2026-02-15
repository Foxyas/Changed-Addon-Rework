package net.foxyas.changedaddon.mixins.entity.goals;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.entity.api.ICrawlAndSwimAbleEntity;
import net.foxyas.changedaddon.entity.defaults.AbstractSemiAquaticEntity;
import net.foxyas.changedaddon.entity.goals.simple.FollowAndLookAtLaser;
import net.foxyas.changedaddon.entity.goals.simple.SleepingWithOwnerGoal;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.entity.beast.DarkLatexWolfPup;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChangedEntity.class)
public class ChangedEntityGoalsMixin {

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void addExtraGoal(CallbackInfo ci) {
        ChangedEntity thisFixed = ((ChangedEntity) (Object) this);
        if (thisFixed instanceof AbstractDarkLatexWolf) {
            thisFixed.goalSelector.addGoal(5, new SleepingWithOwnerGoal.BipedSleepGoal(thisFixed, true, SleepingWithOwnerGoal.BipedSleepGoal.BedSearchType.NEAREST));
        } else if (thisFixed instanceof DarkLatexWolfPup) {
            thisFixed.goalSelector.addGoal(5, new SleepingWithOwnerGoal(thisFixed, true));
        }
        if (thisFixed.getSelfVariant() != null
                && (thisFixed.getSelfVariant().is(ChangedAddonTags.TransfurTypes.CAT_LIKE)
                || thisFixed.getSelfVariant().is(ChangedAddonTags.TransfurTypes.LEOPARD_LIKE))) {
            thisFixed.goalSelector.addGoal(5, new FollowAndLookAtLaser(thisFixed, 0.4));
        }
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V", ordinal = 15), method = "registerGoals", remap = true)
    private void floatGoalHook(GoalSelector instance, int pPriority, Goal pGoal, Operation<Void> original) {
        ChangedEntity self = getSelf();
        if (self instanceof ICrawlAndSwimAbleEntity) {
            var FloatGoal = new FloatGoal(self) {
                @Override
                public boolean canUse() {
                    if (self.getTarget() == null) return super.canUse();
                    if (!self.isInWater() || self.getTarget() != null) return false;
                    return super.canUse();
                }
            };

            original.call(instance, pPriority, FloatGoal);
            return;
        } else if (self instanceof AbstractSemiAquaticEntity) {
//            var FloatGoal = new FloatGoal(self) {
//                @Override
//                public boolean canUse() {
//                    if (self.getTarget() != null) return false;
//                    return super.canUse();
//                }
//            };

            //original.call(instance, pPriority, FloatGoal);
            return;
        }

        original.call(instance, pPriority, pGoal);
    }

    private ChangedEntity getSelf() {
        var self = (ChangedEntity) (Object) this;
        return self;
    }
}
