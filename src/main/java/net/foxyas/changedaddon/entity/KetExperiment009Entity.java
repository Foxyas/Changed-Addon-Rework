package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.entity.CustomHandle.AttributesHandle;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class KetExperiment009Entity extends ChangedEntity {
    //private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.NOTCHED_6);
    private boolean Phase2;

    private double AI;

    public KetExperiment009Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.KET_EXPERIMENT_009.get(), world);
    }

    public KetExperiment009Entity(EntityType<KetExperiment009Entity> type, Level world) {
        super(type, world);
        this.setAttributes(getAttributes());
        maxUpStep = 0.6f;
        xpReward = 160;
        setNoAi(false);
        setPersistenceRequired();
    }

    public static void init() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
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
        return Color3.getColor("#F1F1F1");
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 1000;
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return livingEntity instanceof Player || livingEntity instanceof ServerPlayer || livingEntity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed:humanoids")));
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
        return HairStyle.Collection.MALE.getStyles();
    }

    public Color3 getDripColor() {
        return Color3.getColor("#E2E2E2");
    }

    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.WHITE;
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
        return -0.35D;
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
            lightning.setCause(null);
            this.level.addFreshEntity(lightning);
        }
    }
}
