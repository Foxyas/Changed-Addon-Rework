package net.foxyas.changedaddon.entity.ai.goals.generic;

import net.foxyas.changedaddon.util.FoxyasUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

import static net.minecraft.tags.BlockTags.FIRE;

public class ExtinguishFireNearbyGoal extends Goal {

    private final PathfinderMob mob;
    private int cooldownTicks = 0;
    private int executionTicks = 0;
    private static final int DURATION = 5; // Duração da animação/giro em ticks (0.5 segundos)

    public ExtinguishFireNearbyGoal(PathfinderMob pathfinderMob) {
        this.mob = pathfinderMob;
        // Permite que outras IAs de movimento ou olhar rodem junto se necessário,
        // mas interrompe se a entidade precisar dar prioridade a outra meta.
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldownTicks > 0) {
            this.cooldownTicks--;
            return false;
        }

        Level level = mob.level();
        BlockPos mobPos = mob.blockPosition();

        // Reduzido o raio para 8 (mais que suficiente) e otimizado para não checar 35k blocos por tick.
        // Procura até achar pelo menos 1 bloco de fogo próximo para ativar.
        boolean hasFireNearby = BlockPos.betweenClosedStream(
                mobPos.offset(-8, -4, -8),
                mobPos.offset(8, 4, 8)
        ).anyMatch(pos -> level.getBlockState(pos).is(FIRE));

        if (!hasFireNearby) {
            this.cooldownTicks = 20; // Checa novamente em 1 segundo se não achar fogo
            return false;
        }

        return true;
    }

    // Retorna true enquanto a animação de rotação/ataque estiver rodando
    @Override
    public boolean canContinueToUse() {
        return this.executionTicks < DURATION;
    }

    @Override
    public boolean isInterruptable() {
        return true; // A meta pode ser interrompida por outros fatores se necessário
    }

    @Override
    public void start() {
        this.executionTicks = 0;
        Level level = mob.level();
        BlockPos mobPos = mob.blockPosition();

        // 1. Apaga o fogo ao redor (Otimizado usando um raio menor e mais preciso)
        FoxyasUtil.betweenClosedStreamSphere(mobPos.offset(-8, -4, -8), mobPos.offset(8, 4, 8))
                .map(BlockPos::immutable)
                .filter(pos -> level.getBlockState(pos).is(FIRE))
                .forEach(pos -> {
                    level.removeBlock(pos, false);
                    level.levelEvent(1009, pos, 0); // Som/Partícula de extinguir do vanilla
                });

        // 2. Toca o som de Sweep (Ataque Giratório)
        level.playSound(null, mob.getX(), mob.getY(), mob.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.HOSTILE, 1.5F, 1.0F);

        // 3. Aplica o Knockback nas entidades próximas
        this.knockbackNearbyEntities(mob, 1.5F, new Vec3(0, 0.2, 0));

        // 4. Spawna as partículas de Sweep em 360° expandindo para fora (efeito de vento)
        if (level instanceof ServerLevel serverLevel) {
            // Definimos a força do vento (velocidade de expansão para fora)
            double windForce = 1;

            // Spawna 8 partículas em círculo ao redor do mob
            for (int i = 0; i < 8; i++) {
                double angle = (i * Math.PI / 4);

                // Direção calculada a partir do ângulo do círculo
                double dirX = Math.cos(angle);
                double dirZ = Math.sin(angle);

                // Posição inicial (spawn inicial a 0.5 blocos de distância do centro)
                double ox = dirX * 1.25f;
                double oz = dirZ * 1.25f;

                // Movimento (motion) direcionado para fora multiplicador pela força do vento
                double motionX = dirX * windForce;
                double motionZ = dirZ * windForce;

                serverLevel.sendParticles(
                        ParticleTypes.SWEEP_ATTACK,
                        mob.getX() + ox, mob.getY(0.5), mob.getZ() + oz,
                        0,                          // Quantidade = 0 (ativa o modo motion nos próximos 3 argumentos)
                        motionX,                    // Velocidade X (empurra para fora no eixo X)
                        0.0D,                       // Velocidade Y (estável verticalmente)
                        motionZ,                    // Velocidade Z (empurra para fora no eixo Z)
                        1.0D                        // Speed multiplicadora (mantenha em 1.0 para respeitar o motion acima)
                );
            }
        }
    }

    @Override
    public void tick() {
        this.executionTicks++;

        // Faz a entidade dar o giro de 360 graus completo dividindo o ângulo pelo tempo de execução
        float progress = (float) this.executionTicks / (float) DURATION;
        float targetYaw = mob.getYRot() + (360.0F / DURATION);

        mob.setYRot(targetYaw);
        mob.setYBodyRot(targetYaw);
        mob.setYHeadRot(targetYaw);
    }

    @Override
    public void stop() {
        this.cooldownTicks = 100; // 5 segundos de cooldown após usar a habilidade
    }

    public void knockbackNearbyEntities(LivingEntity source, float force, Vec3 extraMotion) {
        AABB attackArea = source.getBoundingBox().inflate(6);
        List<LivingEntity> nearby = source.level().getEntitiesOfClass(LivingEntity.class, attackArea);

        for (LivingEntity target : nearby) {
            if (target == source || !source.canAttack(target)) continue;

            // Calcula a direção baseada na posição relativa (empurra para longe do centro do mob)
            double dX = target.getX() - source.getX();
            double dZ = target.getZ() - source.getZ();
            float xForce = (float) Mth.atan2(dZ, dX);

            float zForce = -Mth.cos(xForce);
            float xForceFinal = Mth.sin(xForce);

            target.knockback(force, xForceFinal, zForce);
            target.setDeltaMovement(target.getDeltaMovement().add(extraMotion));

            if (target instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(
                        serverPlayer.getId(),
                        serverPlayer.getDeltaMovement())
                );
            }
        }
    }
}