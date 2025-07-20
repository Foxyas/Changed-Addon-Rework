package net.foxyas.changedaddon.procedures;

import io.netty.buffer.Unpooled;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.process.util.PlayerUtil;
import net.foxyas.changedaddon.world.inventory.FightToKeepConsciousnessMinigameMenu;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class FightToKeepYourConsciousnessProcedure {

    @SubscribeEvent
    public static void onPlayerTransfur(ProcessTransfur.KeepConsciousEvent event) {
        if (!event.player.getLevel().isClientSide() && !event.keepConscious && event.player.getLevel().getGameRules().getBoolean(ChangedAddonGameRules.FIGHT_TO_KEEP_CONSCIOUSNESS)) {
            event.shouldKeepConscious = true;
            if (event.player instanceof ServerPlayer _ent) {
                BlockPos _bpos = event.player.getOnPos();
                NetworkHooks.openGui(_ent, new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return new TextComponent("FightToKeepConsciousnessMinigame");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                        return new FightToKeepConsciousnessMinigameMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
                    }
                }, _bpos);

                _ent.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.concience_Fight = true;
                    capability.syncPlayerVariables(_ent);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!event.player.level.isClientSide()) { // Executar apenas no servidor
                execute(event, event.player.level, event.player.getX(), event.player.getY(), event.player.getZ(), event.player);
            }
        }
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity.isAlive()) {
            var playerVars = entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null)
                    .orElse(new ChangedAddonModVariables.PlayerVariables());

            if (playerVars.transfur && playerVars.concience_Fight) {
                if (entity instanceof Player player && ProcessTransfur.getPlayerTransfurVariant(player) != null
                        && ProcessTransfur.getPlayerTransfurVariant(player).ageAsVariant >= 100) {

                    if (playerVars.consciousness_fight_progress >= 25) {
                        // Vitória no minigame
                        player.displayClientMessage(new TextComponent(new TranslatableComponent("changedaddon.fight_concience.success").getString()), true);
                        updatePlayerVariables(playerVars, false, 0, false, entity);

                    } else {
                        // Falha no minigame
                        player.displayClientMessage(new TextComponent("You \u00A74Lose \u00A7rYour Conscience"), true);
                        SummonEntityProcedure.execute((Level) world, player);
                        PlayerUtil.UnTransfurPlayer(entity);
                        if (entity instanceof LivingEntity livingEntity) {
                            livingEntity.hurt(new DamageSource("concience_lose").bypassArmor(), 1200);
                        }
                        updatePlayerVariables(playerVars, false, 0, false, entity);
                    }
                }
            }
        }
    }

    private static void updatePlayerVariables(ChangedAddonModVariables.PlayerVariables vars, boolean fight, int progress, boolean giveUp, Entity entity) {
        vars.concience_Fight = fight;
        vars.consciousness_fight_progress = progress;
        vars.consciousness_fight_give_up = giveUp;
        vars.syncPlayerVariables(entity);
    }

}
