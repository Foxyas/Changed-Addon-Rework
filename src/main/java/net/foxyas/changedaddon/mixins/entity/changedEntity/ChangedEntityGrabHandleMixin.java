package net.foxyas.changedaddon.mixins.entity.changedEntity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.entity.ai.goals.abilities.MayCauseGrabDamageGoal;
import net.foxyas.changedaddon.entity.ai.goals.abilities.MayDropGrabbedEntityGoal;
import net.foxyas.changedaddon.entity.ai.goals.abilities.MayGrabTargetGoal;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.mixins.abilities.AbilityControllerAccessor;
import net.foxyas.changedaddon.world.gamerules.WorldDifficulty;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.boss.Behemoth;
import net.ltxprogrammer.changed.entity.beast.boss.BehemothHand;
import net.ltxprogrammer.changed.entity.beast.boss.BehemothHead;
import net.ltxprogrammer.changed.entity.variant.EntityShape;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ChangedEntity.class, remap = false)
public abstract class ChangedEntityGrabHandleMixin extends Monster implements IGrabberEntity, IAlphaAbleEntity {

    @Unique
    protected GrabEntityAbilityInstance grabEntityAbilityInstance = null;
    private boolean appliedAlphaAttributes = false;
    private boolean appliedAlphaAttributesForHost = false;

    protected ChangedEntityGrabHandleMixin(EntityType<? extends Monster> type, Level pLevel) {
        super(type, pLevel);
    }

    @Shadow
    public abstract TransfurVariant<?> getSelfVariant();

    @Shadow
    public abstract @Nullable Player getUnderlyingPlayer();

    @Shadow
    public abstract LivingEntity maybeGetUnderlying();

    @Inject(at = @At("TAIL"), method = "<init>", cancellable = true)
    private void initHook(EntityType<? extends Monster> type, Level level, CallbackInfo ci) {
        if (ChangedAddonServerConfiguration.CAN_GRABBY_ENTITIES_SPAWN.get()) {
            if (this.getSelfVariant() != null) {
                ChangedEntity self = (ChangedEntity) (Object) this;
                if (self instanceof IGrabberCondition iGrabberCondition && !iGrabberCondition.isAffectedByGrab()) {
                    return;
                }

                List<? extends AbstractAbility<?>> listOfAbilities = this.getSelfVariant().abilities.stream().map((entityTypeFunction -> entityTypeFunction.apply(type))).toList();
                if (listOfAbilities.contains(ChangedAbilities.GRAB_ENTITY_ABILITY.get())) {
                    this.setCanUseGrab(level.getRandom().nextFloat() <= ChangedAddonServerConfiguration.GRABBY_ENTITIES_SPAWN_CHANCE.get()); // Just for fail-safe
                }
            }
        }

        if (canEntityGrab(type, level)) {
            this.grabEntityAbilityInstance = this.createGrabAbility();
        }
    }

    @Override
    public void setCanUseGrab(boolean value) {
        this.entityData.set(CAN_USE_GRAB, value);
    }

    @Override
    public boolean canUseGrab() {
        return this.entityData.get(CAN_USE_GRAB);
    }

    @Override
    public @Nullable GrabEntityAbilityInstance getGrabAbilityInstance() {
        return this.grabEntityAbilityInstance;
    }

    @Override
    public LivingEntity getGrabbedEntity() {
        return this.grabEntityAbilityInstance != null ? this.grabEntityAbilityInstance.grabbedEntity : null;
    }

    @Override
    public PathfinderMob asMob() {
        return this;
    }

    @Inject(at = @At("TAIL"), method = "registerGoals", remap = true, cancellable = true)
    private void goalsHook(CallbackInfo ci) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (self instanceof IGrabberCondition iGrabberCondition && !iGrabberCondition.isAffectedByGrab()) {
            return;
        }
        this.goalSelector.addGoal(10, new MayDropGrabbedEntityGoal(this));
        this.goalSelector.addGoal(10, new MayGrabTargetGoal(this));
        this.goalSelector.addGoal(10, new MayCauseGrabDamageGoal(this));
    }

    @Override
    public boolean canEntityGrab(EntityType<?> selfType, Level level) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (self instanceof IGrabberCondition iGrabberCondition && !iGrabberCondition.isAffectedByGrab()) {
            return false;
        }
        if (self.getEntityShape() == EntityShape.FERAL) {
            return false;
        }
        if (this.getUnderlyingPlayer() != null) {
            return false;
        }

        return selfType.is(ChangedAddonTags.EntityTypes.CAN_GRAB) || isAbleToGrab();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (canEntityGrab(this.getType(), level)) {
            if (grabEntityAbilityInstance == null) {
                this.grabEntityAbilityInstance = createGrabAbility(); // fail-safe
                return;
            }
            if (!this.level.isClientSide()) {
                if (this.getGrabbedEntity() == null) {
                    if (this.getGrabCooldown() > 0) this.setGrabCooldown(this.getGrabCooldown() - 1);
                }
            }
            this.mayTickGrabAbility();
        }

        ChangedEntity self = (ChangedEntity) (Object) this;
        if (self.getDimensions(self.getPose()).makeBoundingBox(self.position()) != self.getBoundingBox()) {
            this.refreshDimensions();
        }

        if (self instanceof IAlphaAbleEntity iAlphaAbleEntity) {
            if (iAlphaAbleEntity.isAlpha() && !appliedAlphaAttributes) {
                refreshAttributes(self);
                appliedAlphaAttributes = true;
            } else if (!iAlphaAbleEntity.isAlpha() && appliedAlphaAttributes) {
                refreshAttributes(self);
                appliedAlphaAttributes = false;
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), remap = true, cancellable = true)
    private void tickHook(CallbackInfo ci) {
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


    @Inject(method = "variantTick", at = @At("HEAD"), remap = false, cancellable = true)
    private void variantTickHook(CallbackInfo ci) {
        ChangedEntity self = (ChangedEntity) (Object) this;

        if (self instanceof IAlphaAbleEntity iAlphaAbleEntity) {
            if (iAlphaAbleEntity.isAlpha() && !appliedAlphaAttributesForHost) {
                refreshAttributesForHost(self);
                appliedAlphaAttributesForHost = true;
            } else if (!iAlphaAbleEntity.isAlpha() && appliedAlphaAttributesForHost) {
                refreshAttributesForHost(self);
                appliedAlphaAttributesForHost = false;
            }
        }
    }

    @Override
    public int getGrabCooldown() {
        return this.entityData.get(GRAB_COOLDOWN);
    }

    @Override
    public void setGrabCooldown(int grabCooldown) {
        GrabEntityAbilityInstance grabAbilityInstance = this.getGrabAbilityInstance();
        if (grabAbilityInstance != null) {
            AbstractAbility.Controller controller = grabAbilityInstance.getController();
            controller.forceCooldown(grabCooldown);
            if (controller instanceof AbilityControllerAccessor abilityControllerAccessor) {
                this.entityData.set(GRAB_COOLDOWN, abilityControllerAccessor.getCooldownTicksRemaining());
            }
        }
    }

    @Override
    public int getGrabMaxCooldown() {
        ChangedEntity self = (ChangedEntity) (Object) this;

        if (self instanceof Behemoth behemoth) {
            int appliedCooldown = 200;
            if (behemoth instanceof BehemothHand hand && (IAlphaAbleEntity.isEntityAlpha(hand.head) || IAlphaAbleEntity.isEntityAlpha(hand))) {
                return (int) (appliedCooldown * 1.5f);
            }
            if (behemoth instanceof BehemothHead head && IAlphaAbleEntity.isEntityAlpha(head)) {
                return (int) (appliedCooldown * 1.5f);
            }
            return appliedCooldown;
        }

        if (IAlphaAbleEntity.isEntityAlpha(self)) {
            return (int) (120 * 1.5);
        }

        return 120;
    }

    @Override
    protected void actuallyHurt(@NotNull DamageSource pDamageSource, float pDamageAmount) {
        if (canEntityGrab(this.getType(), level)) {
            mayDropGrabbedEntity(pDamageSource, pDamageAmount);
        }
        super.actuallyHurt(pDamageSource, pDamageAmount);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"), remap = true)
    public void addAdditionalSaveDataHook(@NotNull CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("canUseGrab", this.canUseGrab());
        if (canEntityGrab(this.getType(), level)) {
            this.saveGrabAbilityInTag(tag);
        }
        tag.putBoolean("isAlpha", isAlpha());
        tag.putFloat("alphaScale", alphaAdditionalScale());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"), remap = true)
    public void readAdditionalSaveDataHook(@NotNull CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("canUseGrab")) setCanUseGrab(tag.getBoolean("canUseGrab"));
        if (canEntityGrab(this.getType(), level)) {
            this.readGrabAbilityInTag(tag);
        }
        if (tag.contains("isAlpha")) setAlpha(tag.getBoolean("isAlpha"));
        if (tag.contains("alphaScale")) setAlphaScale(tag.getFloat("alphaScale"));
    }

    @Override
    public boolean isAbleToGrab() {
        EntityType<?> type = this.getType();
        if (type == ChangedEntities.BEHEMOTH_HEAD.get() || type == ChangedEntities.BEHEMOTH_HAND_LEFT.get() || type == ChangedEntities.BEHEMOTH_HAND_RIGHT.get()) {
            WorldDifficulty worldDifficulty = ChangedAddonServerConfiguration.BEHEMOTH_CAN_USE_GRAB_IN_DIFFICULTY.get();
            Difficulty levelDifficulty = level.getDifficulty();

            if (worldDifficulty == WorldDifficulty.NONE) {
                return this.canUseGrab() || isAlpha();
            } else if (worldDifficulty == WorldDifficulty.HARDCORE) {
                this.setCanUseGrab(level.getLevelData().isHardcore());
            }

            this.setCanUseGrab(levelDifficulty.getId() >= worldDifficulty.getAsLevel());
        }

        return this.canUseGrab() || isAlpha();
    }



    @Inject(method = "copyTraitsFrom", at = @At("TAIL"))
    private void syncAlphaData(IAbstractChangedEntity entity, CallbackInfo ci) {
        var self = (ChangedEntity) (Object) this;
        if (self instanceof IAlphaAbleEntity to && entity.getChangedEntity() instanceof IAlphaAbleEntity from) {
            to.setAlpha(from.isAlpha());
            to.setAlphaScale(from.alphaAdditionalScale());
        }
    }

    @ModifyReturnValue(method = "getAbilityInstance", at = @At("RETURN"))
    private <A extends AbstractAbilityInstance> A getAbilityInstanceHook(A original, AbstractAbility<A> ability) {
        if (canEntityGrab(this.getType(), level) && original == null)
            return (A) (this.grabEntityAbilityInstance != null && ability == this.grabEntityAbilityInstance.ability ? this.grabEntityAbilityInstance : original);
        return original;
    }

    @Override
    public boolean isAlpha() {
        ChangedEntity self = (ChangedEntity) (Object) this;
        return self.getEntityData().get(IS_ALPHA);
    }

    @Override
    public void setAlpha(boolean alpha) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (this.isAlpha() != alpha) {
            self.getEntityData().set(IS_ALPHA, alpha);
            this.refreshDimensions();
            refreshAttributes(self);
            refreshAttributesForHost(self);
            this.setPersistenceRequired();
        }
    }

    @Override
    public void setAlphaScale(float scale) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (this.alphaAdditionalScale() != scale) {
            self.getEntityData().set(ALPHA_SCALE, scale);
            this.refreshDimensions();
            refreshAttributes(self);
            refreshAttributesForHost(self);
            this.setPersistenceRequired();
        }
    }

    @Inject(method = "savePlayerVariantData", at = @At("RETURN"), cancellable = true)
    private void savePlayerVariantDataHook(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = cir.getReturnValue();
        if (tag == null) tag = new CompoundTag();//temporary fix so it doesnt crash
        tag.putBoolean("isAlpha", isAlpha());
        tag.putFloat("alphaScale", alphaAdditionalScale());
    }

    @Inject(method = "readPlayerVariantData", at = @At("RETURN"), cancellable = true)
    private void readPlayerVariantDataHook(CompoundTag tag, CallbackInfo ci) {
        if (tag == null) return;
        if (tag.contains("isAlpha")) setAlpha(tag.getBoolean("isAlpha"));
        if (tag.contains("alphaScale")) setAlphaScale(tag.getFloat("alphaScale"));
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"), remap = true, cancellable = true)
    private void defineSynchedDataHook(CallbackInfo ci) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        self.getEntityData().define(CAN_USE_GRAB, false);
        self.getEntityData().define(GRAB_COOLDOWN, 0);
        self.getEntityData().define(IS_ALPHA, false);
        self.getEntityData().define(ALPHA_SCALE, 0.75f);
    }

//
//
//    @WrapOperation(method = "tryFuseWithTarget",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;changeTransfur(Lnet/minecraft/world/entity/LivingEntity;Lnet/ltxprogrammer/changed/entity/variant/TransfurVariant;)Lnet/minecraft/world/entity/LivingEntity;",
//                    ordinal = 2
//            )
//    )
//    private LivingEntity hook(LivingEntity livingEntity, TransfurVariant<?> fusionVariant, Operation<LivingEntity> original) {
//        LivingEntity originalEntity = original.call(livingEntity, fusionVariant);
//        if (originalEntity instanceof IAlphaAbleEntity iAlphaAbleEntity) {
//            iAlphaAbleEntity.setAlpha(this.isAlpha());
//            iAlphaAbleEntity.setAlphaScale(this.alphaAdditionalScale());
//        } else if (originalEntity instanceof Player player) {
//            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
//            if (transfurVariant != null && transfurVariant.getChangedEntity() instanceof IAlphaAbleEntity iAlphaAbleEntity) {
//                iAlphaAbleEntity.setAlpha(this.isAlpha());
//                iAlphaAbleEntity.setAlphaScale(this.alphaAdditionalScale());
//            }
//        }
//        return originalEntity;
//    }
//
//    @WrapOperation(method = "tryFuseWithTarget",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;changeTransfur(Lnet/minecraft/world/entity/LivingEntity;Lnet/ltxprogrammer/changed/entity/variant/TransfurVariant;)Lnet/minecraft/world/entity/LivingEntity;",
//                    ordinal = 0
//            )
//    )
//    private LivingEntity playerFuseWithOtherPlayer(LivingEntity player,
//                                                   TransfurVariant<?> fusionVariant,
//                                                   Operation<LivingEntity> original,
//                                                   @Local(argsOnly = true) LivingEntity target,
//                                                   @Local(argsOnly = true) IAbstractChangedEntity source,
//                                                   @Local(argsOnly = true) float amount) {
//        ChangedEntity self = (ChangedEntity) (Object) this;
//        LivingEntity call = original.call(player, fusionVariant);
//        var event = new TransfurVariantEvents.OnPlayerFuseWithOther(target, player, self, fusionVariant);
//        ChangedAddonMod.postEvent(event);
//        return call;
//    }
//
//    @WrapOperation(method = "tryFuseWithTarget",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;changeTransfur(Lnet/minecraft/world/entity/LivingEntity;Lnet/ltxprogrammer/changed/entity/variant/TransfurVariant;)Lnet/minecraft/world/entity/LivingEntity;",
//                    ordinal = 1
//            )
//    )
//    private LivingEntity playerFuseWithEntity(LivingEntity player,
//                                              TransfurVariant<?> fusionVariant,
//                                              Operation<LivingEntity> original,
//                                              @Local(argsOnly = true) LivingEntity target,
//                                              @Local(argsOnly = true) IAbstractChangedEntity source,
//                                              @Local(argsOnly = true) float amount) {
//        ChangedEntity self = (ChangedEntity) (Object) this;
//        LivingEntity call = original.call(player, fusionVariant);
//        var event = new TransfurVariantEvents.OnPlayerFuseWithOther(target, player, self, fusionVariant);
//        ChangedAddonMod.postEvent(event);
//        return call;
//    }
//
//    @WrapOperation(method = "tryFuseWithTarget",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;changeTransfur(Lnet/minecraft/world/entity/LivingEntity;Lnet/ltxprogrammer/changed/entity/variant/TransfurVariant;)Lnet/minecraft/world/entity/LivingEntity;",
//                    ordinal = 2
//            )
//    )
//    private LivingEntity entityFuseWithOther(LivingEntity target,
//                                             TransfurVariant<?> fusionVariant,
//                                             Operation<LivingEntity> original,
//                                             @Local(argsOnly = true) IAbstractChangedEntity source,
//                                             @Local(argsOnly = true) float amount) {
//        ChangedEntity self = (ChangedEntity) (Object) this;
//        TransfurVariantInstance<?> oldVariantInstance = ProcessTransfur.getPlayerTransfurVariant(EntityUtil.playerOrNull(target));
//        LivingEntity call = original.call(target, fusionVariant);
//        var event = new TransfurVariantEvents.OnEntityFuseWithOther(target, oldVariantInstance, self, fusionVariant);
//        ChangedAddonMod.postEvent(event);
//        return call;
//    }

    //    @Override
//    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> pKey) {
//        super.onSyncedDataUpdated(pKey);
//        ChangedEntity self = (ChangedEntity) (Object) this;
//        if (pKey == IS_ALPHA || pKey == ALPHA_SCALE) {
//            this.refreshDimensions();
//            IAlphaAbleEntity.applyOrRemoveAlphaModifiers(self, entityData.get(IS_ALPHA), entityData.get(ALPHA_SCALE));
//            IAbstractChangedEntity.forEitherSafe(maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
//        }
//    }
}