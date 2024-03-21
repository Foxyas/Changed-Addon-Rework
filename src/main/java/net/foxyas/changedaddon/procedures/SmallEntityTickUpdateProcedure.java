package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingEvent;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.init.ChangedAddonModGameRules;
import net.foxyas.changedaddon.entity.DazedEntity;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class SmallEntityTickUpdateProcedure {
	@SubscribeEvent
	public static void onEntityTick(LivingEvent.LivingUpdateEvent event) {
		execute(event, event.getEntityLiving().level, event.getEntityLiving());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (world.getLevelData().getGameRules().getBoolean(ChangedAddonModGameRules.DO_DAZED_LATEX_BURN) == true) {
			if (entity instanceof DazedEntity) {
				if (((world.getFluidState(new BlockPos(entity.getX(), entity.getY(), entity.getZ())).createLegacyBlock()).getBlock() instanceof LiquidBlock
						|| (world.getFluidState(new BlockPos(entity.getX(), entity.getY(), entity.getZ())).createLegacyBlock()).getFluidState().isSource())
						&& (world.getFluidState(new BlockPos(entity.getX(), entity.getY(), entity.getZ())).createLegacyBlock()).getBlock() == Blocks.LAVA) {
					if (world.canSeeSkyFromBelowWater(new BlockPos(entity.getX(), entity.getY(), entity.getZ())) && world instanceof Level _lvl21 && _lvl21.isDay() && !entity.isInWaterRainOrBubble()) {
						if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem()) {
							if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MobEffects.FIRE_RESISTANCE) : false)) {
								entity.setSecondsOnFire(3);
							}
						}
					}
				}
			}
			if (((world.getFluidState(new BlockPos(entity.getX(), entity.getY(), entity.getZ())).createLegacyBlock()).getBlock() instanceof LiquidBlock
					|| (world.getFluidState(new BlockPos(entity.getX(), entity.getY(), entity.getZ())).createLegacyBlock()).getFluidState().isSource())
					&& (world.getFluidState(new BlockPos(entity.getX(), entity.getY(), entity.getZ())).createLegacyBlock()).getBlock() == Blocks.LAVA) {
				if (world.canSeeSkyFromBelowWater(new BlockPos(entity.getX(), entity.getY(), entity.getZ())) && world instanceof Level _lvl46 && _lvl46.isDay() && !entity.isInWaterRainOrBubble()) {
					if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem()) {
						if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MobEffects.FIRE_RESISTANCE) : false)) {
							entity.setSecondsOnFire(3);
						}
					}
				}
			}
		}
	}
}
