package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Iterator;

@Mod.EventBusSubscriber
public class GiveBlockCraftAdvancementProcedure {
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        execute(event, event.getPlayer(), event.getCrafting());
    }

    public static void execute(Entity entity, ItemStack itemstack) {
        execute(null, entity, itemstack);
    }

    private static void execute(@Nullable Event event, Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        if (itemstack.getItem() == ChangedAddonBlocks.UNIFUSER.get().asItem()) {
            if (entity instanceof ServerPlayer _player) {
                Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:unifuser_advancement"));
                AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
                if (!_ap.isDone()) {
                    Iterator _iterator = _ap.getRemainingCriteria().iterator();
                    while (_iterator.hasNext())
                        _player.getAdvancements().award(_adv, (String) _iterator.next());
                }
            }
        }
        if (itemstack.getItem() == ChangedAddonBlocks.CATALYZER.get().asItem()) {
            if (entity instanceof ServerPlayer _player) {
                Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:catalyzer_advancement"));
                AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
                if (!_ap.isDone()) {
                    Iterator _iterator = _ap.getRemainingCriteria().iterator();
                    while (_iterator.hasNext())
                        _player.getAdvancements().award(_adv, (String) _iterator.next());
                }
            }
        }
    }
}
