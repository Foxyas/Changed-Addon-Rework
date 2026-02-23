package net.foxyas.changedaddon.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Experiment009dnaItem extends Item {
    public Experiment009dnaItem() {
        super(new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .stacksTo(64).fireResistant().rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.translatable("item.changed_addon.experiment_009_dna.description"));
    }
}
