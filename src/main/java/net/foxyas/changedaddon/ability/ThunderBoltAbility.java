package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ThunderBoltAbility extends SimpleAbility {


    public static boolean Spectator(Entity entity) {
        if (entity instanceof Player player1) {
            return player1.isSpectator();
        }
        return true;
    }

    private static boolean isHandEmpty(Entity entity, InteractionHand hand) {
        return entity instanceof LivingEntity livingEntity && livingEntity.getItemInHand(hand).getItem() == Blocks.AIR.asItem();
    }

    private static InteractionHand getSwingHand(Entity entity) {
        return isHandEmpty(entity, InteractionHand.MAIN_HAND) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public static void SummonLightBolt(Entity entity, LevelAccessor world, float amount) {
        Player player = (Player) entity;
        double range = Math.max(amount, 1.5);
        BlockHitResult clip = entity.level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(range)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
        Vec3 location = clip.getLocation();
        if (world instanceof ServerLevel _level) {
            LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
            assert entityToSpawn != null;
            entityToSpawn.moveTo(location);
            entityToSpawn.setVisualOnly(false);
            _level.addFreshEntity(entityToSpawn);
            player.causeFoodExhaustion((float) 0.5);
            player.swing(getSwingHand(player), true);
        }
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.thunder");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/thunderbolt.png");
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        Player player = (Player) entity.getEntity();
        TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
        return player.getFoodData().getFoodLevel() >= 10 && (Variant == ChangedAddonTransfurVariants.EXPERIMENT_009.get() || Variant == ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get()) && !Spectator(entity.getEntity());
    }

    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.CHARGE_RELEASE;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
        if (Variant == ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get()) {
            return 15;
        }
        return 20;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
        if (Variant == ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get()) {
            return 15;
        }
        return 25;
    }

    public float ReachAmount(IAbstractChangedEntity entity) {
        TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
        if (Variant == ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get()) {
            return 10;
        }
        if (Variant == ChangedAddonTransfurVariants.EXPERIMENT_009.get()) {
            return 5;
        }
        return 3.5F;
    }

    @Override
    public void tickCharge(IAbstractChangedEntity entity, float ticks) {
        super.tickCharge(entity, ticks);
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
        SummonLightBolt(entity.getEntity(), entity.getLevel(), ReachAmount(entity));
    }
}
