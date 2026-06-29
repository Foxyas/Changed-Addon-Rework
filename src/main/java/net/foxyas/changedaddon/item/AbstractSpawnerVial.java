package net.foxyas.changedaddon.item;

import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedTags;
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
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class AbstractSpawnerVial extends InteractableSpecialSpawnEggItem {

    public AbstractSpawnerVial(Supplier<? extends TransfurVariant<?>> variant, Supplier<? extends EntityType<?>> entityType, Properties properties) {
        super(variant, entityType, properties);
    }

    @Override
    protected boolean shouldSpawnInLivingInteraction() {
        return true;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        if (shouldSpawnInLivingInteraction()) {
            if (pInteractionTarget.getType().is(ChangedTags.EntityTypes.HUMANOIDS) && !(pInteractionTarget instanceof Player)) {
                if (!player.isCrouching()) {
                    player.displayClientMessage(Component.translatable("text.changed_addon.confirm_vial_use"), true);
                    return InteractionResult.CONSUME_PARTIAL;
                }

                if (!(player.level() instanceof ServerLevel level)) return InteractionResult.SUCCESS;

                TransfurVariant<?> variant = transfurVariant.get();
                IAbstractChangedEntity changedEntity = variant.replaceEntity(pInteractionTarget);
                if (changedEntity != null) {
                    if (!player.isCreative() && !player.isSpectator()) stack.shrink(1);

                    postSpawn(level, player, changedEntity.getEntity());
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, pInteractionTarget.position());
                }
                return InteractionResult.SUCCESS;
            }
        }

        return onFailInteractLivingEntity(stack, player, pInteractionTarget, pUsedHand);
    }

    @Override
    protected void postSpawn(ServerLevel level, Player player, Entity spawnedEntity) {
        level.playSound(null, player, SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 1, 1);
        if (spawnedEntity instanceof Mob mob && mob.canAttack(player)) mob.setTarget(player);
    }

    @Override
    protected InteractionResult onFailUseOn(UseOnContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(Component.translatable("text.changed_addon.item.vial.wrong_use.hint"), true);
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
