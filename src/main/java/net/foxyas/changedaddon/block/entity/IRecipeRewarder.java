package net.foxyas.changedaddon.block.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public interface IRecipeRewarder {

    void awardUsedRecipes(ServerPlayer player, ItemStack pStack);
}
