package net.foxyas.changedaddon.item;

import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class InteractableSpecialSpawnEggItem extends SpecialSpawnEggItem {

    private final Supplier<? extends TransfurVariant<?>> transfurVariant;

    public InteractableSpecialSpawnEggItem(Supplier<? extends TransfurVariant<?>> variant, Supplier<? extends EntityType<?>> entityType, Properties properties) {
        super(entityType, properties);
        this.transfurVariant = variant;
    }

    protected boolean shouldSpawnInUseOn() {
        return false;
    }

    protected boolean shouldSpawnInLivingInteraction() {
        return false;
    }

    protected InteractionResult onFailInteractLivingEntity(@NotNull ItemStack pStack, @NotNull Player pPlayer, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        return InteractionResult.PASS;
    }

    protected InteractionResult onFailUseOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        return shouldSpawnInUseOn() ? super.useOn(context) : onFailUseOn(context);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, @NotNull Player pPlayer, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        if (shouldSpawnInLivingInteraction()) {
            if (pInteractionTarget.getType().is(ChangedTags.EntityTypes.HUMANOIDS) && !(pInteractionTarget instanceof Player)) {
                TransfurVariant<?> variant = transfurVariant.get();
                IAbstractChangedEntity changedEntity = variant.replaceEntity(pInteractionTarget);
                if (changedEntity != null && pPlayer.level() instanceof ServerLevel serverLevel) {
                    postSpawn(serverLevel, pPlayer, changedEntity.getEntity());
                }
                return InteractionResult.sidedSuccess(pPlayer.level().isClientSide());
            }
        }

        return onFailInteractLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }
}
