package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.item.LatexSyringe;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BossesExpUseProcedure {

    @SubscribeEvent
    public static void VariantGet(LatexSyringe.UsedOnBlock event) {
        TransfurVariant<?> variant = event.syringeVariant;
        if (event.player.isCreative()) {
            return;
        }

        if (variant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009.get()
                || variant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get()
                || variant == ChangedAddonTransfurVariants.EXPERIMENT_10.get()
                || variant == ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS.get()) {
            event.setCanceled(true);
            event.player.displayClientMessage(new TranslatableComponent("changed_addon.latex_syringe.not_valid"), true);
        }
    }
}
