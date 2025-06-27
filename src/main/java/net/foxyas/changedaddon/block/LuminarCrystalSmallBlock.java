package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LuminarCrystalSmallBlock extends AbstractLuminarCrystal.CrystalSmall {

    public LuminarCrystalSmallBlock() {
        super();
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderLayer() {
        ItemBlockRenderTypes.setRenderLayer(ChangedAddonModBlocks.LUMINAR_CRYSTAL_SMALL.get(), renderType -> renderType == RenderType.cutout());
    }
}
