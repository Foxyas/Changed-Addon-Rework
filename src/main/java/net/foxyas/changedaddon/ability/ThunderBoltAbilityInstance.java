package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.IDynamicThunderBolt;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbility.UseType;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ThunderBoltAbilityInstance extends AbstractAbilityInstance {

    public float charge = 0f;

    public ThunderBoltAbilityInstance(AbstractAbility<ThunderBoltAbilityInstance> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    @Override
    public Component getAbilityName() {
        return Component.translatable("ability.changed_addon.thunder");
    }

    public ResourceLocation getTexture() {
        return ResourceLocation.parse("changed_addon:textures/screens/thunderbolt.png");
    }

    @Override
    public boolean canUse() {
        return entity.getFoodLevel() >= 10 && !entity.getEntity().isSpectator();
    }

    public float getCharge() {
        return charge;
    }

    @Override
    public UseType getUseType() {
        return UseType.HOLD;
    }

    @Override
    public boolean canKeepUsing() {
        return canUse();
    }

    @Override
    public void startUsing() {
    }

    @Override
    public void tick() {
        AbstractAbility.Controller controller = this.getController();
        int holdTicks = controller.getHoldTicks();
        chargeAbility(entity, holdTicks);
    }

    @Override
    public void tickIdle() {
        super.tickIdle();
        this.charge = Mth.clamp(charge, 0, 2);
    }

    @Override
    public void stopUsing() {
        if (this.charge < 0.25f) {
            return;
        }

        float maxReach = getReachOfThunder();
        float maxed = this.charge;
        summonLightBolt(entity, maxReach * maxed, getThunderSize() * maxed);
    }

    @Override
    public void saveData(CompoundTag tag) {
        super.saveData(tag);
        tag.putFloat("charge", charge);
    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);
        charge = tag.getFloat("charge");
    }

    protected void chargeAbility(IAbstractChangedEntity entity, int ticks) {
        LivingEntity livingEntity = entity.getEntity();
        Level level = livingEntity.level();
        int chargeTime = ability.getChargeTime(entity);
        if (chargeTime != 0) {
            if (ticks <= chargeTime) {
                if (ticks == chargeTime) {
                    level.playSound(null, livingEntity, SoundEvents.WARDEN_SONIC_CHARGE, SoundSource.PLAYERS, 1, (float) ticks / chargeTime);
                } else {
                    level.playSound(null, livingEntity, SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 1, (float) ticks / chargeTime);
                }
            }
            charge = Mth.clamp((float) ticks / chargeTime, 0.25f, 2f);
        }
    }

    public float getReachOfThunder() {
        ChangedEntity changedEntity = entity.getChangedEntity();
        float alphaMultiplier = changedEntity instanceof IAlphaAbleEntity iAlphaAbleEntity ? iAlphaAbleEntity.alphaScalePercent() : 1f;
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        float size = 3.5f;
        if (variant == ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get()) {
            size = 50;
        } else if (variant == ChangedAddonTransfurVariants.EXPERIMENT_009.get()) {
            size = 25f;
        }
        return size * alphaMultiplier;
    }

    public float getThunderSize() {
        ChangedEntity changedEntity = entity.getChangedEntity();
        float alphaMultiplier = changedEntity instanceof IAlphaAbleEntity iAlphaAbleEntity ? iAlphaAbleEntity.alphaScalePercent() : 1f;
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        float size = 1f;
        if (variant == ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get()) {
            size = 5;
        } else if (variant == ChangedAddonTransfurVariants.EXPERIMENT_009.get()) {
            size = 2.5f;
        }
        return size * alphaMultiplier;
    }


    public boolean Spectator(Entity entity) {
        if (entity instanceof Player player) {
            return player.isSpectator();
        }
        return true;
    }

    private boolean isHandEmpty(Entity entity, InteractionHand hand) {
        return entity instanceof LivingEntity livingEntity && livingEntity.getItemInHand(hand).isEmpty();
    }

    private InteractionHand getSwingHand(Entity entity) {
        return isHandEmpty(entity, InteractionHand.MAIN_HAND) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public void summonLightBolt(IAbstractChangedEntity iAbstractChangedEntity, float reach, float size) {
        LivingEntity entity = iAbstractChangedEntity.getEntity();
        double range = Math.max(reach, 1.5);
        ClipContext clipContext = new ClipContext(entity.getEyePosition(1f),
                entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(range)),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                entity);
        HitResult clip = entity.level.clip(clipContext);
        EntityHitResult entityHitResult = PlayerUtil.getEntityHitLookingAt(entity, reach, ClipContext.Block.COLLIDER);
        if (entityHitResult != null && entityHitResult.getType() != HitResult.Type.MISS) {
            clip = entityHitResult;
        }
        Vec3 location = clip.getLocation();
        if (entity.level() instanceof ServerLevel serverLevel) {
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
            assert lightning != null;
            lightning.moveTo(location);
            lightning.setVisualOnly(false);
            if (lightning instanceof IDynamicThunderBolt lightingBolt) {
                lightingBolt.setScale(size);
                ChangedEntity changedEntity = iAbstractChangedEntity.getChangedEntity();
                Color3 transfurColor = changedEntity.getTransfurColor(TransfurCause.DEFAULT);
                lightingBolt.setThunderColor(transfurColor);
            }

            if (serverLevel.addFreshEntity(lightning)) {
                // 1. Determine the target center block position where the lightning landed
                BlockPos centerHitPos = BlockPos.containing(location);
                BlockState centerState = serverLevel.getBlockState(centerHitPos);

                if (centerState.isAir() || (centerState.canBeReplaced() && !centerState.is(BlockTags.SAND))) {
                    centerHitPos = centerHitPos.below();
                }

                // 2. Area-of-effect Sand-to-Glass conversion loop (radius based on thunder size)
                int radius = Math.max(1, (int) (size * 0.75f)); // Scales the glass-melting radius with the lightning size
                boolean playedGlassSound = false;

                for (BlockPos blockpos : BlockPos.betweenClosed(
                        centerHitPos.offset(-radius, -1, -radius),
                        centerHitPos.offset(radius, 1, radius))) {

                    if (!isInsideSphere(centerHitPos, Vec3.atCenterOf(blockpos), radius)) continue;

                    BlockState currentState = serverLevel.getBlockState(blockpos);

                    // Check if the block is sand
                    if (currentState.is(BlockTags.SAND)) {
                        serverLevel.setBlock(blockpos, Blocks.GLASS.defaultBlockState(), 3);

                        // Play sound once or twice across the area so it doesn't spam sounds too loudly
                        if (!playedGlassSound || serverLevel.random.nextFloat() < 0.3f) {
                            serverLevel.playSound(null, blockpos, SoundEvents.GLASS_BREAK, entity.getSoundSource(), 3.0f, 1.5f + serverLevel.random.nextFloat() * 0.5f);
                            playedGlassSound = true;
                        }
                    }
                }

                // 3. Apply knockback to nearby entities within the strike zone
                double aoeRadius = 3.0 + size;
                List<LivingEntity> hitEntities = serverLevel.getEntitiesOfClass(
                        LivingEntity.class,
                        new AABB(location, location).inflate(aoeRadius),
                        EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(e -> !e.is(entity))
                );

                for (Entity hitEntity : hitEntities) {
                    applyKnockBackToHitEntity(hitEntity, location, 5, Vec3.ZERO);
                }

                if (entity instanceof Player player) {
                    player.causeFoodExhaustion(0.5f);
                }
                entity.swing(getSwingHand(entity), true);

                this.getController().forceCooldown(ability.getCoolDown(iAbstractChangedEntity));
                this.charge = 0;
                this.ability.setDirty(iAbstractChangedEntity);
            }
        }
    }

    protected void applyKnockBackToHitEntity(Entity hitEntity, Vec3 sourcePos, float force, Vec3 extraMotion) {
        double xDiff = hitEntity.getX() - sourcePos.x;
        double zDiff = hitEntity.getZ() - sourcePos.z;

        double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        if (distance > 0.001) {
            xDiff /= distance;
            zDiff /= distance;

            float scaledForce = force * 0.2F;

            if (hitEntity instanceof LivingEntity livingEntity) {
                double xDirection = sourcePos.x - livingEntity.getX();
                double zDirection = sourcePos.z - livingEntity.getZ();

                livingEntity.knockback(scaledForce, xDirection, zDirection);

                if (extraMotion != Vec3.ZERO) {
                    livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(extraMotion));
                }

                if (livingEntity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(
                            serverPlayer.getId(),
                            serverPlayer.getDeltaMovement())
                    );
                }
            } else {
                Vec3 customMotion = new Vec3(xDiff * scaledForce, 0.2D, zDiff * scaledForce).add(extraMotion);
                hitEntity.setDeltaMovement(hitEntity.getDeltaMovement().add(customMotion));
            }

            hitEntity.hasImpulse = true;
        }
    }

    /**
     * Simple check: is the point inside the sphere defined by `center` and `radius`?
     */
    private static boolean isInsideSphere(BlockPos center, Vec3 point, float radius) {
        return center.distToCenterSqr(point) <= radius * radius;
    }
}