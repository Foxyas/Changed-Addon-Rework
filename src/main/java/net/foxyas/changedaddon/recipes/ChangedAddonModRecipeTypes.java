package net.foxyas.changedaddon.recipes;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.recipes.special.DyeableShortsColoringRecipe;
import net.foxyas.changedaddon.recipes.special.HaydenTransfurRecipe;
import net.foxyas.changedaddon.recipes.special.LaserPointerColoringRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonModRecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ChangedAddonMod.MODID);

    // Registrar suas receitas especiais
    public static final RegistryObject<RecipeSerializer<?>> LAZER_POINTER_COLORING =
            SERIALIZERS.register("laser_pointer_coloring", LaserPointerColoringRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> SHORTS_COLORING =
            SERIALIZERS.register("shorts_coloring", DyeableShortsColoringRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> HAYDEN_SYRINGE_RECIPE =
            SERIALIZERS.register("hayden_syringe_recipe", HaydenTransfurRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<CatalyzerRecipe>> CATALYZER_RECIPE =
            SERIALIZERS.register("catalyzer", () -> CatalyzerRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<UnifuserRecipe>> UNIFUSER_RECIPE =
            SERIALIZERS.register("unifuser", () -> UnifuserRecipe.Serializer.INSTANCE);

    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        // Registrar o DeferredRegister no EventBus
        SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
