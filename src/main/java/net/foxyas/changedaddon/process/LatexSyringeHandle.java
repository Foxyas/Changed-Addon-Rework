package net.foxyas.changedaddon.process;

import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.item.LatexSyringe;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LatexSyringeHandle {

    @SubscribeEvent
    public static void onUsedOnBlock(LatexSyringe.UsedOnBlock event) {
        TransfurVariant<?> variant = event.syringeVariant;
        if (event.player.isCreative() || !event.player.isShiftKeyDown()) {
            return;
        }

        if (variant.is(ChangedAddonTags.TransfurVariants.BOSS_VARIANTS)) {
            event.setCanceled(true);
            event.player.displayClientMessage(Component.translatable("changed_addon.latex_syringe.not_valid.bosses"), true);
        } else if (variant.is(ChangedAddonTags.TransfurVariants.REMOVED_FROM_GROUNDED_SYRINGES)) {
            event.setCanceled(true);
            event.player.displayClientMessage(Component.translatable("changed_addon.latex_syringe.not_valid"), true);
        }
    }
}
