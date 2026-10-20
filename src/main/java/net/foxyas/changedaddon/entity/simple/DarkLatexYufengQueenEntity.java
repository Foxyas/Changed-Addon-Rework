package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.entity.ai.FlyingApproachMeleeAttackGoal;
import net.foxyas.changedaddon.entity.ai.FlyingOrWalkingMoveControl;
import net.foxyas.changedaddon.entity.ai.RandomStrollOrHoverGoal;
import net.foxyas.changedaddon.entity.api.*;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.init.ChangedAddonAttributes;
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
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DarkLatexYufengQueenEntity extends AbstractDarkLatexEntity implements IVariantExtraStats,
        GrabEntityAbilityExtensor.IOverrideGrabAbilityTargetConditions, IAlphaAbleEntity, IDynamicInventoryRender, IFlyingCapable, IDynamicRideOffsetEntity {

    protected final FlyingPathNavigation flyingNavigation;
    protected final GroundPathNavigation groundNavigation;

    protected final SimpleAbilityInstance summonPups;

    public DarkLatexYufengQueenEntity(EntityType<? extends DarkLatexYufengQueenEntity> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        this.flyingNavigation = new FlyingPathNavigation(this, level);
        this.flyingNavigation.setCanOpenDoors(true);
        this.flyingNavigation.setCanFloat(true);
        this.flyingNavigation.setCanPassDoors(true);
        this.groundNavigation = new GroundPathNavigation(this, level);
        this.groundNavigation.setCanOpenDoors(true);
        this.navigation = groundNavigation;
        this.moveControl = new FlyingOrWalkingMoveControl<>(this, 20, true);

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
    protected @Nullable Goal makeMeleeTransfurGoal() {
        return new FlyingApproachMeleeAttackGoal<>(this, 0.4, false, 6, 4);
    }

    @Override
    protected @Nullable Goal makeWanderGoal() {
        return new RandomStrollOrHoverGoal<>(this, 0.3, 120, false,
                /* walkingTicks */ UniformInt.of(120, 1200),
                /* flyingTicks */ UniformInt.of(120, 1200));
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
        Objects.requireNonNull(attributes.getInstance(Attributes.FLYING_SPEED)).setBaseValue(0.15F);// player def 0.05, 150% of 0.05 is 0.075, but for better npc movement we will use 0.15
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
        if (this.getUnderlyingPlayer() != null) {
            return super.getFlyingSpeed() * 1.5f; //150% of the default player fly speed. this check IS necessary, the variant of the player defines the "natural fly speed".
        }

        float flySpeedAttribute = (float) this.getAttributeValue(Attributes.FLYING_SPEED) * (this.isSprinting() ? 2 : 1);
        return isFlying() ? flySpeedAttribute * 4 : super.getFlyingSpeed() * 1.5f;
    }

    @Override
    protected boolean isFlapping() {
        return isFlying();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this instanceof IGrabberEntity grabberEntity) {
            GrabEntityAbilityInstance grabAbilityInstance = grabberEntity.getGrabAbilityInstance();

            if (grabAbilityInstance.grabbedEntity != null) {
                this.setYya(0.25f); // Start to fly up to cause fall damage when the target break free of the grab
            }
        }
    }

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
        AttributeInstance alphaScale = this.getAttribute(ChangedAddonAttributes.ALPHA_GENE_SCALE.get());
        if (alphaScale == null) {
            return;
        }

        if (this.alphaAdditionalScale() != scale) {
            alphaScale.setBaseValue(scale);
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
    public void setFlying(boolean flying) {
        this.setChangedEntityFlag(FLAG_IS_FLYING, flying);
        updateNavigationAndControl(isFlying());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("isFlying")) {
            setFlying(tag.getBoolean("isFlying"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isFlying", isFlying());
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
    public boolean canSafelyLandHere() {
        var collisionContext = CollisionContext.of(this);
        var checkBounding = this.getBoundingBox().move(0.0, -1.5, 0.0);
        boolean nearGround = BlockPos.betweenClosedStream(checkBounding).anyMatch(blockPos -> {
            BlockState state = level().getBlockState(blockPos);

            var relativeCheckBounding = checkBounding.move(blockPos.multiply(-1));
            var shape = state.getCollisionShape(level(), blockPos, collisionContext);
            return shape.toAabbs().stream().anyMatch(aabb -> aabb.intersects(relativeCheckBounding));
        });

        return nearGround;
    }

    @Override
    public PathNavigation getNavigationForFlyingState(boolean flying) {
        return flying ? flyingNavigation : groundNavigation;
    }

    @Override
    public void updateNavigationAndControl(boolean state) {
        var wantedNav = state ? flyingNavigation : groundNavigation;
        if (!level().isClientSide() && navigation != wantedNav) {
            navigation.stop();
            navigation = wantedNav;
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, @NotNull DamageSource source) {
        if (this.isFlying()) return false;
        return super.causeFallDamage(fallDistance, damageMultiplier, source);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
        if (!this.isFlying()) {
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
