package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonModVariables {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		ChangedAddonMod.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
	}

	@SubscribeEvent
	public static void init(RegisterCapabilitiesEvent event) {
		event.register(PlayerVariables.class);
	}

	@Mod.EventBusSubscriber
	public static class EventBusVariableHandlers {
		@SubscribeEvent
		public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
			if (!event.getPlayer().level.isClientSide())
				event.getPlayer().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()).syncPlayerVariables(event.getPlayer());
		}

		@SubscribeEvent
		public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
			if (!event.getPlayer().level.isClientSide())
				event.getPlayer().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()).syncPlayerVariables(event.getPlayer());
		}

		@SubscribeEvent
		public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (!event.getPlayer().level.isClientSide())
				event.getPlayer().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()).syncPlayerVariables(event.getPlayer());
		}

		@SubscribeEvent
		public static void clonePlayer(PlayerEvent.Clone event) {
			Player originalPl = event.getOriginal();
			originalPl.reviveCaps();
			PlayerVariables original = originalPl.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
			originalPl.invalidateCaps();

			PlayerVariables clone = event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
			clone.LatexEntitySummon = original.LatexEntitySummon;
			clone.reset_transfur_advancements = original.reset_transfur_advancements;
			clone.aredarklatex = original.aredarklatex;
			clone.untransfurProgress = original.untransfurProgress;
			clone.Exp009TransfurAllowed = original.Exp009TransfurAllowed;
			clone.Exp009Buff = original.Exp009Buff;
			clone.Exp10TransfurAllowed = original.Exp10TransfurAllowed;
			if (!event.isWasDeath()) {
				clone.consciousness_fight_progress = original.consciousness_fight_progress;
				clone.concience_Fight = original.concience_Fight;
				clone.act_cooldown = original.act_cooldown;
				clone.LatexInfectionCooldown = original.LatexInfectionCooldown;
				clone.consciousness_fight_give_up = original.consciousness_fight_give_up;
			}
		}
	}

	public static final Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

	@Mod.EventBusSubscriber
	private static class PlayerVariablesProvider implements ICapabilitySerializable<CompoundTag> {
		@SubscribeEvent
		public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer))
				event.addCapability(ChangedAddonMod.resourceLoc("player_variables"), new PlayerVariablesProvider());
		}

		private final PlayerVariables playerVariables = new PlayerVariables();
		private final LazyOptional<PlayerVariables> instance = LazyOptional.of(() -> playerVariables);

		@Override
		public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
			return cap == PLAYER_VARIABLES_CAPABILITY ? instance.cast() : LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			return playerVariables.writeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			playerVariables.readNBT(nbt);
		}
	}

	public static class PlayerVariables {
		public boolean showwarns = true;
		public double consciousness_fight_progress = 0;
		public boolean concience_Fight = false;
		public String LatexEntitySummon = "any";
		public boolean reset_transfur_advancements = false;
		public boolean act_cooldown = false;
		public boolean aredarklatex = false;
		public double LatexInfectionCooldown = 0.0;
		public double untransfurProgress = 0.0;
		public boolean Exp009TransfurAllowed = false;
		public boolean Exp009Buff = false;
		public boolean consciousness_fight_give_up = false;
		public boolean Exp10TransfurAllowed = false;

		/**
		 * Should never return null unless FakePlayer is used or the player is dead
		 */
		public static @Nullable PlayerVariables of(@NotNull Player player){
			return player.getCapability(PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
		}

		public void syncPlayerVariables(Entity entity) {
			if (entity instanceof ServerPlayer serverPlayer)
				ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerVariablesSyncMessage(this));
		}

		public CompoundTag writeNBT() {
			CompoundTag nbt = new CompoundTag();
			nbt.putBoolean("showwarns", showwarns);
			nbt.putDouble("consciousness_fight_progress", consciousness_fight_progress);
			nbt.putBoolean("concience_Fight", concience_Fight);
			nbt.putString("LatexEntitySummon", LatexEntitySummon);
			nbt.putBoolean("reset_transfur_advancements", reset_transfur_advancements);
			nbt.putBoolean("act_cooldown", act_cooldown);
			nbt.putBoolean("aredarklatex", aredarklatex);
			nbt.putDouble("LatexInfectionCooldown", LatexInfectionCooldown);
			nbt.putDouble("UntransfurProgress", untransfurProgress);
			nbt.putBoolean("Exp009TransfurAllowed", Exp009TransfurAllowed);
			nbt.putBoolean("Exp009Buff", Exp009Buff);
			nbt.putBoolean("consciousness_fight_give_up", consciousness_fight_give_up);
			nbt.putBoolean("Exp10TransfurAllowed", Exp10TransfurAllowed);
			return nbt;
		}

		public void readNBT(Tag Tag) {
			CompoundTag nbt = (CompoundTag) Tag;
			showwarns = nbt.getBoolean("showwarns");
			consciousness_fight_progress = nbt.getDouble("consciousness_fight_progress");
			concience_Fight = nbt.getBoolean("concience_Fight");
			LatexEntitySummon = nbt.getString("LatexEntitySummon");
			reset_transfur_advancements = nbt.getBoolean("reset_transfur_advancements");
			act_cooldown = nbt.getBoolean("act_cooldown");
			aredarklatex = nbt.getBoolean("aredarklatex");
			LatexInfectionCooldown = nbt.getDouble("LatexInfectionCooldown");
			untransfurProgress = nbt.getDouble("UntransfurProgress");
			Exp009TransfurAllowed = nbt.getBoolean("Exp009TransfurAllowed");
			Exp009Buff = nbt.getBoolean("Exp009Buff");
			consciousness_fight_give_up = nbt.getBoolean("consciousness_fight_give_up");
			Exp10TransfurAllowed = nbt.getBoolean("Exp10TransfurAllowed");
		}
	}

	public static class PlayerVariablesSyncMessage {
		public PlayerVariables data;

		public PlayerVariablesSyncMessage(FriendlyByteBuf buffer) {
			this.data = new PlayerVariables();
			this.data.readNBT(buffer.readNbt());
		}

		public PlayerVariablesSyncMessage(PlayerVariables data) {
			this.data = data;
		}

		public static void buffer(PlayerVariablesSyncMessage message, FriendlyByteBuf buffer) {
			buffer.writeNbt(message.data.writeNBT());
		}

		public static void handler(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.setPacketHandled(true);
			if(context.getDirection().getReceptionSide().isServer()) return;

			context.enqueueWork(() -> {
                assert Minecraft.getInstance().player != null;
                PlayerVariables variables = Minecraft.getInstance().player.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
				variables.showwarns = message.data.showwarns;
				variables.consciousness_fight_progress = message.data.consciousness_fight_progress;
				variables.concience_Fight = message.data.concience_Fight;
				variables.LatexEntitySummon = message.data.LatexEntitySummon;
				variables.reset_transfur_advancements = message.data.reset_transfur_advancements;
				variables.act_cooldown = message.data.act_cooldown;
				variables.aredarklatex = message.data.aredarklatex;
				variables.LatexInfectionCooldown = message.data.LatexInfectionCooldown;
				variables.untransfurProgress = message.data.untransfurProgress;
				variables.Exp009TransfurAllowed = message.data.Exp009TransfurAllowed;
				variables.Exp009Buff = message.data.Exp009Buff;
				variables.consciousness_fight_give_up = message.data.consciousness_fight_give_up;
				variables.Exp10TransfurAllowed = message.data.Exp10TransfurAllowed;
			});
		}
	}
}
