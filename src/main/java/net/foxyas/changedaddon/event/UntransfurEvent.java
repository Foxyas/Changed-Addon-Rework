package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.process.UntransfurReason;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

public class UntransfurEvent extends Event {

    public final UntransfurReason untransfurReason;
    private final Player player;
    @Nullable
    private final TransfurVariant<?> oldVariant;
    @Nullable
    public TransfurVariant<?> newVariant = null;

    public UntransfurEvent(Player player, @Nullable TransfurVariant<?> oldVariant, UntransfurReason untransfurReason) {
        this.player = player;
        this.oldVariant = oldVariant;
        this.untransfurReason = untransfurReason;
    }

    public Player getPlayer() {
        return player;
    }

    @Nullable
    public TransfurVariant<?> getOldVariant() {
        return oldVariant;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

//    public static class UntransfurReason {
//        public static final UntransfurReason COMMAND = new UntransfurReason(false, false);
//        public static final UntransfurReason SURVIVAL = new UntransfurReason(true, false);
//
//        public final boolean isNatural;
//        public final boolean bypassImmunity;
//
//        public UntransfurReason(boolean isNatural, boolean bypassImmunity) {
//            this.isNatural = isNatural;
//            this.bypassImmunity = bypassImmunity;
//        }
//    }
}