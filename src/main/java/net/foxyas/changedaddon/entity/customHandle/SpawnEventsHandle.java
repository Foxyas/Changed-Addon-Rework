package net.foxyas.changedaddon.entity.customHandle;

import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.foxyas.changedaddon.entity.bosses.Experiment10BossEntity;
import net.foxyas.changedaddon.entity.defaults.AbstractLuminarcticLeopard;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SpawnEventsHandle {

    @SubscribeEvent
    public static void whenSpawn(EntityJoinLevelEvent event) {
        Level level = event.getLevel();
        Entity entity = event.getEntity();

        if ((!(entity instanceof AbstractLuminarcticLeopard leo) || !leo.isBoss())
                && !(entity instanceof Experiment10BossEntity) && !(entity instanceof Experiment009BossEntity)) return;

        if (destroyBlock(entity, level)) {
            level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1, 1);
        }
    }

    private static boolean destroyBlock(Entity entity, Level level) {
        BlockPos center = entity.blockPosition();
        float radius = 3; // Raio da explosão
        float radiusY = 3;
        boolean itBreak = false;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset((int) -radius, (int) -radiusY, (int) -radius),
                center.offset((int) radius, (int) radiusY, (int) radius))) {

            // Cálculo da distância esférica
            float dx = (pos.getX() - center.getX()) / radius;
            float dy = (pos.getY() - center.getY()) / radiusY;
            float dz = (pos.getZ() - center.getZ()) / radius;
            float distanceSq = dx * dx + dy * dy + dz * dz;

            if (distanceSq <= 1.0) { // Somente dentro da esfera
                if (level.getBlockState(pos).is(Blocks.OBSIDIAN)
                        || level.getBlockState(pos).is(Blocks.CRYING_OBSIDIAN)
                        || level.getBlockState(pos).getBlock() instanceof SlabBlock
                        || level.getBlockState(pos).getBlock() instanceof FenceBlock
                        || level.getBlockState(pos).getBlock() instanceof FenceGateBlock) {
                    level.destroyBlock(pos, true, null);
                    itBreak = true;
                }
            }
        }
        return itBreak;
    }
}
