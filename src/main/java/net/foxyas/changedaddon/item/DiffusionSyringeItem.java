package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonTabs;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.procedure.SummonEntityProcedure;
import net.foxyas.changedaddon.util.ComponentUtil;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DiffusionSyringeItem extends AbstractSyringeItem {

    public DiffusionSyringeItem() {
        super(new Item.Properties().tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB).stacksTo(16)
                .rarity(Rarity.EPIC)
        );
    }

    @Override
    public void applyEffectsAfterUse(@NotNull ItemStack pStack, Level level, LivingEntity entity) {
        super.applyEffectsAfterUse(pStack, level, entity);

        if (!(entity instanceof Player player)) return;

        if (ProcessTransfur.isPlayerTransfurred(player)) {
            SummonEntityProcedure.execute(level, player);
            PlayerUtil.UnTransfurPlayerAndPlaySound(player, !player.isCreative() && !player.isSpectator());
            player.displayClientMessage(new TranslatableComponent("changed_addon.untransfur.diffusion"), true);
            return;
        }

        if (ChangedAddonVariables.ofOrDefault(player).showWarns) player.displayClientMessage(ComponentUtil.translatable("changed_addon.untransfur.no_effect"), true);
    }
}
