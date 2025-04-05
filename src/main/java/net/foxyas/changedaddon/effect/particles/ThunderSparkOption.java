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
            int energyCharge = buffer.readInt();
            return new ThunderSparkOption(type, energyCharge);
        }

        @Override
        public @NotNull ThunderSparkOption fromCommand(@NotNull ParticleType<ThunderSparkOption> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            if (!reader.canRead()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().create();
            }
            try {
                int energyCharge = reader.readInt();
                return new ThunderSparkOption(type, energyCharge);
            } catch (Exception e) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().create(reader);
            }
        }
    };

    public static Codec<ThunderSparkOption> codec(ParticleType<ThunderSparkOption> type) {
        return RecordCodecBuilder.create(builder -> builder.group(
                Codec.INT.fieldOf("energy").forGetter(option -> option.EnergyCharge)
        ).apply(builder, (energy) -> new ThunderSparkOption(type, energy)));
    }

    private final int EnergyCharge;
    private ParticleType<ThunderSparkOption> Type;
    public ThunderSparkOption(ParticleType<ThunderSparkOption> type, int energyCharge) {
        super();
        this.Type = type;
        this.EnergyCharge = energyCharge; //Mth.clamp(energyCharge, 1, 80); // Garante que está entre 0 e 2
    }

    public int getEnergyCharge() {
        return EnergyCharge;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return Type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(EnergyCharge);
    }

    @Override
    public @NotNull String writeToString() {
        return "thunder_spark_" + EnergyCharge;
    }


}
