package net.foxyas.changedaddon.item;

import net.ltxprogrammer.changed.block.AbstractLabDoor;
import net.ltxprogrammer.changed.block.AbstractLargeLabDoor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CrowbarItem extends PickaxeItem {

    public CrowbarItem() {
        super(new Tier() {
                  public int getUses() {
                      return 150;
                  }

                  public float getSpeed() {
                      return 3.5f;
                  }

                  public float getAttackDamageBonus() {
                      return 5.7f;
                  }

                  public int getLevel() {
                      return 2;
                  }

                  public int getEnchantmentValue() {
                      return 40;
                  }

                  public @NotNull Ingredient getRepairIngredient() {
                      return Ingredient.of();
                  }
              }, 1, -2.6f, new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
        );
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack retval = new ItemStack(this);
        retval.setDamageValue(itemStack.getDamageValue() + 1);
        if (retval.getDamageValue() >= retval.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return retval;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isRepairable(@NotNull ItemStack itemstack) {
        return false;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        Level level = player.level;
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof AbstractLabDoor abstractLabDoor) {
            if (abstractLabDoor.openDoor(state, level, pos)) {
                player.getCooldowns().addCooldown(this, 60);
            }
            return InteractionResult.SUCCESS;
        }

        if (state.getBlock() instanceof AbstractLargeLabDoor abstractLargeLabDoor) {
            if (abstractLargeLabDoor.openDoor(state, level, pos)) {
                player.getCooldowns().addCooldown(this, 60);
            }
        }

        return InteractionResult.SUCCESS;
    }
}
