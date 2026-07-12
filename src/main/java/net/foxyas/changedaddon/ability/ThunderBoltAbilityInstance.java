package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.IScalableLightingBolt;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbility.UseType;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
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

    @Override
    public UseType getUseType() {
        return UseType.CHARGE_RELEASE;
    }

    @Override
    public boolean canKeepUsing() {
        return canUse();
    }

    @Override
    public void startUsing() {
        float maxReach = getReachOfThunder();
        summonLightBolt(entity, maxReach * this.charge);
    }

    @Override
    public void tick() {

    }

    @Override
    public void stopUsing() {

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

    public void summonLightBolt(IAbstractChangedEntity iAbstractChangedEntity, float reach) {
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
            if (lightning instanceof IScalableLightingBolt lightingBolt) {
                lightingBolt.setScale(getThunderSize());
            }
            serverLevel.addFreshEntity(lightning);
            if (entity instanceof Player player) {
                player.causeFoodExhaustion(0.5f);
            }
            entity.swing(getSwingHand(entity), true);
        }
    }

}
