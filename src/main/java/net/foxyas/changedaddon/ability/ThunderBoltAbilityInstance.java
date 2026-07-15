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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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
        Player player = (Player) entity.getEntity();
        TransfurVariant<?> variant = entity.getChangedEntity().getSelfVariant();
        return player.getFoodData().getFoodLevel() >= 10
                && (variant == ChangedAddonTransfurVariants.EXPERIMENT_009.get() || variant == ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get())
                && !Spectator(entity.getEntity()
        );
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

        entity.displayClientMessage(Component.literal("TICKS:" + ticks + " AND CHARGE:" + charge), true);
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
                lightning.tick();
                for (Entity hitEntity : lightning.getHitEntities().toList()) {
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
        // 1. Calculate the horizontal direction from the strike source to the hit entity
        double xDiff = hitEntity.getX() - sourcePos.x;
        double zDiff = hitEntity.getZ() - sourcePos.z;

        // Normalize the vector so the knockback force remains consistent regardless of distance
        double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        if (distance > 0.001) {
            xDiff /= distance;
            zDiff /= distance;

            // Scale force down to match vanilla standards (5.0 is a very strong knockback)
            float scaledForce = force * 0.2F;

            if (hitEntity instanceof LivingEntity livingEntity) {
                // Under the hood, Minecraft's knockback() subtracts the direction vector.
                // Passing (source - target) results in the target being pushed AWAY from the source.
                double xDirection = sourcePos.x - livingEntity.getX();
                double zDirection = sourcePos.z - livingEntity.getZ();

                livingEntity.knockback(scaledForce, xDirection, zDirection);

                // Apply any extra custom motion (like extra upward lift)
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
                // Fallback for non-living entities (like items, armor stands, etc.)
                Vec3 customMotion = new Vec3(xDiff * scaledForce, 0.2D, zDiff * scaledForce).add(extraMotion);
                hitEntity.setDeltaMovement(hitEntity.getDeltaMovement().add(customMotion));
            }

            // Mark the entity as dirty so the server syncs the new velocity to the clients
            hitEntity.hasImpulse = true;
        }
    }
}
