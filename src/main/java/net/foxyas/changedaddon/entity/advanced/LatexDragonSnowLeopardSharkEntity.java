package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexShark;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class LatexDragonSnowLeopardSharkEntity extends AbstractLatexShark {

    public LatexDragonSnowLeopardSharkEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.LATEX_DRAGON_SNOW_LEOPARD_SHARK.get(), world);
    }

    public LatexDragonSnowLeopardSharkEntity(EntityType<LatexDragonSnowLeopardSharkEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(false);
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 1.10f);
        builder = builder.add(Attributes.MAX_HEALTH, 20);
        builder = builder.add(Attributes.ARMOR, 4);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void setAttributes(AttributeMap map) {
        super.setAttributes(map);
        map.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.10f);
        map.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.48f);
        map.getInstance(Attributes.MAX_HEALTH).setBaseValue(20.0);
        map.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get()).setBaseValue(2);
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
}
