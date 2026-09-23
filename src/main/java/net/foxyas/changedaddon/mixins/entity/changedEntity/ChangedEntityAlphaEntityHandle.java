package net.foxyas.changedaddon.mixins.entity.changedEntity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.boss.BehemothHead;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Alpha Mixin should only declare IAlphaAbleEntity
@Mixin(value = ChangedEntity.class, remap = false)
public abstract class ChangedEntityAlphaEntityHandle extends Monster implements IGrabberEntity, IAlphaAbleEntity {
    protected ChangedEntityAlphaEntityHandle(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    public abstract TransfurVariant<?> getSelfVariant();

    @Shadow
    public abstract @Nullable Player getUnderlyingPlayer();

    @Shadow
    public abstract LivingEntity maybeGetUnderlying();
    
    @Inject(method = "tick", at = @At("HEAD"), remap = true)
    private void AlphaAbleEntities$tickHook(CallbackInfo ci) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (self instanceof BehemothHead behemothHead) {
            if (behemothHead instanceof IAlphaAbleEntity iAlphaAbleEntity) {
                if (behemothHead.rightHand instanceof IAlphaAbleEntity alphaAbleEntity) {
                    alphaAbleEntity.setAlpha(iAlphaAbleEntity.isAlpha());
                }
                if (behemothHead.leftHand instanceof IAlphaAbleEntity alphaAbleEntity) {
                    alphaAbleEntity.setAlpha(iAlphaAbleEntity.isAlpha());
                }
            }
        }
    }

    @Inject(method = "mirrorLiving", at = @At("TAIL"), remap = false)
    private void AlphaAbleEntities$mirrorLivingHook(LivingEntity player, CallbackInfo ci) {
        if (this.getUnderlyingPlayer() == null || this.getUnderlyingPlayer() != player) return;

        ChangedEntity self = (ChangedEntity) (Object) this;
        AttributeInstance alphaScaleAttributeSelf = self.getAttribute(ChangedAddonAttributes.ALPHA_GENE_SCALE.get());
        AttributeInstance alphaScaleAttributePlayer = player.getAttribute(ChangedAddonAttributes.ALPHA_GENE_SCALE.get());

        if (alphaScaleAttributeSelf != null && alphaScaleAttributePlayer != null) {
            double selfValue = alphaScaleAttributeSelf.getValue();
            double playerValue = alphaScaleAttributePlayer.getValue();
            if (selfValue != playerValue) {
                alphaScaleAttributeSelf.replaceFrom(alphaScaleAttributePlayer);
                this.refreshDimensions();
                refreshAttributes(self);
                refreshAttributesForHost(self);
            }
        }
    }

    @Override
    public boolean isAlpha() {
        ChangedEntity self = (ChangedEntity) (Object) this;
        boolean originalValue = self.getEntityData().get(IS_ALPHA);
        if (this instanceof IOverrideAlphaState IOverrideAlphaState) {
            return IOverrideAlphaState.isConsiderateAlpha(originalValue);
        }
        return originalValue;
    }

    @Override
    public void setAlpha(boolean alpha) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (this.isAlpha() != alpha) {
            self.getEntityData().set(IS_ALPHA, alpha);
            this.refreshDimensions();
            refreshAttributes(self);
            refreshAttributesForHost(self);
        }
    }

    @Override
    public void setAlphaScale(float scale) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        AttributeInstance alphaScale = this.getAttribute(ChangedAddonAttributes.ALPHA_GENE_SCALE.get());
        if (alphaScale == null) return;
        if (this.alphaAdditionalScale() != scale) {
            alphaScale.setBaseValue(scale);
            this.refreshDimensions();
            refreshAttributes(self);
            refreshAttributesForHost(self);
        }
    }

    @Inject(method = "setTarget", at = @At("TAIL"), remap = true)
    private void AlphaAbleEntities$makeAlphaNotDespawnWhenTargetAPlayer(LivingEntity entity, CallbackInfo ci) {
        if ((entity instanceof Player || entity instanceof AbstractVillager) && this.isAlpha()) {
            this.setPersistenceRequired();
        }
    }

    @ModifyReturnValue(method = "savePlayerVariantData", at = @At("RETURN"))
    private CompoundTag AlphaAbleEntities$savePlayerVariantDataHook(CompoundTag original) {
        CompoundTag tag = original != null ? original : new CompoundTag();
        tag.putBoolean("isAlpha", isAlpha());
        return tag;
    }

    @Inject(method = "readPlayerVariantData", at = @At("RETURN"))
    private void AlphaAbleEntities$readPlayerVariantDataHook(CompoundTag tag, CallbackInfo ci) {
        if (tag == null) return;
        if (tag.contains("isAlpha")) setAlpha(tag.getBoolean("isAlpha"));
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"), remap = true)
    private void AlphaAbleEntities$defineSynchedDataHook(CallbackInfo ci) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        self.getEntityData().define(IS_ALPHA, false);
    }


    @Inject(method = "copyTraitsFrom", at = @At("TAIL"))
    private void AlphaAbleEntities$syncAlphaData(IAbstractChangedEntity entity, CallbackInfo ci) {
        var self = (ChangedEntity) (Object) this;
        if (self instanceof IAlphaAbleEntity to && entity.getChangedEntity() instanceof IAlphaAbleEntity from) {
            to.setAlpha(from.isAlpha());
            to.setAlphaScale(from.alphaAdditionalScale());
        }
    }



    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"), remap = true)
    public void AlphaAbleEntities$addAdditionalSaveDataHook(@NotNull CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("isAlpha", isAlpha());
//        tag.putFloat("alphaScale", alphaAdditionalScale());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"), remap = true)
    public void AlphaAbleEntities$readAdditionalSaveDataHook(@NotNull CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("isAlpha")) setAlpha(tag.getBoolean("isAlpha"));
//        if (tag.contains("alphaScale")) setAlphaScale(tag.getFloat("alphaScale"));
    }


}
