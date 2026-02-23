package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;

import static net.foxyas.changedaddon.init.ChangedAddonItems.*;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {

    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicSpawnEgg(PROTOGEN_0SENIA0_SPAWN_EGG);
        basicSpawnEgg(LATEX_KAYLA_SHARK_SPAWN_EGG);
        basicSpawnEgg(LATEX_BORDER_COLLIE_SPAWN_EGG);
        basicSpawnEgg(BUFF_DAZED_LATEX_SPAWN_EGG);

        basicSpawnEgg(AVALI_ZERGODMASTER_SPAWN_EGG);
        basicItem(ALPHA_SERUM_SYRINGE.get());
        basicItem(TRANSLATOR.get());

        layeredItemMirroredHands(KEYCARD_ITEM.get(),
                List.of(
                        ResourceLocation.fromNamespaceAndPath(KEYCARD_ITEM.getId().getNamespace(), "item/" + KEYCARD_ITEM.getId().getPath() + "_base"),
                        ResourceLocation.fromNamespaceAndPath(KEYCARD_ITEM.getId().getNamespace(), "item/" + KEYCARD_ITEM.getId().getPath() + "_top"),
                        ResourceLocation.fromNamespaceAndPath(KEYCARD_ITEM.getId().getNamespace(), "item/" + KEYCARD_ITEM.getId().getPath() + "_bottom")
                )
        );
    }

    public ItemModelBuilder layeredItem(Item item, HashMap<Integer, ResourceLocation> layerTextures) {
        ItemModelBuilder builder = getBuilder(item.toString());

        if (!layerTextures.isEmpty()) {
            layerTextures.forEach(((layer, texture) -> builder.parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer" + layer, texture)));
        }

        return builder;
    }

    public ItemModelBuilder layeredItem(ResourceLocation item, List<ResourceLocation> layersTextures) {
        ItemModelBuilder builder = getBuilder(item.toString());

        if (!layersTextures.isEmpty()) {
            for (int i = 0; i < layersTextures.size(); i++) {
                ResourceLocation texture = layersTextures.get(i);
                builder.parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer" + i, texture)
                ;
            }
        }

        return builder;
    }

    public ItemModelBuilder layeredItem(Item item, List<ResourceLocation> layersTextures) {
        ItemModelBuilder builder = getBuilder(item.toString());

        if (!layersTextures.isEmpty()) {
            for (int i = 0; i < layersTextures.size(); i++) {
                ResourceLocation texture = layersTextures.get(i);
                builder.parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer" + i, texture)
                ;
            }
        }

        return builder;
    }

    public ItemModelBuilder layeredItemMirroredHands(Item item, List<ResourceLocation> layersTextures) {
        ItemModelBuilder builder = getBuilder(item.toString());

        if (!layersTextures.isEmpty()) {
            for (int i = 0; i < layersTextures.size(); i++) {
                ResourceLocation texture = layersTextures.get(i);
                builder.parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer" + i, texture)
                ;
            }
        }

        // === Adds the transforms in the display JSON ===
        builder.transforms()
                // thirdperson_lefthand
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(0, 180, 0)
                .translation(0f, 3.0f, 1.0f)
                .scale(0.55f, 0.55f, 0.55f)
                .end()

                // firstperson_lefthand
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0, 90, -25)
                .translation(1.13f, 3.2f, 1.13f)
                .scale(0.68f, 0.68f, 0.68f)
                .end()
        ;

        return builder;
    }

    public ItemModelBuilder basicSpawnEgg(RegistryObject<? extends Item> item) {
        return basicSpawnEgg(item.getId());
    }

    public ItemModelBuilder basicSpawnEgg(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }
}
