package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.client.particle.EntityModelFadeParticleOptions;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
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

        if (level.isClientSide()) {
            if (this.displayFadeParticles()) {

            }
            double dX = Math.abs(this.getX() - this.xOld);
            double dY = Math.abs(this.getZ() - this.zOld);
            double dZ = Math.abs(this.getZ() - this.zOld);
            float velocity = 0.003F;
            if (dX >= velocity || dY >= velocity || dZ >= velocity) {
                int rgb = getDynamicRainbowColorInt(tickCount, 0.05f);

                EntityModelFadeParticleOptions particleOptions = ChangedAddonParticleTypes.entityModelFade(this, rgb, 1f);
                level.addParticle(particleOptions,
                        this.getX() * 0.925,
                        this.getY() + 1.425f,
                        this.getZ() * 0.925,
                        0,
                        0,
                        0
                );
            }
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
    }

    /**
     * Calculates a dynamic rainbow RGB integer based on a continuous float counter (FPS independent).
     */
    public int getDynamicRainbowColorInt(float ageInTicks, float speed) {
        // Sine wave calculations shifted by 120 and 240 degrees for RGB mixing
        int r = (int) (Math.sin(ageInTicks * speed + 0.0f) * 127 + 128);
        int g = (int) (Math.sin(ageInTicks * speed + 2.0f * Math.PI / 3.0f) * 127 + 128);
        int b = (int) (Math.sin(ageInTicks * speed + 4.0f * Math.PI / 3.0f) * 127 + 128);

        // Combine them into a packed 32-bit RGB integer
        return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
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
