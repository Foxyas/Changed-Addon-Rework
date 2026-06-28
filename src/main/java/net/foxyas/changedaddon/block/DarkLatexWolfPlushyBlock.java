package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.block.entity.DarkLatexWolfPlushyBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonCriteriaTriggers;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.util.DynamicClipContext;
import net.foxyas.changedaddon.util.FoxyasUtils;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DarkLatexWolfPlushyBlock extends AbstractPlushyBlock {

    public DarkLatexWolfPlushyBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).sound(SoundType.WOOL)
                .strength(0.5f, 5f)
                .noOcclusion()
                .isRedstoneConductor((bs, br, bp) -> false));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new DarkLatexWolfPlushyBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return super.getTicker(pLevel, pState, pBlockEntityType);
    }

    @Mod.EventBusSubscriber
    public static class EventHandle {

        public static final ClipContext.ShapeGetter IGNORE_BED = (state, b, pos, context) -> {
            if (state.getBlock() instanceof BedBlock) return Shapes.empty();
            return ClipContext.Block.COLLIDER.get(state, b, pos, context);
        };

        public static TransfurVariant<?> getTransfurVariant(Level world) {
            List<TransfurVariant<?>> list = new ArrayList<>();
            list.add(ChangedTransfurVariants.DARK_LATEX_WOLF_MALE.get());
            list.add(ChangedTransfurVariants.DARK_LATEX_WOLF_FEMALE.get());
            list.add(ChangedAddonTransfurVariants.PURO_KIND_MALE.get());
            list.add(ChangedAddonTransfurVariants.PURO_KIND_FEMALE.get());

            return Util.getRandom(list, world.getRandom());
        }

        @SubscribeEvent
        public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
            if (event.getEntity().level().isClientSide) return;

            Player player = event.getEntity();
            Level world = player.level();
            RandomSource random = player.getRandom();
            BlockPos playerBlockPos = player.blockPosition();
            Stream<BlockPos> posStream = FoxyasUtils.betweenClosedStreamSphere(playerBlockPos, 3, 2);
            Vec3 position = player.getEyePosition();
            float intensity = 1 + (player.getInventory().items.stream().filter((itemStack -> itemStack.is(ChangedAddonItems.DARK_LATEX_WOLF_PLUSH.get()))).count() / 100f);
            for (BlockPos plushyPos : posStream.filter((pos) -> world.getBlockState(pos).is(ChangedAddonBlocks.DARK_LATEX_WOLF_PLUSHY.get())).toList()) {
                boolean canSeePlayer = canPlushySeePlayer(player, Vec3.atCenterOf(plushyPos), 360);
                if (!canSeePlayer || event.updateLevel() || ProcessTransfur.isPlayerTransfurred(player)) continue;

                float randomValue = random.nextFloat();
                float luck = player.getLuck() / 100f;
                double distance = (position.distanceTo(Vec3.atCenterOf(plushyPos)));
                float value = (float) ((0.25f + luck) / (distance * intensity));
                boolean plushyHead = player.getItemBySlot(EquipmentSlot.HEAD).is(ChangedAddonItems.DARK_LATEX_WOLF_PLUSH.get());
                if (plushyHead) value = 1;
                if (randomValue <= value) {
                    ProcessTransfur.transfur(player, world, getTransfurVariant(world), true, TransfurContext.hazard(TransfurCause.FACE_HAZARD));
                    if (player instanceof ServerPlayer serverPlayer) {
                        ChangedAddonCriteriaTriggers.SLEEP_NEXT_A_PLUSHY_TRIGGER.trigger(serverPlayer, ProcessTransfur.getPlayerTransfurVariant(serverPlayer), "dark_latex_plushy", true);
                    }
                    break;
                }
            }
        }

        /**
         * Checks if one entity (targetEntity) can be seen by (plushyPos), using rayCasting and FOV.
         *
         * @param targetEntity The entity doing the looking.
         * @param plushyPos    The target pos to be looked at.
         * @param fovDegrees   Field of view angle in degrees (e.g., 90 means 45 degrees to each side).
         * @return true if visible and within FOV, false otherwise.
         */
        public static boolean canPlushySeePlayer(LivingEntity targetEntity, Vec3 plushyPos, double fovDegrees) {
            Level level = targetEntity.level();
            Vec3 from = targetEntity.getEyePosition(1.0F);

            // First, check field of view using dot product
            Vec3 lookVec = targetEntity.getLookAngle().normalize();
            Vec3 directionToTarget = plushyPos.subtract(from).normalize();

            double dot = lookVec.dot(directionToTarget);
            double requiredDot = Math.cos(Math.toRadians(fovDegrees / 2.0));
            if (dot < requiredDot)
                return false; // Outside of FOV

            // Then, raycast from eyeEntity to targetToSee to check if the view is blocked
            HitResult result = level.clip(new DynamicClipContext(from, plushyPos, IGNORE_BED,
                    ClipContext.Fluid.NONE::canPick, CollisionContext.of(targetEntity)));

            // If result is MISS or hit point is very close to target, it's considered visible
            return result.getType() == HitResult.Type.MISS ||
                    result.getLocation().distanceToSqr(plushyPos) < 1.0;
        }
    }
}
