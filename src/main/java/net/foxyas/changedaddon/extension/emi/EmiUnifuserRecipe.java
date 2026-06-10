package net.foxyas.changedaddon.extension.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.recipe.UnifuserRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmiUnifuserRecipe implements EmiRecipe {

    private final UnifuserRecipe recipe;
    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public EmiUnifuserRecipe(UnifuserRecipe recipe) {
        this.recipe = recipe;
        this.id = recipe.getId();
        
        // O Unifuser possui 3 slots de entrada de acordo com seu setRecipe do JEI
        this.inputs = List.of(
                EmiIngredient.of(recipe.getIngredients().get(0)),
                EmiIngredient.of(recipe.getIngredients().get(1)),
                EmiIngredient.of(recipe.getIngredients().get(2))
        );
        this.outputs = List.of(EmiStack.of(recipe.getResultItem(Minecraft.getInstance().level.registryAccess())));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return CARecipesCategories.UNIFUSER_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return this.inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return this.outputs;
    }

    @Override
    public int getDisplayWidth() {
        return 116;
    }

    @Override
    public int getDisplayHeight() {
        return 54;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        ResourceLocation texture = ChangedAddonMod.textureLoc("textures/screens/jei_unifuser_screen");
        widgets.addTexture(texture, 0, 0, 116, 54, 0, 0);

        // Três inputs mapeados exatamente nas posições do JEI
        widgets.addSlot(inputs.get(0), 1, 1);
        widgets.addSlot(inputs.get(1), 1, 36);
        widgets.addSlot(inputs.get(2), 37, 18);

        // Saída
        widgets.addSlot(outputs.get(0), 96, 18).recipeContext(this);

        // Slot de progresso interativo
        float progressSpeed = recipe.getProgressSpeed();
        widgets.addSlot(EmiStack.of(ChangedAddonItems.UNIFUSER_BLOCK_ILLUSTRATIVE_ITEM.get()), 64, 36)
                .appendTooltip(Component.translatable("gui.changed_addon.recipe_progress", progressSpeed));
    }
}