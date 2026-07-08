package net.foxyas.changedaddon.mixins.blocks;

import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.block.entity.DroppedSyringeBlockEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = DroppedSyringeBlockEntity.class)
public abstract class DroppedSyringeBlockEntityMixin extends BlockEntity {

    @Shadow(remap = false)
    private TransfurVariant<?> variant;
    @Unique
    private boolean changed_Addon_Rework$AllowBosses = false;

    public DroppedSyringeBlockEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "getVariant", at = @At("RETURN"), cancellable = true, remap = false)
    private void checkAllowBossTag(CallbackInfoReturnable<TransfurVariant<?>> cir) {
        if (this.variant == null || cir.getReturnValue() == null || level == null) {
            return;
        }

        if (!changed_Addon_Rework$AllowBosses) {
            List<TransfurVariant<?>> bossVariants = ChangedAddon$getBossVariants();
            if (bossVariants.contains(this.variant)) {
                List<TransfurVariant<?>> list = new ArrayList<>(TransfurVariant.getPublicTransfurVariants().toList());
                list.removeIf(bossVariants::contains);
                TransfurVariant<?> transfurVariant = list.get(this.level.getRandom().nextInt(list.size()));
                if (transfurVariant == null) return;
                this.variant = transfurVariant;
                cir.setReturnValue(transfurVariant);
            }
        }
    }

    @Unique
    private List<TransfurVariant<?>> ChangedAddon$getBossVariants() {
        return ChangedAddonTransfurVariants.getBossVariants(level);
    }

    @Inject(method = "load", at = @At("HEAD"))
    private void getDataMod(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("AllowBosses")) {
            this.changed_Addon_Rework$AllowBosses = tag.getBoolean("AllowBosses");
        }
    }

    @Inject(method = "saveAdditional", at = @At("HEAD"))
    private void AddDataMod(CompoundTag tag, CallbackInfo ci) {
        if (this.variant != null && ChangedAddon$getBossVariants().contains(this.variant)) {
            tag.putBoolean("AllowBosses", changed_Addon_Rework$AllowBosses);
        }
    }
}

