package net.foxyas.changedaddon.effect.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Locale;

public class ThunderParticleOptions implements ParticleOptions {

    private final float speed;
    private final boolean rooted;
    private final Vector3f shake;
    private final Vector3f color;
    private final float size;
    private final int index;

    public ThunderParticleOptions(float speed, boolean rooted, Vector3f shake, Vector3f color, float size, int index) {
        this.speed = speed;
        this.rooted = rooted;
        this.shake = shake;
        this.color = color;
        this.size = size;
        this.index = index;
    }

    public float getSpeed() { return speed; }
    public boolean isRooted() { return rooted; }
    public Vector3f getShake() { return shake; }
    public Vector3f getColor() { return color; }
    public float getSize() { return size; }
    public int getIndex() {
        return index;
    }

    // CODEC for DataFixerUpper / Serialization
    public static final Codec<ThunderParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("speed").forGetter(ThunderParticleOptions::getSpeed),
            Codec.BOOL.fieldOf("rooted").forGetter(ThunderParticleOptions::isRooted),
            ExtraCodecs.VECTOR3F.fieldOf("shake").forGetter(ThunderParticleOptions::getShake),
            ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(ThunderParticleOptions::getColor),
            Codec.FLOAT.fieldOf("size").forGetter(ThunderParticleOptions::getSize),
            Codec.INT.fieldOf("index").forGetter(ThunderParticleOptions::getIndex)
    ).apply(instance, ThunderParticleOptions::new));

    public static Codec<ThunderParticleOptions> codec(ParticleType<ThunderParticleOptions> type) {
        return CODEC;
    }

    // DESERIALIZER for Commands and Network Reading
    @SuppressWarnings("deprecation")
    public static final ParticleOptions.Deserializer<ThunderParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        @Override
        public @NotNull ThunderParticleOptions fromCommand(@NotNull ParticleType<ThunderParticleOptions> type, @NotNull StringReader reader) throws CommandSyntaxException {

            reader.expect(' ');
            float speed = reader.readFloat();

            reader.expect(' ');
            boolean rooted = reader.readBoolean();

            reader.expect(' ');
            float shakeX = reader.readFloat();
            reader.expect(' ');
            float shakeY = reader.readFloat();
            reader.expect(' ');
            float shakeZ = reader.readFloat();

            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();

            reader.expect(' ');
            float size = reader.readFloat();

            reader.expect(' ');
            int index = reader.readInt();

            return new ThunderParticleOptions(
                    speed,
                    rooted,
                    new Vector3f(shakeX, shakeY, shakeZ),
                    new Vector3f(r, g, b),
                    size, index
            );
        }

        @Override
        public @NotNull ThunderParticleOptions fromNetwork(@NotNull ParticleType<ThunderParticleOptions> type, @NotNull FriendlyByteBuf buffer) {
            float speed = buffer.readFloat();
            boolean rooted = buffer.readBoolean();
            Vector3f shake = new Vector3f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
            Vector3f color = new Vector3f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
            float size = buffer.readFloat();

            return new ThunderParticleOptions(speed, rooted, shake, color, size, buffer.readVarInt());
        }
    };

    @Override
    public ParticleType<?> getType() {
        return ChangedAddonParticleTypes.THUNDER_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(speed);
        buffer.writeBoolean(rooted);
        buffer.writeFloat(shake.x()).writeFloat(shake.y()).writeFloat(shake.z());
        buffer.writeFloat(color.x()).writeFloat(color.y()).writeFloat(color.z());
        buffer.writeFloat(size);
        buffer.writeVarInt(index);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %b %.2f %.2f %.2f %.2f %.2f %.2f %.2f %.2s",
                ForgeRegistries.PARTICLE_TYPES.getKey(getType()),
                speed, rooted,
                shake.x(), shake.y(), shake.z(),
                color.x(), color.y(), color.z(),
                size, index
        );
    }
}