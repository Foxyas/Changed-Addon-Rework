package net.foxyas.changedaddon.effect.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class FallingLeafParticleOptions implements ParticleOptions {
    private final ParticleType<FallingLeafParticleOptions> type;
    private final Color3 color;
    private final float fallAcceleration;
    private final float sideAcceleration;
    private final boolean swirl;
    private final boolean flowAway;
    private final float scale;
    private final float startVelocity;

    public FallingLeafParticleOptions(ParticleType<FallingLeafParticleOptions> type, Color3 color, float fallAcceleration, float sideAcceleration, boolean swirl, boolean flowAway, float scale, float startVelocity) {
        this.type = type;
        this.color = color;
        this.fallAcceleration = fallAcceleration;
        this.sideAcceleration = sideAcceleration;
        this.swirl = swirl;
        this.flowAway = flowAway;
        this.scale = scale;
        this.startVelocity = startVelocity;
    }

    @Override
    public @NotNull ParticleType<FallingLeafParticleOptions> getType() {
        return this.type;
    }

    public Color3 getColor() {
        return this.color;
    }

    public float getFallAcceleration() { return fallAcceleration; }
    public float getSideAcceleration() { return sideAcceleration; }
    public boolean isSwirl() { return swirl; }
    public boolean isFlowAway() { return flowAway; }
    public float getScale() { return scale; }
    public float getStartVelocity() { return startVelocity; }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(this.color.toInt()); // Mantém o padrão do Changed de salvar como Int de rede
        buffer.writeFloat(this.fallAcceleration);
        buffer.writeFloat(this.sideAcceleration);
        buffer.writeBoolean(this.swirl);
        buffer.writeBoolean(this.flowAway);
        buffer.writeFloat(this.scale);
        buffer.writeFloat(this.startVelocity);
    }

    @Override
    public @NotNull String writeToString() {
        return ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()) + " " +
                this.color.toInt() + " " +
                this.fallAcceleration + " " +
                this.sideAcceleration + " " +
                this.swirl + " " +
                this.flowAway + " " +
                this.scale + " " +
                this.startVelocity;
    }

    public static Codec<FallingLeafParticleOptions> codec(ParticleType<FallingLeafParticleOptions> type) {
        return RecordCodecBuilder.create((instance) -> instance.group(
                Codec.unit(type).fieldOf("type").forGetter(o -> type),
                Color3.CODEC.fieldOf("color").forGetter(FallingLeafParticleOptions::getColor),
                Codec.FLOAT.fieldOf("fall").forGetter(FallingLeafParticleOptions::getFallAcceleration),
                Codec.FLOAT.fieldOf("side").forGetter(FallingLeafParticleOptions::getSideAcceleration),
                Codec.BOOL.fieldOf("swirl").forGetter(FallingLeafParticleOptions::isSwirl),
                Codec.BOOL.fieldOf("flow").forGetter(FallingLeafParticleOptions::isFlowAway),
                Codec.FLOAT.fieldOf("scale").forGetter(FallingLeafParticleOptions::getScale),
                Codec.FLOAT.fieldOf("velocity").forGetter(FallingLeafParticleOptions::getStartVelocity)
        ).apply(instance, FallingLeafParticleOptions::new));
    }

    public static final ParticleOptions.Deserializer<FallingLeafParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        @Override
        public @NotNull FallingLeafParticleOptions fromCommand(@NotNull ParticleType<FallingLeafParticleOptions> type, @NotNull StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            // Lendo a cor como Int igual a classe do Changed faz no comando (/particle <tipo> <corInt> ...)
            Color3 color = Color3.fromInt(reader.readInt());
            reader.expect(' ');
            float fall = reader.readFloat();
            reader.expect(' ');
            float side = reader.readFloat();
            reader.expect(' ');
            boolean swirl = reader.readBoolean();
            reader.expect(' ');
            boolean flow = reader.readBoolean();
            reader.expect(' ');
            float scale = reader.readFloat();
            reader.expect(' ');
            float vel = reader.readFloat();
            return new FallingLeafParticleOptions(type, color, fall, side, swirl, flow, scale, vel);
        }

        @Override
        public @NotNull FallingLeafParticleOptions fromNetwork(@NotNull ParticleType<FallingLeafParticleOptions> type, FriendlyByteBuf buffer) {
            // Lendo o Int enviado pela rede para reconstruir o Color3
            Color3 color = Color3.fromInt(buffer.readInt());
            return new FallingLeafParticleOptions(type, color,
                    buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(),
                    buffer.readBoolean(), buffer.readFloat(), buffer.readFloat());
        }
    };
}