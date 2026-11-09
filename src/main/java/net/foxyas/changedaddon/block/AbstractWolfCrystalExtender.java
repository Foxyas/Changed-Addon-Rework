package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.ltxprogrammer.changed.block.AbstractLatexIceBlock;
import net.ltxprogrammer.changed.block.TransfurCrystalBlock;
import net.ltxprogrammer.changed.block.WolfCrystal;
import net.ltxprogrammer.changed.block.WolfCrystalBlock;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;


public class AbstractWolfCrystalExtender {

    @Mod.EventBusSubscriber
    public static class TransformBlockSystem {

        @SubscribeEvent
        public static void colorTheCrystal(PlayerInteractEvent.RightClickBlock event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            ItemStack itemStack = event.getItemStack();
            BlockState currentState = level.getBlockState(pos);
            Block block = currentState.getBlock();

            // Verifica se é um bloco que pode ser transformado
            if (block instanceof WolfCrystalBlock || block instanceof AbstractWolfCrystalBlock) {
                // Mapeia os corantes para os blocos correspondentes
                Map<Item, Block> dyeToBlockMap = Map.of(
                        Items.LIGHT_BLUE_DYE, ChangedAddonBlocks.BLUE_WOLF_CRYSTAL_BLOCK.get(),
                        Items.WHITE_DYE, ChangedAddonBlocks.WHITE_WOLF_CRYSTAL_BLOCK.get(),
                        Items.ORANGE_DYE, ChangedAddonBlocks.ORANGE_WOLF_CRYSTAL_BLOCK.get(),
                        Items.YELLOW_DYE, ChangedAddonBlocks.YELLOW_WOLF_CRYSTAL_BLOCK.get(),
                        Items.RED_DYE, ChangedBlocks.WOLF_CRYSTAL_BLOCK.get() // Apenas AbstractWolfCrystalBlock pode ser alterado para vermelho
                );

                // Verifica se o corante corresponde a um bloco
                Block newBlock = dyeToBlockMap.get(itemStack.getItem());
                if (newBlock != null) {
                    // Evita o desperdício verificando se o bloco já tem a mesma cor
                    if (!newBlock.defaultBlockState().equals(currentState)) {
                        level.setBlockAndUpdate(pos, newBlock.defaultBlockState());
                        playDyeUseSound(level, pos);
                        event.getEntity().swing(event.getHand());

                        // Consome o corante se o jogador não estiver no modo criativo
                        if (!event.getEntity().isCreative()) {
                            itemStack.shrink(1);
                        }

                        // Código para conceder um avanço ao jogador
                        grantAdvancement(event.getEntity(), "changed_addon:crystal_dyer");
                    }
                }
            }
        }

        // Method auxiliar para tocar o som de aplicação do corante
        private static void playDyeUseSound(Level level, BlockPos pos) {
            level.playLocalSound(
                    pos.getX(), pos.getY(), pos.getZ(),
                    SoundEvents.DYE_USE, SoundSource.BLOCKS,
                    1.0f, 1.0f, true
            );
        }

        // Method para conceder um avanço ao jogador
        private static void grantAdvancement(Player player, String advancementId) {
            if (player instanceof ServerPlayer serverPlayer) {
                Advancement advancement = serverPlayer.server.getAdvancements()
                        .getAdvancement(ResourceLocation.parse(advancementId));
                if (advancement != null) {
                    AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
                    if (!progress.isDone()) {
                        for (String criterion : progress.getRemainingCriteria()) {
                            serverPlayer.getAdvancements().award(advancement, criterion);
                        }
                    }
                }
            }
        }
    }

    public static abstract class AbstractWolfCrystalBlock extends AbstractLatexIceBlock {

        public AbstractWolfCrystalBlock() {
            super(
                    BlockBehaviour.Properties.of()  //Fixme : (Material.ICE_SOLID, MaterialColor.COLOR_RED)
                            .mapColor(MapColor.COLOR_RED)
                            .friction(0.98F)
                            .sound(SoundType.AMETHYST)
                            .strength(2.0F, 2.0F).noLootTable()
            );
        }


        @Override
        public boolean canSustainPlant(@NotNull BlockState state, @NotNull BlockGetter world, BlockPos pos, @NotNull Direction facing, net.minecraftforge.common.IPlantable plantable) {

            BlockState plant = plantable.getPlant(world, pos.relative(facing));
            return plant.getBlock() instanceof WolfCrystal;
        }

        @Override
        public void stepOn(@NotNull Level level, @NotNull BlockPos position, @NotNull BlockState blockState, @NotNull Entity entity) {
            triggerCrystal(blockState, level, position, entity);
            super.stepOn(level, position, blockState, entity);
        }

        /*
        * public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
            super.tick(state, level, pos, random);
            BlockPos above = pos.above();
            if (level.getBlockState(above).is(Blocks.AIR)) {
                level.setBlock(above, ChangedBlocks.WOLF_CRYSTAL_SMALL.get().defaultBlockState(), 3);
                level.playSound(null, pos, ChangedSounds.ICE2, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
        }*/

        private void triggerCrystal(BlockState blockState, Level level, BlockPos position, Entity entity) {

            if (entity instanceof LivingEntity le && !(entity instanceof ChangedEntity) && !le.isDeadOrDying()) {
                if (entity instanceof Player player && (ProcessTransfur.isPlayerTransfurred(player) || player.isCreative()))
                    return;
                level.scheduleTick(position, this, 20);
            }
        }
    }

    public static abstract class AbstractWolfCrystalSmall extends TransfurCrystalBlock {

        public AbstractWolfCrystalSmall(Supplier<? extends Item> fragment) {
            super(ChangedTransfurVariants.CRYSTAL_WOLF, fragment,

                    BlockBehaviour.Properties.of() //copy(ChangedBlocks.WOLF_CRYSTAL_SMALL.get()) // Fixme : (ChangedMaterials.LATEX_CRYSTAL)
                            .sound(SoundType.AMETHYST_CLUSTER)
                            .noOcclusion()
                            .dynamicShape()
                            .strength(1.7F, 0.2F).noLootTable()
            );
        }


        public AbstractWolfCrystalSmall() {
            super(ChangedTransfurVariants.CRYSTAL_WOLF, ChangedItems.WOLF_CRYSTAL_FRAGMENT,

                    BlockBehaviour.Properties.copy(ChangedBlocks.WOLF_CRYSTAL_SMALL.get())
                            .sound(SoundType.AMETHYST_CLUSTER)
                            .noOcclusion()
                            .dynamicShape()
                            .strength(1.7F, 0.2F).noLootTable()
            );
        }

        @Override
        public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
            return Shapes.empty();
        }

    }
}
