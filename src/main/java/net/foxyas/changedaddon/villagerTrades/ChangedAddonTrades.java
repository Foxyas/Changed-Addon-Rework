package net.foxyas.changedaddon.villagerTrades;

import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.init.ChangedAddonModVillagerProfessions;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ltxprogrammer.changed.init.ChangedItems;

import java.util.Random;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangedAddonTrades {
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        if (event.getType() == ChangedAddonModVillagerProfessions.SCIENTIST.get()) {
            CompoundTag darkLatexLaethin = new CompoundTag();
            darkLatexLaethin.putString("type", "dark_latex");
            CompoundTag WhiteLatexLaethin = new CompoundTag();
            darkLatexLaethin.putString("type", "white_latex");
            // Level 1 Trades
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ChangedAddonModItems.IMPUREAMMONIA.get()), new ItemStack(Items.EMERALD), new ItemStack(ChangedAddonModItems.AMMONIAPARTICLE.get(), 4), 15, 10, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ChangedAddonModItems.AMMONIAPARTICLE.get(), 16), new ItemStack(Items.EMERALD, 3), 15, 10, 0.05f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3 ), new ItemStack(ChangedAddonModItems.UNLATEXBASE.get()), 10, 5, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3 ), new ItemStack(ChangedItems.LATEX_BASE.get()), 10, 5, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.IRON_INGOT, 1 ), new ItemStack(ChangedItems.SYRINGE.get()), 10, 5, 0.02f));

            // Level 2 Trades
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ChangedItems.DARK_LATEX_MASK.get()), new ItemStack(Items.EMERALD, 2 ), 10, 10, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ChangedItems.WHITE_LATEX_GOO.get(), 3 ), new ItemStack(Items.EMERALD), 10, 10, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ChangedItems.DARK_LATEX_GOO.get(), 3 ), new ItemStack(Items.EMERALD), 10, 10, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 1 ), new ItemStack(ChangedItems.WHITE_LATEX_GOO.get()), 10, 15, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 1 ), new ItemStack(ChangedItems.DARK_LATEX_GOO.get()), 10, 15, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 2 ), new ItemStack(ChangedAddonModItems.AMMONIA_COMPRESSED.get(), 4), 15, 10, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 4 ), new ItemStack(ChangedAddonModItems.AMMONIA.get(), 4), 10, 15, 0.02f));

            // Level 3 Trades
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(ChangedAddonModItems.LATEX_INSULATOR.get()), new ItemStack(Items.EMERALD, 2), 15, 10, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(ChangedBlocks.DARK_LATEX_BLOCK.get().asItem()), new ItemStack(Items.EMERALD, 4 ), 12, 15, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(ChangedBlocks.WHITE_LATEX_BLOCK.get().asItem()), new ItemStack(Items.EMERALD, 4 ), 13, 15, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 5 ), new ItemStack(ChangedAddonModItems.LITIX_CAMONIA.get(), 5), 20, 15, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 1 ), new ItemStack(ChangedAddonModItems.SIGNAL_CATCHER.get()), 10, 15, 0.02f));

            // Level 4 Trades
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 2 ), new ItemStack(ChangedAddonModItems.LAETHIN.get(), 5,darkLatexLaethin), 10, 10, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 2 ), new ItemStack(ChangedAddonModItems.LAETHIN.get(), 5,WhiteLatexLaethin), 10, 10, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 5 ), new ItemStack(ChangedAddonModItems.SYRINGEWITHLITIXCAMMONIA.get(), 1), 10, 25, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 4 ), new ItemStack(ChangedAddonModItems.LAETHIN_SYRINGE.get(), 1, darkLatexLaethin), 10, 25, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 4 ), new ItemStack(ChangedAddonModItems.LAETHIN_SYRINGE.get(), 1, WhiteLatexLaethin), 10, 25, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.REDSTONE, 2 ), new ItemStack(ChangedAddonModItems.UNIFUSER.get()), 10, 20, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.COPPER_INGOT, 2 ), new ItemStack(ChangedBlocks.INFUSER.get().asItem()), 10, 20, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.REDSTONE, 2 ), new ItemStack(ChangedAddonModItems.CATLYZER.get()), 10, 20, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.COPPER_INGOT, 2 ), new ItemStack(ChangedBlocks.PURIFIER.get().asItem()), 10, 20, 0.02f));

            // Level 5 Trades
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 2 ), new ItemStack(ChangedAddonModItems.PAINITE_ORE.get()), 25, 10, 0.02f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 2 ), new ItemStack(ChangedAddonModItems.SIGNAL_BLOCK.get()), 20, 10, 0.02f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD), new ItemStack(ChangedAddonModItems.DORMANT_DARK_LATEX.get()), 10, 20, 0.02f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD), new ItemStack(ChangedAddonModItems.DORMANT_WHITE_LATEX.get()), 10, 20, 0.02f));
        }
    }
}
