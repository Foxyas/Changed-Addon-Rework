package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.procedure.AddTransfurProgressProcedure;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class CrystalAddagerRedItem extends SwordItem {
    public CrystalAddagerRedItem() {
        super(new Tier() {
                  public int getUses() {
                      return 524;
                  }

                  public float getSpeed() {
                      return 4f;
                  }

                  public float getAttackDamageBonus() {
                      return 3f;
                  }

                  public int getLevel() {
                      return 1;
                  }

                  public int getEnchantmentValue() {
                      return 20;
                  }

                  public @NotNull Ingredient getRepairIngredient() {
                      return Ingredient.of();
                  }
              }, 3, -2.4f, new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
        );
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemstack, @NotNull LivingEntity entity, @NotNull LivingEntity sourceentity) {
        boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
        AddTransfurProgressProcedure.addRed(entity, 3);
        return retval;
    }
}
