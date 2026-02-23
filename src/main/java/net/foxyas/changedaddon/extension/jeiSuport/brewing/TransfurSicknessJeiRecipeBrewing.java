package net.foxyas.changedaddon.extension.jeiSuport.brewing;

import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonPotions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;


public class TransfurSicknessJeiRecipeBrewing implements IJeiBrewingRecipe {

    private final ItemLike input;

    public TransfurSicknessJeiRecipeBrewing(ItemLike inputLike) {
        this.input = inputLike;
    }

    public static TransfurSicknessJeiRecipeBrewing[] getAllRecipes() {
        return new TransfurSicknessJeiRecipeBrewing[]{
                new TransfurSicknessJeiRecipeBrewing(Items.POTION),
                new TransfurSicknessJeiRecipeBrewing(Items.SPLASH_POTION),
                new TransfurSicknessJeiRecipeBrewing(Items.LINGERING_POTION)
        };
    }

    @Override
    public @Unmodifiable @NotNull List<ItemStack> getPotionInputs() {
        ItemStack[] itemStacks;

        if (input == null) {
            ItemStack potion = new ItemStack(Items.POTION);
            ItemStack splash_potion = new ItemStack(Items.SPLASH_POTION);
            ItemStack lingering_potion = new ItemStack(Items.LINGERING_POTION);
            itemStacks = new ItemStack[]{potion, splash_potion, lingering_potion};
        } else {
            itemStacks = new ItemStack[]{new ItemStack(input)};
        }

        for (ItemStack stack : itemStacks) {
            PotionUtils.setPotion(stack, Potions.AWKWARD);
        }


        return List.of(itemStacks);
    }

    @Override
    public @Unmodifiable @NotNull List<ItemStack> getIngredients() {
        return List.of(new ItemStack(ChangedAddonItems.LAETHIN.get()));
    }

    @Override
    public @NotNull ItemStack getPotionOutput() {
        ItemStack potion;
        potion = new ItemStack(Objects.requireNonNullElse(input, Items.POTION));

        return PotionUtils.setPotion(potion, ChangedAddonPotions.TRANSFUR_SICKNESS_POTION.get());
    }

    @Override
    public int getBrewingSteps() {
        return 2;
    }
}
