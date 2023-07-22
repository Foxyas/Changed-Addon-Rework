package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.init.ChangedAddonModItems;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TickHazardArmorFeatureProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		double LatexSolvent_level = 0;
		if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == ChangedAddonModItems.HAZARD_SUIT_HELMET.get()) {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null)
					_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
							("attribute @s " + "changed_addon:latexresistance " + "modifier add 944c27ef-1695-4da7-80bf-da8d53b46624 \"Hazard Armor Buff\" " + 0.15 + " add"));
			}
		} else {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null)
					_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "attribute @s changed_addon:latexresistance modifier remove 944c27ef-1695-4da7-80bf-da8d53b46624");
			}
		}
		if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem() == ChangedAddonModItems.HAZARD_SUIT_CHESTPLATE.get()) {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null)
					_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
							("attribute @s " + "changed_addon:latexresistance " + "modifier add 8ef509fc-b39a-4240-a344-9eea9a3e897c \"Hazard Armor Buff\" " + 0.35 + " add"));
			}
		} else {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null)
					_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "attribute @s changed_addon:latexresistance modifier remove 8ef509fc-b39a-4240-a344-9eea9a3e897c");
			}
		}
		if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem() == ChangedAddonModItems.HAZARD_SUIT_LEGGINGS.get()) {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null)
					_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
							("attribute @s " + "changed_addon:latexresistance " + "modifier add 17c0d810-400f-4ddd-b66d-a98c90d419ba \"Hazard Armor Buff\" " + 0.2 + " add"));
			}
		} else {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null)
					_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "attribute @s changed_addon:latexresistance modifier remove 17c0d810-400f-4ddd-b66d-a98c90d419ba");
			}
		}
		if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem() == ChangedAddonModItems.HAZARD_SUIT_BOOTS.get()) {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null)
					_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
							("attribute @s " + "changed_addon:latexresistance " + "modifier add ed6d2bc5-0f9c-4d91-8d4e-f249727fcc39 \"Hazard Armor Buff\" " + 0.1 + " add"));
			}
		} else {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null)
					_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "attribute @s changed_addon:latexresistance modifier remove ed6d2bc5-0f9c-4d91-8d4e-f249727fcc39");
			}
		}
	}
}
