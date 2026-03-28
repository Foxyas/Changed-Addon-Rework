package net.foxyas.changedaddon.entity.ai.advanced;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
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
    public void prepare(PathNavigationRegion pLevel, Mob pMob) {
        super.prepare(pLevel, pMob);
        this.entityHeight /= 2;
    }

    @Override
    public @NotNull BlockPathTypes getBlockPathType(@NotNull BlockGetter level, int x, int y, int z, @NotNull Mob mob) {
        BlockPathTypes type = super.getBlockPathType(level, x, y, z, mob);

//        if (this.mob instanceof ChangedEntity changedMob && type == BlockPathTypes.BLOCKED) {
//            BlockPos pos = new BlockPos(x, y, z);
//
//            for (Pose pose : poses) {
//                if (AdvancedGroundPathNavigation.canEntityEnterPoseIn(changedMob, pose, Vec3.atBottomCenterOf(pos))) {
//                    return BlockPathTypes.WALKABLE;
//                }
//            }
//        }
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
                while (true) {
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
                for (blockpos = this.mob.blockPosition(); (this.level.getBlockState(blockpos).isAir() || this.level.getBlockState(blockpos).isPathfindable(this.level, blockpos, PathComputationType.LAND)) && blockpos.getY() > this.mob.level().getMinBuildHeight(); blockpos = blockpos.below()) {
                }

                i = blockpos.above().getY();
            }
        } else {
            while (this.mob.canStandOnFluid(blockstate.getFluidState())) {
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

    protected boolean isDiagonalValid(@NotNull Node pRoot, @Nullable Node pXNode, @Nullable Node pZNode, @Nullable Node pDiagonal) {
        if (pDiagonal != null && pZNode != null && pXNode != null) {
            if (pDiagonal.closed) {
                return false;
            } else if (pZNode.y <= pRoot.y && pXNode.y <= pRoot.y) {
                if (pXNode.type != BlockPathTypes.WALKABLE_DOOR && pZNode.type != BlockPathTypes.WALKABLE_DOOR && pDiagonal.type != BlockPathTypes.WALKABLE_DOOR) {
                    boolean widthFlag = Arrays.stream(poses).map(mob::getDimensions).anyMatch(entityDimensions -> entityDimensions.width < 0.5D);
                    boolean flag = pZNode.type == BlockPathTypes.FENCE && pXNode.type == BlockPathTypes.FENCE && widthFlag;
                    return pDiagonal.costMalus >= 0.0F && (pZNode.y < pRoot.y || pZNode.costMalus >= 0.0F || flag) && (pXNode.y < pRoot.y || pXNode.costMalus >= 0.0F || flag);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    @Nullable
    protected Node findAcceptedNode(int pX, int pY, int pZ, int pVerticalDeltaLimit, double pNodeFloorLevel, @NotNull Direction pDirection, @NotNull BlockPathTypes pPathType) {
        if (!(mob instanceof ChangedEntity changedEntity)) {
            return super.findAcceptedNode(pX, pY, pZ, pVerticalDeltaLimit, pNodeFloorLevel, pDirection, pPathType);
        }

        Node node = null;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        double d0 = this.getFloorLevel(blockpos$mutableblockpos.set(pX, pY, pZ));
        if (d0 - pNodeFloorLevel > this.getMobJumpHeight()) {
            return null;
        } else {
            BlockPathTypes blockpathtypes = this.getCachedBlockType(this.mob, pX, pY, pZ);
            float f = this.mob.getPathfindingMalus(blockpathtypes);
            double d1 = (double) this.mob.getBbWidth() / 2.0D;
            if (f >= 0.0F) {
                node = this.getNodeAndUpdateCostToMax(pX, pY, pZ, blockpathtypes, f);
            }

            if (doesBlockHavePartialCollision(pPathType) && node != null && node.costMalus >= 0.0F && !this.canReachWithoutCollision(node)) {
                node = null;
            }

            if (blockpathtypes != BlockPathTypes.WALKABLE && (!this.isAmphibious() || blockpathtypes != BlockPathTypes.WATER)) {
                if ((node == null || node.costMalus < 0.0F) && pVerticalDeltaLimit > 0 && (blockpathtypes != BlockPathTypes.FENCE || this.canWalkOverFences()) && blockpathtypes != BlockPathTypes.UNPASSABLE_RAIL && blockpathtypes != BlockPathTypes.TRAPDOOR && blockpathtypes != BlockPathTypes.POWDER_SNOW) {
                    node = this.findAcceptedNode(pX, pY + 1, pZ, pVerticalDeltaLimit - 1, pNodeFloorLevel, pDirection, pPathType);
                    boolean widthFlag = Arrays.stream(poses).map(mob::getDimensions).anyMatch(entityDimensions -> entityDimensions.width < 1f);
                    if (node != null && (node.type == BlockPathTypes.OPEN || node.type == BlockPathTypes.WALKABLE) && widthFlag) {
                        double d2 = (double) (pX - pDirection.getStepX()) + 0.5D;
                        double d3 = (double) (pZ - pDirection.getStepZ()) + 0.5D;
                        AABB aabb = new AABB(d2 - d1, this.getFloorLevel(blockpos$mutableblockpos.set(d2, pY + 1, d3)) + 0.001D, d3 - d1, d2 + d1, (double) this.mob.getDimensions(Pose.SWIMMING).height + this.getFloorLevel(blockpos$mutableblockpos.set(node.x, node.y, (double) node.z)) - 0.002D, d3 + d1);
                        if (this.hasCollisions(aabb)) {
                            node = null;
                        }
                    }
                }

                if (!this.isAmphibious() && blockpathtypes == BlockPathTypes.WATER && !this.canFloat()) {
                    if (this.getCachedBlockType(this.mob, pX, pY - 1, pZ) != BlockPathTypes.WATER) {
                        return node;
                    }

                    while (pY > this.mob.level().getMinBuildHeight()) {
                        --pY;
                        blockpathtypes = this.getCachedBlockType(this.mob, pX, pY, pZ);
                        if (blockpathtypes != BlockPathTypes.WATER) {
                            return node;
                        }

                        node = this.getNodeAndUpdateCostToMax(pX, pY, pZ, blockpathtypes, this.mob.getPathfindingMalus(blockpathtypes));
                    }
                }

                if (blockpathtypes == BlockPathTypes.OPEN) {
                    int j = 0;
                    int i = pY;

                    while (blockpathtypes == BlockPathTypes.OPEN) {
                        --pY;
                        if (pY < this.mob.level().getMinBuildHeight()) {
                            return this.getBlockedNode(pX, i, pZ);
                        }

                        if (j++ >= this.mob.getMaxFallDistance()) {
                            return this.getBlockedNode(pX, pY, pZ);
                        }

                        blockpathtypes = this.getCachedBlockType(this.mob, pX, pY, pZ);
                        f = this.mob.getPathfindingMalus(blockpathtypes);
                        if (blockpathtypes != BlockPathTypes.OPEN && f >= 0.0F) {
                            node = this.getNodeAndUpdateCostToMax(pX, pY, pZ, blockpathtypes, f);
                            break;
                        }

                        if (f < 0.0F) {
                            return this.getBlockedNode(pX, pY, pZ);
                        }
                    }
                }

                if (doesBlockHavePartialCollision(blockpathtypes) && node == null) {
                    node = this.getNode(pX, pY, pZ);
                    node.closed = true;
                    node.type = blockpathtypes;
                    node.costMalus = blockpathtypes.getMalus();
                }

                return node;
            } else {
                return node;
            }
        }
//        Node node = super.findAcceptedNode(x, y, z, verticalDeltaLimit, nodeFloorLevel, direction, pathType);
//
//        // Se o super retornou null (bloqueado), tentamos validar manualmente para o "crawl"
//        if (node == null && this.mob instanceof ChangedEntity changedMob) {
//            BlockPos pos = new BlockPos(x, y, z);
//            BlockPathTypes typeAtPos = this.getCachedBlockType(this.mob, x, y, z);
//
//            // Se o bloco é fisicamente passável com rastejo
//            if (AdvancedGroundPathNavigation.canEntityEnterPoseIn(changedMob, Pose.SWIMMING, Vec3.atBottomCenterOf(pos))) {
//                node = this.getNode(x, y, z);
//                node.type = typeAtPos;
//                // Aumentamos o custo (malus) para rastejar, assim ele prefere caminhos abertos
//                node.costMalus = Math.max(node.costMalus, this.mob.getPathfindingMalus(typeAtPos) + 1.5F);
//            }
//        }
//
//        return node;
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

    protected double getMobJumpHeight() {
        return Math.max(1.125D, this.mob.getStepHeight());
    }

    protected Node getNodeAndUpdateCostToMax(int pX, int pY, int pZ, BlockPathTypes pType, float pCostMalus) {
        Node node = this.getNode(pX, pY, pZ);
        node.type = pType;
        node.costMalus = Math.max(node.costMalus, pCostMalus);
        return node;
    }

    protected static boolean doesBlockHavePartialCollision(BlockPathTypes pBlockPathType) {
        return pBlockPathType == BlockPathTypes.FENCE || pBlockPathType == BlockPathTypes.DOOR_WOOD_CLOSED || pBlockPathType == BlockPathTypes.DOOR_IRON_CLOSED;
    }

    protected Node getBlockedNode(int pX, int pY, int pZ) {
        Node node = this.getNode(pX, pY, pZ);
        node.type = BlockPathTypes.BLOCKED;
        node.costMalus = -1.0F;
        return node;
    }
}