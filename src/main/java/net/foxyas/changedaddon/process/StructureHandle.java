package net.foxyas.changedaddon.process;

import net.minecraft.core.BlockPos;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public class StructureHandle {

    /**
     * Obtém as coordenadas do chunk a partir de uma posição de bloco.
     */
    private static int[] getChunkCoordinates(BlockPos pos) {
        return new int[]{pos.getX() >> 4, pos.getZ() >> 4}; // Converte coordenadas de bloco para chunk
    }

    /**
     * Verifica se uma estrutura pode gerar dentro de um determinado raio de chunks.
     *
     * @param level     o ServerLevel
     * @param pos       a posição a ser verificada
     * @param structure ResourceKey da estrutura
     * @param chunkRange o raio de chunks a ser verificado
     * @return true se a estrutura pode gerar na área, false caso contrário.
     */
    public static boolean isStructureNearby(ServerLevel level, BlockPos pos, ResourceKey<StructureSet> structure, int chunkRange) {
        int[] chunkCoords = getChunkCoordinates(pos);
        return level.getChunkSource().getGenerator().hasFeatureChunkInRange(structure, level.getSeed(), chunkCoords[0], chunkCoords[1], chunkRange);
    }

    /**
     * Verifica se uma estrutura pode gerar dentro de um determinado raio de chunks.
     *
     * @param level       o ServerLevel
     * @param pos         a posição a ser verificada
     * @param structureId o ID da estrutura desejada (ex.: "changed_addon:dazed_meteor")
     * @param chunkRange  o raio de chunks a ser verificado
     * @return true se a estrutura pode gerar na área, false caso contrário.
     */
    public static boolean isStructureNearby(ServerLevel level, BlockPos pos, String structureId, int chunkRange) {
        ResourceKey<StructureSet> structureKey = ResourceKey.create(BuiltinRegistries.STRUCTURE_SETS.key(), new ResourceLocation(structureId));
        return isStructureNearby(level, pos, structureKey, chunkRange);
    }


    /**
     * Verifica se uma estrutura pode gerar dentro de um determinado raio de chunks.
     *
     * @param clevel       o ServerLevel
     * @param pos         a posição a ser verificada
     * @param structureId o ID da estrutura desejada (ex.: "changed_addon:dazed_meteor")
     * @param chunkRange  o raio de chunks a ser verificado
     * @return true se a estrutura pode gerar na área, false caso contrário.
     */
    public static boolean isStructureNearby(Level clevel, BlockPos pos, String structureId, int chunkRange) {
        ResourceKey<StructureSet> structureKey = ResourceKey.create(BuiltinRegistries.STRUCTURE_SETS.key(), new ResourceLocation(structureId));
        return clevel instanceof ServerLevel level && isStructureNearby(level, pos, structureKey, chunkRange);
    }
}
