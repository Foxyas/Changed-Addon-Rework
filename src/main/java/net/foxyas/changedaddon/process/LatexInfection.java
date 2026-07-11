package net.foxyas.changedaddon.process;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.ai.AssimilationBehavior;
import net.ltxprogrammer.changed.entity.ai.ImmediateTransfurDecision;
import net.ltxprogrammer.changed.entity.ai.LatexAssimilationDecision;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class LatexInfection {

    public static final @NotNull Codec<TransfurVariant<?>> TRANSFUR_VARIANT = ChangedRegistry.TRANSFUR_VARIANT.get().getCodec();
    public static final Codec<LatexInfection> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("isActive").forGetter(LatexInfection::isActive),
            TRANSFUR_VARIANT.optionalFieldOf("infectionVariant").forGetter(LatexInfection::getInfectionVariantSafe)
    ).apply(instance, (isActive, optionalTransfurVariant) -> new LatexInfection(isActive, optionalTransfurVariant.orElse(null))));

    private static final int HARD_TICK_DELAY = 40;
    private static final int NORMAL_TICK_DELAY = 60;
    private static final int EASY_TICK_DELAY = 100;

    public int latexInfectionTicksUntilDamage = 0;
    public boolean active;
    public TransfurVariant<?> infectionVariant;

    public LatexInfection(boolean active, TransfurVariant<?> infectionVariant) {
        super();
        this.active = active;
        this.infectionVariant = infectionVariant;
    }

    public void tick(Player player) {
        if (player == null || player.level.isClientSide()) return;
        if (!player.level.getGameRules().getBoolean(ChangedAddonGameRules.DO_LATEX_INFECTION)
                || ProcessTransfur.isPlayerTransfurred(player)) {
            if (!this.isDefault()) {
                clearDataAndSync(player);
                return;
            }
        }

        int tickCounter = this.getLatexInfectionTicksUntilDamage();
        float progress = ProcessTransfur.getPlayerTransfurProgress(player);
        float playerMaxTolerance = (float) ProcessTransfur.getEntityTransfurTolerance(player);
        float mathNumber = getValueToApply(player.level(), player);
        int tickDelay = getTickDelayForDifficulty(player.level(), player);

        if (!isPlayerInfected(player)) {
            return;
        }

        // Handle ALWAYS_INFECT option
        boolean alwaysInfect = ChangedAddonServerConfiguration.ALWAYS_INFECT.get();
        if (alwaysInfect) {
            if (player.hasEffect(ChangedAddonMobEffects.LATEX_SOLVENT.get())) {
                setPlayerInfectionToAndSync(player, false);
                clearDataAndSync(player);
                return;
            }
        } else {
            if (progress <= 0) { // Normal behavior -> stop if it reaches 0.
                return;
            }
        }

        if (player.hasEffect(ChangedAddonMobEffects.LATEX_SOLVENT.get())) {
            return;
        }

        // Infection ticking
        if (isSurvivalOrAdventure(player) && player.level.getDifficulty() != Difficulty.PEACEFUL) {
            if (!isPlayerInfected(player)) return;

            if (tickCounter >= tickDelay) {
                LatexAssimilationDecision<?> decision = makeLatexAssimilationDecision(mathNumber);
                AssimilationBehavior assimilationBehavior = decision.latexAssimilateVictimBehavior(player);
                if (progress <= playerMaxTolerance * 0.85f) {
                    ProcessTransfur.setPlayerTransfurProgress(player, progress + mathNumber);
                } else if (ProcessTransfur.progressTransfur(player, decision)) {
                    if (assimilationBehavior.willAssimilate()) {
                        clearDataAndSync(player);
                    }
//                    if (assimilationBehavior.willAssimilate()) {
//                        ProcessTransfur.transfur(player, ImmediateTransfurDecision.safe(infectionVariant, context.cause()));
//                        clearDataAndSync(player);
//                    } else {
//                        ProcessTransfur.progressTransfur(player, decision);
//                    }
                }
                this.latexInfectionTicksUntilDamage = 0;
            } else {
                this.latexInfectionTicksUntilDamage++;
            }
        } else if (!isSurvivalOrAdventure(player) && tickCounter != 0) {
            this.latexInfectionTicksUntilDamage = 0;
        }
    }

    public @NotNull LatexAssimilationDecision<?> makeLatexAssimilationDecision(float mathNumber) {
        TransfurContext context = TransfurContext.hazard(TransfurCause.GRAB_ABSORB);
        return LatexAssimilationDecision.strong(LatexAssimilationDecision.Method.ABSORPTION,
                infectionVariant,
                context,
                mathNumber,
                iAbstractChangedEntity -> {
                    if (iAbstractChangedEntity.isPlayer()) {
                        TransfurVariantInstance<?> transfurVariantInstance = iAbstractChangedEntity.getTransfurVariantInstance();
                        if (transfurVariantInstance != null && iAbstractChangedEntity.getEntity() instanceof ServerPlayer serverPlayer) {
                            transfurVariantInstance.willSurviveTransfur = true;
//                            SyncTransfurPacket.Builder builderTf = new SyncTransfurPacket.Builder();
//                            builderTf.addPlayer(serverPlayer, true);
//                            if (builderTf.worthSending()) {
//                                serverPlayer.connection.send(Changed.PACKET_HANDLER.toVanillaPacket(builderTf.build(), NetworkDirection.PLAY_TO_CLIENT));
//                            }
                        }
                    }
                }
        );
    }

    public void onTransfurAttack(Player player, TransfurVariant<?> variant, TransfurContext context) {
        if (isPlayerInfected(player)) {
            setLastVariantAndSync(player, variant, context);
        } else {
            setPlayerInfectionToAndSync(player, true);
            setLastVariantAndSync(player, variant, context);
        }
    }

    public Codec<LatexInfection> getCodec() {
        return CODEC;
    }

    public void save(CompoundTag tag) {
//        CompoundTag latexInfectionTag = new CompoundTag();
//        latexInfectionTag.putBoolean("isActive", active);
//        if (this.infectionVariant != null)
//            latexInfectionTag.putString("infectionVariant", ChangedRegistry.TRANSFUR_VARIANT.getKey(infectionVariant).toString());
//
//        tag.put("latexInfection", latexInfectionTag);

        CODEC.encodeStart(NbtOps.INSTANCE, this)
                .resultOrPartial(errorMessage -> System.err.println("Failed to encode LatexInfection: " + errorMessage))
                .ifPresent(encodedTag -> {
                    if (encodedTag instanceof CompoundTag compound) {
                        tag.put("latexInfection", compound);
                    }
                });
    }

    public void read(CompoundTag tag) {
//        CompoundTag latexInfectionTag = tag.getCompound("latexInfection");
//        this.setActive(latexInfectionTag.getBoolean("isActive"));
//        this.setInfectionVariant(ChangedRegistry.TRANSFUR_VARIANT.getValue(ResourceLocation.parse(latexInfectionTag.getString("infectionVariant"))));
        CODEC.parse(NbtOps.INSTANCE, tag.getCompound("latexInfection"))
                .resultOrPartial(errorMessage -> System.err.println("Failed to parse LatexInfection: " + errorMessage))
                .ifPresent(parsedInstance -> {
                    // Apply the values from the newly parsed instance directly into this one
                    this.setActive(parsedInstance.isActive());
                    this.setInfectionVariant(parsedInstance.getInfectionVariant());
                });
    }

    public LatexInfection fromTag(CompoundTag tag) {
        CompoundTag latexInfectionTag = tag.getCompound("latexInfection");
        boolean isActive = latexInfectionTag.getBoolean("isActive");
        TransfurVariant<?> variant = latexInfectionTag.contains("infectionVariant") ?
                ChangedRegistry.TRANSFUR_VARIANT.getValue(ResourceLocation.parse(latexInfectionTag.getString("infectionVariant"))) : null;
        return new LatexInfection(isActive, variant);
    }

    public void restoreDefault() {
        this.latexInfectionTicksUntilDamage = 0;
        this.infectionVariant = null;
        this.active = false;
    }

    public boolean isDefault() {
        return this.latexInfectionTicksUntilDamage == 0 && this.infectionVariant == null && !this.active;
    }

    public void setLatexInfectionTicksUntilDamage(int latexInfectionTicksUntilDamage) {
        this.latexInfectionTicksUntilDamage = latexInfectionTicksUntilDamage;
    }

    public int getLatexInfectionTicksUntilDamage() {
        return latexInfectionTicksUntilDamage;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setInfectionVariant(TransfurVariant<?> infectionVariant) {
        this.infectionVariant = infectionVariant;
    }

    public TransfurVariant<?> getInfectionVariant() {
        return infectionVariant;
    }

    public Optional<TransfurVariant<?>> getInfectionVariantSafe() {
        return Optional.ofNullable(infectionVariant);
    }

    public static void transfurPlayerSafe(Player player, TransfurVariant<?> variant) {
        TransfurContext context = TransfurContext.hazard(TransfurCause.GRAB_ABSORB);
        IAbstractChangedEntity source = context.source() == null ? null : context.source().left().orElse(null);
        ImmediateTransfurDecision<?> transfurDecision = ImmediateTransfurDecision.safe(variant, context.cause(), source);
        ProcessTransfur.transfur(player, transfurDecision);
    }

    // ---------------------------------------------
    // Utils
    // ---------------------------------------------
    public static float getValueToApply(Level world, Player player) {
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

    public static int getTickDelayForDifficulty(Level world, Player host) {
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
    public static void setPlayerInfectionToAndSync(Player player, boolean value) {
        ChangedAddonVariables.ofPlayerSafe(player)
                .ifPresent(playerVariables -> {
                            LatexInfection latexInfection = playerVariables.latexInfection;
                            latexInfection.active = value;
                            playerVariables.syncPlayerVariables(player);
                        }
                );
    }

    public static boolean isPlayerInfected(Player player) {
        return ChangedAddonVariables.ofPlayerSafe(player)
                .map(ChangedAddonVariables.PlayerVariables::getLatexInfection)
                .map(LatexInfection::isActive)
                .orElse(false);
    }

    private static void setLastVariantAndSync(Player player, TransfurVariant<?> variant, TransfurContext context) {
        if (!player.level.isClientSide) {
            ChangedAddonVariables.ofPlayerSafe(player)
                    .ifPresent(playerVariables -> {
                                LatexInfection latexInfection = playerVariables.latexInfection;
                                latexInfection.infectionVariant = variant;
                                playerVariables.syncPlayerVariables(player);
                            }
                    );
        }
    }

    private static TransfurVariant<?> getLastVariant(Player player) {
        return ChangedAddonVariables.ofPlayerSafe(player)
                .map(ChangedAddonVariables.PlayerVariables::getLatexInfection)
                .map(LatexInfection::getInfectionVariant)
                .orElse(null);
    }

    private static void clearDataAndSync(Player player) {
        if (!player.level.isClientSide) {
            ChangedAddonVariables.ofPlayerSafe(player)
                    .ifPresent(playerVariables -> {
                                LatexInfection latexInfection = playerVariables.latexInfection;
                                latexInfection.restoreDefault();
                                playerVariables.syncPlayerVariables(player);
                            }
                    );
        }
    }
}
