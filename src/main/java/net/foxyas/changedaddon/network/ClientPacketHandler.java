package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.client.renderer.layers.features.SonarOutlineLayer;
import net.foxyas.changedaddon.event.UntransfurEvent;
import net.foxyas.changedaddon.network.packet.*;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
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

    public static void handleSafeGrabSync(SafeGrabSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;

            Entity e = level.getEntity(packet.targetId());

            Optional<GrabEntityAbilityInstance> ability = IAbstractChangedEntity.forEitherSafe(e).map(entity -> entity.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get()));
            if (ability.isEmpty()) return;

            ((GrabEntityAbilityExtensor) ability.get()).setSafeMode(packet.safeMode());
        });
        context.setPacketHandled(true);
    }

    public static void handlerVariableSync(ChangedAddonVariables.SyncPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            assert player != null;
            if (player.isDeadOrDying()) return;

            ChangedAddonVariables.PlayerVariables variables = ChangedAddonVariables.nonNullOf(player);
            ChangedAddonVariables.PlayerVariables syncedVars = message.data;
            variables.showWarns = syncedVars.showWarns;
            variables.consciousnessFightProgress = syncedVars.consciousnessFightProgress;
            variables.FTKCminigameType = syncedVars.FTKCminigameType;
            variables.resetTransfurAdvancements = syncedVars.resetTransfurAdvancements;
            variables.actCooldown = syncedVars.actCooldown;
            variables.patCooldown = syncedVars.patCooldown;
            variables.areDarkLatex = syncedVars.areDarkLatex;
            variables.LatexInfectionCooldown = syncedVars.LatexInfectionCooldown;
            variables.untransfurProgress = syncedVars.untransfurProgress;
            variables.Exp009TransfurAllowed = syncedVars.Exp009TransfurAllowed;
            variables.Exp10TransfurAllowed = syncedVars.Exp10TransfurAllowed;
        });
        context.setPacketHandled(true);
    }

    public static void handleUntransfurImmunitySync(SyncUntransfurImmunityPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // No Cliente: Player é o Minecraft.getInstance().player
            // No Servidor: Player é o context.getSender()
            if (context.getSender() != null || context.getDirection().getReceptionSide().isServer()) {
                return;
            }

            Entity player = null;
            if (Minecraft.getInstance().level != null) {
                player = Minecraft.getInstance().level.getEntity(packet.playerId);
            }

            if (player != null) {
                TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(EntityUtil.playerOrNull(player));
                if (variant instanceof TransfurVariantInstanceExtensor extensor) {
                    extensor.setUntransfurImmunity(packet.type, packet.value);
                }
            }
        });
        context.setPacketHandled(true);
    }

    public static void handleAllUntransfurImmunitySync(SyncAllUntransfurImmunityPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // No Cliente: Player é o Minecraft.getInstance().player
            // No Servidor: Player é o context.getSender()
            if (context.getSender() != null || context.getDirection().getReceptionSide().isServer()) {
                return;
            }

            Entity player = null;
            if (Minecraft.getInstance().level != null) {
                player = Minecraft.getInstance().level.getEntity(packet.playerId);
            }

            if (player != null) {
                TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(EntityUtil.playerOrNull(player));
                if (variant instanceof TransfurVariantInstanceExtensor extensor) {
                    boolean survivalImmunity = packet.survivalImmunity;
                    boolean commandImmunity = packet.commandImmunity;

                    extensor.setUntransfurImmunity(UntransfurEvent.UntransfurType.SURVIVAL, survivalImmunity);
                    extensor.setUntransfurImmunity(UntransfurEvent.UntransfurType.COMMAND, commandImmunity);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
