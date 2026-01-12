package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.client.renderer.layers.features.SonarOutlineLayer;
import net.foxyas.changedaddon.network.packet.ClientboundOpenFTKCScreenPacket;
import net.foxyas.changedaddon.network.packet.ClientboundSonarUpdatePacket;
import net.foxyas.changedaddon.network.packet.SafeGrabSyncPacket;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class ClientPacketHandler {

    public static void handleOpenFTKCScreenPacket(ClientboundOpenFTKCScreenPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> Minecraft.getInstance().setScreen(packet.minigameType().screen.get()));
        context.setPacketHandled(true);
    }

    public static void handleSonarUpdatePacket(ClientboundSonarUpdatePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> SonarOutlineLayer.SonarClientState.setTicksToRenderEntities(packet.ticks(), packet.lastTicks(), packet.fadeInTicks(), packet.fadeOutTicks(), packet.maxDist(), packet.mode()));
        context.setPacketHandled(true);
    }

    public static void handleSafeGrabSync(SafeGrabSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier){
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;

            Entity e = level.getEntity(packet.targetId());

            Optional<GrabEntityAbilityInstance> ability = IAbstractChangedEntity.forEitherSafe(e).map(entity -> entity.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get()));
            if (ability.isEmpty()) return;

            ((GrabEntityAbilityExtensor)ability.get()).setSafeMode(packet.safeMode());
        });
        context.setPacketHandled(true);
    }
}
