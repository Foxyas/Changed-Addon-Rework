package net.foxyas.changedaddon.entity.CustomHandle;

import net.foxyas.changedaddon.entity.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.entity.Experiment10BossEntity;
import net.foxyas.changedaddon.entity.KetExperiment009BossEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SpawnEventsHandle {

    @SubscribeEvent
    public static void whenSpawn(EntityJoinWorldEvent event) {
        Level level = event.getWorld();

        if (event.getEntity() instanceof AbstractLuminarcticLeopard entity) {
            if (entity.isBoss()) {
                if (destroyBlock(entity, level)) {
                    if (!level.isClientSide()) {
                        level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1, 1);
                    } else {
                        level.playLocalSound(entity.blockPosition().getX(), entity.blockPosition().getY(), entity.blockPosition().getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1, 1, false);
                    }
                }
            }
        } else if (event.getEntity() instanceof Experiment10BossEntity entity) {
            if (destroyBlock(entity, level)) {
                if (!level.isClientSide()) {
                    level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1, 1);
                } else {
                    level.playLocalSound(entity.blockPosition().getX(), entity.blockPosition().getY(), entity.blockPosition().getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1, 1, false);
                }
            }
        } else if (event.getEntity() instanceof KetExperiment009BossEntity entity) {
            if (destroyBlock(entity, level)) {
                if (!level.isClientSide()) {
                    level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1, 1);
                } else {
                    level.playLocalSound(entity.blockPosition().getX(), entity.blockPosition().getY(), entity.blockPosition().getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1, 1, false);
                }
            }
        }
    }

    private static boolean destroyBlock(LivingEntity entity, Level level) {
        BlockPos center = entity.blockPosition();
        int radius = 3; // Raio da explosão
        int radiusY = 3;
        boolean itBreak = false;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -radiusY, -radius),
                center.offset(radius, radiusY, radius))) {

            // Cálculo da distância esférica
            double dx = (pos.getX() - center.getX()) / (double) radius;
            double dy = (pos.getY() - center.getY()) / (double) radiusY;
            double dz = (pos.getZ() - center.getZ()) / (double) radius;
            double distanceSq = dx * dx + dy * dy + dz * dz;

            if (distanceSq <= 1.0) { // Somente dentro da esfera
                if (level().getBlockState(pos).is(Blocks.OBSIDIAN)
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
