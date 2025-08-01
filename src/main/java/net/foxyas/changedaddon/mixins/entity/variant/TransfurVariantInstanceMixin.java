package net.foxyas.changedaddon.mixins.entity.variant;

import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.foxyas.changedaddon.item.DarkLatexCoatItem;
import net.foxyas.changedaddon.item.HazmatSuitItem;
import net.foxyas.changedaddon.variants.ExtraVariantStats;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TransfurVariantInstance.class, remap = false)
public abstract class TransfurVariantInstanceMixin {

    @Shadow
    public abstract TransfurVariant<?> getParent();

    @Shadow
    public abstract boolean shouldApplyAbilities();

    @Shadow
    @Final
    protected TransfurVariant<ChangedEntity> parent;

    @Shadow
    public abstract ChangedEntity getChangedEntity();

    @Shadow
    @Final
    private Player host;

    @Inject(method = "canWear", at = @At("HEAD"), cancellable = true)
    private void negateArmor(Player player, ItemStack itemStack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir) {
        if ((itemStack.getItem() instanceof HazmatSuitItem || itemStack.getItem() instanceof DarkLatexCoatItem) && slot.getType() == EquipmentSlot.Type.ARMOR) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "tickFlying", at = @At("TAIL"), cancellable = true)
    private void negateFly(CallbackInfo cir) {
        if (this.parent.canGlide && this.shouldApplyAbilities()) {
            if (!this.host.isCreative() && !this.host.isSpectator()) {
                if (this.getChangedEntity() instanceof ExtraVariantStats extraVariantStats) {
                    if (extraVariantStats.getFlyType() == ExtraVariantStats.FlyType.ONLY_FALL) {
                        if (this.host.getAbilities().flying || this.host.getAbilities().mayfly) {
                            this.host.getAbilities().mayfly = false;
                            this.host.getAbilities().flying = false;
                            this.host.onUpdateAbilities();
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    private void negateFlyInTick(CallbackInfo cir) {
        if (this.parent.canGlide && this.shouldApplyAbilities()) {
            if (!this.host.isCreative() && !this.host.isSpectator()) {
                if (this.getChangedEntity() instanceof ExtraVariantStats extraVariantStats) {
                    if (extraVariantStats.getFlyType() == ExtraVariantStats.FlyType.ONLY_FALL) {
                        if (this.host.getAbilities().flying || this.host.getAbilities().mayfly) {
                            this.host.getAbilities().mayfly = false;
                            this.host.getAbilities().flying = false;
                            this.host.onUpdateAbilities();
                        }
                    }
                }
            }
        }

    }

    @Inject(method = "save", at = @At("RETURN"), cancellable = true)
    private void InjectData(CallbackInfoReturnable<CompoundTag> cir) {
        if (this.getChangedEntity() instanceof AvaliEntity avaliEntity) {
            avaliEntity.saveColors(cir.getReturnValue());
        }
    }

    @Inject(method = "load", at = @At("RETURN"), cancellable = true)
    private void readInjectedData(CompoundTag tag, CallbackInfo cir) {
        loadTransfurColorData(tag);
    }


    @Unique
    private void loadTransfurColorData(CompoundTag tag){
        if (this.getChangedEntity() instanceof AvaliEntity avaliEntity) {
            avaliEntity.readColors(tag);
        }
    }



    /*@Inject(method = "canWear", at = @At("HEAD"), cancellable = true)
    private void negateArmorForms(Player player, ItemStack itemStack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir){
        if (this.getParent() == ChangedAddonTransfurVariants.LATEX_SNEP_FERAL_FORM.get() || this.getParent() == ChangedAddonTransfurVariants.LATEX_SNEP.get()){
            cir.setReturnValue(false);
        }
    }
*/
}
