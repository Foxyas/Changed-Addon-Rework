package net.foxyas.changedaddon.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.ltxprogrammer.changed.util.UniversalDist;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public record EntityModelFadeParticleOptions(Entity target, int color, float fadeSpeed) implements ParticleOptions {

    public static final Deserializer<EntityModelFadeParticleOptions> DESERIALIZER = new Deserializer() {
        @Override
        public ParticleOptions fromCommand(@NotNull ParticleType pParticleType, @NotNull StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            Entity entity = UniversalDist.getLevel().getEntity(reader.readInt());
            reader.expect(' ');
            int color = reader.readInt();
            reader.expect(' ');
            float fadeSpeed = reader.readFloat();
            return new EntityModelFadeParticleOptions(entity, color, fadeSpeed);
        }

        @Override
        public ParticleOptions fromNetwork(@NotNull ParticleType pParticleType, @NotNull FriendlyByteBuf buf) {
            return new EntityModelFadeParticleOptions(UniversalDist.getLevel().getEntity(buf.readVarInt()), buf.readInt(), buf.readInt());
        }
    };

    public static final Codec<EntityModelFadeParticleOptions> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("target").xmap(i -> UniversalDist.getLevel().getEntity(i), Entity::getId).forGetter(EntityModelFadeParticleOptions::target),
                    Codec.INT.fieldOf("color").forGetter(EntityModelFadeParticleOptions::color),
                    Codec.FLOAT.fieldOf("fadeSpeed").forGetter(EntityModelFadeParticleOptions::fadeSpeed)
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
        buf.writeVarInt(target.getId());
        buf.writeInt(color);
        buf.writeFloat(fadeSpeed);
    }

    @Override
    public @NotNull String writeToString() {
        return ChangedAddonParticleTypes.ENTITY_MODEL_FADE.getId().toString();
    }
}