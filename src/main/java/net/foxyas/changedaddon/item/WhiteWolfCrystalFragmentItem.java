package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.procedure.AddTransfurProgressProcedure;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class WhiteWolfCrystalFragmentItem extends Item {
    public WhiteWolfCrystalFragmentItem() {
        super(new Item.Properties()
                .stacksTo(64).rarity(Rarity.COMMON));
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemstack, @NotNull LivingEntity entity, @NotNull LivingEntity sourceentity) {
        boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
        AddTransfurProgressProcedure.addRed(entity, 5);
        return retval;
    }
}
