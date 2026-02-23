package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.entity.defaults.AbstractExp2SnepChangedEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class Exp2FemaleEntity extends AbstractExp2SnepChangedEntity {

    private static final List<SoundEvent> sounds = List.of(SoundEvents.CAT_AMBIENT, SoundEvents.CAT_PURR, SoundEvents.CAT_PURREOW);

    public Exp2FemaleEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.EXP_2_FEMALE.get(), world);
    }

    public Exp2FemaleEntity(EntityType<Exp2FemaleEntity> type, Level world) {
        super(type, world);
        xpReward = ChangedEntity.XP_REWARD_LARGE;
        this.setAttributes(this.getAttributes());
        setNoAi(false);
        setPersistenceRequired();
    }



    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder =  ChangedEntity.createLatexAttributes();
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
        Objects.requireNonNull(attributes.getInstance(Attributes.MAX_HEALTH)).setBaseValue((30));
        Objects.requireNonNull(attributes.getInstance(Attributes.MAX_HEALTH)).setBaseValue((24));
        Objects.requireNonNull(attributes.getInstance(Attributes.FOLLOW_RANGE)).setBaseValue(40.0f);
        Objects.requireNonNull(attributes.getInstance(Attributes.MOVEMENT_SPEED)).setBaseValue(1.18F);
        Objects.requireNonNull(attributes.getInstance(ForgeMod.SWIM_SPEED.get())).setBaseValue(1.065f);
        Objects.requireNonNull(attributes.getInstance(Attributes.ATTACK_DAMAGE)).setBaseValue(3.0f);
        Objects.requireNonNull(attributes.getInstance(Attributes.ARMOR)).setBaseValue(0);
        Objects.requireNonNull(attributes.getInstance(Attributes.ARMOR_TOUGHNESS)).setBaseValue(0);
        Objects.requireNonNull(attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE)).setBaseValue(0);
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.3f);
        attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get()).setBaseValue(2.5F);
    }

    @Override
    protected @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        return Exp2(player, hand, this.getUnderlyingPlayer());
    }

    public InteractionResult Exp2(Player player, InteractionHand hand, Player Host) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (this.level.isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || this.isTameItem(itemstack) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (tryFuseBack(player, this)) return InteractionResult.SUCCESS;

            if (!this.isTame() && this.isTameItem(itemstack)) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                boolean isTransfured = ProcessTransfur.isPlayerTransfurred(player);

                if (!isTransfured && this.random.nextInt(2) == 0) { // One in 2 chance
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else if (isTransfured && this.random.nextInt(12) == 0) { //One in 12
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
    public TransfurMode getTransfurMode() {
        if (level.random.nextInt() > 5) {
            return TransfurMode.ABSORPTION;
        }
        return TransfurMode.REPLICATION;
    }

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }

    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.GRAY;
    }

    @Override
    public HairStyle getDefaultHairStyle() {
        if (level.random.nextInt(10) > 5) {
            return HairStyle.LONG_MESSY.get();
        }
        return HairStyle.LONG_KEPT.get();
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

    public double getTorsoYOffset(ChangedEntity self) {
        float ageAdjusted = (float) self.tickCount * 0.33333334F * 0.25F * 0.15F;
        float ageSin = Mth.sin(ageAdjusted * 3.1415927F * 0.5F);
        float ageCos = Mth.cos(ageAdjusted * 3.1415927F * 0.5F);
        float bpiSize = (self.getBasicPlayerInfo().getSize(this) - 1.0F) * 2.0F;
        return Mth.lerp(Mth.lerp(1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F), ageSin * ageSin * ageSin * ageSin, 1.0F - ageCos * ageCos * ageCos * ageCos), 0.95F, 0.87F) + bpiSize;
    }

    public double getTorsoYOffsetForFallFly(ChangedEntity self) {
        float bpiSize = (self.getBasicPlayerInfo().getSize(this) - 1.0F) * 2.0F;
        return 0.375 + bpiSize;
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
        return Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.generic.hurt")));
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.generic.death")));
    }

    @Override
    public void WhenPattedReactionSimple() {
        playSound(sounds.get(random.nextInt(sounds.size())), 2.5f, 1);
    }
}
