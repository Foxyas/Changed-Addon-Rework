package net.foxyas.changedaddon.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.entity.UnifuserBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnifuserRecipe implements Recipe<UnifuserBlockEntity> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final float progressSpeed;
    private final float experience;

    // Novos campos para gerenciar a cópia de dados
    private final boolean copyAllNbt;
    private final List<String> specificNbtKeys;

    public UnifuserRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems,
                          float progressSpeed, float experience,
                          boolean copyAllNbt, List<String> specificNbtKeys) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.progressSpeed = progressSpeed;
        this.experience = experience;
        this.copyAllNbt = copyAllNbt;
        this.specificNbtKeys = specificNbtKeys;
    }

    public boolean isHidden() {
        String string = this.getId().getPath();
        return string.contains("_hidden") || string.contains("_secret");
    }

    public CompoundTag getTagOfIngredient(Ingredient ingredient) {
        return Arrays.stream(ingredient.getItems()).findAny().orElse(ItemStack.EMPTY).getOrCreateTag();
    }

    @Override
    public boolean matches(@NotNull UnifuserBlockEntity pContainer, Level pLevel) {
        if (pLevel.isClientSide()) {
            return false;
        }

        // Verifica se a lista de ingredientes não está vazia
        if (!recipeItems.isEmpty()) {
            return StrictNBTIngredient.of(this.output).test(pContainer.getItem(3));
        }

        return false; // Retorna false se a lista de ingredientes estiver vazia
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull UnifuserBlockEntity pContainer, @NotNull RegistryAccess registryAccess) {
        ItemStack resultStack = this.getResultItem(registryAccess);

        ItemStack mainSlotItem = pContainer.getItem(UnifuserBlockEntity.MAIN_SLOT);

        if (!mainSlotItem.isEmpty() && mainSlotItem.hasTag()) {
            CompoundTag sourceTag = mainSlotItem.getTag();
            if (sourceTag != null) {
                if (copyAllNbt) {
                    resultStack.setTag(sourceTag.copy());
                } else if (!specificNbtKeys.isEmpty()) {
                    CompoundTag resultTag = resultStack.getOrCreateTag();
                    for (String key : specificNbtKeys) {
                        if (sourceTag.contains(key)) {
                            resultTag.put(key, sourceTag.get(key).copy());
                        }
                    }
                }
            }
        }

        return resultStack;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public @NotNull ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return output.copy();
    }

    public float getProgressSpeed() {
        return progressSpeed;
    }

    public float getExperience() {
        return experience;
    }

    public boolean isCopyAllNbt() {
        return copyAllNbt;
    }

    public List<String> getSpecificNbtKeys() {
        return specificNbtKeys;
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

    public static class Type implements RecipeType<UnifuserRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "unifuser";

        private Type() {
        }
    }

    public static class Serializer implements RecipeSerializer<UnifuserRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("changed_addon", "unifuser");

        @Override
        public @NotNull UnifuserRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
            JsonObject outputObj = GsonHelper.getAsJsonObject(pSerializedRecipe, "output");
            ItemStack output = ShapedRecipe.itemStackFromJson(outputObj);

            boolean copyAllNbt = false;
            List<String> specificNbtKeys = new ArrayList<>();

            if (outputObj.has("preserveData")) {
                JsonElement preserveDataElement = outputObj.get("preserveData");
                if (preserveDataElement.isJsonPrimitive() && preserveDataElement.getAsJsonPrimitive().isBoolean()) {
                    copyAllNbt = preserveDataElement.getAsBoolean();
                } else if (preserveDataElement.isJsonArray()) {
                    JsonArray jsonArray = preserveDataElement.getAsJsonArray();
                    for (JsonElement element : jsonArray) {
                        if (element.isJsonPrimitive()) {
                            specificNbtKeys.add(element.getAsString());
                        }
                    }
                }
            }

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {
                JsonElement ingredientElement = ingredients.get(i);

                Ingredient ingredient;

                if (ingredientElement.isJsonObject() && ingredientElement.getAsJsonObject().has("nbt")) {
                    JsonObject jsonObject = ingredientElement.getAsJsonObject();
                    ItemStack stack = ShapedRecipe.itemStackFromJson(jsonObject);
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

                    ChangedAddonMod.LOGGER.info("[Changed Addon Recipes Types] Parsing nbt recipe with id {} of type {}", pRecipeId, Type.ID);
                } else {
                    ingredient = Ingredient.fromJson(ingredientElement);
                }

                inputs.set(i, ingredient);
            }

            float progressSpeed = GsonHelper.getAsFloat(pSerializedRecipe, "progressSpeed", 1.0f);
            float experience = GsonHelper.getAsFloat(pSerializedRecipe, "experience", 0f);

            return new UnifuserRecipe(pRecipeId, output, inputs, progressSpeed, experience, copyAllNbt, specificNbtKeys);
        }

        @Override
        public @Nullable UnifuserRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            inputs.replaceAll(ignored -> Ingredient.fromNetwork(buf));
            ItemStack output = buf.readItem();
            float progressSpeed = buf.readFloat();
            float experience = buf.readFloat();

            // Leitura dos dados de preservação enviados pela rede
            boolean copyAllNbt = buf.readBoolean();
            int keySize = buf.readInt();
            List<String> specificNbtKeys = new ArrayList<>();
            for (int i = 0; i < keySize; i++) {
                specificNbtKeys.add(buf.readUtf());
            }

            return new UnifuserRecipe(id, output, inputs, progressSpeed, experience, copyAllNbt, specificNbtKeys);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, UnifuserRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.output, false);
            buf.writeFloat(recipe.getProgressSpeed());
            buf.writeFloat(recipe.getExperience());

            // Escrita dos dados de preservação na rede
            buf.writeBoolean(recipe.isCopyAllNbt());
            buf.writeInt(recipe.getSpecificNbtKeys().size());
            for (String key : recipe.getSpecificNbtKeys()) {
                buf.writeUtf(key);
            }
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