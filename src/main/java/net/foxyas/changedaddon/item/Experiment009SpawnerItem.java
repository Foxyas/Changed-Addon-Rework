package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.item.api.IBestiaryItemData;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Experiment009SpawnerItem extends SpecialSpawnEggItem implements IBestiaryItemData {

    public Experiment009SpawnerItem() {
        super(ChangedAddonEntities.EXPERIMENT_009_BOSS, new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .stacksTo(4).fireResistant().rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.changed_addon.exp_9_containment_vial.desc"));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemstack) {
        return true;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, @NotNull Player pPlayer, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        if (!(pPlayer.level() instanceof ServerLevel serverLevel)) {
            return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
        }
        if (pInteractionTarget.getType().is(ChangedTags.EntityTypes.HUMANOIDS) && !(pInteractionTarget instanceof Player)) {
            TransfurVariant<Experiment009BossEntity> exp9BossVariant = ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get();
            IAbstractChangedEntity changedEntity = exp9BossVariant.replaceEntity(pInteractionTarget);
            if (changedEntity != null) {
                postSpawn(serverLevel, pPlayer, changedEntity.getEntity());
            }
            return InteractionResult.sidedSuccess(pPlayer.level().isClientSide());
        }

        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }

    @Override
    protected void postSpawn(ServerLevel level, Player player, Entity spawnedEntity) {
        level.playSound(null, player, SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 1, 1);
        if (spawnedEntity instanceof Mob mob && mob.canAttack(player)) mob.setTarget(player);
    }

    @Override
    public EntityType<?> getEntityTypeReference() {
        return ChangedAddonEntities.EXPERIMENT_009_BOSS.get();
    }
}
