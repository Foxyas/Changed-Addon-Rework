package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.item.IridiumItem;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class RightClickWithIridiumProcedure {

	// Mapeamento de blocos para substituição
	public static final Map<Block, Block> BASE_CONVERSION = new HashMap<>();

	static {
		BASE_CONVERSION.put(ChangedBlocks.WALL_WHITE.get(),
				ChangedAddonModBlocks.REINFORCED_WALL.get());
		BASE_CONVERSION.put(ChangedBlocks.WALL_CAUTION.get(),
				ChangedAddonModBlocks.REINFORCED_WALL_CAUTION.get());
		BASE_CONVERSION.put(ChangedBlocks.WALL_BLUE_TILED.get(),
				ChangedAddonModBlocks.REINFORCED_WALL_SILVER_TILED.get());
		BASE_CONVERSION.put(ChangedBlocks.WALL_BLUE_STRIPED.get(),
				ChangedAddonModBlocks.REINFORCED_WALL_SILVER_STRIPED.get());
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		// Verifica se o jogador está usando a mão principal e o item correto
		if (event.getHand() == InteractionHand.MAIN_HAND && event.getItemStack().getItem() instanceof IridiumItem) {
			Level world = event.getWorld();
			BlockPos pos = event.getPos();
			Block clickedBlock = world.getBlockState(pos).getBlock();

			// Verifica se o bloco está no mapa de conversão
			if (BASE_CONVERSION.containsKey(clickedBlock)) {
				Block convertedBlock = BASE_CONVERSION.get(clickedBlock);

				// Substitui o bloco no mundo
				world.setBlock(pos, convertedBlock.defaultBlockState(), 3);
				if (world instanceof ServerLevel serverLevel){
					PlayerUtilProcedure.ParticlesUtil.sendParticles(serverLevel,ParticleTypes.END_ROD,pos,0.25f,0.25f,0.25f,10,1);
				}

				// Consome o item na mão, exceto no modo criativo
				if (!event.getPlayer().isCreative()) {
					event.getItemStack().shrink(1);
				}

				// Cancela o evento para evitar interações adicionais
				event.setCanceled(true);
			}
		}
	}
}
