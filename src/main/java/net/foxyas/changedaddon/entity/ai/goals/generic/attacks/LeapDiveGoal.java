package net.foxyas.changedaddon.entity.ai.goals.generic.attacks;

import net.foxyas.changedaddon.util.DelayedTask;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class LeapDiveGoal extends Goal {

    protected enum Phase {ASCEND, DIVE}

    protected final PathfinderMob mob;
    protected final Vec3 followAscendMultiplier;
    protected final double ascendSpeed;     // impulso Y inicial
    protected final double ascendInitialBoost;
    protected final double ascendHoldY;     // altura alvo acima do chão antes do mergulho
    protected final Vec3 diveSpeedMultiplier; // velocidade lateral e vertical para baixo do mergulho
    protected final float ringRadius;       // raio base dos círculos de raio
    protected final int failSafeTicks;
    protected final IntProvider cooldownProvider;
    protected Phase phase;
    protected int ticks;
    protected BlockPos startGroundPos;
    public int cooldown = 0;
    protected Vec3 lateral = Vec3.ZERO;

    public LeapDiveGoal(PathfinderMob mob,
                        IntProvider cooldownProvider,
                        Vec3 followAscendMultiplier,
                        double ascendSpeed,
                        double ascendInitialBoost,
                        double ascendHoldY,
                        Vec3 diveSpeedMultiplier,
                        float ringRadius,
                        int failSafeTicks) {
        this.mob = mob;
        this.cooldownProvider = cooldownProvider;
        this.followAscendMultiplier = followAscendMultiplier;
        this.ascendSpeed = ascendSpeed;
        this.ascendInitialBoost = ascendInitialBoost;
        this.ascendHoldY = ascendHoldY;
        this.diveSpeedMultiplier = diveSpeedMultiplier;
        this.ringRadius = ringRadius;
        this.failSafeTicks = failSafeTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean canUse() {
        LivingEntity t = mob.getTarget();
        if (cooldown > 0) {
            cooldown--;
            if (t != null && (t.isFallFlying() || !t.onGround())) {
                cooldown -= 2;
            } else if (t instanceof Player player && player.getAbilities().flying) {
                cooldown -= 2;
            }
            return false;
        }
        if (this.mob.isNoGravity()) {
            if (t != null && (t.isFallFlying() || !t.onGround())) {
                return t.isAlive();
            } else if (t instanceof Player player && player.getAbilities().flying) {
                return player.isAlive();
            }
        }
        return t != null && t.isAlive() && mob.onGround();
    }

    @Override
    public void start() {
        this.phase = Phase.ASCEND;
        this.ticks = 0;
        this.startGroundPos = mob.blockPosition();

        // impulso para cima e leve drift em direção ao alvo
        LivingEntity t = mob.getTarget();
        Vec3 dir = (t != null ? mob.position().vectorTo(t.position()).normalize() : Vec3.ZERO);
        mob.setDeltaMovement(mob.getDeltaMovement().add(dir.x * 0.2, ascendInitialBoost, dir.z * 0.2));
        ChangedSounds.broadcastSound(mob, ChangedSounds.CARDBOARD_BOX_OPEN, 1, 1);

        // pairar levemente enquanto sobe
        mob.setNoGravity(true);
        mob.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0, false, false));
        mob.getNavigation().stop();
    }

    @Override
    public boolean canContinueToUse() {
        return phase != null && !mob.onGround();
    }

    @Override
    public void tick() {
        ticks++;

        LivingEntity t = mob.getTarget();
        if (t != null) {
            mob.getLookControl().setLookAt(t, 30.0F, 30.0F);
        }

        switch (phase) {
            case ASCEND -> {
                // quando atingir altura desejada (ou após timeout), trocar para DIVE
                boolean highEnough = isHighEnough(t);

                boolean timeout = ticks > failSafeTicks; // failsafe
                if (highEnough || timeout) {
                    phase = Phase.DIVE;
                    // reativa gravidade para garantir colisão e onGround corretos
                    mob.setNoGravity(false);

                    // define velocidade de mergulho (para o alvo, mas puxando bem para baixo)
                    Vec3 lateral = Vec3.ZERO;
                    if (t != null) {
                        Vec3 toT = mob.position().vectorTo(t.position());
                        lateral = new Vec3(toT.x, 0, toT.z).normalize().multiply(diveSpeedMultiplier.x, 0, diveSpeedMultiplier.z);
                    }
                    mob.setDeltaMovement(lateral.x, -diveSpeedMultiplier.y, lateral.z);
                    this.lateral = lateral;
                } else {
                    // manter um “hover” suave (sem subir indefinidamente)
                    if (t != null) {
                        Vec2 mobXZ = new Vec2((float) mob.getX(), (float) mob.getZ());
                        Vec2 targetXZ = new Vec2((float) t.getX(), (float) t.getZ());
                        if (mobXZ.distanceToSqr(targetXZ) > 2.25) {
                            Vec3 dm = mob.position().vectorTo(t.position()).multiply(followAscendMultiplier.x, 0, followAscendMultiplier.z);
                            mob.setDeltaMovement(dm.x, dm.y, dm.z);
                        }
                        mob.getLookControl().setLookAt(t, 90f, 90f);
                    }
                    mob.push(0, 1 * ascendSpeed, 0);
                }
            }
            case DIVE -> {
                // reforça queda e correção lateral durante o mergulho
                if (t != null) {
                    mob.setDeltaMovement(lateral.x, -diveSpeedMultiplier.y, lateral.z);
                    Vec3 position = mob.position().add(lateral.x, -diveSpeedMultiplier.y, lateral.z);
                    mob.getLookControl().setLookAt(position.x, position.y, position.z, 30f, 30f);
                    affectNearbyEntities(lateral);
                } else {
                    // sem alvo, só cai
                    mob.setDeltaMovement(0, -diveSpeedMultiplier.y, 0);
                    Vec3 position = mob.position().add(0, -diveSpeedMultiplier.y, 0);
                    mob.getLookControl().setLookAt(position.x, position.y, position.z, 30f, 30f);
                    affectNearbyEntities(new Vec3(0, -diveSpeedMultiplier.y, 0));
                }
            }
        }
    }

    protected boolean isHighEnough(LivingEntity t) {
        boolean highEnough = mob.getY() >= (startGroundPos.getY() + (ascendHoldY * 1.25f));
        if (t != null) {
            if (t.isFallFlying() || !t.onGround()) {
                double targetY = t.getEyeY() + ascendHoldY;
                highEnough = mob.getY() >= targetY;
            } else if (t instanceof Player player && player.getAbilities().flying) {
                double targetY = t.getEyeY() + ascendHoldY;
                highEnough = mob.getY() >= targetY;
            }
        }
        return highEnough;
    }

    protected void affectNearbyEntities(Vec3 lateral) {
        for (LivingEntity livingEntity : mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(4))) {
            if (livingEntity.isFallFlying()) {
                if (livingEntity instanceof Player player) player.stopFallFlying();
                livingEntity.setDeltaMovement(lateral.x, -diveSpeedMultiplier.y, lateral.z);
                ChangedSounds.broadcastSound(livingEntity, SoundEvents.PLAYER_ATTACK_CRIT.getLocation(), 1, 1);
            } else if (livingEntity instanceof Player player) {
                if (player.getAbilities().flying) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.getAbilities().flying = false;
                        serverPlayer.onUpdateAbilities();
                        serverPlayer.setDeltaMovement(lateral.x, -diveSpeedMultiplier.y, lateral.z);
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer.getId(), serverPlayer.getDeltaMovement()));
                        ChangedSounds.broadcastSound(serverPlayer, SoundEvents.PLAYER_ATTACK_CRIT.getLocation(), 1, 1);
                    } else {
                        player.getAbilities().flying = false;
                        player.onUpdateAbilities();
                        player.setDeltaMovement(lateral.x, -diveSpeedMultiplier.y, lateral.z);
                        ChangedSounds.broadcastSound(player, SoundEvents.PLAYER_ATTACK_CRIT.getLocation(), 1, 1);
                    }
                }
            }
        }
    }

    @Override
    public void stop() {
        // aterrissou
        mob.setNoGravity(false);
        mob.removeEffect(MobEffects.SLOW_FALLING);
        phase = null;

        if (!(mob.level() instanceof ServerLevel serverLevel)) return;

        BlockPos center = mob.blockPosition();

        // Anel of effects em 4 ondas (outline em XZ)
        applyKnockBack(center);
        spawnBlockBreakParticleCircle(serverLevel, center, ringRadius, 6, 5);
        DelayedTask.schedule(2, () -> spawnBlockBreakParticleCircle(serverLevel, center, ringRadius * 1.4, 4, 4));
        DelayedTask.schedule(5, () -> spawnBlockBreakParticleCircle(serverLevel, center, ringRadius * 1.8, 8, 3));
        DelayedTask.schedule(8, () -> spawnBlockBreakParticleCircle(serverLevel, center, ringRadius * 2.2, 14, 2));

        // efeito visual simples no chão
        // serverLevel.levelEvent(2001, center, Block.getId(Blocks.LIGHTNING_ROD.defaultBlockState()));

        cooldown = cooldownProvider.sample(this.mob.getRandom());
    }

    /* ---------- Utils ---------- */

    public void applyKnockBack(BlockPos pos) {
        var list = mob.level()
                .getEntitiesOfClass(
                        LivingEntity.class,
                        new AABB(pos).inflate(16), (target) -> !target.is(mob));

        for (LivingEntity livingEntity : list) {
            Vec3 direction = livingEntity.position().subtract(mob.position());

            direction = direction.normalize();

            double strength = 10.0 / livingEntity.distanceTo(mob);

            livingEntity.push(
                    direction.x * strength,
                    direction.y * strength * 0.5f,
                    direction.z * strength
            );
        }
    }


    public void spawnBlockBreakParticleCircle(
            ServerLevel level,
            BlockPos center,
            double radius,
            int points,
            int crackStage
    ) {
        int minY = level.getMinBuildHeight() + 1;

        for (int i = 0; i < points; i++) {
            double angle = (Math.PI * 2 * i) / points;
            double x = center.getX() + 0.5 + radius * Math.cos(angle);
            double z = center.getZ() + 0.5 + radius * Math.sin(angle);

            BlockPos startPos = new BlockPos(
                    Mth.floor(x),
                    center.getY(),
                    Mth.floor(z)
            );

            BlockPos validPos = null;
            int y = startPos.getY();

            // 🔍 desce até achar chão sólido + espaço suficiente
            while (y > minY) {
                BlockPos feetPos = new BlockPos(startPos.getX(), y, startPos.getZ());
                BlockPos belowPos = feetPos.below();

                boolean hasSpace = true;
                for (int h = 0; h < mob.getBbHeight(); h++) {
                    if (!level.isEmptyBlock(feetPos.above(h))) {
                        hasSpace = false;
                        break;
                    }
                }

                if (hasSpace && !level.isEmptyBlock(belowPos)) {
                    validPos = feetPos;
                    break;
                }

                y--;
            }

            if (validPos == null) continue;

            BlockState state = level.getBlockState(validPos.below());
            if (state.isAir()) continue;

            level.levelEvent(
                    2001,
                    validPos.below(),
                    Block.getId(state)
            );

            if (crackStage >= 0) {
                level.destroyBlockProgress(
                        mob.getId(),
                        validPos.below(),
                        crackStage
                );
            }
        }
    }

}
