package net.foxyas.changedaddon.procedures;

import org.checkerframework.checker.units.qual.Speed;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class ShowExtraTransfurInfoProcedure {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		execute(event, event.getPlayer(), event.getItemStack(), event.getToolTip());
	}

	public static void execute(Entity entity, ItemStack itemstack, List<Component> tooltip) {
		execute(null, entity, itemstack, tooltip);
	}

	private static void execute(@Nullable Event event, Entity entity, ItemStack itemstack, List<Component> tooltip) {
		if (entity == null || tooltip == null)
			return;
		String Item_form = "";
		boolean canFlyOrGlide = false;
		double Hp = 0;
		double Speed = 0;
		double landSpeed = 0;
		double JumpLevel = 0;
		if (itemstack.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:latex_syringe")) || itemstack.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:latex_tipped_arrow"))
				|| itemstack.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:latex_flask"))) {
			if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_ket_experiment009_boss")) {
				tooltip.add(new TextComponent("\u00A78Boss Version\u00A7r"));
			} else if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_experiment_10_boss")) {
				tooltip.add(new TextComponent("\u00A78Boss Version\u00A7r"));
			}
			if (new Object() {
				public boolean checkGamemode(Entity _ent) {
					if (_ent instanceof ServerPlayer _serverPlayer) {
						return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
					} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
						return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
								&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
					}
					return false;
				}
			}.checkGamemode(entity)) {
				if (Screen.hasShiftDown()) {
					Item_form = itemstack.getOrCreateTag().getString("form");
					Hp = (double) VariantUtilProcedure.GetExtraHp(Item_form, (Player) entity);
					Speed = (double) VariantUtilProcedure.GetSwimSpeed(Item_form, (Player) entity);
					landSpeed = (double) VariantUtilProcedure.GetLandSpeed(Item_form, (Player) entity);
					JumpLevel = (double) VariantUtilProcedure.GetJumpStrength(Item_form);
					canFlyOrGlide = VariantUtilProcedure.CanGlideandFly(Item_form);
					tooltip.add(new TextComponent(((new TranslatableComponent("text.changed_addon.additionalHealth").getString() + "" + (Hp > 0 ? "\u00A7a+" + Hp / 2 + "\u00A7r" : "\u00A7c" + Hp / 2 + "\u00A7r")
							+ new TranslatableComponent("text.changed_addon.additionalHealth.Hearts").getString()) + "§r\n"
							+ (new TranslatableComponent("text.changed_addon.landspeed").getString() + "" + (landSpeed >= 0 ? "\u00A7a+" + (float) (landSpeed * 100) + "%" : "\u00A7c" + (float) (landSpeed * 100) + "%")) + "§r\n"
							+ (new TranslatableComponent("text.changed_addon.swimspeed").getString() + "" + (Speed >= 0 ? "\u00A7a+" + (float) (Speed * 100) + "%" : "\u00A7c" + (float) (Speed * 100) + "%")) + "§r\n"
							+ (new TranslatableComponent("text.changed_addon.jumpStrength").getString() + "" + (JumpLevel >= 1 ? "\u00A7a+" + Math.round(JumpLevel * 100) + "%" : "\u00A7c-" + Math.round(JumpLevel * 100) + "%")) + "§r\n"
							+ (new TranslatableComponent("text.changed_addon.canGlide/Fly").getString() + "" + (canFlyOrGlide == true ? "\u00A7a" + canFlyOrGlide + "\u00A7r" : "\u00A7c" + canFlyOrGlide + "\u00A7r")))));
				}
				if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_buny")) {
					tooltip.add(new TextComponent("\u00A78OC Transfur\u00A7r"));
				} else if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_exp6")) {
					tooltip.add(new TextComponent("\u00A78OC Transfur\u00A7r"));
				} else if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_reyn")) {
					tooltip.add(new TextComponent("\u00A78OC Transfur\u00A7r"));
				} else if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_wolfy")) {
					tooltip.add(new TextComponent("\u00A78Dev Helper OC Transfur\u00A7r"));
				} else if ((itemstack.getOrCreateTag().getString("form")).startsWith("changed_addon:form_experiment_10")) {
					tooltip.add(new TextComponent("\u00A78OC Transfur\u00A7r"));
				}
			}
		}
	}
}
