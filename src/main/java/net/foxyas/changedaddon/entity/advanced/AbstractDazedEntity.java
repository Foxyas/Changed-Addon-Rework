package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public abstract class AbstractDazedEntity extends ChangedEntity {

    public static final UseItemMode PuddleForm = UseItemMode.create("PuddleForm", false, false, false, true, false);

    protected static final EntityDataAccessor<Boolean> DATA_PUDDLE_MORPHED = SynchedEntityData.defineId(AbstractDazedEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> DATA_REPLICATION_TIMES = SynchedEntityData.defineId(AbstractDazedEntity.class, EntityDataSerializers.INT);

    protected boolean wasMorphed = false;

    public AbstractDazedEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PUDDLE_MORPHED, false); // Define o valor inicial como 'false'
        this.entityData.define(DATA_REPLICATION_TIMES, 0);
    }

    // Getter para checar se está no estado morphed
    public boolean isMorphed() {
        return this.entityData.get(DATA_PUDDLE_MORPHED);
    }

    // Setter para alterar o estado morphed
    public void setMorphed(boolean morphed) {
        this.entityData.set(DATA_PUDDLE_MORPHED, morphed);
    }

    public int getReplicationTimes() {
        return entityData.get(DATA_REPLICATION_TIMES);
    }

    public void setReplicationTimes(int replicationTimes) {
        this.entityData.set(DATA_REPLICATION_TIMES, replicationTimes);
    }

    public void subReplicationTimes(int replicationTimes) {
        this.entityData.set(DATA_REPLICATION_TIMES, getReplicationTimes() - replicationTimes);
    }

    public void addReplicationTimes(int replicationTimes) {
        this.entityData.set(DATA_REPLICATION_TIMES, getReplicationTimes() + replicationTimes);
    }

    protected void setMorphedAttributes(AttributeMap attributes) {
        safeMulBaseValue(attributes.getInstance(ForgeMod.ENTITY_REACH.get()), 0.5f);
        safeMulBaseValue(attributes.getInstance(ForgeMod.BLOCK_REACH.get()), 0.5f);
    }

    protected void safeSetBaseValue(@Nullable AttributeInstance instance, double value) {
        if (instance != null) {
            instance.setBaseValue(value);
        }
    }

    protected void safeMulBaseValue(@Nullable AttributeInstance instance, double value) {
        if (instance != null) {
            instance.setBaseValue(instance.getBaseValue() * value);
        }
    }

    @Override
    public UseItemMode getItemUseMode() {
        if (this.isMorphed()) {
            return PuddleForm;
        }
        return super.getItemUseMode();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Morphed")) {
            this.setMorphed(tag.getBoolean("Morphed"));
        }
        if (tag.contains("ReplicationTimes")) {
            this.setReplicationTimes(tag.getInt("ReplicationTimes"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Morphed", isMorphed());
        tag.putInt("ReplicationTimes", getReplicationTimes());
    }

    @Override
    public float getEyeHeightMul() {
        if (this.isMorphed())
            return 0.4F;
        else
            return super.getEyeHeightMul();
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pose) {
        EntityDimensions core = super.getDimensions(pose);
        if (this.isMorphed())
            return EntityDimensions.scalable(core.width - 0.05f, core.height - 1.25f);
        else
            return core;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (isMorphed() && !wasMorphed) {
            this.setMorphedAttributes(this.getAttributes());
            wasMorphed = true;

            IAbstractChangedEntity.forEitherSafe(this.maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
        } else if (!isMorphed() && wasMorphed) {
            this.setAttributes(this.getAttributes());
            wasMorphed = false;

            IAbstractChangedEntity.forEitherSafe(this.maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
        }
    }

    public Color3 getHairColor(int i) {
        return Color3.getColor("#E5E5E5");
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 700;
    }

    @Override
    public TransfurMode getTransfurMode() {
        if (this.getReplicationTimes() > 0) {
            return TransfurMode.REPLICATION;
        }
        return TransfurMode.ABSORPTION;
    }

    @Override
    public HairStyle getDefaultHairStyle() {
        HairStyle Hair;
        if (level.random.nextInt(10) > 5) {
            Hair = HairStyle.SHORT_MESSY.get();
        } else {
            Hair = BALD.get();
        }
        return Hair;
    }

    @Override
    public @Nullable List<HairStyle> getValidHairStyles() {
        return HairStyle.Collection.MALE.getStyles();
    }

    public Color3 getDripColor() {
        Color3 color;
        if (level.random.nextInt(10) > 5) {
            color = Color3.getColor("#ffffff");
        } else {
            color = Color3.getColor("#CFCFCF");
        }
        return color;
    }

    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.getColor("#CFCFCF");
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RestrictSunGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && level.getGameRules().getBoolean(ChangedAddonGameRules.DO_DAZED_LATEX_BURN);
            }
        });
    }

    @Override
    public void onReplicateOther(IAbstractChangedEntity other) {
        super.onReplicateOther(other);
        if (this.getReplicationTimes() > 0) {
            this.subReplicationTimes(1);
        }
        ChangedEntity changedEntity = other.getChangedEntity();
        if (changedEntity instanceof AbstractDazedEntity abstractDazedEntity) {
            abstractDazedEntity.setReplicationTimes(0);
        }

    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }
}
