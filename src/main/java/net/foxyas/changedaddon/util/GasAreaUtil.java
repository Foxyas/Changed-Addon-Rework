package net.foxyas.changedaddon.util;

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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GasAreaUtil {

    public record GasHit(BlockPos pos, Direction face, LatexCoverState state) {}
    public record GasHitBlock(BlockPos pos, Direction face, BlockState state) {}

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
                Direction face = hit.getDirection();
                LatexCoverState state = getter.getLatexCover(pos);

                if (state.isPresent()) {
                    hits.add(new GasHit(pos, face, state));
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
                Direction face = hit.getDirection();
                BlockState state = level.getBlockState(pos);

                if (!state.isAir()) {
                    hits.add(new GasHitBlock(pos, face, state));
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
                    Direction face = Direction.getNearest(
                            look.x, look.y, look.z
                    );

                    hits.add(new GasHit(pos, face, state));
                }
            });
        }

        return hits;
    }

    public static void clearLatexFace(Level level, GasHit hit) {
        if (level.isClientSide)
            return;

        LatexCoverState oldState = LatexCoverState.getAt(level, hit.pos());

        LatexCoverState newState = oldState.updateShape(
                hit.face(),
                ChangedLatexTypes.NONE.get().defaultCoverState(),
                level,
                hit.pos(),
                hit.pos().relative(hit.face())
        );

        LatexCoverState.setAtAndUpdate(level, hit.pos(), newState);
    }

    public static List<GasHit> dedupe(List<GasHit> hits) {
        return hits.stream()
                .collect(Collectors.toMap(
                        h -> h.pos().asLong() ^ h.face().ordinal(),
                        h -> h,
                        (a, b) -> a
                ))
                .values()
                .stream()
                .toList();
    }
}
