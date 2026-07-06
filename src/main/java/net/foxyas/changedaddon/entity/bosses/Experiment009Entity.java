package net.foxyas.changedaddon.entity.bosses;

import net.foxyas.changedaddon.entity.ai.goals.IReactiveGoal;
import net.foxyas.changedaddon.entity.ai.goals.exp9.*;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.IBestiaryEntityData;
import net.foxyas.changedaddon.entity.customHandle.AttributesHandle;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.util.ColorUtil;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedDamageSources;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class Experiment009Entity extends ChangedEntity implements PowderSnowWalkable, IBestiaryEntityData, IAlphaAbleEntity.CustomAlphaAttributes {

    private static final EntityDataAccessor<Boolean> PHASE2 = SynchedEntityData.defineId(Experiment009Entity.class, EntityDataSerializers.BOOLEAN);

    public Experiment009Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.EXPERIMENT_009.get(), world);
    }

    public Experiment009Entity(EntityType<? extends Experiment009Entity> type, Level world) {
        super(type, world);
        this.setAttributes(getAttributes());
        xpReward = 160;
        setNoAi(false);
        setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(getPhase2DataAccessor(), false);
    }

    protected EntityDataAccessor<Boolean> getPhase2DataAccessor() {
        return PHASE2;
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 3);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 1.15);
        builder = builder.add(Attributes.MAX_HEALTH, 40);
        builder = builder.add(Attributes.ARMOR, 4);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 5);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.3);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1);
        return builder;
    }

    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);

        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((6));
        AttributeMap defaultPlayerAttributes = AttributesHandle.DefaultPlayerAttributes();
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((defaultPlayerAttributes.getBaseValue(Attributes.MAX_HEALTH) + 20));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.15);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue((1.125));
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(defaultPlayerAttributes.getBaseValue(Attributes.ATTACK_DAMAGE) + 5);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(defaultPlayerAttributes.getBaseValue(Attributes.ARMOR) + 4);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(defaultPlayerAttributes.getBaseValue(Attributes.ARMOR_TOUGHNESS));
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(defaultPlayerAttributes.getBaseValue(Attributes.KNOCKBACK_RESISTANCE));
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(defaultPlayerAttributes.getBaseValue(Attributes.ATTACK_KNOCKBACK));
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.35f);
        attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get()).setBaseValue(2.5F);
    }

    @Override
    public void applyAlphaAttributesModifiers(LivingEntity entity, float normalized) {
        IAlphaAbleEntity.apply(entity, Attributes.MAX_HEALTH, IAlphaAbleEntity.MAX_HEALTH, "Alpha Max Health", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ATTACK_DAMAGE, IAlphaAbleEntity.ATTACK_DAMAGE, "Alpha Attack Damage", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ARMOR, IAlphaAbleEntity.ARMOR, "Alpha Armor", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ARMOR_TOUGHNESS, IAlphaAbleEntity.ARMOR_TOUGHNESS, "Alpha Armor Toughness", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ForgeMod.STEP_HEIGHT_ADDITION.get(), IAlphaAbleEntity.STEP_HEIGHT, "Alpha Step Height", normalized, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ChangedAttributes.TRANSFUR_DAMAGE.get(), IAlphaAbleEntity.TRANSFUR_DAMAGE, "Alpha Transfur Damage", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ATTACK_KNOCKBACK, IAlphaAbleEntity.ATTACK_KNOCKBACK, "Alpha Knockback", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ATTACK_SPEED, IAlphaAbleEntity.ATTACK_SPEED, "Alpha Attack Speed", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ForgeMod.ENTITY_REACH.get(), IAlphaAbleEntity.ENTITY_REACH, "Alpha Attack Reach", normalized * 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ForgeMod.BLOCK_REACH.get(), IAlphaAbleEntity.BLOCK_REACH, "Alpha Block Reach", normalized * 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ChangedAttributes.JUMP_STRENGTH.get(), IAlphaAbleEntity.JUMP_STRENGTH, "Alpha Jump Strength", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean startRiding(@NotNull Entity EntityIn, boolean force) {
        if (EntityIn instanceof Boat || EntityIn instanceof Minecart) {
            return false;
        }

        return super.startRiding(EntityIn, force);
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity target) {
        if (target.getEyeY() > this.getEyeY() + 1) {
            return super.getMeleeAttackRangeSqr(target) * 1.5D;
        }

        return super.getMeleeAttackRangeSqr(target);
    }

    public Color3 getHairColor(int i) {
        return Color3.getColor("#F1F1F1");
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 1000;
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return livingEntity instanceof Player || livingEntity instanceof ServerPlayer || livingEntity.getType().is(ChangedTags.EntityTypes.HUMANOIDS);
    }

    @Override
    public void checkDespawn() {
        super.checkDespawn();
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }


    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.NONE;
    }

    @Override
    public HairStyle getDefaultHairStyle() {
        return BALD.get();
    }

    @Override
    public @Nullable List<HairStyle> getValidHairStyles() {
        return HairStyle.Collection.MALE.getStyles();
    }

    public Color3 getDripColor() {
        return Color3.getColor("#E2E2E2");
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        Color3 firstColor = Color3.WHITE;
        Color3 secondColor = Color3.getColor("#E9E9E9");
        return ColorUtil.lerpTFColor(firstColor, secondColor, this.getUnderlyingPlayer());
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        addAbilitiesGoals();
    }

    protected void addAbilitiesGoals() {
        goalSelector.addGoal(5, new AoEThunderStrikeGoal(
                this,
                UniformInt.of(80, 120), //IntProvider -> cooldownProvider
                UniformInt.of(4, 8), //IntProvider -> damageProvider
                1.5f,
                200) {
            @Override
            public boolean canUse() {
                return super.canUse() && !Experiment009Entity.this.isSwimming();
            }
        });
        goalSelector.addGoal(10, new ThunderDiveGoal(this,
                        UniformInt.of(60, 100), //IntProvider -> cooldownProvider
                        1.5f,
                        6f,
                        1f,
                        0.5f,
                        4) {
                    @Override
                    public boolean canUse() {
                        return super.canUse() && !Experiment009Entity.this.isSwimming();
                    }
                }
        );

        //Basically perfect, damn... well done 0senia0
        goalSelector.addGoal(5, new SummonLightningGoal(this, //PathfinderMob -> holder,
                UniformInt.of(90, 150), //IntProvider -> cooldown,
                UniformInt.of(2, 4), //IntProvider -> lightningCount,
                UniformInt.of(60, 100), //IntProvider -> castDuration,
                UniformInt.of(80, 100), //IntProvider -> lightningDelay,
                ConstantFloat.of(10)) {
            @Override
            public boolean canUse() {
                return super.canUse() && !Experiment009Entity.this.isSwimming();
            }
        }); //FloatProvider -> damage

        goalSelector.addGoal(5, new StaticDischargeGoal(this,//PathfinderMob holder,
                UniformInt.of(75, 125), //IntProvider -> cooldown,
                4,
                UniformInt.of(30, 50), //IntProvider -> castDuration,
                8,
                UniformFloat.of(8, 12)) {
            @Override
            public boolean canUse() {
                return super.canUse() && !Experiment009Entity.this.isSwimming();
            }
        }); //FloatProvider -> damage

        goalSelector.addGoal(1, new InductionCoilGoal(this, //PathfinderMob -> holder
                UniformInt.of(100, 150), //IntProvider -> cooldown
                20,
                UniformInt.of(60, 80), //IntProvider -> duration
                UniformFloat.of(3, 5)) {
            @Override
            public boolean canUse() {
                return super.canUse() && !Experiment009Entity.this.isSwimming();
            }
        }); //FloatProvider -> damage

        goalSelector.addGoal(5, new LightningComboAttackGoal(this, //PathfinderMob -> holder,
                UniformInt.of(150, 200), //IntProvider -> cooldown,
                UniformInt.of(3, 6), //IntProvider -> attackCount,
                UniformInt.of(20, 40), //IntProvider -> castDuration,
                UniformFloat.of(6, 8)) {
            @Override
            public boolean canUse() {
                return super.canUse() && !Experiment009Entity.this.isSwimming();
            }
        }); //FloatProvider -> damage)
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public double getMyRidingOffset() {
        return -0.35D;
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof ThrownPotion ||
                source.getDirectEntity() instanceof AreaEffectCloud ||
                source.is(DamageTypes.FALL) ||
                source.is(DamageTypes.CACTUS) ||
                source.is(DamageTypes.DROWN) ||
                source.is(DamageTypes.LIGHTNING_BOLT) ||
                source.is(DamageTypes.FALLING_ANVIL) ||
                source.is(DamageTypes.DRAGON_BREATH) ||
                source.is(DamageTypes.WITHER) ||
                source.getMsgId().equals("witherSkull")) {
            triggerOnDamageReactiveGoals(source, amount, false);
            return false;
        }

        if (source.is(DamageTypeTags.IS_PROJECTILE) || source.getMsgId().equals("trident")) {
            amount *= 0.5f;
        }

        boolean willCauseDamage = super.hurt(source, amount);
        triggerOnDamageReactiveGoals(source, amount, willCauseDamage);
        return willCauseDamage;
    }

    public void triggerOnDamageReactiveGoals(DamageSource source, float finalAmount, boolean willCauseDamage) {
        this.goalSelector.getRunningGoals()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof IReactiveGoal)
                .forEach(goal -> ((IReactiveGoal) goal).onDamage(this, source, finalAmount, willCauseDamage));
        this.targetSelector.getRunningGoals()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof IReactiveGoal)
                .forEach(goal -> ((IReactiveGoal) goal).onDamage(this, source, finalAmount, willCauseDamage));
    }

    @Override
    protected void actuallyHurt(@NotNull DamageSource pDamageSource, float pDamageAmount) {
        super.actuallyHurt(pDamageSource, pDamageAmount);
        triggerOnHurtReactiveGoals(pDamageSource, pDamageAmount);
    }

    public void triggerOnHurtReactiveGoals(@NotNull DamageSource pDamageSource, float pDamageAmount) {
        this.goalSelector.getRunningGoals()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof IReactiveGoal)
                .forEach(goal -> ((IReactiveGoal) goal).onHurt(this, pDamageSource, pDamageAmount));
        this.targetSelector.getRunningGoals()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof IReactiveGoal)
                .forEach(goal -> ((IReactiveGoal) goal).onHurt(this, pDamageSource, pDamageAmount));
    }

    @Override
    public void heal(float pHealAmount) {
        super.heal(pHealAmount);
        triggerOnHealReactiveGoals(pHealAmount);
    }

    public void triggerOnHealReactiveGoals(float healAmound) {
        this.goalSelector.getRunningGoals()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof IReactiveGoal)
                .forEach(goal -> ((IReactiveGoal) goal).onHeal(this, healAmound));
        this.targetSelector.getRunningGoals()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof IReactiveGoal)
                .forEach(goal -> ((IReactiveGoal) goal).onHeal(this, healAmound));
    }

    @Override
    public boolean isDamageSourceBlocked(@NotNull DamageSource pDamageSource) {
        if (pDamageSource.is(ChangedDamageSources.ELECTROCUTION.key())) {
            return true;
        }
        return super.isDamageSourceBlocked(pDamageSource);
    }

    @Override
    public boolean canChangeDimensions() {
        return this.getTarget() == null && super.canChangeDimensions();
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
    }

    @Override
    protected void initializeBPI(BasicPlayerInfo info, RandomSource random) {
        super.initializeBPI(info, random);
        info.setSize(1f);
        info.setEyeStyle(EyeStyle.TALL);
    }

    public boolean isPhase2() {
        return this.entityData.get(getPhase2DataAccessor());
    }

    public void setPhase2(boolean set) {
        this.entityData.set(getPhase2DataAccessor(), set);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("isPhase2"))
            setPhase2(tag.getBoolean("isPhase2"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isPhase2", isPhase2());
    }

    @Override
    public CompoundTag savePlayerVariantData() {
        CompoundTag tag = super.savePlayerVariantData();
        tag.putBoolean("isPhase2", isPhase2());
        return tag;
    }

    @Override
    public void readPlayerVariantData(CompoundTag tag) {
        super.readPlayerVariantData(tag);
        if (tag.contains("isPhase2"))
            setPhase2(tag.getBoolean("isPhase2"));
    }

    public boolean shouldShowGlow() {
        return isPhase2();
    }

    @Override
    public void baseTick() {
        super.baseTick();
    }

    public void spawnThunderBolt(Vec3 pos) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.level);
        if (lightning == null) return;

        lightning.moveTo(pos.x(), pos.y(), pos.z());
        this.level.addFreshEntity(lightning);
    }

    @Override
    public void applyBestiaryRenderState(ChangedEntity changedEntity, GuiGraphics guiGraphics) {
        if (changedEntity instanceof Experiment009Entity entity) {
            entity.setPhase2(true);
        }
    }

    @Override
    public BestiaryInfo getBasicLore() {
        return new BestiaryInfo(Component.literal("Lore").withStyle(ChatFormatting.YELLOW), Component.translatableWithFallback("text.changed_addon.bestiary.lore.experiment_009", "Unknown"), 0);
    }

    @Override
    public BestiaryInfo getBasicAttributesInfo() {
        BestiaryInfo basicAttributesInfo = IBestiaryEntityData.super.getBasicAttributesInfo();
        return basicAttributesInfo.withHeightOffset(-60);
    }

    @Override
    public List<BestiaryInfo> getBestiaryInfo() {
        List<BestiaryInfo> bestiaryInfo = new ArrayList<>(IBestiaryEntityData.super.getBestiaryInfo());
        bestiaryInfo.add(new BestiaryInfo(Component.literal("Passive skills").withStyle(ChatFormatting.AQUA), Component.literal("Able to manipulate Electricity.\nNot recommended having anything metallic around them"), 2));
        return bestiaryInfo;
    }
}
