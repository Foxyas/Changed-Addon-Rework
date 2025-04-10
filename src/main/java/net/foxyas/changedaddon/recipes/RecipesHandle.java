package net.foxyas.changedaddon.recipes;

import net.foxyas.changedaddon.block.AdvancedUnifuserBlock;
import net.foxyas.changedaddon.block.AdvancedCatalyzerBlock;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class RecipesHandle {

    public static boolean CheckUnifuserRecipe(LevelAccessor level, ItemStack input1, ItemStack input2, ItemStack input3) {
        if (level instanceof ServerLevel serverLevel) {
            RecipeManager recipeManager = serverLevel.getRecipeManager();

            // Obtém todas as receitas do tipo JeiUnifuserRecipe
            List<UnifuserRecipe> unifuserRecipes = recipeManager.getAllRecipesFor(UnifuserRecipe.Type.INSTANCE);

            // Cria um contêiner simples com os inputs fornecidos
            SimpleContainer container = new SimpleContainer(3);
            container.setItem(0, input1);
            container.setItem(1, input2);
            container.setItem(2, input3);

            // Verifica cada receita para ver se ela corresponde aos inputs fornecidos
            for (UnifuserRecipe recipe : unifuserRecipes) {
                if (recipe.matches(container, serverLevel)) {
                    return true; // Receita correspondente encontrada
                }
            }
        }
        return false; // Nenhuma receita correspondente encontrada
    }

    public static boolean CheckCatalyzerRecipe(LevelAccessor level, ItemStack input) {
        if (level instanceof ServerLevel serverLevel) {
            RecipeManager recipeManager = serverLevel.getRecipeManager();

            // Obtém todas as receitas do tipo JeiCatalyzerRecipe
            List<CatalyzerRecipe> catalyzerRecipes = recipeManager.getAllRecipesFor(CatalyzerRecipe.Type.INSTANCE);

            // Cria um contêiner simples com o input fornecido
            SimpleContainer container = new SimpleContainer(1);
            container.setItem(0, input);

            // Verifica cada receita para ver se ela corresponde ao input fornecido
            for (CatalyzerRecipe recipe : catalyzerRecipes) {
                if (recipe.matches(container, serverLevel)) {
                    return true; // Receita correspondente encontrada
                }
            }
        }
        return false; // Nenhuma receita correspondente encontrada
    }

    public static ItemStack getUnifuserRecipeOutputOrDefault(LevelAccessor level, ItemStack input1, ItemStack input2, ItemStack input3) {
        if (level instanceof ServerLevel serverLevel) {
            RecipeManager recipeManager = serverLevel.getRecipeManager();

            // Obtém todas as receitas do tipo JeiUnifuserRecipe
            List<UnifuserRecipe> unifuserRecipes = recipeManager.getAllRecipesFor(UnifuserRecipe.Type.INSTANCE);

            // Cria um contêiner simples com os inputs fornecidos
            SimpleContainer container = new SimpleContainer(3);
            container.setItem(0, input1);
            container.setItem(1, input2);
            container.setItem(2, input3);

            // Verifica cada receita para ver se ela corresponde aos inputs fornecidos
            for (UnifuserRecipe recipe : unifuserRecipes) {
                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                if (!ingredients.get(0).test(input1))
                    continue;
                if (!ingredients.get(1).test(input2))
                    continue;
                if (!ingredients.get(2).test(input3))
                    continue;
                return recipe.getResultItem(); // Retorna o ItemStack do resultado
            }
        }
        return ItemStack.EMPTY; // Retorna um ItemStack vazio se nenhuma receita correspondente for encontrada
    }

    public static ItemStack getCatalyzerRecipeOutputOrDefault(LevelAccessor level, ItemStack input) {
        if (level instanceof ServerLevel serverLevel) {
            RecipeManager recipeManager = serverLevel.getRecipeManager();

            // Obtém todas as receitas do tipo JeiCatalyzerRecipe
            List<CatalyzerRecipe> catalyzerRecipes = recipeManager.getAllRecipesFor(CatalyzerRecipe.Type.INSTANCE);

            // Cria um contêiner simples com o input fornecido
            SimpleContainer container = new SimpleContainer(1);
            container.setItem(0, input);

            // Verifica cada receita para ver se ela corresponde ao input fornecido
            for (CatalyzerRecipe recipe : catalyzerRecipes) {
                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                if (!ingredients.get(0).test(input))
                    continue;
                return recipe.getResultItem();
            }
        }
        return ItemStack.EMPTY; // Retorna um ItemStack vazio se nenhuma receita correspondente for encontrada
    }

    public static float getUnifuserRecipeProgressSpeed(LevelAccessor level, BlockState blockState, ItemStack input1, ItemStack input2, ItemStack input3) {
        if (level instanceof ServerLevel serverLevel) {
            float multiplier = blockState.getBlock() instanceof AdvancedUnifuserBlock ? 4f : 1f;
            RecipeManager recipeManager = serverLevel.getRecipeManager();

            // Obtém todas as receitas do tipo JeiUnifuserRecipe
            List<UnifuserRecipe> unifuserRecipes = recipeManager.getAllRecipesFor(UnifuserRecipe.Type.INSTANCE);

            // Cria um contêiner simples com os inputs fornecidos
            SimpleContainer container = new SimpleContainer(3);
            container.setItem(0, input1);
            container.setItem(1, input2);
            container.setItem(2, input3);

            // Verifica cada receita para ver se ela corresponde aos inputs fornecidos
            for (UnifuserRecipe recipe : unifuserRecipes) {
                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                if (!ingredients.get(0).test(input1))
                    continue;
                if (!ingredients.get(1).test(input2))
                    continue;
                if (!ingredients.get(2).test(input3))
                    continue;
                return recipe.getProgressSpeed() * multiplier; // Retorna o ItemStack do resultado
            }
        }
        return 1f; // Retorna um ItemStack vazio se nenhuma receita correspondente for encontrada
    }

    public static float getCatalyzerRecipeProgressSpeed(LevelAccessor level, BlockState blockState, ItemStack input) {
        if (level instanceof ServerLevel serverLevel) {
            float multiplier = blockState.getBlock() instanceof AdvancedCatalyzerBlock ? 4f : 1f;
            RecipeManager recipeManager = serverLevel.getRecipeManager();

            // Obtém todas as receitas do tipo JeiCatalyzerRecipe
            List<CatalyzerRecipe> catalyzerRecipes = recipeManager.getAllRecipesFor(CatalyzerRecipe.Type.INSTANCE);

            // Cria um contêiner simples com o input fornecido
            SimpleContainer container = new SimpleContainer(1);
            container.setItem(0, input);

            // Verifica cada receita para ver se ela corresponde ao input fornecido
            for (CatalyzerRecipe recipe : catalyzerRecipes) {
                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                if (!ingredients.get(0).test(input))
                    continue;
                return recipe.getProgressSpeed() * multiplier;
            }
        }
        return 1f; // Retorna um ItemStack vazio se nenhuma receita correspondente for encontrada
    }

    public static float getUnifuserRecipeProgressSpeed(LevelAccessor level, ItemStack input1, ItemStack input2, ItemStack input3) {
        if (level instanceof ServerLevel serverLevel) {
            RecipeManager recipeManager = serverLevel.getRecipeManager();

            // Obtém todas as receitas do tipo JeiUnifuserRecipe
            List<UnifuserRecipe> unifuserRecipes = recipeManager.getAllRecipesFor(UnifuserRecipe.Type.INSTANCE);

            // Cria um contêiner simples com os inputs fornecidos
            SimpleContainer container = new SimpleContainer(3);
            container.setItem(0, input1);
            container.setItem(1, input2);
            container.setItem(2, input3);

            // Verifica cada receita para ver se ela corresponde aos inputs fornecidos
            for (UnifuserRecipe recipe : unifuserRecipes) {
                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                if (!ingredients.get(0).test(input1))
                    continue;
                if (!ingredients.get(1).test(input2))
                    continue;
                if (!ingredients.get(2).test(input3))
                    continue;
                return recipe.getProgressSpeed(); // Retorna o ItemStack do resultado
            }
        }
        return 1f; // Retorna um ItemStack vazio se nenhuma receita correspondente for encontrada
    }

    public static float getCatalyzerRecipeProgressSpeed(LevelAccessor level, ItemStack input) {
        if (level instanceof ServerLevel serverLevel) {
            RecipeManager recipeManager = serverLevel.getRecipeManager();

            // Obtém todas as receitas do tipo JeiCatalyzerRecipe
            List<CatalyzerRecipe> catalyzerRecipes = recipeManager.getAllRecipesFor(CatalyzerRecipe.Type.INSTANCE);

            // Cria um contêiner simples com o input fornecido
            SimpleContainer container = new SimpleContainer(1);
            container.setItem(0, input);

            // Verifica cada receita para ver se ela corresponde ao input fornecido
            for (CatalyzerRecipe recipe : catalyzerRecipes) {
                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                if (!ingredients.get(0).test(input))
                    continue;
                return recipe.getProgressSpeed();
            }
        }
        return 1f; // Retorna um ItemStack vazio se nenhuma receita correspondente for encontrada
    }

    public static float getCatalyzerRecipeNitrogenUsage(LevelAccessor level, ItemStack input) {
        if (level instanceof ServerLevel serverLevel) {
            RecipeManager recipeManager = serverLevel.getRecipeManager();

            // Obtém todas as receitas do tipo JeiCatalyzerRecipe
            List<CatalyzerRecipe> catalyzerRecipes = recipeManager.getAllRecipesFor(CatalyzerRecipe.Type.INSTANCE);

            // Cria um contêiner simples com o input fornecido
            SimpleContainer container = new SimpleContainer(1);
            container.setItem(0, input);

            // Verifica cada receita para ver se ela corresponde ao input fornecido
            for (CatalyzerRecipe recipe : catalyzerRecipes) {
                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                if (!ingredients.get(0).test(input))
                    continue;
                return recipe.getNitrogenUsage();
            }
        }
        return 0f; // Retorna um ItemStack vazio se nenhuma receita correspondente for encontrada
    }
}
