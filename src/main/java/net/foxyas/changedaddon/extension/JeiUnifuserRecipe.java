package net.foxyas.changedaddon.extension;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class JeiUnifuserRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public JeiUnifuserRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) {
            return false;
        }

        // Verifica se a lista de ingredientes não está vazia
        if (!recipeItems.isEmpty()) {
            // Percorre todos os itens da lista de ingredientes
            for (Ingredient ingredient : recipeItems) {
                // Verifica se pelo menos um item da lista atende às condições
                if (ingredient.test(pContainer.getItem(3))) {
                    return true;
                }
            }
        }

        return false; // Retorna false se a lista de ingredientes estiver vazia ou nenhum item atender às condições
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Type implements RecipeType<JeiUnifuserRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "jei_unifuser";
    }

    public static class Serializer implements RecipeSerializer<JeiUnifuserRecipe>, IForgeRegistryEntry<RecipeSerializer<?>> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation("changed_addon", "jei_unifuser");

        @Override
        public JeiUnifuserRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {
                JsonElement ingredientElement = ingredients.get(i);
                Ingredient ingredient = Ingredient.fromJson(ingredientElement);
                inputs.set(i, ingredient);
            }

            return new JeiUnifuserRecipe(pRecipeId, output, inputs);
        }


        @Override
        public @Nullable JeiUnifuserRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }
            ItemStack output = buf.readItem();
            return new JeiUnifuserRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, JeiUnifuserRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
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
