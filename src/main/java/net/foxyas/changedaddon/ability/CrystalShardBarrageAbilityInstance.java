package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.ability.CrystalShardBarrageAbility.OrbitType;
import net.foxyas.changedaddon.entity.projectile.LuminarCrystalShardProjectile;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.util.EntityUtil;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.foxyas.changedaddon.ability.CrystalShardBarrageAbility.FireMode;

public class CrystalShardBarrageAbilityInstance extends AbstractAbilityInstance {

    // ---- Configuration ----
    protected static final int COOLDOWN = 200;
    protected static final int PROJECTILE_COUNT = 5;
    protected static final double CIRCLE_RADIUS = 2.5D;
    protected static final int CHARGE_DURATION = 40;   // ticks held before firing starts
    protected static final int FIRING_INTERVAL = 8;    // ticks between shots (SEQUENTIAL)
    protected static final double HOVER_FOLLOW = 0.6D;
    protected static final double ORBIT_SPIN_SPEED = 0.025D;
    protected static final double AIM_RANGE = 48.0D;

    public final List<LuminarCrystalShardProjectile> shards = new ArrayList<>();
    public OrbitType orbitType = OrbitType.HORIZONTAL_AROUND;
    public FireMode fireMode = FireMode.SEQUENTIAL;
    public int projectileCount = PROJECTILE_COUNT;
    public int fireIndex = 0;
    public int lastFireTick = -1;

    public CrystalShardBarrageAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    // ------------------------------------------------------------------
    // Ability lifecycle
    // ------------------------------------------------------------------

    @Override
    public boolean canUse() {
        return ability.canUse(entity);
    }

    @Override
    public boolean canKeepUsing() {
        // Shards only exist server side, so the client just keeps going until the key is released
        if (entity.getLevel().isClientSide) {
            return true;
        }
        LivingEntity living = entity.getEntity();
        return living.isAlive() && fireIndex < shards.size();
    }

    @Override
    public void tickIdle() {
        super.tickIdle();
    }

    @Override
    public void startUsing() {
        if (!(entity.getLevel() instanceof ServerLevel level)) {
            return;
        }
        LivingEntity owner = entity.getEntity();

        clearShards();
        fireIndex = 0;
        lastFireTick = -1;
        projectileCount = PROJECTILE_COUNT;

        OrbitType[] types = OrbitType.values();
        orbitType = types[owner.getRandom().nextInt(types.length)];

        this.fireMode = this.entity.isCrouching() ? FireMode.ALL_AT_ONCE : FireMode.SEQUENTIAL;

        Vec3 aim = getAimPoint(owner);
        for (int i = 0; i < projectileCount; i++) {
            LuminarCrystalShardProjectile shard = createProjectile(level, owner, calculateSlot(owner, i, 0));
            shard.setDeltaMovement(Vec3.ZERO);
            EntityUtil.forceProjectileLookAtVec3(shard, aim);
            level.addFreshEntity(shard);
            shards.add(shard);
        }

        level.playSound(null, owner, SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.5f, 1.5f);
    }

    @Override
    public void tick() {
        if (!(entity.getLevel() instanceof ServerLevel level)) {
            return;
        }
        LivingEntity owner = entity.getEntity();

        int ticks = getController().getHoldTicks();
        Vec3 aim = getAimPoint(owner);

        updateHoverPositions(owner, ticks, aim);

        if (ticks < CHARGE_DURATION || ticks == lastFireTick) {
            return;
        }

        if (fireIndex >= shards.size()) {
            applyCooldown();
            return;
        }

        if (entity.isCrouching()) {
            this.fireMode = FireMode.ALL_AT_ONCE;
        }

        switch (fireMode) {
            case ALL_AT_ONCE -> fireRemaining(level, owner, true);
            case SEQUENTIAL -> {
                if ((ticks - CHARGE_DURATION) % FIRING_INTERVAL == 0) {
                    lastFireTick = ticks;
                    fireShard(level, owner, shards.get(fireIndex));
                    fireIndex++;
                    owner.swing(InteractionHand.MAIN_HAND, true);
                    playFireSound(level, owner);
                }
            }
        }
    }

    @Override
    public void stopUsing() {
        if (entity.getLevel() instanceof ServerLevel level) {
            LivingEntity owner = entity.getEntity();
            boolean charged = lastFireTick != -1 || (getController().getHoldTicks() >= CHARGE_DURATION * 0.25f);
            if (owner.isAlive() && charged && fireIndex < shards.size()) {
                // Released after the charge: dump what's left
                fireRemaining(level, owner, false);
            }
        }
        clearShards();
        applyCooldown();
    }

    @Override
    public void onRemove() {
        clearShards();
    }

    protected void applyCooldown() {
        this.getController().forceCooldown(this.ability.getCoolDown(entity));
    }

    private void clearShards() {
        for (int i = fireIndex; i < shards.size(); i++) {
            LuminarCrystalShardProjectile shard = shards.get(i);
            if (shard != null && shard.isAlive()) {
                shard.onHitSomething();
                shard.discard();
            }
        }
        shards.clear();
        fireIndex = 0;
    }

    // ------------------------------------------------------------------
    // Firing
    // ------------------------------------------------------------------

    private void fireRemaining(ServerLevel level, LivingEntity owner, boolean markTick) {
        for (int i = fireIndex; i < shards.size(); i++) {
            fireShard(level, owner, shards.get(i));
        }
        fireIndex = shards.size();
        if (markTick) {
            lastFireTick = getController().getHoldTicks();
        }
        owner.swing(InteractionHand.MAIN_HAND, true);
        playFireSound(level, owner);
    }

    private void fireShard(ServerLevel level, LivingEntity owner, LuminarCrystalShardProjectile shard) {
        if (shard == null || !shard.isAlive()) {
            return;
        }

        Vec3 startPos = shard.position();
        Vec3 aim = getAimPoint(owner);
        Vec3 dir = aim.subtract(startPos);
        dir = dir.lengthSqr() < 1.0E-4D ? owner.getLookAngle() : dir.normalize();

        // Don't let the shot start inside / cross the owner's hitbox
        AABB inflated = owner.getBoundingBox().inflate(0.5D);
        if (inflated.contains(startPos) || inflated.clip(startPos, startPos.add(dir.scale(CIRCLE_RADIUS * 2))).isPresent()) {
            Vec3 safe = owner.getEyePosition().add(dir.scale(owner.getBbWidth() + 1.2D));
            shard.setPos(safe.x, safe.y, safe.z);
        }

        shard.setNoGravity(false);
        shard.shoot(dir.x, dir.y, dir.z, 2.0f, 0.2f);
        shard.hasImpulse = true;
    }

    private void playFireSound(ServerLevel level, LivingEntity owner) {
        level.playSound(null, owner.getX(), owner.getEyeY(), owner.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    // ------------------------------------------------------------------
    // Aiming: where the player is looking (entity first, then block, then max range)
    // ------------------------------------------------------------------

    private Vec3 getAimPoint(LivingEntity owner) {
        Level level = owner.level();
        Vec3 eye = owner.getEyePosition();
        Vec3 look = owner.getLookAngle();
        Vec3 end = eye.add(look.scale(AIM_RANGE));

        BlockHitResult blockHit = level.clip(new ClipContext(eye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, owner));
        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }

        AABB box = owner.getBoundingBox().expandTowards(look.scale(AIM_RANGE)).inflate(1.0D);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                owner, eye, end, box,
                e -> !e.isSpectator() && e.isPickable() && !(e instanceof Projectile) && e != owner,
                AIM_RANGE * AIM_RANGE
        );
        if (entityHit != null) {
            return entityHit.getEntity().getBoundingBox().getCenter();
        }
        return end;
    }

    // ------------------------------------------------------------------
    // Orbit
    // ------------------------------------------------------------------

    private void updateHoverPositions(LivingEntity owner, int ticks, Vec3 aim) {
        for (int i = fireIndex; i < shards.size(); i++) {
            LuminarCrystalShardProjectile shard = shards.get(i);
            if (shard == null || !shard.isAlive()) {
                continue;
            }
            Vec3 next = shard.position().lerp(calculateSlot(owner, i, ticks), HOVER_FOLLOW);
            shard.setPos(next.x, next.y, next.z);
            shard.setDeltaMovement(Vec3.ZERO);
            shard.hasImpulse = true;
            EntityUtil.forceProjectileLookAtVec3(shard, aim);
        }
    }

    private Vec3 calculateSlot(LivingEntity owner, int index, int currentTick) {
        Vec3 headPos = owner.getEyePosition();

        if (orbitType == OrbitType.HORIZONTAL_AROUND) {
            double angle = index * ((2 * Math.PI) / projectileCount) + currentTick * ORBIT_SPIN_SPEED;
            return headPos.add(CIRCLE_RADIUS * Math.cos(angle), 0, CIRCLE_RADIUS * Math.sin(angle));
        }

        double angleStep = projectileCount > 1 ? Math.PI / (projectileCount - 1) : 0;
        double angle = index * angleStep + Math.sin(currentTick * 0.05 + index) * 0.05;

        float yRot = owner.getYRot();
        float radYaw = yRot * Mth.DEG_TO_RAD;
        Vec3 rightVec = new Vec3(-Math.cos(radYaw), 0, -Math.sin(radYaw));

        Vec3 arcCenter = headPos;
        if (orbitType == OrbitType.VERTICAL_BEHIND) {
            Vec3 lookVec = Vec3.directionFromRotation(0, yRot);
            arcCenter = headPos.subtract(lookVec.scale(owner.getBbWidth() + 0.8D));
        }

        return arcCenter
                .add(rightVec.scale(CIRCLE_RADIUS * Math.cos(angle)))
                .add(0, CIRCLE_RADIUS * Math.sin(angle), 0);
    }

    // ------------------------------------------------------------------
    // Projectile
    // ------------------------------------------------------------------

    private @NotNull LuminarCrystalShardProjectile createProjectile(ServerLevel level, LivingEntity owner, Vec3 spawnPos) {
        RandomSource random = owner.getRandom();

        LuminarCrystalShardProjectile projectile = new LuminarCrystalShardProjectile(ChangedAddonEntities.LUMINAR_CRYSTAL_SHARD.get(), level) {
            @Override
            protected boolean canHitEntity(@NotNull Entity target) {
                if (target.is(owner)) {
                    return false;
                }
                return super.canHitEntity(target);
            }

            @Override
            protected SoundEvent getDefaultHitGroundSoundEvent() {
                return SoundEvents.AMETHYST_BLOCK_BREAK;
            }

            @Override
            protected float getGravity() {
                return super.getGravity() / Math.max(0.01f, (float) this.getDeltaMovement().length());
            }

            @Override
            public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
                super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
                this.setNoGravity(false);
                this.setHealth(1);
                this.setRotFollowMotion(true);
            }

            @Override
            public void shootFromRotation(@NotNull Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
                super.shootFromRotation(pShooter, pX, pY, pZ, pVelocity, pInaccuracy);
                this.setNoGravity(false);
                this.setHealth(1);
                this.setRotFollowMotion(true);
            }

            @Override
            public boolean shouldBreakOnImpact(BlockHitResult hitResult) {
                return true;
            }

            @Override
            public void breakOnImpact(BlockHitResult hitResult) {
                super.breakOnImpact(hitResult);
                ParticlesUtil.sendParticles(this.level, new BlockParticleOption(ParticleTypes.BLOCK, ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState()), this.position(), 0.05f, 0.05f, 0.05f, 8, 0.5f);
            }
        };

        projectile.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        projectile.setOwner(owner);
        projectile.setNoGravity(true);
        projectile.pickup = AbstractArrow.Pickup.DISALLOWED;
        projectile.setCritArrow(random.nextBoolean());
        projectile.setKnockback(2);
        projectile.setBaseDamage(5f);
        projectile.setPierceLevel((byte) random.nextInt(3));
        projectile.setHealth(16);
        projectile.setRotFollowMotion(false);
        EntityUtil.forceProjectileLookAtVec3(projectile, getAimPoint(owner));


        return projectile;
    }
}