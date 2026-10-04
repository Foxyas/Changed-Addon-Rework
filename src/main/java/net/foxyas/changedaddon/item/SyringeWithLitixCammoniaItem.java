package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonDamageSources;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class SyringeWithLitixCammoniaItem extends AbstractSyringeItem {

    public SyringeWithLitixCammoniaItem() {
        super(new Item.Properties()
                .durability(2).rarity(Rarity.UNCOMMON));
    }

    private static void handleUntransfurSuccess(Player player, ItemStack pStack) {
        ChangedAddonVariables.PlayerVariables playerVars = getVars(player);
        if (ProcessTransfur.isPlayerNotLatex(player)) {
            if (!player.level.isClientSide()) {
                player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.UNTRANSFUR.get(), 1000, 0, false, false));
            }
            if (playerVars.showWarns) {
                sendMessage(player, "changed_addon.untransfur.slow_effect");
            }
            return;
        }

        // Visual feedback
        if (PlayerUtil.unTransfurPlayerAndSpawnParticles(player, true, true)) {
            // Optional: Reset advancement
            if (playerVars.resetTransfurAdvancements && player instanceof ServerPlayer sp) {
                resetAdvancement(sp, "minecraft:changed/transfur");
            }

            // Grant untransfur advancement if not already
            if (player instanceof ServerPlayer serverPlayer) {
                grantAdvancement(serverPlayer, pStack);
            }
        }
    }

    private static ChangedAddonVariables.PlayerVariables getVars(Player entity) {
        return ChangedAddonVariables.ofOrDefault(entity);
    }

    private static void applyUntransfurEffect(Player player, int duration) {
        if (!player.level.isClientSide()) {
            player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.UNTRANSFUR.get(), duration, 0, false, false));
        }
    }

    private static void giveSyringeBack(Entity entity) {
        if (entity instanceof Player player) {
            ItemStack syringe = new ItemStack(ChangedItems.SYRINGE.get());
            syringe.setCount(1);
            ItemHandlerHelper.giveItemToPlayer(player, syringe);
        }
    }

    private static void damageItem(ItemStack itemStack) {
        if (itemStack.hurt(1, RandomSource.create(), null)) {
            itemStack.shrink(1);
            itemStack.setDamageValue(0);
        }
    }

    @Override
    public void applyEffectsAfterUse(@NotNull ItemStack pStack, Level level, LivingEntity entity) {
        super.applyEffectsAfterUse(pStack, level, entity);

        if (!(entity instanceof ServerPlayer player)) return;

        if (player.getStats().getValue(Stats.ITEM_USED.get(this)) >= 132) {
            player.hurt(ChangedAddonDamageSources.OVER_DOSE.source(level), 15f);
        }
        if (ProcessTransfur.isPlayerTransfurred(player)) {
            if (player.getRandom().nextFloat() >= 0.35) {
                handleUntransfurSuccess(player, pStack);
            } else {
                player.hurt(ChangedAddonDamageSources.UNTRANSFUR_FAIL.source(level), 15f);
                sendMessage(player, "changed_addon.untransfur.fail");
            }
            return;
        }

        if (getVars(player).showWarns) sendMessage(player, "changed_addon.untransfur.no_effect");
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity entity, @NotNull LivingEntity sourceEntity) {
        if (!(entity instanceof Player player)) return false;

        // Aplicar efeito de Untransfur se aplicável
        if (ProcessTransfur.isPlayerTransfurred(player)) {
            boolean isOrganic = ProcessTransfur.isPlayerNotLatex(player);

            int duration = isOrganic ? 640 : 400;
            applyUntransfurEffect(player, duration);

            if (isOrganic && ChangedAddonVariables.ofOrDefault(player).showWarns) {
                player.displayClientMessage(Component.literal("For some reason, this seems to have a slowed effect"), true);
            }
        } else if (entity.getType().is(ChangedTags.EntityTypes.LATEX)) {
            applyUntransfurEffect(player, 400);
        }

        // Lógica de dano e som
        sourceEntity.level.playSound(null, sourceEntity, ChangedSounds.SYRINGE_PRICK.get(), SoundSource.PLAYERS, 1, 1);

        if (!(sourceEntity instanceof Player player1) || !player1.isCreative()) {
            if (itemStack.getDamageValue() == itemStack.getMaxDamage() - 1) {
                giveSyringeBack(sourceEntity);
                itemStack.shrink(1);
            } else {
                damageItem(itemStack);
            }
        }

        return false;
    }
}
