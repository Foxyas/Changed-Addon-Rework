package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.ForgeFaceData;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelProvider extends net.minecraftforge.client.model.generators.BlockModelProvider {
    public BlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        generateEmissiveCrop();

        withExistingParent(ChangedAddonBlocks.LUMINARA_BLOOM.getId().getPath(), ChangedAddonMod.resourceLoc("customs/emissive_cross"))
                .renderType("minecraft:cutout")
                .texture("cross", ChangedAddonMod.resourceLoc("block/luminara_bloom"))
                .texture("glow", ChangedAddonMod.resourceLoc("block/luminara_bloom_emissive"))
        ;
    }

    private void generateEmissiveCrop() {
        getBuilder("customs/emissive_cross")
                .ao(false) // Disable ambient occlusion for the entire model layout
                .texture("particle", "#cross") // Set up the fallback particle link pointing to the base #cross texture

                // ==========================================
                // BASE LAYER: Standard non-glowing elements
                // ==========================================

                // Diagonal Plane 1 (North-South cross segment)
                .element()
                .from(0.8f, 0.0f, 8.0f)
                .to(15.2f, 16.0f, 8.0f)
                .rotation()
                .origin(8.0f, 8.0f, 8.0f)
                .axis(net.minecraft.core.Direction.Axis.Y)
                .angle(45.0f)
                .rescale(true)
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.NORTH).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#cross").end()
                .face(net.minecraft.core.Direction.SOUTH).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#cross").end()
                .end()

                // Diagonal Plane 2 (West-East cross segment)
                .element()
                .from(8.0f, 0.0f, 0.8f)
                .to(8.0f, 16.0f, 15.2f)
                .rotation()
                .origin(8.0f, 8.0f, 8.0f)
                .axis(net.minecraft.core.Direction.Axis.Y)
                .angle(45.0f)
                .rescale(true)
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.WEST).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#cross").end()
                .face(net.minecraft.core.Direction.EAST).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#cross").end()
                .end()

                // ==========================================
                // GLOW LAYER: Emissive ForgeFaceData elements
                // ==========================================

                // Emissive Diagonal Plane 1 (North-South overlay)
                .element()
                .from(0.8f, 0.0f, 8.0f)
                .to(15.2f, 16.0f, 8.0f)
                .rotation()
                .origin(8.0f, 8.0f, 8.0f)
                .axis(net.minecraft.core.Direction.Axis.Y)
                .angle(45.0f)
                .rescale(true)
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.NORTH).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#glow").color(0xFFFFFFFF).emissivity(15, 0).ao(false).end()
                .shade(false)
                .face(net.minecraft.core.Direction.SOUTH)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#glow")
                .color(0xFFFFFFFF).emissivity(15, 0).ao(false) // 15 block light, no ambient occlusion shadows
                .end()
                .end()

                // Emissive Diagonal Plane 2 (West-East overlay)
                .element()
                .from(8.0f, 0.0f, 0.8f)
                .to(8.0f, 16.0f, 15.2f)
                .rotation()
                .origin(8.0f, 8.0f, 8.0f)
                .axis(net.minecraft.core.Direction.Axis.Y)
                .angle(45.0f)
                .rescale(true)
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.WEST)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#glow")
                .color(0xFFFFFFFF).emissivity(15, 0).ao(false) // 15 block light, no ambient occlusion shadows
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.EAST)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#glow")
                .color(0xFFFFFFFF).emissivity(15, 0).ao(false) // 15 block light, no ambient occlusion shadows
                .end()
                .end();
    }
}
