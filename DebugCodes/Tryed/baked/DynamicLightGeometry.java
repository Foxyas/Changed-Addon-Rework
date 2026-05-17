package net.foxyas.changedaddon.client.model.baked;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.Map;
import java.util.function.Function;

@Deprecated() // IS BUGGED NEED FIX
public class DynamicLightGeometry implements IUnbakedGeometry<DynamicLightGeometry> {

    private final BlockModel baseModel;
    private final Map<Integer, Integer> elementLightMap;

    public DynamicLightGeometry(BlockModel baseModel, Map<Integer, Integer> elementLightMap) {
        this.baseModel = baseModel;
        this.elementLightMap = elementLightMap;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        BakedModel bakedModel = baseModel.bake(baker, baseModel, spriteGetter, modelState, modelLocation, baseModel.hasAmbientOcclusion());

        if (!this.elementLightMap.isEmpty()) {
            return new DynamicLightBakedModelWrapper(bakedModel, this.elementLightMap);
        }
        return bakedModel;
    }
}