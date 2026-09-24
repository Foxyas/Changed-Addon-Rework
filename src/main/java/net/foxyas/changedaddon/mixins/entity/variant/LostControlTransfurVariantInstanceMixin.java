package net.foxyas.changedaddon.mixins.entity.variant;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.entity.api.IDynamicCamera;
import net.foxyas.changedaddon.mixins.entity.LivingEntityAccessor;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.client.LocalPlayerAccessor;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.PlayerDataExtension;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TransfurVariantInstance.class, remap = false)
public abstract class LostControlTransfurVariantInstanceMixin<T extends ChangedEntity> implements TransfurVariantInstanceExtensor {

    @Unique
    protected boolean hasControlOverBody = true;

    @Unique
    protected T entityInControl = null;

    @Shadow
    @Final
    protected TransfurVariant<ChangedEntity> parent;
    @Shadow
    @Final
    private Player host;

    @Shadow
    public abstract TransfurVariant<?> getParent();

    @Shadow
    public abstract boolean shouldApplyAbilities();

    @Shadow
    public abstract ChangedEntity getChangedEntity();

    @Shadow
    public abstract boolean isTemporaryFromSuit();

    @Shadow
    public abstract Player getHost();

    @Shadow
    @Final
    protected T entity;

    @Shadow
    public abstract float getTransfurProgression(float partial);


    @Shadow
    public abstract void setDead();

    @Override
    public void setControlOverBody(boolean controlOverBody) {
        this.hasControlOverBody = controlOverBody;
        maySendDataUpdate();
    }

    @Override
    public ChangedEntity getChangedEntityInControl() {
        return entityInControl;
    }

    @Override
    public void setChangedEntityInControl(ChangedEntity changedEntity) {
        this.entityInControl = (T) changedEntity;
    }

    @Override
    public boolean hasControlOverBody() {
        return hasControlOverBody;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/entity/variant/TransfurVariantInstance;checkForTemporary()Z"))
    private boolean checkForTemporaryHook(TransfurVariantInstance<?> instance, Operation<Boolean> original) {
        if (!ProcessTransfur.isPlayerTransfurred(this.host)) return original.call(instance);

        if (!hasControlOverBody && !host.isSpectator()) {
            if (this.entityInControl != null) {
                if (this.entityInControl.isRemoved()) {
                    this.entityInControl = null;
                    this.ChangedAddon$generateEntityInControl();
                    if (entityInControl == null) return original.call(instance);
                }

                if (this.entityInControl.isDeadOrDying()) {
                    if (entityInControl.getLastDamageSource() != null) {
                        this.getHost().hurt(entityInControl.getLastDamageSource(), Float.MAX_VALUE);
                    } else {
                        this.getHost().kill();
                    }
                    this.setDead();
                    this.entityInControl = null;
                    return original.call(instance);
                }

                if (this.getHost().level().isClientSide() && this.getHost() instanceof LocalPlayer localPlayer) {
                    ((LocalPlayerAccessor) localPlayer).setHandsBusy(true);
                }

                Player player = this.getHost();
                if (!entityInControl.isAddedToWorld() && !host.level().isClientSide()) {
                    if (!player.level().addFreshEntity(entityInControl)) {
                        entityInControl.setUUID(Mth.createInsecureUUID(entityInControl.getRandom()));
                    }

                    maySendDataUpdate();
                }
                if (player instanceof ServerPlayer serverPlayer) {
                    if (serverPlayer instanceof IDynamicCamera iDynamicCamera) {
                        iDynamicCamera.setResetCameraOnShift(false);
                        serverPlayer.setCamera(entityInControl);
                    }
                }
                entityInControl.getBasicPlayerInfo().copyFrom(((PlayerDataExtension) player).getBasicPlayerInfo());
                player.setInvisible(true);
                player.setSilent(true);
                player.setHealth(entityInControl.getHealth());
                player.setLastHurtByMob(entityInControl.getLastHurtByMob());
                player.setLastHurtByPlayer(((LivingEntityAccessor)entityInControl).ChangedAddon$getLastHurtByPlayer());
                player.setLastHurtMob(entityInControl.getLastHurtMob());
//                Vec3 position = entityInControl.position();
//                player.teleportTo(position.x, position.y, position.z);
            } else if (this.getTransfurProgression(0) >= 1f) {
                if (!host.level().isClientSide()) {
                    ChangedAddon$generateEntityInControl();
                }
            }
        } else {
            if (entityInControl != null) {
                this.entityInControl.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
                this.entityInControl = null;
                Player player = this.getHost();
                player.setInvisible(player.isSpectator());
                player.setSilent(false);
                if (player instanceof ServerPlayer serverPlayer) {
                    if (serverPlayer instanceof IDynamicCamera iDynamicCamera) {
                        iDynamicCamera.setResetCameraOnShift(true);
                        serverPlayer.setCamera(null);
                    }
                }

                if (this.getHost().level().isClientSide() && this.getHost() instanceof LocalPlayer localPlayer) {
                    ((LocalPlayerAccessor) localPlayer).setHandsBusy(false);
                }
            }
        }

        return original.call(instance);
    }

    @Unique
    private void ChangedAddon$generateEntityInControl() {
        EntityType<?> type = this.entity.getType();
        Entity rawEntity = type.create(host.level());
        if (rawEntity instanceof ChangedEntity changedEntity) {
            CompoundTag entityData = entity.saveWithoutId(new CompoundTag());
            entityData.remove("UUID");
            changedEntity.load(entityData);
            ChangedAddon$copyHostSimpleData(changedEntity);
            entityInControl = (T) changedEntity;
        }
    }

    @Unique
    private void ChangedAddon$copyHostSimpleData(ChangedEntity entity) {
        Player host = this.getHost();
        entity.setLastHurtByMob(host.getLastHurtByMob());
        entity.setLastHurtByPlayer(((LivingEntityAccessor)host).ChangedAddon$getLastHurtByPlayer());
        entity.setLastHurtMob(host.getLastHurtMob());
    }

    @Inject(method = "unhookAll", at = @At("TAIL"))
    private void injectUnHookALl(Player player, CallbackInfo ci) {
        if (entityInControl != null) {
            this.entityInControl.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
            this.entityInControl = null;
            player.setInvisible(player.isSpectator());
            player.setSilent(false);
            if (player instanceof ServerPlayer serverPlayer) {
                if (serverPlayer instanceof IDynamicCamera iDynamicCamera) {
                    iDynamicCamera.setResetCameraOnShift(true);
                    serverPlayer.setCamera(null);
                }
            }

            if (this.getHost().level().isClientSide() && this.getHost() instanceof LocalPlayer localPlayer) {
                ((LocalPlayerAccessor) localPlayer).setHandsBusy(false);
            }
        }
    }


    @Inject(method = "save", at = @At("RETURN"))
    private void InjectData(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = cir.getReturnValue();
        tag.putBoolean("hasControlOverBody", hasControlOverBody);
        if (!this.hasControlOverBody && this.entityInControl != null) {
            CompoundTag entityInControlTag = new CompoundTag();
            boolean saved = entityInControl.saveAsPassenger(entityInControlTag);
            if (saved) tag.put("entityInControlData", entityInControlTag);
        }
    }

    @Inject(method = "load", at = @At("RETURN"))
    private void readInjectedData(CompoundTag tag, CallbackInfo cir) {
        if (tag.contains("hasControlOverBody")) hasControlOverBody = tag.getBoolean("hasControlOverBody");
        if (tag.contains("entityInControlData")) {
            CompoundTag entityInControlData = tag.getCompound("entityInControlData");
            Level level = host.level;
            if (level.isClientSide()) {
                Entity entityByUUID = PlayerUtil.GlobalEntityUtil.getEntityByUUID(level, entityInControlData.getUUID("UUID"));
                if (entityByUUID instanceof ChangedEntity changedEntity) {
                    this.entityInControl = (T) changedEntity;
                }
            } else {
                Entity spawnedRaw = EntityType.loadEntityRecursive(entityInControlData, level, entity -> {
                    entity.moveTo(host.getX(), host.getY(), host.getZ(), host.getYRot(), host.getXRot());
                    return entity;
                });
                if (spawnedRaw instanceof ChangedEntity changedEntity) {
                    ChangedAddon$copyHostSimpleData(changedEntity);
                    this.entityInControl = (T) changedEntity;
                }
            }
        }
    }
}
