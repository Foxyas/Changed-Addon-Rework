package net.foxyas.changedaddon.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import org.jetbrains.annotations.NotNull;

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
    public boolean matches(@NotNull SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) {
            return false;
        }

        // Verifica se a lista de ingredientes não está vazia
        if (!recipeItems.isEmpty()) {
            return StrictNBTIngredient.of(this.output).test(pContainer.getItem(1));
        }

        return false; // Retorna false se a lista de ingredientes estiver vazia ou nenhum item atender às condições
    }

    public boolean isHidden() {
        String string = this.getId().getPath();
        return string.contains("_hidden") || string.contains("_secret");
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer pContainer, @NotNull RegistryAccess registryAccess) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return output.copy();
    }

    public float getProgressSpeed() {
        return ProgressSpeed;
    }

    public float getNitrogenUsage() {
        return NitrogenUsage;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Type implements RecipeType<CatalyzerRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "catalyzer";

        private Type() {
        }
    }

    public static class Serializer implements RecipeSerializer<CatalyzerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("changed_addon", "catalyzer");

        @Override
        public @NotNull CatalyzerRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            for (int i = 0; i < ingredients.size(); i++) {
                JsonElement ingredientElement = ingredients.get(i);
                Ingredient ingredient;
                if (ingredientElement.isJsonObject() && ingredientElement.getAsJsonObject().has("nbt")) {
                    JsonObject jsonObject = ingredientElement.getAsJsonObject();
                    ItemStack stack = ShapedRecipe.itemStackFromJson(ingredientElement.getAsJsonObject());
                    if (jsonObject.has("exactlyNbt")) {
                        boolean exactlyNbt = jsonObject.get("exactlyNbt").getAsBoolean();
                        if (exactlyNbt) {
                            ingredient = StrictNBTIngredient.of(stack);
                        } else {
                            ingredient = PartialNBTIngredient.of(stack.getItem(), stack.getOrCreateTag());
                        }
                    } else {
                        ingredient = StrictNBTIngredient.of(stack);
                    }

                    ChangedAddonMod.LOGGER.info("Parsing nbt recipe with id {} of type {}", pRecipeId, Type.ID);
                } else {
                    ingredient = Ingredient.fromJson(ingredientElement);
                }

                inputs.set(i, ingredient);
            }
            float ProgressSpeed = GsonHelper.getAsFloat(pSerializedRecipe, "ProgressSpeed", 1.0f);
            float NitrogenUsage = GsonHelper.getAsFloat(pSerializedRecipe, "NitrogenUsage", 0.0f);

            return new CatalyzerRecipe(pRecipeId, output, inputs, ProgressSpeed, NitrogenUsage);
        }

        @Override
        public @Nullable CatalyzerRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            inputs.replaceAll(ignored -> Ingredient.fromNetwork(buf));
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
            buf.writeItemStack(recipe.output, false);
            buf.writeFloat(recipe.getProgressSpeed());
            buf.writeFloat(recipe.getNitrogenUsage());
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
