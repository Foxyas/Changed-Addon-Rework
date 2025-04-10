package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.effect.particles.ChangedAddonParticles;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.item.SpecializedAnimations;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class LaserPointer extends Item implements SpecializedAnimations {
    public LaserPointer() {
        super(new Properties().stacksTo(1).tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getOrCreateTag().putInt("Color", new Color3(140, 0, 0).toInt()); // Cor padrão vermelha
        return stack;
    }

    public static int getColor(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("Color")) {
            return stack.getTag().getInt("Color");
        }
        return new Color3(140, 0, 0).toInt(); // Cor padrão se não tiver NBT
    }

    public static Color3 getColorAsColor3(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("Color")) {
            return Color3.fromInt(stack.getTag().getInt("Color"));
        }
        return new Color3(140, 0, 0); // Cor padrão se não tiver NBT
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 720000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // Obtém a posição para onde o jogador está olhando
            var result = player.pick(64.0D, 0.0F, false);
            if (result.getType() == HitResult.Type.BLOCK) {
                var hitPos = result.getLocation();

                // Spawna a partícula na posição do bloco atingido
                PlayerUtilProcedure.ParticlesUtil.sendParticles(level,
                        ChangedAddonParticles.laserPoint(player, LaserPointer.getColorAsColor3(stack), 0.5f),
                        hitPos.x, hitPos.y, hitPos.z,
                        0.0, 0.0, 0.0,1,0
                );
            }

            player.startUsingItem(hand);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Nullable
    @Override
    public AnimationHandler getAnimationHandler() {
        return ANIMATION_CACHE.computeIfAbsent(this, LaserPointer.Animator::new);
    }

    public static class Animator extends AnimationHandler {
        public Animator(Item item) {
            super(item);
        }

        @Override
        public void setupUsingAnimation(ItemStack itemStack, EntityStateContext entity, UpperModelContext model, HumanoidArm arm, float progress) {
            super.setupUsingAnimation(itemStack, entity, model, arm, progress);

            // Sets the correct arm depending on the hand used
            // HumanoidArm ContextArm = entity.livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? arm : arm.getOpposite();

            // Sets the arm rotation based on the player's head
            model.getArm(arm).xRot = model.head.xRot - 1.570796f - (entity.livingEntity.isCrouching() ? 0.2617994F : 0.0F);
            model.getArm(arm).yRot = model.head.yRot;

            // Silly animation [Intentionally not smooth due to lack of partial ticks and design]
        }
    }
}
