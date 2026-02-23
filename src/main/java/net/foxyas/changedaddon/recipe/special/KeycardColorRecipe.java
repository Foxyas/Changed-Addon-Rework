package net.foxyas.changedaddon.recipe.special;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonRecipeTypes;
import net.foxyas.changedaddon.item.KeycardItem;
import net.foxyas.changedaddon.util.ColorUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class KeycardColorRecipe extends CustomRecipe {

    public KeycardColorRecipe(ResourceLocation id) {
        super(id, CraftingBookCategory.MISC);
    }

    @Override
    public boolean matches(CraftingContainer container, @NotNull Level world) {
        boolean foundKeycard = false;

        boolean topDye = false;
        boolean midDye = false;
        boolean bottomDye = false;

        int w = container.getWidth();
        int h = container.getHeight();

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                ItemStack stack = container.getItem(row * w + col);

                if (stack.getItem() instanceof KeycardItem) {
                    if (foundKeycard) return false;//Cannot have multiple keycards!
                    foundKeycard = true;
                }

                if (stack.getItem() instanceof DyeItem) {
                    if (row == 0) topDye = true;
                    else if (row == h - 1) bottomDye = true;
                    else midDye = true;
                }
            }
        }

        // Válido: keycard + pelo menos um dye em qualquer linha
        return foundKeycard && (topDye || bottomDye || midDye);
    }


    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        ItemStack keycard = ItemStack.EMPTY;

        IntList topColors = new IntArrayList();
        IntList bottomColors = new IntArrayList();

        int w = container.getWidth();
        int h = container.getHeight();

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                ItemStack item = container.getItem(row * w + col);

                if (item.getItem() instanceof KeycardItem) {
                    keycard = item.copy();
                    if (KeycardItem.hasTopColor(keycard)) topColors.add(KeycardItem.getTopColor(keycard));
                    if (KeycardItem.hasBottomColor(keycard)) bottomColors.add(KeycardItem.getBottomColor(keycard));
                    continue;
                }

                if (item.getItem() instanceof DyeItem dye) {
                    DyeColor color = dye.getDyeColor();
                    int rgb = ColorUtil.dyeToARGB(color);

                    if (row == 0) {
                        topColors.add(rgb);
                    } else if (row == h - 1) {
                        bottomColors.add(rgb);
                    } else { // linha do meio → aplica nos dois
                        topColors.add(rgb);
                        bottomColors.add(rgb);
                    }
                }
            }
        }

        if (keycard.isEmpty()) return ItemStack.EMPTY;

        if (!topColors.isEmpty()) KeycardItem.setTopColor(keycard, ColorUtil.mixColors(topColors));
        if (!bottomColors.isEmpty()) KeycardItem.setBottomColor(keycard, ColorUtil.mixColors(bottomColors));

        return keycard;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return w * h >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ChangedAddonRecipeTypes.KEYCARD_COLOR.get();
    }

    public static class Serializer implements RecipeSerializer<KeycardColorRecipe> {

        public static final ResourceLocation ID = ChangedAddonMod.resourceLoc("keycard_coloring");

        @Override
        public @NotNull KeycardColorRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            // Nenhum dado necessário no JSON
            return new KeycardColorRecipe(id);
        }

        @Override
        public KeycardColorRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buffer) {
            // Nenhum dado transmitido, então só retorna a instância
            return new KeycardColorRecipe(id);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull KeycardColorRecipe recipe) {
            // Nada para escrever
        }

        public ResourceLocation getRegistryName() {
            return ID;
        }

        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return this;
        }

        public Class<RecipeSerializer<?>> getRegistryType() {
            return (Class<RecipeSerializer<?>>) (Class<?>) RecipeSerializer.class;
        }
    }
}
