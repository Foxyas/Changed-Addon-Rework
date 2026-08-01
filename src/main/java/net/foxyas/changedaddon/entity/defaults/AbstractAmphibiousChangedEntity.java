package net.foxyas.changedaddon.entity.defaults;

import net.foxyas.changedaddon.entity.ai.ForgeSmoothSwimmingMoveControl;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public abstract class AbstractAmphibiousChangedEntity extends ChangedEntity {

    protected final PathNavigation waterNavigation;
    protected final PathNavigation groundNavigation;

    protected final ForgeSmoothSwimmingMoveControl swimmingMoveControl;
    protected final MoveControl groundMoveControl;

    protected AbstractAmphibiousChangedEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);

        this.groundMoveControl = moveControl;

        // maxTurnX, maxTurnY, inWaterSpeedMod, outsideWaterSpeedMod, applyGravity — ajuste ao seu gosto.
        swimmingMoveControl = new ForgeSmoothSwimmingMoveControl(this, 85, 10, 10f, 1f, true);

        this.waterNavigation = new AmphibiousPathNavigation(this, level);
        this.groundNavigation = new GroundPathNavigation(this, level);
        ((GroundPathNavigation) this.groundNavigation).setCanOpenDoors(true);
        this.groundNavigation.setCanFloat(true);

        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void baseTick() {
        super.baseTick();
    }


    /* =========================
       === DECISION HELPERS ====
       ========================= */

    /**
     * "Quero/preciso procurar ar": junta o soft-want e o hard-need num único ponto de
     * decisão. Sem alvo, sem ar suficiente, ou explicitamente marcado — todos contam.
     */
    protected boolean shouldLookForAir() {
        LivingEntity target = this.getTarget();

        if (!this.isUnderWater()) {
            return false;
        }

        return target == null
                || !target.isInWater()
                || this.getAirSupply() < this.getMaxAirSupply() * 0.5F;
    }

    /**
     * "Realmente dá pra nadar aqui": junto físico (está na água, cabe na água) — não é
     * sobre querer ou não, é sobre ser fisicamente válido entrar em modo natação.
     */
    protected boolean canReallyStartSwimming() {
        return this.isInWater() && canFitInWater(this.position());
    }

    protected boolean canFitInWater(Vec3 pos) {
        float height = this.getDimensions(Pose.STANDING).height;
        BlockPos base = new BlockPos(Mth.floor(pos.x), Mth.floor(pos.y), Mth.floor(pos.z));

        return BlockPos.betweenClosedStream(this.getDimensions(Pose.STANDING).makeBoundingBox(pos).inflate(-0.05))
                .filter(p -> p.getY() == base.getY())
                .allMatch(p -> getWaterDepth(p) >= height);
    }

    protected float getWaterDepth(BlockPos pos) {
        float depth = 0f;
        for (int i = 0; i < 3; i++) {
            var state = this.level().getBlockState(pos.below(i));
            if (state.getFluidState().is(net.minecraft.tags.FluidTags.WATER)) depth++;
            else break;
        }
        for (int i = 1; i < 3; i++) {
            var state = this.level().getBlockState(pos.above(i));
            if (state.getFluidState().is(net.minecraft.tags.FluidTags.WATER)) depth++;
            else break;
        }
        return depth;
    }

    /* =========================
       === SWIM STATE ==========
       ========================= */

    @Override
    public void updateSwimming() {
        if (this.level().isClientSide) return;

        boolean shouldSwim = canReallyStartSwimming();

        this.setMaxUpStep(this.isInWater() ? 1.05F : 0.7F);

        PathNavigation desiredNavigation = shouldSwim ? this.waterNavigation : this.groundNavigation;
        if (this.navigation != desiredNavigation) {
            this.navigation = desiredNavigation;
        }

        MoveControl desiredMover = shouldSwim ? this.swimmingMoveControl : this.groundMoveControl;
        if (this.moveControl != desiredMover) {
            this.moveControl = desiredMover;
        }

        this.setSwimming(shouldSwim);
        this.setPose(shouldSwim ? Pose.SWIMMING : Pose.STANDING);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        // Chamado durante o super() do Mob, antes dos nossos campos existirem — devolve
        // qualquer coisa temporária aqui; o campo real (groundNavigation/waterNavigation)
        // é quem manda depois que o construtor termina e updateSwimming() assume.
        return super.createNavigation(level);
    }

    @Override
    protected @Nullable Goal makeFloatGoal() {
        return null;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new LookForAirGoal(this, 0.8));
    }

    /* =========================
       === GOALS ===============
       ========================= */

    /**
     * Procura o bolsão de ar respirável mais próximo e nada até lá. Prioriza checar
     * reto pra cima primeiro (caso mais comum — superfície aberta), e só cai pra uma
     * varredura por raio ao redor se isso estiver bloqueado (teto de caverna, etc),
     * escolhendo o ponto de ar mais próximo por distância real, não o primeiro achado.
     */
    public static class LookForAirGoal extends Goal {
        private static final int STRAIGHT_UP_SEARCH_HEIGHT = 32;
        private static final int RADIUS_SEARCH_DISTANCE = 8;

        private final AbstractAmphibiousChangedEntity mob;
        private final double speedModifier;
        private final Level level;

        private double wantedX;
        private double wantedY;
        private double wantedZ;

        public LookForAirGoal(AbstractAmphibiousChangedEntity mob, double speedModifier) {
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.level = mob.level();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!this.mob.isInWater() || !this.mob.shouldLookForAir()) {
                return false;
            }

            Vec3 airPos = findNearestAir();
            if (airPos == null) {
                return false;
            }

            this.wantedX = airPos.x;
            this.wantedY = airPos.y;
            this.wantedZ = airPos.z;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return this.mob.isInWater()
                    && this.mob.shouldLookForAir()
                    && !this.mob.getNavigation().isDone();
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        }

        @Override
        public void tick() {
            // Se a corrente empurrou o mob e o caminho antigo já não serve mais,
            // recalcula em cima da posição atual em vez de ficar preso num alvo velho.
            if (this.mob.getNavigation().isDone() && this.mob.shouldLookForAir()) {
                Vec3 airPos = findNearestAir();
                if (airPos != null) {
                    this.wantedX = airPos.x;
                    this.wantedY = airPos.y;
                    this.wantedZ = airPos.z;
                    this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
                }
            }
        }

        @Nullable
        private Vec3 findNearestAir() {
            BlockPos origin = this.mob.blockPosition();

            // 1) Caso comum: checa reto pra cima até achar ar.
            BlockPos.MutableBlockPos cursor = origin.mutable();
            for (int i = 0; i < STRAIGHT_UP_SEARCH_HEIGHT; i++) {
                if (this.level.getBlockState(cursor).getFluidState().isEmpty()) {
                    return Vec3.atBottomCenterOf(cursor);
                }
                cursor.move(Direction.UP);
            }

            // 2) Bloqueado (teto de caverna, etc) — varre um raio ao redor e pega o ar
            // mais próximo de verdade, não o primeiro que aparecer na varredura.
            BlockPos nearest = null;
            double nearestDistSqr = Double.MAX_VALUE;

            for (BlockPos pos : BlockPos.betweenClosed(
                    origin.offset(-RADIUS_SEARCH_DISTANCE, -RADIUS_SEARCH_DISTANCE / 2, -RADIUS_SEARCH_DISTANCE),
                    origin.offset(RADIUS_SEARCH_DISTANCE, RADIUS_SEARCH_DISTANCE, RADIUS_SEARCH_DISTANCE))) {

                if (!this.level.getBlockState(pos).getFluidState().isEmpty()) {
                    continue; // precisa ser ar
                }
                if (this.level.getBlockState(pos.below()).getFluidState().isEmpty()) {
                    continue; // precisa ter água logo abaixo, senão não é uma "saída" da água
                }

                double distSqr = origin.distSqr(pos);
                if (distSqr < nearestDistSqr) {
                    nearestDistSqr = distSqr;
                    nearest = pos.immutable();
                }
            }

            return nearest != null ? Vec3.atBottomCenterOf(nearest) : null;
        }
    }
}