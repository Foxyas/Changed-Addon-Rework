package net.foxyas.changedaddon.entity.ai.advanced;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Override
    @Nullable
    protected Node findAcceptedNode(int x, int y, int z, int verticalDeltaLimit, double nodeFloorLevel, @NotNull Direction direction, @NotNull BlockPathTypes pathType) {
        if (this.mob instanceof ChangedEntity changedMob) {
            BlockPos pos = new BlockPos(x, y, z);

            double verticalDiff = (double)y - nodeFloorLevel;

            if (verticalDiff >= -1.0D && verticalDiff <= 1.25D) {
                Vec3 targetVec = Vec3.atBottomCenterOf(pos);
                if (AdvancedGroundPathNavigation.canEntityEnterPoseIn(changedMob, Pose.SWIMMING, targetVec)) {
                    Node node = this.getNode(x, y, z);
                    node.type = this.getCachedBlockType(this.mob, x, y, z);

                    node.costMalus = Math.max(node.costMalus, this.mob.getPathfindingMalus(node.type) + 2.5F);
                    return node;
                }
            }
        }

        return super.findAcceptedNode(x, y, z, verticalDeltaLimit, nodeFloorLevel, direction, pathType);
    }

    @Override
    protected boolean isDiagonalValid(@NotNull Node pRoot, @Nullable Node pXNode, @Nullable Node pZNode, @Nullable Node pDiagonal) {
        // Se o nó diagonal existe e é um buraco que validamos no findAcceptedNode
        if (pDiagonal != null && this.mob instanceof ChangedEntity) {
            // Se os nós adjacentes (que formam a quina) permitirem a passagem rastejando, a diagonal é válida
            return canReachWithoutCollision(pDiagonal) && pDiagonal.distanceTo(pRoot) <= 1.25f;
        }
        return super.isDiagonalValid(pRoot, pXNode, pZNode, pDiagonal);
    }

    @Override
    public boolean canReachWithoutCollision(@NotNull Node node) {
        // Redefine a verificação de colisão final do pathfinding
        if (this.mob instanceof ChangedEntity changedMob) {
            Vec3 target = Vec3.atBottomCenterOf(node.asBlockPos());
            // Se ele cabe em qualquer pose (Standing, Crouching ou Swimming), o caminho é válido
            if (AdvancedGroundPathNavigation.canEntityEnterPoseIn(changedMob, Pose.SWIMMING, target)) {
                return true;
            }
        }
        return super.canReachWithoutCollision(node);
    }
}