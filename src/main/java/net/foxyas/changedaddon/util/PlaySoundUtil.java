package net.foxyas.changedaddon.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * Small wrappers around {@link Level#playSound} that take a position object
 * ({@link Vec3}, {@link BlockPos} or {@link Entity}) instead of raw x/y/z.
 * <p>
 * Every position type has three overloads:
 * <ul>
 *     <li>defaults: volume 1, pitch 1, heard by everyone</li>
 *     <li>explicit volume and pitch, heard by everyone</li>
 *     <li>explicit volume and pitch, with a player to exclude (the vanilla "except" player)</li>
 * </ul>
 * Like {@link Level#playSound}, call these from the server so every nearby client hears the sound.
 */
public class PlaySoundUtil {

    // ------------------------------------------------------------------------------------------
    // Vec3
    // ------------------------------------------------------------------------------------------

    public static void playSound(Level level, Vec3 pos, SoundEvent sound, SoundSource source) {
        playSound(level, pos, sound, source, 1.0f, 1.0f);
    }

    public static void playSound(Level level, Vec3 pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        playSound(level, null, pos, sound, source, volume, pitch);
    }

    /** Core method: every other overload ends up here. */
    public static void playSound(Level level, @Nullable Player except, Vec3 pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        level.playSound(except, pos.x(), pos.y(), pos.z(), sound, source, volume, pitch);
    }

    // ------------------------------------------------------------------------------------------
    // BlockPos (plays at the center of the block)
    // ------------------------------------------------------------------------------------------

    public static void playSound(Level level, BlockPos pos, SoundEvent sound, SoundSource source) {
        playSound(level, pos, sound, source, 1.0f, 1.0f);
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        playSound(level, null, pos, sound, source, volume, pitch);
    }

    public static void playSound(Level level, @Nullable Player except, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        playSound(level, except, Vec3.atCenterOf(pos), sound, source, volume, pitch);
    }

    // ------------------------------------------------------------------------------------------
    // Entity (plays at the entity's feet position)
    // ------------------------------------------------------------------------------------------

    public static void playSound(Level level, Entity entity, SoundEvent sound, SoundSource source) {
        playSound(level, entity, sound, source, 1.0f, 1.0f);
    }

    public static void playSound(Level level, Entity entity, SoundEvent sound, SoundSource source, float volume, float pitch) {
        playSound(level, null, entity, sound, source, volume, pitch);
    }

    public static void playSound(Level level, @Nullable Player except, Entity entity, SoundEvent sound, SoundSource source, float volume, float pitch) {
        playSound(level, except, entity.position(), sound, source, volume, pitch);
    }
}