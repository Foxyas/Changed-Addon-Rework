package net.foxyas.changedaddon.entity.ai.goals.exp9;

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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ThunderDiveGoal extends Goal {

    private static final int FAILSAFE_DIVING = 120;

    protected final IntProvider cooldownProvider;
    private final PathfinderMob mob;
    private final double ascendBoost;     // impulso Y inicial
    private final double ascendHoldY;     // altura alvo acima do chão antes do mergulho
    private final double diveSpeedXZ;     // velocidade lateral no mergulho
    private final double diveSpeedY;      // velocidade vertical para baixo
    private final float ringRadius;       // raio base dos círculos de raio
    public int cooldown = 0;
    private Phase phase;
    private int ticks;
    private int divingTicks;
    private BlockPos startGroundPos;
    private Vec3 lateral = Vec3.ZERO;

    public ThunderDiveGoal(PathfinderMob mob,
                           IntProvider cooldownProvider,
                           double ascendBoost,
                           double ascendHoldY,
                           double diveSpeedXZ,
                           double diveSpeedY,
                           float ringRadius) {
        this.mob = mob;
        this.cooldownProvider = cooldownProvider;
        this.ascendBoost = ascendBoost;
        this.ascendHoldY = ascendHoldY;
        this.diveSpeedXZ = diveSpeedXZ;
        this.diveSpeedY = diveSpeedY;
        this.ringRadius = ringRadius;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    public static void spawnThunderCircle(ServerLevel level, BlockPos center, double radius, int bolts) {
        // garante que os strikes ocorram no topo do terreno naquele XZ
        for (int i = 0; i < bolts; i++) {
            double angle = (2 * Math.PI * i) / bolts;
            double x = center.getX() + 0.5 + radius * Math.cos(angle);
            double z = center.getZ() + 0.5 + radius * Math.sin(angle);

            int topY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor(x), Mth.floor(z));

            if (level.dimensionType().hasCeiling()) {
                // Começa do teto e desce até achar espaço
                int maxY = level.getHeight() - 1;
                for (int y = maxY; y > 0; y--) {
                    BlockPos checkPos = new BlockPos((int) x, y, (int) z);
                    // Verifica se tem 2 blocos de espaço (ou mais, dependendo da entidade)
                    if (level.isEmptyBlock(checkPos) && level.isEmptyBlock(checkPos.above())) {
                        topY = y;
                        break;
                    }
                }
            }

            BlockPos strikePos = new BlockPos(Mth.floor(x), topY, Mth.floor(z));

            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
            if (bolt != null) {
                bolt.moveTo(strikePos.getX() + 0.5, strikePos.getY(), strikePos.getZ() + 0.5);
                bolt.setVisualOnly(false); // true = só visual (sem dano/fogo)
                bolt.setDamage(2f);
                level.addFreshEntity(bolt);
            }
        }
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
        mob.setDeltaMovement(mob.getDeltaMovement().add(dir.x * 0.2, ascendBoost, dir.z * 0.2));
        ChangedSounds.broadcastSound(mob, ChangedSounds.CARDBOARD_BOX_OPEN, 1, 1);

        // pairar levemente enquanto sobe
        mob.setNoGravity(true);
        mob.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0, false, false));
        mob.getNavigation().stop();
    }

    @Override
    public boolean canContinueToUse() {
        if (phase == Phase.DIVE && divingTicks >= FAILSAFE_DIVING) {
            return false;
        }

        return phase != null && !mob.onGround();
    }

    @Override
    public void tick() {
        ticks++;

        LivingEntity t = mob.getTarget();
        if (t != null) {
            mob.getLookControl().setLookAt(t, 90f, 90f);
            mob.setYBodyRot(mob.yHeadRot);
        }

        switch (phase) {
            case ASCEND -> {
                // quando atingir altura desejada (ou após timeout), trocar para DIVE
                boolean highEnough = mob.getY() >= (startGroundPos.getY() + ascendHoldY);
                boolean timeout = ticks > 60; // failsafe
                if (highEnough || timeout) {
                    phase = Phase.DIVE;
                    // reativa gravidade para garantir colisão e onGround corretos
                    mob.setNoGravity(false);

                    // define velocidade de mergulho (para o alvo, mas puxando bem para baixo)
                    Vec3 lateral = Vec3.ZERO;
                    if (t != null) {
                        Vec3 toT = mob.position().vectorTo(t.position());
                        lateral = new Vec3(toT.x, 0, toT.z).normalize().scale(diveSpeedXZ);
                    }
                    mob.setDeltaMovement(lateral.x, -Math.abs(diveSpeedY), lateral.z);
                    this.lateral = lateral;
                } else {
                    // manter um “hover” suave (sem subir indefinidamente)
                    Vec3 dm = mob.getDeltaMovement();
                    // limita subida
                    if (dm.y > 0.6) mob.setDeltaMovement(dm.x, 0.6, dm.z);
                }
            }
            case DIVE -> {
                // reforça queda e correção lateral durante o mergulho
                if (t != null) {
                    mob.setDeltaMovement(lateral.x, -Math.abs(diveSpeedY), lateral.z);
                    Vec3 position = mob.position().add(lateral.x, -Math.abs(diveSpeedY), lateral.z);
                    mob.getLookControl().setLookAt(position.x, position.y, position.z, 90f, 90f);
                    affectNearbyEntities(lateral);
                } else {
                    // sem alvo, só cai
                    mob.setDeltaMovement(0, -Math.abs(diveSpeedY), 0);
                    Vec3 position = mob.position().add(0, -Math.abs(diveSpeedY), 0);
                    mob.getLookControl().setLookAt(position.x, position.y, position.z, 90f, 90f);
                    affectNearbyEntities(new Vec3(0, -Math.abs(diveSpeedY), 0));
                }

                mob.yBodyRot = mob.getYRot();
                divingTicks++;
            }
        }
    }

    private void affectNearbyEntities(Vec3 lateral) {
        for (LivingEntity livingEntity : mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(4))) {
            if (livingEntity.isFallFlying()) {
                if (livingEntity instanceof Player player) player.stopFallFlying();
                livingEntity.setDeltaMovement(lateral.x, -Math.abs(diveSpeedY), lateral.z);
                ChangedSounds.broadcastSound(livingEntity, SoundEvents.PLAYER_ATTACK_CRIT.getLocation(), 1, 1);
            } else if (livingEntity instanceof Player player) {
                if (player.getAbilities().flying) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.getAbilities().flying = false;
                        serverPlayer.setDeltaMovement(lateral.x, -Math.abs(diveSpeedY), lateral.z);
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer.getId(), serverPlayer.getDeltaMovement()));
                        ChangedSounds.broadcastSound(serverPlayer, SoundEvents.PLAYER_ATTACK_CRIT.getLocation(), 1, 1);
                    } else {
                        player.getAbilities().flying = false;
                        player.onUpdateAbilities();
                        player.setDeltaMovement(lateral.x, -Math.abs(diveSpeedY), lateral.z);
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

        // Anel de trovões em 4 ondas (outline em XZ)
        applyKnockBack(center);
        spawnThunderCircle(serverLevel, center, ringRadius, 6);
        DelayedTask.schedule(5, () -> spawnThunderCircle(serverLevel, center, ringRadius * 1.4, 4));
        DelayedTask.schedule(10, () -> spawnThunderCircle(serverLevel, center, ringRadius * 1.8, 8));
        DelayedTask.schedule(15, () -> spawnThunderCircle(serverLevel, center, ringRadius * 2.2, 14));

        // efeito visual simples no chão
        serverLevel.levelEvent(2001, center, Block.getId(Blocks.LIGHTNING_ROD.defaultBlockState()));

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


    private enum Phase {ASCEND, DIVE}
}
