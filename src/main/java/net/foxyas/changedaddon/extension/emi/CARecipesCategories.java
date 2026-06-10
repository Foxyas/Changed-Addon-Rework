package net.foxyas.changedaddon.extension.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.resources.ResourceLocation;

public class CARecipesCategories {

    public static final ResourceLocation VANILLA_WIDGETS = ResourceLocation.fromNamespaceAndPath("emi", "textures/gui/widgets.png");

    private static final ResourceLocation SIMPLIFIED_TEXTURES = ChangedAddonMod.resourceLoc("textures/gui/emi/simplified.png");

    private static EmiRenderable simplifiedRenderer(int u, int v) {
        return (draw, x, y, delta) ->
                draw.blit(SIMPLIFIED_TEXTURES, x, y, u, v, 16, 16, 64, 16);
    }

    private static EmiRenderable simplifiedRendererVanilla(int u, int v) {
        return (draw, x, y, delta) -> {
            draw.blit(VANILLA_WIDGETS, x, y, u, v, 16, 16, 16, 16);
        };
    }


    // Definição das Categorias (ID, Ícone baseado em EmiStack)
    public static final EmiRecipeCategory CATALYZER_CATEGORY = new EmiRecipeCategory(
            ChangedAddonMod.resourceLoc("catalyzer"),
            EmiStack.of(ChangedAddonBlocks.CATALYZER.get()),
            simplifiedRenderer(0, 0)
    );
    public static final EmiRecipeCategory UNIFUSER_CATEGORY = new EmiRecipeCategory(
            ChangedAddonMod.resourceLoc("unifuser"),
            EmiStack.of(ChangedAddonBlocks.UNIFUSER.get()),
            simplifiedRenderer(0, 0)
    );
    public static final EmiRecipeCategory KEYCARD_COLOR_CATEGORY = new EmiRecipeCategory(
            ChangedAddonMod.resourceLoc("keycard_coloring"),
            EmiStack.of(ChangedAddonItems.KEYCARD_ITEM.get()),
            simplifiedRendererVanilla(240, 240)
    );
}
