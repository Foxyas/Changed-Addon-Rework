package net.foxyas.changedaddon.procedures;

import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.world.inventory.FightTokeepconsciousnessminigameMenu;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModGameRules;

import javax.annotation.Nullable;

import io.netty.buffer.Unpooled;

@Mod.EventBusSubscriber
public class FightTokeepyourconsciousnessProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level, event.player.getX(), event.player.getY(), event.player.getZ(), event.player);
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (world.getLevelData().getGameRules().getBoolean(ChangedAddonModGameRules.FIGHTTOKEEPCONSCIOUSNESS) == true) {
			if (!(((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm_ProgressTransfur).equals("changed:form_latex_crystal_wolf")
					|| ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm_ProgressTransfur).equals("changed:form_latex_crystal_wolf_horned")
					|| ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm_ProgressTransfur).equals("changed:form_latex_beifeng")
					|| ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm_ProgressTransfur).equals("changed:form_aerosol_latex_wolf")
					|| ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm_ProgressTransfur).equals("changed:form_white_latex_wolf"))) {
				if (ReturnMaxTransfurToleranceProcedure.execute() > 1) {
					if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Progress_Transfur_Number >= ReturnMaxTransfurToleranceProcedure.execute() * 0.8) {
						{
							Entity _ent = entity;
							if (!_ent.level.isClientSide() && _ent.getServer() != null)
								_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
										("transfur @s " + (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm_ProgressTransfur));
						}
						{
							if (entity instanceof ServerPlayer _ent) {
								BlockPos _bpos = new BlockPos(x, y, z);
								NetworkHooks.openGui((ServerPlayer) _ent, new MenuProvider() {
									@Override
									public Component getDisplayName() {
										return new TextComponent("FightTokeepconsciousnessminigame");
									}

									@Override
									public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
										return new FightTokeepconsciousnessminigameMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
									}
								}, _bpos);
							}
						}
						{
							String _setval = ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm_ProgressTransfur).replace("form_", "");
							entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.LatexEntitySummon = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							String _setval = ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexEntitySummon).replace("/", "_");
							entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.LatexEntitySummon = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = true;
							entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.concience_Fight = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						SetPlayerTransFurProgressFor0Procedure.execute((Player) entity);
						if (entity.isAlive()) {
							new Object() {
								private int ticks = 0;
								private float waitTicks;
								private LevelAccessor world;

								public void start(LevelAccessor world, int waitTicks) {
									this.waitTicks = waitTicks;
									MinecraftForge.EVENT_BUS.register(this);
									this.world = world;
								}

								@SubscribeEvent
								public void tick(TickEvent.ServerTickEvent event) {
									if (event.phase == TickEvent.Phase.END) {
										this.ticks += 1;
										if (this.ticks >= this.waitTicks)
											run();
									}
								}

								private void run() {
									if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).consciousness_fight_progress >= 25) {
										if (entity instanceof Player _player && !_player.level.isClientSide())
											_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.fight_concience.success").getString())), true);
										{
											boolean _setval = false;
											entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
												capability.concience_Fight = _setval;
												capability.syncPlayerVariables(entity);
											});
										}
										{
											double _setval = 0;
											entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
												capability.consciousness_fight_progress = _setval;
												capability.syncPlayerVariables(entity);
											});
										}
									} else {
										{
											double _setval = 0;
											entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
												capability.consciousness_fight_progress = _setval;
												capability.syncPlayerVariables(entity);
											});
										}
										{
											boolean _setval = false;
											entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
												capability.concience_Fight = _setval;
												capability.syncPlayerVariables(entity);
											});
										}
										if (entity.isAlive()) {
											if (entity instanceof Player _player && !_player.level.isClientSide())
												_player.displayClientMessage(new TextComponent("You \u00A74Lose \u00A7rYour Conscience"), true);
											{
												Entity _ent = entity;
												if (!_ent.level.isClientSide() && _ent.getServer() != null)
													_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "untransfur @s");
											}
											{
												Entity _ent = entity;
												if (!_ent.level.isClientSide() && _ent.getServer() != null)
													_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
															("summon " + (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexEntitySummon + " ~ ~ ~"));
											}
											if (entity instanceof LivingEntity _entity)
												_entity.hurt(new DamageSource("concience_lose").bypassArmor(), 1200);
										}
									}
									MinecraftForge.EVENT_BUS.unregister(this);
								}
							}.start(world, (int) (20 * 4));
						}
					}
				}
			} else {
				if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm_ProgressTransfur).equals("changed:form_white_latex_wolf")) {
					if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Progress_Transfur_Number >= ReturnMaxTransfurToleranceProcedure.execute() * 0.5) {
						{
							Entity _ent = entity;
							if (!_ent.level.isClientSide() && _ent.getServer() != null)
								_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
										("transfur @s " + (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm_ProgressTransfur));
						}
						SetPlayerTransFurProgressFor0Procedure.execute((Player) entity);
					}
				} else {
					if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Progress_Transfur_Number >= ReturnMaxTransfurToleranceProcedure.execute() * 0.5) {
						{
							Entity _ent = entity;
							if (!_ent.level.isClientSide() && _ent.getServer() != null)
								_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
										("transfur @s " + (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm_ProgressTransfur));
						}
						SetPlayerTransFurProgressFor0Procedure.execute((Player) entity);
					}
				}
			}
		}
	}
}
