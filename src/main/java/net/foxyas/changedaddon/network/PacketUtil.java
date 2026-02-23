package net.foxyas.changedaddon.network;

import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.PlayLevelSoundEvent;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class PacketUtil {

    public static <R> R readNullable(FriendlyByteBuf buf, Function<FriendlyByteBuf, R> reader) {
        if (buf.readBoolean()) return reader.apply(buf);
        return null;
    }

    public static <T> void writeNullable(FriendlyByteBuf buf, BiConsumer<FriendlyByteBuf, T> writer, @Nullable T obj) {
        buf.writeBoolean(obj != null);
        if (obj != null) writer.accept(buf, obj);
    }

    public static void playSound(ServerLevel level, Predicate<ServerPlayer> send, double x, double y, double z, SoundEvent sound, SoundSource soundSource, float volume, float pitch) {
        Holder<SoundEvent> direct = Holder.direct(sound);
        PlayLevelSoundEvent.AtPosition event = ForgeEventFactory.onPlaySoundAtPosition(level, x, y, z, direct, soundSource, volume, pitch);
        if (event.isCanceled() || event.getSound() == null) return;

        sound = event.getSound().get();
        soundSource = event.getSource();
        volume = event.getNewVolume();

        long pSeed = level.getRandom().nextLong();
        broadcast(level, send.and(distance(x, y, z, volume > 1 ? volume * 16 : 16)), level.dimension(), new ClientboundSoundPacket(direct, soundSource, x, y, z, volume, pitch, pSeed));
    }

    public static void playSound(ServerLevel level, Predicate<ServerPlayer> send, Vec3 position, SoundEvent sound, SoundSource soundSource, float volume, float pitch) {
        Holder<SoundEvent> direct = Holder.direct(sound);
        PlayLevelSoundEvent.AtPosition event = ForgeEventFactory.onPlaySoundAtPosition(level, position.x, position.y, position.z, direct, soundSource, volume, pitch);
        if (event.isCanceled() || event.getSound() == null) return;

        sound = event.getSound().get();
        soundSource = event.getSource();
        volume = event.getNewVolume();
        double x = position.x, y = position.y, z = position.z;

        long pSeed = level.getRandom().nextLong();
        broadcast(level, send.and(distance(x, y, z, volume > 1 ? volume * 16 : 16)), level.dimension(), new ClientboundSoundPacket(direct, soundSource, x, y, z, volume, pitch, pSeed));
    }

    public static Predicate<ServerPlayer> distance(double x, double y, double z, double distance) {
        return player -> {
            double dX = x - player.getX();
            double dY = y - player.getY();
            double dZ = z - player.getZ();
            return dX * dX + dY * dY + dZ * dZ < distance * distance;
        };
    }

    public static void broadcast(ServerLevel level, Predicate<ServerPlayer> send, ResourceKey<Level> pDimension, Packet<?> packet) {
        List<ServerPlayer> players = level.getServer().getPlayerList().getPlayers();

        for (ServerPlayer serverplayer : players) {
            if (serverplayer.level.dimension() != pDimension) continue;

            if (send.test(serverplayer)) serverplayer.connection.send(packet);
        }
    }
}
