package net.foxyas.changedaddon.util;

import net.ltxprogrammer.changed.effect.particle.ColoredParticleOption;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.awt.*;

public class ParticlesUtil {

    public static void addParticlesAroundBlockBonemealStyle(LevelAccessor pLevel, BlockPos pPos, ParticleOptions particleOptions, int pData) {
        if (pData == 0) {
            pData = 15;
        }

        BlockState blockstate = pLevel.getBlockState(pPos);
        if (!blockstate.isAir()) {
            double d0 = 0.5F;
            double d1;
            if (blockstate.is(Blocks.WATER)) {
                pData *= 3;
                d1 = 1.0F;
                d0 = 3.0F;
            } else if (blockstate.isSolidRender(pLevel, pPos)) {
                pPos = pPos.above();
                pData *= 3;
                d0 = 3.0F;
                d1 = 1.0F;
            } else {
                d1 = blockstate.getShape(pLevel, pPos).max(Direction.Axis.Y);
            }

            pLevel.addParticle(particleOptions, (double) pPos.getX() + (double) 0.5F, (double) pPos.getY() + (double) 0.5F, (double) pPos.getZ() + (double) 0.5F, 0.0F, 0.0F, 0.0F);
            RandomSource random = pLevel.getRandom();

            for (int i = 0; i < pData; ++i) {
                double d2 = random.nextGaussian() * 0.02;
                double d3 = random.nextGaussian() * 0.02;
                double d4 = random.nextGaussian() * 0.02;
                double d5 = (double) 0.5F - d0;
                double d6 = (double) pPos.getX() + d5 + random.nextDouble() * d0 * (double) 2.0F;
                double d7 = (double) pPos.getY() + random.nextDouble() * d1;
                double d8 = (double) pPos.getZ() + d5 + random.nextDouble() * d0 * (double) 2.0F;
                if (!pLevel.getBlockState((new BlockPos((int) d6, (int) d7, (int) d8)).below()).isAir()) {
                    pLevel.addParticle(particleOptions, d6, d7, d8, d2, d3, d4);
                }
            }
        }

    }

    public static void spawnParticlesAroundBlockRedStoneOreStyle(Level pLevel, BlockPos pPos, ParticleOptions particleOptions) {
        double d0 = 0.5625F;
        RandomSource random = pLevel.getRandom();

        for (Direction direction : Direction.values()) {
            BlockPos blockpos = pPos.relative(direction);
            if (!pLevel.getBlockState(blockpos).isSolidRender(pLevel, blockpos)) {
                Direction.Axis direction$axis = direction.getAxis();
                double d1 = direction$axis == Direction.Axis.X ? 0.5d + d0 * (double) direction.getStepX() : (double) random.nextFloat();
                double d2 = direction$axis == Direction.Axis.Y ? 0.5d + d0 * (double) direction.getStepY() : (double) random.nextFloat();
                double d3 = direction$axis == Direction.Axis.Z ? 0.5d + d0 * (double) direction.getStepZ() : (double) random.nextFloat();
                pLevel.addParticle(particleOptions, (double) pPos.getX() + d1, (double) pPos.getY() + d2, (double) pPos.getZ() + d3, 0.0F, 0.0F, 0.0F);
            }
        }

    }

    public static void sendColorTransitionParticles(Level level, double x, double y, double z,
                                                    float redStart, float greenStart, float blueStart,
                                                    float redEnd, float greenEnd, float blueEnd,
                                                    float size, float XV, float YV, float ZV, int count, float speed) {

        // Criar a opção de partícula para transição de cor usando Vector3f
        Vector3f startColor = new Vector3f(redStart, greenStart, blueStart);
        Vector3f endColor = new Vector3f(redEnd, greenEnd, blueEnd);
        DustColorTransitionOptions particleOptions = new DustColorTransitionOptions(startColor, endColor, size);

        // Enviar as partículas
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particleOptions,
                    x, y + 1, z, count, XV, YV, ZV, speed);
        }
    }


    public static void sendColorTransitionParticles(Level level, Player player,
                                                    float redStart, float greenStart, float blueStart,
                                                    float redEnd, float greenEnd, float blueEnd,
                                                    float size, float XV, float YV, float ZV, int count, float speed) {
        sendColorTransitionParticles(level, player.getX(), player.getY(), player.getZ(), redStart, greenStart, blueStart, redEnd, greenEnd, blueEnd, size, XV, YV, ZV, count, speed);
    }

    public static void sendColorTransitionParticles(Level level, Player player,
                                                    Color startColor, Color endColor,
                                                    float size, float XV, float YV, float ZV, int count, float speed) {
        sendColorTransitionParticles(level, player.getX(), player.getY(), player.getZ(), startColor.getRed() / 255f, startColor.getGreen() / 255f, startColor.getBlue() / 255f, endColor.getRed() / 255f, endColor.getGreen() / 255f, endColor.getBlue() / 255f, size, XV, YV, ZV, count, speed);
    }

    public static void sendDripParticles(Level level, Entity entity, float middle,
                                         float red, float green, float blue, float XV, float YV, float ZV, int count, float speed) {

        // Criar a opção de partícula para transição de cor usando Vector3f
        ColoredParticleOption particleOptions = ChangedParticles.drippingLatex(new Color3(red, green, blue));

        // Enviar as partículas
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particleOptions,
                    entity.getX(), entity.getY() + middle, entity.getZ(), count, XV, YV, ZV, speed);
        }
    }


    public static void sendDripParticles(Level level, Entity entity, float middle, String color, float XV, float YV, float ZV, int count, float speed) {

        // Criar a opção de partícula para transição de cor usando Vector3f
        ColoredParticleOption particleOptions = ChangedParticles.drippingLatex(Color3.getColor(color));

        // Enviar as partículas
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particleOptions,
                    entity.getX(), entity.getY() + middle, entity.getZ(), count, XV, YV, ZV, speed);
        }
    }

    public static void sendParticles(Level level, ParticleOptions particleOptions, BlockPos entity, float XV, float YV, float ZV, int count, float speed) {
        // Enviar as partículas
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particleOptions,
                    entity.getX(), entity.getY(), entity.getZ(), count, XV, YV, ZV, speed);
        }
    }

    public static void sendParticles(Level level, ParticleOptions particleOptions, Vec3 entity, float XV, float YV, float ZV, int count, float speed) {
        // Enviar as partículas
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particleOptions,
                    entity.x(), entity.y(), entity.z(), count, XV, YV, ZV, speed);
        }
    }

    public static void sendParticles(Level level, ParticleOptions particleOptions, Vec3 entity, Vec3 motionOrDelta, int count, float speed) {
        float XV = (float) motionOrDelta.x(), YV = (float) motionOrDelta.y(), ZV = (float) motionOrDelta.z();
        // Enviar as partículas
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particleOptions,
                    entity.x(),
                    entity.y(),
                    entity.z(),
                    count,
                    XV,
                    YV,
                    ZV,
                    speed);
        }
    }

    public static void sendParticles(Level level, ParticleOptions particleOptions, Entity entity, float XV, float YV, float ZV, int count, float speed) {
        // Enviar as partículas
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particleOptions,
                    entity.getX(), entity.getY(), entity.getZ(), count, XV, YV, ZV, speed);
        }
    }

    public static void sendParticles(Level level, ParticleOptions particleOptions, double x, double y, double z, double XV, double YV, double ZV, int count, float speed) {
        // Enviar as partículas
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particleOptions,
                    x, y, z, count, XV, YV, ZV, speed);
        }
    }

    public static void sendParticlesWithMotion(Level level, ParticleOptions particleOptions, Vec3 position, Vec3 offset, Vec3 motion, int count, float speed) {
        // Enviar as partículas
        double XV = motion.x(), YV = motion.y(), ZV = motion.z();
        RandomSource random = level.getRandom();

        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < count; ++i) {
                double x = random.nextGaussian() * offset.x;
                double y = random.nextGaussian() * offset.y;
                double z = random.nextGaussian() * offset.z;

                serverLevel.sendParticles(particleOptions,
                        position.x() + x,
                        position.y() + y,
                        position.z() + z,
                        0,
                        XV,
                        YV,
                        ZV,
                        speed);
            }
        }
    }

    public static void sendParticlesWithMotion(LivingEntity livingEntity, ParticleOptions particleOptions, Vec3 position, Vec3 offset, Vec3 motion, int count, float speed) {
        // Enviar as partículas
        double XV = motion.x(), YV = motion.y(), ZV = motion.z();
        RandomSource random = livingEntity.getRandom();

        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < count; ++i) {
                double x = random.nextGaussian() * offset.x;
                double y = random.nextGaussian() * offset.y;
                double z = random.nextGaussian() * offset.z;

                serverLevel.sendParticles(particleOptions,
                        position.x() + x,
                        position.y() + y,
                        position.z() + z,
                        0,
                        XV,
                        YV,
                        ZV,
                        speed);
            }
        }
    }

    public static void sendParticlesWithMotionAndOffset(LivingEntity livingEntity, ParticleOptions particleOptions, Vec3 position, Vec3 positionOffset, Vec3 motion, Vec3 motionOffset, int count, float speed) {
        double XV = motion.x(), YV = motion.y(), ZV = motion.z();
        RandomSource random = livingEntity.getRandom();

        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < count; ++i) {
                double x = random.nextGaussian() * positionOffset.x;
                double y = random.nextGaussian() * positionOffset.y;
                double z = random.nextGaussian() * positionOffset.z;

                double xvOffset = random.nextGaussian() * motionOffset.x;
                double yvOffset = random.nextGaussian() * motionOffset.y;
                double zvOffset = random.nextGaussian() * motionOffset.z;

                serverLevel.sendParticles(particleOptions,
                        position.x() + x,
                        position.y() + y,
                        position.z() + z,
                        0,
                        XV + xvOffset,
                        YV + yvOffset,
                        ZV + zvOffset,
                        speed);
            }
        }
    }

    public static void sendParticlesWithMotionAndOffset(Level level, ParticleOptions particleOptions, Vec3 position, Vec3 positionOffset, Vec3 motion, Vec3 motionOffset, int count, float speed) {
        double XV = motion.x(), YV = motion.y(), ZV = motion.z();
        RandomSource random = level.getRandom();

        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < count; ++i) {
                double x = random.nextGaussian() * positionOffset.x;
                double y = random.nextGaussian() * positionOffset.y;
                double z = random.nextGaussian() * positionOffset.z;

                double xvOffset = random.nextGaussian() * motionOffset.x;
                double yvOffset = random.nextGaussian() * motionOffset.y;
                double zvOffset = random.nextGaussian() * motionOffset.z;

                serverLevel.sendParticles(particleOptions,
                        position.x() + x,
                        position.y() + y,
                        position.z() + z,
                        0,
                        XV + xvOffset,
                        YV + yvOffset,
                        ZV + zvOffset,
                        speed);
            }
        }
    }

    public static void sendParticlesWithMotionAndOffset(LivingEntity livingEntity, ParticleOptions particleOptions, Vec3 positionOffset, Vec3 motion, Vec3 motionOffset, int count, float speed) {
        double XV = motion.x(), YV = motion.y(), ZV = motion.z();
        Vec3 position = livingEntity.position();
        RandomSource random = livingEntity.getRandom();

        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < count; ++i) {
                double x = random.nextGaussian() * positionOffset.x;
                double y = random.nextGaussian() * positionOffset.y;
                double z = random.nextGaussian() * positionOffset.z;

                double xvOffset = random.nextGaussian() * motionOffset.x;
                double yvOffset = random.nextGaussian() * motionOffset.y;
                double zvOffset = random.nextGaussian() * motionOffset.z;

                serverLevel.sendParticles(particleOptions,
                        position.x() + x,
                        position.y() + y,
                        position.z() + z,
                        0,
                        XV + xvOffset,
                        YV + yvOffset,
                        ZV + zvOffset,
                        speed);
            }
        }
    }

    public static void sendParticlesWithMotion(LivingEntity livingEntity, ParticleOptions particleOptions, Vec3 offset, Vec3 motion, int count, float speed) {
        // Enviar as partículas
        double XV = motion.x(), YV = motion.y(), ZV = motion.z();
        RandomSource random = livingEntity.getRandom();
        Vec3 position = livingEntity.position();

        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < count; ++i) {
                double x = random.nextGaussian() * offset.x;
                double y = random.nextGaussian() * offset.y;
                double z = random.nextGaussian() * offset.z;

                serverLevel.sendParticles(particleOptions,
                        position.x() + x,
                        position.y() + y,
                        position.z() + z,
                        0,
                        XV,
                        YV,
                        ZV,
                        speed);
            }
        }
    }

    public static void sendParticlesWithMotion(LivingEntity livingEntity, float partialTicks, ParticleOptions particleOptions, Vec3 offset, Vec3 motion, int count, float speed) {
        // Enviar as partículas
        double XV = motion.x(), YV = motion.y(), ZV = motion.z();
        RandomSource random = livingEntity.getRandom();
        Vec3 position = livingEntity.getPosition(partialTicks);

        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < count; ++i) {
                double x = random.nextGaussian() * offset.x;
                double y = random.nextGaussian() * offset.y;
                double z = random.nextGaussian() * offset.z;

                serverLevel.sendParticles(particleOptions,
                        position.x() + x,
                        position.y() + y,
                        position.z() + z,
                        0,
                        XV,
                        YV,
                        ZV,
                        speed);
            }
        }
    }

    public static void sendParticlesinClient(Level level, ParticleOptions particleOptions, double x, double y, double z, double XV, double YV, double ZV, int count) {
        // Enviar as partículas
        if (level instanceof ClientLevel clientLevel) {
            for (int i = 0; i < count; i++) {
                clientLevel.addParticle(particleOptions,
                        x, y, z, XV, YV, ZV);
            }
        }
    }
}