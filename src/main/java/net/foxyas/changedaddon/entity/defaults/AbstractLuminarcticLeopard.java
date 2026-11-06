package net.foxyas.changedaddon.entity.defaults;

import net.foxyas.changedaddon.block.LuminarCrystalSmall;
import net.foxyas.changedaddon.entity.api.ICrawlAndSwimAbleEntity;
import net.foxyas.changedaddon.entity.api.IDynamicRideOffsetEntity;
import net.foxyas.changedaddon.entity.api.IHasBossMusic;
import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonDamageSources;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.network.syncher.ChangedAddonEntityDataSerializers;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.EyeStyle;
import net.ltxprogrammer.changed.entity.beast.AbstractSnowLeopard;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

public abstract class AbstractLuminarcticLeopard extends AbstractSnowLeopard implements ICrawlAndSwimAbleEntity, IDynamicRideOffsetEntity, IHasBossMusic {

    private static final EntityDataAccessor<Boolean> IS_BOSS =
            SynchedEntityData.defineId(AbstractLuminarcticLeopard.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<GlowState> GLOW_STAGE =
            SynchedEntityData.defineId(AbstractLuminarcticLeopard.class, ChangedAddonEntityDataSerializers.LUMINARCTIC_LEOPARDS_GLOW_STATE.get());

    public enum GlowState {
        NONE,
        PULSE,
        ALWAYS
    }

    public AbstractLuminarcticLeopard(EntityType<? extends AbstractSnowLeopard> type, Level level) {
        super(type, level);
        this.setAttributes(this.getAttributes());
    }

    // ------------------------------------------------------------------
    // Spawning
    // ------------------------------------------------------------------

    public static <T extends AbstractLuminarcticLeopard> boolean canSpawnNear(EntityType<T> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        // Block light must be 6 or lower
        if (world.getBrightness(LightLayer.BLOCK, pos) > 6) {
            return false;
        }

        // The block below must be solid
        BlockState blockBelow = world.getBlockState(pos.below());
        if (!blockBelow.isSolidRender(world, pos.below()) || !blockBelow.isFaceSturdy(world, pos.below(), Direction.UP)) {
            return false;
        }

        // Check area around the spawn (6 block radius)
        AABB checkArea = new AABB(pos).inflate(6);

        // Needs a hearted Luminar Crystal Small nearby
        boolean nearLuminarCrystal = world.getBlockStatesIfLoaded(checkArea)
                .anyMatch(state -> state.is(ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get())
                        && state.getValue(LuminarCrystalSmall.HEARTED));

        if (!nearLuminarCrystal) {
            return false;
        }

        // Limit how many of these can exist near the crystal
        int maxEntitiesNear = 3;
        int currentEntities = world.getEntities(entityType, checkArea, entity -> true).size();

        return currentEntities < maxEntitiesNear;
    }

    // ------------------------------------------------------------------
    // Synched data
    // ------------------------------------------------------------------

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GLOW_STAGE, GlowState.NONE);
        this.entityData.define(IS_BOSS, false);
    }

    public boolean isBoss() {
        return this.entityData.get(IS_BOSS);
    }

    public void setBoss(boolean boss) {
        this.entityData.set(IS_BOSS, boss);
    }

    public GlowState getGlowStage() {
        return this.entityData.get(GLOW_STAGE);
    }

    public void setGlowStage(GlowState stage) {
        this.entityData.set(GLOW_STAGE, stage);
    }

    public boolean hasGlowingMarks() {
        return this.getGlowStage() != GlowState.NONE;
    }

    // ------------------------------------------------------------------
    // Attributes
    // ------------------------------------------------------------------

    /**
     * Single source of truth for stats. Boss stats are layered on top of the
     * base ones here, so any re-run of this method (constructor, variant refresh,
     * boss toggle) always ends up with the right values.
     */
    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        attributes.getInstance(ChangedAttributes.AIR_CAPACITY.get()).setBaseValue(15);
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.35F);
        attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get()).setBaseValue(2.5);

        // Attack
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue(6);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(6.0);
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(
                net.foxyas.changedaddon.entity.customHandle.AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ATTACK_KNOCKBACK) + 1.5
        );

        // Defense
        attributes.getInstance(Attributes.ARMOR).setBaseValue(8);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(2);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);

        // Health / movement
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue(60);
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(128.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.25);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.05);

        if (this.isBoss()) {
            applyBossAttributes(attributes);
        }
    }

    private static void applyBossAttributes(AttributeMap attributes) {
        setBase(attributes.getInstance(Attributes.MAX_HEALTH), 500);
        setBase(attributes.getInstance(Attributes.ATTACK_DAMAGE), 17.5);
        setBase(attributes.getInstance(Attributes.ARMOR), 10);
        setBase(attributes.getInstance(Attributes.ARMOR_TOUGHNESS), 2.5);
    }

    private static void setBase(AttributeInstance instance, double value) {
        if (instance != null) {
            instance.setBaseValue(value);
        }
    }

    // ------------------------------------------------------------------
    // Boss state: event driven, no tick involved
    // ------------------------------------------------------------------

    /**
     * Runs whenever IS_BOSS changes: on the server when setBoss() / NBT load
     * sets it, and on the client when the tracker packet arrives.
     */
    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == IS_BOSS) {
            this.refreshBossState();
        }
    }

    private void refreshBossState() {
        this.setAttributes(this.getAttributes());

        if (!this.level.isClientSide) {
            if (this.isBoss()) {
                this.setHealth(this.getMaxHealth());
            } else if (this.getHealth() > this.getMaxHealth()) {
                this.setHealth(this.getMaxHealth());
            }
        }

        if (this.isBoss()) {
            this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
        }

        IAbstractChangedEntity.forEitherSafe(maybeGetUnderlying())
                .map(IAbstractChangedEntity::getTransfurVariantInstance)
                .ifPresent(TransfurVariantInstance::refreshAttributes);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (source.is(ChangedAddonDamageSources.LATEX_SOLVENT.key())) return super.hurt(source, amount);
        if (source.getEntity() instanceof LivingEntity attacker) {
            if (attacker.getAttributeValue(ChangedAddonAttributes.LATEX_SOLVENT_DAMAGE_MULTIPLIER.get()) > 0) return super.hurt(source, amount);
        }

        return super.hurt(source, isBoss() ? Math.max(amount * 0.05f, 1) : amount * 0.9f);
    }

    // ------------------------------------------------------------------
    // Persistence
    // ------------------------------------------------------------------

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isBoss", this.isBoss());
        tag.putString("glowStage", this.getGlowStage().name());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        // Boss flag first so the attribute pass runs before super restores
        // the saved Health value (that way loading never heals the boss).
        this.setBoss(tag.getBoolean("isBoss"));
        super.readAdditionalSaveData(tag);
        if (tag.contains("glowStage")) {
            try {
                this.setGlowStage(GlowState.valueOf(tag.getString("glowStage")));
            } catch (IllegalArgumentException ignored) {
                this.setGlowStage(GlowState.NONE);
            }
        }
    }

    // ------------------------------------------------------------------
    // Interface contracts
    // ------------------------------------------------------------------

    @Override
    public void updateStepSizeBasedInSwimState(boolean updateSwimmingMovement) {
        this.setMaxUpStep(updateSwimmingMovement ? 1f : 0.7f);
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return false; // They are neutral by default.
    }

    @Override
    public @Nullable ResourceLocation getBossMusicId() {
        return this.isBoss() ? ChangedAddonSoundEvents.LUMINARCTIC_LEOPARD.get().getLocation() : null;
    }

    @Override
    public LivingEntity getSelf() {
        return this;
    }

    // ------------------------------------------------------------------
    // Functions
    // ------------------------------------------------------------------

    public void playRoarSound() {
        this.playSound(ChangedSounds.TIGER_SHARK_ROAR.get(), 1, 0);
    }

}