package net.foxyas.changedaddon.process.variantsExtraStats.visions;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class TransfurVariantVisions {

    public static @Nullable TransfurVariantVision getTransfurVariantVision(Level level, ResourceLocation formId) {
        if (level instanceof ServerLevel serverLevel) {
            if (TransfurVisionRegistry.hasVision(formId)) {
                return TransfurVisionRegistry.get(formId);
            }
        } else if (level instanceof ClientLevel clientLevel) {
            if (ClientTransfurVisionRegistry.hasVision(formId)) {
                return ClientTransfurVisionRegistry.get(formId);
            }
        }

        return null;
    }
}
