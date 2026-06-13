package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.item.api.IDynamicCreativeTab;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.procedure.SummonDripParticlesProcedure;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.item.SpecializedAnimations;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static net.foxyas.changedaddon.item.LaethinItem.setLaethinTypeForStack;

public class LaethinSyringeItem extends AbstractSyringeItem implements SpecializedAnimations, IDynamicCreativeTab {

    public LaethinSyringeItem() {
        super(new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
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
        SummonDripParticlesProcedure.execute(player);
        PlayerUtil.unTransfurPlayer(player);

        // Optional: Reset advancement
        if (playerVars.resetTransfurAdvancements && player instanceof ServerPlayer sp) {
            resetAdvancement(sp, "minecraft:changed/transfur");
        }

        // Apply blindness/confusion if in survival or adventure
        if (!level.isClientSide && !player.isCreative()) {
            applyMobEffect(player, MobEffects.BLINDNESS, 40);
            applyMobEffect(player, MobEffects.CONFUSION, 60);
        }

        // Grant untransfur advancement if not already
        if (player instanceof ServerPlayer serverPlayer) {
            grantAdvancementIfNotDone(serverPlayer, "changed_addon:untransfur_advancement_2");
        }

        // Play sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ChangedAddonSoundEvents.UNTRANSFUR.get(), SoundSource.NEUTRAL, 1, 1);
    }

    protected void applyMobEffect(Player entity, MobEffect effect, int duration) {
        entity.addEffect(new MobEffectInstance(effect, duration, 0, false, false));
    }

    private void resetAdvancement(ServerPlayer player, String id) {
        Advancement adv = player.server.getAdvancements().getAdvancement(ResourceLocation.parse(id));
        if (adv == null) return;

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(adv);
        for (String criteria : progress.getCompletedCriteria()) {
            player.getAdvancements().revoke(adv, criteria);
        }
    }

    protected void grantAdvancementIfNotDone(ServerPlayer player, String advancementId) {
        Advancement advancement = player.server.getAdvancements().getAdvancement(ResourceLocation.parse(advancementId));
        if (advancement == null) return;

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (!progress.isDone()) {
            for (String criterion : progress.getRemainingCriteria()) {
                player.getAdvancements().award(advancement, criterion);
            }
        }
    }
}
