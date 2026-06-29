package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.item.api.IBestiaryItemData;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class Experiment10SpawnerItem extends AbstractSpawnerVial implements IBestiaryItemData {

    public Experiment10SpawnerItem() {
        super(ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS, ChangedAddonEntities.EXPERIMENT_10_BOSS, new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .stacksTo(4).fireResistant().rarity(Rarity.RARE));
    }

    @Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);

        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(pEntity)) {
            return;
        }

        if (pEntity instanceof LivingEntity livingEntity && livingEntity.tickCount % 100 == 0) {
            if (Arrays.stream(InteractionHand.values()).noneMatch(hand -> livingEntity.getItemInHand(hand) == pStack)) {
                return;
            }

            if (!livingEntity.hasEffect(MobEffects.WITHER)) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 60));
                livingEntity.playSound(SoundEvents.WITHER_HURT, 0.1f, 0f);
            }

        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.changed_addon.exp_10_containment_vial.desc"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemstack) {
        return true;
    }

    @Override
    public EntityType<?> getEntityTypeReference() {
        return ChangedAddonEntities.EXPERIMENT_10_BOSS.get();
    }

    @Override
    public boolean canBeHurtBy(DamageSource pSource) {
        if (pSource.is(DamageTypes.CACTUS) || pSource.is(DamageTypes.LIGHTNING_BOLT) || pSource.is(DamageTypeTags.IS_EXPLOSION)) {
            return false;
        }
        return super.canBeHurtBy(pSource);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        boolean returnValue = super.onEntityItemUpdate(stack, entity);
        entity.setGlowingTag(true);
        if (entity.lifespan == 6000) {
            entity.lifespan = 10000;
        }
        return returnValue;
    }
}
