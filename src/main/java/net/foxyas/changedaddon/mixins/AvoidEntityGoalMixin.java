package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(AvoidEntityGoal.class)
public class AvoidEntityGoalMixin {

    @Shadow @Nullable protected LivingEntity toAvoid;

    @Shadow @Final protected PathfinderMob mob;

    @Inject(method = "canUse", at = @At("TAIL"), cancellable = true)
    public void preventAvoidLatexSnep(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob instanceof Ocelot || this.mob instanceof Cat){
            if (toAvoid != null && toAvoid instanceof Player player){
                if (ProcessTransfur.getPlayerTransfurVariant(player) != null) {
                    if (ProcessTransfur.getPlayerTransfurVariant(player).is(ChangedAddonTransfurVariants.LATEX_SNEP.get())){
                        // Cancela a IA de evitar o jogador
                        cir.setReturnValue(false);
                    } else if (ProcessTransfur.getPlayerTransfurVariant(player).getParent().is(ChangedAddonTags.TransfurTypes.CAT_LIKE)
                            || ProcessTransfur.getPlayerTransfurVariant(player).getParent().is(ChangedAddonTags.TransfurTypes.LEOPARD_LIKE)) {
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }
}
