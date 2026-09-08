package net.foxyas.changedaddon.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class OrangeJuiceItem extends Item {

    public OrangeJuiceItem() {
        super(new Item.Properties()
                .stacksTo(64).rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder().nutrition(8).saturationMod(1.125f).alwaysEat().build()));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return 28;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull LivingEntity entity) {
        ItemStack retval = new ItemStack(Items.GLASS_BOTTLE);
        super.finishUsingItem(itemstack, world, entity);
        if (itemstack.isEmpty()) {
            return retval;
        } else {
            if (entity instanceof Player player && !player.getAbilities().instabuild) {
                if (!player.getInventory().add(retval))
                    player.drop(retval, false);
            }
            return itemstack;
        }
    }
}
