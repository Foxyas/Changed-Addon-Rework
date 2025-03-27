package net.foxyas.changedaddon.effect.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class ThunderSparkOption implements ParticleOptions {
    public static final Deserializer<ThunderSparkOption> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull ThunderSparkOption fromNetwork(@NotNull ParticleType<ThunderSparkOption> type, FriendlyByteBuf buffer) {
            return new ThunderSparkOption(type,buffer.readInt());
        }

        @Override
        public @NotNull ThunderSparkOption fromCommand(@NotNull ParticleType<ThunderSparkOption> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            if (!reader.canRead()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().create();
            }

            try {
                int variant = reader.readInt();
                return new ThunderSparkOption(type,variant);
            } catch (Exception e) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().create(reader);
            }
        }
    };

    public static Codec<ThunderSparkOption> codec(ParticleType<ThunderSparkOption> type) {
        return RecordCodecBuilder.create(builder -> builder.group(
                Codec.INT.fieldOf("variant").forGetter(option -> option.variant)
        ).apply(builder, (id) -> new ThunderSparkOption(type, id)));
    }

    private final int variant;

    private ParticleType<ThunderSparkOption> Type;

    public ThunderSparkOption(ParticleType<ThunderSparkOption> type, int variant) {
        super();
        this.Type = type;
        this.variant = Mth.clamp(variant, 0, 2); // Garante que está entre 0 e 2
    }

    public int getVariant() {
        return variant;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return Type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(variant);
    }

    @Override
    public @NotNull String writeToString() {
        return "thunder_spark_" + variant;
    }


}
