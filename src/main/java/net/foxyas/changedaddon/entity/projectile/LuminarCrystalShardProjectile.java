package net.foxyas.changedaddon.entity.projectile;

import net.foxyas.changedaddon.block.LuminarCrystalSmall;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;

public class LuminarCrystalShardProjectile extends CrystalShardProjectile {

    public static final float MAX_DEFAULT_HEALTH = 1f;
    private static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(LuminarCrystalShardProjectile.class, EntityDataSerializers.FLOAT);
    protected boolean shouldSpawnCrystalsNearby = false;
    protected boolean breakOnImpact = false;

    public LuminarCrystalShardProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public LuminarCrystalShardProjectile(Level level) {
        super(ChangedAddonEntities.LUMINAR_CRYSTAL_SHARD.get(), level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HEALTH, 1f);
    }

    @Override
    public void tick() {
        super.tick();

        if (shouldSpawnCrystalsNearby && !onGround() || inGround()) {
            createCrystalCircleRing();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        onHitSomething();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        onHitSomething();
    }

    @Override
    public boolean shouldBreakOnImpact(BlockHitResult hitResult) {
        return breakOnImpact;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("breakOnImpact", isBreakOnImpact());
        pCompound.putFloat("Health", this.getHealth());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("breakOnImpact")) setBreakOnImpact(pCompound.getBoolean("breakOnImpact"));
        if (pCompound.contains("Health")) setHealth(pCompound.getFloat("Health"));
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        }

        if (pAmount > 0) {
            this.setHealth(this.getHealth() - pAmount);
            this.markHurt();

            if (pAmount > this.getHealth()) {
                onHitSomething();
                this.discard();
            }
        } else {
            this.markHurt();
        }
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        if (isProjectileTheSame(this, pSource.getDirectEntity())) {
            return true;
        }

        return super.isInvulnerableTo(pSource);
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity entity) {
        if (isProjectileTheSame(this, entity)) {
           return false;
        }

        return super.canHitEntity(entity);
    }

    private static boolean isProjectileTheSame(LuminarCrystalShardProjectile shard, Entity projectile) {
        if (projectile instanceof LuminarCrystalShardProjectile luminarCrystalShardProjectile) {
            return luminarCrystalShardProjectile.getOwner() == shard.getOwner();
        }
        return false;
    }

    public void setBreakOnImpact(boolean breakOnImpact) {
        this.breakOnImpact = breakOnImpact;
    }

    public boolean isBreakOnImpact() {
        return breakOnImpact;
    }

    protected void onHitSomething() {
        ParticlesUtil.sendParticles(this.level, new BlockParticleOption(ParticleTypes.BLOCK, ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState()), this.position(), 0.05f, 0.05f, 0.05f, 8, 0.5f);
        this.level.playSound(null, this, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.HOSTILE, 1, 1);
    }

    public boolean isShouldSpawnCrystalsNearby() {
        return shouldSpawnCrystalsNearby;
    }

    public void setShouldSpawnCrystalsNearby(boolean shouldSpawnCrystalsNearby) {
        this.shouldSpawnCrystalsNearby = shouldSpawnCrystalsNearby;
    }

    public float getHealth() {
        return this.entityData.get(HEALTH);
    }

    public void setHealth(float health) {
        this.entityData.set(HEALTH, health);
    }

    protected void createCrystalCircleRing() {
        double scale = getScale();
        double radius = 1.5D * scale;
        int points = 8; // Número de cristais à volta do círculo

        Vec3 center = this.position();

        // Gera os pontos do círculo no plano perpendicular (Right e Up locais)
        for (int i = 0; i < points; i++) {
            double angle = (2.0 * Math.PI / points) * i;
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            // deltaX = Sway (direita/esquerda), deltaY = Heave (cima/baixo), deltaZ = Surge (frente/trás)
            // Multiplicamos o cosseno e o seno pelo raio nos eixos X (Right) e Y (Up)
            double deltaX = cos * radius;
            double deltaY = sin * radius;
            double deltaZ = 0.0; // Mantém no plano transversal imediato do projétil

            // Obtém a posição absoluta no mundo usando o utilitário
            Vec3 pointPos = FoxyasUtil.getRelativePosition(this, deltaX, deltaY, deltaZ, false);
            BlockPos targetPos = BlockPos.containing(pointPos);

            // Tenta encontrar um estado sobrevivível apontando para o centro do projétil
            BlockState survivableState = getSurvivableState(this.level(), targetPos, center);
            if (survivableState != null && this.level().isEmptyBlock(targetPos)) {
                this.level().setBlock(targetPos, survivableState, 3);
            }
        }
    }

    /**
     * Builds the crystal state for {@code pos} that points as close as possible toward
     * {@code facePos} while passing the block's normal survival check (CAN_SURVIVE_ANYWHERE stays
     * off). It never points away from {@code facePos}.
     *
     * @return the state, or null if no valid facing exists here.
     */
    @Nullable
    public static BlockState getSurvivableState(LevelReader level, BlockPos pos, Vec3 facePos) {
        Vec3 toFacePos = facePos.subtract(Vec3.atCenterOf(pos));

        Direction[] facings = Direction.values();
        Arrays.sort(facings, Comparator.comparingDouble((Direction d) -> -facingDot(d, toFacePos)));

        for (Direction facing : facings) {

            if (facingDot(facing, toFacePos) < 0) {
                break;
            }

            BlockState state = ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState()
                    .setValue(LuminarCrystalSmall.FACING, facing)
                    .setValue(LuminarCrystalSmall.CAN_SURVIVE_ANYWHERE, true);

            if (state.canSurvive(level, pos)) {
                return state;
            }
        }
        return null;
    }

    private static double facingDot(Direction facing, Vec3 vec) {
        return facing.getStepX() * vec.x + facing.getStepY() * vec.y + facing.getStepZ() * vec.z;
    }

}
