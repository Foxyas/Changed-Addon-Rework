package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.item.Syringe;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class ShowExtraTransfurInfoProcedure {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        execute(event.getPlayer(), event.getItemStack(), event.getToolTip());
    }

    public static void execute(@Nullable Player entity, ItemStack itemstack, List<Component> tooltip) {
        if (entity == null || itemstack == null || tooltip == null) return;

        boolean isSyringe = itemstack.is(ChangedItems.LATEX_SYRINGE.get());
        boolean isFlask = itemstack.is(ChangedItems.LATEX_FLASK.get());
        boolean isArrow = itemstack.is(ChangedItems.LATEX_TIPPED_ARROW.get());

        if (!(isSyringe || isFlask || isArrow)) return;

        String form = itemstack.getOrCreateTag().getString("form");
        boolean isCreative = entity.isCreative();
        boolean hasInformantBlock = entity.getInventory().contains(new ItemStack(ChangedAddonModBlocks.INFORMANTBLOCK.get()));

        if (hasInformantBlock || isCreative) {
            if (hasInformantBlock && !Screen.hasShiftDown()) {
                String variantName = new TranslatableComponent(Syringe.getVariantDescriptionId(itemstack)).getString();
                tooltip.add(new TextComponent("Hold ").append(new TextComponent("<Shift>").withStyle(style -> style.withColor(0xFFD700)))
                        .append(" to show the stats of the " + variantName + " Transfur"));
            }

            if (Screen.hasShiftDown()) {
                double hp = VariantUtilProcedure.GetExtraHp(form, entity);
                double swimSpeed = VariantUtilProcedure.GetSwimSpeed(form, entity);
                double landSpeed = VariantUtilProcedure.GetLandSpeed(form, entity);
                double jumpStrength = VariantUtilProcedure.GetJumpStrength(form);
                boolean canFlyOrGlide = VariantUtilProcedure.CanGlideandFly(form);
                int index = Math.min(tooltip.size(), 3);

                double extraHp = (hp) / 2.0;
                tooltip.add(index, new TranslatableComponent("text.changed_addon.additionalHealth")
                        .append("")
                        .append(extraHp == 0
                                ? new TextComponent("§7None§r")
                                : new TextComponent((extraHp > 0 ? "§a+" : "§c") + extraHp + "§r"))
                        .append(new TranslatableComponent("text.changed_addon.additionalHealth.Hearts")));

                index++;
                double landSpeedPct = (landSpeed - 1) * 100;
                tooltip.add(index, new TranslatableComponent("text.changed_addon.landspeed")
                        .append("")
                        .append(landSpeedPct == 0
                                ? new TextComponent("§7None§r")
                                : new TextComponent((landSpeedPct > 0 ? "§a+" : "§c") + (int) landSpeedPct + "%")));

                index++;
                double swimSpeedPct = (swimSpeed - 1) * 100;
                tooltip.add(index, new TranslatableComponent("text.changed_addon.swimspeed")
                        .append("")
                        .append(swimSpeedPct == 0
                                ? new TextComponent("§7None§r")
                                : new TextComponent((swimSpeedPct > 0 ? "§a+" : "§c") + (int) swimSpeedPct + "%")));

                index++;
                double jumpStrengthPct = (jumpStrength - 1) * 100;
                tooltip.add(index, new TranslatableComponent("text.changed_addon.jumpStrength")
                        .append("")
                        .append(jumpStrengthPct == 0
                                ? new TextComponent("§7None§r")
                                : new TextComponent((jumpStrengthPct > 0 ? "§a+" : "§c") + (int) jumpStrengthPct + "%")));

                index++;
                tooltip.add(index, new TranslatableComponent("text.changed_addon.canGlide/Fly")
                        .append("")
                        .append(canFlyOrGlide
                                ? new TextComponent("§aTrue§r")
                                : new TextComponent("§cFalse§r")));
            }

            if (ChangedAddonTransfurVariants.isVariantOC(form, entity.getLevel())) {
                tooltip.add(new TextComponent("§8OC Transfur"));
            }
        }

        if (form.equals("changed_addon:form_ket_experiment009_boss") || form.equals("changed_addon:form_experiment_10_boss")) {
            tooltip.add(new TextComponent("§8Boss Version"));
        }
    }

}
