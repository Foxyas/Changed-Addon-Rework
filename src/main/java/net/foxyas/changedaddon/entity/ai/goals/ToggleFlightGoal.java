package net.foxyas.changedaddon.entity.ai.goals;

import net.foxyas.changedaddon.entity.api.IFlyableChangedEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class ToggleFlightGoal<T extends PathfinderMob & IFlyableChangedEntity> extends Goal {
    private final T entity;

    public ToggleFlightGoal(T entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        if (entity.getTarget() != null) {
            return false; //There's no need to stop or start flying randomly if it has a target
        }
        return this.entity.getRandom().nextInt(200) == 0;
    }

    @Override
    public void start() {
        boolean currentlyFlying = this.entity.isFlyingMode();
        this.entity.setFlyingMode(!currentlyFlying);
    }
}