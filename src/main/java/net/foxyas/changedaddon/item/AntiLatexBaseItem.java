package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.mixins.entity.MobAccessor;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.PathFinderMobDataExtension;
import net.ltxprogrammer.changed.network.packet.AssimilatedEntitySyncPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class AntiLatexBaseItem extends ItemWithDescription {

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Perform a ray trace to detect what the player is looking at
        HitResult hit = PlayerUtil.getEntityHitLookingAt(player, (float) player.getEntityReach(), null);

        if (hit != null && hit.getType() == HitResult.Type.ENTITY && hit instanceof EntityHitResult entityHitResult) {
            // If the hit is an entity, try to interact specifically with it
            InteractionResult result = interactLivingEntitySpecific(stack, player, entityHitResult, hand);
            if (result.consumesAction()) {
                return InteractionResultHolder.success(stack);
            } else {
                return super.use(level, player, hand);
            }
        } else if (hit == null) {
            return super.use(level, player, hand);
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }

    public @NotNull InteractionResult interactLivingEntitySpecific(@NotNull ItemStack stack, @NotNull Player player,
                                                                   @NotNull EntityHitResult entityHitResult, @NotNull InteractionHand hand) {
        if (!(entityHitResult.getEntity() instanceof LivingEntity target)) return InteractionResult.PASS;

        if (!(target instanceof PathfinderMob pathfinderMob)) return InteractionResult.PASS;
        if (!(pathfinderMob instanceof PathFinderMobDataExtension pathFinderMobDataExtension)) return InteractionResult.PASS;
        Level level = player.level();
        if (pathFinderMobDataExtension.isLatexAssimilated() && !level.isClientSide()) {
            pathFinderMobDataExtension.markAsLatexAssimilated(false);
            Changed.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> target), new AssimilatedEntitySyncPacket(target.getId(), false));
            LivingEntity lastHurtByMob = pathfinderMob.getLastHurtByMob();

            if (lastHurtByMob != pathfinderMob.getTarget()) pathfinderMob.setTarget(null);

            // Play eating sound for feedback
            target.level().playSound(null, target.blockPosition(),
                    SoundEvents.GENERIC_DRINK, target.getSoundSource(), 1.0F, 1.0F);

            // Consume one item if player is not in creative mode
            if (!player.isCreative())
                stack.shrink(1);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
