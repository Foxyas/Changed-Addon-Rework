package net.foxyas.changedaddon.entity.CustomHandle;

import net.foxyas.changedaddon.effect.particles.ChangedAddonParticles;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Exp9AttacksHandle {

    public static class TeleportAirComboGoal extends Goal {
        private final KetExperiment009BossEntity boss;
        private LivingEntity target;
        private int phase = 0;
        private int ticks = 0;
        private int delay = 5;
        private final Random random = new Random();


        public TeleportAirComboGoal(KetExperiment009BossEntity boss) {
            this.boss = boss;
        }

        @Override
        public boolean canUse() {
            this.target = boss.getTarget();

            if (target instanceof Player player && (player.isCreative() || player.isSpectator())) {
                return false; // Evita atacar jogadores no modo criativo ou spectator
            }

            return target != null && target.isOnGround() && (boss.distanceTo(target) >= 7.5 && boss.distanceTo(target) <= 18) && (boss.isPhase2() || (!boss.isPhase2() && random.nextFloat() <= 0.25f));
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
            super.start();
            phase = 0;
            ticks = 0;
            this.delay = this.boss.isPhase2() && this.boss.computeHealthRatio() <= 0.30f ? 3 : 5;
            teleportTargetUp();
        }

        @Override
        public void tick() {
            super.tick();
            ticks++;

            if (ticks % delay == 0) {
                switch (phase) {
                    case 1, 2, 3 -> followAndKnockback();
                    case 4 -> hammerSmash();
                }
                phase++;
            }
        }

        @Override
        public void stop() {
            super.stop();
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
                boss.setAttackCoolDown(0);
            }
        }

        private void teleportTargetUp() {
            SummonTeleportParticles();
            boss.teleportTo(target.position().x, target.position().y, target.position().z);
            boss.setAttackCoolDown(0);
            this.boss.swing(InteractionHand.MAIN_HAND);
            Vec3 newPos = target.position().add(0, 10, 0);
            if (target.isBlocking()) {
                this.phase = 5;
                target.teleportTo(newPos.x, newPos.y * 0.5, newPos.z);
                return;
            }
            target.teleportTo(newPos.x, newPos.y, newPos.z);
            target.invulnerableTime = 0;
            target.hurt(boss.ThunderDmg, 4);

            // Aplica Slow Falling no boss e no target por 4 segundos (80 ticks)
            MobEffectInstance slowFallingEffect = new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, false, false);
            target.addEffect(slowFallingEffect);
            boss.addEffect(slowFallingEffect);

            spawnThunder(newPos);
        }

        private void SummonTeleportParticles() {
            Vec3 posDifference = target.position().subtract(this.boss.position());
            int maxAmount = 20;

            for (int i = 0; i <= maxAmount; i++) {
                double ratio = (double) i / maxAmount;
                Vec3 spawnPos = this.boss.getEyePosition().add(posDifference.scale(ratio));
                PlayerUtilProcedure.ParticlesUtil.sendParticles(
                        this.boss.getLevel(),
                        ChangedAddonParticles.thunderSpark(1),
                        spawnPos,
                        0.25f, 0.25f, 0.25f,
                        5, // quantidade por ponto
                        1  // speed
                );
            }
        }


        private void followAndKnockback() {
            SummonTeleportParticles();
            Vec3 knockDir = target.getLookAngle().scale(-1).add(0, 0.5, 0);
            boss.teleportTo(target.getX(), target.getY(), target.getZ());
            target.setDeltaMovement(knockDir);
            this.boss.swing(InteractionHand.MAIN_HAND);
            target.invulnerableTime = 0;
            target.hurt(boss.ThunderDmg, 2);
            spawnParticles(target.position());
        }

        private void hammerSmash() {
            // Aplica O Rastro de particulas para simular velocidade
            SummonTeleportParticles();

            // Aplica o impacto ao alvo
            target.setDeltaMovement(0, -3, 0);
            boss.swing(InteractionHand.MAIN_HAND);
            target.invulnerableTime = 0;
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

    public static class TeleportComboGoal extends Goal {
        private final KetExperiment009BossEntity boss;
        private LivingEntity target;
        private int phase = 0;
        private int ticks = 0;
        private int delay = 5;
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

            return target != null && target.isOnGround() && (boss.distanceTo(target) >= 7.5 && boss.distanceTo(target) <= 18) && (boss.isPhase2() || (!boss.isPhase2() && random.nextFloat() <= 0.25f));
        }

        @Override
        public boolean canContinueToUse() {
            if (target instanceof Player player) {
                if (player.isCreative() || player.isSpectator()) {
                    return false;
                }
            }


            return phase < 7 && target != null && target.isAlive();
        }


        @Override
        public void start() {
            super.start();
            phase = 0;
            ticks = 0;
            this.delay = this.boss.isPhase2() && this.boss.computeHealthRatio() <= 0.30f ? 3 : 5;
            teleportToTarget();
        }

        @Override
        public void tick() {
            super.tick();
            ticks++;

            if (ticks % delay == 0) {
                switch (phase) {
                    case 1, 2, 3, 4 -> followAttackAndKnockback();
                    case 5 -> UpperCut();
                    case 6 -> hammerSmash();
                }
                phase++;
            }
        }

        @Override
        public void stop() {
            super.stop();
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
                boss.setAttackCoolDown(0);
            }
        }

        private void teleportToTarget() {
            SummonTeleportParticles();
            boss.teleportTo(target.position().x, target.position().y, target.position().z);
            boss.setAttackCoolDown(0);
            this.boss.swing(InteractionHand.MAIN_HAND);
            Vec3 knockDir = target.getLookAngle().scale(-1).add(0, 0.1f, 0);
            Vec3 newPos = target.position().add(0, 0, 0);
            if (target.isBlocking()) {
                target.knockback(2, knockDir.x, knockDir.z);
            } else {
                target.knockback(4, knockDir.x, knockDir.z);
            }
            target.invulnerableTime = 0;
            target.hurt(boss.ThunderDmg, 4);
            spawnThunder(newPos);
        }


        private void followAttackAndKnockback() {
            SummonTeleportParticles();
            Vec3 knockDir = target.getLookAngle().scale(-1).add(0, 0.1f, 0);
            boss.teleportTo(target.getX(), target.getY(), target.getZ());
            target.setDeltaMovement(knockDir);
            this.boss.swing(InteractionHand.MAIN_HAND);
            target.invulnerableTime = 0;
            target.hurt(boss.ThunderDmg, 2);
            spawnParticles(target.position());
        }

        private void UpperCut() {
            // Aplica O Rastro de particulas para simular velocidade
            SummonTeleportParticles();

			
            // Aplica o impacto ao alvo
            target.setDeltaMovement(0, 3, 0);
            boss.teleportTo(target.getX(), target.getY(), target.getZ());
            boss.swing(InteractionHand.MAIN_HAND);
            target.invulnerableTime = 0;
            target.hurt(boss.ThunderDmg, 4);
            boss.setAttackCoolDown(0);

            // Aplica Slow Falling no boss e no target por 4 segundos (80 ticks)
            MobEffectInstance slowFallingEffect = new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, false, false);
            target.addEffect(slowFallingEffect);
            boss.addEffect(slowFallingEffect);

            // Spawn de partículas no local do impacto
            spawnParticles(target.position());
        }

        private void hammerSmash() {
            // Aplica O Rastro de particulas para simular velocidade
            SummonTeleportParticles();

            // Aplica o impacto ao alvo
            target.setDeltaMovement(0, -3, 0);
            boss.teleportTo(target.getX(), target.getY(), target.getZ());
            boss.swing(InteractionHand.MAIN_HAND);
            target.invulnerableTime = 0;
            target.hurt(boss.ThunderDmg, 4);
            boss.setAttackCoolDown(0);

            // Remove Slow Falling do boss e do target.
            //target.removeEffect(MobEffects.SLOW_FALLING);
            boss.removeEffect(MobEffects.SLOW_FALLING);

            // Spawn de partículas no local do impacto
            spawnParticles(target.position());
        }

        private void SummonTeleportParticles() {
            Vec3 posDifference = target.position().subtract(this.boss.position());
            int maxAmount = 20;

            for (int i = 0; i <= maxAmount; i++) {
                double ratio = (double) i / maxAmount;
                Vec3 spawnPos = this.boss.getEyePosition().add(posDifference.scale(ratio));
                PlayerUtilProcedure.ParticlesUtil.sendParticles(
                        this.boss.getLevel(),
                        ChangedAddonParticles.thunderSpark(1),
                        spawnPos,
                        0.25f, 0.25f, 0.25f,
                        5, // quantidade por ponto
                        1  // speed
                );
            }
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
        private int ticks;
        private int thunderIndex;
        private int MaxThunderIndex;
        private boolean running;
        private Vec3 forward; // Recalcula para cada tick

        public ThunderPathway(KetExperiment009BossEntity boss) {
            super();
            this.boss = boss;
            this.MaxThunderIndex = 10;
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
            this.ticks = 0;
            this.thunderIndex = 0;
            this.running = true;
            this.forward = this.boss.getLookAngle().scale(5);
            this.boss.setAttackCoolDown(0); // Reseta o cooldown da IA
        }

        @Override
        public boolean canContinueToUse() {
            return running;
        }

        @Override
        public void tick() {
            ticks++;

            // Spawna um thunder a cada 2 ticks
            if (ticks % 2 == 0 && thunderIndex < MaxThunderIndex) {
                if (this.boss.isPhase2()) {
                    forward = this.boss.getLookAngle().scale(MaxThunderIndex); // Recalcula se necessário
                }

                Vec3 startPos = this.boss.position();
                Vec3 currentPos = startPos.add(forward.scale((double) thunderIndex / MaxThunderIndex));
                this.boss.SpawnThunderBolt(currentPos);

                thunderIndex++;
            }

            // Quando terminar os 5 spawns, finaliza
            if (thunderIndex >= MaxThunderIndex) {
                running = false;
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
            return boss.getTarget() != null && (boss.isOnGround() || boss.getTarget().isOnGround()) && boss.getHealth() / boss.getMaxHealth() <= 0.5 && this.boss.distanceTo(boss.getTarget()) >= 8 && this.boss.getAttackCoolDown() >= 50;
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

    public static class RandomTeleportAttack extends Goal {
        private final KetExperiment009BossEntity boss;

        public RandomTeleportAttack(KetExperiment009BossEntity boss) {
            this.boss = boss;
        }

        @Override
        public boolean canUse() {
            return boss.getTarget() != null && (boss.isOnGround() || boss.getTarget().isOnGround()) && boss.getRandom().nextFloat() <= (boss.isPhase2() ? 0.35f : 0.25f) && this.boss.distanceTo(boss.getTarget()) >= 1.5;
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
        }

        public static void Teleport(KetExperiment009BossEntity boss, LivingEntity target) {
            if (target == null) {
                return;
            }
            Vec3 targetPos = target.position().add(0, target.getEyeHeight() * 0.5, 0);
            boss.teleportTo(targetPos.x, targetPos.y, targetPos.z);
            target.hurt(boss.ThunderDmg, 2);
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

            boolean isClose = boss.distanceTo(target) <= 3;
            boolean lastHitTimeHigher = boss.getLastHurtByMobTimestamp() > target.getLastHurtByMobTimestamp();
            boolean attackedByAnother = boss.getLastHurtByMob() != null && boss.getLastHurtByMob() != target;

            return isClose && (attackedByAnother);
        }


        @Override
        public void start() {
            BossAbilitiesHandle.ExplosionBurst(this.boss);
        }
    }

    public static class ThunderBoltImpactAttack extends Goal {
        private final KetExperiment009BossEntity boss;
        private final List<BlockPos> thunderPositions = new ArrayList<>();
        private int tickDelay = -1;
        private int thunderIndex = 0;

        public ThunderBoltImpactAttack(KetExperiment009BossEntity boss) {
            this.boss = boss;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = boss.getTarget();
            if (target == null) return false;

            boolean isClose = boss.distanceTo(target) <= 3;
            boolean timePassed = boss.tickCount - boss.getLastHurtByMobTimestamp() > 80; // 4s

            return isClose && timePassed;
        }

        @Override
        public void start() {
            thunderPositions.clear();
            thunderIndex = 0;
            tickDelay = 2;

            BlockPos center = boss.blockPosition();

            // Cria uma área circular de 4x4 (raio ~2)
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if (x * x + z * z <= 4) { // raio <= 2.0 (círculo)
                        thunderPositions.add(center.offset(x, 0, z));
                    }
                }
            }

            // Embaralhar (opcional)
            Collections.shuffle(thunderPositions);
        }

        @Override
        public void tick() {
            if (tickDelay > 0) {
                tickDelay--;
                return;
            }

            if (thunderIndex < thunderPositions.size()) {
                BlockPos pos = thunderPositions.get(thunderIndex);

                // Summona trovão visual
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(boss.level);
                if (lightning != null) {
                    lightning.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    lightning.setVisualOnly(true);
                    boss.level.addFreshEntity(lightning);
                }

                // Knockback vertical se tiver entidade perto
                AABB hitbox = new AABB(pos).inflate(1.5); // raio de impacto
                List<LivingEntity> nearby = boss.level.getEntitiesOfClass(LivingEntity.class, hitbox, e -> e != boss && e.isAlive());

                for (LivingEntity e : nearby) {
                    e.setDeltaMovement(e.getDeltaMovement().x, 0.5f, e.getDeltaMovement().z);
                    e.hurtMarked = true;
                    e.hurt(boss.ThunderDmg, 2.5f);
                }

                thunderIndex++;
                tickDelay = 2;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return thunderIndex < thunderPositions.size();
        }

        @Override
        public void stop() {
            thunderPositions.clear();
            thunderIndex = 0;
            tickDelay = -1;
        }
    }

    public static class ThunderBoltAreaAttack extends Goal {
        private final KetExperiment009BossEntity boss;
        private final List<LivingEntity> targets = new ArrayList<>();
        private int tickDelay = -1;
        private int thunderIndex = 0;

        public ThunderBoltAreaAttack(KetExperiment009BossEntity boss) {
            this.boss = boss;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = boss.getTarget();
            if (target == null) return false;

            boolean isClose = boss.distanceTo(target) <= 3;
            boolean timePassed = boss.tickCount - boss.getLastHurtByMobTimestamp() > 80; // 4 segundos

            return isClose && timePassed;
        }

        @Override
        public void start() {
            // Coleta as entidades próximas uma vez
            AABB area = boss.getBoundingBox().inflate(3); // raio de 3 blocos
            List<LivingEntity> nearbyEntities = boss.level.getEntitiesOfClass(LivingEntity.class, area, e -> e != boss && e.isAlive());
            targets.clear();
            targets.addAll(nearbyEntities);

            tickDelay = 2; // delay inicial antes do primeiro trovão
            thunderIndex = 0;
        }

        @Override
        public void tick() {
            if (tickDelay > 0) {
                tickDelay--;
                return;
            }

            if (thunderIndex < targets.size()) {
                LivingEntity entity = targets.get(thunderIndex);

                // Summona trovão visual
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(boss.level);
                if (lightning != null) {
                    lightning.moveTo(entity.getX(), entity.getY(), entity.getZ());
                    lightning.setVisualOnly(true);
                    boss.level.addFreshEntity(lightning);
                }

                // Aplica knockback vertical
                entity.setDeltaMovement(entity.getDeltaMovement().x, 0.5f, entity.getDeltaMovement().z);
                entity.hurtMarked = true;

                thunderIndex++;
                tickDelay = 5; // delay entre cada trovão (5 ticks = 0.25s)
            }
        }

        @Override
        public boolean canContinueToUse() {
            return thunderIndex < targets.size();
        }

        @Override
        public void stop() {
            targets.clear();
            tickDelay = -1;
            thunderIndex = 0;
        }
    }


}
