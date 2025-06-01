package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.block.entity.DroppedSyringeBlockEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(value = DroppedSyringeBlockEntity.class)
public abstract class DroppedSyringeMixin {
    @Shadow
    private TransfurVariant<?> variant;
    @Unique
    private boolean changed_Addon_Rework$AllowBosses = false;

    @Inject(method = "getVariant", at = @At("RETURN"), cancellable = true, remap = false)
    private void checkAllowBossTag(CallbackInfoReturnable<TransfurVariant<?>> cir) {
        if (!changed_Addon_Rework$AllowBosses) {
            if (ChangedAddonTransfurVariants.getBossesVariantsList().contains(this.variant)) {
                List<TransfurVariant<?>> list = new ArrayList<>(TransfurVariant.getPublicTransfurVariants().toList());
                list.removeIf(transfurVariant -> ChangedAddonTransfurVariants.getBossesVariantsList().contains(transfurVariant));
                TransfurVariant<?> transfurVariant = list.get(new Random().nextInt(list.size()));
                this.variant = transfurVariant;
                cir.setReturnValue(transfurVariant);
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

