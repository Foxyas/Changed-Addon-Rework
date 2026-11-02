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
import net.foxyas.changedaddon.process.ProcessGrabbyEntities;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.EntityShape;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.minecraft.nbt.CompoundTag;
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

import java.util.List;

// Grab Mixin should only declare IGrabberEntity
@Mixin(value = ChangedEntity.class, remap = false)
public abstract class ChangedEntityGrabHandleMixin extends Monster implements IGrabberEntity, IAlphaAbleEntity {

    @Unique
    protected GrabEntityAbilityInstance grabEntityAbilityInstance = null;

    protected ChangedEntityGrabHandleMixin(EntityType<? extends Monster> type, Level pLevel) {
        super(type, pLevel);
    }

    @Shadow
    public abstract TransfurVariant<?> getSelfVariant();

    @Shadow
    public abstract @Nullable Player getUnderlyingPlayer();

    @Shadow
    public abstract LivingEntity maybeGetUnderlying();

    @Shadow
    private @Nullable Player underlyingPlayer;

    @Inject(at = @At("TAIL"), method = "<init>")
    // Todo: Remove This in 0.16.0
    private void initHook(EntityType<? extends Monster> type, Level level, CallbackInfo ci) {
        if (ChangedAddonServerConfiguration.CAN_GRABBY_ENTITIES_SPAWN.get()) {
            if (this.getSelfVariant() != null) {
                ChangedEntity self = (ChangedEntity) (Object) this;
                if (!ProcessGrabbyEntities.canSpawnWithGrabAbility(self)) {
                    return;
                }

                List<? extends AbstractAbility<?>> listOfAbilities = this.getSelfVariant().abilities.stream().map((entityTypeFunction -> entityTypeFunction.apply(type))).toList();
                if (listOfAbilities.contains(ChangedAbilities.GRAB_ENTITY_ABILITY.get())) {
                    this.setCanUseGrab(level.getRandom().nextFloat() <= ChangedAddonServerConfiguration.GRABBY_ENTITIES_SPAWN_CHANCE.get()); // Just for fail-safe
                }
            }
        }

        if (canEntityGrab(type, level)) {
            this.grabEntityAbilityInstance = this.makeGrabAbility();
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
    public void setGrabAbilityInstance(GrabEntityAbilityInstance instance) {
        this.grabEntityAbilityInstance = instance;
    }

    @Override
    public LivingEntity getGrabbedEntity() {
        return this.grabEntityAbilityInstance != null ? this.grabEntityAbilityInstance.grabbedEntity : null;
    }

    @Override
    public PathfinderMob asMob() {
        return this;
    }

    @Inject(at = @At("TAIL"), method = "registerGoals", remap = true)
    private void goalsHook(CallbackInfo ci) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (!ProcessGrabbyEntities.isAffectedByGrabAbility(self)) {
            return;
        }

        if (self instanceof IHasGrabberGoals iHasGrabberGoals) {
            iHasGrabberGoals.makeGrabberGoals();
            return;
        }

        this.goalSelector.addGoal(10, new MayDropGrabbedEntityGoal(this));
        this.goalSelector.addGoal(10, new MayCauseGrabDamageGoal(this));

        // Todo: Remove This in 0.16.0
        this.goalSelector.addGoal(10, new MayGrabTargetGoal(this));
    }

    @Override
    public boolean canEntityGrab(EntityType<?> selfType, Level level) {
        // Todo: Remove This in 0.16.0
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (!ProcessGrabbyEntities.isAffectedByGrabAbility(self)) {
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

        // Todo: Remove This in 0.16.0
        if (canEntityGrab(this.getType(), level)) {
            if (grabEntityAbilityInstance == null) {
                this.grabEntityAbilityInstance = makeGrabAbility(); // fail-safe
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
    }

    @Inject(method = "variantTick", at = @At("HEAD"), remap = false)
    private void variantTickHook(CallbackInfo ci) {
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
        return ProcessGrabbyEntities.getMaxGrabCooldownOf(self);
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
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"), remap = true)
    public void readAdditionalSaveDataHook(@NotNull CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("canUseGrab")) setCanUseGrab(tag.getBoolean("canUseGrab"));
        if (canEntityGrab(this.getType(), level)) {
            this.readGrabAbilityInTag(tag);
        }
    }

    @Override
    public boolean isAbleToGrab() {
        ChangedEntity self = (ChangedEntity) (Object) this;
        return this.canUseGrab() || ProcessGrabbyEntities.isAbleToUseGrab(self);
    }

    @ModifyReturnValue(method = "getAbilityInstance", at = @At("RETURN"))
    private <A extends AbstractAbilityInstance> A getAbilityInstanceHook(A original, AbstractAbility<A> ability) {
        if (canEntityGrab(this.getType(), level) && original == null)
            return (A) (this.grabEntityAbilityInstance != null && ability == this.grabEntityAbilityInstance.ability ? this.grabEntityAbilityInstance : original);
        return original;
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"), remap = true)
    private void defineSynchedDataHook(CallbackInfo ci) {
        ChangedEntity self = (ChangedEntity) (Object) this;
        self.getEntityData().define(CAN_USE_GRAB, false);
        self.getEntityData().define(GRAB_COOLDOWN, 0);
    }
}