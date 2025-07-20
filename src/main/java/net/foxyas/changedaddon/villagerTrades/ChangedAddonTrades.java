package net.foxyas.changedaddon.villagerTrades;

import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonModVillagerProfessions;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.minecraft.nbt.CompoundTag;
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
            Random PriceRandom = new Random();

            // Level 1 Trades
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 1), new ItemStack(ChangedAddonItems.IMPUREAMMONIA.get(), 1), new ItemStack(ChangedAddonItems.AMMONIAPARTICLE.get(), 6), 8, 10, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 4 ), new ItemStack(ChangedAddonItems.ANTI_LATEX_BASE.get()), 12, 5, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3 + PriceRandom.nextInt(3)), new ItemStack(ChangedItems.LATEX_BASE.get(), 1 + PriceRandom.nextInt(4)), 12, 5, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ChangedItems.LATEX_BASE.get(), 2), new ItemStack(Items.EMERALD, 1 + PriceRandom.nextInt(3)), 12, 5, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.IRON_INGOT, 1), new ItemStack(ChangedItems.SYRINGE.get(), 4), 12, 5, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ChangedItems.SYRINGE.get(), 8), new ItemStack(Items.EMERALD, 1), 8, 5, 0.02f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 4), new ItemStack(ChangedAddonItems.IMPUREAMMONIA.get(), 2), 16, 15, 0.02f));

            // Level 2 Trades
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ChangedItems.DARK_LATEX_MASK.get()), new ItemStack(Items.EMERALD, 3), 12, 10, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ChangedItems.WHITE_LATEX_GOO.get(), 16), new ItemStack(Items.EMERALD), 12, 10, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ChangedItems.DARK_LATEX_GOO.get(), 16), new ItemStack(Items.EMERALD), 12, 10, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 1), new ItemStack(ChangedItems.WHITE_LATEX_GOO.get(), 2), 12, 15, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 1), new ItemStack(ChangedItems.DARK_LATEX_GOO.get(), 2), 12, 15, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10), new ItemStack(ChangedAddonItems.AMMONIA_COMPRESSED.get(), 2), 8, 10, 0.02f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 12), new ItemStack(ChangedAddonItems.AMMONIA.get(), 2), 8, 15, 0.02f));

            // Level 3 Trades
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(ChangedAddonItems.LATEX_INSULATOR.get(), 4), new ItemStack(Items.EMERALD, 1), 12, 10, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(ChangedBlocks.DARK_LATEX_BLOCK.get().asItem(), 4), new ItemStack(Items.EMERALD, 1), 12, 15, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(ChangedBlocks.WHITE_LATEX_BLOCK.get().asItem(), 4), new ItemStack(Items.EMERALD, 1), 12, 15, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 4), new ItemStack(ChangedAddonItems.LITIX_CAMONIA.get(), 2), 12, 15, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 1), new ItemStack(ChangedAddonItems.SIGNAL_CATCHER.get()), 12, 15, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 4), new ItemStack(ChangedAddonItems.SNEPSI.get(), 1), 12, 15, 0.02f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 2), new ItemStack(ChangedAddonItems.FOXTA.get(), 1), 12, 15, 0.02f));

            // Level 4 Trades
            ItemStack Laethin = new ItemStack(ChangedAddonItems.LAETHIN.get(), 2);
            ItemStack LaethinSyringe = new ItemStack(ChangedAddonItems.LAETHIN_SYRINGE.get(), 1);
            ItemStack DarkLaethin = new ItemStack(ChangedAddonItems.LAETHIN.get(), 2);
            ItemStack DarkLaethinSyringe = new ItemStack(ChangedAddonItems.LAETHIN_SYRINGE.get(), 1);
            Laethin.setTag(WhiteLatexLaethin);
            DarkLaethin.setTag(darkLatexLaethin);
            LaethinSyringe.setTag(WhiteLatexLaethin);
            DarkLaethinSyringe.setTag(darkLatexLaethin);

            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 12 + PriceRandom.nextInt(11)), Laethin, 8, 18, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 12 + PriceRandom.nextInt(11)), DarkLaethin, 8, 18, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 7 + PriceRandom.nextInt(11)), new ItemStack(ChangedAddonItems.SYRINGEWITHLITIXCAMMONIA.get(), 1), 12, 25, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10 + PriceRandom.nextInt(11)), LaethinSyringe, 8, 25, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10 + PriceRandom.nextInt(11)), DarkLaethinSyringe, 8, 25, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8 + PriceRandom.nextInt(11)), new ItemStack(Items.REDSTONE, 16 + PriceRandom.nextInt(6)), new ItemStack(ChangedAddonItems.UNIFUSER.get()), 8, 20, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 4 + PriceRandom.nextInt(11)), new ItemStack(Items.IRON_INGOT, 1 + PriceRandom.nextInt(6)), new ItemStack(ChangedBlocks.INFUSER.get().asItem()), 8, 20, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8 + PriceRandom.nextInt(11)), new ItemStack(Items.REDSTONE, 8 + PriceRandom.nextInt(6)), new ItemStack(ChangedAddonItems.CATALYZER.get()), 8, 20, 0.02f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8 + PriceRandom.nextInt(11)), new ItemStack(Items.IRON_INGOT, 4 + PriceRandom.nextInt(6)), new ItemStack(ChangedBlocks.PURIFIER.get().asItem()), 8, 20, 0.02f));

            // Level 5 Trades
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(ChangedAddonItems.PAINITE.get()), new ItemStack(Items.EMERALD, 24 + PriceRandom.nextInt(10)), 6, 35, 0.15f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 32), new ItemStack(ChangedAddonItems.SIGNAL_BLOCK.get()), 6, 10, 0.02f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD), new ItemStack(ChangedAddonItems.DORMANT_DARK_LATEX.get()), 16, 20, 0.02f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD), new ItemStack(ChangedAddonItems.DORMANT_WHITE_LATEX.get()), 16, 20, 0.02f));
        }
    }
}