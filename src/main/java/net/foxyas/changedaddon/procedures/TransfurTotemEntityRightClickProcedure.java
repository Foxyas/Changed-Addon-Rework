package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModItems;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TransfurTotemEntityRightClickProcedure {
	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getHand() != event.getPlayer().getUsedItemHand())
			return;
		execute(event, event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getTarget(), event.getPlayer());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		execute(null, world, x, y, z, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		ItemStack a = ItemStack.EMPTY;
		ItemStack b = ItemStack.EMPTY;
		a = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
		b = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY);
		if (!(sourceentity instanceof Player _player ? _player.getCooldowns().isOnCooldown(ChangedAddonModItems.TRANSFUR_TOTEM.get()) : false)) {
			if (a.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get() || b.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get()) {
				if (sourceentity.isShiftKeyDown()) {
					if (entity instanceof Player) {
						if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true) {
							if (a.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get() && (a.getOrCreateTag().getString("form")).isEmpty()) {
								if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed:form")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(a.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(a);
									a.getOrCreateTag().putString("form", ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm));
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								} else if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed_addon:form")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(a.getItem(), 50);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(a);
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door")), SoundSource.NEUTRAL, 1, 0);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door")), SoundSource.NEUTRAL, 1, 0, false);
										}
									}
									if (entity instanceof Player _player && !_player.level.isClientSide())
										_player.displayClientMessage(new TextComponent((new TranslatableComponent("changed_addon.latex_totem.notvalid").getString())), true);
								}
							} else if (b.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get() && (b.getOrCreateTag().getString("form")).isEmpty()) {
								if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed:form")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(b.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(b);
									b.getOrCreateTag().putString("form", ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm));
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								} else if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed_addon:form")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(b.getItem(), 50);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(b);
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door")), SoundSource.NEUTRAL, 1, 0);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door")), SoundSource.NEUTRAL, 1, 0, false);
										}
									}
									if (entity instanceof Player _player && !_player.level.isClientSide())
										_player.displayClientMessage(new TextComponent((new TranslatableComponent("changed_addon.latex_totem.notvalid").getString())), true);
								}
							}
						}
					} else {
						if (entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:latexentity")))
								|| entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed:organic_latex")))) {
							if (a.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get() && (a.getOrCreateTag().getString("form")).isEmpty()) {
								if (((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")).contains("_male")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(a.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(a);
									a.getOrCreateTag().putString("form", ("" + ((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")).replace("_male", "/male")));
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								} else if (((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")).contains("_female")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(a.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(a);
									a.getOrCreateTag().putString("form", ("" + ((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")).replace("_female", "/female")));
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								} else if (!((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")).contains("_male")
										&& !((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")).contains("_female")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(a.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(a);
									a.getOrCreateTag().putString("form", ((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")));
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								}
							} else if (b.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get() && (b.getOrCreateTag().getString("form")).isEmpty()) {
								if (((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")).contains("_male")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(b.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(b);
									b.getOrCreateTag().putString("form", ("" + ((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")).replace("_male", "/male")));
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								} else if (((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")).contains("_female")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(b.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(b);
									b.getOrCreateTag().putString("form", ("" + ((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")).replace("_female", "/female")));
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								} else if (!((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")).contains("_male")
										&& !((ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")).contains("_female")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(b.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(b);
									b.getOrCreateTag().putString("form", ("" + (ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace("changed:", "changed:form_")));
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
