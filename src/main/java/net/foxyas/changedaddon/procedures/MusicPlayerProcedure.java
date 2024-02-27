package net.foxyas.changedaddon.procedures;

import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.Music;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.entity.Experiment009phase2Entity;
import net.foxyas.changedaddon.entity.Experiment009Entity;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfigsConfiguration;
import net.foxyas.changedaddon.ChangedAddonMod;

import javax.annotation.Nullable;

import java.util.Comparator;

@Mod.EventBusSubscriber
public class MusicPlayerProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			PlayBossMusic(event, event.player.level, event.player);
		}
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	public static void PlayBossMusic(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null) {
			return;
		}

		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();

		Entity Experiment009 = (Entity) world.getEntitiesOfClass(Experiment009Entity.class, AABB.ofSize(new Vec3(x, y, z), 32, 32, 32), e -> true).stream().sorted(new Object() {
			Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
				return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
			}
		}.compareDistOf(x, y, z)).findFirst().orElse(null);

		Entity Experiment009Phase2 = (Entity) world.getEntitiesOfClass(Experiment009phase2Entity.class, AABB.ofSize(new Vec3(x, y, z), 32, 32, 32), e -> true).stream().sorted(new Object() {
			Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
				return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
			}
		}.compareDistOf(x, y, z)).findFirst().orElse(null);

		boolean Phase1isclose = !world.getEntitiesOfClass(Experiment009Entity.class, AABB.ofSize(new Vec3(x, y, z), 32, 32, 32), e -> true).isEmpty();

		boolean Phase2isclose = !world.getEntitiesOfClass(Experiment009phase2Entity.class, AABB.ofSize(new Vec3(x, y, z), 32, 32, 32), e -> true).isEmpty();


		if (world.isClientSide() && ChangedAddonClientConfigsConfiguration.MUSICPLAYER.get() == true) {
			boolean canwork = false;

			boolean Spectator = new Object() {
				public boolean checkGamemode(Player _player) {
					if (_player instanceof ServerPlayer _serverPlayer) {
						return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
					} else if (_player.level.isClientSide()) {
						return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
								&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
					}
					return false;
				}
			}.checkGamemode((Player) entity);
			if (!Spectator) {
				canwork = true;
			}
			if (Phase2isclose && canwork) {
				Minecraft minecraft = Minecraft.getInstance();
				MusicManager musicManager = minecraft.getMusicManager();
				net.minecraft.sounds.SoundEvent Experiment009Phase2Music = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment009_theme_phase2"));
				Music Experiment009_phase2_theme_MusicInstance = new Music(Experiment009Phase2Music, 0, 0, true);
				boolean isExperiment009Phase2ThemePlaying = musicManager.isPlayingMusic(Experiment009_phase2_theme_MusicInstance);
				if (!isExperiment009Phase2ThemePlaying) {
					musicManager.startPlaying(Experiment009_phase2_theme_MusicInstance);
				} else if (!Experiment009Phase2.isAlive()) {
					if (isExperiment009Phase2ThemePlaying){
						minecraft.getSoundManager().stop(new ResourceLocation("changed_addon","experiment009_theme_phase2"), SoundSource.MUSIC);
					}
				}
			} else if (Phase1isclose && canwork) {
				Minecraft minecraft = Minecraft.getInstance();
				MusicManager musicManager = minecraft.getMusicManager();
				net.minecraft.sounds.SoundEvent Experiment009Music = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment009_theme"));
				Music Experiment009_Theme_MusicInstance = new Music(Experiment009Music, 0, 0, true);
				boolean isExperiment009ThemePlaying = musicManager.isPlayingMusic(Experiment009_Theme_MusicInstance);
				if (!isExperiment009ThemePlaying) {
					musicManager.startPlaying(Experiment009_Theme_MusicInstance);
				} else if (isExperiment009ThemePlaying){
					if (!Experiment009.isAlive()){
						minecraft.getSoundManager().stop(new ResourceLocation("changed_addon","experiment009_theme"), SoundSource.MUSIC);
					}
				}
			}
		}
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		boolean isExperiment009ThemePlaying = false;
		boolean isExperiment009Phase2ThemePlaying = false;
		if (world.isClientSide()) {
			if (ChangedAddonClientConfigsConfiguration.MUSICPLAYER.get() == true) {
				if (!(new Object() {
					public boolean checkGamemode(Entity _ent) {
						if (_ent instanceof ServerPlayer _serverPlayer) {
							return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
						} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
							return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
									&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
						}
						return false;
					}
				}.checkGamemode(entity))) {
					if (!world.getEntitiesOfClass(Experiment009Entity.class, AABB.ofSize(new Vec3((entity.getX()), (entity.getY()), (entity.getZ())), 32, 32, 32), e -> true).isEmpty()) {
						Minecraft minecraft = Minecraft.getInstance();
						MusicManager musicManager = minecraft.getMusicManager();
						net.minecraft.sounds.SoundEvent Experiment009Music = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment009_theme"));
						Music Experiment009_Theme_MusicInstance = new Music(Experiment009Music, 0, 0, true);
						isExperiment009ThemePlaying = musicManager.isPlayingMusic(Experiment009_Theme_MusicInstance);
						if (!(((Entity) world.getEntitiesOfClass(Experiment009Entity.class, AABB.ofSize(new Vec3((entity.getX()), (entity.getY()), (entity.getZ())), 32, 32, 32), e -> true).stream().sorted(new Object() {
							Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
								return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
							}
						}.compareDistOf((entity.getX()), (entity.getY()), (entity.getZ()))).findFirst().orElse(null)) == null)
								&& ((Entity) world.getEntitiesOfClass(Experiment009Entity.class, AABB.ofSize(new Vec3((entity.getX()), (entity.getY()), (entity.getZ())), 32, 32, 32), e -> true).stream().sorted(new Object() {
									Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
										return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
									}
								}.compareDistOf((entity.getX()), (entity.getY()), (entity.getZ()))).findFirst().orElse(null)).isAlive()) {
							if (!isExperiment009ThemePlaying) {
								musicManager.startPlaying(Experiment009_Theme_MusicInstance);
							}
						}
					} else if (!world.getEntitiesOfClass(Experiment009phase2Entity.class, AABB.ofSize(new Vec3((entity.getX()), (entity.getY()), (entity.getZ())), 32, 32, 32), e -> true).isEmpty()) {
						Minecraft minecraft = Minecraft.getInstance();
						MusicManager musicManager = minecraft.getMusicManager();
						net.minecraft.sounds.SoundEvent Experiment009Phase2Music = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment009_theme_phase2"));
						Music Experiment009_phase2_theme_MusicInstance = new Music(Experiment009Phase2Music, 0, 0, true);
						isExperiment009Phase2ThemePlaying = musicManager.isPlayingMusic(Experiment009_phase2_theme_MusicInstance);
						if (!(((Entity) world.getEntitiesOfClass(Experiment009phase2Entity.class, AABB.ofSize(new Vec3((entity.getX()), (entity.getY()), (entity.getZ())), 32, 32, 32), e -> true).stream().sorted(new Object() {
							Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
								return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
							}
						}.compareDistOf((entity.getX()), (entity.getY()), (entity.getZ()))).findFirst().orElse(null)) == null)
								&& ((Entity) world.getEntitiesOfClass(Experiment009phase2Entity.class, AABB.ofSize(new Vec3((entity.getX()), (entity.getY()), (entity.getZ())), 32, 32, 32), e -> true).stream().sorted(new Object() {
									Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
										return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
									}
								}.compareDistOf((entity.getX()), (entity.getY()), (entity.getZ()))).findFirst().orElse(null)).isAlive()) {
							if (!isExperiment009Phase2ThemePlaying) {
								musicManager.startPlaying(Experiment009_phase2_theme_MusicInstance);
							}
						}
					}
				}
			}
		}
	}
}
