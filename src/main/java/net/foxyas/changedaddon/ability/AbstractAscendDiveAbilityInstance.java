package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractAscendDiveAbilityInstance extends AbstractAbilityInstance {


    protected enum Phase {
        NONE,
        ASCEND,
        DIVE;
    }

    protected boolean started;
    protected Phase phase = Phase.NONE;
    protected int divingTicks;
    protected int ascendTicks;
    protected Vec3 diveDirection = Vec3.ZERO;

    protected AbstractAscendDiveAbilityInstance(
            AbstractAbility<?> ability,
            IAbstractChangedEntity entity
    ) {
        super(ability, entity);
    }

    /* ---------------- Lifecycle ---------------- */

    @Override
    public void startUsing() {
        ascendTicks = 0;
        phase = Phase.ASCEND;

        entity.getEntity().setNoGravity(true);
        entity.getEntity().setDeltaMovement(entity.getEntity().getDeltaMovement().multiply(0.5, 0, 0.5));
        this.started = true;
    }

    @Override
    public void tickIdle() {
        super.tickIdle();

        switch (phase) {
            case NONE -> {
            }
            case ASCEND -> tickAscend();
            case DIVE -> tickDive();
        }

        if (!canKeepUsing() && started) stopEarly(stoppedByFloorOrWall());

    }

    private boolean stoppedByFloorOrWall() {
        return entity.getEntity().isOnGround() || entity.getEntity().horizontalCollision || entity.getEntity().verticalCollision;
    }

    @Override
    public void tick() {
        switch (phase) {
            case NONE -> {
            }
            case ASCEND -> tickAscend();
            case DIVE -> tickDive();
        }

        if (!canKeepUsing() && started) stopEarly(stoppedByFloorOrWall());
    }

    @Override
    public boolean canUse() {
        return this.entity.getEntity().isOnGround();
    }

    @Override
    public boolean canKeepUsing() {
        if (phase != Phase.NONE && started) {
            if (entity.getEntity().isSpectator()) {
               return false;
            } else if (entity.getChangedEntity().isFlying()) {
                return false;
            } else if (entity.getEntity() instanceof Player player && player.getAbilities().flying) {
                return false; // fail Safe
            }
        }

        if (phase == Phase.ASCEND) return true;
        if (phase == Phase.DIVE && this.divingTicks > this.getDivingTicksTimeout()) {
            return false;
        }

        return phase != Phase.NONE && !entity.getEntity().isOnGround()
                && !entity.getEntity().horizontalCollision
                && !entity.getEntity().verticalCollision;
    }

    @Override
    public void stopUsing() {
    }

    public void stopEarly(boolean applyImpact) {
        entity.getEntity().setNoGravity(false);
        entity.getEntity().removeEffect(MobEffects.SLOW_FALLING);

        if (applyImpact) {
            onImpact();
        }

        this.getController().applyCoolDown();
        this.phase = Phase.NONE;
        this.ascendTicks = 0;
        this.divingTicks = 0;
        this.started = false;
    }

    @Override
    public void onRemove() {
        this.phase = Phase.NONE;
        entity.getEntity().setNoGravity(false);
        super.onRemove();
    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);
        if (tag.contains("phaseState")) this.phase = Phase.valueOf(tag.getString("phaseState"));
        if (tag.contains("ascendTicks")) this.ascendTicks = tag.getInt("ascendTicks");
        if (tag.contains("divingTicks")) this.divingTicks = tag.getInt("divingTicks");
        if (tag.contains("started")) this.started = tag.getBoolean("started");
    }

    @Override
    public void saveData(CompoundTag tag) {
        super.saveData(tag);
        tag.putString("phaseState", this.phase.name());
        tag.putInt("ascendTicks", this.ascendTicks);
        tag.putInt("divingTicks", this.divingTicks);
        tag.putBoolean("started", this.started);
    }

    /* ---------------- Phases ---------------- */

    protected void tickAscend() {
        this.ascendTicks++;
        if (ascendTicks < getAscendNeededTicks()) {
            Vec3 dm = entity.getEntity().getDeltaMovement();
            entity.getEntity().setDeltaMovement(dm.x, getAscendSpeed(), dm.z);
            return;
        }

        // 🔒 trava direção e entra no dive
        Vec3 look = entity.getEntity().getLookAngle();

        // ignora pitch → apenas yaw
        Vec3 horizontal = new Vec3(look.x, 0, look.z);
        if (horizontal.lengthSqr() < 1.0E-4) {
            horizontal = entity.getEntity().getForward();
        }

        horizontal = horizontal.normalize();

        diveDirection = new Vec3(
                horizontal.x * getDiveSpeed(),
                -getVerticalDiveSpeed(),
                horizontal.z * getDiveSpeed()
        );
        phase = Phase.DIVE;

        entity.getEntity().setNoGravity(false);
    }

    protected void tickDive() {
        this.divingTicks++;
        Vec3 dive = this.getDiveDirection().scale(getDiveSpeed());
        entity.getEntity().setDeltaMovement(dive);

        syncPlayerMotion();
    }

    /* ---------------- Utils ---------------- */

    protected void syncPlayerMotion() {
        if (!(entity.getEntity() instanceof ServerPlayer sp)) return;

        sp.connection.send(
                new ClientboundSetEntityMotionPacket(
                        sp.getId(),
                        sp.getDeltaMovement()
                )
        );
    }

    /* ---------------- Hooks ---------------- */

    public abstract int getAscendNeededTicks();

    public abstract double getAscendSpeed();

    public abstract double getDiveSpeed();

    public abstract double getVerticalDiveSpeed();

    public Vec3 getDiveDirection() {
        return this.diveDirection;
    }

    public int getDivingTicksTimeout() {
        return 100;
    }

    /**
     * chamado quando colide com o chão
     */
    protected abstract void onImpact();
}