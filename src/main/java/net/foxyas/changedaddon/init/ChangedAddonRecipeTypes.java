package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
import net.foxyas.changedaddon.recipe.UnifuserRecipe;
import net.foxyas.changedaddon.recipe.special.HaydenTransfurRecipe;
import net.foxyas.changedaddon.recipe.special.KeycardColorRecipe;
import net.foxyas.changedaddon.recipe.special.LaserPointerColoringRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChangedAddonRecipeTypes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ChangedAddonMod.MODID);

    // Registrar suas receitas especiais
    public static final RegistryObject<RecipeSerializer<?>> LAZER_POINTER_COLORING =
            SERIALIZERS.register("laser_pointer_coloring", LaserPointerColoringRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> KEYCARD_COLOR =
            SERIALIZERS.register("keycard_coloring", KeycardColorRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> HAYDEN_SYRINGE_RECIPE =
            SERIALIZERS.register("hayden_syringe_recipe", HaydenTransfurRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<CatalyzerRecipe>> CATALYZER_RECIPE =
            SERIALIZERS.register("catalyzer", () -> CatalyzerRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<UnifuserRecipe>> UNIFUSER_RECIPE =
            SERIALIZERS.register("unifuser", () -> UnifuserRecipe.Serializer.INSTANCE);
}
