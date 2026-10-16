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
        List<BestiaryCondition> conditions,
        int order
) {
    public static final Codec<BestiaryEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    listOrSingle(TransfurVariantHolder.CODEC).fieldOf("variants").forGetter(BestiaryEntry::variants),
                    ExtraCodecs.COMPONENT.fieldOf("title").forGetter(BestiaryEntry::title), // matching your JSON key "title"
                    ExtraCodecs.COMPONENT.fieldOf("description").forGetter(BestiaryEntry::description),
                    listOrSingle(BestiaryCondition.CODEC).fieldOf("condition").forGetter(BestiaryEntry::conditions),
                    Codec.INT.optionalFieldOf("order", 0).forGetter(BestiaryEntry::order)
            ).apply(instance, BestiaryEntry::new)
    );


    public boolean isUnlockedForPlayer(Player player) {
        return this.conditions.stream().allMatch(bestiaryCondition -> bestiaryCondition.test(player));
    }
}