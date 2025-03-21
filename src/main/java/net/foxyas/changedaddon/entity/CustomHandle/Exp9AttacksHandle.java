package net.foxyas.changedaddon.entity.CustomHandle;

import net.foxyas.changedaddon.entity.KetExperiment009BossEntity;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.foxyas.changedaddon.process.util.DelayedTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

import net.minecraft.world.entity.player.Player;

public class Exp9AttacksHandle {

    public static class TeleportComboGoal extends Goal {
        private final KetExperiment009BossEntity boss;
        private LivingEntity target;
        private int phase = 0;
        private int ticks = 0;
        private final Random random = new Random();

        public TeleportComboGoal(KetExperiment009BossEntity boss) {
            this.boss = boss;
        }

        @Override
        public boolean canUse() {
            this.target = boss.getTarget();

            if (target instanceof Player player && (player.isCreative() || player.isSpectator())) {
                return false; // Evita atacar jogadores no modo criativo ou spectator
            }

            return target != null && (boss.distanceTo(target) >= 4 && boss.distanceTo(target) <= 16) && (boss.isPhase2() ? true : random.nextFloat() >= 0.25f);
        }

        @Override
        public boolean canContinueToUse() {
            if (target instanceof Player player) {
                if (player.isCreative() || player.isSpectator()) {
                    return false;
                }
            }


            return phase < 5 && target != null && target.isAlive();
        }


        @Override
        public void start() {
            phase = 0;
            ticks = 0;
            teleportTargetUp();
        }

        @Override
        public void tick() {
            ticks++;

            if (ticks % 5 == 0) { // Executa a cada segundo
                switch (phase) {
                    case 1, 2, 3 -> followAndKnockback();
                    case 4 -> hammerSmash();
                }
                phase++;
            }
        }

        @Override
        public void stop() {
            whenStopUsing();
        }

        private void whenStopUsing() {
            if (!boss.isOnGround()) { // Se o boss estiver no ar
                double x = boss.getX();
                double z = boss.getZ();

                // Obtém a altura do primeiro bloco sólido abaixo
                int groundY = boss.level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) x, (int) z);

                // Define a nova posição para o teleporte (levemente acima do chão para evitar clipagem)
                Vec3 smashPos = new Vec3(x, groundY + 0.5, z);

                // Teleporta o boss para o chão dinamicamente
                boss.teleportTo(smashPos.x, smashPos.y, smashPos.z);
                boss.SpawnThunderBolt(smashPos);
            }
        }

        private void teleportTargetUp() {
            Vec3 newPos = target.position().add(0, 10, 0);
            target.teleportTo(newPos.x, newPos.y, newPos.z);
            this.boss.swing(InteractionHand.MAIN_HAND);
            target.hurt(boss.ThunderDmg, 4);
            boss.setAttackCoolDown(0);

            // Aplica Slow Falling no boss e no target por 4 segundos (80 ticks)
            MobEffectInstance slowFallingEffect = new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, false, false);
            target.addEffect(slowFallingEffect);
            boss.addEffect(slowFallingEffect);

            spawnThunder(newPos);
        }


        private void followAndKnockback() {
            Vec3 knockDir = target.getLookAngle().scale(-1).add(0, 0.5, 0);
            boss.teleportTo(target.getX(), target.getY(), target.getZ());
            target.setDeltaMovement(knockDir);
            this.boss.swing(InteractionHand.MAIN_HAND);
            target.hurt(boss.ThunderDmg, 2);
            spawnParticles(target.position());
        }

        private void hammerSmash() {
            // Aplica o impacto ao alvo
            target.setDeltaMovement(0, -3, 0);
            boss.swing(InteractionHand.MAIN_HAND);
            target.hurt(boss.ThunderDmg, 4);
            boss.setAttackCoolDown(0);

            // Remove Slow Falling do boss e do target.
            //target.removeEffect(MobEffects.SLOW_FALLING);
            boss.removeEffect(MobEffects.SLOW_FALLING);

            // Spawn de partículas no local do impacto
            spawnParticles(target.position());
        }


        private void spawnParticles(Vec3 pos) {
            if (boss.level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.FLASH, pos.x, pos.y, pos.z, 10, 0.5, 0.5, 0.5, 0);
                boss.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 2, 1);
            }
        }

        private void spawnThunder(Vec3 pos) {
            if (boss.level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.FLASH, pos.x, pos.y, pos.z, 10, 0.5, 0.5, 0.5, 0);
                boss.SpawnThunderBolt(pos);
            }
        }
    }


    public static class ThunderWave extends Goal {

        public final KetExperiment009BossEntity boss;

        public ThunderWave(KetExperiment009BossEntity boss) {
            super();
            this.boss = boss;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.getTarget();
            if (target == null) return false;

            double distance = this.boss.distanceTo(target);
            return boss.getAttackCoolDown() >= 100 && distance < 5;
        }

        @Override
        public void start() {
            run();
        }

        public void run() {
            thunderWave();
            boss.setAttackCoolDown(0); // Reseta a AI
        }

        public LivingEntity getTarget() {
            return this.boss.getTarget();
        }

        private void thunderWave() {
            if (this.getTarget() != null) {
                if (boss.level instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 5; i++) {
                        double offsetX = (boss.getRandom().nextDouble() - 0.5) * 6;
                        double offsetZ = (boss.getRandom().nextDouble() - 0.5) * 6;
                        BlockPos pos = new BlockPos(this.boss.getX() + offsetX, this.boss.getY(), this.boss.getZ() + offsetZ);
                        if (serverLevel.getBlockState(pos.below()).getBlock() != Blocks.AIR) {
                            this.boss.SpawnThunderBolt(pos);
                        }
                    }
                }
            }
        }
    }

    public static class ThunderSpeed extends Goal {

        public final KetExperiment009BossEntity boss;

        public ThunderSpeed(KetExperiment009BossEntity boss) {
            super();
            this.boss = boss;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.getTarget();
            if (target == null) return false;

            double distance = this.boss.distanceTo(target);
            return boss.getAttackCoolDown() >= 100 && distance > 5;
        }


        public LivingEntity getTarget() {
            return this.boss.getTarget();
        }

        @Override
        public void start() {
            run();
        }

        public void run() {
            thunderSpeed();
            boss.setAttackCoolDown(0); // Reseta a AI
        }

        private void thunderSpeed() {
            if (this.getTarget() != null) {
                LivingEntity target = this.getTarget();
                Vec3 direction = target.position().subtract(this.boss.position()).normalize();
                this.boss.setDeltaMovement(direction.scale(1.5));
                new DelayedTask(10, this.boss, (e) -> {
                    if (this.boss.distanceTo(target) < 2.5) {
                        this.boss.SpawnThunderBolt(target.position());
                        target.hurt(this.boss.ThunderDmg, 4);
                        target.setDeltaMovement(target.getDeltaMovement().add(new Vec3(0, 1f, 0)));
                    }
                });
            }
        }
    }

    public static class ThunderShock extends Goal {

        public final KetExperiment009BossEntity boss;

        public ThunderShock(KetExperiment009BossEntity boss) {
            super();
            this.boss = boss;
        }

        public LivingEntity getTarget() {
            return this.boss.getTarget();
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.getTarget();
            if (target == null) return false;

            double distance = this.boss.distanceTo(target);
            return boss.getAttackCoolDown() >= 100 && distance <= 10;
        }

        @Override
        public void start() {
            run();
        }

        public void run() {
            thunderShock();
            boss.setAttackCoolDown(0); // Reseta a AI
        }

        private void thunderShock() {
            if (this.getTarget() != null) {

                LivingEntity target = this.getTarget();

                // Pegando a posição dos olhos da entidade
                Vec3 startPos = this.boss.position().add(0, this.boss.getEyeHeight(), 0); // Posiciona no nível dos olhos
                Vec3 endPos = target.position().add(0, target.getEyeHeight() * 0.5, 0); // Meio do corpo do alvo

                // Criando partículas visuais para o raio com efeito de ziguezague
                for (int i = 0; i < 10; i++) {
                    double progress = i / 10.0;
                    // Calculando a posição intermediária do raio
                    Vec3 pos = startPos.add(endPos.subtract(startPos).scale(progress));

                    // Introduzindo variação aleatória para criar um efeito zig-zag
                    double offsetX = (Math.random() - 0.5) * 0.5; // Variação aleatória lateral (±0.5)
                    double offsetY = (Math.random() - 0.5) * 0.5; // Variação aleatória vertical (±0.5)
                    double offsetZ = (Math.random() - 0.5) * 0.5; // Variação aleatória ao longo do eixo Z

                    // Aplicando a variação
                    Vec3 zigzagPos = pos.add(offsetX, offsetY, offsetZ);


                    BlockHitResult hitResult = this.boss.level.clip(new ClipContext(this.boss.getEyePosition(1f), this.boss.getEyePosition(1f).add(this.boss.getViewVector(1f).scale(5)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.boss)); //adicione aqui um get block
                    //BlockState blockState = this.boss.level.getBlockState(new BlockPos(pos));
                    var state = this.boss.getLevel().getBlockState(hitResult.getBlockPos());

                    // Verificando se o bloco é metálico (exemplo usando ferro, mas pode ser expandido)
                    if (hitResult.getType() == HitResult.Type.MISS) {
                        if (this.boss.level instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, zigzagPos.x, zigzagPos.y, zigzagPos.z, 1, 0, 0, 0, 0);
                        }
                    } else if (!state.isAir()) {
                        if (state.is(Blocks.IRON_BLOCK) || state.is(Blocks.GOLD_BLOCK) || state.is(Blocks.COPPER_BLOCK)) {
                            break;
                        }
                    }

                }

                EntityHitResult hitResult = PlayerUtilProcedure.getEntityHitLookingAt(this.boss, 10);

                // Verifique o resultado do hit
                if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
                    Entity hitEntity = hitResult.getEntity();
                    // Se a entidade atingida for o alvo
                    if (hitEntity instanceof LivingEntity livingEntity && this.boss.canAttack(livingEntity)) {
                        livingEntity.hurt(this.boss.ThunderDmg, 4); // Causa dano a outra entidade
                        this.boss.swing(InteractionHand.MAIN_HAND);
                    }
                }

            }
        }
    }

    public static class ThunderPathway extends Goal {

        public final KetExperiment009BossEntity boss;

        public ThunderPathway(KetExperiment009BossEntity boss) {
            super();
            this.boss = boss;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.getTarget();
            if (target == null) return false;

            double distance = this.boss.distanceTo(target);
            return boss.getAttackCoolDown() >= 100 && distance >= 5;
        }


        public LivingEntity getTarget() {
            return this.boss.getTarget();
        }

        @Override
        public void start() {
            run();
        }

        public void run() {
            thunderPathway();
            boss.setAttackCoolDown(0); // Reseta a AI
        }

        private void thunderPathway() {
            if (this.getTarget() != null && this.boss.level instanceof ServerLevel) {
                Vec3 forward = this.boss.getLookAngle().scale(5); // Direção para frente (ajustada pela rotação da entidade)
                for (int i = 0; i < 5; i++) {
                    if (this.boss.isPhase2()) {
                        forward = this.boss.getLookAngle().scale(5); // Direção para frente, se estiver na fase 2
                    }

                    Vec3 startPos = this.boss.position();
                    Vec3 currentPos = startPos.add(forward.scale(i)); // Avança na direção do olhar
                    this.boss.SpawnThunderBolt(currentPos);
                }
            }
        }

    }

    public static class ThunderStorm extends Goal {

        public final KetExperiment009BossEntity boss;

        public ThunderStorm(KetExperiment009BossEntity boss) {
            super();
            this.boss = boss;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.getTarget();
            if (target != null) {
                double distance = this.boss.distanceTo(target);
                return distance <= 6;
            }
            return true;
        }


        public LivingEntity getTarget() {
            return this.boss.getTarget();
        }

        @Override
        public void start() {
            run();
        }

        public void run() {
            thunderStorm();
        }


        private void thunderStorm() {
            if (this.boss.level instanceof ServerLevel) {
                if (this.boss.getTarget() == null) {
                    for (int i = 0; i < 7; i++) {
                        double offsetX = (boss.getRandom().nextDouble() - 0.5) * 10;
                        double offsetZ = (boss.getRandom().nextDouble() - 0.5) * 10;
                        BlockPos pos = new BlockPos(this.boss.getX() + offsetX, this.boss.getY(), this.boss.getZ() + offsetZ);
                        this.boss.SpawnThunderBolt(pos);
                    }
                } else {
                    for (int i = 0; i < 12; i++) {
                        double offsetX = (boss.getRandom().nextDouble() - 0.5) * 20;
                        double offsetZ = (boss.getRandom().nextDouble() - 0.5) * 20;
                        BlockPos pos = new BlockPos(this.boss.getX() + offsetX, this.boss.getY(), this.boss.getZ() + offsetZ);
                        this.boss.SpawnThunderBolt(pos);
                    }
                }
            }
        }

    }

    public static class TeleportAttack extends Goal {
        private final KetExperiment009BossEntity boss;

        public TeleportAttack(KetExperiment009BossEntity boss) {
            this.boss = boss;
        }

        @Override
        public boolean canUse() {
            return boss.getTarget() != null && boss.getHealth() / boss.getMaxHealth() <= 0.5 && this.boss.distanceTo(boss.getTarget()) >= 6;
        }

        @Override
        public void start() {
            Teleport();
        }

        public void Teleport() {
            LivingEntity target = boss.getTarget();
            if (target == null) {
                return;
            }
            Vec3 targetPos = target.position().add(0, target.getEyeHeight() * 0.5, 0);
            boss.teleportTo(targetPos.x, targetPos.y, targetPos.z);
            target.hurt(boss.ThunderDmg, 2);
            boss.setAttackCoolDown(0);
        }

        public static void Teleport(KetExperiment009BossEntity boss, LivingEntity target) {
            if (target == null) {
                return;
            }
            Vec3 targetPos = target.position().add(0, target.getEyeHeight() * 0.5, 0);
            boss.teleportTo(targetPos.x, targetPos.y, targetPos.z);
            target.hurt(boss.ThunderDmg, 2);
            boss.setAttackCoolDown(0);
        }
    }

    public static class BurstAttack extends Goal {
        private final KetExperiment009BossEntity boss;

        public BurstAttack(KetExperiment009BossEntity boss) {
            this.boss = boss;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = boss.getTarget();
            if (target == null) return false;

            boolean isClose = boss.distanceTo(target) < 3;
            boolean lastHitTimeHigher = boss.getLastHurtByMobTimestamp() > target.getLastHurtByMobTimestamp();
            boolean attackedByAnother = boss.getLastHurtByMob() != null && boss.getLastHurtByMob() != target;

            return isClose && (attackedByAnother);
        }


        @Override
        public void start() {
            BossAbilitiesHandle.ExplosionBurst(this.boss);
        }
    }
}
