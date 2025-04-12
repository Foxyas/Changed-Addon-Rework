package net.foxyas.changedaddon.recipes.special;

import com.google.gson.JsonObject;
import net.foxyas.changedaddon.recipes.ChangedAddonModRecipeTypes;
import net.foxyas.changedaddon.registers.ChangedAddonRegisters;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class ColofulShortsColoringRecipe extends CustomRecipe {
    public ColofulShortsColoringRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        boolean hasShorts = false;
        boolean hasDye = false;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                if (item == ChangedAddonRegisters.COLORFUL_SHORTS.get()) {
                    if (hasShorts) return false;
                    hasShorts = true;
                } else if (item instanceof DyeItem) {
                    hasDye = true;
                } else {
                    return false;
                }
            }
        }

        return hasShorts && hasDye;
    }

    @Override
    public ItemStack assemble(CraftingContainer container) {
        ItemStack pointer = ItemStack.EMPTY;

        int totalR = 0;
        int totalG = 0;
        int totalB = 0;
        int dyeCount = 0;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                if (item == ChangedAddonRegisters.COLORFUL_SHORTS.get()) {
                    pointer = stack;
                } else if (item instanceof DyeItem dyeItem) {
                    int color = dyeItem.getDyeColor().getTextColor(); // 0xRRGGBB
                    totalR += (color >> 16) & 0xFF;
                    totalG += (color >> 8) & 0xFF;
                    totalB += color & 0xFF;
                    dyeCount++;
                }
            }
        }

        if (!pointer.isEmpty() && dyeCount > 0) {
            int r = totalR / dyeCount;
            int g = totalG / dyeCount;
            int b = totalB / dyeCount;
            int finalColor = (r << 16) | (g << 8) | b;

            ItemStack result = pointer.copy();
            result.setCount(1);
            if (result.getItem() instanceof DyeableLeatherItem dyeableLeatherItem){
                dyeableLeatherItem.setColor(result, finalColor);
            }
            return result;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ChangedAddonModRecipeTypes.SHORTS_COLORING.get();
    }

    public static class Serializer implements RecipeSerializer<ColofulShortsColoringRecipe> {

        public static final ResourceLocation ID = new ResourceLocation("changed_addon", "coloring_shorts");

        @Override
        public ColofulShortsColoringRecipe fromJson(ResourceLocation id, JsonObject json) {
            // Nenhum dado necessário no JSON
            return new ColofulShortsColoringRecipe(id);
        }

        @Override
        public ColofulShortsColoringRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            // Nenhum dado transmitido, então só retorna a instância
            return new ColofulShortsColoringRecipe(id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ColofulShortsColoringRecipe recipe) {
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
