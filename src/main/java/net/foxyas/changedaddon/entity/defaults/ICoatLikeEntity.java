package net.foxyas.changedaddon.entity.defaults;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public interface ICoatLikeEntity extends TamableLatexEntity {
    boolean isUnfusedFromHost();

    void setIsUnfusedFromHost(boolean value);

    default void tryFuseBack(Player player, ChangedEntity changedEntity) {
        if (isTame() && getOwner() == player) {
            if (isUnfusedFromHost() && player.isShiftKeyDown()) {
                TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
                if (transfurVariantInstance == null && changedEntity.getSelfVariant() != null) {
                    ProcessTransfur.setPlayerTransfurVariant(player, changedEntity.getSelfVariant(), TransfurContext.hazard(TransfurCause.GRAB_REPLICATE), 1f);
                    ChangedSounds.broadcastSound(player, changedEntity.getSelfVariant().sound, 1, 1);
                    Arrays.stream(EquipmentSlot.values())
                            .filter(slot -> slot.getType() == EquipmentSlot.Type.ARMOR)
                            .forEach(slot -> {
                                ItemStack entityStack = changedEntity.getItemBySlot(slot);

                                if (!entityStack.isEmpty()) {
                                    ItemStack copy = entityStack.copy();
                                    entityStack.setCount(0);

                                    ItemStack playerStack = player.getItemBySlot(slot);
                                    if (playerStack.isEmpty()) {
                                        // Equip directly to player
                                        player.setItemSlot(slot, copy);
                                    } else {
                                        // Drop near player if their slot is occupied
                                        if (player.addItem(copy)) {
                                            player.drop(copy, false);
                                        }
                                    }
                                }
                            });
                    changedEntity.discard();
                }
            }
        }
    }

}
