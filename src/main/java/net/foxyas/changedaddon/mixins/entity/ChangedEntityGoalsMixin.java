package net.foxyas.changedaddon.mixins.entity;

import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.entity.goals.FollowAndLookAtLaser;
import net.foxyas.changedaddon.entity.goals.SleepingWithOwnerGoal;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.entity.beast.DarkLatexWolfPup;
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
}
