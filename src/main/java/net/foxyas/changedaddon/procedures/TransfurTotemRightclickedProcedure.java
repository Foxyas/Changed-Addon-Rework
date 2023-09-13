package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

import java.util.Iterator;

public class TransfurTotemRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (!entity.isShiftKeyDown()) {
			if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
				{
					Entity _ent = entity;
					if (!_ent.level.isClientSide() && _ent.getServer() != null)
						_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "untransfur @s");
				}
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown(itemstack.getItem(), 100);
				if (world.isClientSide())
					Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:untransfursound")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:untransfursound")), SoundSource.NEUTRAL, 1, 1, false);
					}
				}
				if (entity instanceof ServerPlayer _player) {
					Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:transfur_totem_advancement_1"));
					AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
					if (!_ap.isDone()) {
						Iterator _iterator = _ap.getRemainingCriteria().iterator();
						while (_iterator.hasNext())
							_player.getAdvancements().award(_adv, (String) _iterator.next());
					}
				}
			} else {
				if ((itemstack.getOrCreateTag().getString("form")).isEmpty()) {
					if (entity instanceof Player _player && !_player.level.isClientSide())
						_player.displayClientMessage(new TextComponent("Any any form linked please link one"), true);
				} else {
					{
						Entity _ent = entity;
						if (!_ent.level.isClientSide() && _ent.getServer() != null)
							_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), ("transfur @s " + itemstack.getOrCreateTag().getString("form")));
					}
					if (world.isClientSide())
						Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
					if (entity instanceof Player _player)
						_player.getCooldowns().addCooldown(itemstack.getItem(), 100);
					if (entity instanceof ServerPlayer _player) {
						Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:transfur_totem_advancement_1"));
						AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
						if (!_ap.isDone()) {
							Iterator _iterator = _ap.getRemainingCriteria().iterator();
							while (_iterator.hasNext())
								_player.getAdvancements().award(_adv, (String) _iterator.next());
						}
					}
				}
			}
		} else {
			if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
				if ((itemstack.getOrCreateTag().getString("form")).isEmpty()) {
					if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed:form")) {
						itemstack.getOrCreateTag().putString("form", ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm));
						if (world.isClientSide())
							Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
						if (entity instanceof Player _player)
							_player.getCooldowns().addCooldown(itemstack.getItem(), 100);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
					} else if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed:special")) {
						itemstack.getOrCreateTag().putString("form", "changed:form_light_latex_wolf");
						if (world.isClientSide())
							Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
						if (entity instanceof Player _player)
							_player.getCooldowns().addCooldown(itemstack.getItem(), 100);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
					}
				} else {
					itemstack.getOrCreateTag().putString("form", "");
					if (world.isClientSide())
						Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
					if (entity instanceof Player _player)
						_player.getCooldowns().addCooldown(itemstack.getItem(), 50);
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.deactivate")), SoundSource.NEUTRAL, 1, 1);
						} else {
							_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.deactivate")), SoundSource.NEUTRAL, 1, 1, false);
						}
					}
				}
			} else {
				if (!(itemstack.getOrCreateTag().getString("form")).isEmpty()) {
					itemstack.getOrCreateTag().putString("form", "");
					if (entity instanceof Player _player)
						_player.getCooldowns().addCooldown(itemstack.getItem(), 50);
					if (world.isClientSide())
						Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.deactivate")), SoundSource.NEUTRAL, 1, 1);
						} else {
							_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.deactivate")), SoundSource.NEUTRAL, 1, 1, false);
						}
					}
				}
			}
		}
	}
}
