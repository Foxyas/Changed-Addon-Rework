package net.foxyas.changedaddon.compatibility;

import dev.ghen.thirst.foundation.common.event.RegisterThirstValueEvent;
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
        event.addDrink(ChangedItems.MUG_WITH_WATER.get(), 6, 8);
        event.addDrink(ChangedItems.ORANGE.get(), 2, 3);

        event.addDrink(ChangedAddonItems.POT_WITH_CAMONIA.get(), 6, 8);
        event.addDrink(ChangedAddonItems.ORANGE_JUICE.get(), 6, 8);
        event.addDrink(ChangedAddonItems.FOXTA.get(), 6, 8);
        event.addDrink(ChangedAddonItems.SNEPSI.get(), 6, 8);
        event.addDrink(ChangedAddonItems.OPENED_CANNED_SOUP.get(), 4, 5);
        event.addDrink(ChangedAddonItems.GOLDEN_ORANGE.get(), 2, 3);
    }
}
