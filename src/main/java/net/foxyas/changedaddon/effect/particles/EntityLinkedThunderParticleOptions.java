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

public class EntityLinkedThunderParticleOptions implements ParticleOptions {

    protected final float speed;
    protected final float size;

    protected final boolean rooted;
    protected final boolean staticBody;

    protected final Vector3f shake;
    protected final Vector3f color;

    protected final int lifeTime;
    protected final int bodyShakeFrequency;

    private final int targetId;
    private final boolean useTargetPosAsBaseForDeltas;

    public EntityLinkedThunderParticleOptions(
            int targetId,
            boolean useTargetPosAsBaseForDeltas,
            int lifeTime,
            float speed,
            boolean rooted,
            boolean staticBody,
            int bodyShakeFrequency,
            Vector3f shake,
            Vector3f color,
            float size
    ) {
        this.targetId = targetId;
        this.useTargetPosAsBaseForDeltas = useTargetPosAsBaseForDeltas;
        this.lifeTime = lifeTime;
        this.speed = speed;
        this.rooted = rooted;
        this.shake = shake;
        this.color = color;
        this.size = size;
        this.staticBody = staticBody;
        this.bodyShakeFrequency = bodyShakeFrequency;
    }

    public boolean shouldUseTargetPosAsBaseForDeltas() {
        return useTargetPosAsBaseForDeltas;
    }

    public int getTargetId() {
        return targetId;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isRooted() {
        return rooted;
    }

    public Vector3f getShake() {
        return shake;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getSize() {
        return size;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public int getBodyShakeFrequency() {
        return bodyShakeFrequency;
    }

    public boolean isStaticBody() {
        return staticBody;
    }

    // CODEC for DataFixerUpper / Serialization
    public static final Codec<EntityLinkedThunderParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("targetId").forGetter(EntityLinkedThunderParticleOptions::getTargetId),
            Codec.BOOL.fieldOf("useTargetPosAsBaseForDeltas").forGetter(EntityLinkedThunderParticleOptions::shouldUseTargetPosAsBaseForDeltas),
            Codec.INT.fieldOf("lifeTime").forGetter(EntityLinkedThunderParticleOptions::getLifeTime),
            Codec.FLOAT.fieldOf("speed").forGetter(EntityLinkedThunderParticleOptions::getSpeed),
            Codec.BOOL.fieldOf("rooted").forGetter(EntityLinkedThunderParticleOptions::isRooted),
            Codec.BOOL.fieldOf("staticBody").forGetter(EntityLinkedThunderParticleOptions::isStaticBody),
            Codec.INT.fieldOf("bodyShakeFrequency").forGetter(EntityLinkedThunderParticleOptions::getBodyShakeFrequency),
            ExtraCodecs.VECTOR3F.fieldOf("shake").forGetter(EntityLinkedThunderParticleOptions::getShake),
            ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(EntityLinkedThunderParticleOptions::getColor),
            Codec.FLOAT.fieldOf("size").forGetter(EntityLinkedThunderParticleOptions::getSize)
    ).apply(instance, EntityLinkedThunderParticleOptions::new));

    public static Codec<EntityLinkedThunderParticleOptions> codec(ParticleType<EntityLinkedThunderParticleOptions> type) {
        return CODEC;
    }

    // DESERIALIZER for Commands and Network Reading
    @SuppressWarnings("deprecation")
    public static final Deserializer<EntityLinkedThunderParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull EntityLinkedThunderParticleOptions fromCommand(@NotNull ParticleType<EntityLinkedThunderParticleOptions> type, @NotNull StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int targetId = reader.readInt();

            reader.expect(' ');
            boolean useTargetPosAsBaseForDeltas = reader.readBoolean();

            reader.expect(' ');
            int lifeTime = reader.readInt();

            reader.expect(' ');
            float speed = reader.readFloat();

            reader.expect(' ');
            boolean rooted = reader.readBoolean();

            reader.expect(' ');
            boolean staticBody = reader.readBoolean();

            reader.expect(' ');
            int bodyShakeFrequency = reader.readInt();

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

            return new EntityLinkedThunderParticleOptions(
                    targetId,
                    useTargetPosAsBaseForDeltas,
                    lifeTime,
                    speed,
                    rooted,
                    staticBody,
                    bodyShakeFrequency,
                    new Vector3f(shakeX, shakeY, shakeZ),
                    new Vector3f(r, g, b),
                    size
            );
        }

        @Override
        public @NotNull EntityLinkedThunderParticleOptions fromNetwork(@NotNull ParticleType<EntityLinkedThunderParticleOptions> type, @NotNull FriendlyByteBuf buffer) {
            int entityId = buffer.readVarInt();
            boolean useTargetPosAsBaseForDeltas = buffer.readBoolean();
            int lifeTime = buffer.readVarInt();
            int bodyShakeFrequency = buffer.readInt();
            float speed = buffer.readFloat();
            boolean rooted = buffer.readBoolean();
            boolean staticBody = buffer.readBoolean();
            Vector3f shake = new Vector3f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
            Vector3f color = new Vector3f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
            float size = buffer.readFloat();

            return new EntityLinkedThunderParticleOptions(entityId, useTargetPosAsBaseForDeltas, lifeTime, speed, rooted, staticBody, bodyShakeFrequency, shake, color, size);
        }
    };

    @Override
    public ParticleType<?> getType() {
        return ChangedAddonParticleTypes.ENTITY_LINKED_THUNDER_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeVarInt(targetId);
        buffer.writeBoolean(useTargetPosAsBaseForDeltas);
        buffer.writeVarInt(lifeTime);
        buffer.writeInt(bodyShakeFrequency);
        buffer.writeFloat(speed);
        buffer.writeBoolean(rooted);
        buffer.writeBoolean(staticBody);
        buffer.writeFloat(shake.x()).writeFloat(shake.y()).writeFloat(shake.z());
        buffer.writeFloat(color.x()).writeFloat(color.y()).writeFloat(color.z());
        buffer.writeFloat(size);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format(Locale.ROOT, "%s %d %b %d %.2f %b %b %d %.2f %.2f %.2f %.2f %.2f %.2f %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(getType()),
                targetId, useTargetPosAsBaseForDeltas, lifeTime, speed, rooted, staticBody, bodyShakeFrequency,
                shake.x(), shake.y(), shake.z(),
                color.x(), color.y(), color.z(),
                size
        );
    }
}