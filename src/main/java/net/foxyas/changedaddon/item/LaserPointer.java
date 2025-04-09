package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.effect.particles.ChangedAddonParticles;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class LaserPointer extends Item {
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

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // Obtém a posição para onde o jogador está olhando
            var result = player.pick(64.0D, 0.0F, false);
            if (result.getType() == HitResult.Type.BLOCK) {
                var hitPos = result.getLocation();

                // Spawna a partícula na posição do bloco atingido
                level.addAlwaysVisibleParticle(
                        ChangedAddonParticles.laserPoint(player),
                        hitPos.x, hitPos.y, hitPos.z,
                        0.0, 0.0, 0.0
                );
            }

            player.startUsingItem(hand);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
