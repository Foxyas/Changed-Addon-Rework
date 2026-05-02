package net.foxyas.changedaddon.datagen.compatibility;

import net.foxyas.changedaddon.datagen.TrimMaterials;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import java.util.HashMap;
import java.util.Map;

public class ModTrimMapsProvider extends TrimMapsProvider {

    public ModTrimMapsProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildMaps(MapBuilder builder) {
        Map<String, String> modPairs = new HashMap<>();
        for (Map.Entry<String, ResourceLocation> entry : TrimMaterials.TRIMS.entrySet()) {
            modPairs.putIfAbsent(entry.getValue().toString(), entry.getKey());
        }
//        modPairs.put("changed_addon:trims/color_palettes/iridium", "iridium");
//        modPairs.put("changed_addon:trims/color_palettes/goo_core", "goo_core");
        builder.addFile(ResourceLocation.fromNamespaceAndPath("trimmed", "custom_trim_material_permutations"), modPairs);
    }
}