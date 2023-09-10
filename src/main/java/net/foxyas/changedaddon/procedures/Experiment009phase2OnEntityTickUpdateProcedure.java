package net.foxyas.changedaddon.procedures;

import net.minecraftforge.common.ForgeMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

import java.util.Comparator;

public class Experiment009phase2OnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		Entity TpTarget = null;
		double randown = 0;
		double Pz = 0;
		double Px = 0;
		double Py = 0;
		BlockState a = Blocks.AIR.defaultBlockState();
		entity.getPersistentData().putDouble("IA", (entity.getPersistentData().getDouble("IA") + 1));
		if (entity.getPersistentData().getDouble("IA") >= 100) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= 175) {
				if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
					{
						Entity _ent = entity;
						_ent.teleportTo(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
								((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()));
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
									((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()), _ent.getYRot(), _ent.getXRot());
					}
					if (world instanceof ServerLevel _level) {
						LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
						entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(entity.getX(), entity.getY(), entity.getZ())));
						entityToSpawn.setVisualOnly(true);
						_level.addFreshEntity(entityToSpawn);
					}
					entity.getPersistentData().putDouble("IA", 0);
				}
			}
		}
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= 175) {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null)
					_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "particle dust_color_transition 0 0.57 0.82 1 0 0.69 0.78 ~ ~1 ~ 0.2 0.5 0.2 -0 10");
			}
		}
		if (!world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3((entity.getX()), (entity.getY()), (entity.getZ())), 20, 20, 20), e -> true).isEmpty()) {
			TpTarget = (Entity) world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3((entity.getX()), (entity.getY()), (entity.getZ())), 20, 20, 20), e -> true).stream().sorted(new Object() {
				Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
					return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
				}
			}.compareDistOf((entity.getX()), (entity.getY()), (entity.getZ()))).findFirst().orElse(null);
		} else {
			TpTarget = null;
		}
		if ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == (null)) {
			if (entity.getPersistentData().getDouble("BossTp") >= 5) {
				if (!(TpTarget == null)) {
					if (!(new Object() {
						public boolean checkGamemode(Entity _ent) {
							if (_ent instanceof ServerPlayer _serverPlayer) {
								return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
							} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
								return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
										&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
							}
							return false;
						}
					}.checkGamemode(TpTarget)) && !(new Object() {
						public boolean checkGamemode(Entity _ent) {
							if (_ent instanceof ServerPlayer _serverPlayer) {
								return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
							} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
								return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
										&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
							}
							return false;
						}
					}.checkGamemode(TpTarget))) {
						{
							Entity _ent = entity;
							_ent.teleportTo((TpTarget.getX()), (TpTarget.getY()), (TpTarget.getZ()));
							if (_ent instanceof ServerPlayer _serverPlayer)
								_serverPlayer.connection.teleport((TpTarget.getX()), (TpTarget.getY()), (TpTarget.getZ()), _ent.getYRot(), _ent.getXRot());
						}
						if (world instanceof ServerLevel _level) {
							LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
							entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(entity.getX(), entity.getY(), entity.getZ())));
							entityToSpawn.setVisualOnly(true);
							_level.addFreshEntity(entityToSpawn);
						}
						entity.getPersistentData().putDouble("BossTp", 0);
					}
				}
			}
		} else {
			if (entity.getPersistentData().getDouble("BossTp") >= 5) {
				entity.getPersistentData().putDouble("BossTp", 0);
			}
		}
		if (entity.isInWater()) {
			if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == (null))) {
				if ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).isInWater() && entity.isInWater()) {
					((LivingEntity) entity).getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(4);
				} else if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).isInWater()) && entity.isInWater()) {
					((LivingEntity) entity).getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(4);
				}
			}
		}
	}
}
