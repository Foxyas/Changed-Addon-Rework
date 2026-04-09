package net.foxyas.changedaddon.util;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

// CREDITS TO LTX. THIS CODE IS JUST A BACKPORT OF HIS.
public class LevelUtil {
    public static boolean isTouchingShape(BlockPos blockPos, VoxelShape shape, Entity entity) {
        VoxelShape positionedShape = shape.move(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (Shapes.joinIsNotEmpty(positionedShape, Shapes.create(entity.getBoundingBox()), BooleanOp.AND))
            return true;

        if (entity.verticalCollision) {
            VoxelShape positionedShapeU = shape.move(blockPos.getX(), (double)blockPos.getY() + 0.025, blockPos.getZ());
            if (Shapes.joinIsNotEmpty(positionedShapeU, Shapes.create(entity.getBoundingBox()), BooleanOp.AND))
                return true;
            VoxelShape positionedShapeD = shape.move(blockPos.getX(), (double)blockPos.getY() - 0.025, blockPos.getZ());
            if (Shapes.joinIsNotEmpty(positionedShapeD, Shapes.create(entity.getBoundingBox()), BooleanOp.AND))
                return true;
        }

        if (entity.horizontalCollision) {
            VoxelShape positionedShapeN = shape.move(blockPos.getX(), blockPos.getY(), (double)blockPos.getZ() + 0.025);
            if (Shapes.joinIsNotEmpty(positionedShapeN, Shapes.create(entity.getBoundingBox()), BooleanOp.AND))
                return true;
            VoxelShape positionedShapeE = shape.move((double)blockPos.getX() + 0.025, blockPos.getY(), blockPos.getZ());
            if (Shapes.joinIsNotEmpty(positionedShapeE, Shapes.create(entity.getBoundingBox()), BooleanOp.AND))
                return true;
            VoxelShape positionedShapeS = shape.move(blockPos.getX(), blockPos.getY(), (double)blockPos.getZ() - 0.025);
            if (Shapes.joinIsNotEmpty(positionedShapeS, Shapes.create(entity.getBoundingBox()), BooleanOp.AND))
                return true;
            VoxelShape positionedShapeW = shape.move((double)blockPos.getX() - 0.025, blockPos.getY(), blockPos.getZ());
            return Shapes.joinIsNotEmpty(positionedShapeW, Shapes.create(entity.getBoundingBox()), BooleanOp.AND);
        }

        return false;
    }

    public static boolean isTouchingBlockCollision(BlockGetter level, BlockPos blockPos, BlockState blockState, Entity entity) {
        VoxelShape collisionShape = blockState.getCollisionShape(level, blockPos, CollisionContext.of(entity));
        return isTouchingShape(blockPos, collisionShape, entity);
    }

    public static boolean isTouchingBlockInteraction(BlockGetter level, BlockPos blockPos, BlockState blockState, Entity entity) {
        VoxelShape interactionShape = blockState.getInteractionShape(level, blockPos);
        return isTouchingShape(blockPos, interactionShape, entity);
    }
}