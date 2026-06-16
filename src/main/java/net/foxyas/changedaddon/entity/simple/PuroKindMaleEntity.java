package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.entity.api.IDynamicRideOffsetEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
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

public class PuroKindMaleEntity extends AbstractDarkLatexWolf implements IDynamicRideOffsetEntity {

    public PuroKindMaleEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.PURO_KIND_MALE.get(), world);
    }

    public PuroKindMaleEntity(EntityType<PuroKindMaleEntity> type, Level world) {
        super(type, world);
        xpReward = AbstractDarkLatexWolf.XP_REWARD_MEDIUM;
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

    @Override
    public LatexType getLatexType() {
        return ChangedLatexTypes.DARK_LATEX.get();
    }

    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);

        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((24));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(25.0F);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.08f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.0f);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }

	/*@Override
	public LatexType getLatexType() {
		return LatexType.DARK_LATEX;
	}*/

    @Override
    public HairStyle getDefaultHairStyle() {
        return HairStyle.BALD.get();
    }

    public @Nullable List<HairStyle> getValidHairStyles() {
        return HairStyle.Collection.getAll();
    }

    public Color3 getHairColor(int layer) {
        return Color3.DARK;
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
		/*this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
			@Override
			protected double getAttackReachSqr(LivingEntity entity) {
				return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
			}
		});
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(5, new FloatGoal(this));*/

    }

    public Color3 getDripColor() {
        Color3 color;
        if (random.nextInt(10) > 5) {
            color = Color3.getColor("#393939");
        } else {
            color = Color3.getColor("#303030");
        }
        return color;
    }

    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.getColor("#303030");
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

    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult result = super.mobInteract(player, hand);
        ItemStack itemstack = player.getItemInHand(hand);
        if (!this.level().isClientSide) {
            if (result.consumesAction()) {
                return result;
            }

            if (!this.isTame() && this.isTameItem(itemstack)) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                ProcessTransfur.ifPlayerTransfurred(player, (variant) -> {
                    if (ChangedLatexTypes.DARK_LATEX.get().isFriendlyTo(variant.getLatexType()) && this.random.nextInt(3) == 0) {
                        this.tame(player);
                        this.navigation.stop();
                        this.setTarget(null);
                        this.level().broadcastEntityEvent(this, (byte)7);
                    } else if (!ChangedLatexTypes.DARK_LATEX.get().isHostileTo(variant.getLatexType()) && this.random.nextInt(10) == 0) {
                        this.tame(player);
                        this.navigation.stop();
                        this.setTarget(null);
                        this.level().broadcastEntityEvent(this, (byte)7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte)6);
                    }

                }, () -> {
                    if (this.random.nextInt(10) == 0) {
                        this.tame(player);
                        this.navigation.stop();
                        this.setTarget(null);
                        this.level().broadcastEntityEvent(this, (byte)7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte)6);
                    }

                });
                return InteractionResult.SUCCESS;
            } else {
                return result;
            }
        } else {
            boolean flag = this.isOwnedBy(player) || this.isTame() || this.isTameItem(itemstack) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
    }

}