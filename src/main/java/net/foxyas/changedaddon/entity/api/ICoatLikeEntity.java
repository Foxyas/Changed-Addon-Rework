package net.foxyas.changedaddon.entity.api;

import net.foxyas.changedaddon.entity.ai.LatexInventory;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public interface ICoatLikeEntity extends TamableLatexEntity {

    boolean isUnfusedFromHost();

    void setIsUnfusedFromHost(boolean value);

    default boolean tryFuseBack(Player player, ChangedEntity changedEntity) {
        if (!isTame() || getOwner() != player) return false;

        if (!isUnfusedFromHost() || !player.isShiftKeyDown()) return false;

        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (instance != null || changedEntity.getSelfVariant() == null) return false;

        ProcessTransfur.setPlayerTransfurVariant(player, changedEntity.getSelfVariant(), TransfurContext.hazard(TransfurCause.GRAB_ABSORB), 1f, false, (transfurVariantInstance) -> {
            ChangedEntity variantInstanceChangedEntity = transfurVariantInstance.getChangedEntity();

            if (changedEntity instanceof IAlphaAbleEntity original && variantInstanceChangedEntity instanceof IAlphaAbleEntity transfurred) {
                transfurred.setAlpha(original.isAlpha());
                transfurred.setAlphaScale(original.alphaAdditionalScale());
            }

            /*
            if (changedEntity instanceof TamableLatexEntityFavors original && variantInstanceChangedEntity instanceof TamableLatexEntityFavors transfurred) {

                if (variantInstanceChangedEntity instanceof AbstractTamableLatexEntity abstractTamableLatexEntity) {
                    abstractTamableLatexEntity.tame(host);
                } else if (variantInstanceChangedEntity instanceof AbstractExp2SnepChangedEntity abstractExp2SnepChangedEntity) {
                    abstractExp2SnepChangedEntity.tame(host);
                } else if (variantInstanceChangedEntity instanceof AbstractUnfuseableChangedEntity unfuseableChangedEntity) {
                    unfuseableChangedEntity.tame(host);
                } else if (variantInstanceChangedEntity instanceof TamableLatexEntityWithTameFunction tamableLatexEntityWithTameFunction) {
                    tamableLatexEntityWithTameFunction.tameEntityForPlayer(host);
                }

                LatexInventory originalInventory = original.getInventory();
                if (originalInventory != null) {
                    transfurred.setInventory(originalInventory);
                }
                LatexInventory transfurredInventory = transfurred.getInventory();
//                if (originalInventory != null && transfurredInventory != null) {
//                    transfurredInventory.load(originalInventory.save(new ListTag()));
//                }
            }
            */
        });
        ChangedSounds.broadcastSound(player, changedEntity.getSelfVariant().sound, 1, 1);
        if (changedEntity.level instanceof ServerLevel entityServerLevel) {
            player.teleportTo(entityServerLevel, changedEntity.getX(), changedEntity.getY(), changedEntity.getZ(), RelativeMovement.ALL, changedEntity.getViewYRot(0), changedEntity.getViewXRot(0));
        }

        ItemStack stack, copy, playerStack;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;

            stack = changedEntity.getItemBySlot(slot);
            if (stack.isEmpty()) continue;

            copy = stack.copy();
            stack.setCount(0);
            playerStack = player.getItemBySlot(slot);

            if (playerStack.isEmpty()) {
                player.setItemSlot(slot, copy);
            } else {
                ItemHandlerHelper.giveItemToPlayer(player, copy);
            }
        }

        if (changedEntity instanceof TamableLatexEntityFavors latexEntityFavors) {
            LatexInventory inventory = latexEntityFavors.getInventory();
            if (inventory != null) {
                NonNullList<ItemStack> items = NonNullList.create();
                items.addAll(inventory.items);
                items.addAll(inventory.offhand);
                for (ItemStack item : items) {
                    ItemHandlerHelper.giveItemToPlayer(player, item);
                }
            }
        }

        changedEntity.discard();
        return true;
    }
}
