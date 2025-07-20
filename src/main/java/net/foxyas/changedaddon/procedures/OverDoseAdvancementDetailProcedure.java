package net.foxyas.changedaddon.procedures;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class OverDoseAdvancementDetailProcedure {
    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        execute(event, event.getPlayer().level, event.getAdvancement(), event.getPlayer());
    }

    public static void execute(LevelAccessor world, Advancement advancement, Entity entity) {
        execute(null, world, advancement, entity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Advancement advancement, Entity entity) {
        if (advancement == null || entity == null)
            return;
        if (world instanceof Level _lvl && _lvl.getServer() != null && _lvl.getServer().getAdvancements().getAdvancement(new ResourceLocation("changed_addon:over_dose")).equals(advancement)) {
            entity.hurt((new DamageSource("OverDose")), 10);
        }
    }
}
