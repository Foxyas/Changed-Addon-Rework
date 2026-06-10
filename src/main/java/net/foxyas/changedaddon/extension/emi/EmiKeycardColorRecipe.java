package net.foxyas.changedaddon.extension.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.recipe.special.KeycardColorRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmiKeycardColorRecipe implements EmiRecipe {

    private final ResourceLocation id;
    private final EmiIngredient keycard;
    private final EmiIngredient dyes;

    public EmiKeycardColorRecipe(KeycardColorRecipe recipe) {
        this.id = recipe.getId();
        this.keycard = EmiIngredient.of(List.of(EmiStack.of(ChangedAddonItems.KEYCARD_ITEM.get())));
        // Puxa a tag forge:dyes convertida em ingrediente do EMI
        this.dyes = EmiIngredient.of(Tags.Items.DYES);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return CARecipesCategories.KEYCARD_COLOR_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        // Retorna o Keycard + os Dyes como lista de inputs requeridos
        return List.of(keycard, dyes);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(ChangedAddonItems.KEYCARD_ITEM.get()));
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
        ResourceLocation texture = ChangedAddonMod.textureLoc("textures/misc/gui_vanilla");
        widgets.addTexture(texture, 0, 0, 116, 54, 0, 0);

        // Grade 3x3 imitando a receita dinâmica que você tinha feito
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                if (y == 1 && x == 1) {
                    widgets.addSlot(keycard, x * 18 + 1, y * 18 + 1);
                } else {
                    // O EMI cuida de alternar visualmente os itens da Tag automaticamente!
                    widgets.addSlot(dyes, x * 18 + 1, y * 18 + 1);
                }
            }
        }

        // Output final
        widgets.addSlot(EmiStack.of(ChangedAddonItems.KEYCARD_ITEM.get()), 95, 19).recipeContext(this);
    }
}