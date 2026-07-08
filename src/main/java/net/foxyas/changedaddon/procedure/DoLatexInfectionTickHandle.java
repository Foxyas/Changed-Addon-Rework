package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.network.ChangedAddonVariables.PlayerVariables;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.ai.ImmediateTransfurDecision;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class DoLatexInfectionTickHandle {

    private static final int HARD_TICK_DELAY = 40;
    private static final int NORMAL_TICK_DELAY = 60;
    private static final int EASY_TICK_DELAY = 100;

    private static final String NBT_INFECTED = "transfur_infected";
    private static final String NBT_LAST_VARIANT = "last_transfur_variant";

    // ---------------------------------------------
    // Utils
    // ---------------------------------------------
    private static float getValueToApply(Level world, Player player) {
        float maxTolerance = (float) Objects.requireNonNull(
                player.getAttribute(ChangedAttributes.TRANSFUR_TOLERANCE.get())
        ).getValue();

        return switch (world.getDifficulty()) {
            case HARD -> maxTolerance * (12.5f / 100);
            case NORMAL -> maxTolerance * (6.25f / 100);
            case EASY -> maxTolerance * (3.1f / 100);
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

    private static boolean isSurvivalOrAdventure(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            GameType gameMode = serverPlayer.gameMode.getGameModeForPlayer();
            return gameMode == GameType.SURVIVAL || gameMode == GameType.ADVENTURE;
        } else if (player.level.isClientSide() && player instanceof AbstractClientPlayer clientPlayer) {
            PlayerInfo playerInfo = Objects.requireNonNull(
                    Minecraft.getInstance().getConnection()
            ).getPlayerInfo(clientPlayer.getGameProfile().getId());

            return playerInfo != null &&
                    playerInfo.getGameMode() != GameType.SPECTATOR &&
                    playerInfo.getGameMode() != GameType.CREATIVE;
        }
        return false;
    }

    // ---------------------------------------------
    // Infection Flags
    // ---------------------------------------------
    public static void setInfected(Player player, boolean value) {
        if (!player.level.isClientSide) {
            player.getPersistentData().putBoolean(NBT_INFECTED, value);
        }
    }

    public static boolean getInfected(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        return persistentData.contains(NBT_INFECTED) && persistentData.getBoolean(NBT_INFECTED);
    }

    private static void setLastVariant(Player player, TransfurVariant<?> variant) {
        if (variant != null && !player.level.isClientSide) {
            player.getPersistentData().putString(NBT_LAST_VARIANT, variant.getFormId().toString());
        }
    }

    private static TransfurVariant<?> getLastVariant(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains(NBT_LAST_VARIANT)) {
            String id = persistentData.getString(NBT_LAST_VARIANT);
            return ChangedRegistry.TRANSFUR_VARIANT.get().getValue(ResourceLocation.parse(id));
        }
        return null;
    }

    private static void clearTempData(Player player) {
        if (!player.level.isClientSide) {
            CompoundTag persistentData = player.getPersistentData();
            persistentData.remove(NBT_INFECTED);
            persistentData.remove(NBT_LAST_VARIANT);
        }
    }

    // ---------------------------------------------
    // Events
    // ---------------------------------------------
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            runDoInfectionTick(event.player);
        }
    }

    @SubscribeEvent
    public static void onTransfurAttackPlayer(ProcessTransfur.TransfurAttackEvent event) {
        LivingEntity entity = event.target;
        if (!(entity instanceof Player player)) return;

        if (!player.level().getGameRules().getBoolean(ChangedAddonGameRules.DO_LATEX_INFECTION)) {
            return;
        }

        if (getInfected(player)) {
            setLastVariant(player, event.variant);
        } else {
            setInfected(player, true);
            setLastVariant(player, event.variant);
        }
    }

    // ---------------------------------------------
    // Infection Tick
    // ---------------------------------------------
    public static void runDoInfectionTick(Player player) {
        if (player == null) return;
        if (ProcessTransfur.isPlayerTransfurred(player)) return;

        if (!player.level().getGameRules().getBoolean(ChangedAddonGameRules.DO_LATEX_INFECTION)) {
            clearTempData(player);
            return;
        }

        PlayerVariables playerVariables = ChangedAddonVariables.ofOrDefault(player);

        int tickCounter = playerVariables.latexInfectionTicks;
        float progress = ProcessTransfur.getPlayerTransfurProgress(player);
        float maxTolerance = (float) ProcessTransfur.getEntityTransfurTolerance(player);
        float mathNumber = getValueToApply(player.level(), player);
        int tickDelay = getTickDelayForDifficulty(player.level());

        // Handle ALWAYS_INFECT option
        if (ChangedAddonServerConfiguration.ALWAYS_INFECT.get()) {
            if (progress > 0 && !getInfected(player)) {
                setInfected(player, true);
            }
            if (!getInfected(player)) {
                return;
            }
            if (player.hasEffect(ChangedAddonMobEffects.LATEX_SOLVENT.get())) {
                setInfected(player, false);
                clearTempData(player);
                return;
            }
        } else {
            if (!(progress > 0) && progress >= maxTolerance) {
                return;
            } else if (player.hasEffect(ChangedAddonMobEffects.LATEX_SOLVENT.get())) {
                return;
            }
        }

        // Infection ticking
        if (isSurvivalOrAdventure(player) && player.level.getDifficulty() != Difficulty.PEACEFUL) {
            if (!getInfected(player)) return;

            if (tickCounter >= tickDelay) {
                if (progress + mathNumber < maxTolerance * 0.998f) {
                    ProcessTransfur.setPlayerTransfurProgress(player, progress + mathNumber);
                    playerVariables.latexInfectionTicks = 0;
                } else {
                    // Chegou ao máximo -> aplica transfur
                    TransfurVariant<?> variant = getLastVariant(player);
                    if (variant != null) {
                        transfurPlayerSafe(player, variant);
                        clearTempData(player);
                    }
                }
            } else {
                playerVariables.latexInfectionTicks++;
            }
        } else if (!isSurvivalOrAdventure(player) && tickCounter != 0) {
            playerVariables.latexInfectionTicks = 0;
        }
    }

    private static void transfurPlayerSafe(Player player, TransfurVariant<?> variant) {
        TransfurContext context = TransfurContext.hazard(TransfurCause.GRAB_ABSORB);
        IAbstractChangedEntity source = context.source() == null ? null : context.source().left().orElse(null);
        ImmediateTransfurDecision<?> transfurDecision = ImmediateTransfurDecision.safe(variant, context.cause(), source);
        ProcessTransfur.transfur(player, transfurDecision);
    }
}
