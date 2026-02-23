package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.entity.defaults.AbstractBasicOrganicChangedEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.variant.VariantExtraStats;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.network.PlayMessages;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

public class AvaliEntity extends AbstractBasicOrganicChangedEntity implements VariantExtraStats {

    protected static final EntityDataAccessor<Integer> PRIMARY_COLOR = SynchedEntityData.defineId(AvaliEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> SECONDARY_COLOR = SynchedEntityData.defineId(AvaliEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> STRIPES_COLOR = SynchedEntityData.defineId(AvaliEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> SIZE_SCALE = SynchedEntityData.defineId(AvaliEntity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<String> STYLE_OF_COLOR = SynchedEntityData.defineId(AvaliEntity.class, EntityDataSerializers.STRING);
    public final Set<String> StyleTypes = Set.of("male", "female");

    public AvaliEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.AVALI.get(), world);
    }

    public AvaliEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    public boolean isColorful() {
        return true;
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);

        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((20));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(40.0f);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.1f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(0.85f);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        defineAvaliSyncData();
    }

    protected void defineAvaliSyncData() {
        this.entityData.define(PRIMARY_COLOR, Color3.WHITE.toInt());
        this.entityData.define(SECONDARY_COLOR, Color3.WHITE.toInt());
        this.entityData.define(STRIPES_COLOR, Color3.WHITE.toInt());
        this.entityData.define(SIZE_SCALE, 0.8f);
        this.entityData.define(STYLE_OF_COLOR, "male");
    }

    public Color3 getDripColor() {
        return Color3.WHITE;
    }

    @Override
    public FlyType getFlyType() {
        return FlyType.ONLY_FALL;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        saveColors(tag);
    }

    public float getDimensionScale() {
        return this.entityData.get(SIZE_SCALE);
    }

    public void setDimensionScale(float scale) {
        this.entityData.set(SIZE_SCALE, scale);
    }

    @Override
    public CompoundTag savePlayerVariantData() {
        CompoundTag tag = super.savePlayerVariantData();
        saveColors(tag);
        return tag;
    }

    @Override
    public void readPlayerVariantData(CompoundTag tag) {
        super.readPlayerVariantData(tag);
        readColors(tag);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(this.getDimensionScale());
    }

    public void saveColors(CompoundTag originalTag) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("PrimaryColor", getPrimaryColor().toInt());
        tag.putInt("SecondaryColor", getSecondaryColor().toInt());
        tag.putInt("StripesColor", getStripesColor().toInt());
        tag.putString("StyleOfColor", getStyleOfColor());
        originalTag.put("TransfurColorData", tag);
        originalTag.putFloat("size_scale", getDimensionScale());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        readColors(tag);
    }

    public void readColors(CompoundTag originalTag) {
        if (originalTag.contains("size_scale")) {
            setDimensionScale(originalTag.getFloat("size_scale"));
        }
        CompoundTag tag = originalTag.getCompound("TransfurColorData");
        if (tag.contains("PrimaryColor")) setPrimaryColor(Color3.fromInt(tag.getInt("PrimaryColor")));
        if (tag.contains("SecondaryColor")) setSecondaryColor(Color3.fromInt(tag.getInt("SecondaryColor")));
        if (tag.contains("StripesColor")) setStripesColor(Color3.fromInt(tag.getInt("StripesColor")));
        if (tag.contains("StyleOfColor")) setStyleOfColor(tag.getString("StyleOfColor"));
    }

    @Override
    public void baseTick() {
        super.baseTick();
        failSafe();
    }

    protected void failSafe() {
        if (!getStyleOfColor().equals("male") && !getStyleOfColor().equals("female")) {
            this.setStyleOfColor("male");
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        // Randomize as cores
        applyRandomColors();

        return super.finalizeSpawn(world, difficulty, reason, data, tag);
    }

    protected void applyRandomColors() {
        this.setPrimaryColor(Color3.fromInt(random.nextInt(0, Integer.MAX_VALUE - 1)));
        this.setSecondaryColor(Color3.fromInt(random.nextInt(0, Integer.MAX_VALUE - 1)));
        this.setStripesColor(Color3.fromInt(random.nextInt(0, Integer.MAX_VALUE - 1)));
        this.setStyleOfColor(this.random.nextBoolean() ? "male" : "female");
    }

    @Nullable
    public Color3 getColor(int layer) {
        if (!isColorful()) return null;
        return switch (layer) {
            case 1 -> getSecondaryColor();
            case 2 -> getStripesColor();
            default -> getPrimaryColor();
        };
    }

    public Color3 getPrimaryColor() {
        return Color3.fromInt(this.entityData.get(PRIMARY_COLOR));
    }

    public void setPrimaryColor(Color3 color) {
        this.entityData.set(PRIMARY_COLOR, color.toInt());
    }

    public void setColor(int layer, Color3 color3) {
        if (!isColorful()) return;
        switch (layer) {
            case 1 -> setSecondaryColor(color3);
            case 2 -> setStripesColor(color3);
            default -> setPrimaryColor(color3);
        }
    }

    public Color3 getSecondaryColor() {
        return Color3.fromInt(this.entityData.get(SECONDARY_COLOR));
    }

    public void setSecondaryColor(Color3 color) {
        this.entityData.set(SECONDARY_COLOR, color.toInt());
    }

    public Color3 getStripesColor() {
        return Color3.fromInt(this.entityData.get(STRIPES_COLOR));
    }

    public void setStripesColor(Color3 color) {
        this.entityData.set(STRIPES_COLOR, color.toInt());
    }

    public String getStyleOfColor() {
        return this.entityData.get(STYLE_OF_COLOR);
    }

    public void setStyleOfColor(String style) {
        this.entityData.set(STYLE_OF_COLOR, style);
    }

    public enum SizeScaling implements IExtensibleEnum {
        NORMAL(0.8f),
        TALL(0.9f),
        VERY_TALL(1.0f);

        private final float scale;

        SizeScaling(float size) {
            scale = size;
        }

        public static SizeScaling create(String name, float scale) {
            throw new NotImplementedException("Not extended");
        }

        public float getScale() {
            return scale;
        }
    }
}
