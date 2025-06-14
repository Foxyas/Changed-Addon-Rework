package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.entity.goals.phantom.AvoidCatlikePlayerGoal;
import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Phantom.class)
public abstract class PhantomAIMixin {

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void addCustomAI(CallbackInfo ci) {
        Phantom thisFixed = ((Phantom) (Object) this);
        thisFixed.goalSelector.addGoal(3, new AvoidCatlikePlayerGoal(thisFixed));
    }
}
