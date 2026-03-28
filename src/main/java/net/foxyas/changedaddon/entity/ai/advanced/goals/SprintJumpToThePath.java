package net.foxyas.changedaddon.entity.ai.advanced.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class SprintJumpToThePath extends Goal {

    protected final PathfinderMob mob;
    protected final EnumSet<Flag> flags = EnumSet.of(Flag.MOVE, Flag.JUMP);

    public SprintJumpToThePath(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(flags);
    }

    @Override
    public boolean canUse() {
        PathNavigation pathNavigation = this.mob.getNavigation();
        Path path = pathNavigation.getPath();
        if (path != null) {
            BlockPos targetPos = path.getTarget();
            return mob.position().distanceTo(targetPos.getCenter()) >= 3;
        }

        return false;
    }

    @Override
    public void start() {
        super.start();
        this.mob.setSprinting(true);
    }

    @Override
    public void tick() {
        if (this.mob.onGround() && this.mob.isSprinting() && !this.mob.isInWater()) {
            // Verifica se há um obstáculo ou se o próximo nó está longe
            // O pulo no sprint aumenta a velocidade horizontal (forward)
            this.mob.getJumpControl().jump();
        }
    }
}
