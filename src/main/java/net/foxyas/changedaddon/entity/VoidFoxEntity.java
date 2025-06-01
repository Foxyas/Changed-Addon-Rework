
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.entity.CustomHandle.CrawlFeature;
import net.foxyas.changedaddon.entity.CustomHandle.IHasBossMusic;
import net.foxyas.changedaddon.entity.goals.BossComboAbilityGoal;
import net.foxyas.changedaddon.entity.goals.ComboAbilityGoal;
import net.foxyas.changedaddon.entity.goals.DashAttack;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.process.util.ChangedAddonSounds;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VoidFoxEntity extends ChangedEntity implements CrawlFeature, IHasBossMusic {
    private static final int MAX_DODGING_TICKS = 20;
    private int AttackComboCooldown;

    public VoidFoxEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonModEntities.VOID_FOX.get(), world);
    }

    public VoidFoxEntity(EntityType<VoidFoxEntity> type, Level world) {
        super(type, world);
        maxUpStep = 0.6f;
        xpReward = 1000;
        setNoAi(false);
        setPersistenceRequired();
    }

    public final ServerBossEvent bossBar = getBossBar();

    public ServerBossEvent getBossBar() {
        var bossBar = new ServerBossEvent(
                this.getDisplayName(), // Nome exibido na boss bar
                BossEvent.BossBarColor.WHITE, // Cor da barra
                BossEvent.BossBarOverlay.NOTCHED_10 // Estilo da barra
        );
        bossBar.setCreateWorldFog(true);
        bossBar.setDarkenScreen(true);
        return bossBar;
    }

    private static final EntityDataAccessor<Integer> DODGE_ANIM_TICKS =
            SynchedEntityData.defineId(VoidFoxEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DODGE_ANIM_TICKS, 0);
    }

    @Override
    public LatexType getLatexType() {
        return LatexType.NEUTRAL;
    }

    @Override
    public TransfurMode getTransfurMode() {
        if (this.getTarget() != null) {
            return TransfurMode.NONE;
        }

        return TransfurMode.ABSORPTION;
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(3, new ComboAbilityGoal(
                this, 3f, 18f, 8f, 5,
                new SoundEvent[]{SoundEvents.PLAYER_ATTACK_SWEEP,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.LIGHTNING_BOLT_THUNDER},
                new ParticleOptions[]{ParticleTypes.FLASH, ParticleTypes.FLASH, ParticleTypes.FLASH}
        ));

        this.goalSelector.addGoal(3, new DashAttack(this));


        BossComboAbilityGoal.DefaultCombos defaultCombos = new BossComboAbilityGoal.DefaultCombos(this,
                getTarget(),
                6,
                new SoundEvent[]{SoundEvents.PLAYER_ATTACK_SWEEP,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.LIGHTNING_BOLT_THUNDER},
                new ParticleOptions[]{ParticleTypes.FLASH, ParticleTypes.FLASH, ParticleTypes.FLASH});

        this.goalSelector.addGoal(3,
                new BossComboAbilityGoal(
                        this,
                        3,
                        3f,
                        7f,
                        0.5f,
                        defaultCombos::uppercut,
                        defaultCombos::slam,
                        livingEntity -> defaultCombos.teleportAndKnockbackInAir(3),
                        livingEntity -> defaultCombos.teleportAndKnockback(2),
                        livingEntity -> defaultCombos.teleportAndKnockback(1)
                )
        );

        this.goalSelector.addGoal(3,
                new BossComboAbilityGoal(
                        this,
                        1,
                        1f,
                        4f,
                        0.5f,
                        defaultCombos::uppercut,
                        defaultCombos::slam,
                        livingEntity -> defaultCombos.teleportAndKnockbackInAir(5)
                )
        );
    }

    public void setAttackComboCooldown(int attackComboCooldown) {
        AttackComboCooldown = attackComboCooldown;
    }

    @Override
    public Color3 getDripColor() {
        return this.getRandom().nextBoolean() ? Color3.BLACK : Color3.WHITE;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public double getMyRidingOffset() {
        return super.getMyRidingOffset();
    }

    public double getTorsoYOffset(ChangedEntity self) {
        float ageAdjusted = (float) self.tickCount * 0.33333334F * 0.25F * 0.15F;
        float ageSin = Mth.sin(ageAdjusted * 3.1415927F * 0.5F);
        float ageCos = Mth.cos(ageAdjusted * 3.1415927F * 0.5F);
        float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;
        return (double) (Mth.lerp(Mth.lerp(1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F), ageSin * ageSin * ageSin * ageSin, 1.0F - ageCos * ageCos * ageCos * ageCos), 0.95F, 0.87F) + bpiSize);
    }

    public double getTorsoYOffsetForFallFly(ChangedEntity self) {
        float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;
        return 0.375 + bpiSize;
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.getPose() == Pose.STANDING || this.getPose() == Pose.CROUCHING) {
            return super.getPassengersRidingOffset() + this.getTorsoYOffset(this) + (this.isCrouching() ? 1.2 : 1.15);
        }
        return getTorsoYOffsetForFallFly(this);
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return true;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("dodgeTicks")) {
            this.setDodgingTicks(tag.getInt("dodgeTicks"));
        }
        if (tag.contains("AttackCooldown")) this.AttackComboCooldown = tag.getInt("AttackCooldown");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("dodgeTicks", this.getDodgingTicks());
        tag.putInt("AttackCooldown", this.AttackComboCooldown);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        float randomValue = this.getRandom().nextFloat();
        boolean willHit = randomValue < 0.25f;

        if (source.getEntity() != null && !willHit) {
            this.setDodging(source.getEntity(), true);
            return false;
        }

        this.setDodging(source.getEntity(), false);
        return super.hurt(source, amount);
    }

    private void setDodging(Entity entity, boolean b) {
        if (b) {
            this.setDodgingTicks(getLevel().random.nextBoolean() ? MAX_DODGING_TICKS : -MAX_DODGING_TICKS);

            if (entity != null) {
                this.lookAt(entity, 1, 1);
            }
            this.getNavigation().stop();
        } else {

            if (entity != null) {
                this.lookAt(entity, 1, 1);
            }
            this.setDodgingTicks(0);
        }
    }

    public void setDodgingTicks(int dodgingTicks) {
        this.entityData.set(DODGE_ANIM_TICKS, dodgingTicks);
    }

    public boolean isDodging() {
        return getDodgingTicks() > 0;
    }

    public int getDodgingTicks() {
        return this.entityData.get(DODGE_ANIM_TICKS);
    }

    public static int getMaxDodgingTicks() {
        return MAX_DODGING_TICKS;
    }

    public void tickDodgeTicks() {
        if (!this.isNoAi()) {
            int ticks = this.getDodgingTicks();
            if (ticks > 0) {
                this.setDodgingTicks(ticks - 2);
            } else if (ticks < 0) {
                this.setDodgingTicks(ticks + 2);
            }
        }
    }

    @Override
    public @NotNull SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    public static void init() {
    }

    @Override
    public void visualTick(Level level) {
        super.visualTick(level);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        crawlingSystem(this, this.getTarget());
        tickDodgeTicks();
        if (!this.level.isClientSide) {
            this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossBar.addPlayer(player);
        player.displayClientMessage(
                new TextComponent("An ominous presence looms in the region...\nAre you prepared to face the consequences of your actions?").withStyle((style -> {
                    Style returnStyle = style.withColor(ChatFormatting.BLACK);
                    returnStyle = returnStyle.withItalic(true);
                    return returnStyle;
                })),
                false
        );
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossBar.removePlayer(player);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder = builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 3f);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 24);
        builder = builder.add(Attributes.ARMOR, 2);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((7.5));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((500));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.1);
        attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue(1.1);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(10);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(20);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(12);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(2);
    }

    @Override
    public ResourceLocation getBossMusic() {
        assert ChangedAddonSounds.EXP9_THEME != null;
        return ChangedAddonSounds.EXP9_THEME.getLocation();
    }

    @Override
    public int getMusicRange() {
        return IHasBossMusic.super.getMusicRange();
    }

    @Override
    public LivingEntity getSelf() {
        return this;
    }
}
