package net.foxyas.changedaddon.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.foxyas.changedaddon.procedures.BiomassPlayerFinishesUsingItemProcedure;
import net.foxyas.changedaddon.init.ChangedAddonTabs;
import org.jetbrains.annotations.NotNull;

public class BiomassItem extends Item {
    public BiomassItem() {
        super(new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON).stacksTo(64).rarity(Rarity.RARE));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.EAT; // Define a animação como comer
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return 15; // Define a duração da animação
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        return ItemUtils.startUsingInstantly(world, player, hand); // Inicia o uso do item instantaneamente
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull LivingEntity entity) {
        super.finishUsingItem(itemstack, world, entity);
        if (entity instanceof Player player && !player.isCreative()) {
            itemstack.shrink(1); // Consome uma unidade do item
        }

        BiomassPlayerFinishesUsingItemProcedure.execute(entity); // Executa a função desejada
        return itemstack.isEmpty() ? ItemStack.EMPTY : itemstack; // Retorna vazio se todo o item foi consumido
    }
}
