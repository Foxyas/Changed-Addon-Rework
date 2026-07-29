package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.entity.ai.ChangedEntityFlyingMoveControl;
import net.foxyas.changedaddon.entity.ai.goals.RandomLandingGoal;
import net.foxyas.changedaddon.entity.ai.goals.ToggleFlightGoal;
import net.foxyas.changedaddon.entity.ai.goals.ToggleFlightModeForAttackingGoal;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.IDynamicInventoryRender;
import net.foxyas.changedaddon.entity.api.IDynamicRideOffsetEntity;
import net.foxyas.changedaddon.entity.api.IFlyableChangedEntity;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.variant.IVariantExtraStats;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbilityInstance;
import net.ltxprogrammer.changed.entity.AttributePresets;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.ai.LatexAssimilationDecision.Method;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexEntity;
import net.ltxprogrammer.changed.entity.beast.DarkLatexEntity;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.util.Color3;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DarkLatexYufengQueenEntity extends AbstractDarkLatexEntity implements IVariantExtraStats,
        GrabEntityAbilityExtensor.IOverrideGrabAbilityTargetConditions, IAlphaAbleEntity, IDynamicInventoryRender, IFlyableChangedEntity, IDynamicRideOffsetEntity {

    protected final SimpleAbilityInstance summonPups;

    protected MoveControl groundMoveControl;
    protected ChangedEntityFlyingMoveControl flyingMoveControl;

    public DarkLatexYufengQueenEntity(EntityType<? extends DarkLatexYufengQueenEntity> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        this.groundMoveControl = this.moveControl;
        this.flyingMoveControl = new ChangedEntityFlyingMoveControl(this, 20, true);
        summonPups = registerAbility(ability -> this.wantToSummon(), new SimpleAbilityInstance(ChangedAddonAbilities.SUMMON_DL_PUP.get(), IAbstractChangedEntity.forEntity(this)));
    }

    public static AttributeSupplier.Builder createLatexAttributes() {
        return ChangedEntity.createLatexAttributes().add(Attributes.FLYING_SPEED).add(ForgeMod.ENTITY_REACH.get()).add(ForgeMod.BLOCK_REACH.get());
    }

    @Override
    protected TransfurVariant<?> getTransfurVariant(Method method) {
        if (method == Method.ABSORPTION) {
            return super.getSelfVariant();
        }

        return ChangedTransfurVariants.DARK_LATEX_YUFENG.get();
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        AttributePresets.dragonLike(attributes);
        Objects.requireNonNull(attributes.getInstance(Attributes.MOVEMENT_SPEED)).setBaseValue(1.15f);
        Objects.requireNonNull(attributes.getInstance(ForgeMod.ENTITY_REACH.get())).setBaseValue(3.5F); // oh my LARD!!! it was a pain in the ass to figure out how to modify that attribute
        Objects.requireNonNull(attributes.getInstance(ForgeMod.BLOCK_REACH.get())).setBaseValue(5F);
        Objects.requireNonNull(attributes.getInstance(Attributes.ATTACK_DAMAGE)).setBaseValue(4F);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue(5f);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get())).setBaseValue(1.5F);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get())).setBaseValue(2F);
        Objects.requireNonNull(attributes.getInstance(Attributes.ATTACK_KNOCKBACK)).setBaseValue(1.5F);
        Objects.requireNonNull(attributes.getInstance(Attributes.MAX_HEALTH)).setBaseValue(40F);
        Objects.requireNonNull(attributes.getInstance(Attributes.ARMOR)).setBaseValue(8F);
        Objects.requireNonNull(attributes.getInstance(Attributes.ARMOR_TOUGHNESS)).setBaseValue(2F);
        Objects.requireNonNull(attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE)).setBaseValue(0.6F);
        Objects.requireNonNull(attributes.getInstance(Attributes.FLYING_SPEED)).setBaseValue(0.1);// player def 0.05
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        if (LatexType.getEntityLatexType(livingEntity) == ChangedLatexTypes.WHITE_LATEX.get()) {
            return false;
        } else {
            return super.targetSelectorTest(livingEntity);
        }
    }

    public boolean wantToSummon() {
        return getTarget() != null;
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.fromInt(0x3d3d3d);
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.ABSORPTION;
    }

    @Override
    public float getFlyingSpeed() {
        return isFlying() ? (float) this.getAttributeValue(Attributes.FLYING_SPEED) : super.getFlyingSpeed() * 1.5f;
    }

    @Override
    public void baseTick() {
        super.baseTick();
    }

    //    @Override
//    public void variantTick(Level level) {
//        super.variantTick(level);
//        GrabEntityAbilityInstance grab = getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
//
//        if (grab != null) {
//            if (grab instanceof GrabEntityAbilityExtensor extensor) extensor.setAllowGrabTransfurred(true);
//        }
//    }

    @Override
    public boolean canGrabEntity(LivingEntity livingTarget, GrabEntityAbilityInstance grabEntityAbilityInstance) {
        boolean couldGrabEntity = GrabEntityAbilityExtensor.IOverrideGrabAbilityTargetConditions.super.canGrabEntity(livingTarget, grabEntityAbilityInstance);
        if (!couldGrabEntity && EntityUtil.maybeGetOverlaying(livingTarget) instanceof ChangedEntity changedEntity && changedEntity instanceof DarkLatexEntity) {
            if (changedEntity instanceof WolfyEntity) {
                return false;
            }

            var selfAlpha = this;
            if (changedEntity instanceof IAlphaAbleEntity targetAlpha) {
                if (targetAlpha.isAlpha() && !selfAlpha.isAlpha()) {
                    return false;
                }
                if (targetAlpha.isAlpha() && selfAlpha.isAlpha()) {
                    return selfAlpha.alphaAdditionalScale() >= targetAlpha.alphaAdditionalScale();
                }
            }

            return !(changedEntity instanceof DarkLatexYufengQueenEntity);
        }

        return couldGrabEntity;
    }

    @Override
    public boolean isAlpha() {
        return true;
    }

    @Override
    public float alphaAdditionalScale() {
        return 0; // 0 is "use the normal size"
    }

    @Override
    public void setAlpha(boolean alpha) {
        if (!entityData.hasItem(IS_ALPHA)) {
            return;
        }

        if (this.isAlpha() != alpha) {
            this.getEntityData().set(IS_ALPHA, alpha);
            this.refreshDimensions();
            refreshAttributes(this);
            refreshAttributesForHost(this);
        }
    }

    @Override
    public void setAlphaScale(float scale) {
        if (!entityData.hasItem(ALPHA_SCALE)) {
            return;
        }

        if (this.alphaAdditionalScale() != scale) {
            this.getEntityData().set(ALPHA_SCALE, scale);
            this.refreshDimensions();
            refreshAttributes(this);
            refreshAttributesForHost(this);
        }
    }

    @Override
    public Vec3 getInventoryRenderScale() {
        return new Vec3(0.75f, 0.75f, 0.75f);
    }

    @Override
    public void setFlyingMode(boolean flying) {
        this.setChangedEntityFlag(0, flying);
        updateNavigationAndControl(isFlying());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("isFlying")) {
            setFlyingMode(tag.getBoolean("isFlying"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isFlying", isFlyingMode());
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        super.travel(pTravelVector);
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.getPose() == Pose.STANDING || this.getPose() == Pose.CROUCHING) {
            return super.getPassengersRidingOffset() + this.getTorsoYOffset(this) + (this.isCrouching() ? 1.2 : 1.15);
        }
        return getTorsoYOffsetForFallFly(this);
    }

    @Override
    public boolean isFlying() {
        return super.isFlying();
    }

    @Override
    public boolean isFlyingMode() {
        return this.isFlying();
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return super.createNavigation(pLevel);
    }

    protected @NotNull FlyingPathNavigation createFlyNavigation(@NotNull Level pLevel) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, pLevel);
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(true);
        flyingPathNavigation.setCanPassDoors(true);
        return flyingPathNavigation;
    }

    @Override
    public void updateNavigationAndControl(boolean flying) {
        if (flying) {
            this.moveControl = this.flyingMoveControl;
            this.navigation = createFlyNavigation(level);
            this.setNoGravity(true);
        } else {
            this.moveControl = this.groundMoveControl;
            this.navigation = createNavigation(level);
            this.setNoGravity(false);
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new WaterAvoidingRandomFlyingGoal(this, 0.3) {
            @Override
            public boolean canUse() {
                return isFlyingMode() && super.canUse();
            }
        });

        this.goalSelector.addGoal(1, new ToggleFlightGoal<>(this));
        this.goalSelector.addGoal(1, new ToggleFlightModeForAttackingGoal<>(this));
        this.goalSelector.addGoal(1, new RandomLandingGoal<>(this));
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, @NotNull DamageSource source) {
        if (this.isFlyingMode()) return false;
        return super.causeFallDamage(fallDistance, damageMultiplier, source);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
        if (!this.isFlyingMode()) {
            super.checkFallDamage(y, onGround, state, pos);
        }
    }

    @Override
    public void removeAlphaGoals() {
    }

    @Override
    public void addAlphaGoals() {
    }
}
