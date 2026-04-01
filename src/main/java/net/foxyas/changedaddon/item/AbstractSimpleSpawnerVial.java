package net.foxyas.changedaddon.item;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class AbstractSimpleSpawnerVial extends InteractableSpecialSpawnEggItem{
    public AbstractSimpleSpawnerVial(Supplier<? extends TransfurVariant<?>> variant, Supplier<? extends EntityType<?>> entityType, Properties properties) {
        super(variant, entityType, properties);
    }

    @Override
    protected boolean shouldSpawnInUseOn() {
        return super.shouldSpawnInUseOn();
    }

    @Override
    protected boolean shouldSpawnInLivingInteraction() {
        return true;
    }

    @Override
    protected void postSpawn(ServerLevel level, Player player, Entity spawnedEntity) {
        level.playSound(null, player, SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 1, 1);
        if (spawnedEntity instanceof Mob mob && mob.canAttack(player)) mob.setTarget(player);
    }

    @Override
    protected InteractionResult onFailUseOn(UseOnContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(Component.translatable("text.changed_addon.item.vial.wrong_use"), true);
        }


        return super.onFailUseOn(context);
    }

    @Override
    protected InteractionResult onFailInteractLivingEntity(@NotNull ItemStack pStack, @NotNull Player pPlayer, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        if (pPlayer instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(Component.translatable("text.changed_addon.item.vial.wrong_use"), true);
        }

        return super.onFailInteractLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }
}
