package net.foxyas.changedaddon.block.advanced;


import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.ltxprogrammer.changed.block.KeypadBlock;
import net.ltxprogrammer.changed.block.entity.KeypadBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class KeypadUpgradeEvent {

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getPlayer();
        Level level = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        // Verifica se está no servidor
        if (level.isClientSide()) return;

        // Verifica se está agachado e segurando relógio
        if (!player.isShiftKeyDown()) return;
        if (!player.getItemInHand(event.getHand()).is(Items.CLOCK)) return;

        Block block = state.getBlock();
        if (!(block instanceof KeypadBlock)) return;

        // Substituir por TimedKeypad mantendo a orientação
        BlockState newState = ChangedAddonBlocks.TIMED_KEYPAD.get().defaultBlockState();

        if (state.hasProperty(KeypadBlock.FACING)) {
            Direction facing = state.getValue(KeypadBlock.FACING);
            newState = newState.setValue(KeypadBlock.FACING, facing);
        }

        if (state.hasProperty(KeypadBlock.POWERED)) {
            boolean value = state.getValue(KeypadBlock.POWERED);
            newState = newState.setValue(KeypadBlock.POWERED, value);
        }

        BlockEntity originalBlockEntity = level.getBlockEntity(pos);
        level.setBlockAndUpdate(pos, newState);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (originalBlockEntity instanceof KeypadBlockEntity keypadBlockEntity) {
            if (blockEntity instanceof TimedKeypadBlockEntity timedKeypadBlockEntity) {
                timedKeypadBlockEntity.code = keypadBlockEntity.code;
                level.sendBlockUpdated(pos, newState, newState, 3);
            }
        }

        // Feedback opcional
        // player.displayClientMessage(new TextComponent("Converted to Timed Keypad!"), true);

        // Cancelar uso padrão do clique
        event.setCanceled(true);
        event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
    }
}
