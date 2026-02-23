package net.foxyas.changedaddon.util;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class VoxelShapeCache {

    private final HashMap<Pair<Direction, Integer>, VoxelShape> shapes = new HashMap<>();

    public static VoxelShape orUnoptimized(VoxelShape shape1, VoxelShape shape2) {
        return Shapes.joinUnoptimized(shape1, shape2, BooleanOp.OR);
    }

    public static @NotNull VoxelShape rotateShape(Direction direction, VoxelShape source) {
        AtomicReference<VoxelShape> newShape = new AtomicReference<>(Shapes.empty());
        Vec3i normal = direction.getNormal();
        Vector3f min = new Vector3f(), max = new Vector3f();
        source.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            min.set(minX - 0.5, minY - 0.5, minZ - 0.5);
            max.set(maxX - 0.5, maxY - 0.5, maxZ - 0.5);

            min.set(min.x * -normal.getZ() + min.z * -normal.getX(), min.y, min.z * -normal.getZ() + min.x * normal.getX());
            max.set(max.x * -normal.getZ() + max.z * -normal.getX(), max.y, max.z * -normal.getZ() + max.x * normal.getX());

            VoxelShape s = Shapes.create(0.5 + Math.min(min.x, max.x), 0.5 + Math.min(min.y, max.y), 0.5 + Math.min(min.z, max.z),
                    0.5 + Math.max(min.x, max.x), 0.5 + Math.max(min.y, max.y), 0.5 + Math.max(min.z, max.z));
            newShape.set(orUnoptimized(newShape.get(), s));
        });
        return newShape.get();
    }

    public VoxelShape getShape(Direction direction, int id, VoxelShape baseShape) {
        return shapes.computeIfAbsent(Pair.of(direction, id), pair ->
                direction != Direction.NORTH ? rotateShape(direction, baseShape) : baseShape);
    }

    public VoxelShape getShape(Direction direction, int id, Supplier<VoxelShape> supplier) {
        return shapes.computeIfAbsent(Pair.of(direction, id), pair ->
                direction != Direction.NORTH ? rotateShape(direction, supplier.get()) : supplier.get());
    }
}