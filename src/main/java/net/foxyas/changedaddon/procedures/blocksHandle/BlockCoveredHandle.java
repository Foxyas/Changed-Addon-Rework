package net.foxyas.changedaddon.procedures.blocksHandle;

//@Mod.EventBusSubscriber
public class BlockCoveredHandle {

//    @SubscribeEvent
//    public static void attemptToInfectBlock(LatexCoveredBlocks.CoveringBlockEvent event) {
//        Level level = event.level;
//        boolean gameRule = level.getGameRules().getBoolean(ChangedAddonModGameRules.NEED_FULL_SOURCE_TO_SPREAD);
//
//        if (gameRule) {
//            if (event.plannedState.getValue(AbstractLatexBlock.COVERED) == LatexType.DARK_LATEX) {
//                if (BlockPos.betweenClosedStream(new AABB(event.blockPos).inflate(16)).noneMatch((pos -> level.getBlockState(pos).is(ChangedBlocks.DARK_LATEX_BLOCK.get())))) {
//                    event.setCanceled(true);
//                }
//            } else if (event.plannedState.getValue(AbstractLatexBlock.COVERED) == LatexType.WHITE_LATEX) {
//                if (BlockPos.betweenClosedStream(new AABB(event.blockPos).inflate(16)).noneMatch((pos -> level.getBlockState(pos).is(ChangedBlocks.WHITE_LATEX_BLOCK.get())))) {
//                    event.setCanceled(true);
//                }
//            }
//        }
//    }
}
