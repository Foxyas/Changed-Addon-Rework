package net.foxyas.changedaddon.entity.projectile;

import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGenericParticleProjectile extends AbstractArrow {

    @Nullable
    protected LivingEntity target = null;

    protected ParticleOptions particle = ParticleTypes.END_ROD;
    private int lifeSpamWithoutTarget;

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
    }

    public void setParticle(ParticleOptions particle) {
        this.particle = particle;
    }

    public @Nullable LivingEntity getTarget() {
        return target;
    }

    public ParticleOptions getParticle() {
        return particle;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("ticksWandering", lifeSpamWithoutTarget);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("ticksWandering")) this.lifeSpamWithoutTarget = tag.getInt("ticksWandering");
    }

    @Override
    public void tick() {
        super.tick();
        this.setRemainingFireTicks(0);


        if (!level.isClientSide && target != null && target.isAlive()) {
            this.lifeSpamWithoutTarget = 0;
            if (target instanceof Player player) {
                if (player.isCreative() || player.isSpectator()) {
                    PlayerUtilProcedure.ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f,0.05f,0.05f, 20, 0.5f);
                    this.discard();
                }
            }
            double dx = target.getX() - getX();
            double dy = (target.getY() + target.getBbHeight() / 2.0) - getY();
            double dz = target.getZ() - getZ();

            double speed = 0.35;
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distance > 0.001) {
                this.setDeltaMovement(dx / distance * speed, dy / distance * speed, dz / distance * speed);
                this.hasImpulse = true;
            }
        } else if (!level.isClientSide && (target == null || target.isDeadOrDying())) {
            this.lifeSpamWithoutTarget ++;
            if (this.lifeSpamWithoutTarget >= 120) {
                PlayerUtilProcedure.ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.05f,0.05f,0.05f, 20, 0.5f);
                this.lifeSpamWithoutTarget = 0;
                this.discard();
            }

        }

        PlayerUtilProcedure.ParticlesUtil.sendParticles(this.level, particle, this.position(), 0.3f,0.3f,0.3f, 2, 0.05f);

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
        super.onHitEntity(result);
        if (!level.isClientSide && result.getEntity() instanceof LivingEntity targetHit) {
            boolean blocked = targetHit.isBlocking();

            if (!blocked && this.getOwner() instanceof LivingEntity owner) {
                DamageSource source = DamageSource.indirectMagic(this, owner);
                targetHit.hurt(source, 4.0F); // você pode ajustar esse valor
                targetHit.setArrowCount(0);
                if (targetHit.getRandom().nextFloat() <= 0.25f){
                    targetHit.teleportTo(owner.getX(),owner.getY(), owner.getZ());
                } else {
                    owner.teleportTo(targetHit.getX(),targetHit.getY(), targetHit.getZ());
                }
            }
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
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
    protected void doPostHurtEffects(@NotNull LivingEntity target) {
        // Não adiciona flechas visuais ao corpo
    }
}
