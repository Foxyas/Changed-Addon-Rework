package net.foxyas.changedaddon.entity.bosses;

import net.foxyas.changedaddon.entity.ai.goals.exp9.*;
import net.foxyas.changedaddon.entity.customHandle.AttributesHandle;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.util.ColorUtil;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedDamageSources;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class Experiment009Entity extends ChangedEntity implements PowderSnowWalkable {
    //private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.NOTCHED_6);
    private boolean Phase2;

    private double AI;

    public Experiment009Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.EXPERIMENT_009.get(), world);
    }

    public Experiment009Entity(EntityType<Experiment009Entity> type, Level world) {
        super(type, world);
        this.setAttributes(getAttributes());
        xpReward = 160;
        setNoAi(false);
        setPersistenceRequired();
    }



    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder =  ChangedEntity.createLatexAttributes();
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
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.MAX_HEALTH) + 20));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.15);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue((1.125));
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ATTACK_DAMAGE) + 5);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ARMOR) + 4);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ARMOR_TOUGHNESS));
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.KNOCKBACK_RESISTANCE));
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ATTACK_KNOCKBACK));
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.35f);
        attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get()).setBaseValue(2.5F);
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

        goalSelector.addGoal(5, new ThunderStrikeGoal(
                this,
                UniformInt.of(80, 120), //IntProvider -> cooldownProvider
                UniformInt.of(4, 8), //IntProvider -> damageProvider
                1.5f,
                200));
        goalSelector.addGoal(10, new ThunderDiveGoal(this,
                UniformInt.of(60, 100), //IntProvider -> cooldownProvider
                1.5f,
                6f,
                1f,
                0.5f,
                4));

        //Basically perfect, damn... well done 0senia0
        goalSelector.addGoal(5, new SummonLightningGoal(this, //PathfinderMob -> holder,
                UniformInt.of(90, 150), //IntProvider -> cooldown,
                UniformInt.of(2, 4), //IntProvider -> lightningCount,
                UniformInt.of(60, 100), //IntProvider -> castDuration,
                UniformInt.of(80, 100), //IntProvider -> lightningDelay,
                ConstantFloat.of(10))); //FloatProvider -> damage

        goalSelector.addGoal(5, new StaticDischargeGoal(this,//PathfinderMob holder,
                UniformInt.of(75, 125), //IntProvider -> cooldown,
                4,
                UniformInt.of(30, 50), //IntProvider -> castDuration,
                8,
                UniformFloat.of(8, 12))); //FloatProvider -> damage

        goalSelector.addGoal(1, new InductionCoilGoal(this, //PathfinderMob -> holder
                UniformInt.of(100, 150), //IntProvider -> cooldown
                20,
                UniformInt.of(60, 80), //IntProvider -> duration
                UniformFloat.of(3, 5))); //FloatProvider -> damage

        goalSelector.addGoal(5, new LightningComboAttackGoal(this, //PathfinderMob -> holder,
                UniformInt.of(150, 200), //IntProvider -> cooldown,
                UniformInt.of(3, 6), //IntProvider -> attackCount,
                UniformInt.of(20, 40), //IntProvider -> castDuration,
                UniformFloat.of(6, 8))); //FloatProvider -> damage)
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
        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
            return false;
        if (source.is(DamageTypes.FALL))
            return false;
        if (source.is(DamageTypes.CACTUS))
            return false;
        if (source.is(DamageTypes.DROWN))
            return false;
        if (source.is(DamageTypes.LIGHTNING_BOLT))
            return false;
        if (source.getMsgId().equals("trident")) {
            if (this.level().random.nextFloat() <= 0.25f) {
                if (source.getEntity() instanceof Player player) {
                    player.displayClientMessage(Component.literal("§l§o§3YOU'RE COWARD! Is distance all you can rely on? How PATHETIC!!!"), true);
                }
            }
            return super.hurt(source, amount * 0.5f);
        }
        if (source.is(DamageTypes.FALLING_ANVIL))
            return false;
        if (source.is(DamageTypes.DRAGON_BREATH))
            return false;
        if (source.is(DamageTypes.WITHER))
            return false;
        if (source.getMsgId().equals("witherSkull"))
            return false;
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            if (this.level().random.nextFloat() <= 0.25f) {
                if (source.getEntity() instanceof Player player) {
                    player.displayClientMessage(Component.literal("§l§o§4Coward! Is distance all you can rely on? How PATHETIC!!!"), true);
                }
            }
            return super.hurt(source, amount * 0.5f);
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean isDamageSourceBlocked(@NotNull DamageSource pDamageSource) {
        if (pDamageSource.is(ChangedDamageSources.ELECTROCUTION.key())) {
            return true;
        }
        return super.isDamageSourceBlocked(pDamageSource);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
        this.getBasicPlayerInfo().setSize(1f);
        this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
        return retval;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        //this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        //this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        //this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public boolean isPhase2() {
        return this.Phase2;
    }

    public void setPhase2(boolean set) {
        this.Phase2 = set;
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Phase2"))
            Phase2 = tag.getBoolean("Phase2");
        if (tag.contains("AI"))
            AI = tag.getDouble("AI");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Phase2", Phase2);
        tag.putDouble("AI", AI);
    }


    @Override
    public void baseTick() {
        super.baseTick();
    }

    public void SpawnThunderBolt(Vec3 pos) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.level);
        if (lightning != null) {
            lightning.moveTo(pos.x(), pos.y(), pos.z());
            this.level.addFreshEntity(lightning);
        }
    }
}
