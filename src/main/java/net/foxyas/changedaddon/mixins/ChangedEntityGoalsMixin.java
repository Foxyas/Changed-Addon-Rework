package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.entity.CustomHandle.SleepingWithOwnerGoal;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.entity.beast.DarkLatexWolfPup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChangedEntity.class)
public class ChangedEntityGoalsMixin {

    @Inject(method = "registerGoals",at = @At("HEAD"))
    private void addExtraGoal(CallbackInfo ci){
        ChangedEntity thisFixed = ((ChangedEntity) (Object) this);
        if (thisFixed instanceof AbstractDarkLatexWolf){
            thisFixed.goalSelector.addGoal(5, new SleepingWithOwnerGoal.BipedSleepGoal(thisFixed, true, SleepingWithOwnerGoal.BipedSleepGoal.BedSearchType.NEAREST));
        } else if (thisFixed instanceof DarkLatexWolfPup){
            thisFixed.goalSelector.addGoal(5, new SleepingWithOwnerGoal(thisFixed, true));
        }
    }
}
