package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.item.LatexSyringe;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BossesExpUseProcedure {

    @SubscribeEvent
    public static void VariantGet(LatexSyringe.UsedOnBlock event) {
        TransfurVariant<?> variant = event.syringeVariant;
        if (event.player.isCreative() || !event.player.isShiftKeyDown()) {
            return;
        }

        if (variant == ChangedAddonTransfurVariants.EXPERIMENT_009.get()
                || variant == ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get()
                || variant == ChangedAddonTransfurVariants.EXPERIMENT_10.get()
                || variant == ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS.get()) {
            event.setCanceled(true);
            event.player.displayClientMessage(Component.translatable("changed_addon.latex_syringe.not_valid"), true);
        }
    }
}
