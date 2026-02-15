package net.foxyas.changedaddon.mixins.client;

import com.google.common.collect.ImmutableMap;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.Util;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(PalettedPermutations.class)
public abstract class PalettedPermutationsMixin implements SpriteSource {

    @Shadow @Final private ResourceLocation paletteKey;

    @Shadow
    @Final
    @Mutable private Map<String, ResourceLocation> permutations;

    @Inject(method = "run", at = @At("HEAD"))
    private void injectCustomPermutations(ResourceManager pResourceManager, Output pOutput, CallbackInfo ci) {
//        if (this.paletteKey.equals(ResourceLocation.parse("trims/color_palettes/trim_palette"))) {
//            if (this.permutations instanceof ImmutableMap<String, ResourceLocation>) {
//                HashMap<String, ResourceLocation> mutableMap = new HashMap<>(permutations);
//                mutableMap.put("iridium", ChangedAddonMod.resourceLoc("trims/color_palettes/iridium"));
//                this.permutations = mutableMap;
//            } else {
//                this.permutations.put("iridium", ChangedAddonMod.resourceLoc("trims/color_palettes/iridium"));
//            }
//        }
    }
}
