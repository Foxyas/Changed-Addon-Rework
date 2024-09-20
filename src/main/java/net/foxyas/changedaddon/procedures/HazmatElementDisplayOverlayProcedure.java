package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class HazmatElementDisplayOverlayProcedure {
	public static boolean execute(Entity entity) {
		Minecraft minecraft = Minecraft.getInstance();
		if (entity == null) {
			return false;
		}
        if (!minecraft.options.getCameraType().isFirstPerson()) return false;
        assert minecraft.player != null;
        return minecraft.player.getItemBySlot(EquipmentSlot.HEAD).is(ChangedAddonModItems.HAZARD_SUIT_HELMET.get())
        || minecraft.player.getItemBySlot(EquipmentSlot.HEAD).is(ChangedAddonModItems.HAZMAT_SUIT_HELMET.get());
	}
}
