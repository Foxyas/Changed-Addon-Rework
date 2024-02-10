package net.foxyas.changedaddon.procedures;


import net.ltxprogrammer.changed.entity.LatexEntity;
import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
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
						if (variant.getParent().getEntityType().is(ChangedTags.EntityTypes.ORGANIC_LATEX)) {
							entity.getServer().getCommands().performCommand(entity.createCommandSourceStack().withSuppressedOutput().withPermission(4), "particle changed:gas " + color3.toInt() + " ~ ~1 ~ 0.2 0.5 0.2 0.1 50 force");
						} else {
							entity.getServer().getCommands().performCommand(entity.createCommandSourceStack().withSuppressedOutput().withPermission(4), "particle changed:dripping_latex " + color3.toInt() + " ~ ~1 ~ 0.2 0.5 0.2 0.1 50 force");

						}
					}

				}
		}
	}
}