package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;

public class GetfullpainitearmorprocedureProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        if ((entity instanceof Player _playerHasItem && _playerHasItem.getInventory().contains(new ItemStack(ChangedAddonItems.PAINITE_ARMOR_HELMET.get())))
                && (entity instanceof Player _playerHasItem && _playerHasItem.getInventory().contains(new ItemStack(ChangedAddonItems.PAINITE_ARMOR_CHESTPLATE.get())))
                && (entity instanceof Player _playerHasItem && _playerHasItem.getInventory().contains(new ItemStack(ChangedAddonItems.PAINITE_ARMOR_LEGGINGS.get())))
                && (entity instanceof Player _playerHasItem && _playerHasItem.getInventory().contains(new ItemStack(ChangedAddonItems.PAINITE_ARMOR_BOOTS.get())))) {
            if (entity instanceof ServerPlayer _player) {
                Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:deleted_mod_element"));
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
