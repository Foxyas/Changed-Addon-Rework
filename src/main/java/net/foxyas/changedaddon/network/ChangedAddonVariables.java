package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.qte.FightToKeepConsciousness;
import net.foxyas.changedaddon.variant.LatexInfection;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ChangedAddonVariables {

    public static final Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    /**
     * Should never return null unless FakePlayer is used or the player is dead
     */
    public static @Nullable PlayerVariables of(@NotNull Player player) {
        return player.getCapability(PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
    }

    public static Optional<PlayerVariables> ofPlayerSafe(@NotNull Player player) {
        return player.getCapability(PLAYER_VARIABLES_CAPABILITY).resolve();
    }

    public static @NotNull PlayerVariables ofOrDefault(@NotNull Player player) {
        return player.getCapability(PLAYER_VARIABLES_CAPABILITY).resolve().orElseGet(PlayerVariables::new);
    }

    public static @NotNull PlayerVariables nonNullOf(@NotNull Player player) {
        return player.getCapability(PLAYER_VARIABLES_CAPABILITY).orElseThrow(() -> new IllegalStateException("Player Variables Capability expected but not found!"));
    }

    @Mod.EventBusSubscriber
    public static class Provider implements ICapabilitySerializable<CompoundTag> {

        private final PlayerVariables playerVariables = new PlayerVariables();
        private final LazyOptional<PlayerVariables> instance = LazyOptional.of(() -> playerVariables);

        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {//For some reason only works with <Entity>
            if (!(event.getObject() instanceof Player player) || player instanceof FakePlayer) return;
            event.addCapability(ChangedAddonMod.resourceLoc("player_variables"), new Provider());
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return cap == PLAYER_VARIABLES_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return playerVariables.writeNBT(false);
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            playerVariables.readNBT(nbt);
        }
    }

    public static class PlayerVariables {
        @Nullable
        public FightToKeepConsciousness.MinigameType FTKCminigameType = null;

        @NotNull
        public LatexInfection latexInfection = new LatexInfection(); //It shouldn't be null, due serialization we need to keep this as a "defaultable" object.

        public float consciousnessFightProgress = 0;
        public boolean isTransfuredBySafeMethod = true;
        public int timeAfterVictoryOfFTK = 0;
        public int ticksFightingForConsciousness = 0;

        public double untransfurProgress = 0.0;

        public boolean showWarns = true;
        public boolean resetTransfurAdvancements = false;
        public int actCooldown = 0;
        public int patCooldown = 0;
        public boolean exp009BossTransfurPermission = false;
        public boolean exp10BossTransfurPermission = false;

        public boolean isCuddling = false;

        public void tickCooldowns() {
            if (actCooldown > 0) {
                actCooldown--;
            }
            if (patCooldown > 0) {
                patCooldown--;
            }
        }

        public void syncPlayerVariables(Entity entity) {
            if (entity instanceof ServerPlayer serverPlayer)
                ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncPacket(this));
        }

        public void copyTo(PlayerVariables other, boolean wasDeath) {
            other.resetTransfurAdvancements = resetTransfurAdvancements;
            other.untransfurProgress = untransfurProgress;
            other.exp009BossTransfurPermission = exp009BossTransfurPermission;
            other.exp10BossTransfurPermission = exp10BossTransfurPermission;
            other.isCuddling = isCuddling;
            if (!wasDeath) {
                other.consciousnessFightProgress = consciousnessFightProgress;
                other.FTKCminigameType = FTKCminigameType;
                other.isTransfuredBySafeMethod = isTransfuredBySafeMethod;
                other.timeAfterVictoryOfFTK = timeAfterVictoryOfFTK;
                other.ticksFightingForConsciousness = ticksFightingForConsciousness;
                other.latexInfection = latexInfection;
            }
        }

        public CompoundTag writeNBT(boolean forSync) {
            CompoundTag nbt = new CompoundTag();
            nbt.putBoolean("showWarns", showWarns);
            nbt.putFloat("consciousnessFightProgress", consciousnessFightProgress);
            nbt.putByte("FTKCminigameType", FTKCminigameType != null ? (byte) FTKCminigameType.ordinal() : -1);
            nbt.putBoolean("resetTransfurAdvancements", resetTransfurAdvancements);
            if (forSync) {
                nbt.putInt("actCooldown", actCooldown);
                nbt.putInt("patCooldown", patCooldown);
            }
            nbt.putDouble("UntransfurProgress", untransfurProgress);
            nbt.putBoolean("Exp009TransfurAllowed", exp009BossTransfurPermission);
            nbt.putBoolean("Exp10TransfurAllowed", exp10BossTransfurPermission);
            nbt.putBoolean("isCuddling", isCuddling);
            nbt.putBoolean("isTransfuredBySafeMethod", isTransfuredBySafeMethod);
            nbt.putInt("timeAfterVictoryOfFTK", timeAfterVictoryOfFTK);
            nbt.putInt("ticksFightingForConsciousness", ticksFightingForConsciousness);

            latexInfection.save(nbt);
            return nbt;
        }

        public void readNBT(Tag Tag) {
            CompoundTag nbt = (CompoundTag) Tag;
            showWarns = nbt.getBoolean("showWarns");
            consciousnessFightProgress = nbt.getFloat("consciousnessFightProgress");

            if (nbt.contains("FTKCminigameType")) {
                FTKCminigameType = nbt.getByte("FTKCminigameType") != (byte) -1
                        ? FightToKeepConsciousness.MinigameType.values()[nbt.getByte("FTKCminigameType")]
                        : null;
            } else FTKCminigameType = null;

            resetTransfurAdvancements = nbt.getBoolean("resetTransfurAdvancements");
            actCooldown = nbt.getInt("actCooldown");
            patCooldown = nbt.getInt("patCooldown");
            untransfurProgress = nbt.getDouble("UntransfurProgress");
            exp009BossTransfurPermission = nbt.getBoolean("Exp009TransfurAllowed");
            exp10BossTransfurPermission = nbt.getBoolean("Exp10TransfurAllowed");
            isCuddling = nbt.getBoolean("isCuddling");
            isTransfuredBySafeMethod = nbt.getBoolean("isTransfuredBySafeMethod");
            timeAfterVictoryOfFTK = nbt.getInt("timeAfterVictoryOfFTK");
            ticksFightingForConsciousness = nbt.getInt("ticksFightingForConsciousness");

            latexInfection.read(nbt);
        }

        public void copyFrom(PlayerVariables other) {
            showWarns = other.showWarns;
            consciousnessFightProgress = other.consciousnessFightProgress;
            FTKCminigameType = other.FTKCminigameType;
            resetTransfurAdvancements = other.resetTransfurAdvancements;
            actCooldown = other.actCooldown;
            patCooldown = other.patCooldown;
            untransfurProgress = other.untransfurProgress;
            exp009BossTransfurPermission = other.exp009BossTransfurPermission;
            exp10BossTransfurPermission = other.exp10BossTransfurPermission;
            isCuddling = other.isCuddling;
            isTransfuredBySafeMethod = other.isTransfuredBySafeMethod;
            timeAfterVictoryOfFTK = other.timeAfterVictoryOfFTK;
            ticksFightingForConsciousness = other.ticksFightingForConsciousness;
            latexInfection = other.latexInfection;
        }

        public LatexInfection getLatexInfection() {
            return latexInfection;
        }

        public double getUntransfurProgress() {
            return untransfurProgress;
        }

        public int getPatCooldownTicks() {
            return this.patCooldown;
        }

        public int getActCooldownTicks() {
            return actCooldown;
        }

        public boolean isActInCooldown() {
            return this.actCooldown > 0;
        }

        public boolean isPatInCooldown() {
            return this.actCooldown > 0;
        }

        public boolean isCuddling() {
            return isCuddling;
        }

        public boolean has10BossTransfurPermission() {
            return exp10BossTransfurPermission;
        }

        public boolean hasExp009BossTransfurPermission() {
            return exp009BossTransfurPermission;
        }

        public boolean isTransfuredBySafeMethod() {
            return isTransfuredBySafeMethod;
        }

        public boolean shouldResetTransfurAdvancements() {
            return resetTransfurAdvancements;
        }
    }

    public static class SyncPacket {

        public PlayerVariables data;

        public SyncPacket(PlayerVariables data) {
            this.data = data;
        }

        public SyncPacket(FriendlyByteBuf buffer) {
            this.data = new PlayerVariables();
            this.data.readNBT(buffer.readNbt());
        }

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeNbt(data.writeNBT(true));
        }
    }
}
