package net.foxyas.changedaddon.process;

import net.foxyas.changedaddon.item.HazardSuitItem;
import net.foxyas.changedaddon.item.HazmatSuitItem;
import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ItemSlotEventHandle {
    @SubscribeEvent
    public static void onArmorEquip(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            EquipmentSlot slot = event.getSlot();
            ItemStack from = event.getFrom();
            ItemStack to = event.getTo();
            LatexVariantInstance<?> latexVariantInstance = ProcessTransfur.getPlayerLatexVariant(player);
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                if (to.getItem() instanceof HazmatSuitItem) {
                    if (latexVariantInstance != null){
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
