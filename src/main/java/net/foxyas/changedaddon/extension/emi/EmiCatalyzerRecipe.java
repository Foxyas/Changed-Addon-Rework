package net.foxyas.changedaddon.extension.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmiCatalyzerRecipe implements EmiRecipe {
    
    private final CatalyzerRecipe recipe;
    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public EmiCatalyzerRecipe(CatalyzerRecipe recipe) {
        this.recipe = recipe;
        this.id = recipe.getId();
        // Converte as entradas do Forge/Vanilla para EmiIngredient
        this.inputs = List.of(EmiIngredient.of(recipe.getIngredients().get(0)));
        // Converte a saída para EmiStack
        this.outputs = List.of(EmiStack.of(recipe.getResultItem(Minecraft.getInstance().level.registryAccess())));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return CARecipesCategories.CATALYZER_CATEGORY;
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
        // Define a textura customizada de fundo do seu mod
        ResourceLocation texture = ChangedAddonMod.textureLoc("textures/screens/jei_catalyzer_screen");
        widgets.addTexture(texture, 0, 0, 116, 54, 0, 0);

        // Slot de Entrada (Input)
        widgets.addSlot(inputs.get(0), 12, 18);

        // Slot de Saída (Output)
        widgets.addSlot(outputs.get(0), 96, 18).recipeContext(this);

        // Item Ilustrativo com Tooltip de Progresso e Nitrogênio
        float progressSpeed = recipe.getProgressSpeed();
        float nitrogenUsage = recipe.getNitrogenUsage();
        
        widgets.addSlot(EmiStack.of(ChangedAddonItems.CATALYZER_BLOCK_ILLUSTRATIVE_ITEM.get()), 51, 36)
                .appendTooltip(Component.translatable("gui.changed_addon.catalyzer.nitrogen_usage", progressSpeed, nitrogenUsage));
    }
}