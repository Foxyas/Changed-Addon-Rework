package net.foxyas.changedaddon.process.features;

import net.foxyas.changedaddon.item.TranslatorItem;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ProcessLatexLanguage {

    /* ===== helpers ===== */
    public static boolean canUnderstandLatex(Player player) {
        return isPlayerConsiderateLatex(player) || hasTranslator(player);
    }

    public static boolean isPlayerConsiderateLatex(Player player) {
        return ProcessTransfur.isPlayerTransfurred(player);
    }

    public static boolean hasTranslator(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty()
                    && stack.getItem() instanceof TranslatorItem
                    && TranslatorItem.isEnabled(stack)) {
                return true;
            }
        }
        return false;
    }
}
