package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.configuration.ChangedAddonConfigsConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonModGameRules;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class DolatexinfectiontickProcedure {
    private static final int HARD_TICK_DELAY = 40;
    private static final int NORMAL_TICK_DELAY = 60;
    private static final int EASY_TICK_DELAY = 100;

    private static float getValueToApply(Level world, Player player) {
        float MAX_TRANSFUR_PROGRESS = ((float) Objects.requireNonNull(player.getAttribute(ChangedAttributes.TRANSFUR_TOLERANCE.get())).getValue());
        //float MAX_TRANSFUR_PROGRESS = Changed.config.server.transfurTolerance.get().floatValue();
        return switch (world.getDifficulty()) {
            case HARD -> MAX_TRANSFUR_PROGRESS * (12.5f / 100);
            case NORMAL -> MAX_TRANSFUR_PROGRESS * (6.25f / 100);
            case EASY -> MAX_TRANSFUR_PROGRESS * (3.1f / 100);
            default -> 0f;
        };
    }

    private static int getTickDelayForDifficulty(Level world) {
        return switch (world.getDifficulty()) {
            case EASY -> EASY_TICK_DELAY;
            case NORMAL -> NORMAL_TICK_DELAY;
            case HARD -> HARD_TICK_DELAY;
            default -> -1;
        };
    }

    public static void setInfected(Player player, boolean value) {
        if (!player.level.isClientSide) {
            player.getPersistentData().putBoolean("transfur_infected", value);
        }
    }

    public static boolean getInfected(Player player) {
        return player.getPersistentData().contains("transfur_infected") && player.getPersistentData().getBoolean("transfur_infected");
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event.player);
        }
    }

    public static void execute(Player player) {
        if (player == null) return;

		if (!player.getLevel().getGameRules().getBoolean(ChangedAddonModGameRules.DO_LATEX_INFECTION)) {
			return;
		}

        ChangedAddonModVariables.PlayerVariables playerVariables = player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY)
                .orElse(new ChangedAddonModVariables.PlayerVariables());

        int tickCounter = (int) playerVariables.LatexInfectionCooldown;

        float transfurProgress = ProcessTransfur.getPlayerTransfurProgress(player);
        float mathnumber = getValueToApply(player.getLevel(), player);
        int tickDelay = getTickDelayForDifficulty(player.getLevel());
        var PlayerTolerance = ProcessTransfur.getEntityTransfurTolerance(player);

        if (ChangedAddonConfigsConfiguration.ALWAYS_INFECT.get()) {
            if (transfurProgress > 0) {
                if (!getInfected(player)) {
                    setInfected(player, true);
                }
            } 
            if (!getInfected(player)) {// transfurProgress.progress() > 0
                return;
            } 
            if (player.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get())) {
                if (getInfected(player)) {
                    setInfected(player, false);
                }
                return;
            }
        } else {
            if (!(transfurProgress > 0) && transfurProgress >= PlayerTolerance) {
                return;
            } else if (player.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get())) {
                return;
            }
        }

        if (isSurvivalOrAdventure(player)
                && (player.level.getDifficulty() != Difficulty.PEACEFUL)) {

            if (tickCounter >= tickDelay) {
                if (transfurProgress + mathnumber < PlayerTolerance * 0.995f){
                    ProcessTransfur.setPlayerTransfurProgress(player, transfurProgress + mathnumber);
                    playerVariables.LatexInfectionCooldown = 0;
                }
            } else {
                playerVariables.LatexInfectionCooldown++;
            }

        } else if (!isSurvivalOrAdventure(player) && tickCounter != 0) {
            playerVariables.LatexInfectionCooldown = 0;
        }
    }

    private static boolean isSurvivalOrAdventure(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            GameType gameMode = serverPlayer.gameMode.getGameModeForPlayer();
            return gameMode == GameType.SURVIVAL || gameMode == GameType.ADVENTURE;
        } else if (player.level.isClientSide() && player instanceof AbstractClientPlayer clientPlayer) {
            PlayerInfo playerInfo = Objects.requireNonNull(Minecraft.getInstance().getConnection()).getPlayerInfo(clientPlayer.getGameProfile().getId());
            return playerInfo != null && playerInfo.getGameMode() != GameType.SPECTATOR && playerInfo.getGameMode() != GameType.CREATIVE;
        }
        return false;
    }
}
