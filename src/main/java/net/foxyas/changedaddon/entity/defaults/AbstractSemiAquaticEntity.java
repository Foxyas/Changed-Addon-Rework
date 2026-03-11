package net.foxyas.changedaddon.entity.defaults;

import net.foxyas.changedaddon.entity.ai.goals.generic.SwimToTheTargetGoal;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class AbstractSemiAquaticEntity extends ChangedEntity {

    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation groundNavigation;
    private boolean wantsSurface;
    private float oldWaterCost;

    protected AbstractSemiAquaticEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
        this.moveControl = new SwimableEntityMoveControl(this);
        this.waterNavigation = new WaterBoundPathNavigation(this, level);
        this.groundNavigation = new GroundPathNavigation(this, level);
        this.groundNavigation.setCanOpenDoors(true);
        this.groundNavigation.setCanFloat(true);
        this.oldWaterCost = getPathfindingMalus(BlockPathTypes.WATER);
    }

    /* =========================
       === ENTITY PROPERTIES ===
       ========================= */

    public boolean wantsSurface() {
        return wantsSurface;
    }

    public void setWantsSurface(boolean value) {
        this.wantsSurface = value;
    }

    public double getPreferredSurfaceDepth() {
        return super.getFluidJumpThreshold();
    }

    @Override
    public double getFluidJumpThreshold() {
        return super.getFluidJumpThreshold();
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return false;
    }

    @Override
    public LatexType getLatexType() {
        return super.getLatexType();
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }

    /* =========================
       === AI / GOALS ==========
       ========================= */

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new RiseToSurfaceGoal(this, 1));
        this.goalSelector.addGoal(0, new SwimToTheTargetGoal(this, 0.5f));

        //this.goalSelector.addGoal(0, new FloatGoal(this));
        //this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.8));
    }

    /* =========================
       === SWIMMING LOGIC ======
       ========================= */

    protected boolean wantsToSwim() {
        LivingEntity target = this.getTarget();

        // Quase se afogando → subir / nadar
        if (this.getAirSupply() < this.getMaxAirSupply() * 0.25f && this.isUnderWater())
            return false;

        // Target está na água → perseguir
        return (target != null && target.isInWater()) || this.isUnderWater();
    }

    public boolean wantsToSurface() {
        LivingEntity target = this.getTarget();

        return wantsSurface() || target == null || !this.isSwimming() || (this.getAirSupply() < this.getMaxAirSupply() * 0.25f && this.isUnderWater());
    }

    protected boolean canFitInWater(Vec3 pos) {
        float height = this.getDimensions(Pose.STANDING).height;
        BlockPos base = new BlockPos(Mth.floor(pos.x), Mth.floor(pos.y), Mth.floor(pos.z));

        return BlockPos.betweenClosedStream(
                        this.getDimensions(Pose.STANDING)
                                .makeBoundingBox(pos)
                                .inflate(-0.05)
                ).filter(p -> p.getY() == base.getY())
                .allMatch(p -> getWaterDepth(p) >= height);
    }

    protected float getWaterDepth(BlockPos pos) {
        float depth = 0f;

        for (int i = 0; i < 3; i++) {
            BlockState state = this.level.getBlockState(pos.below(i));
            if (state.getFluidState().is(FluidTags.WATER)) depth++;
            else break;
        }

        for (int i = 1; i < 3; i++) {
            BlockState state = this.level.getBlockState(pos.above(i));
            if (state.getFluidState().is(FluidTags.WATER)) depth++;
            else break;
        }

        return depth;
    }

    protected boolean adjacentToLand(BlockPos pos) {
        return Plane.HORIZONTAL.stream()
                .map(pos::relative)
                .anyMatch(p ->
                        this.level.getBlockState(p).isCollisionShapeFullBlock(this.level, p)
                                && this.level.getBlockState(p.above()).isAir()
                );
    }

    @Override
    public void updateSwimming() {
        updateSwimmingState();
    }

    protected void updateSwimmingState() {
        if (this.level.isClientSide) return;

        boolean shouldSwim =
                this.isInWater()
                        && this.canFitInWater(this.position())
                        && this.wantsToSwim();

        this.setMaxUpStep(shouldSwim ? 1.0F : 0.7F);

        if (wantsToSwim()) {
            this.setPathfindingMalus(BlockPathTypes.WATER, 0);
        } else this.setPathfindingMalus(BlockPathTypes.WATER, oldWaterCost);

        if (isEffectiveAi() && shouldSwim) {
            this.navigation = this.waterNavigation;
            this.setSwimming(true);
            this.setPose(Pose.SWIMMING);
        } else {
            this.navigation = this.groundNavigation;
            this.setSwimming(false);
            switchToSafePose();
        }
    }

    protected void switchToSafePose() {
        Pose currentPose = this.getPose();
        Pose safePose = currentPose;

        if (canEnterPose(Pose.STANDING)) {
            safePose = Pose.STANDING;
        } else if (canEnterPose(Pose.CROUCHING)) {
            safePose = Pose.CROUCHING;
        } else if (canEnterPose(Pose.SWIMMING)) {
            safePose = Pose.SWIMMING;
        }

        if (safePose != currentPose) {
            this.setPose(safePose);
            //this.refreshDimensions();
        }
    }

    public double getWaterSurfaceY(BlockPos startPos) {
        BlockPos.MutableBlockPos pos = startPos.mutable();


        while (level().getFluidState(pos).isSource() ||
                level().getFluidState(pos).getType().isSame(Fluids.WATER)) {
            pos.move(Direction.UP);

            if (pos.getY() >= level().getMaxBuildHeight()) {
                break;
            }
        }


        // Aqui pos já está FORA da água,
        // então a superfície é o Y anterior
        return pos.getY();
    }

    /* =========================
       === MOVE CONTROL ========
       ========================= */

    public static class SwimableEntityMoveControl extends MoveControl {

        private final AbstractSemiAquaticEntity mob;

        public SwimableEntityMoveControl(AbstractSemiAquaticEntity mob) {
            super(mob);
            this.mob = mob;
        }

        @Override
        public void tick() {
            LivingEntity target = mob.getTarget();

            if (mob.isSwimming()) {
                if (target != null && target.getY() > mob.getY() || mob.wantsToSurface()) {
                    mob.setDeltaMovement(mob.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
                }

                if (this.operation != Operation.MOVE_TO || mob.getNavigation().isDone()) {
                    mob.setSpeed(0);
                    return;
                }

                double dx = this.wantedX - mob.getX();
                double dy = this.wantedY - mob.getY();
                double dz = this.wantedZ - mob.getZ();

                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                dy /= dist;

                float yaw = (float) (Mth.atan2(dz, dx) * 180F / Math.PI) - 90F;
                mob.setYRot(this.rotlerp(mob.getYRot(), yaw, 90F));
                mob.yBodyRot = mob.getYRot();

                float swimSpeed = (float) (this.speedModifier * mob.getAttributeValue(ForgeMod.SWIM_SPEED.get()));

                mob.setSpeed(swimSpeed);
                mob.setDeltaMovement(mob.getDeltaMovement().add(
                        dx * 0.005 * swimSpeed,
                        dy * 0.1 * swimSpeed,
                        dz * 0.005 * swimSpeed)
                );
            } else {
                super.tick();
            }
        }
    }

    public static class RiseToSurfaceGoal extends Goal {
        private final AbstractSemiAquaticEntity mob;
        private final double speedModifier;
        private final Level level;
        private double wantedX;
        private double wantedY;
        private double wantedZ;

        public RiseToSurfaceGoal(AbstractSemiAquaticEntity entity, double speed) {
            this.mob = entity;
            this.speedModifier = speed;
            this.level = entity.level();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (this.mob.getTarget() != null) {
                return false;
            } else if (!this.mob.wantsToSurface()) {
                return false;
            } else if (!this.mob.isInWater()) {
                return false;
            } else if (level.getBlockState(EntityUtil.getEyeBlock(mob)).isAir()) {
                return false;
            } else {
                this.wantedX = this.mob.getX();
                this.wantedY = this.mob.getY() + 1.0;
                this.wantedZ = this.mob.getZ();
                return true;
            }
        }

        public boolean canContinueToUse() {
            if (this.mob.getTarget() != null) {
                return false;
            }

            return !this.mob.getNavigation().isDone();
        }

        public void start() {
            if (this.mob.isSwimming()) {
                this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
            }
        }

        @Nullable
        private Vec3 getWaterPos() {
            RandomSource random = this.mob.getRandom();
            BlockPos blockpos = this.mob.blockPosition();

            for (int i = 0; i < 10; ++i) {
                BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
                if (this.level.getBlockState(blockpos1).is(Blocks.WATER)) {
                    return Vec3.atBottomCenterOf(blockpos1);
                }
            }

            return null;
        }
    }
}