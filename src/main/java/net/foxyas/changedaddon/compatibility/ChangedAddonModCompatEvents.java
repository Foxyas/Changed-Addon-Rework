package net.foxyas.changedaddon.compatibility;

import net.minecraftforge.fml.ModList;

public class ChangedAddonModCompatEvents {

    public static void registerOptionalEvents() {
        // Verifica se o mod TACZ está presente
        if (ModList.get().isLoaded("tacz")) {
            // Só chama a classe que contém o evento se o mod existir
            TACZEvents.register();
        }
        
        if (ModList.get().isLoaded("jeg")) {
            JEGSEvents.register();
        }
    }
}