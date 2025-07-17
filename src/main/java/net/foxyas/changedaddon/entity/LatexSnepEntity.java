
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.entity.defaults.AbstractCanTameSnepChangedEntity;
import net.foxyas.changedaddon.entity.goals.SleepingWithOwnerGoal;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class LatexSnepEntity extends AbstractCanTameSnepChangedEntity {

	public boolean WantLoaf = false; //Lol the cat don't WANT LOAF!

	public LatexSnepEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.LATEX_SNEP.get(), world);
	}

	public LatexSnepEntity(EntityType<LatexSnepEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 0;
		this.setAttributes(this.getAttributes());
		setNoAi(false);
	}

	protected void setAttributes(AttributeMap attributes) {
		Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((10));
		attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(16.0f);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.15F);
		attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue(1.0f);
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
		attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
		attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
		attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
	}

	@Override
	public TransfurVariant<?> getSelfVariant() {
		return ChangedAddonTransfurVariants.LATEX_SNEP.get();
	}

	public boolean WantToLoaf() {
		return WantLoaf;
	}

	@Override
	protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return false;
    }

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("wantLoaf")){
			this.WantLoaf = tag.getBoolean("wantLoaf");
		}
	}

	@Override
	public boolean isBiped() {
		return false;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("wantLoaf",WantLoaf);
	}

	@Override
	public LatexType getLatexType() {
		return LatexType.NEUTRAL;
	}

	@Override
	public TransfurMode getTransfurMode() {
		return TransfurMode.NONE;
	}

	public Gender gender = Gender.MALE;

	@Override
	public Gender getGender() {
		return gender;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(5,new SleepingWithOwnerGoal(this));
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public double getMyRidingOffset() {
		return super.getMyRidingOffset();
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return SoundEvents.OCELOT_HURT; //ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.OCELOT_DEATH; //ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source == DamageSource.FALL)
			return false;
		if (source == DamageSource.FREEZE)
			return false;
		return super.hurt(source, amount);
	}

	@Override
	public boolean isItemAllowedInSlot(ItemStack stack, EquipmentSlot slot) {
		if (slot.getType() == EquipmentSlot.Type.ARMOR) {
			return false;
		} /*else if (slot == EquipmentSlot.OFFHAND) {
			return false;
		}
*/
		return super.isItemAllowedInSlot(stack, slot);
	}

	public InteractionResult LatexSnepInteraction(Player player, InteractionHand hand) {
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

				if (!isTransfur && this.random.nextInt(3) == 0) { // One in 3 chance
					this.tame(player);
					this.navigation.stop();
					this.setTarget(null);
					this.level.broadcastEntityEvent(this, (byte)7);
				} else if(isTransfur && this.random.nextInt(6) == 0) {
					this.tame(player);
					this.navigation.stop();
					this.setTarget(null);
					this.level.broadcastEntityEvent(this, (byte)7);
				} else {
					this.level.broadcastEntityEvent(this, (byte)6);
				}

				return InteractionResult.SUCCESS;
			}

			return super.mobInteract(player, hand);
		}
	}

	@Override
	protected @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
		return LatexSnepInteraction(player, hand);
	}

	@Override
	public void startSleeping(BlockPos pos) {
		// Obtém todas as entidades dentro de um cubo 3x3x3 ao redor do bloco onde a entidade vai dormir
		List<Entity> entities = this.level.getEntitiesOfClass(Entity.class, new AABB(pos).inflate(1));

		// Verifica se há alguma entidade exatamente no mesmo BlockPos onde a entidade vai dormir
		boolean isEntityOnBed = entities.stream().anyMatch(e -> (e instanceof Player && this.getOwner() == e && e.blockPosition().equals(pos)));

		if (isEntityOnBed) {
			this.playSound(SoundEvents.CAT_PURR, 1.0F, 1.0F); // Toca o som de ronronar
		}
		super.startSleeping(pos);
	}

	@Override
	public boolean canBeLeashed(Player p_21418_) {
		return !this.isLeashed();
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder.add((Attribute) ChangedAttributes.TRANSFUR_DAMAGE.get(), 3);
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}


}
