package net.foxyas.changedaddon.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public record EntityModelFadeParticleOptions(int targetId, int color, float duration) implements ParticleOptions {

    public static final Deserializer<EntityModelFadeParticleOptions> DESERIALIZER = new Deserializer() {
        @Override
        public ParticleOptions fromCommand(@NotNull ParticleType pParticleType, @NotNull StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int entity = reader.readInt();
            reader.expect(' ');
            int color = reader.readInt();
            reader.expect(' ');
            float fadeSpeed = reader.readFloat();
            return new EntityModelFadeParticleOptions(entity, color, fadeSpeed);
        }

        @Override
        public ParticleOptions fromNetwork(@NotNull ParticleType pParticleType, @NotNull FriendlyByteBuf buf) {
            return new EntityModelFadeParticleOptions(buf.readInt(), buf.readInt(), buf.readFloat());
        }
    };

    public static final Codec<EntityModelFadeParticleOptions> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("targetId").forGetter(EntityModelFadeParticleOptions::targetId),
                    Codec.INT.fieldOf("color").forGetter(EntityModelFadeParticleOptions::color),
                    Codec.FLOAT.fieldOf("duration").forGetter(EntityModelFadeParticleOptions::duration)
            ).apply(instance, EntityModelFadeParticleOptions::new));

    public static Codec<EntityModelFadeParticleOptions> codec(ParticleType<EntityModelFadeParticleOptions> type) {
        return CODEC;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return ChangedAddonParticleTypes.ENTITY_MODEL_FADE.get();
    }

    @Override
    public void writeToNetwork(@NotNull FriendlyByteBuf buf) {
        buf.writeVarInt(targetId);
        buf.writeInt(color);
        buf.writeFloat(duration);
    }

    @Override
    public @NotNull String writeToString() {
        return ChangedAddonParticleTypes.ENTITY_MODEL_FADE.getId().toString();
    }
}