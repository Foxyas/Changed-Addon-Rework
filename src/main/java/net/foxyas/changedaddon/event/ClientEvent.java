package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.process.util.TransfurVariantUtils;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.item.Syringe;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        showExtraTransfurInfo(event.getPlayer(), event.getItemStack(), event.getToolTip());
    }

    public static void showExtraTransfurInfo(@Nullable Player entity, ItemStack itemstack, List<Component> tooltip) {
        if (entity == null || itemstack == null || tooltip == null) return;

        if (!(itemstack.is(ChangedItems.LATEX_SYRINGE.get()) || itemstack.is(ChangedItems.LATEX_FLASK.get())
                || itemstack.is(ChangedItems.LATEX_TIPPED_ARROW.get()))) return;

        ResourceLocation loc = ResourceLocation.tryParse(itemstack.getOrCreateTag().getString("form"));
        if(loc == null) return;

        //boolean hasInformantBlock = entity.getInventory().contains(new ItemStack(ChangedAddonModBlocks.INFORMANTBLOCK.get()));

//        if (hasInformantBlock || isCreative) {
//            if (hasInformantBlock && !Screen.hasShiftDown()) {
//                String variantName = new TranslatableComponent(Syringe.getVariantDescriptionId(itemstack)).getString();
//                tooltip.add(new TextComponent("Hold ").append(new TextComponent("<Shift>").withStyle(style -> style.withColor(0xFFD700)))
//                        .append(" to show the stats of the " + variantName + " Transfur"));
//            }

        if (entity.isCreative()) {
            if (!Screen.hasShiftDown()) {
                String variantName = new TranslatableComponent(Syringe.getVariantDescriptionId(itemstack)).getString();
                tooltip.add(new TextComponent("Hold ").append(new TextComponent("<Shift>").withStyle(style -> style.withColor(0xFFD700)))
                        .append(" to show the stats of the " + variantName + " Transfur"));
            } else {
                int index = Math.min(tooltip.size(), 3);

                float extraHp = TransfurVariantUtils.GetExtraHp(loc, entity) / 2f;
                tooltip.add(index, new TranslatableComponent("text.changed_addon.additionalHealth")
                        .append("")
                        .append(extraHp == 0
                                ? new TextComponent("§7None§r")
                                : new TextComponent((extraHp > 0 ? "§a+" : "§c") + extraHp + "§r"))
                        .append(new TranslatableComponent("text.changed_addon.additionalHealth.Hearts")));

                index++;
                tooltip.add(index, new TranslatableComponent("text.changed_addon.miningStrength", TransfurVariantUtils.getMiningStrength(loc)));

                index++;
                float landSpeed = TransfurVariantUtils.GetLandSpeed(loc, entity);
                float landSpeedPct = landSpeed == 0 ? 0 : (landSpeed - 1) * 100;
                tooltip.add(index, new TranslatableComponent("text.changed_addon.land_speed")
                        .append("")
                        .append(landSpeedPct == 0
                                ? new TextComponent("§7None§r")
                                : new TextComponent((landSpeedPct > 0 ? "§a+" : "§c") + (int) landSpeedPct + "%")));

                index++;
                float swimSpeed = TransfurVariantUtils.GetSwimSpeed(loc, entity);
                float swimSpeedPct = swimSpeed == 0 ? 0 : (swimSpeed - 1) * 100;
                tooltip.add(index, new TranslatableComponent("text.changed_addon.swim_speed")
                        .append("")
                        .append(swimSpeedPct == 0
                                ? new TextComponent("§7None§r")
                                : new TextComponent((swimSpeedPct > 0 ? "§a+" : "§c") + (int) swimSpeedPct + "%")));

                index++;
                float jumpStrength = TransfurVariantUtils.GetJumpStrength(loc);
                float jumpStrengthPct = jumpStrength == 0 ? 0 : (jumpStrength - 1) * 100;
                tooltip.add(index, new TranslatableComponent("text.changed_addon.jumpStrength")
                        .append("")
                        .append(jumpStrengthPct == 0
                                ? new TextComponent("§7None§r")
                                : new TextComponent((jumpStrengthPct > 0 ? "§a+" : "§c") + (int) jumpStrengthPct + "%")));

                index++;
                tooltip.add(index, new TranslatableComponent("text.changed_addon.canGlide/Fly")
                        .append("")
                        .append(TransfurVariantUtils.CanGlideandFly(loc)
                                ? new TextComponent("§aTrue§r")
                                : new TextComponent("§cFalse§r")));
            }

            if (ChangedAddonTransfurVariants.isVariantOC(loc, entity.getLevel())) {
                tooltip.add(new TextComponent("§8OC Transfur"));
            }
        }

        if (ChangedAddonTransfurVariants.getBossesVariantsList().stream().anyMatch(variant ->
                variant.getFormId().equals(loc))) {
            tooltip.add(new TextComponent("§8Boss Version"));
        }
    }
}
