package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.entity.simple.PuroKindFemaleEntity;
import net.foxyas.changedaddon.entity.simple.PuroKindMaleEntity;
import net.foxyas.changedaddon.entity.simple.WolfyEntity;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.registries.RegistryObject;

//TODO: remove this class
public class TamePuroKindProcedure {

    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != event.getEntity().getUsedItemHand()) return;

        Entity entity = event.getTarget();
        Player player = event.getEntity();

        if (!(entity instanceof AbstractDarkLatexWolf wolf) || player == null) return;

        ItemStack stack;
        if (entity instanceof PuroKindMaleEntity || entity instanceof PuroKindFemaleEntity) {
            if (wolf.isTame()) return;

            stack = matchingHandStack(player, ChangedItems.ORANGE);
            if (stack == null) return;

            if (!player.isCreative()) stack.shrink(1);
            wolf.tame(player);
            return;
        }

        if (entity instanceof WolfyEntity) {
            if (wolf.isTame()) return;

            stack = matchingHandStack(player, ChangedAddonItems.FOXTA);
            if (stack == null) return;

            if (!player.isCreative()) stack.shrink(1);
            wolf.tame(player);
        }
    }

    private static ItemStack matchingHandStack(Player player, RegistryObject<? extends Item> item) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(item.get())) return stack;
        stack = player.getOffhandItem();
        return stack.is(item.get()) ? stack : null;
    }
}
