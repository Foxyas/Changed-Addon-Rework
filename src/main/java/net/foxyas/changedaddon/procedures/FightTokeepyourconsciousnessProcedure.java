package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
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
		String form = "";
		if (world.getLevelData().getGameRules().getBoolean(ChangedAddonModGameRules.FIGHTTOKEEPCONSCIOUSNESS) == true) {
			form = (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm_ProgressTransfur;
			if (!((form).equals("changed:form_latex_crystal_wolf") || (form).equals("changed:form_latex_crystal_wolf_horned") || (form).equals("changed:form_latex_beifeng") || (form).equals("changed:form_aerosol_latex_wolf")
					|| (form).equals("changed:form_white_latex_wolf"))) {
				if (ReturnMaxTransfurToleranceProcedure.execute() > 1) {
					if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Progress_Transfur_Number >= ReturnMaxTransfurToleranceProcedure.execute() * 0.8) {
						PlayerUtilProcedure.TransfurPlayer(entity, form);
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
							boolean _setval = true;
							entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.concience_Fight = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						SetPlayerTransFurProgressFor0Procedure.execute((Player) entity);
					}
				}
			} else {
				if ((form).equals("changed:form_white_latex_wolf")) {
					if ((world.getBlockState(new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()))).getBlock() == ForgeRegistries.BLOCKS.getValue(new ResourceLocation("changed:white_latex_pillar"))
							|| (world.getBlockState(new BlockPos(entity.getX(), entity.getY(), entity.getZ()))).getBlock() == ForgeRegistries.BLOCKS.getValue(new ResourceLocation("changed:white_latex_pillar"))) {
						if (((world.getBlockState(new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()))).getBlock().getStateDefinition().getProperty("extended") instanceof BooleanProperty _getbp18
								&& (world.getBlockState(new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()))).getValue(_getbp18)) == true
								|| ((world.getBlockState(new BlockPos(entity.getX(), entity.getY(), entity.getZ()))).getBlock().getStateDefinition().getProperty("extended") instanceof BooleanProperty _getbp23
										&& (world.getBlockState(new BlockPos(entity.getX(), entity.getY(), entity.getZ()))).getValue(_getbp23)) == true) {
							if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Progress_Transfur_Number >= ReturnMaxTransfurToleranceProcedure.execute()
									* 0.8) {
								PlayerUtilProcedure.TransfurPlayer(entity, form);
								SetPlayerTransFurProgressFor0Procedure.execute((Player) entity);
							}
						}
					} else {
						if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Progress_Transfur_Number >= ReturnMaxTransfurToleranceProcedure.execute() * 0.8) {
							PlayerUtilProcedure.TransfurPlayer(entity, form);
							SetPlayerTransFurProgressFor0Procedure.execute((Player) entity);
						}
					}
				} else {
					if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Progress_Transfur_Number >= ReturnMaxTransfurToleranceProcedure.execute() * 0.8) {
						PlayerUtilProcedure.TransfurPlayer(entity, form);
						SetPlayerTransFurProgressFor0Procedure.execute((Player) entity);
					}
				}
			}
			if (entity.isAlive()) {
				if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
					if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).concience_Fight) {
						if (new Object() {
							public int getValue() {
								CompoundTag dataIndex25 = new CompoundTag();
								entity.saveWithoutId(dataIndex25);
								return dataIndex25.getInt("LatexVariantAge");
							}
						}.getValue() >= 100) {
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
								{
									boolean _setval = false;
									entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.consciousness_fight_give_up = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							} else {
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
								{
									boolean _setval = false;
									entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.consciousness_fight_give_up = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
								if (entity.isAlive()) {
									if (entity instanceof Player _player && !_player.level.isClientSide())
										_player.displayClientMessage(new TextComponent("You \u00A74Lose \u00A7rYour Conscience"), true);
									SummonEntityProcedure.execute((Level) world, (Player) entity);
									PlayerUtilProcedure.UnTransfurPlayer(entity);
									if (entity instanceof LivingEntity _entity)
										_entity.hurt(new DamageSource("concience_lose").bypassArmor(), 1200);
								}
							}
						}
					}
				}
			}
		}
	}
}
