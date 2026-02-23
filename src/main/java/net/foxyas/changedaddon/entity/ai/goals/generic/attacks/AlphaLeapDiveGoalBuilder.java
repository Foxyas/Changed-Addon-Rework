package net.foxyas.changedaddon.entity.ai.goals.generic.attacks;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class AlphaLeapDiveGoalBuilder {

    private final PathfinderMob mob;

    private Vec3 followAscendMultiplier = new Vec3(0.25, 1.0, 0.25);
    private double ascendSpeed = 0.6D;
    private double ascendHoldY = 6.0D;
    private Vec3 diveSpeedMultiplier = new Vec3(1.8, -1.2, 1.8);
    private float ringRadius = 3.5F;
    private int failSafeTicks = 80;
    private IntProvider cooldownProvider = ConstantInt.of(100);
    private double ascendInitialBoost = 0.6;

    public AlphaLeapDiveGoalBuilder(PathfinderMob mob) {
        this.mob = Objects.requireNonNull(mob, "mob");
    }

    // ---------- cooldown ----------
    public AlphaLeapDiveGoalBuilder withCooldown(IntProvider cooldownProvider) {
        this.cooldownProvider = Objects.requireNonNull(cooldownProvider);
        return this;
    }

    // ---------- ascend ----------
    public AlphaLeapDiveGoalBuilder withFollowAscendMultiplier(Vec3 multiplier) {
        this.followAscendMultiplier = Objects.requireNonNull(multiplier);
        return this;
    }

    public AlphaLeapDiveGoalBuilder withAscendInitialBoost(double boost) {
        this.ascendInitialBoost = boost;
        return this;
    }

    public AlphaLeapDiveGoalBuilder withAscendSpeed(double speed) {
        this.ascendSpeed = speed;
        return this;
    }

    public AlphaLeapDiveGoalBuilder withAscendHoldY(double holdY) {
        this.ascendHoldY = holdY;
        return this;
    }

    // ---------- dive ----------
    public AlphaLeapDiveGoalBuilder withDiveSpeedMultiplier(Vec3 multiplier) {
        this.diveSpeedMultiplier = Objects.requireNonNull(multiplier);
        return this;
    }

    // ---------- impact visuals ----------
    public AlphaLeapDiveGoalBuilder withRingRadius(float radius) {
        this.ringRadius = radius;
        return this;
    }

    // ---------- safety ----------
    public AlphaLeapDiveGoalBuilder withFailSafeTicks(int ticks) {
        this.failSafeTicks = ticks;
        return this;
    }

    // ---------- build ----------
    public AlphaLeapDiveGoal build() {
        return new AlphaLeapDiveGoal(
                mob,
                cooldownProvider,
                followAscendMultiplier,
                ascendSpeed,
                ascendInitialBoost,
                ascendHoldY,
                diveSpeedMultiplier,
                ringRadius,
                failSafeTicks
        );
    }
}
