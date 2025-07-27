
package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonAnimationEvents;
import net.foxyas.changedaddon.init.ChangedAddonTabs;
import net.ltxprogrammer.changed.init.ChangedAnimationEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ChangedBookItem extends Item {
    public ChangedBookItem() {
        super(new Item.Properties().stacksTo(1).tab(ChangedAddonTabs.TAB_CHANGED_ADDON).rarity(Rarity.RARE));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack p_41452_) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack p_41454_) {
        return 1;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        return super.use(level, player, interactionHand);
    }
}
