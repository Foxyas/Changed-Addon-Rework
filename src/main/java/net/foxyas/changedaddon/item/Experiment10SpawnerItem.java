package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.item.api.IBestiaryItemData;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
}
