package net.foxyas.changedaddon.mixins;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolem.class)
public class IronGolemMixin {

    @Inject(method = "registerGoals", at = @At("TAIL"), cancellable = true)
    private void injectGoals(CallbackInfo ci) {
        IronGolem self = (IronGolem) (Object) this;
        self.targetSelector.getAvailableGoals().removeIf(wrappedGoal -> wrappedGoal.getPriority() == 2 && wrappedGoal.getGoal() instanceof NearestAttackableTargetGoal<?> attackableTargetGoal);
        self.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(self, Player.class, true, (livingEntity) -> {
            if (self.isPlayerCreated()) {
                return false;
            } else if (livingEntity instanceof Player player) {
                if (player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
                    return false;
                }
                return !ProcessTransfur.isPlayerNotLatex(player);
            } else {
                return false;
            }
        }));
    }
}
