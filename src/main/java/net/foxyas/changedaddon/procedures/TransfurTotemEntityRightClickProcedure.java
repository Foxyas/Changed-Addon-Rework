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
import net.foxyas.changedaddon.configuration.ChangedAddonConfigsConfiguration;

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
		String string = "";
		a = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
		b = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY);
		if (!(sourceentity instanceof Player _player ? _player.getCooldowns().isOnCooldown(ChangedAddonModItems.TRANSFUR_TOTEM.get()) : false)) {
			if (a.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get() || b.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get()) {
				if (sourceentity.isShiftKeyDown()) {
					if (entity instanceof Player) {
						if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true) {
							if (a.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get() && (a.getOrCreateTag().getString("form")).isEmpty()) {
								if (ChangedAddonConfigsConfiguration.ACCEPT_ALL_VARIANTS.get() == false) {
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
								} else {
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
								}
							} else if (b.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get() && (b.getOrCreateTag().getString("form")).isEmpty()) {
								if (ChangedAddonConfigsConfiguration.ACCEPT_ALL_VARIANTS.get() == false) {
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
								} else {
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
								}
							}
						}
					} else {
						if (entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:latexentity")))
								|| entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed:organic_latex"))) || entity instanceof net.ltxprogrammer.changed.entity.ChangedEntity) {
							string = (ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).replace(":", ":form_");
							if ((string).equals("changed_addon:form_dazed")) {
								string = "changed_addon:form_dazed_latex";
							} else if ((string).equals("changed_addon:form_puro_kind")) {
								string = "changed_addon:form_latex_puro_kind/male";
							} else if ((string).equals("changed_addon:form_puro_kind/female")) {
								string = "changed_addon:form_latex_puro_kind/female";
							} else if (string.startsWith("changed_addon:form_snow_leopard")) {
								if ((string).equals("changed_addon:form_snow_leopard/male_organic")) {
									string = "changed_addon:form_biosynth_snow_leopard/female";
								} else if ((string).equals("changed_addon:form_snow_leopard/female_organic")) {
									string = "changed_addon:form_biosynth_snow_leopard/male";
								}
							} else if ((string).equals("changed_addon:form_latex_snow_fox")) {
								string = "changed_addon:form_latex_snow_fox/male";
							}
							if (a.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get() && (a.getOrCreateTag().getString("form")).isEmpty()) {
								if (string.contains("_male")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(a.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(a);
									if ((string).equals("changed_addon:form_dazed")) {
										string = "changed_addon:form_dazed_latex";
									} else if ((string).equals("changed_addon:form_puro_kind")) {
										string = "changed_addon:form_latex_puro_kind/male";
									} else if ((string).equals("changed_addon:form_puro_kind/female")) {
										string = "changed_addon:form_latex_puro_kind/female";
									} else if (string.startsWith("changed_addon:form_snow_leopard")) {
										if ((string).equals("changed_addon:form_snow_leopard/male_organic")) {
											string = "changed_addon:form_biosynth_snow_leopard/female";
										} else if ((string).equals("changed_addon:form_snow_leopard/female_organic")) {
											string = "changed_addon:form_biosynth_snow_leopard/male";
										}
									} else if ((string).equals("changed_addon:form_latex_snow_fox")) {
										string = "changed_addon:form_latex_snow_fox/male";
									}
									a.getOrCreateTag().putString("form", ("" + string.replace("_male", "/male")));
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								} else if (string.contains("_female")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(a.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(a);
									if ((string).equals("changed_addon:form_dazed")) {
										string = "changed_addon:form_dazed_latex";
									} else if ((string).equals("changed_addon:form_puro_kind")) {
										string = "changed_addon:form_latex_puro_kind/male";
									} else if ((string).equals("changed_addon:form_puro_kind/female")) {
										string = "changed_addon:form_latex_puro_kind/female";
									} else if (string.startsWith("changed_addon:form_snow_leopard")) {
										if ((string).equals("changed_addon:form_snow_leopard/male_organic")) {
											string = "changed_addon:form_biosynth_snow_leopard/female";
										} else if ((string).equals("changed_addon:form_snow_leopard/female_organic")) {
											string = "changed_addon:form_biosynth_snow_leopard/male";
										}
									} else if ((string).equals("changed_addon:form_latex_snow_fox")) {
										string = "changed_addon:form_latex_snow_fox/male";
									}
									a.getOrCreateTag().putString("form", ("" + string.replace("_female", "/female")));
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								} else if (!string.contains("_male") && !string.contains("_female")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(a.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(a);
									if ((string).equals("changed_addon:form_dazed")) {
										string = "changed_addon:form_dazed_latex";
									} else if ((string).equals("changed_addon:form_puro_kind")) {
										string = "changed_addon:form_latex_puro_kind/male";
									} else if ((string).equals("changed_addon:form_puro_kind/female")) {
										string = "changed_addon:form_latex_puro_kind/female";
									} else if (string.startsWith("changed_addon:form_snow_leopard")) {
										if ((string).equals("changed_addon:form_snow_leopard/male_organic")) {
											string = "changed_addon:form_biosynth_snow_leopard/female";
										} else if ((string).equals("changed_addon:form_snow_leopard/female_organic")) {
											string = "changed_addon:form_biosynth_snow_leopard/male";
										}
									} else if ((string).equals("changed_addon:form_latex_snow_fox")) {
										string = "changed_addon:form_latex_snow_fox/male";
									} else if ((string).equals("changed_addon:form_ket_experiment_009")) {
										string = "changed_addon:form_ket_experiment009";
									}
									a.getOrCreateTag().putString("form", string);
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								}
							} else if (b.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get() && (b.getOrCreateTag().getString("form")).isEmpty()) {
								if (string.contains("_male")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(b.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(b);
									if ((string).equals("changed_addon:form_dazed")) {
										string = "changed_addon:form_dazed_latex";
									} else if ((string).equals("changed_addon:form_puro_kind")) {
										string = "changed_addon:form_latex_puro_kind/male";
									} else if ((string).equals("changed_addon:form_puro_kind/female")) {
										string = "changed_addon:form_latex_puro_kind/female";
									} else if (string.startsWith("changed_addon:form_snow_leopard")) {
										if ((string).equals("changed_addon:form_snow_leopard/male_organic")) {
											string = "changed_addon:form_biosynth_snow_leopard/female";
										} else if ((string).equals("changed_addon:form_snow_leopard/female_organic")) {
											string = "changed_addon:form_biosynth_snow_leopard/male";
										}
									} else if ((string).equals("changed_addon:form_latex_snow_fox")) {
										string = "changed_addon:form_latex_snow_fox/male";
									}
									b.getOrCreateTag().putString("form", ("" + string.replace("_male", "/male")));
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								} else if (string.contains("_female")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(b.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(b);
									if ((string).equals("changed_addon:form_dazed")) {
										string = "changed_addon:form_dazed_latex";
									} else if ((string).equals("changed_addon:form_puro_kind")) {
										string = "changed_addon:form_latex_puro_kind/male";
									} else if ((string).equals("changed_addon:form_puro_kind/female")) {
										string = "changed_addon:form_latex_puro_kind/female";
									} else if (string.startsWith("changed_addon:form_snow_leopard")) {
										if ((string).equals("changed_addon:form_snow_leopard/male_organic")) {
											string = "changed_addon:form_biosynth_snow_leopard/female";
										} else if ((string).equals("changed_addon:form_snow_leopard/female_organic")) {
											string = "changed_addon:form_biosynth_snow_leopard/male";
										}
									} else if ((string).equals("changed_addon:form_latex_snow_fox")) {
										string = "changed_addon:form_latex_snow_fox/male";
									}
									b.getOrCreateTag().putString("form", ("" + string.replace("_female", "/female")));
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								} else if (!string.contains("_male") && !string.contains("_female")) {
									if (sourceentity instanceof Player _player)
										_player.getCooldowns().addCooldown(b.getItem(), 20);
									if (world.isClientSide())
										Minecraft.getInstance().gameRenderer.displayItemActivation(b);
									if ((string).equals("changed_addon:form_dazed")) {
										string = "changed_addon:form_dazed_latex";
									} else if ((string).equals("changed_addon:form_puro_kind")) {
										string = "changed_addon:form_latex_puro_kind/male";
									} else if ((string).equals("changed_addon:form_puro_kind/female")) {
										string = "changed_addon:form_latex_puro_kind/female";
									} else if (string.startsWith("changed_addon:form_snow_leopard")) {
										if ((string).equals("changed_addon:form_snow_leopard/male_organic")) {
											string = "changed_addon:form_biosynth_snow_leopard/female";
										} else if ((string).equals("changed_addon:form_snow_leopard/female_organic")) {
											string = "changed_addon:form_biosynth_snow_leopard/male";
										}
									} else if ((string).equals("changed_addon:form_latex_snow_fox")) {
										string = "changed_addon:form_latex_snow_fox/male";
									} else if ((string).equals("changed_addon:form_ket_experiment_009")) {
										string = "changed_addon:form_ket_experiment009";
									}
									b.getOrCreateTag().putString("form", ("" + string));
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
