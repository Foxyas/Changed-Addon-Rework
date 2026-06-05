package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;

import static net.foxyas.changedaddon.init.ChangedAddonItems.*;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {

    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChangedAddonMod.MODID, existingFileHelper);
    }

    private static ResourceLocation blockLoc(ResourceLocation loc) {
        return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + loc.getPath());
    }

    private static ResourceLocation blockLoc(ResourceLocation loc, String path) {
        return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + path + "/" + loc.getPath());
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

        basicItem(CRAFTABLE_PROTOTYPE_SPAWN_EGG.get());

        basicBlockItem(ChangedAddonBlocks.LUMINARA_LEAVES);
    }

    private <T extends Block> ResourceLocation key(RegistryObject<T> block) {
        return ForgeRegistries.BLOCKS.getKey(block.get());
    }

    public <T extends Block> void basicBlockItem(RegistryObject<T> block) {
        ResourceLocation blockLoc = blockLoc(block.getId());

        ModelFile defaultModel = getExistingFile(blockLoc);

        getBuilder(key(block).getPath()).parent(defaultModel);
    }

    public <T extends Block> void basicBlockItem(RegistryObject<T> block, ModelFile model) {
        getBuilder(key(block).getPath()).parent(model);
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
