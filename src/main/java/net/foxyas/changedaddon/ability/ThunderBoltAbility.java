package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.variants.AddonLatexVariant;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class ThunderBoltAbility extends SimpleAbility {


	@Override
	public TranslatableComponent getDisplayName(IAbstractChangedEntity entity) {
		return new TranslatableComponent("changed_addon.ability.thunder");
	}

	@Override
	public ResourceLocation getTexture(IAbstractChangedEntity entity) {
		return new ResourceLocation("changed_addon:textures/screens/thunderbolt.png");
	}

	@Override
	public boolean canUse(IAbstractChangedEntity entity) {
		Player player = (Player) entity.getEntity();
		TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
		return player.getFoodData().getFoodLevel() >= 10 && Variant == AddonLatexVariant.KET_EXPERIMENT_009.get() || Variant == AddonLatexVariant.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get() && !Spectator(entity.getEntity());
	}

	public static boolean Spectator(Entity entity){
		if (entity instanceof Player player1){
			return player1.isSpectator();
		}
		return true;
	}

	public UseType getUseType(IAbstractChangedEntity entity) {
		return UseType.CHARGE_RELEASE;
	}

	@Override
	public int getChargeTime(IAbstractChangedEntity entity) {
		TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
		if (Variant == AddonLatexVariant.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get()) {
			return 15;
		}
		return 20;
	}

	@Override
	public int getCoolDown(IAbstractChangedEntity entity) {
		TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
		if (Variant == AddonLatexVariant.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get()) {
			return 15;
		}
		return 25;
	}


	public float ReachAmount(IAbstractChangedEntity entity) {
		TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
		if (Variant == AddonLatexVariant.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get()) {
			return 10;
		}
		if (Variant == AddonLatexVariant.KET_EXPERIMENT_009.get()){
			return 5;
		}
		return 3.5F;
	}

	@Override
	public void tickCharge(IAbstractChangedEntity entity, float ticks) {
		super.tickCharge(entity, ticks);
	}

	
	@Override
	public void startUsing(IAbstractChangedEntity entity) {
		super.startUsing(entity);
		SummonLightBolt(entity.getEntity(),entity.getLevel(),ReachAmount(entity));
	}


	
	private static boolean isHandEmpty(Entity entity, InteractionHand hand) {
		return entity instanceof LivingEntity livingEntity && livingEntity.getItemInHand(hand).getItem() == Blocks.AIR.asItem();
	}
	private static InteractionHand getSwingHand(Entity entity) {
		return isHandEmpty(entity, InteractionHand.MAIN_HAND) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}


	
	public static void SummonLightBolt(Entity entity, LevelAccessor world,float amount) {
		Player player = (Player) entity;
		double x = 0;
		double y = 0;
		double z = 0;
		double range = Math.max(amount,1.5);
		x = entity.level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(range)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getBlockPos().getX();
		y = entity.level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(range)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getBlockPos().getY();
		z = entity.level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(range)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ();
		if (world instanceof ServerLevel _level) {
			LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
			entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(x, y, z)));
			entityToSpawn.setVisualOnly(false);
			_level.addFreshEntity(entityToSpawn);
			player.causeFoodExhaustion((float) 0.5);
			player.swing(getSwingHand(player), true);
		}
	}
}
