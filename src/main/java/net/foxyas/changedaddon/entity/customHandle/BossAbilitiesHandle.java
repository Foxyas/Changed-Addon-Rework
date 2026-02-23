package net.foxyas.changedaddon.entity.customHandle;

import net.foxyas.changedaddon.block.LuminarCrystalBlock;
import net.foxyas.changedaddon.entity.defaults.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.CameraUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

import java.util.List;

// Class for handling boss abilities
public record BossAbilitiesHandle(AbstractLuminarcticLeopard boss) {

    public static void BurstAttack(LivingEntity livingEntity) {
        if (livingEntity instanceof Mob boss) {
            if (boss.getTarget() != null && boss.distanceTo(boss.getTarget()) <= 2) {
                BossAbilitiesHandle.ExplosionBurst(boss);
            }
        }
    }

    public static void ExplosionBurst(Entity boss) {
        if (boss.level().isClientSide()) {
            return;
        }

        Level world = boss.level();

        world.explode(boss, boss.getX(), boss.getY(), boss.getZ(), 1, Level.ExplosionInteraction.MOB);
        for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(1.5))) {
            if (entity == boss) {
                continue;
            }
            entity.knockback(1.5, boss.getX() - entity.getX(), boss.getZ() - entity.getZ());
        }
    }

    private static List<EntityType<?>> ImmuneEntities() {
        return List.of(ChangedAddonEntities.LUMINARCTIC_LEOPARD_MALE.get(), ChangedAddonEntities.LUMINARCTIC_LEOPARD_FEMALE.get());
    }

    public void tick() {
        // Seleciona aleatoriamente uma habilidade para ser executada
        RandomSource random = boss.level().getRandom();
        int abilityIndex; // 8 habilidades ativas restantes, índices de 0 a
        LivingEntity bossTarget = this.boss.getTarget();


        if (bossTarget != null) {
            if (bossTarget.distanceTo(boss) >= 4) {
                arcticDash();
                abilityIndex = random.nextInt(5);
                switch (abilityIndex) {
                    case 0:
                        arcticDash();
                        break;
                    case 1:
                        irradiatedPulse();
                        break;
                    case 2:
                        radiantField();
                        break;
                    case 3:
                        meltdown();
                        break;
                    case 4:
                        RadioActiveIceExplosion();
                        break;
                }
            } else if (bossTarget.distanceTo(boss) <= 4) {
                // Habilidades ativas
                abilityIndex = random.nextInt(9);
                switch (abilityIndex) {
                    case 0:
                        glacialSpikes();
                        break;
                    case 1:
                        frostNova();
                        break;
                    case 2:
                        crystallineShield();
                        break;
                    case 3:
                        radioactiveBurst();
                        break;
                    case 4:
                        irradiatedPulse();
                        break;
                    case 5:
                        radiantField();
                        break;
                    case 6:
                        meltdown();
                        break;
                    case 7:
                        arcticDash();
                        break;
                    case 8:
                        RadioActiveIceExplosion();
                        break;
                }
            }
        }
    }

    // Radio Ice Explosion
    public void RadioActiveIceExplosion() {
        if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
            for (int theta = 0; theta < 360; theta += 15) { // Ângulo horizontal
                double angleTheta = Math.toRadians(theta);
                for (int phi = 0; phi <= 180; phi += 15) { // Ângulo vertical
                    double anglePhi = Math.toRadians(phi);
                    double x = this.boss.getX() + Math.sin(anglePhi) * Math.cos(angleTheta) * 4.0;
                    double y = this.boss.getY() + Math.cos(anglePhi) * 4.0;
                    double z = this.boss.getZ() + Math.sin(anglePhi) * Math.sin(angleTheta) * 4.0;
                    Vec3 pos = new Vec3(x, y, z);
                    ParticlesUtil.sendParticles(
                            this.boss.level(),
                            ParticleTypes.SOUL_FIRE_FLAME,
                            pos,
                            0.3f, 0.2f, 0.3f,
                            15, 0
                    );
                }
            }
            createIceExplosion();
        }
    }

    public void createIceExplosion() {
        if (boss.level().isClientSide) {
            return; // Garante que o código só execute no servidor
        }

        int radius = 3; // Raio da explosão
        int radiusY = 3;
        BlockPos center = boss.blockPosition();
        Level world = boss.level();

        world.explode(boss, boss.getX(), boss.getY(), boss.getZ(), 1.0f, Level.ExplosionInteraction.MOB);

        Explosion explosionReference = new Explosion(world, boss, boss.getX(), boss.getY(), boss.getZ(), 4.0f, false, Explosion.BlockInteraction.KEEP);

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -radiusY, -radius),
                center.offset(radius, radiusY, radius))) {

            // Cálculo da distância esférica
            double dx = (pos.getX() - center.getX()) / (double) radius;
            double dy = (pos.getY() - center.getY()) / (double) radiusY;
            double dz = (pos.getZ() - center.getZ()) / (double) radius;
            double distanceSq = dx * dx + dy * dy + dz * dz;

            if (distanceSq <= 1.0) { // Somente dentro da esfera
                BlockState state = world.getBlockState(pos);

                // Verifica a resistência à explosão
                float blastResistance = state.getBlock().getExplosionResistance();

                if (!state.isAir() && blastResistance <= 2.0f) {
                    BlockState newState = ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get().defaultBlockState()
                            .setValue(LuminarCrystalBlock.AGE, 0).setValue(LuminarCrystalBlock.DEFROST, true);
                    world.setBlockAndUpdate(pos, newState);
                    // Toca o som de gelo sendo colocado
                    world.playSound(null, pos, SoundEvents.GLASS_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }
        }

        boss.playSound(SoundEvents.GENERIC_EXPLODE, 4.5f, 1);
        boss.AbilitiesTicksCooldown = 125;
    }


    // Arctic Dash Ability
    public void arcticDash() {
        if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0 && boss.onGround()) { // Executa quando o cooldown for 0
            // Primeiro Dash: Direção do olhar do boss
            Vec3 lookDirection = boss.getLookAngle().normalize();
            boss.setDeltaMovement(lookDirection.scale(2.5)); // Dash rápido na direção do olhar
            // Segundo Dash: Direção do boss para o alvo
            LivingEntity target = boss.getTarget();
            if (target != null) {
                this.boss.DashingTicks = 40;
                Vec3 targetDirection = target.position().subtract(boss.position()).normalize();
                boss.setDeltaMovement(targetDirection.scale(2.5)); // Dash rápido para a posição do alvo
                // Para cada entidade dentro da área do dash
                for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(3))) {
                    if (!entity.isInvulnerable() && !ImmuneEntities().contains(entity.getType()) && entity != boss) {
                        entity.hurt(entity.level().damageSources().mobAttack(boss), 4.0f);
                        entity.knockback(1.5, boss.getX() - entity.getX(), boss.getZ() - entity.getZ());

                        // Cria partículas de glow no local da entidade atingida
                        Vec3 pos = new Vec3(entity.position().x, entity.position().y + 0.5, entity.position().z);
                        ParticlesUtil.sendParticles(entity.level, ParticleTypes.GLOW, pos, 0.3f, 0.2f, 0.3f, 25, 1);
                        entity.playSound(SoundEvents.BEACON_AMBIENT, 4.5f, 0);
                    }
                }
                boss.playSound(ChangedSounds.CARDBOARD_BOX_OPEN.get(), 4.5f, 1);
                boss.AbilitiesTicksCooldown = 60; // Set cooldown for the next activation
                this.boss.DashingTicks = 0;
            }
        }
        //this.boss.DashingTicks = 0;
    }


    // Glacial Spikes Ability
    public void glacialSpikes() {
        if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
            for (int i = 0; i < 16; i++) {
                double angle = Math.toRadians(i * 45); // 8 direções
                double x = boss.getX() + Mth.cos((float) angle) * 3.0;
                double z = boss.getZ() + Mth.sin((float) angle) * 3.0;
                Vec3 pos = new Vec3(x, boss.getY(), z);
                ParticlesUtil.sendParticles(this.boss.level(), ParticleTypes.SNOWFLAKE, pos, 0.3f, 0.2f, 0.3f, 15, 0);
                for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(3.0))) {
                    if (!entity.isInvulnerable() && !ImmuneEntities().contains(entity.getType()) && entity != boss) {
                        entity.hurt(entity.level().damageSources().mobAttack(boss), 4.0f);
                        entity.knockback(1.0, boss.getX() - entity.getX(), boss.getZ() - entity.getZ());
                    }
                }
            }
            boss.playSound(SoundEvents.GENERIC_EXPLODE, 4.5f, 1);
            boss.AbilitiesTicksCooldown = 100; // Set cooldown for the next activation
        }
    }

    // Frost Nova Ability
    public void frostNova() {
        if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
            ParticlesUtil.sendParticles(this.boss.level(), ParticleTypes.CLOUD, boss.position(), 0.3f, 0.2f, 0.3f, 15, 0);
            for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(6.0))) {
                if (!entity.isInvulnerable() && !ImmuneEntities().contains(entity.getType()) && entity != boss) {
                    entity.hurt(entity.level().damageSources().mobAttack(boss), 4.0f);
                    if (!entity.level().isClientSide()) {
                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 2));
                    }
                }
            }
            boss.playSound(SoundEvents.GENERIC_EXPLODE, 4.5f, 1);
            boss.AbilitiesTicksCooldown = 150; // Set cooldown for the next activation
        }
    }

    // Crystalline Shield Ability
    public void crystallineShield() {
        if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
            for (int i = 0; i < 16; i++) {
                double angle = Math.toRadians(i * 45); // 8 direções
                double x = boss.getX() + Mth.cos((float) angle) * 3.0;
                double z = boss.getZ() + Mth.sin((float) angle) * 3.0;
                Vec3 pos = new Vec3(x, boss.getY(), z);
                ParticlesUtil.sendParticles(this.boss.level(), ParticleTypes.END_ROD, pos, 0.3f, 0.2f, 0.3f, 15, 0);
            }
            boss.playSound(SoundEvents.BEACON_AMBIENT, 4.5f, 0);
            boss.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 4)); // Resistência por 5 segundos
            boss.AbilitiesTicksCooldown = 80; // Set cooldown for the next activation
        }
    }

    // Radioactive Burst Ability
    public void radioactiveBurst() {
        if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
            for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(5.0))) {
                if (!entity.isInvulnerable() && !ImmuneEntities().contains(entity.getType()) && entity != boss) {
                    entity.hurt(entity.level().damageSources().mobAttack(boss), 4.0f);
                    if (!entity.level().isClientSide()) {
                        entity.addEffect(new MobEffectInstance(MobEffects.POISON, 20, 1));
                    }
                }
            }
            boss.playSound(SoundEvents.GENERIC_EXPLODE, 4.5f, 2);
            ParticlesUtil.sendParticles(this.boss.level(), ParticleTypes.SMOKE, this.boss.position(), 0.1f, 0.2f, 0.1f, 15, 0);
            boss.AbilitiesTicksCooldown = 80; // Set cooldown for the next activation
        }
    }

    // Toxic Glare Ability
    public void toxicGlare(LivingEntity entity) {
        if (entity == null || boss == null) {
            return;
        }

        if (entity instanceof Player player) {
            // Calcula se o jogador está olhando para o boss
            Vec3 lookVec = player.getLookAngle().normalize(); // Vetor de direção do olhar do jogador
            Vec3 directionToBoss = boss.position().subtract(player.position()).normalize(); // Vetor direção do jogador para o boss
            double dotProduct = lookVec.dot(directionToBoss); // Produto escalar para verificar alinhamento

            //player.displayClientMessage(Component.literal("Dot Value = " + dotProduct), true); //Debug
            // Se o ângulo entre o olhar e o boss for menor que 30 graus
            if (dotProduct > 0.95) { // ~30 graus de tolerância (cos(30º) ≈ 0.866)
                // Verifica se a visão está limpa
                Level world = player.level();
                Vec3 playerEyePos = player.getEyePosition(1.0F); // Posição dos olhos do jogador
                Vec3 bossPos = boss.position().add(0, boss.getBbHeight() / 2.0, 0); // Centro aproximado do boss

                // Verifica obstruções de blocos
                BlockHitResult blockHitResult = world.clip(new ClipContext(
                        playerEyePos,
                        bossPos,
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        player
                ));

                // Verifica obstruções de entidades
                EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                        world,
                        player,
                        playerEyePos,
                        bossPos,
                        player.getBoundingBox().expandTowards(lookVec.scale(64)), // Alcance de 64 blocos
                        e -> e == boss // Filtra apenas o boss como alvo válido
                );

                // Verifica se não há bloco ou entidade obstruindo
                if ((blockHitResult.getType() == HitResult.Type.MISS || blockHitResult.getBlockPos().equals(boss.blockPosition()))
                        && entityHitResult != null && entityHitResult.getEntity() == boss) {
                    if (!player.level().isClientSide()) {
                        if (ProcessTransfur.isPlayerNotLatex(player)) {
                            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 30, 2)); // Aplica "Radiação"
                        } else {
                            player.addEffect(new MobEffectInstance(MobEffects.POISON, 30, 2)); // Aplica "Radiação"
                        }
                    }
                }
            }
        }
    }

    public void tugPlayerLook(Player player, Vec3 target, double strength) {
        // Obtém a posição dos olhos do jogador
        Vec3 eyePosition = player.getEyePosition(1.0F);

        // Calcula a direção entre o jogador e o alvo
        Vec3 direction = target.subtract(eyePosition);

        // Calcula as rotações necessárias
        double distanceXZ = Math.sqrt(direction.x * direction.x + direction.z * direction.z);
        float targetYaw = (float) (Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90.0F);
        float targetPitch = (float) -Math.toDegrees(Math.atan2(direction.y, distanceXZ));

        // Aplica rotação suavemente com força (interpolação)
        float newYaw = (float) (player.getYRot() + (targetYaw - player.getYRot()) * strength);
        float newPitch = (float) (player.getXRot() + (targetPitch - player.getXRot()) * strength);

        player.setYRot(newYaw);
        player.setXRot(newPitch);

        // Atualiza as rotações anteriores para suavizar
        player.yRotO = newYaw;
        player.xRotO = newPitch;
    }


    // Hypnotic Gaze Ability
    public void hypnoticGaze(LivingEntity entity) {
        LivingEntity self = boss;
        Level level = boss.level();
        level.getNearbyEntities(Mob.class, TargetingConditions.DEFAULT, self, AABB.ofSize(self.position(), 3.0, 3.0, 3.0)).forEach((mob) -> {
            if (mob.getTarget() != null && mob.getTarget().is(self)) {
                mob.setTarget(null);
            }
        });
        level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, self, AABB.ofSize(self.position(), 10.0, 10.0, 10.0)).forEach((livingEntity) -> {
            if (!(livingEntity.getLookAngle().dot(self.getEyePosition().subtract(livingEntity.getEyePosition()).normalize()) < 0.8500000238418579)) {
                if (!(livingEntity instanceof Player) || Changed.config.server.playerControllingAbilities.get()) {
                    CameraUtil.tugEntityLookDirection(livingEntity, self, 0.125);
                    if (!livingEntity.level().isClientSide()) {
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 2, false, false), self);
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 2, false, false), self);
                    }
                }
            }
        });
    }

    public void Passives() {
        if (boss.isActivatedAbility()) {
            for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(10.0))) {
                // Verifica a porcentagem de vida do alvo para determinar qual habilidade passiva usar
                double healthPercentage = (entity.getHealth() / entity.getMaxHealth()) * 100;
                boolean isToUseToxicGlare = healthPercentage > 15;
                //boolean isToUseHypnoticGaze = healthPercentage <= 15;

                // Executa as habilidades com base na porcentagem de vida do alvo
                if (isToUseToxicGlare) {
                    toxicGlare(entity);
                    this.boss.PassivesTicksCooldown++;
                }
            }
        }
    }

    // Irradiated Pulse Ability
    public void irradiatedPulse() {
        if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
            for (int i = 0; i < 5; i++) {
                ParticlesUtil.sendParticles(this.boss.level(), ParticleTypes.DRAGON_BREATH, this.boss.position(), 0.3f, 0.2f, 0.3f, 15, 0);
            }
            for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(6.0))) {
                if (!entity.isInvulnerable() && !ImmuneEntities().contains(entity.getType()) && entity != boss) {
                    if (!entity.level().isClientSide()) {
                        entity.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 1)); // Aplica veneno por 6 segundos
                    }
                    entity.hurt(entity.level().damageSources().mobAttack(boss), 6.0F); // Dano direto
                }
            }
            boss.playSound(SoundEvents.BEACON_ACTIVATE, 4.5f, 0);
            boss.AbilitiesTicksCooldown = 100; // Set cooldown for the next activation
        }
    }

    // Radiant Field Ability
    public void radiantField() {
        if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
            // Partículas em círculo ao redor do boss
				/*
				 * for (int i = 0; i < 360; i += 15) {
					double angle = Math.toRadians(i);
					double x = boss.getX() + Mth.cos((float) angle) * 4.0;
					double z = boss.getZ() + Mth.sin((float) angle) * 4.0;
					Vec3 pos = new Vec3(x, boss.getY(), z);
					PlayerUtilProcedure.ParticlesUtil.sendParticles(
							boss.level(),
							ParticleTypes.GLOW,
							pos,
							0.1f, 0.2f, 0.1f,
							15, 0
					);
				}*/


            for (int theta = 0; theta < 360; theta += 45) { // Ângulo horizontal
                double angleTheta = Math.toRadians(theta);
                for (int phi = 0; phi <= 180; phi += 45) { // Ângulo vertical
                    double anglePhi = Math.toRadians(phi);
                    double x = this.boss.getX() + Math.sin(anglePhi) * Math.cos(angleTheta) * 4.0;
                    double y = this.boss.getY() + Math.cos(anglePhi) * 4.0;
                    double z = this.boss.getZ() + Math.sin(anglePhi) * Math.sin(angleTheta) * 4.0;
                    Vec3 pos = new Vec3(x, y, z);
                    ParticlesUtil.sendParticles(
                            this.boss.level(),
                            ParticleTypes.GLOW,
                            pos,
                            0.3f, 0.2f, 0.3f,
                            15, 0
                    );
                }
            }

            // Dano e efeitos em entidades próximas
            for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(4.0))) {
                if (!entity.isInvulnerable() && !ImmuneEntities().contains(entity.getType()) && entity != boss) {
                    entity.hurt(entity.level().damageSources().mobAttack(boss), 2.0F); // Dano leve
                    if (!entity.level().isClientSide()) {
                        entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0)); // Bloqueia regeneração
                    }
                }
            }

            // Som e cooldown
            boss.playSound(SoundEvents.BEACON_DEACTIVATE, 4.5f, 1);
            boss.AbilitiesTicksCooldown = 100; // Set cooldown for the next activation
        }
    }


    // Meltdown Ability - Agora com partículas de esfera ao redor da Entity
    public void meltdown() {
        if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
            ParticlesUtil.sendParticles(this.boss.level(), ParticleTypes.EXPLOSION, this.boss.position(), 0.3f, 0.2f, 0.3f, 15, 0);

            // Para cada entidade dentro da área do meltdown
            for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(5.0))) {
                if (!entity.isInvulnerable() && !ImmuneEntities().contains(entity.getType()) && entity != boss) {
                    entity.hurt(entity.level().damageSources().mobAttack(boss), 14.0F); // Dano alto
                    if (!entity.level().isClientSide()) {
                        entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0)); // Aplica cegueira
                    }

                    for (int theta = 0; theta < 360; theta += 45) { // Ângulo horizontal
                        double angleTheta = Math.toRadians(theta);
                        for (int phi = 0; phi <= 180; phi += 45) { // Ângulo vertical
                            double anglePhi = Math.toRadians(phi);
                            double x = this.boss.getX() + Math.sin(anglePhi) * Math.cos(angleTheta) * 4.0;
                            double y = this.boss.getY() + Math.cos(anglePhi) * 4.0;
                            double z = this.boss.getZ() + Math.sin(anglePhi) * Math.sin(angleTheta) * 4.0;
                            Vec3 pos = new Vec3(x, y, z);
                            ParticlesUtil.sendParticles(
                                    this.boss.level(),
                                    ParticleTypes.SMOKE,
                                    pos,
                                    0.3f, 0.2f, 0.3f,
                                    15, 0
                            );
                        }
                    }
						/*
						*for (int i = 0; i < 360; i += 15) {

							double angle = Math.toRadians(i);
							double x = entity.getX() + Math.cos(angle) * 2.0; // Distância esférica
							double z = entity.getZ() + Math.sin(angle) * 2.0; // Distância esférica
							double y = entity.getY() + Math.sin(angle) * 2.0; // Distância esférica em Y
							Vec3 pos = new Vec3(x,y,z);
							PlayerUtilProcedure.ParticlesUtil.sendParticles(this.boss.level(), ParticleTypes.SMOKE, pos, 0.3f, 0, 0.3f, 3,0);
						}
						*/
                }
            }

            // O boss fica exausto
            boss.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
            boss.playSound(SoundEvents.BEACON_DEACTIVATE, 4.5f, 0);
            boss.AbilitiesTicksCooldown = 30; // Set cooldown for the next activation
            boss.PassivesTicksCooldown = 150;
        }
    }
}
