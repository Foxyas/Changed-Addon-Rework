package net.foxyas.changedaddon.entity.projectile;

import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class AbstractGenericParticleProjectile extends AbstractArrow {

    @Nullable
    private UUID targetUUID;
    @Nullable
    protected Entity target = null;

    protected ParticleOptions particle = ParticleTypes.END_ROD;
    private int lifeSpamWithoutTarget;
    public boolean teleport = false;

    protected AbstractGenericParticleProjectile(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.setBaseDamage(1.0f);
        this.setPierceLevel((byte) 0);
        this.setKnockback(0);
        this.setSilent(true);
        this.setNoGravity(true);
    }

    protected AbstractGenericParticleProjectile(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
    }

    protected AbstractGenericParticleProjectile(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    public void setTarget(@Nullable LivingEntity target) {
        this.target = target;
        if (target != null) {
            this.targetUUID = target.getUUID();
        }
    }

    public void setParticle(ParticleOptions particle) {
        this.particle = particle;
    }

    public @Nullable Entity getTarget() {
        if (this.target != null && !this.target.isRemoved()) {
            return this.target;
        } else if (this.targetUUID != null && this.level instanceof ServerLevel) {
            this.target = ((ServerLevel)this.level).getEntity(this.targetUUID);
            return this.target;
        } else {
            return null;
        }
    }

    public @Nullable UUID getTargetUUID() {
        return targetUUID;
    }

    public ParticleOptions getParticle() {
        return particle;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("ticksWandering", lifeSpamWithoutTarget);

        if (this.targetUUID != null) {
            tag.putUUID("target", this.targetUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("ticksWandering")) this.lifeSpamWithoutTarget = tag.getInt("ticksWandering");

        if (tag.hasUUID("target")) {
            this.targetUUID = tag.getUUID("target");
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.setRemainingFireTicks(0);
        if (!(this.getTarget() instanceof LivingEntity livingTarget)) {
            return;
        }

        if (!level.isClientSide && livingTarget.isAlive()) {
            if (this.inGround || this.onGround) {
                PlayerUtilProcedure.ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f,0.05f,0.05f, 20, 0.5f);
                this.discard();
            }
            this.lifeSpamWithoutTarget = 0;
            if (livingTarget instanceof Player player) {
                if (player.isCreative() || player.isSpectator()) {
                    PlayerUtilProcedure.ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f,0.05f,0.05f, 20, 0.5f);
                    this.discard();
                }
            }
            double dx = livingTarget.getX() - getX();
            double dy = (livingTarget.getY() + livingTarget.getBbHeight() / 2.0) - getY();
            double dz = livingTarget.getZ() - getZ();

            double speed = 0.35;
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distance > 0.001) {
                this.setDeltaMovement(dx / distance * speed, dy / distance * speed, dz / distance * speed);
                this.hasImpulse = true;
            }
        } else if (!level.isClientSide && livingTarget.isDeadOrDying()) {
            this.lifeSpamWithoutTarget ++;
            if (this.lifeSpamWithoutTarget >= 120) {
                PlayerUtilProcedure.ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f,0.05f,0.05f, 20, 0.5f);
                this.lifeSpamWithoutTarget = 0;
                this.discard();
            }
        } else if (!level.isClientSide() && (this.getOwner() == null
                || (this.getOwner() instanceof LivingEntity livingEntity && livingEntity.isDeadOrDying()))) {
            PlayerUtilProcedure.ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f,0.05f,0.05f, 20, 0.5f);
            this.discard();
        }

        PlayerUtilProcedure.ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.3f,0.3f,0.3f, 1, 0.005f);

        // Partículas
        /*if (level.isClientSide) {
            for (int i = 0; i < 2; ++i) {
                double offsetX = (random.nextDouble() - 0.5) * 0.2;
                double offsetY = (random.nextDouble() - 0.5) * 0.2;
                double offsetZ = (random.nextDouble() - 0.5) * 0.2;
                level.addParticle(particle,
                        getX() + offsetX,
                        getY() + offsetY,
                        getZ() + offsetZ,
                        0.0, 0.01, 0.0
                );
            }
        }*/
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        Entity entity = result.getEntity();
        float f = (float)this.getDeltaMovement().length();
        int i = Mth.ceil(Mth.clamp((double)f * this.getBaseDamage(), 0.0D, 2.147483647E9D));

        if (this.isCritArrow()) {
            long j = this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(j + (long)i, 2147483647L);
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource;
        if (entity1 == null) {
            damagesource = DamageSource.arrow(this, this);
        } else {
            damagesource = DamageSource.arrow(this, entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity)entity1).setLastHurtMob(entity);
            }
        }

        if (entity.hurt(damagesource, (float)i)) {
            if (entity instanceof LivingEntity livingentity) {
                if (this.getKnockback() > 0) {
                    Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.getKnockback() * 0.6D);
                    if (vec3.lengthSqr() > 0.0D) {
                        livingentity.push(vec3.x, 0.1D, vec3.z);
                    }
                }

                if (!this.level.isClientSide && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity);
                }

                this.doPostHurtEffects(livingentity);
                if (livingentity != entity1 && livingentity instanceof Player && entity1 instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }
            }

            this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.discard();
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
            if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            }
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        //super.onHitBlock(result);
        PlayerUtilProcedure.ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f,0.05f,0.05f, 20, 0.5f);
        this.discard();
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity targetHit) {
        // Não adiciona flechas visuais ao corpo

        if (!level.isClientSide) {
            boolean blocked = targetHit.isBlocking();
            if (!blocked && this.getOwner() instanceof LivingEntity owner && this.teleport) {
                if (targetHit.getRandom().nextFloat() <= 0.25f){
                    targetHit.teleportTo(owner.getX(),owner.getY(), owner.getZ());
                } else {
                    owner.teleportTo(targetHit.getX(),targetHit.getY(), targetHit.getZ());
                }
            }
        }
    }
}
