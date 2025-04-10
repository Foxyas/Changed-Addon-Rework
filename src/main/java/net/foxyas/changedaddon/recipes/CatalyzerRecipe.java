package net.foxyas.changedaddon.recipes;

import com.google.gson.JsonArray;
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

public class CatalyzerRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final float ProgressSpeed;
    private final float NitrogenUsage;

    public CatalyzerRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, float ProgressSpeed, float NitrogenUsage) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.ProgressSpeed = ProgressSpeed;
        this.NitrogenUsage = NitrogenUsage;
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
                if (ingredient.test(pContainer.getItem(2))) {
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

    public float getProgressSpeed() {
        return ProgressSpeed;
    }

    public float getNitrogenUsage() {
        return NitrogenUsage;
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

    public static class Type implements RecipeType<CatalyzerRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "catalyzer";
    }

    public static class Serializer implements RecipeSerializer<CatalyzerRecipe>, IForgeRegistryEntry<RecipeSerializer<?>> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation("changed_addon", "catalyzer");

        @Override
        public CatalyzerRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            inputs.set(0, Ingredient.fromJson(ingredients.get(0)));
            float ProgressSpeed  = GsonHelper.getAsFloat(pSerializedRecipe, "ProgressSpeed", 1.0f);
            float NitrogenUsage  = GsonHelper.getAsFloat(pSerializedRecipe, "NitrogenUsage", 0.0f);

            return new CatalyzerRecipe(pRecipeId, output, inputs, ProgressSpeed, NitrogenUsage);
        }

        @Override
        public @Nullable CatalyzerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }
            ItemStack output = buf.readItem();
            float ProgressSpeed = buf.readFloat();
            float NitrogenUsage = buf.readFloat();
            return new CatalyzerRecipe(id, output, inputs, ProgressSpeed, NitrogenUsage);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CatalyzerRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
            buf.writeFloat(recipe.getProgressSpeed());
            buf.writeFloat(recipe.getNitrogenUsage());
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
