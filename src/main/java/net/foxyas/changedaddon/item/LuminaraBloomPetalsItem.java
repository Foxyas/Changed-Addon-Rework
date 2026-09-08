package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LuminaraBloomPetalsItem extends Item {

    public LuminaraBloomPetalsItem() {
        super(new Properties()

                .rarity(Rarity.UNCOMMON)
                .food(new FoodProperties.Builder()
                        .saturationMod(1)
                        .nutrition(1)
                        .fast()
                        .effect(() -> new MobEffectInstance(ChangedAddonMobEffects.PACIFIED.get(), 60 * 20), 1.0F)
                        .alwaysEat()
                        .build()));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Sneaking → normal eating behavior
        if (player.isShiftKeyDown()) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }

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

        if (!(entityHitResult.getEntity() instanceof LivingEntity target))
            return InteractionResult.PASS;

        // If player is sneaking, just pass — allows normal eating behavior
        if (player.isShiftKeyDown())
            return InteractionResult.PASS;

        // Check if target is a ChangedEntity or another player
        boolean validTarget = target instanceof ChangedEntity || target instanceof Player;

        if (validTarget && !target.hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
            Vec3 hitResultLocation = entityHitResult.getLocation();
            Vec3 targetMouth = target.getEyePosition(); // approximate mouth position

            // Distance check — must be close to the target's mouth
            double distance = hitResultLocation.distanceTo(targetMouth);
            if (distance > 0.5D)
                return InteractionResult.PASS; // too far away

            // Direction check — player must be in front of the target
            Vec3 targetLook = target.getViewVector(0).normalize(); // entity's facing direction
            Vec3 fromHitToMouth = targetMouth.subtract(hitResultLocation).scale(-1).normalize();

            // Dot product > 0 means in front, < 0 means behind
            double facingDot = targetLook.dot(fromHitToMouth);

            // Require the player to be at least somewhat in front (45° cone)
            if (facingDot < 0.8D)
                return InteractionResult.PASS; // player is behind or to the side

            // Apply pacified effect for 60 seconds
            target.addEffect(new MobEffectInstance(ChangedAddonMobEffects.PACIFIED.get(), 60 * 20));

            // Play eating sound for feedback
            target.level().playSound(null, target.blockPosition(),
                    SoundEvents.GENERIC_EAT, target.getSoundSource(), 1.0F, 1.0F);

            // Consume one item if player is not in creative mode
            if (!player.isCreative())
                stack.shrink(1);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
