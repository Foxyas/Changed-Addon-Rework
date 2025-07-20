package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class HazmatSuitTickUsedProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        ItemStack item;
        item = itemstack;
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);

            if (instance != null) {
                if (!ProcessTransfur.isPlayerLatex(player)) {
                    entity.hurt(((new EntityDamageSource("latex_solvent" + ".player", entity)).bypassArmor()), 2);
                }
                if (entity instanceof Player _player) {
                    _player.getInventory().clearOrCountMatchingItems(p -> item.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                }
                if (entity instanceof Player _player) {
                    item.setCount(1);
                    ItemHandlerHelper.giveItemToPlayer(_player, item);
                }
            }
        }

    }
}
