
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.CustomHandle.CrawlFeature;
import net.foxyas.changedaddon.entity.CustomHandle.IHasBossMusic;
import net.foxyas.changedaddon.entity.goals.ComboAbilityGoal;
import net.foxyas.changedaddon.entity.goals.DashAttack;
import net.foxyas.changedaddon.entity.goals.KnockBackBurstGoal;
import net.foxyas.changedaddon.entity.goals.SimpleComboAbilityGoal;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.process.util.ChangedAddonSounds;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.ChatFormatting;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VoidFoxEntity extends ChangedEntity implements CrawlFeature, IHasBossMusic {
    private static final int MAX_DODGING_TICKS = 20;
    private int Attack1Cooldown, Attack2Cooldown, Attack3Cooldown;
    private int AttackInUse;
    public int timesUsedAttack1, timesUsedAttack2, timesUsedAttack3, timesUsedAttack4 = 0;
    private static final int MAX_COOLDOWN = 120;
    public static final int MAX_1_COOLDOWN = 120;
    public static final int MAX_2_COOLDOWN = 120;

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

    public void RegisterHit(){
        this.goalSelector.getAvailableGoals().forEach((wrappedGoal -> {
            if (wrappedGoal.getGoal() instanceof KnockBackBurstGoal knockBackBurstGoal) {
                knockBackBurstGoal.registerHit();
            }
        }));
    }

    public void RegisterDamage(float amount){
        this.goalSelector.getAvailableGoals().forEach((wrappedGoal -> {
            if (wrappedGoal.getGoal() instanceof KnockBackBurstGoal knockBackBurstGoal) {
                knockBackBurstGoal.registerDamage(amount);
            }
        }));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new KnockBackBurstGoal(this, 10));
        this.goalSelector.addGoal(1, new DashAttack(this) {
            @Override
            public boolean canUse() {
                if (VoidFoxEntity.this.getAttack2Cooldown() < VoidFoxEntity.MAX_1_COOLDOWN) {
                    return false;
                }
                if (VoidFoxEntity.this.AttackInUse > 0 && VoidFoxEntity.this.AttackInUse != 1) {
                    return false;
                }

                if (VoidFoxEntity.this.timesUsedAttack1 >= 4) {
                    return false;
                }

                return super.canUse();
            }

            @Override
            public void start() {
                super.start();

                VoidFoxEntity.this.timesUsedAttack1++;
                VoidFoxEntity.this.timesUsedAttack2 = 0;
                VoidFoxEntity.this.timesUsedAttack3 = 0;
                VoidFoxEntity.this.timesUsedAttack4 = 0;
            }

            @Override
            public void tick() {
                VoidFoxEntity.this.AttackInUse = 1;
                super.tick();
            }

            @Override
            public void stop() {
                super.stop();
                VoidFoxEntity.this.setAttack2Cooldown(0);
                VoidFoxEntity.this.AttackInUse = 0;
            }
        });

        this.goalSelector.addGoal(1, new ComboAbilityGoal(
                this, 3f, 18f, 8f, 5,
                new SoundEvent[]{SoundEvents.PLAYER_ATTACK_SWEEP,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.LIGHTNING_BOLT_THUNDER},
                new ParticleOptions[]{ParticleTypes.FLASH, ParticleTypes.FLASH, ParticleTypes.FLASH}
        ) {
            @Override
            public boolean canUse() {
                if (VoidFoxEntity.this.getAttack1Cooldown() < VoidFoxEntity.getMaxCooldown()) {
                    return false;
                }

                if (VoidFoxEntity.this.AttackInUse > 0 && VoidFoxEntity.this.AttackInUse != 2) {
                    return false;
                }

                if (VoidFoxEntity.this.timesUsedAttack2 >= 4) {
                    return false;
                }

                if (VoidFoxEntity.this.getRandom().nextFloat() >= 0.25f) {
                    return false;
                }

                return super.canUse();
            }

            @Override
            public void start() {
                super.start();
                VoidFoxEntity.this.setAttack1Cooldown(0);

                VoidFoxEntity.this.timesUsedAttack1 = 0;
                VoidFoxEntity.this.timesUsedAttack2++;
                VoidFoxEntity.this.timesUsedAttack3 = 0;
                VoidFoxEntity.this.timesUsedAttack4 = 0;
            }

            @Override
            public void tick() {
                VoidFoxEntity.this.AttackInUse = 2;
                super.tick();
            }

            @Override
            public void stop() {
                super.stop();
                VoidFoxEntity.this.AttackInUse = 0;
            }
        });

        this.goalSelector.addGoal(1, new SimpleComboAbilityGoal(
                this, 2, 3f, 18f, 8f, 5,
                new SoundEvent[]{SoundEvents.PLAYER_ATTACK_SWEEP,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.LIGHTNING_BOLT_THUNDER},
                new ParticleOptions[]{ParticleTypes.FLASH, ParticleTypes.FLASH, ParticleTypes.FLASH}
        ) {
            @Override
            public boolean canUse() {
                if (VoidFoxEntity.this.getAttack3Cooldown() < VoidFoxEntity.getMaxCooldown()) {
                    return false;
                }

                if (VoidFoxEntity.this.AttackInUse > 0 && VoidFoxEntity.this.AttackInUse != 3) {
                    return false;
                }

                if (VoidFoxEntity.this.timesUsedAttack3 >= 4) {
                    return false;
                }

                if (VoidFoxEntity.this.getRandom().nextFloat() >= 0.70f) {
                    return false;
                }

                return super.canUse();
            }

            @Override
            public void start() {
                super.start();
                VoidFoxEntity.this.setAttack3Cooldown(0);

                VoidFoxEntity.this.timesUsedAttack1 = 0;
                VoidFoxEntity.this.timesUsedAttack2 = 0;
                VoidFoxEntity.this.timesUsedAttack3++;
                VoidFoxEntity.this.timesUsedAttack4 = 0;
            }

            @Override
            public void tick() {
                VoidFoxEntity.this.AttackInUse = 3;
                super.tick();
            }

            @Override
            public void stop() {
                super.stop();
                VoidFoxEntity.this.AttackInUse = 0;
            }
        });

        this.goalSelector.addGoal(1, new ComboAbilityGoal(
                this, 6f, 18f, 8f, 5,
                new SoundEvent[]{SoundEvents.PLAYER_ATTACK_SWEEP,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.LIGHTNING_BOLT_THUNDER},
                new ParticleOptions[]{ParticleTypes.FLASH, ParticleTypes.FLASH, ParticleTypes.FLASH}
        ) {
            @Override
            public boolean canUse() {
                if (VoidFoxEntity.this.getAttack3Cooldown() < VoidFoxEntity.getMaxCooldown()) {
                    return false;
                }

                if (VoidFoxEntity.this.AttackInUse > 0 && VoidFoxEntity.this.AttackInUse != 4) {
                    return false;
                }

                if (VoidFoxEntity.this.timesUsedAttack4 >= 4) {
                    return false;
                }

                if (VoidFoxEntity.this.getRandom().nextFloat() >= 0.5) {
                    return false;
                }

                return super.canUse();
            }

            @Override
            public void start() {
                super.start();
                VoidFoxEntity.this.setAttack3Cooldown(0);

                VoidFoxEntity.this.timesUsedAttack1 = 0;
                VoidFoxEntity.this.timesUsedAttack2 = 0;
                VoidFoxEntity.this.timesUsedAttack3 = 0;
                VoidFoxEntity.this.timesUsedAttack4++;
            }

            @Override
            public void tick() {
                VoidFoxEntity.this.AttackInUse = 4;
                super.tick();
            }

            @Override
            public void stop() {
                super.stop();
                VoidFoxEntity.this.AttackInUse = 0;
            }
        });


    }

    public static int getMaxCooldown() {
        return MAX_COOLDOWN;
    }

    public void setAttack1Cooldown(int attack1Cooldown) {
        Attack1Cooldown = attack1Cooldown;
    }

    public void setAttack2Cooldown(int attack2Cooldown) {
        Attack2Cooldown = attack2Cooldown;
    }

    public void setAttack3Cooldown(int attack3Cooldown) {
        Attack3Cooldown = attack3Cooldown;
    }

    public int getAttack1Cooldown() {
        return Attack1Cooldown;
    }

    public int getAttack2Cooldown() {
        return Attack2Cooldown;
    }

    public int getAttack3Cooldown() {
        return Attack3Cooldown;
    }

    public static EntityDataAccessor<Integer> getDodgeAnimTicks() {
        return DODGE_ANIM_TICKS;
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


    public void doClawsAttackEffect(){// Efeito visual
        double d0 = (double) (-Mth.sin(this.getYRot() * 0.017453292F)) * 1;
        double d1 = (double) Mth.cos(this.getYRot() * 0.017453292F) * 1;
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, this.getX() + d0, this.getY(0.5), this.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, this.getX() + d0, this.getY(0.6), this.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, this.getX() + d0, this.getY(0.7), this.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1f, 0.75f);
        }
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
        if (tag.contains("Attack1Cooldown")) this.Attack1Cooldown = tag.getInt("Attack1Cooldown");
        if (tag.contains("Attack2Cooldown")) this.Attack2Cooldown = tag.getInt("Attack2Cooldown");
        if (tag.contains("Attack3Cooldown")) this.Attack3Cooldown = tag.getInt("Attack3Cooldown");
        if (tag.contains("AttackInUse")) this.Attack3Cooldown = tag.getInt("AttackInUse");
        if (tag.contains("timeUsedAttack1")) this.timesUsedAttack1 = tag.getInt("timeUsedAttack1");
        if (tag.contains("timeUsedAttack2")) this.timesUsedAttack2 = tag.getInt("timeUsedAttack2");
        if (tag.contains("timeUsedAttack3")) this.timesUsedAttack3 = tag.getInt("timeUsedAttack3");
        if (tag.contains("timeUsedAttack4")) this.timesUsedAttack4 = tag.getInt("timeUsedAttack4");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("dodgeTicks", this.getDodgingTicks());
        tag.putInt("Attack1Cooldown", this.Attack1Cooldown);
        tag.putInt("Attack2Cooldown", this.Attack2Cooldown);
        tag.putInt("Attack3Cooldown", this.Attack3Cooldown);
        tag.putInt("AttackInUse", this.AttackInUse);
        tag.putInt("timeUsedAttack1", this.timesUsedAttack1);
        tag.putInt("timeUsedAttack2", this.timesUsedAttack2);
        tag.putInt("timeUsedAttack3", this.timesUsedAttack3);
        tag.putInt("timeUsedAttack4", this.timesUsedAttack4);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        float randomValue = this.getRandom().nextFloat();
        float value = this.computeHealthRatio() <= 0.5f ? 0.75f : 0.5f;
        boolean willHit = randomValue <= value;

        if (source.getEntity() != null) {
            if (VoidFoxEntity.this.AttackInUse > 0) {
                if (VoidFoxEntity.this.AttackInUse != 1) {
                    return super.hurt(source, amount);
                } else {
                    this.goalSelector.getRunningGoals().forEach((wrappedGoal -> {
                        if (wrappedGoal.getGoal() instanceof DashAttack dashAttack) {
                            if (dashAttack.isChargingDash()) {
                                dashAttack.setTickCount(dashAttack.getTickCount() + 5);
                            }
                        }
                    }));
                }

            }
            if (!willHit) {
                this.setDodging(source.getEntity(), true);
                return false;
            } else {
                this.RegisterDamage(amount);


                this.setDodging(source.getEntity(), false);
                return super.hurt(source, amount);
            }

        }

        this.setDodging(source.getEntity(), false);
        return super.hurt(source, amount);
    }

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
    public static class WhenAttackAEntity {
        @SubscribeEvent
        public static void WhenAttack(LivingAttackEvent event) {
            LivingEntity target = event.getEntityLiving();
            Entity source = event.getSource().getEntity();

            if (source instanceof VoidFoxEntity voidFoxEntity) {
                voidFoxEntity.RegisterHit();
                /*if (voidFoxEntity.getMainHandItem().isEmpty()) {
                    voidFoxEntity.doClawsAttackEffect();
                }*/
            }
        }
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

    public void tickAttackTicks() {
        if (!this.isNoAi()) {
            if (AttackInUse != 0) {
                return;
            }
            if (this.Attack1Cooldown < MAX_COOLDOWN) {
                if (this.tickCount % 5 == 1) {
                    this.Attack1Cooldown++;
                }
            }
            if (this.Attack2Cooldown < MAX_1_COOLDOWN) {
                this.Attack2Cooldown++;
            }
            if (this.Attack3Cooldown < MAX_2_COOLDOWN) {
                this.Attack3Cooldown++;
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
        tickAttackTicks();
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
        if (this.computeHealthRatio() <= 0.5f) {
			assert ChangedAddonSounds.EXP10_THEME != null;
			return ChangedAddonSounds.EXP10_THEME.getLocation();
        }


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

    public int getAttackInUse() {
        return AttackInUse;
    }
}
