package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.event.UntransfurEvent;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.network.PacketUtil;
import net.foxyas.changedaddon.util.TransfurVariantUtils;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.Util;
import net.minecraft.commands.Commands;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public record RespawnAsTransfurPacket(ResourceLocation selected) {

    private static final ResourceLocation RANDOM = Changed.modResource("random");

    public RespawnAsTransfurPacket(FriendlyByteBuf buf) {
        this(PacketUtil.readNullable(buf, FriendlyByteBuf::readResourceLocation));
    }

    public static void handler(RespawnAsTransfurPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {// Server Side Packet
            ServerPlayer player = context.getSender();
            if (player == null || !player.isDeadOrDying()) return;


            Level level = player.level();
            player.connection.handleClientCommand(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
            player = player.connection.player;//Get new player

            if (!ChangedAddonServerConfiguration.ALLOW_RESPAWN_AS_TRANSFUR.get()) return;

            ResourceLocation selected = message.selected;
            TransfurVariant<?> tf;
            if (!player.hasPermissions(Commands.LEVEL_GAMEMASTERS)
                    && !ChangedAddonServerConfiguration.ALLOW_PLAYERS_TO_SELECT_RESPAWN_TRANSFUR.get()) {
                tf = pickRandomAllowed(player);
            } else {
                if (selected == null || selected.equals(RANDOM)) {
                    tf = pickRandom(level);
                } else {
                    tf = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(selected);
                    if (isForRemoval(tf, level)) tf = pickRandom(level);
                }
            }

            if (tf == null) return;

            TransfurVariantInstance<?> instance = ProcessTransfur.setPlayerTransfurVariant(player,
                    tf,
                    TransfurContext.hazard(TransfurCause.DEFAULT),
                    1);

            if (instance instanceof TransfurVariantInstanceExtensor transfurVariantInstanceExtensor) {
                transfurVariantInstanceExtensor.setUntransfurImmunity(UntransfurEvent.UntransfurType.SURVIVAL, true);
            }
        });
        context.setPacketHandled(true);
    }

    private static TransfurVariant<?> pickRandomAllowed(ServerPlayer player) {
        List<? extends TransfurVariant<?>> list = TransfurVariantUtils.getTransfurVariantsFormIdFromStringList(
                ChangedAddonServerConfiguration.ALLOWED_RESPAWN_TRANSFURS.get(),
                player.level,
                true,
                true
        ).stream().map(loc -> ChangedRegistry.TRANSFUR_VARIANT.get().getValue(loc))
                .filter(tf -> tf != null && !isForRemoval(tf, player.level()))
                .toList();

        return Util.getRandom(list, player.getRandom());
    }

    private static TransfurVariant<?> pickRandom(Level level) {
        RandomSource random = level.getRandom();
        return Util.getRandom(TransfurVariant.getPublicTransfurVariants().filter(tf -> !isForRemoval(tf, level)).toList(), random);
    }

    private static boolean isForRemoval(TransfurVariant<?> variant, Level level) {
        return ChangedAddonTransfurVariants.getRemovedVariantsList(level).contains(variant);
    }

    public void encode(FriendlyByteBuf buf) {
        PacketUtil.writeNullable(buf, FriendlyByteBuf::writeResourceLocation, selected);
    }
}