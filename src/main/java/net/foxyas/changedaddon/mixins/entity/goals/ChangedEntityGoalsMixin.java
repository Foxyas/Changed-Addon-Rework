package net.foxyas.changedaddon.mixins.entity.goals;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.entity.ai.goals.simple.FollowAndLookAtLaser;
import net.foxyas.changedaddon.entity.ai.goals.simple.SleepingWithOwnerGoal;
import net.foxyas.changedaddon.entity.api.ICrawlAndSwimAbleEntity;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.entity.beast.DarkLatexWolfPup;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
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

    @ModifyReturnValue(at = @At(value = "RETURN"), method = "makeFloatGoal", remap = false)
    private Goal floatGoalHook(Goal original) {
        ChangedEntity self = ChangedAddonChangedEntityGoalsMixin$getSelf();
        if (self instanceof ICrawlAndSwimAbleEntity swimAbleEntity) {
            return new FloatGoal(self) {
                @Override
                public boolean canUse() {
                    return super.canUse() && swimAbleEntity.shouldFloat();
                }
            };
        }
        return original;
    }

    private ChangedEntity ChangedAddonChangedEntityGoalsMixin$getSelf() {
        var self = (ChangedEntity) (Object) this;
        return self;
    }
}
