package net.foxyas.changedaddon.mixins.entity.changedEntity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.entity.goals.abilities.MayCauseGrabDamageGoal;
import net.foxyas.changedaddon.entity.goals.abilities.MayDropGrabbedEntityGoal;
import net.foxyas.changedaddon.entity.goals.abilities.MayGrabTargetGoal;
import net.foxyas.changedaddon.init.ChangedAddonTags;
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
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Monster;
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

    @Shadow public abstract TransfurVariant<?> getSelfVariant();

    @Shadow public abstract LivingEntity maybeGetUnderlying();

    @Unique
    protected GrabEntityAbilityInstance grabEntityAbilityInstance = null;
    @Unique
    protected int grabCooldown = 0;
    @Unique
    protected boolean ableToGrab;

    protected ChangedEntityGrabHandleMixin(EntityType<? extends Monster> type, Level pLevel) {
        super(type, pLevel);
    }

    @Inject(at = @At("TAIL"), method = "<init>", cancellable = true)
    private void initHook(EntityType<? extends Monster> type, Level level, CallbackInfo ci) {
        if (this.getSelfVariant() != null) {
            List<? extends AbstractAbility<?>> listOfAbilities = this.getSelfVariant().abilities.stream().map((entityTypeFunction -> entityTypeFunction.apply(type))).toList();
            if (listOfAbilities.contains(ChangedAbilities.GRAB_ENTITY_ABILITY.get())) {
                this.ableToGrab = level.getRandom().nextFloat() <= 0.15f; // Just for fail-safe
            }
        }
        if (canEntityGrab(type, level)) {
            this.grabEntityAbilityInstance = this.createGrabAbility();
        }
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
        this.goalSelector.addGoal(10, new MayDropGrabbedEntityGoal(this));
        this.goalSelector.addGoal(10, new MayGrabTargetGoal(this));
        this.goalSelector.addGoal(10, new MayCauseGrabDamageGoal(this));
    }

    @Override
    public boolean canEntityGrab(EntityType<?> type, Level level) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (self.getEntityShape() == EntityShape.FERAL) {
            return false;
        }

        return type.is(ChangedAddonTags.EntityTypes.CAN_GRAB) || isAbleToGrab();
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (canEntityGrab(this.getType(), level)) {
            if (grabEntityAbilityInstance == null) {
                this.grabEntityAbilityInstance = createGrabAbility(); // fail-safe
                return;
            }
            if (grabEntityAbilityInstance.grabbedEntity == null) {
                if (grabCooldown > 0) this.grabCooldown--;
            }
            this.mayTickGrabAbility();
        }

        ChangedEntity self = (ChangedEntity) (Object) this;
        if (self.getDimensions(self.getPose()).makeBoundingBox(self.position()) != self.getBoundingBox()) {
            this.refreshDimensions();
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

    @Inject(method = "variantTick", at = @At("HEAD"), remap = true, cancellable = true)
    private void variantTickHook(CallbackInfo ci) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (self instanceof IAlphaAbleEntity iAlphaAbleEntity) {
            //Todo sync Alpha variant Attributes for the transfured player
        }
    }

    @Override
    public int getGrabCooldown() {
        return grabCooldown;
    }

    @Override
    public void setGrabCooldown(int grabCooldown) {
        this.grabCooldown = grabCooldown;
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

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (canEntityGrab(this.getType(), level)) {
            this.saveGrabAbilityInTag(tag);
        }
        tag.putBoolean("isAlpha", isAlpha());
        tag.putFloat("alphaScale", alphaAdditionalScale());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
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
            this.ableToGrab = level.getDifficulty().equals(Difficulty.HARD);
        }

        return ableToGrab || isAlpha();
    }

    @ModifyReturnValue(method = "getAbilityInstance", at = @At("RETURN"))
    private <A extends AbstractAbilityInstance> A getAbilityInstanceHook(A original, AbstractAbility<A> ability) {
        if (canEntityGrab(this.getType(), level)) return (A) (this.grabEntityAbilityInstance != null && ability == this.grabEntityAbilityInstance.ability ? this.grabEntityAbilityInstance : original);
        return original;
    }

    @Override
    public void setAlpha(boolean alpha) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (this.isAlpha() != alpha) {
            self.getEntityData().set(IS_ALPHA, alpha);
            this.refreshDimensions();
            refreshAttributes(self);
        }
    }

    @Override
    public boolean isAlpha() {
        ChangedEntity self = (ChangedEntity) (Object) this;
        return self.getEntityData().get(IS_ALPHA);
    }

    @Override
    public void setAlphaScale(float scale) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (this.alphaAdditionalScale() != scale) {
            self.getEntityData().set(ALPHA_SCALE, scale);
            this.refreshDimensions();
            refreshAttributes(self);
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
        self.getEntityData().define(IS_ALPHA, false);
        self.getEntityData().define(ALPHA_SCALE, 0.75f);
    }

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