package net.foxyas.changedaddon.mixins.abilities;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SwitchGenderAbility;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(value = SwitchGenderAbility.class, remap = false)
public class SwitchGenderAbilityMixin {

    @WrapOperation(method = "lambda$startUsing$0", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/ability/IAbstractChangedEntity;replaceVariant(Lnet/ltxprogrammer/changed/entity/variant/TransfurVariant;)V"))
    private static void applyAlphaState(IAbstractChangedEntity source, TransfurVariant<?> oppositeVariant, Operation<Void> original){
        if (source == null) return;
        if (source.isPlayer() && source.getTransfurVariantInstance() == null) return;
        LivingEntity mainEntity = source.getEntity();
        ChangedEntity oldEntity = source.getChangedEntity();
        original.call(source, oppositeVariant);
        IAbstractChangedEntity abstractChangedEntity = IAbstractChangedEntity.forEither(mainEntity);
        if (abstractChangedEntity == null) return;
        if (abstractChangedEntity.isPlayer() && abstractChangedEntity.getTransfurVariantInstance() == null) return;

        if (abstractChangedEntity.getChangedEntity() instanceof IAlphaAbleEntity alphaTarget) {
            if (oldEntity instanceof IAlphaAbleEntity alphaSource) {
                alphaTarget.setAlpha(alphaSource.isAlpha());
                alphaTarget.setAlphaScale(alphaSource.alphaAdditionalScale());
            }
        }
    }
}
