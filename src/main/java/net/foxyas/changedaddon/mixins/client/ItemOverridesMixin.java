package net.foxyas.changedaddon.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ItemOverrides.class)
public class ItemOverridesMixin {

    // TODO: CHECK IF THIS WORKS.

    @Inject(
            method = "resolve",
            at = @At("HEAD"),
            cancellable = true
    )
    private void resolveCustomTrims(BakedModel model, ItemStack stack, ClientLevel level, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
        if (level == null) return;

        Optional<ArmorTrim> trimOptional = ArmorTrim.getTrim(level.registryAccess(), stack);
        if (trimOptional.isEmpty()) return;

        ArmorTrim trim = trimOptional.get();
        String materialAssetName = trim.material().value().assetName();

        // Check if material uses a modded namespace/asset key
        ResourceLocation materialLoc = ResourceLocation.tryParse(materialAssetName);
        if (materialLoc == null) return;

        // If it's a non-vanilla material or modded trim
        if (!"minecraft".equals(materialLoc.getNamespace())) {
            ResourceLocation itemLoc = BuiltInRegistries.ITEM.getKey(stack.getItem());

            // Construct the expected trim model location: "modid:item/itemId_trimid_trim"
            // Example: "changed_addon:item/netherite_boots_iridium_trim#inventory"
            ModelResourceLocation customModelLoc = new ModelResourceLocation(
                    ResourceLocation.fromNamespaceAndPath(
                            materialLoc.getNamespace(),
                            "item/" + itemLoc.getPath() + "_" + materialLoc.getPath() + "_trim"
                    ),
                    "inventory"
            );

            ModelManager modelManager = Minecraft.getInstance().getModelManager();
            BakedModel customModel = modelManager.getModel(customModelLoc);

            // If the model exists and is loaded in the ModelManager (not missing_model), use it!
            if (customModel != null && customModel != modelManager.getMissingModel()) {
                cir.setReturnValue(customModel);
            }
        }
    }
}