package net.foxyas.changedaddon.qte;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonDamageSources;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.network.packet.ClientboundOpenFTKCScreenPacket;
import net.foxyas.changedaddon.procedure.SummonEntityProcedure;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class FightToKeepConsciousness {

    public static int getStruggleTime() {
        return ChangedAddonServerConfiguration.FIGHT_TO_KEEP_CONSCIOUSNESS_TIMER.get();
    }

    public static double getStruggleNeed() {
        return ChangedAddonServerConfiguration.FIGHT_TO_KEEP_CONSCIOUSNESS_STRUGGLE_NEED.get();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerTransfur(ProcessTransfur.KeepConsciousEvent event) {
        if (!(event.player instanceof ServerPlayer player) || event.shouldKeepConscious
                || !player.level.getGameRules().getBoolean(ChangedAddonGameRules.FIGHT_TO_KEEP_CONSCIOUSNESS)) return;

        @Nullable
        TransfurVariantInstance<?> oldVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);

        if (event.context.cause == TransfurCause.WHITE_LATEX && oldVariantInstance != null) {
            return;
        }

        event.shouldKeepConscious = true;

        MinigameType minigameType = MinigameType.getRandom(player.getRandom());
        updatePlayerVariables(ChangedAddonVariables.ofOrDefault(player), minigameType, 0, player);

        ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientboundOpenFTKCScreenPacket(minigameType));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) return;

        if (!player.isAlive()) return;

        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
        ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.ofOrDefault(player);

        if (vars.FTKCminigameType == null) return;

        if (instance == null) {
            FightToKeepConsciousness.successFTKC(vars, player);
            return;
        }

        if (instance.ageAsVariant >= getStruggleTime()) {

            if (vars.consciousnessFightProgress >= getStruggleNeed()) {
                successFTKC(vars, player);
                return;
            }

            failFTKC(vars, player);
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ServerPlayer player)) return;

        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
        ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.ofOrDefault(player);

        if (instance == null || vars.FTKCminigameType == null) return;

        vars.FTKCminigameType = null;
        vars.syncPlayerVariables(player);

        PlayerUtil.unTransfurPlayer(player);
    }

    private static void updatePlayerVariables(ChangedAddonVariables.PlayerVariables vars, MinigameType minigameType, int progress, Entity entity) {
        vars.FTKCminigameType = minigameType;
        vars.consciousnessFightProgress = progress;
        vars.syncPlayerVariables(entity);
    }

    @ApiStatus.Internal
    public static void successFTKC(ChangedAddonVariables.PlayerVariables vars, ServerPlayer player) {
        player.displayClientMessage(Component.translatable("changed_addon.fight_conscience.success"), true);

        updatePlayerVariables(vars, null, 0, player);
    }

    @ApiStatus.Internal
    public static void failFTKC(ChangedAddonVariables.PlayerVariables vars, ServerPlayer player) {
        player.displayClientMessage(Component.translatable("changed_addon.fight_conscience.fail"), true);

        SummonEntityProcedure.execute(player.level, player);
        PlayerUtil.unTransfurPlayer(player);

        DamageSource source = ChangedAddonDamageSources.CONSCIENCE_LOSE.source(player.level());
        player.hurt(new DamageSource(source.typeHolder()) {
            @Override
            public boolean is(@NotNull TagKey<DamageType> pDamageTypeKey) {
                if (pDamageTypeKey == DamageTypeTags.BYPASSES_ARMOR) return true;
                return super.is(pDamageTypeKey);
            }
        }, Float.MAX_VALUE);
        updatePlayerVariables(vars, null, 0, player);
    }

    public enum MinigameType {
        MOUSE_PULL(3.5f, ChangedSounds.TRANSFUR_BY_LATEX.get(), FMLLoader.getDist().isDedicatedServer() ? null : FightToKeepConsciousnessClient.MOUSE_PULL()),
        MOUSE_CIRCLE_PULL(4.5f, ChangedSounds.TRANSFUR_BY_LATEX.get(), FMLLoader.getDist().isDedicatedServer() ? null : FightToKeepConsciousnessClient.MOUSE_CIRCLE_PULL()),
        KEY_PRESS(1, FMLLoader.getDist().isDedicatedServer() ? null : FightToKeepConsciousnessClient.KEY_PRESS()),
        CIRCLE_HOVER(0.5f, FMLLoader.getDist().isDedicatedServer() ? null : FightToKeepConsciousnessClient.CIRCLE_HOVER());

        public final Supplier<Screen> screen;
        public final float progressAmount;
        @Nullable
        public final SoundEvent struggleSound;
        @Nullable
        public final SoundEvent successSound;

        MinigameType(float progressAmount, Supplier<Screen> supplier) {
            this.screen = supplier;
            this.progressAmount = progressAmount;
            this.struggleSound = null;
            this.successSound = null;
        }

        MinigameType(float progressAmount, @Nullable SoundEvent struggleSound, Supplier<Screen> supplier) {
            this.screen = supplier;
            this.progressAmount = progressAmount;
            this.struggleSound = struggleSound;
            this.successSound = struggleSound;
        }

        MinigameType(float progressAmount, @Nullable SoundEvent struggleSound, @Nullable SoundEvent successSound, Supplier<Screen> supplier) {
            this.screen = supplier;
            this.progressAmount = progressAmount;
            this.struggleSound = struggleSound;
            this.successSound = successSound;
        }

        public @Nullable SoundEvent getStruggleSound() {
            return struggleSound;
        }

        public @Nullable SoundEvent getSuccessSound() {
            return successSound;
        }

        public static MinigameType getRandom(RandomSource random) {
            return values()[random.nextInt(values().length)];
        }
    }
}
