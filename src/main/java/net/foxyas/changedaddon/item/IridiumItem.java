package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class IridiumItem extends Item {

    private static Map<Block, Block> CONVERSION;

    public IridiumItem() {
        super(new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .stacksTo(64).fireResistant().rarity(Rarity.UNCOMMON));
    }

    // Mapeamento de blocos para substituição
    public static Map<Block, Block> conversion() {
        if (CONVERSION != null) return CONVERSION;

        CONVERSION = Map.of(
                ChangedBlocks.WALL_WHITE.get(), ChangedAddonBlocks.REINFORCED_WALL.get(),
                ChangedBlocks.WALL_CAUTION.get(), ChangedAddonBlocks.REINFORCED_WALL_CAUTION.get(),
                ChangedBlocks.WALL_BLUE_TILED.get(), ChangedAddonBlocks.REINFORCED_WALL_SILVER_TILED.get(),
                ChangedBlocks.WALL_BLUE_STRIPED.get(), ChangedAddonBlocks.REINFORCED_WALL_SILVER_STRIPED.get()
        );

        return CONVERSION;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        if (context.getHand() != InteractionHand.MAIN_HAND) return super.useOn(context);

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Block clickedBlock = world.getBlockState(pos).getBlock();

        // Verifica se o bloco está no mapa de conversão
        if (conversion().containsKey(clickedBlock)) {
            Block convertedBlock = conversion().get(clickedBlock);

            // Substitui o bloco no mundo
            world.setBlock(pos, convertedBlock.defaultBlockState(), 3);
            if (world instanceof ServerLevel serverLevel) {
                ParticlesUtil.sendParticles(serverLevel, ParticleTypes.END_ROD, pos, 0.25f, 0.25f, 0.25f, 10, 1);
                serverLevel.playSound(null, pos, SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS, 1, 1);
            }

            // Consome o item na mão, exceto no modo criativo
            Player player = context.getPlayer();
            if (player != null) {
                if (!player.isCreative()) context.getItemInHand().shrink(1);
                player.swing(InteractionHand.MAIN_HAND);
            }

            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }
}
