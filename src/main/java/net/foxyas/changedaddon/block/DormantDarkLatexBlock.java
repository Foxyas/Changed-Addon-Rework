
package net.foxyas.changedaddon.block;

import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Collections;
import java.util.List;

public class DormantDarkLatexBlock extends AbstractLatexBlock {
	public DormantDarkLatexBlock() {
		super(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_GRAY).sound(SoundType.SLIME_BLOCK).strength(1.0F, 4.0F), LatexType.DARK_LATEX, ChangedItems.DARK_LATEX_GOO);
	}
}
