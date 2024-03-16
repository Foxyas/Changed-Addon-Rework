package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.GameType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

public class LeapProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double motionZ = 0;
		double deltaZ = 0;
		double distance = 0;
		double deltaX = 0;
		double motionY = 0;
		double deltaY = 0;
		double motionX = 0;
		double maxSpeed = 0;
		double speed = 0;
		double Yspeed = 0;
		if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
			if (CanLeapProcedure.execute(entity)) {
				if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(ChangedAddonModMobEffects.FADIGE.get()) : false)) {
					if ((entity instanceof Player _plr ? _plr.getFoodData().getFoodLevel() : 0) > 6) {
						if (entity.isOnGround() && !entity.isInWater()) {
							if (!entity.isShiftKeyDown()) {
								deltaX = -Math.sin((entity.getYRot() / 180) * (float) Math.PI);
								deltaY = -Math.sin((entity.getXRot() / 180) * (float) Math.PI);
								deltaZ = Math.cos((entity.getYRot() / 180) * (float) Math.PI);
								speed = 0.4;
								motionX = deltaX * speed;
								motionY = deltaY * speed;
								motionZ = deltaZ * speed;
								entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
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
								}.checkGamemode(entity))) {
									if (entity instanceof Player _player)
										_player.causeFoodExhaustion((float) 0.25);
									if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
										_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 40, 0, false, false));
								}
								{
									Entity _ent = entity;
									if (!_ent.level.isClientSide() && _ent.getServer() != null)
										_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
								}
							}
							if (entity.isShiftKeyDown()) {
								deltaX = -Math.sin((entity.getYRot() / 180) * (float) Math.PI);
								deltaY = entity.level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(1)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()
										- entity.getY();
								deltaZ = Math.cos((entity.getYRot() / 180) * (float) Math.PI);
								speed = 0.15;
								Yspeed = 0.5;
								motionX = deltaX * speed;
								motionY = deltaY * Yspeed;
								motionZ = deltaZ * speed;
								entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
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
								}.checkGamemode(entity))) {
									if (entity instanceof Player _player)
										_player.causeFoodExhaustion((float) (motionY * 0.25));
									if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
										_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 40, 0, false, false));
								}
								{
									Entity _ent = entity;
									if (!_ent.level.isClientSide() && _ent.getServer() != null)
										_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
								}
							}
						}
					}
				}
			} else if (CanLeapProcedure.flyentity(entity)) {
				if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(ChangedAddonModMobEffects.FADIGE.get()) : false)) {
					if ((entity instanceof Player _plr ? _plr.getFoodData().getFoodLevel() : 0) > 8) {
						if (entity instanceof Player player && player.getAbilities().flying && !(entity instanceof LivingEntity _livEnt ? _livEnt.isFallFlying() : false)) {
							deltaX = -Math.sin((entity.getYRot() / 180) * (float) Math.PI);
							deltaY = -Math.sin((entity.getXRot() / 180) * (float) Math.PI);
							deltaZ = Math.cos((entity.getYRot() / 180) * (float) Math.PI);
							speed = 2;
							motionX = deltaX * speed;
							motionY = deltaY * speed;
							motionZ = deltaZ * speed;
							entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
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
							}.checkGamemode(entity))) {
								if (entity instanceof Player _player)
									_player.causeFoodExhaustion((float) 0.3);
								if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
									_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 20, 0, false, false));
							}
							{
								Entity _ent = entity;
								if (!_ent.level.isClientSide() && _ent.getServer() != null)
									_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
							}
						}
					}
				}
			}
		}
	}
}
