package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.ability.handle.dodgeTypes.CounterDodgeType;
import net.foxyas.changedaddon.ability.handle.dodgeTypes.DodgeType;
import net.foxyas.changedaddon.ability.handle.dodgeTypes.WeaveDodgeType;
import net.foxyas.changedaddon.client.model.animations.parameters.DodgeAnimationParameters;
import net.foxyas.changedaddon.client.particle.EntityModelFadeParticleOptions;
import net.foxyas.changedaddon.init.ChangedAddonAnimationEvents;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAnimationEvents;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Random;

public class DodgeAbilityInstance extends AbstractAbilityInstance {

    public static final int INF_DODGE_TICKS = -1;
    public static final Color FADE_COLOR = new Color(96, 96, 96);
    public boolean ultraInstinct = false; //FUNNY VARIABLE :3
    public DodgeType dodgeType = WeaveDodgeType.INSTANCE;
    public int projectilesImmuneTicks = 0;
    public int canDodgeTicks = 0;
    public int trailTicks = 0;
    private int dodgeAmount = 0;
    private int maxDodgeAmount = 4;
    private boolean dodgeActive = false;

    public DodgeAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    public DodgeAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity, int maxDodge) {
        this(ability, entity);
        this.maxDodgeAmount = maxDodge;
        this.dodgeAmount = maxDodge;
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
        if (entity.getLevel().isClientSide()) {
            return;
        }

        if (entity.getEntity() instanceof Player player && this.getController().getHoldTicks() == 0) {
            this.dodgeType.startUsing(this);

            if (!ultraInstinct) {
                player.displayClientMessage(
                        Component.translatable("ability.changed_addon.dodge.dodge_amount", getDodgeStaminaRatio()),
                        true
                );
            }

            this.ability.setDirty(entity);
        }
    }

    @Override
    public void tick() {
        if (entity.getLevel().isClientSide()) {
            return;
        }

        if (entity.getEntity() instanceof Player player) {
            if (!ultraInstinct && this.dodgeType.shouldDisplayDodgeAmount(this)) {
                player.displayClientMessage(
                        Component.translatable("ability.changed_addon.dodge.dodge_amount", getDodgeStaminaRatio()), true);
            }
        }
        setDodgeActivate(canUse());
        this.ability.setDirty(entity);
    }

    @Override
    public void stopUsing() {
        if (entity.getLevel().isClientSide()) {
            return;
        }

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
        if (entity.getLevel().isClientSide()) {
            return;
        }

        if (entity.getLevel() instanceof ServerLevel level) {
            if (trailTicks > 0) {
                addFadeParticle(level);
                trailTicks--;
            }
        }

        if (ultraInstinct && !dodgeActive) {
            this.setDodgeActivate(true);
        }

        if (projectilesImmuneTicks > 0) {
            projectilesImmuneTicks--;
        }

        boolean nonHurtFrame = entity.getEntity().hurtTime <= 10 && entity.getEntity().invulnerableTime <= 10;
        if (nonHurtFrame && !isDodgeActive() && dodgeAmount < maxDodgeAmount) {
            if (entity.getEntity().tickCount % 5 == 0) {
                addDodgeAmount();

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
            }
        }

        this.getDodgeType().tickIdle(this);
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

    public boolean isUltraInstinct() {
        return ultraInstinct;
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

    protected void addFadeParticle(ServerLevel level) {
        Vec3 particlePos = entity.getEntity().position().add(0, 1.425f, 0);
        EntityModelFadeParticleOptions particleOption = ChangedAddonParticleTypes.entityModelFade(entity.getEntity(), FADE_COLOR.getRGB(), 0.25f);
        Vec3 motionOrDelta = new Vec3(0, 1, 0);
        ParticlesUtil.sendParticles(level, particleOption, particlePos, motionOrDelta, 0, 0.1f);
    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);
        if (tag.contains("dodgeAmount")) dodgeAmount = tag.getInt("dodgeAmount");
        if (tag.contains("maxDodgeAmount")) maxDodgeAmount = tag.getInt("maxDodgeAmount");
        if (tag.contains("canDodgeTicks")) canDodgeTicks = tag.getInt("canDodgeTicks");
        if (tag.contains("dodgeActivate")) dodgeActive = tag.getBoolean("dodgeActivate");
        if (tag.contains("ultraInstinct")) ultraInstinct = tag.getBoolean("ultraInstinct");
    }

    @Override
    public void saveData(CompoundTag tag) {
        super.saveData(tag);
        tag.putInt("dodgeAmount", dodgeAmount);
        tag.putInt("maxDodgeAmount", maxDodgeAmount);
        tag.putInt("canDodgeTicks", getCanDodgeTicks());
        tag.putBoolean("dodgeActivate", dodgeActive);
        tag.putBoolean("ultraInstinct", ultraInstinct);
    }

    public boolean willDodge(Entity entity) {
        return this.dodgeType.willDodge(this, entity);
    }

    private static Vec3 divideVec(Vec3 vec3, double value) {
        double vecX = vec3.x, vecY = vec3.y, vecZ = vec3.z;
        return new Vec3(vecX / value, vecY / value, vecZ / value);
    }

    public static boolean isSpectator(Entity entity) {
        return entity instanceof Player player && player.isSpectator();
    }

    public void applyDodgeEffects(LevelAccessor level, @Nullable Entity attacker, LivingEntity dodger, boolean causeExhaustion) {
        if (!ultraInstinct) this.subDodgeAmount();

        if (ultraInstinct) {
            applyDodgeAnimations(dodger);
            if (dodger instanceof Player player) {
                player.displayClientMessage(Component.translatable("ability.changed_addon.dodge.ultra_instinct"), true);
            }
            return;
        }
        if (dodger instanceof Player player) {
            player.displayClientMessage(Component.translatable("ability.changed_addon.dodge.dodge_amount_left", this.getDodgeStaminaRatio()), false);
            if (causeExhaustion) {
                player.causeFoodExhaustion(8f);
            }
        }

        this.dodgeType.applyDodgeEffects(this, level, dodger, attacker, dodgeType, causeExhaustion);
    }

    public void applyDodgeParticles(LivingEntity dodger, Entity attacker) {
        if (attacker instanceof LivingEntity attackerLiving) {
            applyDodgeAwayParticlesTrails(dodger, attackerLiving);
        }
    }

    public void applyDodgeAnimations(LivingEntity dodger) {
        ChangedSounds.broadcastSound(dodger, ChangedSounds.CARDBOARD_BOX_OPEN, 2.5f, 1);
        if (this.getDodgeType().willPlayDodgeAnimation(dodger)) {
            int randomValue = dodger.getRandom().nextInt(6);
            final DodgeAnimationParameters animationParameters = DodgeAnimationParameters.DEFAULT;
            Vec3 particlePos = entity.getEntity().position().add(0, 1.425f, 0);
            Vec3 motionOrDelta = new Vec3(0, 1, 0);
            switch (randomValue) {
                case 0 ->
                        ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_LEFT.get(), animationParameters);
                case 1 ->
                        ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_RIGHT.get(), animationParameters);
                case 2 ->
                        ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_WEAVE_LEFT.get(), animationParameters);
                case 3 ->
                        ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_WEAVE_RIGHT.get(), animationParameters);
                case 4 ->
                        ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_DOWN_LEFT.get(), animationParameters);
                case 5 ->
                        ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_DOWN_RIGHT.get(), animationParameters);
            }
        }
    }

    public void applyDodgeAnimations(LivingEntity dodger, DodgeAnimationParameters animationParameters) {
        ChangedSounds.broadcastSound(dodger, ChangedSounds.CARDBOARD_BOX_OPEN, 2.5f, 1);
        if (this.getDodgeType().willPlayDodgeAnimation(dodger)) {
            int randomValue = dodger.getRandom().nextInt(6);
            Vec3 particlePos = entity.getEntity().position().add(0, 1.425f, 0);
            Vec3 motionOrDelta = new Vec3(0, 1, 0);
            switch (randomValue) {
                case 0 ->
                        ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_LEFT.get(), animationParameters);
                case 1 ->
                        ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_RIGHT.get(), animationParameters);
                case 2 ->
                        ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_WEAVE_LEFT.get(), animationParameters);
                case 3 ->
                        ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_WEAVE_RIGHT.get(), animationParameters);
                case 4 ->
                        ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_DOWN_LEFT.get(), animationParameters);
                case 5 ->
                        ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_DOWN_RIGHT.get(), animationParameters);
                //default -> ChangedAnimationEvents.broadcastEntityAnimation(player, ChangedAddonAnimationEvents.DODGE_LEFT.get(), null);
            }
        }
    }

    public void applyDodgeEffects(LevelAccessor levelAccessor, @Nullable Entity attacker, LivingEntity dodger) {
        this.applyDodgeEffects(levelAccessor, attacker, dodger, true);
    }

    public void applyDodgeEffects(LivingEntity dodger, Entity attacker) {
        this.applyDodgeEffects(dodger.level(), attacker, dodger);
    }

    public void applyDodgeMovement(LevelAccessor levelAccessor, Entity attacker, LivingEntity dodger, boolean causeExhaustion) {
        Vec3 attackerPos = attacker.position();
        Vec3 lookDirection = attacker.getLookAngle().normalize();
        final double distanceBehind = 3;
        Vec3 dodgePosBehind = attackerPos.subtract(lookDirection.scale(distanceBehind));
        double distance = attacker.distanceTo(dodger);

        if (this.ultraInstinct) { // UI override any behavior.
            dodgeAwayFromAttacker(dodger, attacker);
            return;
        }

        if (this.dodgeType instanceof CounterDodgeType counterDodgeType) {
            counterDodgeType.applyDodgeMovement(this, levelAccessor, dodger, attacker, distance, dodgePosBehind, causeExhaustion);
        } else {
            this.dodgeType.applyDodgeMovement(this, levelAccessor, dodger, attacker, distance, dodgePosBehind, causeExhaustion);
        }
    }

    public void applyDodgeMovement(LivingEntity dodger, Entity attacker) {
        this.applyDodgeMovement(dodger.level(), attacker, dodger, true);
    }

    public static void executeRandomDodgeAnimation(LivingEntity dodger) {
        ChangedSounds.broadcastSound(dodger, ChangedSounds.CARDBOARD_BOX_OPEN, 2.5f, 1);
        int randomValue = dodger.getRandom().nextInt(6);
        switch (randomValue) {
            case 0 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_LEFT.get(), DodgeAnimationParameters.DEFAULT);
            case 1 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_RIGHT.get(), DodgeAnimationParameters.DEFAULT);
            case 2 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_WEAVE_LEFT.get(), DodgeAnimationParameters.DEFAULT);
            case 3 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_WEAVE_RIGHT.get(), DodgeAnimationParameters.DEFAULT);
            case 4 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_DOWN_LEFT.get(), DodgeAnimationParameters.DEFAULT);
            case 5 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_DOWN_RIGHT.get(), DodgeAnimationParameters.DEFAULT);
            //default -> ChangedAnimationEvents.broadcastEntityAnimation(player, ChangedAddonAnimationEvents.DODGE_LEFT.get(), null);
        }
    }

    public static void executeRandomDodgeAnimation(LivingEntity dodger, DodgeAnimationParameters animationParameters) {
        ChangedSounds.broadcastSound(dodger, ChangedSounds.CARDBOARD_BOX_OPEN, 2.5f, 1);
        int randomValue = dodger.getRandom().nextInt(6);
        switch (randomValue) {
            case 0 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_LEFT.get(), animationParameters);
            case 1 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_RIGHT.get(), animationParameters);
            case 2 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_WEAVE_LEFT.get(), animationParameters);
            case 3 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_WEAVE_RIGHT.get(), animationParameters);
            case 4 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_DOWN_LEFT.get(), animationParameters);
            case 5 ->
                    ChangedAnimationEvents.broadcastEntityAnimation(dodger, ChangedAddonAnimationEvents.DODGE_DOWN_RIGHT.get(), animationParameters);
            //default -> ChangedAnimationEvents.broadcastEntityAnimation(player, ChangedAddonAnimationEvents.DODGE_LEFT.get(), null);
        }
    }

    public static void executeRandomDodgeAnimationWithFade(LivingEntity dodger) {
        ChangedSounds.broadcastSound(dodger, ChangedSounds.CARDBOARD_BOX_OPEN, 2.5f, 1);
        int randomValue = dodger.getRandom().nextInt(6);
        final DodgeAnimationParameters animationParameters = DodgeAnimationParameters.DEFAULT;
        Vec3 particlePos = dodger.position().add(0, 1.425f, 0);
        Vec3 motionOrDelta = new Vec3(0, 1, 0);
        switch (randomValue) {
            case 0 ->
                    ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_LEFT.get(), animationParameters);
            case 1 ->
                    ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_RIGHT.get(), animationParameters);
            case 2 ->
                    ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_WEAVE_LEFT.get(), animationParameters);
            case 3 ->
                    ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_WEAVE_RIGHT.get(), animationParameters);
            case 4 ->
                    ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_DOWN_LEFT.get(), animationParameters);
            case 5 ->
                    ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_DOWN_RIGHT.get(), animationParameters);
            //default -> ChangedAnimationEvents.broadcastEntityAnimation(player, ChangedAddonAnimationEvents.DODGE_LEFT.get(), null);
        }
    }

    public static void executeRandomDodgeAnimationWithFade(LivingEntity dodger, DodgeAnimationParameters animationParameters) {
        ChangedSounds.broadcastSound(dodger, ChangedSounds.CARDBOARD_BOX_OPEN, 2.5f, 1);
        int randomValue = dodger.getRandom().nextInt(6);
        Vec3 particlePos = dodger.position().add(0, 1.425f, 0);
        Vec3 motionOrDelta = new Vec3(0, 1, 0);
        switch (randomValue) {
            case 0 ->
                    ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_LEFT.get(), animationParameters);
            case 1 ->
                    ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_RIGHT.get(), animationParameters);
            case 2 ->
                    ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_WEAVE_LEFT.get(), animationParameters);
            case 3 ->
                    ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_WEAVE_RIGHT.get(), animationParameters);
            case 4 ->
                    ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_DOWN_LEFT.get(), animationParameters);
            case 5 ->
                    ChangedAddonAnimationEvents.broadcastEntityAnimationWithFade(dodger, FADE_COLOR, particlePos, motionOrDelta, 0.1f, 1, ChangedAddonAnimationEvents.DODGE_DOWN_RIGHT.get(), animationParameters);
            //default -> ChangedAnimationEvents.broadcastEntityAnimation(player, ChangedAddonAnimationEvents.DODGE_LEFT.get(), null);
        }
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
}
