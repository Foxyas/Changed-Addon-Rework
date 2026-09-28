package net.foxyas.changedaddon.entity.projectile;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CrystalShardProjectile extends Projectile {

    private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(CrystalShardProjectile.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> PIERCE_LEVEL = SynchedEntityData.defineId(CrystalShardProjectile.class, EntityDataSerializers.BYTE);

    private BlockState lastState;
    protected boolean inGround;
    protected int inGroundTime;
    public AbstractArrow.Pickup pickup = AbstractArrow.Pickup.DISALLOWED;
    private int life;
    private double baseDamage = 2.0D;
    private int knockback;
    private SoundEvent soundEvent = getDefaultHitGroundSoundEvent();
    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;
    private final IntOpenHashSet ignoredEntities = new IntOpenHashSet();

    public CrystalShardProjectile(Level level) {
        super(ChangedAddonEntities.CRYSTAL_SHARD.get(), level);
    }

    public CrystalShardProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void setSoundEvent(SoundEvent pSoundEvent) {
        soundEvent = pSoundEvent;
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        double d0 = getBoundingBox().getSize() * 10.0D;
        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 *= 64.0D * getViewScale();
        return pDistance < d0 * d0;
    }

    protected void defineSynchedData() {
        entityData.define(ID_FLAGS, (byte)0);
        entityData.define(PIERCE_LEVEL, (byte)0);
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
        life = 0;
    }

    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        setPos(pX, pY, pZ);
        setRot(pYaw, pPitch);
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    public void lerpMotion(double pX, double pY, double pZ) {
        super.lerpMotion(pX, pY, pZ);
        life = 0;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        boolean flag = isNoPhysics();
        Vec3 vec3 = getDeltaMovement();
        if (xRotO == 0.0F && yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            yRotO = getYRot();
            xRotO = getXRot();
        }

        BlockPos blockpos = blockPosition();
        BlockState blockstate = level().getBlockState(blockpos);
        if (!blockstate.isAir() && !flag) {
            VoxelShape voxelshape = blockstate.getCollisionShape(level(), blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3 vec31 = position();

                for(AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vec31)) {
                        inGround = true;
                        break;
                    }
                }
            }
        }

        if (isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW) || isInFluidType((fluidType, height) -> canFluidExtinguish(fluidType))) {
            clearFire();
        }

        if (inGround && !flag) {
            if (lastState != blockstate && shouldFall()) {
                startFalling();
            } else if (!level().isClientSide) {
                tickDespawn();
            }

            ++inGroundTime;
        } else {
            inGroundTime = 0;
            Vec3 vec32 = position();
            Vec3 vec33 = vec32.add(vec3);
            HitResult hitresult = level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hitresult.getType() != HitResult.Type.MISS) {
                vec33 = hitresult.getLocation();
            }

            while(!isRemoved()) {
                EntityHitResult entityhitresult = findHitEntity(vec32, vec33);
                if (entityhitresult != null) {
                    hitresult = entityhitresult;
                }

                if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                    Entity entity = ((EntityHitResult)hitresult).getEntity();
                    Entity entity1 = getOwner();
                    if (entity instanceof Player && entity1 instanceof Player && !((Player)entity1).canHarmPlayer((Player)entity)) {
                        hitresult = null;
                        entityhitresult = null;
                    }
                }

                if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !flag) {
                    var result = net.minecraftforge.event.ForgeEventFactory.onProjectileImpactResultNullable(this, hitresult);
                    if (result == null) {
                        if (hitresult.getType() != HitResult.Type.ENTITY)
                            break;

                        result = net.minecraftforge.event.entity.ProjectileImpactEvent.ImpactResult.SKIP_ENTITY;
                    }
                    switch (result) {
                        case SKIP_ENTITY:
                            if (hitresult.getType() != HitResult.Type.ENTITY) { // If there is no entity, we just return default behaviour
                                onHit(hitresult);
                                hasImpulse = true;
                                break;
                            }
                            ignoredEntities.add(entityhitresult.getEntity().getId());
                            entityhitresult = null; // Don't process any further
                            break;
                        case STOP_AT_CURRENT_NO_DAMAGE:
                            discard();
                            entityhitresult = null; // Don't process any further
                            break;
                        case STOP_AT_CURRENT:
                            setPierceLevel((byte) 0);
                        case DEFAULT:
                            onHit(hitresult);
                            hasImpulse = true;
                            break;
                    }
                }

                if (entityhitresult == null || getPierceLevel() <= 0) {
                    break;
                }

                hitresult = null;
            }

            if (isRemoved())
                return;

            vec3 = getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;
            if (isCrit()) {
                for(int i = 0; i < 4; ++i) {
                    level().addParticle(ParticleTypes.CRIT, getX() + d5 * (double)i / 4.0D, getY() + d6 * (double)i / 4.0D, getZ() + d1 * (double)i / 4.0D, -d5, -d6 + 0.2D, -d1);
                }
            }

            double d7 = getX() + d5;
            double d2 = getY() + d6;
            double d3 = getZ() + d1;
            double d4 = vec3.horizontalDistance();
            if (flag) {
                setYRot((float)(Mth.atan2(-d5, -d1) * (double)(180F / (float)Math.PI)));
            } else {
                setYRot((float)(Mth.atan2(d5, d1) * (double)(180F / (float)Math.PI)));
            }

            setXRot((float)(Mth.atan2(d6, d4) * (double)(180F / (float)Math.PI)));
            setXRot(lerpRotation(xRotO, getXRot()));
            setYRot(lerpRotation(yRotO, getYRot()));
            float f = 0.99F;
            if (isInWater()) {
                for(int j = 0; j < 4; ++j) {
                    level().addParticle(ParticleTypes.BUBBLE, d7 - d5 * 0.25D, d2 - d6 * 0.25D, d3 - d1 * 0.25D, d5, d6, d1);
                }

                f = getWaterInertia();
            }

            setDeltaMovement(vec3.scale(f));
            if (!isNoGravity() && !flag) {
                Vec3 vec34 = getDeltaMovement();
                setDeltaMovement(vec34.x, vec34.y - (double)0.05F, vec34.z);
            }

            setPos(d7, d2, d3);
            checkInsideBlocks();
        }
    }

    private boolean shouldFall() {
        return inGround && level().noCollision((new AABB(position(), position())).inflate(0.06D));
    }

    private void startFalling() {
        inGround = false;
        Vec3 vec3 = getDeltaMovement();
        setDeltaMovement(vec3.multiply(random.nextFloat() * 0.2F, random.nextFloat() * 0.2F, random.nextFloat() * 0.2F));
        life = 0;
    }

    public void move(MoverType pType, Vec3 pPos) {
        super.move(pType, pPos);
        if (pType != MoverType.SELF && shouldFall()) {
            startFalling();
        }
    }

    protected void tickDespawn() {
        ++life;
        if (life >= 1200) {
            discard();
        }

    }

    private void resetPiercedEntities() {
        if (piercingIgnoreEntityIds != null) {
            piercingIgnoreEntityIds.clear();
        }
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();
        float f = (float)getDeltaMovement().length();
        int i = Mth.ceil(Mth.clamp((double)f * baseDamage, 0.0D, Integer.MAX_VALUE));
        if (getPierceLevel() > 0) {
            if (piercingIgnoreEntityIds == null) {
                piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (piercingIgnoreEntityIds.size() >= getPierceLevel() + 1) {
                discard();
                return;
            }

            piercingIgnoreEntityIds.add(entity.getId());
        }

        if (isCrit()) {
            long j = random.nextInt(i / 2 + 2);
            i = (int)Math.min(j + (long)i, 2147483647L);
        }

        Entity entity1 = getOwner();
        DamageSource damagesource;
        if (entity1 == null) {
            damagesource = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FREEZE), this, this);
        } else {
            damagesource = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FREEZE), this, entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity)entity1).setLastHurtMob(entity);
            }
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int k = entity.getRemainingFireTicks();
        if (isOnFire() && !flag) {
            entity.setSecondsOnFire(5);
        }

        if (entity.hurt(damagesource, (float)i)) {
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity livingentity) {
                if (!level().isClientSide && getPierceLevel() <= 0) {
                    livingentity.setArrowCount(livingentity.getArrowCount() + 1);
                }

                if (knockback > 0) {
                    double d0 = Math.max(0.0D, 1.0D - livingentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    Vec3 vec3 = getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)knockback * 0.6D * d0);
                    if (vec3.lengthSqr() > 0.0D) {
                        livingentity.push(vec3.x, 0.1D, vec3.z);
                    }
                }

                if (!level().isClientSide && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity);
                }

                doPostHurtEffects(livingentity);
                if (livingentity != entity1 && livingentity instanceof Player && entity1 instanceof ServerPlayer && !isSilent()) {
                    ((ServerPlayer)entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }
            }

            playSound(soundEvent, 1.0F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
            if (getPierceLevel() <= 0) {
                discard();
            }
        } else {
            entity.setRemainingFireTicks(k);
            setDeltaMovement(getDeltaMovement().scale(-0.1D));
            setYRot(getYRot() + 180.0F);
            yRotO += 180.0F;
            if (!level().isClientSide && getDeltaMovement().lengthSqr() < 1.0E-7D) {
                discard();
            }
        }
    }

    protected void onHitBlock(BlockHitResult pResult) {
        lastState = level().getBlockState(pResult.getBlockPos());
        super.onHitBlock(pResult);
        Vec3 vec3 = pResult.getLocation().subtract(getX(), getY(), getZ());
        setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale(0.05F);
        setPosRaw(getX() - vec31.x, getY() - vec31.y, getZ() - vec31.z);
        playSound(getHitGroundSoundEvent(), 1.0F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
        inGround = true;
        setCrit(false);
        setPierceLevel((byte)0);
        setSoundEvent(SoundEvents.ARROW_HIT);
        resetPiercedEntities();
    }

    /**
     * The sound made when an entity is hit by this projectile
     */
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ARROW_HIT;
    }

    protected final SoundEvent getHitGroundSoundEvent() {
        return soundEvent;
    }

    protected void doPostHurtEffects(LivingEntity pTarget) {
    }

    /**
     * Gets the EntityHitResult representing the entity hit
     */
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return ProjectileUtil.getEntityHitResult(level(), this, pStartVec, pEndVec, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    protected boolean canHitEntity(@NotNull Entity p_36743_) {
        return super.canHitEntity(p_36743_) && (piercingIgnoreEntityIds == null || !piercingIgnoreEntityIds.contains(p_36743_.getId())) && !ignoredEntities.contains(p_36743_.getId());
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putShort("life", (short)life);
        if (lastState != null) {
            pCompound.put("inBlockState", NbtUtils.writeBlockState(lastState));
        }

        pCompound.putBoolean("inGround", inGround);
        pCompound.putByte("pickup", (byte)pickup.ordinal());
        pCompound.putDouble("damage", baseDamage);
        pCompound.putBoolean("crit", isCrit());
        pCompound.putByte("PierceLevel", getPierceLevel());
        pCompound.putString("SoundEvent", BuiltInRegistries.SOUND_EVENT.getKey(soundEvent).toString());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        life = pCompound.getShort("life");
        if (pCompound.contains("inBlockState", 10)) {
            lastState = NbtUtils.readBlockState(level().holderLookup(Registries.BLOCK), pCompound.getCompound("inBlockState"));
        }

        inGround = pCompound.getBoolean("inGround");
        if (pCompound.contains("damage", 99)) {
            baseDamage = pCompound.getDouble("damage");
        }

        setCrit(pCompound.getBoolean("crit"));
        setPierceLevel(pCompound.getByte("PierceLevel"));
        if (pCompound.contains("SoundEvent", 8)) {
            soundEvent = BuiltInRegistries.SOUND_EVENT.getOptional(ResourceLocation.parse(pCompound.getString("SoundEvent"))).orElse(this.getDefaultHitGroundSoundEvent());
        }
    }

    protected Entity.@NotNull MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    public void setBaseDamage(double pBaseDamage) {
        baseDamage = pBaseDamage;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    /**
     * Sets the amount of knockback the arrow applies when it hits a mob.
     */
    public void setKnockback(int pKnockback) {
        knockback = pKnockback;
    }

    public int getKnockback() {
        return knockback;
    }

    /**
     * Returns {@code true} if it's possible to attack this entity with an item.
     */
    public boolean isAttackable() {
        return false;
    }

    protected float getEyeHeight(@NotNull Pose pPose, @NotNull EntityDimensions pSize) {
        return 0.13F;
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public void setCrit(boolean pCritArrow) {
        setFlag(1, pCritArrow);
    }

    public void setPierceLevel(byte pPierceLevel) {
        entityData.set(PIERCE_LEVEL, pPierceLevel);
    }

    private void setFlag(int pId, boolean pValue) {
        byte b0 = entityData.get(ID_FLAGS);
        if (pValue) {
            entityData.set(ID_FLAGS, (byte)(b0 | pId));
        } else {
            entityData.set(ID_FLAGS, (byte)(b0 & ~pId));
        }
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public boolean isCrit() {
        byte b0 = entityData.get(ID_FLAGS);
        return (b0 & 1) != 0;
    }

    public byte getPierceLevel() {
        return entityData.get(PIERCE_LEVEL);
    }

    protected float getWaterInertia() {
        return 0.6F;
    }

    /**
     * Sets if this arrow can noClip
     */
    public void setNoPhysics(boolean pNoPhysics) {
        noPhysics = pNoPhysics;
        setFlag(2, pNoPhysics);
    }

    /**
     * Whether the arrow can noClip
     */
    public boolean isNoPhysics() {
        if (!level().isClientSide) {
            return noPhysics;
        } else {
            return (entityData.get(ID_FLAGS) & 2) != 0;
        }
    }
}
