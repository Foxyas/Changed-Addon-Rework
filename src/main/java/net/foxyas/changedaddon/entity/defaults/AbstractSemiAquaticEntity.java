package net.foxyas.changedaddon.entity.defaults;

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
import net.minecraft.world.entity.ai.goal.FloatGoal;
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
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public abstract class AbstractSemiAquaticEntity extends ChangedEntity {

    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation groundNavigation;
    private boolean wantsSurface;
    private final float oldWaterCost;

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

    public double getSwimSpeed() {
        return this.getAttributeValue(ForgeMod.SWIM_SPEED.get());
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

        this.goalSelector.addGoal(0, new FloatGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && (!AbstractSemiAquaticEntity.this.wantsToSwim() || AbstractSemiAquaticEntity.this.wantsToSurface());
            }

            @Override
            public void start() {
                super.start();
            }
        });
        //this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.8));
    }

    @Override
    protected @Nullable Goal makeFloatGoal() {
        return null;
    }

    /* =========================
       === SWIMMING LOGIC ======
       ========================= */

    protected boolean needToRecoverBreath = false;

    protected boolean wantsToSwim() {
        LivingEntity target = this.getTarget();
        if (needToRecoverBreath) {
            this.needToRecoverBreath = !(this.getAirSupply() >= this.getMaxAirSupply() * 0.75f);
            return false;
        }

        // Quase se afogando → subir / nadar
        if (this.getAirSupply() < this.getMaxAirSupply() * 0.25f && this.isUnderWater()) {
            this.needToRecoverBreath = true;
            return false;
        }

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

    protected boolean isAirAtEyesWhenStanding(Vec3 pos) {
        BlockPos originalPos = new BlockPos(Mth.floor(pos.x), Mth.floor(pos.y), Mth.floor(pos.z));
        return BlockPos.betweenClosedStream(this.getDimensions(Pose.STANDING).makeBoundingBox(pos).inflate(-0.05)).filter((checkPos) -> checkPos.getY() > originalPos.getY()).allMatch((blockPos) -> this.level().getBlockState(blockPos).getFluidState().isEmpty());
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
        private final AbstractSemiAquaticEntity semiAquaticEntity;

        public SwimableEntityMoveControl(AbstractSemiAquaticEntity p_32433_) {
            super(p_32433_);
            this.semiAquaticEntity = p_32433_;
        }

        public void tick() {
            this.semiAquaticEntity.updateSwimming();
            LivingEntity livingentity = this.semiAquaticEntity.getTarget();
            if (this.semiAquaticEntity.isSwimming()) {
                if (semiAquaticEntity.wantsToSurface()) {
                    super.tick();
                    return;
                }

                if (livingentity != null && livingentity.getY() > this.semiAquaticEntity.getY()) {
                    double dx = livingentity.getX() - this.semiAquaticEntity.getX();
                    double dz = livingentity.getZ() - this.semiAquaticEntity.getZ();
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    this.semiAquaticEntity.setDeltaMovement(this.semiAquaticEntity.getDeltaMovement().add(dx / dist * 0.02, 0.04, dz / dist * 0.02));
                }

                if (this.operation != Operation.MOVE_TO || this.semiAquaticEntity.getNavigation().isDone()) {
                    this.semiAquaticEntity.setSpeed(0.0F);
                    return;
                }

                double dx = this.wantedX - this.semiAquaticEntity.getX();
                double dy = this.wantedY - this.semiAquaticEntity.getY();
                double dz = this.wantedZ - this.semiAquaticEntity.getZ();
                //double d3 = Math.sqrt(dx * dx + dy * dy + dz * dz);
                //dy /= d3;
                float f = (float) (Mth.atan2(dz, dx) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.semiAquaticEntity.setYRot(this.rotlerp(this.semiAquaticEntity.getYRot(), f, 90.0F));
                this.semiAquaticEntity.yBodyRot = this.semiAquaticEntity.getYRot();
                float swimSpeed = (float) (this.speedModifier * this.semiAquaticEntity.getSwimSpeed());
                float speed = Mth.lerp(0.125F, this.semiAquaticEntity.getSpeed(), swimSpeed);
                this.semiAquaticEntity.setSpeed(speed * 1.05F);
                this.semiAquaticEntity.setDeltaMovement(this.semiAquaticEntity.getDeltaMovement().add(
                        speed * dx * 0.005,
                        speed * dy * 0.1,
                        speed * dz * 0.005
                ));
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

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
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