package net.foxyas.changedaddon.procedures;


import net.ltxprogrammer.changed.entity.LatexEntity;
import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class SummonDripParticlesProcedure {
	public static void execute(Entity entity) {
		{
            if (!entity.level.isClientSide() && entity.getServer() != null)
				if (entity instanceof Player player) {
					LatexVariantInstance<?> variant = ProcessTransfur.getPlayerLatexVariant(player);
					if (variant != null) {
						LatexEntity fakeEntity = variant.getLatexEntity();
						Color3 color3 = fakeEntity.getDripColor();
						if(player.getLevel() instanceof ServerLevel serverLevel){
							if (variant.getParent().getEntityType().is(ChangedTags.EntityTypes.ORGANIC_LATEX)) {
								serverLevel.sendParticles(ChangedParticles.gas(color3), entity.getX(), entity.getY() + 1, entity.getZ(),20, 0.2, 0.5, 0.2,0);
							} else {
								serverLevel.sendParticles(ChangedParticles.drippingLatex(color3), entity.getX(), entity.getY() + 1, entity.getZ(),20, 0.2, 0.5, 0.2,0);
							}

						}
					}

				}
		}
	}
}