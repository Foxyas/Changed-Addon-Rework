package net.foxyas.changedaddon.procedures.blocksHandle;

import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.entity.LatexType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class BoneMealExpansion {

    @Mod.EventBusSubscriber
    public static class AttemptToApply {

        @SubscribeEvent
        public static void onBoneMealUse(BonemealEvent event) {
            Player player = event.getPlayer();
            Level level = event.getWorld();
            BlockPos pos = event.getPos();
            BlockState state = event.getBlock();
            ItemStack stack = event.getStack();

            if (stack.getItem() == Items.BONE_MEAL) {
                if (state.getValue(AbstractLatexBlock.COVERED) != LatexType.NEUTRAL && level instanceof ServerLevel serverLevel) {
                    for (BlockPos blockPos : BlockPos.betweenClosedStream(new AABB(pos).inflate(16)).toList()) {
                        BlockState posState = level.getBlockState(blockPos);
                        if (posState.getValue(AbstractLatexBlock.COVERED) != LatexType.NEUTRAL) {
                            posState.randomTick(serverLevel, pos, level.getRandom());
                            // Force Success
                            if (event.hasResult()) {
                                event.setResult(Event.Result.ALLOW);
                            }
                        }
                    }
                }


                // Cancel Action
                // event.setCanceled(true);
            }
        }
    }

    public static class BoneMealDispenserHandler {
        public static void registerDispenserBehavior() {
            DispenserBlock.registerBehavior(Items.BONE_MEAL, (source, stack) -> {
                Level level = source.getLevel();
                Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos targetPos = source.getPos().relative(facing);

                if (level instanceof ServerLevel serverLevel) {
                    BlockState targetState = serverLevel.getBlockState(targetPos);

                    // Se for bloco com COVERED diferente de NEUTRAL, aplica randomTick e espalha
                    if (targetState.hasProperty(AbstractLatexBlock.COVERED)
                            && targetState.getValue(AbstractLatexBlock.COVERED) != LatexType.NEUTRAL) {

                        // Dispara um randomTick nele mesmo
                        targetState.randomTick(serverLevel, targetPos, serverLevel.getRandom());

                        // Também em área em volta
                        for (BlockPos pos : BlockPos.betweenClosedStream(new AABB(targetPos).inflate(16)).toList()) {
                            BlockState posState = level.getBlockState(pos);
                            if (posState.hasProperty(AbstractLatexBlock.COVERED)
                                    && posState.getValue(AbstractLatexBlock.COVERED) != LatexType.NEUTRAL) {
                                posState.randomTick(serverLevel, pos, serverLevel.getRandom());
                            }
                        }

                        // Consome o bone meal 1x
                        stack.shrink(1);
                        return stack;
                    }

                    // Vanilla fallback
                    if (BoneMealItem.applyBonemeal(stack, serverLevel, targetPos, FakePlayerFactory.getMinecraft(serverLevel))) {
                        serverLevel.levelEvent(1505, targetPos, 0);
                        return stack;
                    }

                }

                return stack;
            });
        }
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(BoneMealDispenserHandler::registerDispenserBehavior);
    }

}
