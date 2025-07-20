
package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LuminarCrystalBlock extends AbstractLuminarCrystal.Block {
	public LuminarCrystalBlock() {
		super();
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRenderLayer() {
		ItemBlockRenderTypes.setRenderLayer(ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get(), renderType -> renderType == RenderType.translucent());
	}
}
