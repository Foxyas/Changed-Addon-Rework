package net.foxyas.changedaddon.entity.ai.advanced;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class AdvancedNodeEvaluator extends WalkNodeEvaluator {

    static Pose[] poses = {Pose.CROUCHING, Pose.SWIMMING};

    @Override
    public void prepare(@NotNull PathNavigationRegion pLevel, @NotNull Mob pMob) {
        super.prepare(pLevel, pMob);
        // Artificially reduce height during evaluation to allow nodes in tight spaces
        this.entityHeight = Mth.floor(pMob.getDimensions(Pose.SWIMMING).height + 0.5f);
    }

    @Override
    public @NotNull BlockPathTypes getBlockPathType(@NotNull BlockGetter level, int x, int y, int z, @NotNull Mob mob) {
        return super.getBlockPathType(level, x, y, z, mob);
    }

    @Override
    public int getNeighbors(Node @NotNull [] pNeighborNodes, @NotNull Node pNode) {
        int i = super.getNeighbors(pNeighborNodes, pNode);

        // If the vanilla logic found neighbors (walking), we add the "jump" ones.
        // Check 4 horizontal directions to see if a jump is possible across a gap.
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            Node jumpNode = this.getJumpNeighbor(pNode.x + direction.getStepX(), pNode.y, pNode.z + direction.getStepZ(), direction);
            if (jumpNode != null && !jumpNode.closed && i < pNeighborNodes.length) {
                pNeighborNodes[i++] = jumpNode;
            }
        }

        return i;
    }

    @Nullable
    protected Node getJumpNeighbor(int x, int y, int z, Direction dir) {
        BlockPos gapPos = new BlockPos(x, y, z);

        // 1. Is it a real gap?
        // Checks if the block ahead and the two blocks below are non-solid to confirm a deep hole/gap.
        boolean isDeepGap = this.level.getBlockState(gapPos).isAir() &&
                !this.level.getBlockState(gapPos.below()).isSolidRender(this.level, gapPos.below()) &&
                !this.level.getBlockState(gapPos.below(2)).isSolidRender(this.level, gapPos.below(2));

        if (isDeepGap) {
            // 2. Look for a landing spot (Scanning from distance 2 to 4)
            for (int distance = 2; distance <= 4; distance++) {
                for (int yOffset = 0; yOffset < 1; yOffset++) {
                    int jumpX = x + dir.getStepX() * (distance - 1);
                    int jumpZ = z + dir.getStepZ() * (distance - 1);
                    BlockPos landPos = new BlockPos(jumpX, y + yOffset, jumpZ);

                    if (this.isWalkable(landPos)) {

                        // 3. Triple-Clip Check (Start, Mid-Air, Landing)
                        // We check if the entity's hitbox (based on current or swimming pose) fits throughout the jump.
                        if (isPathClearForJump(gapPos.relative(Direction.UP, yOffset), landPos, dir)) {

                            Node node = this.getNode(landPos.getX(), landPos.getY(), landPos.getZ());
                            node.type = BlockPathTypes.WALKABLE;

                            // 4. High Cost Penalty: Jumping is expensive so AI prefers walking if a bridge exists.
                            node.costMalus = this.mob.getPathfindingMalus(node.type) + 20.0f;

                            if (node instanceof IAdvancedNode advancedNode) {
                                advancedNode.setJumpNode(true);
                            }
                            return node;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isWalkable(BlockPos pos) {
        // Reuse ChangedEntity logic to check if it fits (even in crawl pose)
        /*
        if (this.mob instanceof ChangedEntity ce) {
            return AdvancedGroundPathNavigation.canEntityEnterPoseIn(ce, Pose.SWIMMING, Vec3.atBottomCenterOf(pos));
        }
        */
        BlockPathTypes cachedBlockType = this.getCachedBlockType(this.mob, pos.getX(), pos.getY(), pos.getZ());
        return cachedBlockType == BlockPathTypes.WALKABLE;
    }

    /**
     * Performs a 3-point clearance check to ensure the entity doesn't hit its head or a wall mid-jump.
     */
    private boolean isPathClearForJump(BlockPos start, BlockPos end, Direction dir) {
        boolean belowClear = isSpaceEmpty(start.getCenter(), Vec3.atBottomCenterOf(end));
        boolean middleClear = isSpaceEmpty(start.getCenter().relative(Direction.UP, 1), Vec3.atBottomCenterOf(end));
        boolean topClear = isSpaceEmpty(start.getCenter().relative(Direction.UP, 2), Vec3.atBottomCenterOf(end));
        return belowClear && middleClear && topClear;
    }

    /**
     * Checks if the vertical space at a position is clear for the entity's height.
     */
    private boolean isSpaceEmpty(Vec3 position, Vec3 finalPosition) {
        BlockHitResult clip = level.clip(new ClipContext(position, finalPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob));
        return clip.getType() == HitResult.Type.MISS;
    }

    @Override
    public @NotNull Node getStart() {
        if (!(mob instanceof ChangedEntity changedEntity)) {
            return super.getStart();
        }

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int i = this.mob.getBlockY();
        BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), i, this.mob.getZ()));

        // --- Fluid and Ground Detection ---
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

        // --- Start Node Verification ---
        BlockPos blockpos1 = this.mob.blockPosition();
        if (!this.canStartAt(blockpos$mutableblockpos.set(blockpos1.getX(), i, blockpos1.getZ()))) {
            // Check alternative poses if standing fails (allows starting inside ducts)
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

        // Jump height check
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

            // Pathfinding logic for verticality and obstacles
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

                // Liquid handling
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

                // Fall handling
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
    }

    @Override
    public boolean canReachWithoutCollision(@NotNull Node node) {
        // Redefine collision check for the pathfinding final validation
        if (this.mob instanceof ChangedEntity changedMob) {
            Vec3 target = Vec3.atBottomCenterOf(node.asBlockPos());
            // If the entity fits in ANY pose, consider it reachable
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