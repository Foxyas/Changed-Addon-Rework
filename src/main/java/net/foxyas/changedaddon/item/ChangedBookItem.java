package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.NotNull;

public class ChangedBookItem extends Item {
    public ChangedBookItem() {
        super(new Item.Properties().stacksTo(1)
                .rarity(Rarity.RARE));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack p_41452_) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack p_41454_) {
        return 1;
    }
}
