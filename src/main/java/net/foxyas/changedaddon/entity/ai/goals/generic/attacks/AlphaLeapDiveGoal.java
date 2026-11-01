package net.foxyas.changedaddon.entity.ai.goals.generic.attacks;

import net.foxyas.changedaddon.entity.ai.goals.simple.IAlphaEntityGoal;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class AlphaLeapDiveGoal extends LeapDiveGoal implements IAlphaEntityGoal {

    public AlphaLeapDiveGoal(PathfinderMob mob,
                             IntProvider cooldownProvider,
                             Vec3 followAscendMultiplier,
                             double ascendSpeed,
                             double ascendInitialBoost,
                             double ascendHoldY,
                             Vec3 diveSpeedMultiplier,
                             float ringRadius,
                             int failSafeTicks) {
        super(mob,
                cooldownProvider,
                followAscendMultiplier,
                ascendSpeed,
                ascendInitialBoost,
                ascendHoldY,
                diveSpeedMultiplier,
                ringRadius,
                failSafeTicks);
    }

}
