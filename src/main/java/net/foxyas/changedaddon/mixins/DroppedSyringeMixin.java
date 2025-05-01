package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.block.entity.DroppedSyringeBlockEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DroppedSyringeBlockEntity.class)
public abstract class DroppedSyringeMixin {
    @Shadow
    private TransfurVariant<?> variant;
    @Unique
    private boolean changed_Addon_Rework$AllowBosses = false;

    @Inject(method = "getVariant", at = @At("RETURN"), cancellable = true, remap = false)
    private void checkAllowBossTag(CallbackInfoReturnable<TransfurVariant<?>> cir) {
        if (!changed_Addon_Rework$AllowBosses) {
            if (this.variant == ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS.get()) {
                this.variant = ChangedAddonTransfurVariants.EXPERIMENT_10.get();
                cir.setReturnValue(ChangedAddonTransfurVariants.EXPERIMENT_10.get());
            } else if (this.variant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get()) {
                this.variant = ChangedAddonTransfurVariants.KET_EXPERIMENT_009.get();
                cir.setReturnValue(ChangedAddonTransfurVariants.KET_EXPERIMENT_009.get());
            }
        }
    }

    @Inject(method = "load", at = @At("HEAD"))
    private void getDataMod(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("AllowBosses")) {
            this.changed_Addon_Rework$AllowBosses = tag.getBoolean("AllowBosses");
        }
    }

    @Inject(method = "saveAdditional", at = @At("HEAD"))
    private void AddDataMod(CompoundTag tag, CallbackInfo ci) {
        if (this.variant != null && ChangedAddonTransfurVariants.getBossesVariantsList().contains(this.variant)) {
            tag.putBoolean("AllowBosses", changed_Addon_Rework$AllowBosses);
        }
    }
}

