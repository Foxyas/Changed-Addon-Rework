package net.foxyas.changedaddon.entity.bosses;

import net.foxyas.changedaddon.entity.customHandle.AttributesHandle;
import net.foxyas.changedaddon.entity.defaults.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LuminarcticLeopardFemaleEntity extends AbstractLuminarcticLeopard {

    public LuminarcticLeopardFemaleEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.LUMINARCTIC_LEOPARD_FEMALE.get(), world);
    }

    public LuminarcticLeopardFemaleEntity(EntityType<LuminarcticLeopardFemaleEntity> type, Level world) {
        super(type, world);
        xpReward = XP_REWARD_HUGE;
        setNoAi(false);
        setPersistenceRequired();
    }


    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 6);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 1.25f);
        builder = builder.add(Attributes.MAX_HEALTH, 60F);
        builder = builder.add(Attributes.ARMOR, 8F);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 8);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);

        //Attack stats
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((6));
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(6.0f);
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(
                AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ATTACK_KNOCKBACK) + 1.5f
        );

        //Armor Stats
        attributes.getInstance(Attributes.ARMOR).setBaseValue(8);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(2);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);

        //Health Stats
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((60));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(128.0F);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.25f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.05f);
    }

    @Override
    public TransfurMode getTransfurMode() {
        if (this.getTarget() != null && (this.getTarget().getHealth() / this.getTarget().getMaxHealth() * 100) <= 15) {
            return TransfurMode.ABSORPTION;
        }
        return TransfurMode.NONE;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
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
        return super.getMyRidingOffset();
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.getPose() == Pose.STANDING || this.getPose() == Pose.CROUCHING) {
            return super.getPassengersRidingOffset() + this.getTorsoYOffset(this) + (this.isCrouching() ? 1.2 : 1.15);
        }
        return getTorsoYOffsetForFallFly(this);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        crawlingSystem(this, (float) this.getAttributeValue(ForgeMod.SWIM_SPEED.get()) * 0.35f);
    }

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }
}
