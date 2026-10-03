package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.item.api.IDynamicCreativeTab;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.item.SpecializedAnimations;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.foxyas.changedaddon.item.LaethinItem.getLaethinTypeOfStack;
import static net.foxyas.changedaddon.item.LaethinItem.setLaethinTypeForStack;

public class LaethinSyringeItem extends AbstractSyringeItem implements SpecializedAnimations, IDynamicCreativeTab {

    public LaethinSyringeItem() {
        super(new Item.Properties()
                .stacksTo(64)
                .rarity(Rarity.RARE)
        );
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack defaultInstance = super.getDefaultInstance();
        setLaethinTypeForStack(defaultInstance, LaethinItem.Type.WHITE_LATEX);
        return defaultInstance;
    }

    @Override
    public void fillItemCategory(CreativeModeTab.@NotNull Output tab) {
        for (LaethinItem.Type type : LaethinItem.Type.values()) {
            ItemStack stack = new ItemStack(this);
            setLaethinTypeForStack(stack, type);
            tab.accept(stack);
        }
    }

    @Override
    public void applyEffectsAfterUse(@NotNull ItemStack pStack, Level level, LivingEntity entity) {
        super.applyEffectsAfterUse(pStack, level, entity);

        if (!(entity instanceof Player player)) return;

        var playerVars = ChangedAddonVariables.ofOrDefault(player);

        if (!ProcessTransfur.isPlayerTransfurred(player)) {
            if (playerVars.showWarns && !player.level.isClientSide())
                player.displayClientMessage(Component.translatable("changed_addon.untransfur.no_effect"), true);
            return;
        }

        if (ProcessTransfur.isPlayerNotLatex(player)) {
            applyMobEffect(player, ChangedAddonMobEffects.UNTRANSFUR.get(), 1000);
            if (playerVars.showWarns && !player.level.isClientSide())
                player.displayClientMessage(Component.translatable("changed_addon.untransfur.slow_effect"), true);
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

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        LaethinItem.Type laethinTypeOfStack = getLaethinTypeOfStack(pStack);
        pTooltipComponents.add(laethinTypeOfStack.getFormatedName());

    }

    protected void applyMobEffect(Player entity, MobEffect effect, int duration) {
        entity.addEffect(new MobEffectInstance(effect, duration, 0, false, false));
    }
}
