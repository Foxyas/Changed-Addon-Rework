
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.entity.CustomHandle.AttributesHandle;
import net.foxyas.changedaddon.entity.CustomHandle.BossAbilitiesHandle;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class Experiment10Entity extends ChangedEntity implements GenderedEntity {
    //private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.RED, ServerBossEvent.BossBarOverlay.NOTCHED_6);
    private float TpCooldown;
    private boolean Phase2;

    public Experiment10Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonModEntities.EXPERIMENT_10.get(), world);
    }

    public Experiment10Entity(EntityType<Experiment10Entity> type, Level world) {
        super(type, world);
        this.setAttributes(getAttributes());
        maxUpStep = 0.6f;
        xpReward = 160;
        setNoAi(false);
        setPersistenceRequired();
    }

    protected void setAttributes(AttributeMap attributes) {
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.MAX_HEALTH) + 16));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.17);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue((1.1));
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ATTACK_DAMAGE) + 6.5);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ARMOR));
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ARMOR_TOUGHNESS));
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.KNOCKBACK_RESISTANCE));
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ATTACK_KNOCKBACK));
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

    @Override
    public Color3 getHairColor(int i) {
        return Color3.getColor("#1f1f1f");
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 1000;
    }

    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return livingEntity instanceof Player || livingEntity instanceof ServerPlayer || livingEntity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed:humanoids")));
    }

    @Override
    public void checkDespawn() {
        if (true) {
            return;
        }
        super.checkDespawn();
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance mobEffectInstance) {
        if (mobEffectInstance.getEffect() == MobEffects.WITHER){
            return false;
        }

        return super.canBeAffected(mobEffectInstance);
    }

    @Override
    public LatexType getLatexType() {
        return LatexType.NEUTRAL;
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
        return HairStyle.Collection.FEMALE.getStyles();
    }

    @Override
    public Color3 getDripColor() {
        return Color3.getColor("#181818");
    }

    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.DARK;
    }


    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
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

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
            return false;
        if (source == DamageSource.FALL)
            return false;
        if (source == DamageSource.CACTUS)
            return false;
        if (source == DamageSource.DROWN)
            return false;
        if (source == DamageSource.LIGHTNING_BOLT)
            return false;
        if (source.getMsgId().equals("trident")) {
            if (this.getLevel().random.nextFloat() <= 0.25f) {
                if (source.getEntity() instanceof Player player) {
                    player.displayClientMessage(new TextComponent("§l§o§3YOU'RE COWARD! Is distance all you can rely on? How PATHETIC!!!"), true);
                }
            }
            return super.hurt(source, amount * 0.5f);
        }
        if (source == DamageSource.ANVIL)
            return false;
        if (source == DamageSource.DRAGON_BREATH)
            return false;
        if (source == DamageSource.WITHER)
            return false;
        if (source.getMsgId().equals("witherSkull"))
            return false;
        if (source.isProjectile()) {
            if (this.getLevel().random.nextFloat() <= 0.25f) {
                if (source.getEntity() instanceof Player player) {
                    player.displayClientMessage(new TextComponent("§l§o§4Coward! Is distance all you can rely on? How PATHETIC!!!"), true);
                }
            }
            return super.hurt(source, amount * 0.5f);
        }
        return super.hurt(source, amount);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
        this.getBasicPlayerInfo().setSize(1f);
        this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
        this.getBasicPlayerInfo().setRightIrisColor(Color3.parseHex("#edbd25"));
        this.getBasicPlayerInfo().setLeftIrisColor(Color3.parseHex("#edbd25"));
        this.getBasicPlayerInfo().setScleraColor(Color3.parseHex("#edd725"));
        return retval;
    }


    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        //this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        //this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        //this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public static void init() {
    }


    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder.add((Attribute) ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 300);
        builder = builder.add(Attributes.ARMOR, 20);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 12);
        builder = builder.add(Attributes.FOLLOW_RANGE, 32);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.25);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1);
        return builder;
    }

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }

    public void setPhase2(boolean set) {
        this.Phase2 = set;
    }

    public boolean isPhase2() {
        return this.Phase2;
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Tp_Cooldown"))
            TpCooldown = tag.getFloat("Tp_Cooldown");
        if (tag.contains("Phase2")) {
            Phase2 = tag.getBoolean("Phase2");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Tp_Cooldown", TpCooldown);
        tag.putBoolean("Phase2", Phase2);
    }

    @Override
    public void visualTick(Level level) {
        super.visualTick(level);
    }

    @Override
    public void baseTick() {
        super.baseTick();
    }

    private void thisBurstAttack() {
        if (TpCooldown <= 0) {
            BossAbilitiesHandle.BurstAttack(this);
            this.TpCooldown = 50;
        }
    }

    public void CrawSystem(LivingEntity target) {
        if (target != null) {
            setCrawlingPoseIfNeeded(target);
            crawlToTarget(target);
        } else {
            if (!this.isSwimming() && !this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ())).isAir()) {
                this.setPose(Pose.SWIMMING);
            }
        }
    }

    public void setCrawlingPoseIfNeeded(LivingEntity target) {
        double targetEyeY = target.getEyeY();
        double entityEyeY = this.getEyeY();

        if (target.getPose() == Pose.SWIMMING && !(this.getPose() == Pose.SWIMMING)) {
            if (target.getY() < entityEyeY && !(target.level.getBlockState(new BlockPos(target.getX(), target.getEyeY(), target.getZ()).above()).isAir())) {
                this.setPose(Pose.SWIMMING);
            }
        } else {
            if (!this.isSwimming() && this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ()).above()).isAir()) {
                this.setPose(Pose.STANDING);
            }
        }
    }

    public void crawlToTarget(LivingEntity target) {
        double targetEyeY = target.getEyeY();
        double entityEyeY = this.getEyeY();

        if (target.getPose() == Pose.SWIMMING && this.getPose() == Pose.SWIMMING) {
            double deltaX = target.getX() - this.getX();
            double deltaY = target.getY() - this.getY();
            double deltaZ = target.getZ() - this.getZ();
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

            if (distance > 1.0) {
                double speed = 0.00015;
                double motionX = deltaX / distance * speed;
                double motionY = deltaY / distance * speed;
                double motionZ = deltaZ / distance * speed;
                this.setDeltaMovement(this.getDeltaMovement().add(motionX, motionY, motionZ));
            }
        }
    }

    public void updateSwimmingMovement() {
        if (this.isInWater()) {
            if (this.getTarget() != null) {
                LivingEntity target = this.getTarget();
                double deltaX = target.getX() - this.getX();
                double deltaY = target.getY() - this.getY();
                double deltaZ = target.getZ() - this.getZ();
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                if (distance > 0) {
                    double speed = 0.07;
                    double motionX = deltaX / distance * speed;
                    double motionY = deltaY / distance * speed;
                    double motionZ = deltaZ / distance * speed;
                    this.setDeltaMovement(this.getDeltaMovement().add(motionX, motionY, motionZ));
                }
            }

            if (this.isEyeInFluid(FluidTags.WATER)) {
                this.setPose(Pose.SWIMMING);
                this.setSwimming(true);
            } else if (this.getPose() == Pose.SWIMMING && !this.isEyeInFluid(FluidTags.WATER)) {
                this.setPose(Pose.STANDING);
                this.setSwimming(false);
            }
        } else if (this.getPose() == Pose.SWIMMING && !this.isInWater() && this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ()).above()).isAir()) {
            this.setPose(Pose.STANDING);
        }
    }

    public void SetDefense(Experiment10Entity entity) {
        AttributeModifier AttibuteChange = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "ArmorChange", 20, AttributeModifier.Operation.ADDITION);
        AttributeModifier AttibuteDefenseChange = new AttributeModifier(UUID.fromString("10-10-0-0-0"), "ArmorChange", 0.7, AttributeModifier.Operation.MULTIPLY_BASE);
        if (entity.isPhase2()) {
            if (!((entity.getAttribute(Attributes.ARMOR).hasModifier(AttibuteChange)))) {
                entity.getAttribute(Attributes.ARMOR).addTransientModifier(AttibuteChange);
            }

            if (!((entity.getAttribute(Attributes.ARMOR_TOUGHNESS).hasModifier(AttibuteDefenseChange)))) {
                entity.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(AttibuteDefenseChange);
            }

        } else {
            entity.getAttribute(Attributes.ARMOR).removeModifier(AttibuteChange);
            entity.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(AttibuteDefenseChange);
        }
    }

    public void SetAttack(Experiment10Entity entity) {
        AttributeModifier AttibuteChange = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "Attack", 0.6667, AttributeModifier.Operation.MULTIPLY_BASE);
        if (entity.isPhase2()) {
            if (!((entity.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(AttibuteChange)))) {
                entity.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(AttibuteChange);
            }
        } else {
            entity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(AttibuteChange);
        }
    }

    public void SetSpeed(Experiment10Entity entity) {
        AttributeModifier AttibuteChange = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "Speed", -0.4, AttributeModifier.Operation.MULTIPLY_BASE);
        if (entity.getPose() == Pose.SWIMMING) {
            if (!((entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttibuteChange)))) {
                entity.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(AttibuteChange);
            }
        } else {
            entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttibuteChange);
        }
    }

    public void TpEntity(Experiment10Entity entity) {
        double deltaZ;
        double distance;
        double deltaX;
        double deltaY;
        if (entity.getTarget() == null) {
            return; //stop if target = @null
        }


        Entity Target = entity.getTarget();
        LivingEntity Targets = entity.getLastHurtByMob();
        deltaX = Target.getX() - entity.getX();
        deltaY = Target.getY() - entity.getY();
        deltaZ = Target.getZ() - entity.getZ();
        distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        if (TpCooldown <= 0) {
            if (distance > 3) {
                if (entity.getLastHurtByMob() == Target) {
                    entity.teleportTo(Target.getX(), Target.getY(), Target.getZ());
                    this.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), ChangedSounds.BOW2, SoundSource.HOSTILE, 10, 1, true);
                    TpCooldown = 40;
                } else {
                    if (Targets != null && !(Targets instanceof ServerPlayer)) {
                        entity.setTarget(Targets);
                    } else if (Targets != null && Targets instanceof ServerPlayer serverPlayer) {
                        if (serverPlayer.gameMode.getGameModeForPlayer() != GameType.CREATIVE && serverPlayer.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
                            entity.setTarget(Targets);
                        }
                    }// Check if the entity in not null and is instance of server player if is will check if the gametype and if is not Creative and Spectator return true
                    entity.teleportTo(Target.getX(), Target.getY(), Target.getZ());
                    this.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), ChangedSounds.BOW2, SoundSource.HOSTILE, 10, 1, true);
                    TpCooldown = 40;
                }

				/*if((TpCooldown != 0)){
					TpCooldown -= 0.5f;
				}*/
            }
        } else {
            TpCooldown -= 0.5f;
        }
    }
}
