package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.client.renderer.layers.features.SonarOutlineLayer;
import net.foxyas.changedaddon.event.UntransfurEvent;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.foxyas.changedaddon.network.packet.*;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.client.animations.AnimationAssociations;
import net.ltxprogrammer.changed.entity.animation.AnimationParameters;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
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

            ChangedAddonVariables.nonNullOf(player).copyFrom(message.data);
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



    public static void handlePlayAnimationAfterParticleFade(S2CPlayAnimationAfterParticleFade<?> message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                Entity entity = level.getEntity(message.entityId);
                if (entity instanceof LivingEntity livingEntity) {

                    // 1. Partículas
                    var particleOption = ChangedAddonParticleTypes.entityModelFade(livingEntity, message.colorRGB, 0.25f);
                    if (message.count <= 1) {
                        level.addParticle(particleOption, message.pos.x, message.pos.y, message.pos.z,
                                message.motion.x * message.speed, message.motion.y * message.speed, message.motion.z * message.speed);
                    } else {
                        for (int i = 0; i < message.count; i++) {
                            level.addParticle(particleOption, message.pos.x, message.pos.y, message.pos.z,
                                    message.motion.x * message.speed, message.motion.y * message.speed, message.motion.z * message.speed);
                        }
                    }

                    // 2. Animação (Dispatch)
                    // Como não passamos listas de itens/entidades extras no construtor simplificado, usamos listas vazias
                    AnimationAssociations.dispatchAnimation(livingEntity, message.event, message.category, castParam(message.parameters), new ArrayList<>(), new ArrayList<>());
                }
            }
        });
        context.setPacketHandled(true);
    }

    // Helper para lidar com o Generics no handle
    @SuppressWarnings("unchecked")
    private static <T extends AnimationParameters> T castParam(Object param) {
        return (T) param;
    }
}
