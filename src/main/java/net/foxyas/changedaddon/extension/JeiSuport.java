package net.foxyas.changedaddon.extension;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
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
import net.minecraft.network.chat.TranslatableComponent;
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



 
   @JeiPlugin
	public class JeiSuport implements IModPlugin {
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
            List<JeiCatalyzerRecipe> JeiCatalyzerRecipes = recipeManager.getAllRecipesFor(JeiCatalyzerRecipe.Type.INSTANCE);
            registration.addRecipes(JeiCatalyzer_Type, JeiCatalyzerRecipes);
            List<JeiUnifuserRecipe> JeiUnifuserRecipes = recipeManager.getAllRecipesFor(JeiUnifuserRecipe.Type.INSTANCE);
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
        public final static ResourceLocation TEXTURE = new ResourceLocation("changed_addon", "textures/screens/jei_catalyzer_screen.png");
        private final IDrawable background;
        private final IDrawable icon;

        public JeiCatalyzerRecipeCategory(IGuiHelper helper) {
            this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 54);
            this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChangedAddonModBlocks.CATLYZER.get().asItem()));
        }

        @Override
        public mezz.jei.api.recipe.RecipeType<JeiCatalyzerRecipe> getRecipeType() {
            return JeiSuport.JeiCatalyzer_Type;
        }

        @Override
        public Component getTitle() {
            return new TextComponent((new TranslatableComponent("block.changed_addon.catlyzer").getString()));
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
            builder.addSlot(RecipeIngredientRole.INPUT, 12, 18).addIngredients(recipe.getIngredients().get(0));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 18).addItemStack(recipe.getResultItem());
        }
    }

    class JeiCatalyzerRecipe implements Recipe<SimpleContainer> {
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
     	   // Verifica se o contêiner possui pelo menos dois slots
         if (pContainer.getContainerSize() > 1) {
            // Percorre todos os itens da lista de ingredientes
            for (Ingredient ingredient : recipeItems) {
                // Verifica se pelo menos um item da lista atende às condições
                if (ingredient.test(pContainer.getItem(1))) {
       	             return true;
          	      }
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
            return net.foxyas.changedaddon.extension.JeiCatalyzerRecipe.Type.INSTANCE;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return net.foxyas.changedaddon.extension.JeiCatalyzerRecipe.Serializer.INSTANCE;
        }

        public static class Type implements RecipeType<net.foxyas.changedaddon.extension.JeiCatalyzerRecipe> {
            private Type() {
            }

            public static final net.foxyas.changedaddon.extension.JeiCatalyzerRecipe.Type INSTANCE = new net.foxyas.changedaddon.extension.JeiCatalyzerRecipe.Type();
            public static final String ID = "jei_catalyzer";
        }

        public static class Serializer implements RecipeSerializer<net.foxyas.changedaddon.extension.JeiCatalyzerRecipe>, IForgeRegistryEntry<RecipeSerializer<?>> {
            public static final net.foxyas.changedaddon.extension.JeiCatalyzerRecipe.Serializer INSTANCE = new net.foxyas.changedaddon.extension.JeiCatalyzerRecipe.Serializer();
            public static final ResourceLocation ID = new ResourceLocation("changed_addon", "jei_catalyzer");

            @Override
            public net.foxyas.changedaddon.extension.JeiCatalyzerRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
                ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
                JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
                NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
                inputs.set(0, Ingredient.fromJson(ingredients.get(0)));

                return new net.foxyas.changedaddon.extension.JeiCatalyzerRecipe(pRecipeId, output, inputs);
            }

            @Override
            public @Nullable net.foxyas.changedaddon.extension.JeiCatalyzerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
                NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
                for (int i = 0; i < inputs.size(); i++) {
                    inputs.set(i, Ingredient.fromNetwork(buf));
                }
                ItemStack output = buf.readItem();
                return new net.foxyas.changedaddon.extension.JeiCatalyzerRecipe(id, output, inputs);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, net.foxyas.changedaddon.extension.JeiCatalyzerRecipe recipe) {
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
        public final static ResourceLocation TEXTURE = new ResourceLocation("changed_addon", "textures/screens/jei_unifuser_screen.png");
        private final IDrawable background;
        private final IDrawable icon;

        public JeiUnifuserRecipeCategory(IGuiHelper helper) {
            this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 54);
            this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChangedAddonModBlocks.UNIFUSER.get().asItem()));
        }

        @Override
        public mezz.jei.api.recipe.RecipeType<JeiUnifuserRecipe> getRecipeType() {
            return JeiSuport.JeiUnifuser_Type;
        }

        @Override
        public Component getTitle() {
            return new TextComponent((new TranslatableComponent("block.changed_addon.unifuser").getString()));
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
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 18).addItemStack(recipe.getResultItem());
            builder.addSlot(RecipeIngredientRole.INPUT, 37, 18).addIngredients(recipe.getIngredients().get(2));
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 36).addIngredients(recipe.getIngredients().get(1));
        }
    }

    class JeiUnifuserRecipe implements Recipe<SimpleContainer> {
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
            return net.foxyas.changedaddon.extension.JeiUnifuserRecipe.Type.INSTANCE;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return net.foxyas.changedaddon.extension.JeiUnifuserRecipe.Serializer.INSTANCE;
        }

        public static class Type implements RecipeType<net.foxyas.changedaddon.extension.JeiUnifuserRecipe> {
            private Type() {
            }

            public static final net.foxyas.changedaddon.extension.JeiUnifuserRecipe.Type INSTANCE = new net.foxyas.changedaddon.extension.JeiUnifuserRecipe.Type();
            public static final String ID = "jei_unifuser";
        }

        public static class Serializer implements RecipeSerializer<net.foxyas.changedaddon.extension.JeiUnifuserRecipe>, IForgeRegistryEntry<RecipeSerializer<?>> {
   		public static final net.foxyas.changedaddon.extension.JeiUnifuserRecipe.Serializer INSTANCE = new net.foxyas.changedaddon.extension.JeiUnifuserRecipe.Serializer();
   		public static final ResourceLocation ID = new ResourceLocation("changed_addon", "jei_unifuser");

   		@Override
    	public net.foxyas.changedaddon.extension.JeiUnifuserRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
      	  ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
     	   JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
      	  NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

     	   for (int i = 0; i < ingredients.size(); i++) {
      	      JsonElement ingredientElement = ingredients.get(i);
     	       Ingredient ingredient = Ingredient.fromJson(ingredientElement);
        	    inputs.set(i, ingredient);
       			 }

      		  return new net.foxyas.changedaddon.extension.JeiUnifuserRecipe(pRecipeId, output, inputs);
   			 }
		


            @Override
            public @Nullable net.foxyas.changedaddon.extension.JeiUnifuserRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
                NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
                for (int i = 0; i < inputs.size(); i++) {
                    inputs.set(i, Ingredient.fromNetwork(buf));
                }
                ItemStack output = buf.readItem();
                return new net.foxyas.changedaddon.extension.JeiUnifuserRecipe(id, output, inputs);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, net.foxyas.changedaddon.extension.JeiUnifuserRecipe recipe) {
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
