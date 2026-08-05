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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Locale;

public class ThunderParticleOptions implements ParticleOptions {
    private final Vec3 startPos;
    private final Vec3 endPos;
    private final float speed;
    private final boolean rooted;
    private final Vector3f shake;
    private final Vector3f color;
    private final float size;

    public ThunderParticleOptions(Vec3 startPos, Vec3 endPos, float speed, boolean rooted, Vector3f shake, Vector3f color, float size) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.speed = speed;
        this.rooted = rooted;
        this.shake = shake;
        this.color = color;
        this.size = size;
    }

    public Vec3 getStartPos() { return startPos; }
    public Vec3 getEndPos() { return endPos; }
    public float getSpeed() { return speed; }
    public boolean isRooted() { return rooted; }
    public Vector3f getShake() { return shake; }
    public Vector3f getColor() { return color; }
    public float getSize() { return size; }

    // CODEC for DataFixerUpper / Serialization
    public static final Codec<ThunderParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf("start_pos").forGetter(ThunderParticleOptions::getStartPos),
            Vec3.CODEC.fieldOf("end_pos").forGetter(ThunderParticleOptions::getEndPos),
            Codec.FLOAT.fieldOf("speed").forGetter(ThunderParticleOptions::getSpeed),
            Codec.BOOL.fieldOf("rooted").forGetter(ThunderParticleOptions::isRooted),
            ExtraCodecs.VECTOR3F.fieldOf("shake").forGetter(ThunderParticleOptions::getShake),
            ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(ThunderParticleOptions::getColor),
            Codec.FLOAT.fieldOf("size").forGetter(ThunderParticleOptions::getSize)
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
            double sx = reader.readDouble();
            reader.expect(' ');
            double sy = reader.readDouble();
            reader.expect(' ');
            double sz = reader.readDouble();

            reader.expect(' ');
            double ex = reader.readDouble();
            reader.expect(' ');
            double ey = reader.readDouble();
            reader.expect(' ');
            double ez = reader.readDouble();

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

            return new ThunderParticleOptions(
                    new Vec3(sx, sy, sz),
                    new Vec3(ex, ey, ez),
                    speed,
                    rooted,
                    new Vector3f(shakeX, shakeY, shakeZ),
                    new Vector3f(r, g, b),
                    size
            );
        }

        @Override
        public @NotNull ThunderParticleOptions fromNetwork(@NotNull ParticleType<ThunderParticleOptions> type, @NotNull FriendlyByteBuf buffer) {
            Vec3 start = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
            Vec3 end = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
            float speed = buffer.readFloat();
            boolean rooted = buffer.readBoolean();
            Vector3f shake = new Vector3f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
            Vector3f color = new Vector3f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
            float size = buffer.readFloat();

            return new ThunderParticleOptions(start, end, speed, rooted, shake, color, size);
        }
    };

    @Override
    public ParticleType<?> getType() {
        return ChangedAddonParticleTypes.THUNDER_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeDouble(startPos.x).writeDouble(startPos.y).writeDouble(startPos.z);
        buffer.writeDouble(endPos.x).writeDouble(endPos.y).writeDouble(endPos.z);
        buffer.writeFloat(speed);
        buffer.writeBoolean(rooted);
        buffer.writeFloat(shake.x()).writeFloat(shake.y()).writeFloat(shake.z());
        buffer.writeFloat(color.x()).writeFloat(color.y()).writeFloat(color.z());
        buffer.writeFloat(size);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f %b %.2f %.2f %.2f %.2f %.2f %.2f %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(getType()),
                startPos.x, startPos.y, startPos.z,
                endPos.x, endPos.y, endPos.z,
                speed, rooted,
                shake.x(), shake.y(), shake.z(),
                color.x(), color.y(), color.z(),
                size
        );
    }
}