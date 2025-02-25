package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.configuration.ChangedAddonConfigsConfiguration;
import net.foxyas.changedaddon.entity.CustomHandle.SleepingWithOwnerGoal;
import net.foxyas.changedaddon.item.DarkLatexCoatItem;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.entity.beast.DarkLatexWolfPup;
import net.ltxprogrammer.changed.entity.beast.PhageLatexWolfFemale;
import net.ltxprogrammer.changed.entity.beast.PhageLatexWolfMale;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChangedEntity.class,remap = false)
public class ChangedEntityTargetSelectorMixin {

    @Inject(method = "targetSelectorTest", at = @At("HEAD"), cancellable = true)
    private void CancelTarget(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir){
        ItemStack Head = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack Chest = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (ChangedAddonConfigsConfiguration.DL_COAT_AFFECT_ALL.get()) {
            if (isDarkLatexCoat(Head) && isDarkLatexCoat(Chest)){
                //ChangedAddonMod.LOGGER.info("Evento cancelado: capacete e peitoral detectados");
                cir.setReturnValue(false);
            } else if (isDarkLatexCoat(Head) ^ isDarkLatexCoat(Chest)) {
                if (livingEntity.distanceTo((ChangedEntity) (Object) this) >= 4){
                    //ChangedAddonMod.LOGGER.info("Evento cancelado: item parcial detectado, distância > 4");
                    cir.setReturnValue(false);
                }
            }
        } else {
            if ((ChangedEntity) (Object) this instanceof AbstractDarkLatexWolf){
                if (isDarkLatexCoat(Head) && isDarkLatexCoat(Chest)){
                    //ChangedAddonMod.LOGGER.info("Evento cancelado: capacete e peitoral detectados");
                    cir.setReturnValue(false);
                } else if (isDarkLatexCoat(Head) ^ isDarkLatexCoat(Chest)) {
                    if (livingEntity.distanceTo((ChangedEntity) (Object) this) >= 4){
                        //ChangedAddonMod.LOGGER.info("Evento cancelado: item parcial detectado, distância > 4");
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }

    @Inject(method = "registerGoals", at = @At("HEAD"), cancellable = true)
    private void addExtraGoal(CallbackInfo ci){
        var thisFixed = ((ChangedEntity) (Object) this);
        if (thisFixed instanceof AbstractDarkLatexWolf){
            thisFixed.goalSelector.addGoal(5,new SleepingWithOwnerGoal.BipedSleepGoal(thisFixed,true, thisFixed.getLevel().getRandom()));
        } else if (thisFixed instanceof DarkLatexWolfPup){
            thisFixed.goalSelector.addGoal(5,new SleepingWithOwnerGoal(thisFixed , true));
        }
    }

    @Unique
    private static boolean isDarkLatexCoat(ItemStack itemStack) {
        return itemStack != null 
                && !itemStack.isEmpty() 
                && itemStack.getItem() instanceof DarkLatexCoatItem;
    }
}
