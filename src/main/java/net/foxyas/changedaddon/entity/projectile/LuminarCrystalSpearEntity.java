
package net.foxyas.changedaddon.entity.projectile;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.process.util.FoxyasUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class LuminarCrystalSpearEntity extends AbstractArrow implements ItemSupplier {

    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(LuminarCrystalSpearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(LuminarCrystalSpearEntity.class, EntityDataSerializers.BOOLEAN);
    private ItemStack SpearItem = new ItemStack(ChangedAddonItems.LUMINAR_CRYSTAL_SPEAR.get());
    private boolean dealtDamage;
    public int clientSideReturnSpearTickCount;

    public LuminarCrystalSpearEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(ChangedAddonEntities.LUMINAR_CRYSTAL_SPEAR.get(), world);
    }

    public LuminarCrystalSpearEntity(Level p_37569_, LivingEntity p_37570_, ItemStack p_37571_) {
        super(ChangedAddonEntities.LUMINAR_CRYSTAL_SPEAR.get(), p_37570_, p_37569_);
        this.SpearItem = p_37571_.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(p_37571_));
        this.entityData.set(ID_FOIL, p_37571_.hasFoil());
    }


    public LuminarCrystalSpearEntity(EntityType<? extends LuminarCrystalSpearEntity> type, Level world) {
        super(type, world);
    }

    public LuminarCrystalSpearEntity(EntityType<? extends LuminarCrystalSpearEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public LuminarCrystalSpearEntity(EntityType<? extends LuminarCrystalSpearEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte) 0);
        this.entityData.define(ID_FOIL, false);
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull ItemStack getItem() {
        return this.SpearItem;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return this.SpearItem;
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level.isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double) i, this.getZ());
                if (this.level.isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double) i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnSpearTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnSpearTickCount;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Nullable
    protected EntityHitResult findHitEntity(@NotNull Vec3 p_37575_, @NotNull Vec3 p_37576_) {
        return this.dealtDamage ? null : super.findHitEntity(p_37575_, p_37576_);
    }


    /*@Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            BlockState hitState = serverLevel.getBlockState(result.getBlockPos());
            if (hitState.is(ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get()) || hitState.isAir()) {
                return;
            }
            Explosion explosion = new Explosion(serverLevel, this, this.position().x(), this.position().y(), this.position().z(), 3f);
            //AABB BoundBox = new AABB(result.getBlockPos());
            //BoundBox.inflate(1 + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, this.SpearItem));
            int radius = 1 + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, this.SpearItem);

            // Todas as direções possíveis
            Direction[] directions = Direction.values();

            for (Direction dir1 : directions) {
                for (Direction dir2 : directions) {
                    if (dir1 != dir2 && dir1.getAxis() != dir2.getAxis()) {
                        for (BlockPos pos : BlockPos.spiralAround(result.getBlockPos(), radius, dir1, dir2)) {
                            BlockState state = serverLevel.getBlockState(pos);
                            if (state.is(ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get()) || state.isAir()) continue;

                            if (TierSortingRegistry.isCorrectTierForDrops(Tiers.STONE, state) &&
                                    state.getExplosionResistance(serverLevel, result.getBlockPos(), explosion) < 1) {
                                serverLevel.setBlockAndUpdate(pos, ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get().defaultBlockState());
                                serverLevel.playSound(null, pos,
                                        ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get().defaultBlockState().getSoundType().getPlaceSound(),
                                        SoundSource.BLOCKS, 1, 1);
                            }
                        }
                    }
                }
            }

        }
    }*/

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            BlockState hitState = serverLevel.getBlockState(result.getBlockPos());
            if (hitState.is(ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get()) || hitState.isAir()) {
                return;
            }
            Explosion explosion = new Explosion(serverLevel, this, this.position().x(), this.position().y(), this.position().z(), 3f);
            //AABB BoundBox = new AABB(result.getBlockPos());
            //BoundBox.inflate(1 + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, this.SpearItem));
            int radius = 1 + Math.max(0, (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, this.SpearItem) - 1));
            for (BlockPos pos : FoxyasUtils.betweenClosedStreamSphere(result.getBlockPos(), radius, radius).toList()) {
                BlockState state = serverLevel.getBlockState(pos);
                if (state.is(ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get()) || state.isAir()) {
                    continue;
                }
                if (TierSortingRegistry.isCorrectTierForDrops(Tiers.STONE, state) && state.getExplosionResistance(serverLevel, result.getBlockPos(), explosion) < 1) {
                    serverLevel.setBlockAndUpdate(pos, ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get().defaultBlockState());
                    serverLevel.playSound(null, pos,
                            ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get().defaultBlockState().getSoundType().getPlaceSound(),
                            SoundSource.BLOCKS, 1, 1);
                }
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37573_) {
        Entity entity = p_37573_.getEntity();
        float f = 12.0F;
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.SpearItem, livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = DamageSource.trident(this, (Entity) (entity1 == null ? this : entity1));
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity) entity;
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity1);
                }

                this.doPostHurtEffects(livingentity1);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        if (this.level instanceof ServerLevel && this.level.isThundering() && this.isChanneling()) {
            BlockPos blockpos = entity.blockPosition();
            if (this.level.canSeeSky(blockpos)) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level);
                lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                lightningbolt.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer) entity1 : null);
                this.level.addFreshEntity(lightningbolt);
                soundevent = SoundEvents.TRIDENT_THUNDER;
                f1 = 5.0F;
            }
        }

        this.playSound(soundevent, f1, 1.0F);
    }

    public boolean isChanneling() {
        return EnchantmentHelper.hasChanneling(this.SpearItem);
    }

    protected boolean tryPickup(@NotNull Player p_150196_) {
        return super.tryPickup(p_150196_) || this.isNoPhysics() && this.ownedBy(p_150196_) && p_150196_.getInventory().add(this.getPickupItem());
    }

    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    public void playerTouch(@NotNull Player p_37580_) {
        if (this.ownedBy(p_37580_) || this.getOwner() == null) {
            super.playerTouch(p_37580_);
        }

    }

    public void readAdditionalSaveData(@NotNull CompoundTag p_37578_) {
        super.readAdditionalSaveData(p_37578_);
        if (p_37578_.contains("CrystalSpear", 10)) {
            this.SpearItem = ItemStack.of(p_37578_.getCompound("CrystalSpear"));
        }

        this.dealtDamage = p_37578_.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.SpearItem));
    }

    public void addAdditionalSaveData(@NotNull CompoundTag p_37582_) {
        super.addAdditionalSaveData(p_37582_);
        p_37582_.put("CrystalSpear", this.SpearItem.save(new CompoundTag()));
        p_37582_.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }

    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean shouldRender(double p_37588_, double p_37589_, double p_37590_) {
        return true;
    }

    public static LuminarCrystalSpearEntity shoot(Level world, LivingEntity entity, Random random, float power, double damage, int knockback) {
        LuminarCrystalSpearEntity entityarrow = new LuminarCrystalSpearEntity(ChangedAddonEntities.LUMINAR_CRYSTAL_SPEAR.get(), entity, world);
        entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
        entityarrow.setSilent(true);
        entityarrow.setCritArrow(false);
        entityarrow.setBaseDamage(damage);
        entityarrow.setKnockback(knockback);
        world.addFreshEntity(entityarrow);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
        return entityarrow;
    }

    public static LuminarCrystalSpearEntity shoot(LivingEntity entity, LivingEntity target) {
        LuminarCrystalSpearEntity entityarrow = new LuminarCrystalSpearEntity(ChangedAddonEntities.LUMINAR_CRYSTAL_SPEAR.get(), entity, entity.level);
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
        entityarrow.setSilent(true);
        entityarrow.setBaseDamage(5);
        entityarrow.setKnockback(5);
        entityarrow.setCritArrow(false);
        entity.level.addFreshEntity(entityarrow);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.throw")), SoundSource.PLAYERS, 1, 1f / (new Random().nextFloat() * 0.5f + 1));
        return entityarrow;
    }
}
