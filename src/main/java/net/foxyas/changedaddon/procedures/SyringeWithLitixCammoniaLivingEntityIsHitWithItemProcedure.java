package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.Random;

public class SyringeWithLitixCammoniaLivingEntityIsHitWithItemProcedure {

    public static void execute(Entity entity, Entity sourceEntity, ItemStack itemStack) {
        if (!(entity instanceof Player player) || sourceEntity == null) return;

        boolean isTransfurred = ProcessTransfur.isPlayerTransfurred(player);
        boolean isOrganic = ProcessTransfur.isPlayerNotLatex(player);
        boolean isLatex = entity.getType().is(ChangedTags.EntityTypes.LATEX);

        // Aplicar efeito de Untransfur se aplicável
        if (isTransfurred) {
            int duration = isOrganic ? 640 : 400;
            applyUntransfurEffect(player, duration);

            if (isOrganic && shouldShowWarn(player)) {
                player.displayClientMessage(new TextComponent("For some reason, this seems to have a slowed effect"), true);
            }
        } else if (isLatex) {
            applyUntransfurEffect(player, 400);
        }

        boolean isCreative = isCreativeMode(sourceEntity);

        // Lógica de dano e som
        playUntransfurSound(sourceEntity);

        if (!isCreative) {
            if (itemStack.getDamageValue() == itemStack.getMaxDamage() - 1) {
                giveSyringeBack(sourceEntity);
                removeItem(sourceEntity, itemStack);
            } else {
                damageItem(itemStack);
            }
        }
    }

    private static void applyUntransfurEffect(Player player, int duration) {
        if (!player.level.isClientSide()) {
            player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.UNTRANSFUR.get(), duration, 0, false, false));
        }
    }

    private static boolean shouldShowWarn(Player player) {
        return player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null)
                .orElse(new ChangedAddonModVariables.PlayerVariables()).showwarns;
    }

    private static boolean isCreativeMode(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            return serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
        } else if (entity instanceof Player player && entity.level.isClientSide()) {
            var info = Objects.requireNonNull(Minecraft.getInstance().getConnection()).getPlayerInfo(player.getGameProfile().getId());
            return info != null && info.getGameMode() == GameType.CREATIVE;
        }
        return false;
    }

    private static void giveSyringeBack(Entity entity) {
        if (entity instanceof Player player) {
            ItemStack syringe = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:syringe")));
            syringe.setCount(1);
            ItemHandlerHelper.giveItemToPlayer(player, syringe);
        }
    }

    private static void removeItem(Entity entity, ItemStack stack) {
        if (entity instanceof Player player) {
            player.getInventory().clearOrCountMatchingItems(p -> p.getItem() == stack.getItem(), 1, player.inventoryMenu.getCraftSlots());
        }
    }

    private static void damageItem(ItemStack itemStack) {
        if (itemStack.hurt(1, new Random(), null)) {
            itemStack.shrink(1);
            itemStack.setDamageValue(0);
        }
    }

    private static void playUntransfurSound(Entity entity) {
        if (!entity.level.isClientSide() && entity.getServer() != null) {
            entity.getLevel().playSound(null, entity.position().x, entity.position().y, entity.position().z, ChangedSounds.SWORD1, SoundSource.PLAYERS, 1, 1);
        }
    }
}
