package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.ability.handle.CounterDodgeType;
import net.foxyas.changedaddon.client.model.animations.parameters.DodgeAnimationParameters;
import net.foxyas.changedaddon.client.particle.EntityModelFadeParticleOptions;
import net.foxyas.changedaddon.init.ChangedAddonAnimationEvents;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.foxyas.changedaddon.util.DelayedTask;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAnimationEvents;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Random;

public class DodgeAbilityInstance extends AbstractAbilityInstance {

    public static final int INF_DODGE_TICKS = -5;
    public static final Color FADE_COLOR = new Color(96, 96, 96);
    private final int defaultRegenCooldown = 20;
    public boolean ultraInstinct = false; //FUNNY VARIABLE :3
    public DodgeType dodgeType = DodgeType.WEAVE;
    private int dodgeAmount = 0;
    private int maxDodgeAmount = 4;
    private boolean dodgeActive = false;
    private int dodgeRegenCooldown = defaultRegenCooldown;
    public int projectilesImmuneTicks = 0;
    public int canDodgeTicks = 0;
    public int trailTicks = 0;

    public DodgeAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    public DodgeAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity, int maxDodge) {
        this(ability, entity);
        this.maxDodgeAmount = maxDodge;
        this.dodgeAmount = maxDodge;
    }

    private void dodgeAwayFromAttacker(Entity dodger, Entity attacker) {
        Vec3 attackerPosition = attacker.position();
        Vec3 dodgerPosition = dodger.position();

        Vec3 rawMotion = attackerPosition.subtract(dodgerPosition).scale(-0.25);
        float motionScale;
        if (dodger instanceof LivingEntity living) {
            float randomYawDeg = (float) (living.getRandom().nextGaussian() * 90f);
            float randomYawRad = randomYawDeg * ((float) Math.PI / 180F);

            rawMotion = rawMotion.yRot(randomYawRad);

            float absYawDeg = Math.abs(randomYawDeg);
            float accuracy = absYawDeg / 90f;

            motionScale = 1 + accuracy;
        } else {
            motionScale = 1;
        }

        Vec3 motion = divideVec(rawMotion, Math.max(dodger.distanceTo(attacker), 1d)).scale(motionScale);
        if (dodger instanceof ServerPlayer serverPlayer) {
            serverPlayer.setDeltaMovement(motion.x, motion.y, motion.z);
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer.getId(), serverPlayer.getDeltaMovement()));
        } else {
            dodger.setDeltaMovement(motion.x, motion.y, motion.z);
        }
    }

    private static Vec3 divideVec(Vec3 vec3, double value) {
        double vecX = vec3.x, vecY = vec3.y, vecZ = vec3.z;
        return new Vec3(vecX / value, vecY / value, vecZ / value);
    }


    private void applyDodgeAwayParticlesTrails(LivingEntity dodger, LivingEntity attacker) {
        Vec3 motion = attacker.getEyePosition().subtract(dodger.getEyePosition()).scale(-0.25);
        Vec3 dodgerPos = dodger.position().add(0, 0.5f, 0);

        if (dodger.level() instanceof ServerLevel serverLevel) {
            int steps = 3;         // número de partículas por linha
            int lines = 5;          // quantas linhas paralelas
            float spread = 1;    // afastamento lateral das linhas

            for (int l = 0; l < lines; l++) {
                // gera um deslocamento lateral aleatório (x,z) em círculo
                Random random = new Random();
                Vec3 lateralOffset = new Vec3(random.nextFloat(-spread, spread),
                        random.nextFloat(-spread, spread),
                        random.nextFloat(-spread, spread));
                if (l == 0) {
                    lateralOffset = Vec3.ZERO;
                }

                for (int s = 0; s <= steps; s++) {
                    //float t = s / (float) steps;
                    Vec3 particlePos = dodgerPos.add(lateralOffset);

                    serverLevel.sendParticles(
                            new DustParticleOptions(new Vector3f(1, 1, 1), 1),
                            particlePos.x(),
                            particlePos.y(),
                            particlePos.z(),
                            0, // count
                            motion.x, motion.y, motion.z, 1 // sem velocidade extra
                    );
                }
            }
        }
    }

    public static boolean isSpectator(Entity entity) {
        return entity instanceof Player player && player.isSpectator();
    }

    public DodgeAbilityInstance withDodgeType(DodgeType dodgeType) {
        this.dodgeType = dodgeType;
        return this;
    }

    public boolean isDodgeActive() {
        return this.ultraInstinct || this.getCanDodgeTicks() > 0 || dodgeActive;
    }

    public void setDodgeActivate(boolean active) {
        this.dodgeActive = active;
        this.ability.setDirty(entity);
    }

    public int getDodgeAmount() {
        return dodgeAmount;
    }

    public void setDodgeAmount(int amount) {
        dodgeAmount = Math.min(amount, maxDodgeAmount);
        this.ability.setDirty(entity);
    }

    public void addDodgeAmount() {
        if (dodgeAmount < maxDodgeAmount) dodgeAmount++;
        this.ability.setDirty(entity);
    }

    public void subDodgeAmount() {
        if (dodgeAmount > 0) dodgeAmount--;
        if (dodgeAmount <= 0 && (this.getCanDodgeTicks() > 0 && this.getDodgeType() instanceof CounterDodgeType))
            this.canDodgeTicks = 0;
        if (dodgeAmount <= 0) {
            this.setDodgeActivate(false);
            this.getController().resetHoldTicks();
            this.getController().applyCoolDown();
        }
        this.ability.setDirty(entity);
    }

    public DodgeType getDodgeType() {
        return dodgeType;
    }

    public int getCanDodgeTicks() {
        return this.getDodgeType() instanceof CounterDodgeType ? this.canDodgeTicks : INF_DODGE_TICKS;
    }

    public void executeDodgeEffects(LevelAccessor levelAccessor, @Nullable Entity attacker, LivingEntity dodger, @Nullable LivingAttackEvent event, boolean causeExhaustion) {
        if (!ultraInstinct) {
            this.subDodgeAmount();
        }
        if (dodger instanceof Player player) {
            if (!ultraInstinct) {
                player.displayClientMessage(Component.translatable("ability.changed_addon.dodge.dodge_amount_left", this.getDodgeStaminaRatio()), false);
            } else {
                player.displayClientMessage(Component.translatable("ability.changed_addon.dodge.ultra_instinct"), true);
            }
            if (causeExhaustion && !ultraInstinct) {
                player.causeFoodExhaustion(8f);
            }
        }

        if (this.getDodgeType() == DodgeType.WEAVE) {
            dodger.invulnerableTime = 20 * 3;
            dodger.hurtDuration = 20 * 3;
            dodger.hurtTime = dodger.hurtDuration;
            dodger.hurtMarked = false;
        } else if (this.getDodgeType() == DodgeType.TELEPORT) {
            dodger.hurtMarked = false;
        } else {
            if (this.dodgeType instanceof CounterDodgeType counterDodgeType) {
                counterDodgeType.runDodgeEffects(this, levelAccessor, dodger, attacker, dodgeType, event, causeExhaustion);
                return;
            }

            this.dodgeType.runDodgeEffects(this, levelAccessor, dodger, attacker, dodgeType, event, causeExhaustion);
            return;
        }

        if (event != null) {
            event.setCanceled(true);
        }
        if (this.getDodgeType() == DodgeType.WEAVE) {
            executeDodgeParticles(levelAccessor, dodger, attacker);
            executeDodgeAnimations(levelAccessor, dodger);
        }
    }

    public void executeDodgeParticles(LevelAccessor levelAccessor, LivingEntity dodger, Entity attacker) {
        if (levelAccessor instanceof ServerLevel serverLevel) {
            //spawnDodgeParticles(serverLevel, dodger, 0.5f, 0.3f, 0.3f, 0.3f, 10, 0.05f);
        }
        if (attacker instanceof LivingEntity attackerLiving) {
            applyDodgeAwayParticlesTrails(dodger, attackerLiving);
        }
    }

    public void executeDodgeAnimations(LevelAccessor levelAccessor, LivingEntity dodger) {
        ChangedSounds.broadcastSound(dodger, ChangedSounds.CARDBOARD_BOX_OPEN, 2.5f, 1);
        if (this.getDodgeType().shouldPlayDodgeAnimation()) {
            int randomValue = levelAccessor.getRandom().nextInt(6);
            switch (randomValue) {
                case 0 ->
                        ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_LEFT.get(), DodgeAnimationParameters.INSTANCE);
                case 1 ->
                        ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_RIGHT.get(), DodgeAnimationParameters.INSTANCE);
                case 2 ->
                        ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_WEAVE_LEFT.get(), DodgeAnimationParameters.INSTANCE);
                case 3 ->
                        ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_WEAVE_RIGHT.get(), DodgeAnimationParameters.INSTANCE);
                case 4 ->
                        ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_DOWN_LEFT.get(), DodgeAnimationParameters.INSTANCE);
                case 5 ->
                        ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_DOWN_RIGHT.get(), DodgeAnimationParameters.INSTANCE);
                //default -> ChangedAnimationEvents.broadcastEntityAnimation(player, ChangedAddonAnimationEvents.DODGE_LEFT.get(), null);
            }
            DelayedTask.schedule(5, () -> this.trailTicks = 1);
        }
    }

    public static void executeRandomDodgeAnimation(LevelAccessor levelAccessor, LivingEntity dodger) {
        ChangedSounds.broadcastSound(dodger, ChangedSounds.CARDBOARD_BOX_OPEN, 2.5f, 1);
        int randomValue = levelAccessor.getRandom().nextInt(6);
        switch (randomValue) {
            case 0 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_LEFT.get(), DodgeAnimationParameters.INSTANCE);
            case 1 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_RIGHT.get(), DodgeAnimationParameters.INSTANCE);
            case 2 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_WEAVE_LEFT.get(), DodgeAnimationParameters.INSTANCE);
            case 3 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_WEAVE_RIGHT.get(), DodgeAnimationParameters.INSTANCE);
            case 4 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_DOWN_LEFT.get(), DodgeAnimationParameters.INSTANCE);
            case 5 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_DOWN_RIGHT.get(), DodgeAnimationParameters.INSTANCE);
            //default -> ChangedAnimationEvents.broadcastEntityAnimation(player, ChangedAddonAnimationEvents.DODGE_LEFT.get(), null);
        }
    }

    public void executeDodgeEffects(LevelAccessor levelAccessor, @Nullable Entity attacker, LivingEntity dodger, @Nullable LivingAttackEvent event) {
        this.executeDodgeEffects(levelAccessor, attacker, dodger, event, true);
    }

    public void executeDodgeHandle(LevelAccessor levelAccessor, Entity attacker, LivingEntity dodger, LivingAttackEvent event, boolean causeExhaustion) {
        Vec3 attackerPos = attacker.position();
        Vec3 lookDirection = attacker.getLookAngle().normalize();
        //Vec3 dodgerLookDirection = dodger.getLookAngle();
        final double distanceBehind = 3;
        Vec3 dodgePosBehind = attackerPos.subtract(lookDirection.scale(distanceBehind));
        double distance = attacker.distanceTo(dodger);

        if (this.ultraInstinct) {
            dodgeAwayFromAttacker(dodger, attacker);
            if (event != null) {
                event.setCanceled(true);
            }
            return;
        }

        if (this.getDodgeType() == DodgeType.TELEPORT) {
            if (distance > 2f) {
                // Random offset values
                double maxDistance = 16.0; // maximum distance for teleport
                double dx = (dodger.getRandom().nextDouble() - 0.5) * 2 * maxDistance;
                double dz = (dodger.getRandom().nextDouble() - 0.5) * 2 * maxDistance;
                double dy = (dodger.getRandom().nextInt(16) - 8); // vertical offset -8 to +7

                // Calculate target position
                BlockPos targetPos = new BlockPos((int) (dodger.getX() + dx), (int) (dodger.getY() + dy), (int) (dodger.getZ() + dz));
                if (dodger.randomTeleport(targetPos.getX(), targetPos.getY(), targetPos.getZ(), true)) {
                    // Optional: play sound & particles like Enderman
                    levelAccessor.playSound(null, dodger.blockPosition(),
                            SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (levelAccessor instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.PORTAL,
                                dodger.getX(), dodger.getY() + 0.5, dodger.getZ(),
                                20, 0.5, 1.0, 0.5, 0.1);
                    }
                }
            } else {
                if (dodger.randomTeleport(dodgePosBehind.x, dodgePosBehind.y, dodgePosBehind.z, true)) {
                    // Optional: play sound & particles like Enderman
                    levelAccessor.playSound(null, dodger.blockPosition(),
                            SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (levelAccessor instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.PORTAL,
                                dodger.getX(), dodger.getY() + 0.5, dodger.getZ(),
                                20, 0.5, 1.0, 0.5, 0.1);
                    }
                }
            }

            if (event != null) {
                event.setCanceled(true);
            }
        } else if (this.dodgeType == DodgeType.WEAVE) {
            dodgeAwayFromAttacker(dodger, attacker);
            if (event != null) {
                event.setCanceled(true);
            }
        } else {
            if (this.dodgeType instanceof CounterDodgeType counterDodgeType) {
                counterDodgeType.runDodge(this, levelAccessor, dodger, attacker, event, distance, dodgePosBehind, causeExhaustion);
            } else {
                this.dodgeType.runDodge(this, levelAccessor, dodger, attacker, event, distance, dodgePosBehind, causeExhaustion);
            }
        }
    }

    public void executeDodgeHandle(LivingEntity dodger, Entity attacker) {
        this.executeDodgeHandle(dodger.level(), attacker, dodger, null, true);
    }

    public void executeDodgeEffects(LivingEntity dodger, Entity attacker) {
        this.executeDodgeEffects(dodger.level(), attacker, dodger, null);
    }

    private void spawnDodgeParticles(ServerLevel level, Entity entity, float middle, float xV, float yV, float zV, int count, float speed) {
        level.sendParticles(ParticleTypes.POOF,
                entity.getX(), entity.getY() + middle, entity.getZ(), count, xV, yV, zV, speed);
    }

    public int getMaxDodgeAmount() {
        return maxDodgeAmount;
    }

    public void setMaxDodgeAmount(int max) {
        maxDodgeAmount = max;
        dodgeAmount = Math.min(dodgeAmount, max); // Adjust current amount if needed
    }

    public float getDodgeStaminaRatio() {
        return ((float) dodgeAmount / maxDodgeAmount) * 100f;
    }

    public void setUltraInstinct(boolean ultraInstinct) {
        if (ultraInstinct) {
            if (this.entity.getEntity() instanceof Player player) {
                player.displayClientMessage(Component.translatable("ability.changed_addon.dodge.ultra_instinct.activated"), false);
            }
        }
        this.ultraInstinct = ultraInstinct;
    }

    @Override
    public boolean canUse() {
        if (ultraInstinct) {
            return true;
        }
        return dodgeAmount > 0 && !isSpectator(entity.getEntity());
    }

    @Override
    public boolean canKeepUsing() {
        return canUse();
    }

    @Override
    public void startUsing() {
        if (entity.getEntity() instanceof Player player && this.getController().getHoldTicks() == 0) {
            if (!(player.level().isClientSide())) {
                if (this.dodgeType instanceof CounterDodgeType) {
                    this.canDodgeTicks = 60;
                    return;
                }

                if (!ultraInstinct) {
                    player.displayClientMessage(
                            Component.translatable("ability.changed_addon.dodge.dodge_amount", getDodgeStaminaRatio()),
                            true
                    );
                }

                this.ability.setDirty(entity);
            }
        }
    }

    @Override
    public void tick() {
        //super.tick();
        if (entity.getEntity() instanceof Player player) {
            if (!(player.level().isClientSide())) {
                if (this.dodgeType instanceof CounterDodgeType) {
                    return;
                }
                if (!ultraInstinct) {
                    player.displayClientMessage(
                            Component.translatable("ability.changed_addon.dodge.dodge_amount", getDodgeStaminaRatio()), true);
                }
            }
        }
        setDodgeActivate(canUse());
        this.ability.setDirty(entity);
    }

    @Override
    public void stopUsing() {
        setDodgeActivate(false);
        this.ability.setDirty(entity);
        if (entity.getEntity() instanceof Player player) {
            if (!(player.level.isClientSide())) {
                if (!ultraInstinct) {
                    player.displayClientMessage(
                            Component.translatable("ability.changed_addon.dodge.dodge_amount",
                                    getDodgeStaminaRatio()),
                            true
                    );
                }
            }
        }
    }

    @Override
    public void tickIdle() {
        super.tickIdle();

        if (entity.getLevel() instanceof ServerLevel level) {
            if (trailTicks > 0) {
                addFadeParticle(level);


                /*for (ServerPlayer serverPlayer : level.players()) {
                    if (level.sendParticles(serverPlayer, particleOption, true, particlePos.x, particlePos.y, particlePos.z, 0, 0f, 1, 0, 0.02f)) {
                    }
                }
                ParticlesUtil.sendParticles(level, ChangedAddonParticleTypes.entityModelFade(dodger, color.getRGB(), 2.5f), dodger.position().add(0, 1.425f, 0), 0, 1f, 0, 0, 0.02f);*/
                trailTicks--;
            }
        }

        if (ultraInstinct) {
            this.setDodgeActivate(true);
        }

        if (this.getController().getHoldTicks() > 0) this.dodgeRegenCooldown = 5;

        if (this.getDodgeType() instanceof CounterDodgeType) {
            this.setDodgeActivate(this.getCanDodgeTicks() > 0);
            if (this.getCanDodgeTicks() > 0) {
                this.canDodgeTicks--;
                if (this.canDodgeTicks <= 0) {
                    this.entity.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, true, true));
                }
            }
        } else {
            this.canDodgeTicks = INF_DODGE_TICKS;
        }

        if (projectilesImmuneTicks > 0) {
            projectilesImmuneTicks--;
        }

        boolean nonHurtFrame = entity.getEntity().hurtTime <= 10 && entity.getEntity().invulnerableTime <= 10;
        if (nonHurtFrame && !isDodgeActive() && dodgeAmount < maxDodgeAmount) {
            if (dodgeRegenCooldown <= 0) {
                addDodgeAmount();
                dodgeRegenCooldown = 5;

                if (entity.getEntity() instanceof Player player) {
                    if (!(player.level().isClientSide())) {
                        if (!ultraInstinct) {
                            player.displayClientMessage(
                                    Component.translatable("ability.changed_addon.dodge.dodge_amount",
                                            getDodgeStaminaRatio()),
                                    true
                            );
                        } else {
                            player.displayClientMessage(Component.translatable("ability.changed_addon.dodge.ultra_instinct"),
                                    true);
                        }
                    }
                }
            } else {
                dodgeRegenCooldown--;
            }
        }
    }

    protected void addFadeParticle(ServerLevel level) {
        Vec3 particlePos = entity.getEntity().position().add(0, 1.425f, 0);
        EntityModelFadeParticleOptions particleOption = ChangedAddonParticleTypes.entityModelFade(entity.getEntity(), FADE_COLOR.getRGB(), 0.25f);
        Vec3 motionOrDelta = new Vec3(0, 1, 0);
        ParticlesUtil.sendParticles(level, particleOption, particlePos, motionOrDelta, 0, 0.1f);
    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);
        if (tag.contains("DodgeAmount")) dodgeAmount = tag.getInt("DodgeAmount");
        if (tag.contains("canDodgeTicks")) canDodgeTicks = tag.getInt("canDodgeTicks");
        if (tag.contains("MaxDodgeAmount")) maxDodgeAmount = tag.getInt("MaxDodgeAmount");
        if (tag.contains("DodgeRegenCooldown")) dodgeRegenCooldown = tag.getInt("DodgeRegenCooldown");
        if (tag.contains("DodgeActivate")) dodgeActive = tag.getBoolean("DodgeActivate");
        if (tag.contains("ultraInstinct")) ultraInstinct = tag.getBoolean("ultraInstinct");
    }

    @Override
    public void saveData(CompoundTag tag) {
        super.saveData(tag);
        tag.putInt("DodgeAmount", dodgeAmount);
        tag.putInt("MaxDodgeAmount", maxDodgeAmount);
        tag.putInt("DodgeRegenCooldown", dodgeRegenCooldown);
        tag.putInt("canDodgeTicks", getCanDodgeTicks());
        tag.putBoolean("DodgeActivate", dodgeActive);
        tag.putBoolean("ultraInstinct", ultraInstinct);
    }

    public static class DodgeType {
        public static final DodgeType TELEPORT = new DodgeType();
        public static final DodgeType WEAVE = new DodgeType() {
            @Override
            public boolean shouldPlayDodgeAnimation() {
                return true;
            }

            @Override
            public boolean shouldApplyIframes(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, @Nullable LivingAttackEvent event, boolean causeExhaustion) {
                return true;
            }
        };

        public DodgeType() {
            super();
        }

        public void runDodge(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, LivingEntity dodger, Entity attacker, LivingAttackEvent event, double distance, Vec3 dodgePosBehind, boolean causeExhaustion) {
        }

        public void runDodgeEffects(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, @Nullable LivingAttackEvent event, boolean causeExhaustion) {
            if (this.shouldApplyIframes(dodgeAbilityInstance, levelAccessor, dodger, attacker, dodgeType, event, causeExhaustion) && dodger != null) {
                dodger.invulnerableTime = 20 * 3;
                dodger.hurtDuration = 20 * 3;
                dodger.hurtTime = dodger.hurtDuration;
                dodger.hurtMarked = false;
            }
        }

        public boolean shouldApplyIframes(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, @Nullable LivingAttackEvent event, boolean causeExhaustion) {
            return false;
        }

        public boolean shouldPlayDodgeAnimation() {
            return false;
        }
    }
}
