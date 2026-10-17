package net.foxyas.changedaddon.util;

import net.ltxprogrammer.changed.entity.latex.SpreadingLatexType;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.world.LatexCoverGetter;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;
import java.util.stream.Collectors;

public class GasAreaUtil {

    public static List<Vec3> sampleGasCone(
            Player player,
            double range,
            double spread,
            double step
    ) {
        List<Vec3> points = new ArrayList<>();

        Vec3 from = player.getEyePosition(1.0F);
        Vec3 look = player.getLookAngle().normalize();
        Vec3 right = look.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 up = right.cross(look).normalize();

        for (double d = 0; d <= range; d += step) {
            double radius = d * spread;

            for (int i = 0; i < 6; i++) { // densidade lateral
                double angle = (Math.PI * 2 / 6) * i;

                Vec3 offset =
                        right.scale(Math.cos(angle) * radius)
                                .add(up.scale(Math.sin(angle) * radius));

                points.add(from.add(look.scale(d)).add(offset));
            }
        }

        return points;
    }

    /**
     * Checks the full shape and returns ALL faces intersected by the hit vector.
     */
    public static Set<Direction> getIntersectingFaces(Level level, BlockPos pos, LatexCoverState state, Vec3 hitVec, Direction fallback) {
        Set<Direction> intersectingFaces = EnumSet.noneOf(Direction.class);
        Vec3 localHit = hitVec.subtract(pos.getX(), pos.getY(), pos.getZ());
        VoxelShape clickPointShape = Shapes.create(new AABB(localHit, localHit).inflate(0.01D));
        LatexCoverGetter getter = LatexCoverGetter.extendDefault(level);

        for (Map.Entry<Direction, BooleanProperty> entry : SpreadingLatexType.FACES.entrySet()) {
            Direction direction = entry.getKey();
            BooleanProperty faceProperty = entry.getValue();

            if (state.hasProperty(faceProperty) && state.getValue(faceProperty)) {
                LatexCoverState singleFaceState = state;
                for (BooleanProperty prop : SpreadingLatexType.FACES.values()) {
                    if (singleFaceState.hasProperty(prop)) {
                        singleFaceState = singleFaceState.setValue(prop, prop == faceProperty);
                    }
                }
                VoxelShape singleFaceShape = singleFaceState.getShape(getter, pos);
                if (Shapes.joinIsNotEmpty(singleFaceShape, clickPointShape, BooleanOp.AND)) {
                    intersectingFaces.add(direction);
                }
            }
        }

        // Fallback if precision fails to catch an exact bound
        if (intersectingFaces.isEmpty() && fallback != null) {
            intersectingFaces.add(fallback);
        }

        return intersectingFaces;
    }

    public static List<GasHit> getGasConeHits(
            Level level,
            Entity entity,
            double range,
            double spread,
            int density
    ) {
        LatexCoverGetter getter = LatexCoverGetter.extendDefault(level);

        Vec3 from = entity.getEyePosition(1.0F);
        Vec3 look = entity.getLookAngle().normalize();

        Vec3 right = look.cross(new Vec3(0, 1, 0)).normalize();
        if (right.lengthSqr() < 1e-4) {
            right = new Vec3(1, 0, 0); // fallback
        }
        Vec3 up = right.cross(look).normalize();

        List<GasHit> hits = new ArrayList<>();

        for (int x = -density; x <= density; x++) {
            for (int y = -density; y <= density; y++) {
                Vec3 offset = right.scale(x * spread)
                        .add(up.scale(y * spread));

                Vec3 to = from.add(look.scale(range)).add(offset);

                ClipContext context = new ClipContext(
                        from,
                        to,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
                        entity
                );

                BlockHitResult hit = getter.clip(context);
                if (hit.getType() != BlockHitResult.Type.BLOCK)
                    continue;

                BlockPos pos = hit.getBlockPos();
                LatexCoverState state = getter.getLatexCover(pos);

                if (state.isPresent()) {
                    Set<Direction> faces = getIntersectingFaces(level, pos, state, hit.getLocation(), hit.getDirection().getOpposite());
                    hits.add(new GasHit(hit, state, faces));
                }
            }
        }

        return hits;
    }

    public static List<GasHitBlock> getGasConeHitsNormalBlocks(
            Level level,
            Entity entity,
            double range,
            double spread,
            int density
    ) {

        Vec3 from = entity.getEyePosition(1.0F);
        Vec3 look = entity.getLookAngle().normalize();

        Vec3 right = look.cross(new Vec3(0, 1, 0)).normalize();
        if (right.lengthSqr() < 1e-4) {
            right = new Vec3(1, 0, 0); // fallback
        }
        Vec3 up = right.cross(look).normalize();

        Set<GasHitBlock> hits = new HashSet<>();

        for (int x = -density; x <= density; x++) {
            for (int y = -density; y <= density; y++) {
                Vec3 offset = right.scale(x * spread)
                        .add(up.scale(y * spread));

                Vec3 to = from.add(look.scale(range)).add(offset);

                ClipContext context = new ClipContext(
                        from,
                        to,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
                        entity
                );

                BlockHitResult hit = level.clip(context);
                if (hit.getType() != BlockHitResult.Type.BLOCK)
                    continue;

                BlockPos pos = hit.getBlockPos();
                BlockState state = level.getBlockState(pos);

                if (!state.isAir()) {
                    hits.add(new GasHitBlock(hit, state));
                }
            }
        }

        return hits.stream().toList();
    }

    public static List<GasHit> getGasVolumeHits(
            Level level,
            Entity entity,
            double range,
            double radius
    ) {
        LatexCoverGetter getter = LatexCoverGetter.extendDefault(level);

        Vec3 from = entity.getEyePosition(1.0F);
        Vec3 look = entity.getLookAngle().normalize();

        List<GasHit> hits = new ArrayList<>();

        for (double d = 0.5; d <= range; d += 0.5) {
            Vec3 center = from.add(look.scale(d));
            BlockPos centerPos = BlockPos.containing(center);

            BlockPos.betweenClosed(
                    centerPos.offset((int) -radius, (int) -radius, (int) -radius),
                    centerPos.offset((int) radius, (int) radius, (int) radius)
            ).forEach(pos -> {
                LatexCoverState state = getter.getLatexCover(pos);
                if (!state.isPresent())
                    return;

                VoxelShape shape = state.getInteractionShape(getter, pos);
                if (shape.isEmpty())
                    return;

                if (shape.bounds().move(pos).contains(center)) {
                    Direction fallbackFace = Direction.getNearest(
                            look.x, look.y, look.z
                    );
                    Set<Direction> faces = getIntersectingFaces(level, pos, state, center, fallbackFace);

                    BlockHitResult refinedHit = new BlockHitResult(center, fallbackFace, pos, true);
                    hits.add(new GasHit(refinedHit, state, faces));
                }
            });
        }

        return hits;
    }

    public static void clearLatexFaces(Level level, GasHit gasHit) {
        if (level.isClientSide)
            return;

        BlockPos pos = gasHit.pos();
        LatexCoverState oldState = LatexCoverState.getAt(level, pos);
        if (oldState.isAir()) return;

        LatexCoverState newState = oldState;
        for (Direction face : gasHit.faces()) {
            newState = newState.updateShape(
                    face,
                    ChangedLatexTypes.NONE.get().defaultCoverState(),
                    level,
                    pos,
                    pos.relative(face)
            );
        }

        LatexCoverState.setAtAndUpdate(level, pos, newState);
    }

    public static int getLatexStateFaceMask(LatexCoverState state) {
        int mask = 0;
        for (Map.Entry<Direction, BooleanProperty> entry : SpreadingLatexType.FACES.entrySet()) {
            if (state.hasProperty(entry.getValue()) && state.getValue(entry.getValue())) {
                mask |= (1 << entry.getKey().ordinal());
            }
        }
        return mask;
    }

    public static List<GasHit> dedupe(List<GasHit> hits) {
        return hits.stream()
                .collect(Collectors.toMap(
                        h -> {
                            long posKey = h.pos().asLong();
                            int faceMask = getLatexStateFaceMask(h.state());
                            return posKey ^ ((long) faceMask << 32);
                        },
                        h -> h,
                        (a, b) -> a
                ))
                .values()
                .stream()
                .toList();
    }

    public record GasHit(BlockHitResult hitResult, LatexCoverState state, Set<Direction> faces) {
        public BlockPos pos() {
            return hitResult.getBlockPos();
        }
    }

    public record GasHitBlock(BlockHitResult hitResult, BlockState state) {
        public BlockPos pos() {
            return hitResult.getBlockPos();
        }

        public Direction face() {
            return hitResult.getDirection();
        }
    }
}