package net.foxyas.changedaddon.process.bestiary;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static net.foxyas.changedaddon.util.ExtraCodecs.listOrSingle;

public record BestiaryCondition(
        List<ResourceLocation> achievements,
        boolean reverse
) implements Predicate<Player> {

    public static final Codec<BestiaryCondition> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    listOrSingle(ResourceLocation.CODEC)
                            .optionalFieldOf("achievements", Collections.emptyList())
                            .forGetter(BestiaryCondition::achievements),
                    Codec.BOOL.optionalFieldOf("reverse", false)
                            .forGetter(BestiaryCondition::reverse)
            ).apply(instance, BestiaryCondition::new)
    );

    @Override
    public boolean test(Player player) {
        boolean result = evaluateConditions(player);
        return reverse != result;
    }

    private boolean evaluateConditions(Player player) {
        // 1. Validar se todas as conquistas (achievements) listadas foram obtidas
        if (!achievements.isEmpty()) {
            if (player instanceof ServerPlayer serverPlayer) {
                var advancementManager = serverPlayer.getServer().getAdvancements();
                for (ResourceLocation id : achievements) {
                    Advancement advancement = advancementManager.getAdvancement(id);
                    if (advancement == null) {
                        return false;
                    }
                    AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
                    if (!progress.isDone()) {
                        return false; // Precisa ter completado todas da lista
                    }
                }
            } else if (player.level().isClientSide()) {
                for (ResourceLocation id : achievements) {
                    if (!checkClientAdvancement(id)) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }

        return true;
    }

    private static boolean checkClientAdvancement(ResourceLocation advancementId) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            var connection = Minecraft.getInstance().getConnection();
            if (connection != null) {
                ClientAdvancements clientAdvancements = connection.getAdvancements();
                Advancement advancement = clientAdvancements.getAdvancements().get(advancementId);
                return advancement != null;
            }
        }
        return false;
    }
}