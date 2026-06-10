package net.foxyas.changedaddon.extension.jei.brewing;

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

public class UntransfurPotionJeiRecipeBrewing implements IJeiBrewingRecipe {

    private final ItemLike input;

    public UntransfurPotionJeiRecipeBrewing(ItemLike inputLike) {
        this.input = inputLike;
    }

    public static UntransfurPotionJeiRecipeBrewing[] getAllRecipes() {
        return new UntransfurPotionJeiRecipeBrewing[]{
                new UntransfurPotionJeiRecipeBrewing(Items.POTION),
                new UntransfurPotionJeiRecipeBrewing(Items.SPLASH_POTION),
                new UntransfurPotionJeiRecipeBrewing(Items.LINGERING_POTION)
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
        return List.of(new ItemStack(ChangedAddonItems.LITIX_CAMONIA.get()));
    }

    @Override
    public @NotNull ItemStack getPotionOutput() {
        ItemStack potion;
        potion = new ItemStack(Objects.requireNonNullElse(input, Items.POTION));

        return PotionUtils.setPotion(potion, ChangedAddonPotions.LITIX_CAMMONIA_EFFECT.get());
    }

    @Override
    public int getBrewingSteps() {
        return 2;
    }
}
