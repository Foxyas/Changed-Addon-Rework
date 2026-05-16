package net.foxyas.changedaddon.compatibility;

import dev.ghen.thirst.foundation.common.event.RegisterThirstValueEvent;
import dev.ghen.thirst.foundation.config.ItemSettingsConfig;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegisterThirst {

    static void register() {
        MinecraftForge.EVENT_BUS.register(new RegisterThirst());
    }

    @SubscribeEvent
    public void onRegisterThirst(RegisterThirstValueEvent event) {
        // Short it by;
        // Foods.
        // Drinks.

        // Changed Mod Stuff
        event.addFood(ChangedItems.ORANGE.get(), 2, 3);
        event.addDrink(ChangedItems.MUG_WITH_WATER.get(), 6, 8);
        event.addDrink(ChangedItems.MUG_WITH_COFFEE.get(), 4, 6);
        event.addDrink(ChangedItems.MUG_WITH_MILK.get(), 4, 6);
        event.addDrink(ChangedItems.MUG_WITH_DARK_LATEX.get(), 4, 6);
        event.addDrink(ChangedItems.MUG_WITH_WHITE_LATEX.get(), 4, 6);

        // Changed Addon Stuff.
        event.addFood(ChangedAddonItems.GOLDEN_ORANGE.get(), 2, 6);
        event.addDrink(ChangedAddonItems.POT_WITH_CAMONIA.get(), 6, 8);
        event.addDrink(ChangedAddonItems.ORANGE_JUICE.get(), 6, 8);
        event.addDrink(ChangedAddonItems.FOXTA.get(), 6, 8);
        event.addDrink(ChangedAddonItems.SNEPSI.get(), 6, 8);
        event.addDrink(ChangedAddonItems.OPENED_CANNED_SOUP.get(), 4, 5);
    }
}
