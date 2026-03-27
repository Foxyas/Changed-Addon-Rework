package net.foxyas.changedaddon.entity.ai.advanced;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class AdvancedNodeEvaluator extends WalkNodeEvaluator {

    static Pose[] poses = {Pose.CROUCHING, Pose.SWIMMING};

    @Override
    public @NotNull BlockPathTypes getBlockPathType(@NotNull BlockGetter level, int x, int y, int z, @NotNull Mob mob) {
        BlockPathTypes type = super.getBlockPathType(level, x, y, z, mob);

        if (this.mob instanceof ChangedEntity changedMob && type == BlockPathTypes.BLOCKED) {
            BlockPos pos = new BlockPos(x, y, z);
            // Verifica se o mob cabe no bloco usando a menor pose (Crawl/Swimming)
            for (Pose pose : poses) {
                if (AdvancedGroundPathNavigation.canEntityEnterPoseIn(changedMob, pose, Vec3.atBottomCenterOf(pos))) {
                    return BlockPathTypes.WALKABLE;
                }
            }
        }
        return type;
    }

    public @NotNull Node getStart() {
        if (!(mob instanceof ChangedEntity changedEntity)) {
            return super.getStart();
        }

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int i = this.mob.getBlockY();
        BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), i, this.mob.getZ()));
        if (!this.mob.canStandOnFluid(blockstate.getFluidState())) {
            if (this.canFloat() && this.mob.isInWater()) {
                while(true) {
                    if (!blockstate.is(Blocks.WATER) && blockstate.getFluidState() != Fluids.WATER.getSource(false)) {
                        --i;
                        break;
                    }

                    ++i;
                    blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), i, this.mob.getZ()));
                }
            } else if (this.mob.onGround()) {
                i = Mth.floor(this.mob.getY() + 0.5D);
            } else {
                BlockPos blockpos;
                for(blockpos = this.mob.blockPosition(); (this.level.getBlockState(blockpos).isAir() || this.level.getBlockState(blockpos).isPathfindable(this.level, blockpos, PathComputationType.LAND)) && blockpos.getY() > this.mob.level().getMinBuildHeight(); blockpos = blockpos.below()) {
                }

                i = blockpos.above().getY();
            }
        } else {
            while(this.mob.canStandOnFluid(blockstate.getFluidState())) {
                ++i;
                blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), i, this.mob.getZ()));
            }

            --i;
        }

        BlockPos blockpos1 = this.mob.blockPosition();
        if (!this.canStartAt(blockpos$mutableblockpos.set(blockpos1.getX(), i, blockpos1.getZ()))) {
            AABB[] aabbs = Arrays.stream(poses).map(changedEntity::getBoundingBoxForPose).toArray(AABB[]::new);
            for (AABB aabb : aabbs) {
                if (this.canStartAt(blockpos$mutableblockpos.set(aabb.minX, i, aabb.minZ)) || this.canStartAt(blockpos$mutableblockpos.set(aabb.minX, i, aabb.maxZ)) || this.canStartAt(blockpos$mutableblockpos.set(aabb.maxX, i, aabb.minZ)) || this.canStartAt(blockpos$mutableblockpos.set(aabb.maxX, i, aabb.maxZ))) {
                    return this.getStartNode(blockpos$mutableblockpos);
                }
            }


        }

        return this.getStartNode(new BlockPos(blockpos1.getX(), i, blockpos1.getZ()));
    }

    @Override
    @Nullable
    protected Node findAcceptedNode(int x, int y, int z, int verticalDeltaLimit, double nodeFloorLevel, @NotNull Direction direction, @NotNull BlockPathTypes pathType) {
        Node node = super.findAcceptedNode(x, y, z, verticalDeltaLimit, nodeFloorLevel, direction, pathType);

        // Se o super retornou null (bloqueado), tentamos validar manualmente para o "crawl"
        if (node == null && this.mob instanceof ChangedEntity changedMob) {
            BlockPos pos = new BlockPos(x, y, z);
            BlockPathTypes typeAtPos = this.getCachedBlockType(this.mob, x, y, z);

            // Se o bloco é fisicamente passável com rastejo
            if (AdvancedGroundPathNavigation.canEntityEnterPoseIn(changedMob, Pose.SWIMMING, Vec3.atBottomCenterOf(pos))) {
                node = this.getNode(x, y, z);
                node.type = typeAtPos;
                // Aumentamos o custo (malus) para rastejar, assim ele prefere caminhos abertos
                node.costMalus = Math.max(node.costMalus, this.mob.getPathfindingMalus(typeAtPos) + 1.5F);
            }
        }

        return node;
    }

    @Override
    public boolean canReachWithoutCollision(@NotNull Node node) {
        // Redefine a verificação de colisão final do pathfinding
        if (this.mob instanceof ChangedEntity changedMob) {
            Vec3 target = Vec3.atBottomCenterOf(node.asBlockPos());
            // Se ele cabe em qualquer pose (Standing, Crouching ou Swimming), o caminho é válido
            for (Pose pose : poses) {
                if (AdvancedGroundPathNavigation.canEntityEnterPoseIn(changedMob, pose, target)) {
                    return true;
                }
            }
        }
        return super.canReachWithoutCollision(node);
    }
}