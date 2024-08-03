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
            WhiteLatexLaethin.putString("type", "white_latex");
            // Level 1 Trades
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 1 ), new ItemStack(ChangedAddonModItems.IMPUREAMMONIA.get(), 1), new ItemStack(ChangedAddonModItems.AMMONIAPARTICLE.get(), 4), 16, 10, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ChangedAddonModItems.AMMONIAPARTICLE.get(), 16), new ItemStack(Items.EMERALD, 3), 16, 10, 0.05f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3), new ItemStack(ChangedAddonModItems.AMMONIAPARTICLE.get(), 16), 16, 10, 0.05f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3 ), new ItemStack(ChangedAddonModItems.UNLATEXBASE.get()), 12, 5, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3 ), new ItemStack(ChangedItems.LATEX_BASE.get()), 12, 5, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ChangedItems.LATEX_BASE.get(), 2 ), new ItemStack(Items.EMERALD), 12, 5, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.IRON_INGOT, 1 ), new ItemStack(ChangedItems.SYRINGE.get(), 6), 12, 5, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ChangedItems.SYRINGE.get(), 4 ), new ItemStack(Items.EMERALD, 1), 8, 5, 0.02f));

            // Level 2 Trades
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ChangedItems.DARK_LATEX_MASK.get()), new ItemStack(Items.EMERALD, 3), 12, 10, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ChangedItems.WHITE_LATEX_GOO.get(), 16), new ItemStack(Items.EMERALD), 12, 10, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ChangedItems.DARK_LATEX_GOO.get(), 16), new ItemStack(Items.EMERALD), 12, 10, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 1 ), new ItemStack(ChangedItems.WHITE_LATEX_GOO.get()), 12, 15, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 1 ), new ItemStack(ChangedItems.DARK_LATEX_GOO.get()), 12, 15, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3 ), new ItemStack(ChangedAddonModItems.AMMONIA_COMPRESSED.get(), 4), 15, 10, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 4 ), new ItemStack(ChangedAddonModItems.AMMONIA.get(), 4), 12, 15, 0.02f));

            // Level 3 Trades
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(ChangedAddonModItems.LATEX_INSULATOR.get(), 4), new ItemStack(Items.EMERALD, 1), 12, 10, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(ChangedBlocks.DARK_LATEX_BLOCK.get().asItem(), 4), new ItemStack(Items.EMERALD, 1), 12, 15, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(ChangedBlocks.WHITE_LATEX_BLOCK.get().asItem(), 4), new ItemStack(Items.EMERALD, 1), 12, 15, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 4 ), new ItemStack(ChangedAddonModItems.LITIX_CAMONIA.get(), 2), 12, 15, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 1 ), new ItemStack(ChangedAddonModItems.SIGNAL_CATCHER.get()), 12, 15, 0.02f));

            // Level 4 Trades
            ItemStack Laethin = new ItemStack(ChangedAddonModItems.LAETHIN.get(), 5);
            ItemStack LaethinSyringe = new  ItemStack(ChangedAddonModItems.LAETHIN_SYRINGE.get(), 1);
            ItemStack DarkLaethin = new ItemStack(ChangedAddonModItems.LAETHIN.get(), 5);
            ItemStack DarkLaethinSyringe = new  ItemStack(ChangedAddonModItems.LAETHIN_SYRINGE.get(), 1);
			Laethin.setTag(WhiteLatexLaethin);
          	DarkLaethin.setTag(darkLatexLaethin);
            LaethinSyringe.setTag(WhiteLatexLaethin);
            DarkLaethinSyringe.setTag(darkLatexLaethin);

            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 5 ),Laethin, 12, 18, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 5 ),DarkLaethin, 12, 18, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3 ), new ItemStack(ChangedAddonModItems.SYRINGEWITHLITIXCAMMONIA.get(), 1), 12, 25, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 6 ),LaethinSyringe, 12, 25, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 6 ),DarkLaethinSyringe, 12, 25, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8 ), new ItemStack(Items.REDSTONE, 16 ), new ItemStack(ChangedAddonModItems.UNIFUSER.get()), 8, 20, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 4 ), new ItemStack(Items.IRON_INGOT, 2 ), new ItemStack(ChangedBlocks.INFUSER.get().asItem()), 8, 20, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8 ), new ItemStack(Items.REDSTONE, 8 ), new ItemStack(ChangedAddonModItems.CATLYZER.get()), 8, 20, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8 ), new ItemStack(Items.IRON_INGOT, 4 ), new ItemStack(ChangedBlocks.PURIFIER.get().asItem()), 8, 20, 0.02f));

            // Level 5 Trades
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 12 ), new ItemStack(ChangedAddonModItems.PAINITE.get()), 12, 10, 0.02f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32 ), new ItemStack(ChangedAddonModItems.SIGNAL_BLOCK.get()), 12, 10, 0.02f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD), new ItemStack(ChangedAddonModItems.DORMANT_DARK_LATEX.get()), 16, 20, 0.02f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD), new ItemStack(ChangedAddonModItems.DORMANT_WHITE_LATEX.get()), 16, 20, 0.02f));
        }
    }
}
