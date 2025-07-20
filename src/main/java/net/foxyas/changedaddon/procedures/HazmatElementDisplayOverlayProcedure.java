package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;

public class HazmatElementDisplayOverlayProcedure {
    public static boolean execute(Entity entity) {
        Minecraft minecraft = Minecraft.getInstance();
        if (entity == null) {
            return false;
        }
        if (!minecraft.options.getCameraType().isFirstPerson()) return false;
        assert minecraft.player != null;
        return minecraft.player.getItemBySlot(EquipmentSlot.HEAD).is(ChangedAddonItems.HAZARD_SUIT_HELMET.get())
                || minecraft.player.getItemBySlot(EquipmentSlot.HEAD).is(ChangedAddonItems.HAZMAT_SUIT_HELMET.get());
    }
}
