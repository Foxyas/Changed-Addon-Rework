package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.event.LatexTypePlayerEvent;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.entity.latex.SpreadingLatexType;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.util.UniversalDist;
import net.ltxprogrammer.changed.world.LatexCoverGetter;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.ltxprogrammer.changed.world.LevelExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class SprayItem extends Item {

    protected final Supplier<LatexType> latexType;

    public SprayItem(Supplier<LatexType> latexType) {
        super(new Item.Properties()
                .durability(64).rarity(Rarity.COMMON)
        );
        this.latexType = latexType;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;
        if (player.level().isClientSide()) return InteractionResult.SUCCESS;

        ItemStack stack = context.getItemInHand();
        player.getCooldowns().addCooldown(stack.getItem(), 20);

//        BlockPos origin = context.getClickedPos();
//        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        Direction clickedFace = context.getClickedFace();

        Level level = player.level;
//        pos.set(origin);

        int affectedBlocks = 0;

        if (latexType.get() == ChangedLatexTypes.NONE.get()) {
            handleLatexRemoval(context);
//            LatexCoverState latexCoverState = LatexCoverState.getAt(level, pos);
//            if (!latexCoverState.isAir()) {
//                BooleanProperty faceProp = SpreadingLatexType.FACES.get(clickedFace.getOpposite());
//                LatexCoverState.setAtAndUpdate(level, pos, latexCoverState.setValue(faceProp, false));
//
//                for (Direction dir : Direction.values()) {
//                    pos.set(origin).move(dir);
//                    latexCoverState = LatexCoverState.getAt(level, pos);
//
//                    if (!latexCoverState.isAir())
//                        LatexCoverState.setAtAndUpdate(level, pos, latexCoverState.setValue(faceProp, false));
//                }
//            }
        } else {
            BlockPos clickedOriginPos = context.getClickedPos();
            List<BlockPos> clickedPoses = new ArrayList<>(List.of(clickedOriginPos));
            for (Direction direction : Direction.values()) {
                clickedPoses.add(clickedOriginPos.relative(direction));
            }
            for (BlockPos clickedPos : clickedPoses) {
                BlockState clickedState = context.getLevel().getBlockState(clickedPos);
                if (clickedState.is(ChangedTags.Blocks.DENY_LATEX_COVER)) {
                    return InteractionResult.FAIL;
                } else {
                    affectedBlocks += applyGooOnBlock(context, clickedPos, clickedState) ? 1 : 0;
                }
            }
        }

        if (affectedBlocks > 0 && (!player.isCreative() && EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) == 0)) {
            if (stack.hurt(1, player.getRandom(), player instanceof ServerPlayer sPlayer ? sPlayer : null)) {
                stack.shrink(1);
                stack.setDamageValue(0);
            }
        }

        level.playSound(null, player, ChangedAddonSoundEvents.SPRAY_SOUND.get(), SoundSource.PLAYERS, 1, 1);

        return InteractionResult.SUCCESS;
    }

    public void handleLatexRemoval(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos originPos = context.getClickedPos();
        LatexCoverState originState = LatexCoverState.getAt(level, originPos);

        if (originState.isAir()) {
            return;
        }

        // Ponto exato de clique convertido para coordenadas locais do bloco (0.0 a 1.0)
        Vec3 hitVec = context.getClickLocation();
        Vec3 localHit = hitVec.subtract(originPos.getX(), originPos.getY(), originPos.getZ());

        // Posição pontual inflada para o check de colisão
        VoxelShape clickPointShape = Shapes.create(new AABB(localHit, localHit).inflate(0.01D));

        // Guarda quais direções (faces) foram interceptadas pelo ponto de clique
        Set<Direction> facesToRemove = EnumSet.noneOf(Direction.class);

        // Loop por todas as direções registradas no EnumMap FACES
        SpreadingLatexType.FACES.forEach((direction, faceProperty) -> {
            // Checa apenas se a propriedade atualmente existe/está ativa no bloco
            if (originState.getValue(faceProperty)) {

                // Cria um estado virtual isolado contendo APENAS a face atual
                LatexCoverState singleFaceState = originState;
                for (BooleanProperty prop : SpreadingLatexType.FACES.values()) {
                    singleFaceState = singleFaceState.setValue(prop, prop == faceProperty);
                }

                // Pega o VoxelShape apenas dessa face isolada
                VoxelShape singleFaceShape = singleFaceState.getShape(LatexCoverGetter.extendDefault(level), originPos);

                // Se o ponto clicado colide/intercepta a forma dessa face isolada
                if (Shapes.joinIsNotEmpty(singleFaceShape, clickPointShape, BooleanOp.AND)) {
                    facesToRemove.add(direction);
                }
            }
        });

        // Se nenhuma face colidiu diretamente (ex: imprecisão no clique), usa a face clicada oposta como fallback
        if (facesToRemove.isEmpty()) {
            Direction fallbackDir = context.getClickedFace().getOpposite();
            BooleanProperty fallbackProp = SpreadingLatexType.FACES.get(fallbackDir);
            if (originState.getValue(fallbackProp)) {
                facesToRemove.add(fallbackDir);
            }
        }

        // Aplica a remoção das propriedades identificadas
        if (!facesToRemove.isEmpty()) {
            LatexCoverState updatedState = originState;

            // Desativa todas as faces detectadas no estado
            for (Direction dir : facesToRemove) {
                BooleanProperty property = SpreadingLatexType.FACES.get(dir);
                updatedState = updatedState.setValue(property, false);
            }

            List<Direction> remainFaces = FoxyasUtil.getActiveFacesOfLatexCoverState(updatedState);

            if (remainFaces.isEmpty()) {
                LevelExtension levelExtension = UniversalDist.getLevelExtension(level);
                levelExtension.destroyLatexCover(level, originPos, true, context.getPlayer());
//                levelExtension.sendCoverUpdated(level, originPos, originState, ChangedLatexTypes.NONE.get().defaultCoverState(), 3);

            } else {
                // Aplica o estado atualizado no mundo
                LatexCoverState.setAtAndUpdate(level, originPos, updatedState);
            }


            // Atualiza blocos vizinhos nas direções removidas, se necessário
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = originPos.relative(direction);
                LatexCoverState neighborState = LatexCoverState.getAt(level, neighborPos);
                if (!neighborState.isAir()) {
                    LatexCoverState neighborUpdatedState = neighborState;
                    for (Direction removedFace : facesToRemove) {
                        BooleanProperty property = SpreadingLatexType.FACES.get(removedFace);
                        if (neighborState.getValue(property)) {
                            neighborUpdatedState = neighborUpdatedState.setValue(property, false);
                        }
                    }

                    List<Direction> neighborRemainFaces = FoxyasUtil.getActiveFacesOfLatexCoverState(neighborUpdatedState);
                    if (neighborRemainFaces.isEmpty()) {
                        LevelExtension levelExtension = UniversalDist.getLevelExtension(level);
                        levelExtension.destroyLatexCover(level, neighborPos, true, context.getPlayer());
                    } else {
                        LatexCoverState.setAtAndUpdate(level, neighborPos, neighborUpdatedState);
                    }
                }
            }
        }
    }

    ///
    /// @param context      Self explanatory.
    /// @param clickedPos   Self explanatory.
    /// @param clickedState Self explanatory.
    /// @return true if it applied any goo on any block.
    protected boolean applyGooOnBlock(@NotNull UseOnContext context, BlockPos clickedPos, BlockState clickedState) {
        BlockPos positionToCover = clickedState.isFaceSturdy(context.getLevel(), clickedPos, context.getClickedFace(), SupportType.FULL) ? clickedPos.relative(context.getClickedFace()) : clickedPos;
        BlockState originalState = context.getLevel().getBlockState(positionToCover);
        if (SpreadingLatexType.canExistOnSurface(context.getLevel(), positionToCover, originalState, positionToCover, originalState, context.getClickedFace())) {
            return false;
        } else {
            LatexCoverState originalCover = LatexCoverState.getAt(context.getLevel(), positionToCover);
            SpreadingLatexType spreadingLatexType = (SpreadingLatexType) this.latexType.get();
            SpreadingLatexType.CoveringBlockEvent event = new SpreadingLatexType.CoveringBlockEvent(spreadingLatexType, originalState, originalState, spreadingLatexType.spreadState(context.getLevel(), positionToCover, spreadingLatexType.sourceCoverState()), positionToCover, context.getLevel());
            spreadingLatexType.defaultCoverBehavior(event);
            if (Changed.postModEvent(event)) {
                return false;
            } else if (event.originalState == event.getPlannedState() && event.plannedCoverState == originalCover) {
                return false;
            } else {
                context.getLevel().setBlockAndUpdate(event.blockPos, event.getPlannedState());
                LatexCoverState.setAtAndUpdate(context.getLevel(), event.blockPos, event.plannedCoverState);
                SoundType soundType = event.plannedCoverState.getSoundType(context.getLevel(), event.blockPos, context.getPlayer());
                if (soundType != null) {
                    context.getLevel().playSound(context.getPlayer(), event.blockPos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                }

                event.getPostProcess().accept(context.getLevel(), positionToCover);
                return true;
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag pIsAdvanced) {
        tooltip.add(Component.literal(stack.getMaxDamage() - stack.getDamageValue() + "/" + stack.getMaxDamage() + " Uses"));
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.INFINITY_ARROWS || super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 1;
    }

    @Mod.EventBusSubscriber
    public static class Event {

        @SubscribeEvent
        public static void useOnLatexHook(LatexTypePlayerEvent.RightClick clickEvent) {
            InteractionHand hand = clickEvent.getHand();
            Player player = clickEvent.getPlayer();
            ItemStack itemInHand = player.getItemInHand(hand);
            if (itemInHand.getItem() instanceof SprayItem sprayItem) {
                BlockHitResult hitResult = clickEvent.getHitResult();
                if (!player.getCooldowns().isOnCooldown(sprayItem)) {
                    InteractionResult result = sprayItem.useOn(new UseOnContext(player, hand, hitResult));
                    if (result.shouldSwing()) player.swing(hand);
                    clickEvent.setResult(result);
                }
            }
        }
    }

    @Mod.EventBusSubscriber
    public static class OnBreak {

        @SubscribeEvent
        public static void onBreak(PlayerDestroyItemEvent event) {
            Player player = event.getEntity();
            if (player == null) return;

            ItemStack itemstack = event.getOriginal();
            if (itemstack.is(ChangedAddonItems.LITIX_CAMONIA_SPRAY.get())
                    || itemstack.is(ChangedAddonItems.WHITE_LATEX_SPRAY.get())
                    || itemstack.is(ChangedAddonItems.DARK_LATEX_SPRAY.get())) {
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ChangedAddonItems.EMPTY_SPRAY.get()));
            }
        }
    }
}
