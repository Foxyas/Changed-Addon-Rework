package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;

public class SetRecipePageProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double MAXPAGE = 0;
		double CatlyzerMAXPAGE = 0;
		MAXPAGE = (world.getBlockState(new BlockPos(x, y, z))).getBlock() == ChangedAddonModBlocks.UNIFUSER.get() ? 2 : 2;
		CatlyzerMAXPAGE = (world.getBlockState(new BlockPos(x, y, z))).getBlock() == ChangedAddonModBlocks.CATLYZER.get() ? 1 : 1;
		if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).UnifuserRecipePage < MAXPAGE) {
			{
				double _setval = (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).UnifuserRecipePage + 1;
				entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.UnifuserRecipePage = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		} else {
			{
				double _setval = 1;
				entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.UnifuserRecipePage = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
	}
}
