package net.foxyas.changedaddon.recipes.special;

import com.google.gson.JsonObject;
import net.foxyas.changedaddon.recipes.ChangedAddonModRecipeTypes;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.item.Syringe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HaydenTransfurRecipe extends CustomRecipe {
    public HaydenTransfurRecipe(ResourceLocation p_43833_) {
        super(p_43833_);
    }

    @Override
    public boolean isSpecial() {
        return super.isSpecial();
    }

    @Override
    public boolean matches(CraftingContainer inv, @NotNull Level level) {
        // A C A
        // C # C
        // A C A

        ItemStack center = inv.getItem(4);
        if (!center.is(ChangedItems.LATEX_SYRINGE.get())) return false;
        TransfurVariant<?> variant = Syringe.getVariant(center);
        if (variant == null || !variant.is(ChangedTransfurVariants.LATEX_FENNEC_FOX.get())) return false;

        // Check surrounding
        int[] cookieSlots = {1, 3, 5, 7};
        for (int slot : cookieSlots) {
            if (!inv.getItem(slot).is(Items.COOKIE)) return false;
        }

        int[] airSlots = {0, 2, 6, 8};
        for (int slot : airSlots) {
            if (!inv.getItem(slot).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack center = inv.getItem(4);
        if (center.is(ChangedItems.LATEX_SYRINGE.get())) {
            TransfurVariant<?> variant = Syringe.getVariant(center);
            if (variant != null && variant.is(ChangedTransfurVariants.LATEX_FENNEC_FOX.get())) {
                ItemStack stack = center.copy();
                return Syringe.setVariant(stack, ChangedAddonTransfurVariants.HAYDEN_FENNEC_FOX.get().getFormId()); // Output
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ChangedAddonModRecipeTypes.HAYDEN_SYRINGE_RECIPE.get(); // Veja abaixo
    }

    public static class Serializer implements RecipeSerializer<HaydenTransfurRecipe> {

        public static final ResourceLocation ID = new ResourceLocation("changed_addon", "hayden_syringe_recipe");

        @Override
        public HaydenTransfurRecipe fromJson(ResourceLocation id, JsonObject json) {
            // Nenhum dado necessário no JSON
            return new HaydenTransfurRecipe(id);
        }

        @Override
        public HaydenTransfurRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            // Nenhum dado transmitido, então só retorna a instância
            return new HaydenTransfurRecipe(id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, HaydenTransfurRecipe recipe) {
            // Nada para escrever
        }

        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return this;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return (Class<RecipeSerializer<?>>) (Class<?>) RecipeSerializer.class;
        }
    }
}
