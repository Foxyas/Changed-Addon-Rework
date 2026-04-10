package net.foxyas.changedaddon.variant;

import net.foxyas.changedaddon.event.UntransfurEvent;
import net.foxyas.changedaddon.network.packet.SyncAllUntransfurImmunityPacket;
import net.foxyas.changedaddon.network.packet.utils.PacketsUtils;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.util.KeyStateTracker;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface TransfurVariantInstanceExtensor {

    AbstractAbility<?> getSecondSelectedAbility();

    void setSecondSelectedAbility(AbstractAbility<?> ability);

    int getTicksSinceSecondAbilityActivity();

    void resetTicksSinceSecondAbilityActivity();

    KeyStateTracker getSecondAbilityKey();

    void setSecondAbilityKey(KeyStateTracker secondAbilityKey);

    AbstractAbilityInstance getSecondSelectedAbilityInstance();

    boolean getUntransfurImmunity(UntransfurEvent.UntransfurType type);

    void setUntransfurImmunity(UntransfurEvent.UntransfurType type, boolean value);

    default boolean isTransfuredBySafeMethod() {
        return true;
    }

    default void setTransfuredBySafeMethod(boolean value) {

    }

    //TODO make this getters
//    boolean wonFTK();
//    int getTicksSinceWinningFTK();
//    int getTicksFightingForConscience();

    default void maySendDataUpdate() {
        if (!(this instanceof TransfurVariantInstance<?> variantInstance)) {
            return;
        }

        Player player = variantInstance.getHost();
        if (player == null) return;

        if (!player.level().isClientSide) {
            if (player instanceof ServerPlayer serverPlayer && serverPlayer.connection != null) {
                boolean untransfurImmunitySurvival = this.getUntransfurImmunity(UntransfurEvent.UntransfurType.SURVIVAL);
                boolean untransfurImmunityCommand = this.getUntransfurImmunity(UntransfurEvent.UntransfurType.COMMAND);
                PacketsUtils.sendToPlayer(new SyncAllUntransfurImmunityPacket(serverPlayer.getId(), untransfurImmunityCommand, untransfurImmunitySurvival), serverPlayer);

                // TODO: maybe Change this to be just the packet below instead of a custom one?
                //  Changed.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer), SyncTransfurPacket.Builder.of(player));
            }
        }
    }
}
