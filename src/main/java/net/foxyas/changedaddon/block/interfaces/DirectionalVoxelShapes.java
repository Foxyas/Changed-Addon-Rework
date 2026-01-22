package net.foxyas.changedaddon.block.interfaces;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public final class DirectionalVoxelShapes {

    private DirectionalVoxelShapes() {}

    /**
     * Cria VoxelShapes rotacionados a partir de um shape base (assumido como UP).
     */
    public static @NotNull Map<Direction, VoxelShape> create(
            VoxelShape baseShape,
            EnumSet<Direction> directions
    ) {
        EnumMap<Direction, VoxelShape> map = new EnumMap<>(Direction.class);

        for (Direction dir : directions) {
            map.put(dir, rotate(baseShape, dir));
        }

        return map;
    }

    /**
     * Atalho: gera para todas as direções.
     */
    public static @NotNull Map<Direction, VoxelShape> createAll(VoxelShape baseShape) {
        return create(baseShape, EnumSet.allOf(Direction.class));
    }

    /**
     * Rotação matemática real, sem alterar o shape base.
     * Assume que o shape base aponta para UP.
     */
    private static @NotNull VoxelShape rotate(VoxelShape source, Direction direction) {
        if (direction == Direction.UP) {
            return source;
        }

        AtomicReference<VoxelShape> result = new AtomicReference<>(Shapes.empty());

        source.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {

            // centraliza em 0
            double x1 = minX - 0.5;
            double y1 = minY - 0.5;
            double z1 = minZ - 0.5;
            double x2 = maxX - 0.5;
            double y2 = maxY - 0.5;
            double z2 = maxZ - 0.5;

            double[] min;
            double[] max;

            switch (direction) {
                case DOWN -> {
                    min = new double[]{x1, -y2, z1};
                    max = new double[]{x2, -y1, z2};
                }
                case NORTH -> {
                    min = new double[]{x1, z1, -y2};
                    max = new double[]{x2, z2, -y1};
                }
                case SOUTH -> {
                    min = new double[]{x1, -z2, y1};
                    max = new double[]{x2, -z1, y2};
                }
                case WEST -> {
                    min = new double[]{y1, x1, z1};
                    max = new double[]{y2, x2, z2};
                }
                case EAST -> {
                    min = new double[]{-y2, x1, z1};
                    max = new double[]{-y1, x2, z2};
                }
                default -> {
                    min = new double[]{x1, y1, z1};
                    max = new double[]{x2, y2, z2};
                }
            }

            VoxelShape box = Shapes.create(
                    0.5 + Math.min(min[0], max[0]),
                    0.5 + Math.min(min[1], max[1]),
                    0.5 + Math.min(min[2], max[2]),
                    0.5 + Math.max(min[0], max[0]),
                    0.5 + Math.max(min[1], max[1]),
                    0.5 + Math.max(min[2], max[2])
            );

            result.set(Shapes.or(result.get(), box));
        });

        return result.get();
    }
}
