package net.foxyas.changedaddon.entity.projectile;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class LuminarCrystalSpearProjectile extends AbstractArrow implements ItemSupplier {

    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(LuminarCrystalSpearProjectile.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(LuminarCrystalSpearProjectile.class, EntityDataSerializers.BOOLEAN);
    public int clientSideReturnSpearTickCount;
    private ItemStack spearItem = new ItemStack(ChangedAddonItems.LUMINAR_CRYSTAL_SPEAR.get());
    private boolean dealtDamage;

    public LuminarCrystalSpearProjectile(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        super(ChangedAddonEntities.LUMINAR_CRYSTAL_SPEAR.get(), world);
    }

    public LuminarCrystalSpearProjectile(Level level, LivingEntity shooter, ItemStack weapon) {
        super(ChangedAddonEntities.LUMINAR_CRYSTAL_SPEAR.get(), shooter, level);
        this.spearItem = weapon.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(weapon));
        this.entityData.set(ID_FOIL, weapon.hasFoil());
    }


    public LuminarCrystalSpearProjectile(EntityType<? extends LuminarCrystalSpearProjectile> type, Level world) {
        super(type, world);
    }

    public LuminarCrystalSpearProjectile(EntityType<? extends LuminarCrystalSpearProjectile> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public LuminarCrystalSpearProjectile(EntityType<? extends LuminarCrystalSpearProjectile> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte) 0);
        this.entityData.define(ID_FOIL, false);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull ItemStack getItem() {
        return this.spearItem;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return this.spearItem;
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

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        if (this.level() instanceof ServerLevel serverLevel) {
            BlockPos hitPos = result.getBlockPos();
            BlockState hitState = serverLevel.getBlockState(hitPos);

            if (hitState.isAir()) {
                return;
            }

            // Direção da face da parede atingida (ex: NORTH, SOUTH, EAST, WEST, UP, DOWN)
            Direction faceDirection = result.getDirection();

            // Objeto dummy de explosão para checar a resistência dos blocos
            Explosion explosion = new Explosion(serverLevel, this, this.position().x(), this.position().y(), this.position().z(), 3f, false, Explosion.BlockInteraction.DESTROY);

            // Raio de espalhamento na parede baseado no encantamento Sharpness
            int radius = 1 + Math.max(0, (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SHARPNESS, this.spearItem) / 3));

            // Obtém o estado padrão do pequeno cristal
            BlockState crystalState = ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState();

            // Varre a área plana da parede ao redor do ponto atingido
            for (BlockPos wallPos : FoxyasUtil.betweenClosedStreamSphere(hitPos, radius, radius, 1.25f).toList()) {
                BlockState state = serverLevel.getBlockState(wallPos);

                // Onde o cristal vai ser colado (o bloco de ar diretamente na frente do bloco da parede)
                BlockPos targetAirPos = wallPos.relative(faceDirection);
                BlockState airState = serverLevel.getBlockState(targetAirPos);

                // Verifica se o bloco da parede não é ar, se pode ser minerado com nível pedra e se o espaço na frente está livre
                if (!state.isAir()
                        && airState.isAir()
                        && TierSortingRegistry.isCorrectTierForDrops(Tiers.STONE, state)
                        && state.getExplosionResistance(serverLevel, hitPos, explosion) < 1) {

                    // Se o seu cristal tiver propriedade de direção/facing, tentamos ajustar para ele grudar na parede
                    BlockState finalCrystalState = crystalState;
                    if (crystalState.hasProperty(DirectionalBlock.FACING)) {
                        finalCrystalState = crystalState.setValue(DirectionalBlock.FACING, faceDirection);
                    } else if (crystalState.hasProperty(HorizontalDirectionalBlock.FACING) && faceDirection.getAxis().isHorizontal()) {
                        finalCrystalState = crystalState.setValue(HorizontalDirectionalBlock.FACING, faceDirection);
                    }

                    // Posiciona o pequeno cristal no espaço de ar adjacente à parede
                    serverLevel.setBlockAndUpdate(targetAirPos, finalCrystalState);

                    serverLevel.playSound(null, targetAirPos,
                            finalCrystalState.getSoundType().getPlaceSound(),
                            SoundSource.BLOCKS, 1.0f, 1.2f);
                }
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37573_) {
        Entity entity = p_37573_.getEntity();
        float f = 12.0F;
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.spearItem, livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.level().damageSources().trident(this, entity1 == null ? this : entity1);
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity livingentity1) {
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
                assert lightningbolt != null;
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
        return EnchantmentHelper.hasChanneling(this.spearItem);
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
            this.spearItem = ItemStack.of(p_37578_.getCompound("CrystalSpear"));
        }

        this.dealtDamage = p_37578_.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.spearItem));
    }

    public void addAdditionalSaveData(@NotNull CompoundTag p_37582_) {
        super.addAdditionalSaveData(p_37582_);
        p_37582_.put("CrystalSpear", this.spearItem.save(new CompoundTag()));
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
}
