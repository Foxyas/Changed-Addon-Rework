package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.client.particle.EntityModelFadeParticleOptions;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

public class WhiteFoxEntity extends AbstractSnowFoxEntity {

    private static final EntityDataAccessor<Boolean> DISPLAY_PARTICLES_FADE = SynchedEntityData.defineId(WhiteFoxEntity.class, EntityDataSerializers.BOOLEAN);

    public WhiteFoxEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.WHITE_FOX.get(), world);
    }

    public WhiteFoxEntity(EntityType<WhiteFoxEntity> type, Level world) {
        super(type, world);
        xpReward = 5;
        this.setAttributes(this.getAttributes());
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.ABSORPTION;
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getUnderlyingPlayer() == null) {
            mayAddFadeParticle(level);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DISPLAY_PARTICLES_FADE, false);
    }

    public boolean displayFadeParticles() {
        return entityData.get(DISPLAY_PARTICLES_FADE);
    }

    public void setDisplayParticlesFade(boolean v) {
        entityData.set(DISPLAY_PARTICLES_FADE, v);
    }

    @Override
    public void variantTick(Level level) {
        super.variantTick(level);
        mayAddFadeParticle(level);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("displayFadeParticles", this.displayFadeParticles());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setDisplayParticlesFade(tag.contains("displayFadeParticles") && tag.getBoolean("displayFadeParticles"));
    }

    @Override
    public CompoundTag savePlayerVariantData() {
        CompoundTag tag = super.savePlayerVariantData();
        tag.putBoolean("displayFadeParticles", this.displayFadeParticles());
        return tag;
    }

    @Override
    public void readPlayerVariantData(CompoundTag tag) {
        super.readPlayerVariantData(tag);
        this.setDisplayParticlesFade(tag.contains("displayFadeParticles") && tag.getBoolean("displayFadeParticles"));
    }


    private void mayAddFadeParticle(Level level) {
        if (this.displayFadeParticles()) {
            int rgb = getDynamicRainbowColor(this.tickCount, 0.05f);

            EntityModelFadeParticleOptions particleOptions = ChangedAddonParticleTypes.entityModelFade(this.maybeGetUnderlying(), rgb, 0.25f);
            ParticlesUtil.sendParticles(level, particleOptions, getPosition(0).add(0, 1.501f, 0), Vec3.ZERO, 0, 0.1f);
        }
    }

    /**
     * Calculates a dynamic rainbow RGB integer based on a game tick counter.
     * Adjust the multiplier (speed) to change the speed of the rainbow cycle.
     */
    private int getDynamicRainbowColor(int ticks, float speed) {
        // Sine wave calculations shifted by 120 and 240 degrees for RGB mixing
        int r = (int) (Math.sin(ticks * speed + 0.0f) * 127 + 128);
        int g = (int) (Math.sin(ticks * speed + 2.0f * Math.PI / 3.0f) * 127 + 128);
        int b = (int) (Math.sin(ticks * speed + 4.0f * Math.PI / 3.0f) * 127 + 128);

        // Explicitly include Alpha (0xFF) at the front for proper 32-bit ARGB alignment
        return (0xFF << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 24);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }
}
