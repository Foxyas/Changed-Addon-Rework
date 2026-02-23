package net.foxyas.changedaddon.mixins.entity;

import net.foxyas.changedaddon.entity.ai.goals.simple.FollowAndLookAtLaser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimal {

    protected CatMixin(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(at = @At("HEAD"), method = "registerGoals")
    private void onRegisterGoals(CallbackInfo ci) {
        goalSelector.addGoal(5, new FollowAndLookAtLaser(this, 1.2));
    }
}
