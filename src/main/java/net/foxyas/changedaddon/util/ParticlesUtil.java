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

import java.awt.Color;

/**
 * Particle helpers.
 * <p>
 * Almost everything here is a thin overload that ends up in one of two core methods:
 * <ul>
 *     <li>{@link #sendParticles(Level, ParticleOptions, double, double, double, double, double, double, int, float)}
 *     - one vanilla {@code sendParticles} packet (count > 0: delta = spread; count = 0: delta = motion).</li>
 *     <li>{@link #sendParticlesScattered(Level, ParticleOptions, Vec3, Vec3, Vec3, Vec3, int, float)}
 *     - {@code count} individual particles, each with a gaussian random position and motion offset.</li>
 * </ul>
 * If you need a new variant, call one of those two instead of rebuilding the loop.
 */
public class ParticlesUtil {

    /** The color transition helpers have always spawned 1 block above the given position. */
    private static final double COLOR_TRANSITION_Y_OFFSET = 1.0;

    // ------------------------------------------------------------------------------------------
    // Core methods
    // ------------------------------------------------------------------------------------------

    /**
     * Sends a single particle packet from the server. Does nothing on the client.
     * With {@code count > 0} the delta is a random spread; with {@code count == 0} it is the motion.
     */
    public static void sendParticles(Level level, ParticleOptions particleOptions,
                                     double x, double y, double z,
                                     double XV, double YV, double ZV,
                                     int count, float speed) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particleOptions, x, y, z, count, XV, YV, ZV, speed);
        }
    }

    /**
     * Sends {@code count} particles, each one with its own gaussian random offset.
     *
     * @param position       base position
     * @param positionSpread per-axis gaussian multiplier for the position offset
     * @param motion         base motion
     * @param motionSpread   per-axis gaussian multiplier for the motion offset ({@link Vec3#ZERO} for none)
     */
    public static void sendParticlesScattered(Level level, ParticleOptions particleOptions,
                                              Vec3 position, Vec3 positionSpread,
                                              Vec3 motion, Vec3 motionSpread,
                                              int count, float speed) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        RandomSource random = level.getRandom();
        for (int i = 0; i < count; ++i) {
            serverLevel.sendParticles(particleOptions,
                    position.x() + random.nextGaussian() * positionSpread.x,
                    position.y() + random.nextGaussian() * positionSpread.y,
                    position.z() + random.nextGaussian() * positionSpread.z,
                    0,
                    motion.x() + random.nextGaussian() * motionSpread.x,
                    motion.y() + random.nextGaussian() * motionSpread.y,
                    motion.z() + random.nextGaussian() * motionSpread.z,
                    speed);
        }
    }

    // ------------------------------------------------------------------------------------------
    // sendParticles overloads
    // ------------------------------------------------------------------------------------------

    public static void sendParticles(Level level, ParticleOptions particleOptions, BlockPos pos,
                                     float XV, float YV, float ZV, int count, float speed) {
        sendParticles(level, particleOptions, pos.getX(), pos.getY(), pos.getZ(), XV, YV, ZV, count, speed);
    }

    public static void sendParticles(Level level, ParticleOptions particleOptions, Vec3 pos,
                                     float XV, float YV, float ZV, int count, float speed) {
        sendParticles(level, particleOptions, pos.x(), pos.y(), pos.z(), XV, YV, ZV, count, speed);
    }

    public static void sendParticles(Level level, ParticleOptions particleOptions, Vec3 pos,
                                     Vec3 motionOrDelta, int count, float speed) {
        sendParticles(level, particleOptions, pos.x(), pos.y(), pos.z(),
                motionOrDelta.x(), motionOrDelta.y(), motionOrDelta.z(), count, speed);
    }

    public static void sendParticles(Level level, ParticleOptions particleOptions, Entity entity,
                                     float XV, float YV, float ZV, int count, float speed) {
        sendParticles(level, particleOptions, entity.getX(), entity.getY(), entity.getZ(), XV, YV, ZV, count, speed);
    }

    // ------------------------------------------------------------------------------------------
    // sendParticlesWithMotion overloads (gaussian position offset, fixed motion)
    // ------------------------------------------------------------------------------------------

    public static void sendParticlesWithMotion(Level level, ParticleOptions particleOptions,
                                               Vec3 position, Vec3 offset, Vec3 motion,
                                               int count, float speed) {
        sendParticlesScattered(level, particleOptions, position, offset, motion, Vec3.ZERO, count, speed);
    }

    public static void sendParticlesWithMotion(LivingEntity livingEntity, ParticleOptions particleOptions,
                                               Vec3 position, Vec3 offset, Vec3 motion,
                                               int count, float speed) {
        sendParticlesWithMotion(livingEntity.level(), particleOptions, position, offset, motion, count, speed);
    }

    /** Spawns around the entity's current position. */
    public static void sendParticlesWithMotion(LivingEntity livingEntity, ParticleOptions particleOptions,
                                               Vec3 offset, Vec3 motion, int count, float speed) {
        sendParticlesWithMotion(livingEntity.level(), particleOptions, livingEntity.position(), offset, motion, count, speed);
    }

    /** Spawns around the entity's interpolated position. */
    public static void sendParticlesWithMotion(LivingEntity livingEntity, float partialTicks, ParticleOptions particleOptions,
                                               Vec3 offset, Vec3 motion, int count, float speed) {
        sendParticlesWithMotion(livingEntity.level(), particleOptions, livingEntity.getPosition(partialTicks), offset, motion, count, speed);
    }

    // ------------------------------------------------------------------------------------------
    // sendParticlesWithMotionAndOffset overloads (gaussian position AND motion offset)
    // ------------------------------------------------------------------------------------------

    public static void sendParticlesWithMotionAndOffset(Level level, ParticleOptions particleOptions,
                                                        Vec3 position, Vec3 positionOffset,
                                                        Vec3 motion, Vec3 motionOffset,
                                                        int count, float speed) {
        sendParticlesScattered(level, particleOptions, position, positionOffset, motion, motionOffset, count, speed);
    }

    public static void sendParticlesWithMotionAndOffset(LivingEntity livingEntity, ParticleOptions particleOptions,
                                                        Vec3 position, Vec3 positionOffset,
                                                        Vec3 motion, Vec3 motionOffset,
                                                        int count, float speed) {
        sendParticlesScattered(livingEntity.level(), particleOptions, position, positionOffset, motion, motionOffset, count, speed);
    }

    /** Spawns around the entity's current position. */
    public static void sendParticlesWithMotionAndOffset(LivingEntity livingEntity, ParticleOptions particleOptions,
                                                        Vec3 positionOffset, Vec3 motion, Vec3 motionOffset,
                                                        int count, float speed) {
        sendParticlesScattered(livingEntity.level(), particleOptions, livingEntity.position(), positionOffset, motion, motionOffset, count, speed);
    }

    // ------------------------------------------------------------------------------------------
    // Color transition (dust)
    // ------------------------------------------------------------------------------------------

    public static void sendColorTransitionParticles(Level level, double x, double y, double z,
                                                    float redStart, float greenStart, float blueStart,
                                                    float redEnd, float greenEnd, float blueEnd,
                                                    float size, float XV, float YV, float ZV, int count, float speed) {
        DustColorTransitionOptions options = new DustColorTransitionOptions(
                new Vector3f(redStart, greenStart, blueStart),
                new Vector3f(redEnd, greenEnd, blueEnd),
                size);

        sendParticles(level, options, x, y + COLOR_TRANSITION_Y_OFFSET, z, XV, YV, ZV, count, speed);
    }

    public static void sendColorTransitionParticles(Level level, Player player,
                                                    float redStart, float greenStart, float blueStart,
                                                    float redEnd, float greenEnd, float blueEnd,
                                                    float size, float XV, float YV, float ZV, int count, float speed) {
        sendColorTransitionParticles(level, player.getX(), player.getY(), player.getZ(),
                redStart, greenStart, blueStart, redEnd, greenEnd, blueEnd,
                size, XV, YV, ZV, count, speed);
    }

    public static void sendColorTransitionParticles(Level level, Player player,
                                                    Color startColor, Color endColor,
                                                    float size, float XV, float YV, float ZV, int count, float speed) {
        sendColorTransitionParticles(level, player,
                channel(startColor.getRed()), channel(startColor.getGreen()), channel(startColor.getBlue()),
                channel(endColor.getRed()), channel(endColor.getGreen()), channel(endColor.getBlue()),
                size, XV, YV, ZV, count, speed);
    }

    /** Converts an AWT 0-255 channel to the 0.0-1.0 range particles expect. */
    private static float channel(int value) {
        return value / 255f;
    }

    // ------------------------------------------------------------------------------------------
    // Latex drip
    // ------------------------------------------------------------------------------------------

    public static void sendDripParticles(Level level, Entity entity, float middle,
                                         float red, float green, float blue,
                                         float XV, float YV, float ZV, int count, float speed) {
        sendDripParticles(level, entity, middle, new Color3(red, green, blue), XV, YV, ZV, count, speed);
    }

    public static void sendDripParticles(Level level, Entity entity, float middle, String color,
                                         float XV, float YV, float ZV, int count, float speed) {
        sendDripParticles(level, entity, middle, Color3.getColor(color), XV, YV, ZV, count, speed);
    }

    private static void sendDripParticles(Level level, Entity entity, float middle, Color3 color,
                                          float XV, float YV, float ZV, int count, float speed) {
        ColoredParticleOption options = ChangedParticles.drippingLatex(color);
        sendParticles(level, options, entity.getX(), entity.getY() + middle, entity.getZ(), XV, YV, ZV, count, speed);
    }

    // ------------------------------------------------------------------------------------------
    // Client side
    // ------------------------------------------------------------------------------------------

    public static void sendParticlesInClient(Level level, ParticleOptions particleOptions,
                                             double x, double y, double z,
                                             double XV, double YV, double ZV, int count) {
        if (level instanceof ClientLevel clientLevel) {
            for (int i = 0; i < count; i++) {
                clientLevel.addParticle(particleOptions, x, y, z, XV, YV, ZV);
            }
        }
    }

    // ------------------------------------------------------------------------------------------
    // Block effects (vanilla-style)
    // ------------------------------------------------------------------------------------------

    public static void addParticlesAroundBlockBonemealStyle(LevelAccessor level, BlockPos pos, ParticleOptions particleOptions, int amount) {
        if (amount == 0) {
            amount = 15;
        }

        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return;

        double spread = 0.5;
        double height;
        if (state.is(Blocks.WATER)) {
            amount *= 3;
            height = 1.0;
            spread = 3.0;
        } else if (state.isSolidRender(level, pos)) {
            pos = pos.above();
            amount *= 3;
            spread = 3.0;
            height = 1.0;
        } else {
            height = state.getShape(level, pos).max(Direction.Axis.Y);
        }

        level.addParticle(particleOptions, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
        RandomSource random = level.getRandom();

        for (int i = 0; i < amount; ++i) {
            double dx = random.nextGaussian() * 0.02;
            double dy = random.nextGaussian() * 0.02;
            double dz = random.nextGaussian() * 0.02;
            double baseOffset = 0.5 - spread;
            double x = pos.getX() + baseOffset + random.nextDouble() * spread * 2.0;
            double y = pos.getY() + random.nextDouble() * height;
            double z = pos.getZ() + baseOffset + random.nextDouble() * spread * 2.0;

            if (!level.getBlockState(new BlockPos((int) x, (int) y, (int) z).below()).isAir()) {
                level.addParticle(particleOptions, x, y, z, dx, dy, dz);
            }
        }
    }

    public static void spawnParticlesAroundBlockRedStoneOreStyle(Level level, BlockPos pos, ParticleOptions particleOptions) {
        double faceOffset = 0.5625;
        RandomSource random = level.getRandom();

        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.relative(direction);
            if (level.getBlockState(neighbor).isSolidRender(level, neighbor)) continue;

            Direction.Axis axis = direction.getAxis();
            double ox = axis == Direction.Axis.X ? 0.5 + faceOffset * direction.getStepX() : random.nextFloat();
            double oy = axis == Direction.Axis.Y ? 0.5 + faceOffset * direction.getStepY() : random.nextFloat();
            double oz = axis == Direction.Axis.Z ? 0.5 + faceOffset * direction.getStepZ() : random.nextFloat();

            level.addParticle(particleOptions, pos.getX() + ox, pos.getY() + oy, pos.getZ() + oz, 0.0, 0.0, 0.0);
        }
    }
}