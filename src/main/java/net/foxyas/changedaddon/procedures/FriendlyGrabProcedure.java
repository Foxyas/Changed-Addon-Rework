package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Iterator;
import java.util.Comparator;

public class FriendlyGrabProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).isFriendlyGrabbing == false) {
			{
				final Vec3 _center = new Vec3(x, y, z);
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
						.collect(Collectors.toList());
				for (Entity entityiterator : _entfound) {
					if (!(entityiterator == entity)) {
						if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true
								&& (entityiterator.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true) {
							if (!(entityiterator == entity)) {
								if (entity instanceof Player _player && !_player.level.isClientSide())
									_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.friendlygrab.you_hug").getString())), true);
								if (entityiterator instanceof Player _player && !_player.level.isClientSide())
									_player.displayClientMessage(new TextComponent(("" + (new TranslatableComponent("changedaddon.friendlygrab.hug").getString()).replace("entity", entity.getDisplayName().getString()))), true);
								if (entity instanceof ServerPlayer _player) {
									Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:hug"));
									AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
									if (!_ap.isDone()) {
										Iterator _iterator = _ap.getRemainingCriteria().iterator();
										while (_iterator.hasNext())
											_player.getAdvancements().award(_adv, (String) _iterator.next());
									}
								}
								if (entityiterator instanceof ServerPlayer _player) {
									Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:hug"));
									AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
									if (!_ap.isDone()) {
										Iterator _iterator = _ap.getRemainingCriteria().iterator();
										while (_iterator.hasNext())
											_player.getAdvancements().award(_adv, (String) _iterator.next());
									}
								}
								if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).contains("dark_latex")
										&& ((entityiterator.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).equals("changed:form_white_latex_wolf")) {
									if (entityiterator instanceof ServerPlayer _player) {
										Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:gneticsdefying_friendship"));
										AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
										if (!_ap.isDone()) {
											Iterator _iterator = _ap.getRemainingCriteria().iterator();
											while (_iterator.hasNext())
												_player.getAdvancements().award(_adv, (String) _iterator.next());
										}
									}
									if (entity instanceof ServerPlayer _player) {
										Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:gneticsdefying_friendship"));
										AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
										if (!_ap.isDone()) {
											Iterator _iterator = _ap.getRemainingCriteria().iterator();
											while (_iterator.hasNext())
												_player.getAdvancements().award(_adv, (String) _iterator.next());
										}
									}
								} else if (((entityiterator.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).contains("dark_latex")
										&& ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).equals("changed:form_white_latex_wolf")) {
									if (entityiterator instanceof ServerPlayer _player) {
										Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:gneticsdefying_friendship"));
										AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
										if (!_ap.isDone()) {
											Iterator _iterator = _ap.getRemainingCriteria().iterator();
											while (_iterator.hasNext())
												_player.getAdvancements().award(_adv, (String) _iterator.next());
										}
									}
									if (entity instanceof ServerPlayer _player) {
										Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:gneticsdefying_friendship"));
										AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
										if (!_ap.isDone()) {
											Iterator _iterator = _ap.getRemainingCriteria().iterator();
											while (_iterator.hasNext())
												_player.getAdvancements().award(_adv, (String) _iterator.next());
										}
									}
								}
							}
						}
						if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Friendly_mode == true
								&& (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true
								&& (entityiterator.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == false
								&& (entityiterator.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).isFriendlyGrabbing == false) {
							if (!(entityiterator == entity)) {
								if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_transfur == false) {
									if ((entityiterator.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).wantfriendlygrab == true) {
										if (entityiterator instanceof Player || entity instanceof ServerPlayer) {
											{
												Entity _ent = entityiterator;
												_ent.teleportTo((entity.getX()), (entity.getY()), (entity.getZ()));
												if (_ent instanceof ServerPlayer _serverPlayer)
													_serverPlayer.connection.teleport((entity.getX()), (entity.getY()), (entity.getZ()), _ent.getYRot(), _ent.getXRot());
											}
											if (entityiterator instanceof ServerPlayer _player)
												_player.setGameMode(GameType.SPECTATOR);
											if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
												_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.INFRIENDLYGRABEFFECT.get(), 100, 0, false, false));
											if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
												_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.INFRIENDLYGRAB.get(), 120, 0, false, false));
											if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
												_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.INFRIENDLYGRAB.get(), 200, 0, false, false));
											if (world instanceof Level _level) {
												if (!_level.isClientSide()) {
													_level.playSound(null, new BlockPos(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")),
															SoundSource.NEUTRAL, 1, 1);
												} else {
													_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 1,
															1, false);
												}
											}
											{
												String _setval = entity.getStringUUID();
												entityiterator.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
													capability.FriendlyGrabbing = _setval;
													capability.syncPlayerVariables(entityiterator);
												});
											}
											{
												String _setval = entityiterator.getStringUUID();
												entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
													capability.FriendlyGrabbing = _setval;
													capability.syncPlayerVariables(entity);
												});
											}
										}
									} else {
										if (entity instanceof Player _player && !_player.level.isClientSide())
											_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.friendlygrab.dont_want").getString())), true);
									}
								} else {
									if (entity instanceof Player _player && !_player.level.isClientSide())
										_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.friendlygrab.you_hug.organic").getString())), true);
									if (entityiterator instanceof Player _player && !_player.level.isClientSide())
										_player.displayClientMessage(new TextComponent(("" + (new TranslatableComponent("changedaddon.friendlygrab.hug").getString()).replace("entity", entity.getDisplayName().getString()))), true);
									if (entity instanceof ServerPlayer _player) {
										Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:hug"));
										AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
										if (!_ap.isDone()) {
											Iterator _iterator = _ap.getRemainingCriteria().iterator();
											while (_iterator.hasNext())
												_player.getAdvancements().award(_adv, (String) _iterator.next());
										}
									}
									if (entityiterator instanceof ServerPlayer _player) {
										Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:hug"));
										AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
										if (!_ap.isDone()) {
											Iterator _iterator = _ap.getRemainingCriteria().iterator();
											while (_iterator.hasNext())
												_player.getAdvancements().award(_adv, (String) _iterator.next());
										}
									}
								}
							}
						}
					}
				}
			}
		} else {
			if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns == true) {
				if (entity instanceof Player _player && !_player.level.isClientSide())
					_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.friendlygrab.alreadyin").getString())), false);
			}
		}
	}
}
