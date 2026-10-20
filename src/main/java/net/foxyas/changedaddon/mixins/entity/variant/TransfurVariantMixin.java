package net.foxyas.changedaddon.mixins.entity.variant;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.entity.api.ChangedEntityExtension;
import net.foxyas.changedaddon.entity.api.ISafeChangedEntity;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TransfurVariant.class, remap = false)
public class TransfurVariantMixin {

    @ModifyReturnValue(method = "shouldScareVillager", at = @At("RETURN"))
    private static boolean shouldScareVillagersHook(boolean original, ChangedEntity entity, AbstractVillager villager) {
        if (entity.getType().is(ChangedTags.EntityTypes.LATEX) && entity.hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
            return false;
        } else if (entity.getType().is(ChangedTags.EntityTypes.LATEX) && ChangedEntityExtension.of(entity).isPacified()) {
            return false;
        } else if (entity instanceof ISafeChangedEntity safeChanged) {
            boolean flag = safeChanged.shouldScareVillagers(entity, villager);
            if (flag && villager.isSleeping()) {
                villager.stopSleeping();
            }
            return flag;
        }
        if (original && villager.isSleeping()) {
            villager.stopSleeping();
        }
        return original;
    }


    @WrapOperation(
            method = "replaceEntity(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Lnet/ltxprogrammer/changed/ability/IAbstractChangedEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/ltxprogrammer/changed/entity/ChangedEntity;copyTraitsFrom(Lnet/ltxprogrammer/changed/ability/IAbstractChangedEntity;)V",
                    ordinal = 0
            )
    )
    private void removeUnderlyingPlayerFromEntity(ChangedEntity newEntity, IAbstractChangedEntity entity, Operation<Void> original) {
        original.call(newEntity, entity);
        if (newEntity.getUnderlyingPlayer() != null) newEntity.setUnderlyingPlayer(null);
    }
}
