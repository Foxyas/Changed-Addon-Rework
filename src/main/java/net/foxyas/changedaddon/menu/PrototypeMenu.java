package net.foxyas.changedaddon.menu;

import net.foxyas.changedaddon.entity.advanced.PrototypeEntity;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class PrototypeMenu extends AbstractEntityMenu<PrototypeEntity> {

    public PrototypeMenu(int containerId, Inventory playerInv, PrototypeEntity prototype) {
        super(ChangedAddonMenus.PROTOTYPE_MENU.get(), containerId, playerInv, prototype);
        IItemHandler combinedInv = prototype.getItemHandler();

        //Inventory
        for (int i = 0; i < 3; i++) {
            for (int ii = 0; ii < 3; ii++) {
                addSlot(new SlotItemHandler(combinedInv, 6 + i * 3 + ii, 107 + ii * 18, 18 + i * 18));
            }
        }
    }

    public PrototypeMenu(int containerId, Inventory playerInv, FriendlyByteBuf data) {
        this(containerId, playerInv, (PrototypeEntity) playerInv.player.level.getEntity(data.readVarInt()));
    }
}
