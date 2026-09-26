package net.foxyas.changedaddon.compatibility;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.fml.ModList;

public class ChangedAddonModCompatEvents {

    public static void registerOptionalEvents() {
        ModList list = ModList.get();

        // Verifica se o mod TACZ está presente
        if (list.isLoaded("tacz")) {
            // Só chama a classe que contém o evento se o mod existir
            TACZCompatibility.register();
        }
        
        if (list.isLoaded("jeg")) {
            JEGSCompatibility.register();
        }

        if (list.isLoaded("thirst")) {
            ThirstCompatibility.register();
        }
    }

    public static boolean isDamageTypeBullet(DamageSource source) {
        return isDamageTypeBulletTACZ(source) || isDamageTypeBulletJEG(source);
    }

    public static boolean isDamageTypeBulletTACZ(DamageSource damageSource) {
        return ModList.get().isLoaded("tacz") && TACZCompatibility.isDamageTypeBullet(damageSource);
    }

    public static boolean isDamageTypeBulletJEG(DamageSource damageSource) {
        return ModList.get().isLoaded("jeg") && JEGSCompatibility.isDamageTypeBullet(damageSource);
    }
}