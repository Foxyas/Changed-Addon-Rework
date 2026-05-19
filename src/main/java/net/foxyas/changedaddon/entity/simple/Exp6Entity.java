package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.entity.ai.goals.simple.SleepingWithOwnerGoal;
import net.foxyas.changedaddon.entity.defaults.AbstractCanTameSnepChangedEntityFavors;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class Exp6Entity extends AbstractCanTameSnepChangedEntityFavors {

    public Exp6Entity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.EXP_6.get(), world);
    }

    public Exp6Entity(EntityType<Exp6Entity> type, Level world) {
        super(type, world);
        xpReward = ChangedEntity.XP_REWARD_LARGE;
        this.setAttributes(this.getAttributes());
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

    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((26));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(40.0f);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.18F);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.065f);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.05f);
        attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get()).setBaseValue(2.5F);
    }

    public InteractionResult Exp2(Player player, InteractionHand hand, Player Host) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (this.level.isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || this.isTameItem(itemstack) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (!this.isTame() && this.isTameItem(itemstack)) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                boolean isTransfur = ProcessTransfur.isPlayerTransfurred(player);

                if (!isTransfur && this.random.nextInt(2) == 0) { // One in 2 chance
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else if (isTransfur && this.random.nextInt(12) == 0) { //One in 12
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }

                return InteractionResult.SUCCESS;
            }

            return super.mobInteract(player, hand);
        }
    }

    @Override
    protected @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        return Exp2(player, hand, this.getUnderlyingPlayer());
    }

    @Override
    public TransfurMode getTransfurMode() {
        if (random.nextInt() > 5) {
            return TransfurMode.ABSORPTION;
        }
        return TransfurMode.REPLICATION;
    }

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }

    @Override
    public HairStyle getDefaultHairStyle() {
        if (random.nextInt(10) > 5) {
            return HairStyle.LONG_MESSY.get();
        }
        return HairStyle.LONG_KEPT.get();
    }

    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.GRAY;
    }

    @Override
    public @Nullable List<HairStyle> getValidHairStyles() {
        return HairStyle.Collection.FEMALE.getStyles();
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new SleepingWithOwnerGoal.BipedSleepGoal(this, false, SleepingWithOwnerGoal.BipedSleepGoal.BedSearchType.NEAREST));
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
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }
}
