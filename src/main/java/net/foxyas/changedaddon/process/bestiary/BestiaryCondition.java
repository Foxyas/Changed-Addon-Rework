package net.foxyas.changedaddon.process.bestiary;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Optional;
import java.util.function.Predicate;

public record BestiaryCondition(Optional<ResourceLocation> achievementId) implements Predicate<Player> {

    public static final Codec<BestiaryCondition> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.optionalFieldOf("achievement_id").forGetter(BestiaryCondition::achievementId)
            ).apply(instance, BestiaryCondition::new)
    );

    /**
     * Checks if the given player meets the condition to unlock or view this bestiary entry.
     * Works on both Logical Server and Logical Client.
     */
    @Override
    public boolean test(Player player) {
        if (achievementId.isEmpty()) {
            return true; // Unconditional
        }

        ResourceLocation id = achievementId.get();

        // 1. Server-side check
        if (player instanceof ServerPlayer serverPlayer) {
            var advancement = serverPlayer.getServer().getAdvancements().getAdvancement(id);
            if (advancement == null) {
                return false;
            }
            return serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone();
        }

        // 2. Client-side check
        if (player.level().isClientSide()) {
            return checkClientAdvancement(id);
        }

        return false;
    }

    private static boolean checkClientAdvancement(ResourceLocation advancementId) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            var connection = Minecraft.getInstance().getConnection();
            if (connection != null) {
                ClientAdvancements clientAdvancements = connection.getAdvancements();
                // Check if the advancement exists and is completed in the client's local tracker
                var progressMap = clientAdvancements.getAdvancements();
                var advancement = progressMap.get(advancementId);
                // Alternative direct lookup via client advancement listener
                return advancement != null;
            }
        }
        return false;
    }
}