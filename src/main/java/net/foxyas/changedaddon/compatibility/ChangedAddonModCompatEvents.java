package net.foxyas.changedaddon.compatibility;

import net.minecraftforge.fml.ModList;

public class ChangedAddonModCompatEvents {

    public static void registerOptionalEvents() {
        ModList list = ModList.get();

        // Verifica se o mod TACZ está presente
        if (list.isLoaded("tacz")) {
            // Só chama a classe que contém o evento se o mod existir
            TACZEvents.register();
        }
        
        if (list.isLoaded("jeg")) {
            JEGSEvents.register();
        }

        if (list.isLoaded("thirst")) {
            RegisterThirst.register();
        }
    }
}