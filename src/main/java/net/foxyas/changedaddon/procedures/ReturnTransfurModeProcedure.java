package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import static net.ltxprogrammer.changed.entity.TransfurMode.*;

public class ReturnTransfurModeProcedure {
    public static double execute(Entity entity) {
        if (entity == null)
            return 0;
        Player PlayerTarget = (Player) entity;
        TransfurVariantInstance<?> PlayerVariant = ProcessTransfur.getPlayerTransfurVariant(PlayerTarget);
        boolean istransfur = ProcessTransfur.isPlayerLatex(PlayerTarget);
        if (istransfur) {
            TransfurVariant<?> Variant = PlayerVariant.getParent();
            if (PlayerVariant.transfurMode.equals(REPLICATION)) {
                return 1;
            } else if (PlayerVariant.transfurMode.equals(ABSORPTION)) {
                return 2;
            } else if (PlayerVariant.transfurMode.equals(NONE)) {
                return 3;
            }
        }
        return 0;
    }
}

class setPlayerLatexAge {
    public static void execute(Player player, int number, boolean add) {
        if (player == null) {
            return;
        }
        TransfurVariantInstance<?> PlayerVariant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (PlayerVariant == null) {
        } else {
            int Age = PlayerVariant.ageAsVariant;
            if (add = true) {
                PlayerVariant.ageAsVariant = Age + number;
            }
            if (add = false) {
                PlayerVariant.ageAsVariant = number;
            }
        }
    }
}

class setPlayerTransfurMode {
    public static void execute(Player player, int mode) {
        if (player == null) {
            return;
        }
        TransfurVariantInstance<?> PlayerVariant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (PlayerVariant == null) {
        } else {
            if (mode == 1) {
                PlayerVariant.transfurMode = TransfurMode.REPLICATION;
            } else if (mode == 2) {
                PlayerVariant.transfurMode = TransfurMode.ABSORPTION;
            } else if (mode >= 3) {
                PlayerVariant.transfurMode = TransfurMode.NONE;
            }
        }
    }

    public static void execute(Player player, double mode) {
        if (player == null) {
            return;
        }
        TransfurVariantInstance<?> PlayerVariant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (PlayerVariant == null) {
        } else {
            if (mode == 1) {
                PlayerVariant.transfurMode = TransfurMode.REPLICATION;
            } else if (mode == 2) {
                PlayerVariant.transfurMode = TransfurMode.ABSORPTION;
            } else if (mode >= 3) {
                PlayerVariant.transfurMode = TransfurMode.NONE;
            }
        }
    }

    public static void execute(Player player, float mode) {
        if (player == null) {
            return;
        }
        TransfurVariantInstance<?> PlayerVariant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (PlayerVariant == null) {
        } else {
            if (mode == 1) {
                PlayerVariant.transfurMode = TransfurMode.REPLICATION;
            } else if (mode == 2) {
                PlayerVariant.transfurMode = TransfurMode.ABSORPTION;
            } else if (mode >= 3) {
                PlayerVariant.transfurMode = TransfurMode.NONE;
            }
        }
    }
}


class GetDefault {
    public static boolean execute(Player player) {
        if (player == null) {
            return false;
        }
        TransfurVariantInstance<?> PlayerVariant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (PlayerVariant == null) {
            return false;
        } else {
            TransfurVariant<?> getVariant = PlayerVariant.getParent();
            return getVariant.transfurMode() != TransfurMode.NONE;
        }
    }

    public static double GetDefaultValue(Entity entity) {
        if (entity == null)
            return 0;
        Player PlayerTarget = (Player) entity;
        TransfurVariantInstance<?> PlayerVariant = ProcessTransfur.getPlayerTransfurVariant(PlayerTarget);
        boolean istransfur = ProcessTransfur.isPlayerLatex(PlayerTarget);
        if (istransfur) {
            TransfurVariant<?> Variant = PlayerVariant.getParent();
            if (Variant.transfurMode.equals(REPLICATION)) {
                return 1;
            } else if (Variant.transfurMode.equals(ABSORPTION)) {
                return 2;
            } else if (Variant.transfurMode.equals(NONE)) {
                return 3;
            }
        }
        return 0;
    }
}

