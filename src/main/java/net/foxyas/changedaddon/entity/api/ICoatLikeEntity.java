package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
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

        ProcessTransfur.setPlayerTransfurVariant(player, changedEntity.getSelfVariant(), TransfurContext.hazard(TransfurCause.GRAB_ABSORB), 1f);
        ChangedSounds.broadcastSound(player, changedEntity.getSelfVariant().sound, 1, 1);
        if (changedEntity.level instanceof ServerLevel entityServerLevel && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.teleportTo(entityServerLevel, changedEntity.getX(), changedEntity.getY(), changedEntity.getZ(), changedEntity.getViewYRot(0), changedEntity.getViewXRot(0));
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

        if (changedEntity instanceof IAlphaAbleEntity original && ProcessTransfur.getPlayerTransfurVariant(player).getChangedEntity() instanceof IAlphaAbleEntity alphaAble) {
            alphaAble.setAlpha(original.isAlpha());
        }

        changedEntity.discard();
        return true;
    }
}
