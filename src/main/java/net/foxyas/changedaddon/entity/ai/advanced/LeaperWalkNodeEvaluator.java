package net.foxyas.changedaddon.entity.ai.advanced;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LeaperWalkNodeEvaluator extends WalkNodeEvaluator {

    @Override
    public int getNeighbors(Node @NotNull [] pNeighborNodes, @NotNull Node pNode) {
        int i = super.getNeighbors(pNeighborNodes, pNode);

        // VERIFICAÇÃO: Se o nó atual onde estamos já for um nó de pulo,
        // não permitimos que ele gere OUTRO nó de pulo como vizinho imediato.
        boolean isCurrentlyJumping = pNode instanceof IAdvancedNode advNode && advNode.isJumpNode();

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            Node jumpNode = this.getJumpNeighbor(pNode.x + direction.getStepX(), pNode.y + direction.getStepY(), pNode.z + direction.getStepZ(), direction);
            if (jumpNode != null && !jumpNode.closed && i < pNeighborNodes.length) {
                if (isCurrentlyJumping) {
                    jumpNode.costMalus *= 0.5f;
                }
                pNeighborNodes[i++] = jumpNode;
            }
        }

        return i;
    }

    @Nullable
    protected Node getJumpNeighbor(int x, int y, int z, Direction dir) {
        for (int yOffset = 0; yOffset <= 1; yOffset++) {
            BlockPos gapPos = new BlockPos(x, y + yOffset, z);
            BlockState stateAhead = this.level.getBlockState(gapPos);

            // AJUSTE 1: Só considera pulo se o bloco à frente for AR
            // E o bloco abaixo dele (onde ela pisaria) NÃO for sólido.

            Vec3 bottomCenterOfGap = Vec3.atBottomCenterOf(gapPos);
            Vec3 bottomCenterOfBelowGap = Vec3.atBottomCenterOf(gapPos.below(5));
            BlockHitResult clip = this.level.clip(new ClipContext(bottomCenterOfGap, bottomCenterOfBelowGap, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob));

            boolean isActualGap = stateAhead.isAir() && clip.getLocation().distanceTo(bottomCenterOfGap) >= 1.5;

            // || !stateAhead.isSolidRender(this.level, gapPos)
            if (isActualGap) {
                for (int distance = 2; distance <= 4; distance++) {
                    int jumpX = x + dir.getStepX() * (distance);
                    int jumpZ = z + dir.getStepZ() * (distance);
                    BlockPos landPos = new BlockPos(jumpX, y + yOffset, jumpZ);
                    BlockState belowLandPoseState = this.level.getBlockState(landPos.below());

                    if (this.isWalkable(landPos) && belowLandPoseState.isFaceSturdy(level, landPos.below(), Direction.UP)) {
                        if (isPathClearForJump(gapPos, landPos, dir)) {
                            Node node = this.getNode(landPos.getX(), landPos.getY(), landPos.getZ());
                            node.type = BlockPathTypes.OPEN;


                            // Isso faz com que um desvio de até 20 blocos caminhando seja
                            // preferível a um único pulo arriscado.
                            float verticalPenalty = yOffset > 0 ? 15.0f : 0.0f;
                            node.costMalus = this.mob.getPathfindingMalus(node.type) + 20f + verticalPenalty;

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
        BlockPathTypes type = this.getCachedBlockType(this.mob, pos.getX(), pos.getY(), pos.getZ());
        // Aceita se for caminhável ou ar, desde que o chão abaixo seja sólido ou passável
        if (type == BlockPathTypes.WALKABLE || type == BlockPathTypes.OPEN) {
            // Verifica se o bloco dos pés está livre
            BlockState state = this.level.getBlockState(pos);
            return state.isAir() || !state.isSolidRender(this.level, pos);
        }
        return false;
    }

    /**
     * Performs a 3-point clearance check to ensure the entity doesn't hit its head or a wall mid-jump.
     */
    private boolean isPathClearForJump(BlockPos start, BlockPos end, Direction dir) {
        Vec3 startVec = Vec3.atCenterOf(start);
        Vec3 endVec = Vec3.atCenterOf(end);

        // Raycast para garantir que a trajetória está vazia
        // Usamos o ClipContext para checar se há colisores no caminho
        BlockHitResult hit = this.level.clip(new ClipContext(
                startVec,
                endVec,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this.mob
        ));

        return hit.getType() == HitResult.Type.MISS;
    }

    /**
     * Checks if the vertical space at a position is clear for the entity's height.
     */
    private boolean isSpaceEmpty(Vec3 position, Vec3 finalPosition) {
        BlockHitResult clip = level.clip(new ClipContext(position, finalPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob));
        return clip.getType() == HitResult.Type.MISS;
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