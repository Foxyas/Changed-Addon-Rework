/*
  
package net.foxyas.changedaddon.extension;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import net.minecraftforge.fml.common.Mod;


public class JeiSuport {  


    @JeiPlugin
    class ChangedAddonModJeiPlugin implements IModPlugin {
		public static mezz.jei.api.recipe.RecipeType<JeiCatalyzerRecipe> JeiCatalyzer_Type = new mezz.jei.api.recipe.RecipeType<>(JeiCatalyzerRecipeCategory.UID, JeiCatalyzerRecipe.class);
        public static mezz.jei.api.recipe.RecipeType<JeiUnifuserRecipe> JeiUnifuser_Type = new mezz.jei.api.recipe.RecipeType<>(JeiUnifuserRecipeCategory.UID, JeiUnifuserRecipe.class);

		
       
        @Override
        public ResourceLocation getPluginUid() {
            return new ResourceLocation("changed_addon:jei_plugin");
        }

        @Override
        public void registerCategories(IRecipeCategoryRegistration registration) {
            registration.addRecipeCategories(new JeiCatalyzerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
            registration.addRecipeCategories(new JeiUnifuserRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        }

        @Override
        public void registerRecipes(IRecipeRegistration registration) {
            RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
            List<JeiCatalyzerRecipe> JeiCatalyzerRecipes = recipeManager.getAllRecipesFor(JeiSuport.JeiCatalyzerRecipe.Type.INSTANCE);
            registration.addRecipes(JeiCatalyzer_Type, JeiCatalyzerRecipes);
            List<JeiUnifuserRecipe> JeiUnifuserRecipes = recipeManager.getAllRecipesFor(JeiSuport.JeiUnifuserRecipe.Type.INSTANCE);
            registration.addRecipes(JeiUnifuser_Type, JeiUnifuserRecipes);
        }

        @Override
        public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
            registration.addRecipeCatalyst(new ItemStack(ChangedAddonModBlocks.CATLYZER.get().asItem()), JeiCatalyzer_Type);
            registration.addRecipeCatalyst(new ItemStack(ChangedAddonModBlocks.UNIFUSER.get().asItem()), JeiUnifuser_Type);
        }
    }

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    class ChangedAddonModRecipeTypes {
        public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "changed_addon");

        @SubscribeEvent
        public static void register(FMLConstructModEvent event) {
            IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
            event.enqueueWork(() -> {
                SERIALIZERS.register(bus);
                SERIALIZERS.register("jei_catalyzer", () -> JeiCatalyzerRecipe.Serializer.INSTANCE);
                SERIALIZERS.register("jei_unifuser", () -> JeiUnifuserRecipe.Serializer.INSTANCE);
            });
      	  }
    	}

    @Deprecated
    class JeiCatalyzerRecipeCategory implements IRecipeCategory<JeiCatalyzerRecipe> {
        public final static ResourceLocation UID = new ResourceLocation("changed_addon", "jei_catalyzer");
        public final static ResourceLocation TEXTURE = new ResourceLocation("changed_addon", "textures/screens/catalyzer_screen.png");
        private final IDrawable background;
        private final IDrawable icon;

        public JeiCatalyzerRecipeCategory(IGuiHelper helper) {
            this.background = helper.createDrawable(TEXTURE, 0, 0, 200, 86);
            this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChangedAddonModBlocks.CATLYZER.get().asItem()));
        }

        @Override
        public mezz.jei.api.recipe.RecipeType<JeiCatalyzerRecipe> getRecipeType() {
            return net.foxyas.changedaddon.extension.JeiSuport.ChangedAddonModJeiPlugin.JeiCatalyzer_Type;
        }

        @Override
        public Component getTitle() {
            return new TextComponent("Jei Catalyzer");
        }

        @Override
        public IDrawable getBackground() {
            return this.background;
        }

        @Override
        public IDrawable getIcon() {
            return this.icon;
        }

        @Deprecated
        @Override
        public Class<? extends JeiCatalyzerRecipe> getRecipeClass() {
            return JeiCatalyzerRecipe.class;
        }

        @Override
        public ResourceLocation getUid() {
            return UID;
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, JeiCatalyzerRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 23, 44).addIngredients(recipe.getIngredients().get(0));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 153, 44).addItemStack(recipe.getResultItem());
        }
    }

    static class JeiCatalyzerRecipe implements Recipe<SimpleContainer> {
        private final ResourceLocation id;
        private final ItemStack output;
        private final NonNullList<Ingredient> recipeItems;

        public JeiCatalyzerRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
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
            if (ingredient.test(pContainer.getItem(1))) {
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
            return net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe.Type.INSTANCE;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe.Serializer.INSTANCE;
        }

        public static class Type implements RecipeType<net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe> {
            private Type() {
            }

            public static final net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe.Type INSTANCE = new net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe.Type();
            public static final String ID = "jei_catalyzer";
        }

        public static class Serializer implements RecipeSerializer<net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe>, IForgeRegistryEntry<RecipeSerializer<?>> {
            public static final net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe.Serializer INSTANCE = new net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe.Serializer();
            public static final ResourceLocation ID = new ResourceLocation("changed_addon", "jei_catalyzer");

            @Override
            public net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
                ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
                JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
                NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);
                for (int i = 0; i < inputs.size(); i++) {
                    inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
                }
                return new net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe(pRecipeId, output, inputs);
            }

            @Override
            public @Nullable net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
                NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
                for (int i = 0; i < inputs.size(); i++) {
                    inputs.set(i, Ingredient.fromNetwork(buf));
                }
                ItemStack output = buf.readItem();
                return new net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe(id, output, inputs);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, net.foxyas.changedaddon.extension.JeiSuport.JeiCatalyzerRecipe recipe) {
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

    @Deprecated
    class JeiUnifuserRecipeCategory implements IRecipeCategory<JeiUnifuserRecipe> {
        public final static ResourceLocation UID = new ResourceLocation("changed_addon", "jei_unifuser");
        public final static ResourceLocation TEXTURE = new ResourceLocation("changed_addon", "textures/screens/unifuserscreen.png");
        private final IDrawable background;
        private final IDrawable icon;

        public JeiUnifuserRecipeCategory(IGuiHelper helper) {
            this.background = helper.createDrawable(TEXTURE, 0, 0, 200, 104);
            this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChangedAddonModBlocks.UNIFUSER.get().asItem()));
        }

        @Override
        public mezz.jei.api.recipe.RecipeType<JeiUnifuserRecipe> getRecipeType() {
            return net.foxyas.changedaddon.extension.JeiSuport.ChangedAddonModJeiPlugin.JeiUnifuser_Type;
        }

        @Override
        public Component getTitle() {
            return new TextComponent("Test");
        }

        @Override
        public IDrawable getBackground() {
            return this.background;
        }

        @Override
        public IDrawable getIcon() {
            return this.icon;
        }

        @Deprecated
        @Override
        public Class<? extends JeiUnifuserRecipe> getRecipeClass() {
            return JeiUnifuserRecipe.class;
        }

        @Override
        public ResourceLocation getUid() {
            return UID;
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, JeiUnifuserRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 15, 45).addIngredients(recipe.getIngredients().get(0));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 155, 57).addItemStack(recipe.getResultItem());
            builder.addSlot(RecipeIngredientRole.INPUT, 50, 57).addIngredients(recipe.getIngredients().get(2));
            builder.addSlot(RecipeIngredientRole.INPUT, 15, 70).addIngredients(recipe.getIngredients().get(1));
        }
    }

    static class JeiUnifuserRecipe implements Recipe<SimpleContainer> {
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
            if (ingredient.test(pContainer.getItem(1))) {
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
            return net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe.Type.INSTANCE;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe.Serializer.INSTANCE;
        }

        public static class Type implements RecipeType<net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe> {
            private Type() {
            }

            public static final net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe.Type INSTANCE = new net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe.Type();
            public static final String ID = "jei_unifuser";
        }

        public static class Serializer implements RecipeSerializer<net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe>, IForgeRegistryEntry<RecipeSerializer<?>> {
            public static final net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe.Serializer INSTANCE = new net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe.Serializer();
            public static final ResourceLocation ID = new ResourceLocation("changed_addon", "jei_unifuser");

            @Override
            public net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
                ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
                JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
                NonNullList<Ingredient> inputs = NonNullList.withSize(4, Ingredient.EMPTY);
                for (int i = 0; i < inputs.size(); i++) {
                    inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
                }
                return new net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe(pRecipeId, output, inputs);
            }

            @Override
            public @Nullable net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
                NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
                for (int i = 0; i < inputs.size(); i++) {
                    inputs.set(i, Ingredient.fromNetwork(buf));
                }
                ItemStack output = buf.readItem();
                return new net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe(id, output, inputs);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, net.foxyas.changedaddon.extension.JeiSuport.JeiUnifuserRecipe recipe) {
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
}
*/