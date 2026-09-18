package net.foxyas.changedaddon.mixins.entity.changedEntity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.foxyas.changedaddon.entity.api.IAlphaAbleEntity.IS_ALPHA;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityChangedEntityMixin extends Entity {

    protected LivingEntityChangedEntityMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static Entity resolveChangedEntity(Entity entity) {
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> transfur = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfur != null) {
                return transfur.getChangedEntity();
            }
        }
        return entity;
    }

//    @ModifyReturnValue(method = "getJumpPower", at = @At("RETURN"))
//    private float changedJumpPower(float original) {
//        var self = ChangedAddon$selfMixin();
//        Entity entity = resolveChangedEntity(self);
//        if (IAlphaAbleEntity.isEntityAlpha(entity)) {
//            return original * (1 + (0.25f * (IAlphaAbleEntity.getEntityAlphaScale(entity) / 0.75f)));
//        }
//        return original;
//    }

    @Inject(method = "onSyncedDataUpdated", at = @At("TAIL"))
    private void changedEntityOnSyncedDataUpdatedHook(EntityDataAccessor<?> pKey, CallbackInfo ci) {
        LivingEntity self = ChangedAddon$selfMixin();
        if (!(self instanceof ChangedEntity changedEntity)) return;
        if (changedEntity.level().isClientSide()) return;

        if (self.isDeadOrDying()) return;

        if (pKey == IS_ALPHA) {
            this.refreshDimensions();
            IAlphaAbleEntity.applyOrRemoveAlphaModifiers(self, entityData.get(IS_ALPHA), (float) changedEntity.getAttributeValue(ChangedAddonAttributes.ALPHA_GENE_SCALE.get()));
            IAbstractChangedEntity.forEitherSafe(changedEntity.maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
        }
    }

//
//    @ModifyReturnValue(method = "getAttribute", at = @At("RETURN"))
//    private AttributeInstance getUnderlyingAlphaSizeAttribute(AttributeInstance original, @Local(argsOnly = true) Attribute attribute) {
//        LivingEntity self = ChangedAddon$selfMixin();
//        if (!(self instanceof ChangedEntity changedEntity)) return original;
//        if (attribute == ChangedAddonAttributes.ALPHA_GENE_SCALE.get()) {
//            return EntityUtil.maybeGetUnderlying(changedEntity).getAttribute(attribute);
//        }
//        return original;
//    }

    @Unique
    private LivingEntity ChangedAddon$selfMixin() {
        return (LivingEntity) (Object) this;
    }
}
