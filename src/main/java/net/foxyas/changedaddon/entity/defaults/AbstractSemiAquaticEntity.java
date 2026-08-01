package net.foxyas.changedaddon.entity.defaults;

import net.foxyas.changedaddon.entity.api.ISwimableEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.PathfindingRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public abstract class AbstractSemiAquaticEntity extends ChangedEntity implements ISwimableEntity {

    protected final PathNavigation groundNavigation;
    protected final PathNavigation waterNavigation;

    protected boolean wantsSurface;
    protected final float oldWaterCost;

    // --- Two-tier surfacing thresholds (as a fraction of max air supply) ---
    // "wants": casual preference, gets checked but won't interrupt combat.
    protected static final float SURFACE_WANT_AIR_THRESHOLD = 0.5F;
    // "needs": urgent, will interrupt everything (including combat) to breathe.
    protected static final float SURFACE_NEED_AIR_THRESHOLD = 0.2F;
    // Once in "needs" mode, air must climb back above this before it clears (hysteresis,
    // prevents the entity from flip-flopping right at the threshold).
    protected static final float SURFACE_NEED_RECOVER_THRESHOLD = 0.8F;

    // true once the entity has crossed into "urgent" mode, cleared once it recovers enough air
    protected boolean needsAirUrgently = false;

    protected AbstractSemiAquaticEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
        this.moveControl = new SwimableEntityMoveControl(this);
        this.groundNavigation = createNavigation(level);
        this.waterNavigation = createWaterNavigation(level);

        this.oldWaterCost = getPathfindingMalus(BlockPathTypes.WATER);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        PathNavigation pathNavigation = super.createNavigation(pLevel);
        if (pathNavigation instanceof GroundPathNavigation groundPathNavigation) {
            groundPathNavigation.setCanOpenDoors(true);
            groundPathNavigation.setCanFloat(true);
        }
        return pathNavigation;
    }

    protected @NotNull PathNavigation createWaterNavigation(Level level) {
        WaterBoundPathNavigation pathNavigation = new WaterBoundPathNavigation(this, level);
        pathNavigation.setCanFloat(true);
        return pathNavigation;
//        return new AmphibiousPathNavigation(this, level);
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
    public @NotNull MobType getMobType() {
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
        this.goalSelector.addGoal(0, new EmergencyBreatheGoal(this, 0.6));
//        this.goalSelector.addGoal(1, new SinkFromSurfaceGoal(this, 0.3));
        this.goalSelector.addGoal(1, new RiseToSurfaceGoal(this, 0.3));
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 0.4, 10));
    }

    @Override
    public void baseTick() {
        super.baseTick();
        Path path = this.navigation.getPath();
        if (path != null) {
            Minecraft minecraft = Minecraft.getInstance();
            PathfindingRenderer pathfindingRenderer = minecraft.debugRenderer.pathfindingRenderer;
            pathfindingRenderer.addPath(getId(), path, 1f);
        }
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        boolean animateSwim = this.isInWater() && this.canFitInWater(this.position());

        if (this.isEffectiveAi() && animateSwim && this.isSwimming()) {
            this.moveRelative(0.01F, pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(pTravelVector);
        }
    }

    @Override
    protected @Nullable Goal makeFloatGoal() {
        return new FloatGoal(this) {
            @Override
            public boolean canUse() {
                if (true) return false;
                return super.canUse() && AbstractSemiAquaticEntity.this.wantsToSurface();
            }
        };
    }

    /* =========================
       === SWIMMING LOGIC ======
       ========================= */

    protected boolean wantsToSwim() {
        LivingEntity target = this.getTarget();

        // Urgent need for air always wins — stop swimming around, surface instead.
        if (this.needsToSurface()) {
            return false;
        }

        // Target is in the water → chase it.
        return (target != null && target.isInWater()) || this.isUnderWater();
    }

    /**
     * Soft "I'd like to go to the surface" signal. This can be ignored while the
     * entity is busy (e.g. mid-combat) — it's a preference, not an emergency.
     */
    public boolean wantsToSurface() {
        LivingEntity target = this.getTarget();

        return wantsSurface()
                || needsToSurface()
                || target == null
                || !this.isSwimming()
                || (this.isUnderWater() && this.getAirSupply() < this.getMaxAirSupply() * SURFACE_WANT_AIR_THRESHOLD);
    }

    /**
     * Hard "I NEED to go to the surface" signal. Air is critically low — this should
     * override everything else, including chasing a target, or the entity will drown.
     * Uses hysteresis: once triggered, stays true until air recovers well above the
     * trigger point, so the entity doesn't dive right back down the moment it pokes
     * its head out.
     */
    public boolean needsToSurface() {
        if (!this.isUnderWater()) {
            this.needsAirUrgently = false;
            return false;
        }

        if (this.needsAirUrgently) {
            this.needsAirUrgently = this.getAirSupply() < this.getMaxAirSupply() * SURFACE_NEED_RECOVER_THRESHOLD;
        } else if (this.getAirSupply() < this.getMaxAirSupply() * SURFACE_NEED_AIR_THRESHOLD) {
            this.needsAirUrgently = true;
        }

        return this.needsAirUrgently;
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

    protected boolean isAirAtEyesWhenInPose(Vec3 pos, Pose pose) {
        BlockPos originalPos = new BlockPos(Mth.floor(pos.x), Mth.floor(pos.y), Mth.floor(pos.z));
        return BlockPos.betweenClosedStream(this.getDimensions(pose).makeBoundingBox(pos).inflate(-0.05)).filter((checkPos) -> checkPos.getY() > originalPos.getY()).allMatch((blockPos) -> this.level().getBlockState(blockPos).getFluidState().isEmpty());
    }

    @Override
    public void updateSwimming() {
//        super.updateSwimming();
        updateSwimmingState();
    }

    protected void updateSwimmingState() {
        if (this.level.isClientSide) return;

        boolean shouldSwim = shouldSwim();

        this.setMaxUpStep(this.isInWater() ? 1.05F : 0.7F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0);

//        if (shouldSwim || this.isInWater()) {
//            this.setPathfindingMalus(BlockPathTypes.WATER, 0);
//        } else {
//            this.setPathfindingMalus(BlockPathTypes.WATER, oldWaterCost);
//        }

        if (isEffectiveAi() && shouldSwim) {
            updateNavigationAndControl(true);
            this.setSwimming(true);
            this.setPose(Pose.SWIMMING);
        } else {
            updateNavigationAndControl(false);
            this.setSwimming(false);
            switchToSafePose();
        }

        if ((!shouldSwim || !wantsToSwim() || this.wantsToSurface()) && this.isAirAtEyesWhenStanding(this.position())) {
            this.setPose(Pose.STANDING);
        } else {
            this.setPose(Pose.SWIMMING);
        }
    }

    protected boolean shouldSwim() {
        if (!this.isInWater() || !this.canFitInWater(this.position())) {
            // Fisicamente não dá pra nadar aqui — não importa quantos motivos existam.
            return false;
        }

        return this.hasReasonToKeepSwimming();
    }

    /**
     * Reúne todo motivo válido pra estar em modo natação, tanto pra COMEÇAR a nadar
     * quanto pra CONTINUAR nadando. A entidade só deve parar de nadar quando nenhum
     * destes for verdadeiro — evita que ela saia do modo natação no meio de uma
     * emergência de ar, ou no meio de perseguir um alvo, só porque um motivo isolado
     * piscou como falso por um tick.
     */
    protected boolean hasReasonToKeepSwimming() {
        LivingEntity target = this.getTarget();

        // Quer perseguir um alvo que está na água.
        if (target != null && target.isInWater()) {
            return true;
        }

        // Ainda está submersa e precisa OU quer respirar — isso exige continuar em
        // modo natação até sair da água, senão perde a física de nado bem na hora
        // que mais precisa dela (subindo pra respirar).
        if (this.isUnderWater() && (this.needsToSurface() || this.wantsToSurface())) {
            return true;
        }

        // Motivo genérico de IA (perseguir presa em água, etc.).
        return this.wantsToSwim();
    }

    @Override
    public void setSwimming(boolean pSwimming) {
        super.setSwimming(pSwimming);
    }

    @Override
    public void updateNavigationAndControl(boolean swimming) {
        if (swimming) {
            this.navigation = this.waterNavigation;
        } else {
            this.navigation = this.groundNavigation;
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
        return pos.getY();
    }

    /* =========================
       === MOVE CONTROL ========
       ========================= */

    public static class SwimableEntityMoveControl extends MoveControl {
        private final AbstractSemiAquaticEntity semiAquaticEntity;

        public SwimableEntityMoveControl(AbstractSemiAquaticEntity semiAquaticEntity) {
            super(semiAquaticEntity);
            this.semiAquaticEntity = semiAquaticEntity;
        }

        public void tick() {
//            this.semiAquaticEntity.updateSwimming();
            LivingEntity livingentity = this.semiAquaticEntity.getTarget();
            if (this.semiAquaticEntity.isSwimming() && this.semiAquaticEntity.wantsToSwim() && this.semiAquaticEntity.isInWater()) {
                if (livingentity != null && livingentity.getY() > this.semiAquaticEntity.getY()) {
                    double dx = livingentity.getX() - this.semiAquaticEntity.getX();
                    double dz = livingentity.getZ() - this.semiAquaticEntity.getZ();
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    this.semiAquaticEntity.setDeltaMovement(this.semiAquaticEntity.getDeltaMovement().add(dx / dist * 0.02, 0.04, dz / dist * 0.02));
                }

                if (this.semiAquaticEntity.wantsToSurface()) {
                    this.semiAquaticEntity.setDeltaMovement(this.semiAquaticEntity.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
                }

                if (this.operation != Operation.MOVE_TO || this.semiAquaticEntity.getNavigation().isDone()) {
                    this.semiAquaticEntity.setSpeed(0.0F);
                    return;
                }

                double dx = this.wantedX - this.semiAquaticEntity.getX();
                double dy = this.wantedY - this.semiAquaticEntity.getY();
                double dz = this.wantedZ - this.semiAquaticEntity.getZ();
                double d3 = Math.sqrt(dx * dx + dy * dy + dz * dz);
                dy /= d3;
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

    /**
     * Top-priority goal: fires only when the entity urgently NEEDS air.
     * Unlike {@link RiseToSurfaceGoal}, this ignores whatever target/combat goal is
     * running and forces the entity toward the surface immediately.
     */
    public static class EmergencyBreatheGoal extends Goal {
        private final AbstractSemiAquaticEntity mob;
        private final double speedModifier;
        private final Level level;

        public EmergencyBreatheGoal(AbstractSemiAquaticEntity entity, double speed) {
            this.mob = entity;
            this.speedModifier = speed;
            this.level = entity.level();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.mob.isInWater() && this.mob.needsToSurface();
        }

        @Override
        public boolean canContinueToUse() {
            // Keep pushing up until the hysteresis in needsToSurface() clears,
            // or until we're no longer in water (e.g. we made it out).
            return this.mob.isInWater() && this.mob.needsToSurface();
        }

        @Override
        public void start() {
            retarget();
        }

        @Override
        public void tick() {
            // Air pocket may have moved (current pushed the mob, etc.), so keep re-aiming up.
            if (this.mob.getNavigation().isDone()) {
                retarget();
            }
        }

        private void retarget() {
            double x = this.mob.getX();
            double y = this.mob.getWaterSurfaceY(this.mob.blockPosition()) + 1;
            double z = this.mob.getZ();
            this.mob.getNavigation().moveTo(x, y, z, this.speedModifier);
        }

        @Override
        public boolean isInterruptable() {
            // Never let a lower-priority goal (like re-acquiring a target) interrupt breathing.
            return false;
        }
    }

    public static class SinkFromSurfaceGoal extends Goal {
        private final AbstractSemiAquaticEntity mob;
        private double wantedX;
        private double wantedY;
        private double wantedZ;
        private final double speedModifier;
        private final Level level;

        public SinkFromSurfaceGoal(AbstractSemiAquaticEntity entity, double speed) {
            this.mob = entity;
            this.speedModifier = speed;
            this.level = entity.level();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (this.mob.getTarget() != null) {
                return false;
            } else if (!this.mob.isInWater()) {
                return false;
            } else if (this.mob.wantsToSurface()) {
                return false;
            } else if (!this.level.getBlockState(this.mob.blockPosition()).isAir()) {
                return false;
            } else if (!this.mob.canFitInWater(this.mob.position())) {
                return false;
            } else {
                this.wantedX = this.mob.getX();
                this.wantedY = this.mob.getY() - (double) 1.0F;
                this.wantedZ = this.mob.getZ();
                return true;
            }
        }

        public boolean canContinueToUse() {
            if (this.mob.getTarget() != null) {
                return false;
            } else {
                return !this.mob.getNavigation().isDone();
            }
        }

        public void start() {
            this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        }

        @javax.annotation.Nullable
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

    public static class RiseToSurfaceGoal extends Goal {
        private final AbstractSemiAquaticEntity mob;
        private double wantedX;
        private double wantedY;
        private double wantedZ;
        private final double speedModifier;
        private final Level level;

        public RiseToSurfaceGoal(AbstractSemiAquaticEntity entity, double speed) {
            this.mob = entity;
            this.speedModifier = speed;
            this.level = entity.level();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (this.mob.getTarget() != null) {
                return false;
            } else if (!this.mob.wantsToSurface()) {
                return false;
            } else if (!this.mob.isInWater()) {
                return false;
            } else if (this.level.getBlockState(EntityUtil.getEyeBlock(this.mob)).isAir()) {
                return false;
            } else {
                this.wantedX = this.mob.getX();
                this.wantedY = this.mob.getY() + (double) 1.0F;
                this.wantedZ = this.mob.getZ();
                return true;
            }
        }

        public boolean canContinueToUse() {
            if (this.mob.getTarget() != null) {
                return false;
            } else {
                return !this.mob.getNavigation().isDone();
            }
        }

        public void start() {
            this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        }

        @javax.annotation.Nullable
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