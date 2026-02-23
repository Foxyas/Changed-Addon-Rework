package net.foxyas.changedaddon.block.advanced;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.ltxprogrammer.changed.block.KeypadBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TimedKeypadBlock extends KeypadBlock {

    public static final VoxelShape BUTTON_LEFT = Block.box(
            12, 3, 15,
            13, 4, 15.2
    );

    public static final VoxelShape BUTTON_CENTER = Block.box(
            13, 3, 15,
            14, 4, 15.2
    );

    public static final VoxelShape BUTTON_RIGHT = Block.box(
            14, 3, 15,
            15, 4, 15.2
    );


    public static final VoxelShape EXTRA_BUTTONS = Shapes.or(
            BUTTON_LEFT,
            BUTTON_CENTER,
            BUTTON_RIGHT
    );

    public TimedKeypadBlock() {
        super();
        drops = BuiltInLootTables.EMPTY;
    }

    public static boolean isNearBounds(AABB bound, Vec3 pos, double tolerance) {
        boolean nearX =
                Math.abs(pos.x - bound.minX) <= tolerance ||
                        Math.abs(pos.x - bound.maxX) <= tolerance;

        boolean nearY =
                Math.abs(pos.y - bound.minY) <= tolerance ||
                        Math.abs(pos.y - bound.maxY) <= tolerance;

        boolean nearZ =
                Math.abs(pos.z - bound.minZ) <= tolerance ||
                        Math.abs(pos.z - bound.maxZ) <= tolerance;

        // TODOS os eixos devem estar perto
        return nearX && nearY && nearZ;
    }

    public static boolean isDistanceAround3D(double x1, double y1, double z1,
                                             double x2, double y2, double z2,
                                             double target, double tolerance) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;

        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        return Math.abs(dist - target) <= tolerance;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TimedKeypadBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(BlockState blockState, BlockGetter level, BlockPos blockPos) {
        VoxelShape main = calculateShapes(blockState.getValue(FACING), SHAPE_WHOLE);
        VoxelShape buttons = calculateShapes(blockState.getValue(FACING).getClockWise(), Shapes.or(BUTTON_CENTER, BUTTON_LEFT, BUTTON_RIGHT));
        return Shapes.or(main, buttons);
    }

    public @NotNull VoxelShape getOcclusionShape(BlockState blockState, BlockGetter level, BlockPos blockPos) {
        return this.getInteractionShape(blockState, level, blockPos);
    }

    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return this.getInteractionShape(blockState, level, blockPos);
    }

    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return this.getInteractionShape(blockState, level, blockPos);
    }

    public VoxelShape getButtonsInteractionShape(KeypadButton button, BlockState blockState) {
        return calculateShapes(
                blockState.getValue(FACING).getClockWise(),
                switch (button) {
                    case LEFT -> BUTTON_LEFT;
                    case CENTER -> BUTTON_CENTER;
                    case RIGHT -> BUTTON_RIGHT;
                }
        );
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (!state.getValue(KeypadBlock.POWERED)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof TimedKeypadBlockEntity keypad)) {
                return InteractionResult.PASS;
            }
            Vec3 location = hitResult.getLocation();
            Vec3 localLocation = location.subtract(pos.getX(), pos.getY(), pos.getZ());

            for (KeypadButton keypadButton : KeypadButton.values()) {
                VoxelShape interactionShape = getButtonsInteractionShape(keypadButton, state).move(0, 0, 0);

                if (interactionShape.bounds().inflate(0.01, 0.01, 0.0005).contains(localLocation.scale(1))) {
                    if (!level.isClientSide()) {
                        switch (keypadButton) {
                            case LEFT -> {
                                keypad.addTimer(1);
                                keypad.playTimerAdjust(true);
                            }
                            case RIGHT -> {
                                keypad.addTimer(-1);
                                keypad.playTimerAdjust(false);
                            }
                            case CENTER -> {
                                if (keypad.getTimer() > 0) {
                                    keypad.setTimer(0);
                                    keypad.playTimerAdjust(true);
                                }
                            }
                            default -> {
                                ChangedAddonMod.LOGGER.error("Some wierd stuff happen that broke the KeypadButton Enum, How that even happened? we don't know but it cause this trigger to be called");
                            }
                        }
                    }
                    return InteractionResult.SUCCESS;
                }

            }

            if (player instanceof ServerPlayer serverPlayer && serverPlayer.isShiftKeyDown()) {
                NetworkHooks.openScreen(serverPlayer, keypad.getMenuProvider(state, level, pos), keypad.getBlockPos());
                return InteractionResult.SUCCESS;
            }

            return super.use(state, level, pos, player, hand, hitResult);
        }

        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        CompoundTag tag = stack.getOrCreateTag();
        if (!level.isClientSide && stack.hasTag() && tag.contains("TimerValue")) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TimedKeypadBlockEntity keypad) {
                keypad.addTimer(tag.getInt("TimerValue"));
                keypad.setCanTick(true);
                keypad.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
            }
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(
                type,
                ChangedAddonBlockEntities.TIMED_KEYPAD_BLOCK_ENTITY.get(), // Certifique-se que esse seja o registry correto
                (lvl, pos, blockState, be) -> {
                    be.tick(lvl, pos);
                    lvl.sendBlockUpdated(pos, blockState, blockState, 3);
                }
        );
    }

    // pixelX, pixelY, pixelZ vão de 0 a 15 (inclusive)
    private boolean isInsidePixel(Vec3 relative, int px, int py, int pz) {
        final float pixelSize = 1.0f / 16.0f;
        float minX = px * pixelSize;
        float maxX = minX + pixelSize;

        float minY = py * pixelSize;
        float maxY = minY + pixelSize;

        float minZ = pz * pixelSize;
        float maxZ = minZ + pixelSize;

        return relative.x >= minX && relative.x < maxX &&
                relative.y >= minY && relative.y < maxY &&
                relative.z >= minZ && relative.z < maxZ;
    }

    private boolean isInside(Vec3 rel, double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        return rel.x >= minX && rel.x < maxX &&
                rel.y >= minY && rel.y < maxY &&
                rel.z >= minZ && rel.z < maxZ;
    }

    private boolean isInsideWithDirection(Vec3 rel, Direction direction, double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            return isInside(rel, minX, maxX, minY, maxY, minZ, maxZ);
        } else if (direction == Direction.WEST || direction == Direction.EAST) {
            return isInside(rel, minZ, maxZ, minY, maxY, minX, maxX);
        }

        return isInside(rel, minX, maxX, minY, maxY, minZ, maxZ);
    }


    public enum KeypadButton {
        LEFT,
        CENTER,
        RIGHT
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT)
    public static class ClientEvent {

        @SubscribeEvent
        public static void RenderOutlineButton(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS) return;

            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (mc.level == null || player == null) return;

            Level level = mc.level;


            // Cam pos — important
            Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();

            BlockHitResult blockHitResult = level.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getViewVector(0).scale(player.getBlockReach())), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            if (blockHitResult.getType() != HitResult.Type.MISS) {
                BlockPos pos = blockHitResult.getBlockPos();
                BlockState state = level.getBlockState(pos);
                if (state.getBlock() instanceof TimedKeypadBlock keypad) {
                    Vec3 location = blockHitResult.getLocation();
                    Vec3 localLocation = location.subtract(pos.getX(), pos.getY(), pos.getZ());

                    for (TimedKeypadBlock.KeypadButton btn : TimedKeypadBlock.KeypadButton.values()) {
                        VoxelShape shape = keypad.getButtonsInteractionShape(btn, state);
                        // Convert each AABB from the shape
                        boolean renderAnButton = false;
                        for (AABB aabb : shape.toAabbs()) {
                            // Move it to the world
                            AABB worldAABB = aabb.move(pos);

                            // world-space → camera-space
                            AABB renderAABB = worldAABB.move(-cam.x, -cam.y, -cam.z).inflate(0.00125);

                            if (aabb.inflate(0.01, 0.01, 0.0005).contains(localLocation.scale(1))) {
                                // Render Box (Lines)
                                LevelRenderer.renderLineBox(
                                        event.getPoseStack(),
                                        mc.renderBuffers().bufferSource().getBuffer(RenderType.lines()),
                                        renderAABB,
                                        1f, 1f, 1f, 1f     // R, G, B, Alpha
                                );
                                renderAnButton = true;
                            }
                        }
                        if (renderAnButton) break;
                    }
                }
            }
        }

    }
}