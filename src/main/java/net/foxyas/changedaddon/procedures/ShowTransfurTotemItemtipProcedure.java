package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class ShowTransfurTotemItemtipProcedure {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        execute(event, event.getItemStack(), event.getToolTip());
    }

    public static void execute(ItemStack itemstack, List<Component> tooltip) {
        execute(null, itemstack, tooltip);
    }

    private static void execute(@Nullable Event event, ItemStack itemstack, List<Component> tooltip) {
        if (tooltip == null)
            return;
        String ID = "";
        if (itemstack.getItem() == ChangedAddonItems.TRANSFUR_TOTEM.get()) {
            if ((itemstack.getOrCreateTag().getString("form")).isEmpty()) {
                tooltip.add(new TextComponent("\u00A76No Form Linked"));
            } else {
                if (Screen.hasShiftDown() && !Screen.hasAltDown() && !Screen.hasControlDown()) {
                    tooltip.add(new TextComponent(("\u00A76Form=" + itemstack.getOrCreateTag().getString("form"))));
                } else if (Screen.hasAltDown() && Screen.hasControlDown()) {
                    tooltip.add(new TextComponent((new TranslatableComponent("item.changed_addon.transfur_totem.desc_1").getString())));
                } else {
                    ID = net.ltxprogrammer.changed.item.Syringe.getVariantDescriptionId(itemstack);
                    tooltip.add(new TextComponent(("\u00A76(" + new TranslatableComponent(ID).getString() + ")")));
                }
            }
        }
    }
}
