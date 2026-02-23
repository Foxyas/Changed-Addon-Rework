package net.foxyas.changedaddon.effect.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class SignalParticleOption implements ParticleOptions {

    public static final Deserializer<SignalParticleOption> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull SignalParticleOption fromNetwork(@NotNull ParticleType<SignalParticleOption> type, FriendlyByteBuf buffer) {
            int energyCharge = buffer.readInt();
            return new SignalParticleOption(type, energyCharge);
        }

        @Override
        public @NotNull SignalParticleOption fromCommand(@NotNull ParticleType<SignalParticleOption> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            if (!reader.canRead()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().create();
            }
            try {
                int signalStrength = reader.readInt();
                return new SignalParticleOption(type, signalStrength);
            } catch (Exception e) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().create(reader);
            }
        }
    };

    private final int signalStrength;
    private final ParticleType<SignalParticleOption> Type;
    private ItemStack signalCatcherItem = ItemStack.EMPTY;

    public SignalParticleOption(ParticleType<SignalParticleOption> type, int signalStrength) {
        super();
        this.Type = type;
        this.signalStrength = signalStrength; //Mth.clamp(energyCharge, 1, 80); // Garante que está entre 0 e 2
    }

    public SignalParticleOption(ParticleType<SignalParticleOption> type, int signalStrength, ItemStack signalCatcherItem) {
        super();
        this.Type = type;
        this.signalStrength = signalStrength; //Mth.clamp(energyCharge, 1, 80); // Garante que está entre 0 e 2
        this.signalCatcherItem = signalCatcherItem;
    }

    public static Codec<SignalParticleOption> codec(ParticleType<SignalParticleOption> type) {
        return RecordCodecBuilder.create(builder -> builder.group(
                Codec.INT.fieldOf("signalStrength").forGetter(option -> option.signalStrength),
                ItemStack.CODEC.optionalFieldOf("signalCatcherItem", ItemStack.EMPTY).forGetter(option -> option.signalCatcherItem)
        ).apply(builder, (signalStrength, itemStack) -> new SignalParticleOption(type, signalStrength)));
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return Type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(signalStrength);
    }

    @Override
    public @NotNull String writeToString() {
        return "signal_" + signalStrength;
    }

    @Nullable
    public ItemStack getSignalCatcherItem() {
        return signalCatcherItem;
    }

    public void setSignalCatcherItem(@Nullable ItemStack signalCatcherItem) {
        this.signalCatcherItem = signalCatcherItem;
    }
}
