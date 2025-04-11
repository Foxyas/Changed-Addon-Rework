package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = "net.minecraft.world.entity.monster.Phantom.PhantomSweepAttackGoal")
public abstract class PhantomSweepAttackGoalMixin extends Goal {
    @Shadow private boolean isScaredOfCat;

    @Shadow @Final private Phantom this$0;

    @Shadow private int catSearchTick;

    @Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
    private void canContinueToUseInject(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingentity = this$0.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            if (this.canUse()) {
                if (this$0.tickCount > this.catSearchTick) {
                    this.catSearchTick = this$0.tickCount + 20;

                    List<LivingEntity> list = this$0.level.getEntitiesOfClass(
                            LivingEntity.class,
                            this$0.getBoundingBox().inflate(16.0D),
                            (e) -> {
                                if (e instanceof Player player) {
                                    return ProcessTransfur.getPlayerTransfurVariantSafe(player).map(transfurVariantInstance ->
                                            transfurVariantInstance.getParent().is(ChangedAddonTransfurVariants.TransfurVariantTags.CAT_LIKE)
                                                    || transfurVariantInstance.getParent().is(ChangedAddonTransfurVariants.TransfurVariantTags.LEOPARD_LIKE)
                                    ).orElse(false);
                                } else if (e instanceof ChangedEntity changedEntity && changedEntity.getSelfVariant() != null){
                                    return changedEntity.getSelfVariant().is(ChangedAddonTransfurVariants.TransfurVariantTags.CAT_LIKE)
                                            || changedEntity.getSelfVariant().is(ChangedAddonTransfurVariants.TransfurVariantTags.LEOPARD_LIKE);
                                }
                                return false;
                            }
                    );

                    for (LivingEntity entity : list) {
                        if (entity instanceof Player player) {
                            // Toca o som de hiss
                            this$0.level.playSound(
                                    null, // null = som global (todos ouvem)
                                    player.blockPosition(),
                                    SoundEvents.CAT_HISS,
                                    SoundSource.PLAYERS,
                                    1.0F,
                                    1.0F
                            );
                        }
                    }

                    this.isScaredOfCat = !list.isEmpty();
                }
                cir.setReturnValue(!this.isScaredOfCat);
            }
        }
    }
}
