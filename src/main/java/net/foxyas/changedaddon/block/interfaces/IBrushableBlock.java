package net.foxyas.changedaddon.block.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IBrushableBlock {

    /**
     * Chamado a cada "tique" de sucesso da flag (geralmente a cada 10 ticks de uso).
     */
    void onBrushTick(Level level, BlockState state, BlockPos pos, Player player, Direction side, ItemStack brushStack);

    /**
     * Opcional: Chamado quando o jogador termina de escovar o bloco completamente.
     */
    default void onBrushComplete(Level level, BlockState state, BlockPos pos, Player player) {
        // Implementação opcional
    }

    /**
     * Opcional: Define se o bloco pode ser escovado no momento (ex: checar NBT).
     */
    default boolean canBeBrushed(Level level, BlockPos pos, BlockState state) {
        return true;
    }
}