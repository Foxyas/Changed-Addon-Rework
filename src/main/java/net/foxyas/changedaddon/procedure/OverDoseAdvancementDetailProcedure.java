package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OverDoseAdvancementDetailProcedure {

    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        Player player = event.getEntity();
        Level level = player.level;
        Advancement advancement = event.getAdvancement();

        if (advancement == null) return;
        if (level.getServer() != null && advancement.getId().equals(ChangedAddonMod.resourceLoc("over_dose"))) {
            player.hurt(new DamageSource(Holder.direct(new DamageType("OverDose", 0.1f))), 10);
        }
    }
}
