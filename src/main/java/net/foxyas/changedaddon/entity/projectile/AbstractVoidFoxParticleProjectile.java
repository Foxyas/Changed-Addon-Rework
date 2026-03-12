package net.foxyas.changedaddon.entity.projectile;

import net.foxyas.changedaddon.entity.bosses.VoidFoxEntity;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class AbstractVoidFoxParticleProjectile extends ParriableProjectile {

    private static final EntityDataAccessor<Boolean> PARRY_ABLE =
            SynchedEntityData.defineId(AbstractVoidFoxParticleProjectile.class, EntityDataSerializers.BOOLEAN);
    public boolean teleport = false;
    @Nullable
    protected Entity target = null;
    @Nullable
    protected Vec3 targetPos = null;
    protected ParticleOptions particle = ParticleTypes.END_ROD;
    protected int lifeSpamWithoutTarget;
    protected int lifeSpamNearTarget = 0;
    @Nullable
    private UUID targetUUID;
    private boolean smoothMotion = false;

    protected AbstractVoidFoxParticleProjectile(EntityType<? extends AbstractVoidFoxParticleProjectile> type, Level level) {
        super(type, level);
        this.setBaseDamage(1.0f);
        this.setPierceLevel((byte) 0);
        this.setKnockback(0);
        this.setSilent(true);
        this.setNoGravity(true);
    }

    protected AbstractVoidFoxParticleProjectile(EntityType<? extends AbstractVoidFoxParticleProjectile> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
    }

    protected AbstractVoidFoxParticleProjectile(EntityType<? extends AbstractVoidFoxParticleProjectile> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PARRY_ABLE, false);
    }

    public boolean isParryAble() {
        return this.entityData.get(PARRY_ABLE);
    }

    public void setParryAble(boolean set) {
        this.entityData.set(PARRY_ABLE, set);
    }

    public @Nullable Entity getTarget() {
        if (this.target != null && !this.target.isRemoved()) {
            return this.target;
        } else if (this.targetUUID != null && this.level instanceof ServerLevel) {
            this.target = ((ServerLevel) this.level).getEntity(this.targetUUID);
            return this.target;
        } else {
            return null;
        }
    }

    public void setTarget(@Nullable LivingEntity target) {
        this.target = target;
        if (target != null) {
            this.targetUUID = target.getUUID();
        }
    }

    public void setTarget(@Nullable Entity target) {
        this.target = target;
    }

    public @Nullable UUID getTargetUUID() {
        return targetUUID;
    }

    public void setTargetUUID(@Nullable UUID targetUUID) {
        this.targetUUID = targetUUID;
    }

    public ParticleOptions getParticle() {
        return particle;
    }

    public void setParticle(ParticleOptions particle) {
        this.particle = particle;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("ticksWandering", lifeSpamWithoutTarget);
        tag.putInt("lifeSpamNearTarget", lifeSpamNearTarget);

        tag.putBoolean("isSmoothMotion", smoothMotion);
        tag.putBoolean("isParryAble", this.isParryAble());


        if (targetPos != null) {
            tag.put("TargetPos", this.newDoubleList(targetPos.x, targetPos.y, targetPos.z));
        }

        if (this.targetUUID != null) {
            tag.putUUID("target", this.targetUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("ticksWandering")) this.lifeSpamWithoutTarget = tag.getInt("ticksWandering");
        if (tag.contains("lifeSpamNearTarget")) this.lifeSpamNearTarget = tag.getInt("lifeSpamNearTarget");

        if (tag.contains("isSmoothMotion")) this.smoothMotion = tag.getBoolean("isSmoothMotion");
        if (tag.contains("isParryAble")) this.setParryAble(tag.getBoolean("isParryAble"));

        if (tag.contains("TargetPos")) {
            try {
                ListTag TargetPosList = tag.getList("TargetPos", 6);

                this.targetPos = new Vec3(TargetPosList.getDouble(0),
                        TargetPosList.getDouble(1),
                        TargetPosList.getDouble(2));
            } catch (Throwable ignored) {
                this.targetPos = Vec3.ZERO;
            }
        }

        if (tag.hasUUID("target")) {
            this.targetUUID = tag.getUUID("target");
        }
    }

    public @Nullable Vec3 getTargetPos() {
        return targetPos;
    }

    public void setTargetPos(@Nullable Vec3 targetPos) {
        this.targetPos = targetPos;
    }

    public int getLifeSpamWithoutTarget() {
        return lifeSpamWithoutTarget;
    }

    public void setLifeSpamWithoutTarget(int lifeSpamWithoutTarget) {
        this.lifeSpamWithoutTarget = lifeSpamWithoutTarget;
    }

    @Override
    public void tick() {
        super.tick();
        this.setRemainingFireTicks(0);

        if (this.tickCount > 400) {
            ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 20, 0.5f);
            this.discard();
            return;
        }

        if (this.lifeSpamNearTarget >= 100) {
            ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 20, 0.5f);
            this.lifeSpamWithoutTarget = 0;
            this.discard();
            return;
        }


        // Se só tiver posição fixa
        if (this.getTarget() == null && targetPos != null) {
            if (this.onGround()) {
                ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 20, 0.5f);
                this.discard();
                return;
            }
            this.lifeSpamWithoutTarget = 0;
            double dx = targetPos.x() - getX();
            double dy = targetPos.y() - getY();
            double dz = targetPos.z() - getZ();

            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            this.lookAt(EntityAnchorArgument.Anchor.EYES, targetPos);

            if (distance > 0.1f) {
                double speed = 0.35;

                // Direção normalizada desejada
                Vec3 desiredMotion = new Vec3(dx / distance, dy / distance, dz / distance).scale(speed);

                if (smoothMotion) {
                    this.applyMotionSmooth(desiredMotion);
                } else {
                    this.applyMotion(desiredMotion);
                }
            } else {
                lifeSpamNearTarget++;
            }
        }

        if (!(this.getTarget() instanceof LivingEntity livingTarget)) {
            return;
        }


        if (!level.isClientSide && livingTarget.isAlive()) {
            if (getOwner() != null && livingTarget.is(getOwner())) {
                ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 20, 0.5f);
                this.discard();
                return;
            }
            if (this.onGround()) {
                ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 20, 0.5f);
                this.discard();
                return;
            }
            this.lifeSpamWithoutTarget = 0;
            if (livingTarget instanceof Player player) {
                if (player.isCreative() || player.isSpectator()) {
                    ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 20, 0.5f);
                    this.discard();
                    return;
                }
            }
            double dx = livingTarget.getX() - getX();
            double dy = (livingTarget.getY() + livingTarget.getBbHeight() / 2.0) - getY();
            double dz = livingTarget.getZ() - getZ();
            this.lookAt(EntityAnchorArgument.Anchor.EYES, livingTarget.position());


            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (distance > 0.1f) {
                double speed = 0.35;

                // Direção normalizada desejada
                Vec3 desiredMotion = new Vec3(dx / distance, dy / distance, dz / distance).scale(speed);

                if (smoothMotion) {
                    this.applyMotionSmooth(desiredMotion);
                } else {
                    this.applyMotion(desiredMotion);
                }
            } else {
                lifeSpamNearTarget++;
                this.onHitEntity(new EntityHitResult(livingTarget));
            }
        } else if (!level.isClientSide && livingTarget.isDeadOrDying()) {
            this.lifeSpamWithoutTarget++;
            if (this.lifeSpamWithoutTarget >= 120) {
                ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 20, 0.5f);
                this.lifeSpamWithoutTarget = 0;
                this.discard();
                return;
            }
        } else if (!level.isClientSide() && (this.getOwner() == null
                || (this.getOwner() instanceof LivingEntity livingEntity && livingEntity.isDeadOrDying()))) {
            ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 20, 0.5f);
            this.discard();
            return;
        }

        ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.3f, 0.3f, 0.3f, 1, 0.005f);

        // Partículas
    }

    public boolean isSmoothMotion() {
        return smoothMotion;
    }

    public void setSmoothMotion(boolean smoothMotion) {
        this.smoothMotion = smoothMotion;
    }

    public void applyMotion(Vec3 desiredMotion) {
        this.lerpMotion(desiredMotion.x, desiredMotion.y, desiredMotion.z);
        this.hasImpulse = true;
    }

    public void applyMotionSmooth(Vec3 desiredMotion) {
        // Movimento atual
        Vec3 currentMotion = this.getDeltaMovement();

        //Interpolação: 0.1 = mais suave; 1.0 = instantâneo
        double lerpFactor = 0.1;
        Vec3 smoothedMotion = currentMotion.lerp(desiredMotion, lerpFactor);

        this.setDeltaMovement(smoothedMotion);
        this.hasImpulse = true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        if (result.getEntity() instanceof VoidFoxEntity voidFox) {
            voidFox.invulnerableTime = 0;
            voidFox.hurtDuration = 1;
            voidFox.hurtTime = 1;
            super.onHitEntity(result);
            return;
        }

        if (this.isParryAble()) {
            if (result.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.invulnerableTime = 0;
                livingEntity.hurtDuration = 0;
                livingEntity.hurtTime = 0;
                livingEntity.hurtMarked = false;
            }
        }

        if (this.isParryAble()) {
            this.HandleParry(result);
        } else {
            ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 5, 0.5f);
            super.onHitEntity(result);
            if (result.getEntity().hurtMarked) {
                if (!this.isRemoved()) {
                    ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 20, 0.5f);
                    this.discard();
                }
            }
        }
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity entity) {
        if (entity instanceof AbstractGenericParticleProjectile genericParticleProjectile) {
            if (genericParticleProjectile.getOwner() == this.getOwner()) {
                return false;
            }
        }
        if (getOwner() instanceof Player player) {
            return !player.is(entity);
        } else if (getOwner() instanceof VoidFoxEntity voidFoxEntity && voidFoxEntity.is(entity)) {
            return true;
        }
        return super.canHitEntity(entity);
    }

    private void HandleParry(@NotNull EntityHitResult result) {
        Entity entity = result.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.isBlocking()) {
                if (this.getOwner() == null) {
                    ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 5, 0.5f);
                    if (!this.isRemoved()) {
                        ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 20, 0.5f);
                        this.discard();
                    }
                }
                if (this.getOwner() != null && this.getTarget() != null) {
                    Entity oTarget = this.getTarget();
                    Entity oOwner = this.getOwner();
                    this.setOwner(oTarget);
                    this.setTarget(oOwner);
                    this.setBaseDamage(5d);
                    if (!oTarget.level.isClientSide() && oTarget.level() instanceof ServerLevel serverLevel) {
                        serverLevel.playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.SHIELD_BLOCK, SoundSource.MASTER, 1.0F, 1.0F);
                        serverLevel.playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.ANVIL_LAND, SoundSource.MASTER, 0.5F, 1.2F);
                        for (EquipmentSlot itemBySlot : EquipmentSlot.values()) {
                            ItemStack stack = livingEntity.getItemBySlot(itemBySlot);
                            stack.setDamageValue(Math.max(stack.getDamageValue() - 2, 0));
                        }
                    }
                    this.setDeltaMovement(this.getDeltaMovement().scale(-1));
                    this.lifeSpamNearTarget = 0;
                    this.lifeSpamWithoutTarget = 0;
                }
            }
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float amount) {
        if (this.isParryAble()) {
            Entity entity = damageSource.getDirectEntity();
            if (entity instanceof LivingEntity livingEntity) {
                if (this.getOwner() == null) {
                    ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 5, 0.5f);
                    if (!this.isRemoved()) {
                        ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 20, 0.5f);
                        this.discard();
                    }
                }
                if (this.getOwner() != null && this.getTarget() != null) {
                    Entity oTarget = this.getTarget();
                    Entity oOwner = this.getOwner();
                    this.setOwner(oTarget);
                    this.setTarget(oOwner);
                    this.setBaseDamage(5d);
                    if (!oTarget.level().isClientSide() && oTarget.level() instanceof ServerLevel serverLevel) {
                        serverLevel.playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.SHIELD_BLOCK, SoundSource.MASTER, 1.0F, 1.0F);
                        serverLevel.playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.ANVIL_LAND, SoundSource.MASTER, 0.5F, 1.2F);
                        for (EquipmentSlot itemBySlot : EquipmentSlot.values()) {
                            ItemStack stack = livingEntity.getItemBySlot(itemBySlot);
                            stack.setDamageValue(Math.max(stack.getDamageValue() - 2, 0));
                        }
                    }
                    this.setDeltaMovement(this.getDeltaMovement().scale(-1));
                    this.lifeSpamNearTarget = 0;
                    this.lifeSpamWithoutTarget = 0;
                    this.markHurt();
                    return true;
                }
            }
        } else if (damageSource.getEntity() != null) {
            if (this.isInvulnerableTo(damageSource)) {
                return false;
            }
            this.setDeltaMovement(this.getDeltaMovement().scale(-amount * 0.1));
            this.lifeSpamNearTarget = 0;
            this.lifeSpamWithoutTarget = 0;
            this.markHurt();
            if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
                serverLevel.playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.SHIELD_BLOCK, SoundSource.MASTER, 1.0F, 1.0F);
                serverLevel.playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.ANVIL_LAND, SoundSource.MASTER, 0.5F, 1.2F);
            }
            return true;
        }
        return super.hurt(damageSource, amount);
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        //super.onHitBlock(result);
        ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f, 0.05f, 0.05f, 20, 0.5f);
        this.discard();
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean shouldBeSaved() {
        return getOwner() != null;
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity targetHit) {
        // Não adiciona flechas visuais ao corpo

        if (!level.isClientSide) {
            boolean blocked = targetHit.isBlocking();
            if (!blocked && this.getOwner() instanceof LivingEntity owner && this.teleport) {
                if (targetHit.getRandom().nextFloat() <= 0.25f) {
                    targetHit.teleportTo(owner.getX(), owner.getY(), owner.getZ());
                } else {
                    owner.teleportTo(targetHit.getX(), targetHit.getY(), targetHit.getZ());
                }
            }
        }
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
