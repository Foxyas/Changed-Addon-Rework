package net.foxyas.changedaddon.process.bestiary;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDiet;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;

import java.util.List;

import static net.foxyas.changedaddon.util.ExtraCodecs.*;

public record BestiaryEntry(
        List<TransfurVariantHolder> variants,
        Component title,
        Component description,
        BestiaryCondition condition,
        int order
) {
    public static final Codec<BestiaryEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    listOrSingle(TransfurVariantHolder.CODEC).fieldOf("variants").forGetter(BestiaryEntry::variants),
                    ExtraCodecs.COMPONENT.fieldOf("tittle").forGetter(BestiaryEntry::title), // matching your JSON key "tittle"
                    ExtraCodecs.COMPONENT.fieldOf("description").forGetter(BestiaryEntry::description),
                    BestiaryCondition.CODEC.fieldOf("condition").forGetter(BestiaryEntry::condition),
                    Codec.INT.optionalFieldOf("order", 0).forGetter(BestiaryEntry::order)
            ).apply(instance, BestiaryEntry::new)
    );


    public boolean isUnlockedForPlayer(Player player) {
        return this.condition.test(player);
    }
}