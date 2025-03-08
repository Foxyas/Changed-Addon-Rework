package net.foxyas.changedaddon.network;

import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.ChangedAddonMod;

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
				((PlayerVariables) event.getPlayer().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(event.getPlayer());
		}

		@SubscribeEvent
		public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
			if (!event.getPlayer().level.isClientSide())
				((PlayerVariables) event.getPlayer().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(event.getPlayer());
		}

		@SubscribeEvent
		public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (!event.getPlayer().level.isClientSide())
				((PlayerVariables) event.getPlayer().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(event.getPlayer());
		}

		@SubscribeEvent
		public static void clonePlayer(PlayerEvent.Clone event) {
			event.getOriginal().revive();
			PlayerVariables original = ((PlayerVariables) event.getOriginal().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
			PlayerVariables clone = ((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
			clone.transfur = original.transfur;
			clone.LatexForm = original.LatexForm;
			clone.showwarns = original.showwarns;
			clone.Progress_Transfur_Number = original.Progress_Transfur_Number;
			clone.LatexEntitySummon = original.LatexEntitySummon;
			clone.organic_transfur = original.organic_transfur;
			clone.human_Form = original.human_Form;
			clone.reset_transfur_advancements = original.reset_transfur_advancements;
			clone.ShowRecipes = original.ShowRecipes;
			clone.UnifuserRecipePage = original.UnifuserRecipePage;
			clone.CatlyzerRecipePage = original.CatlyzerRecipePage;
			clone.aredarklatex = original.aredarklatex;
			clone.UntransfurProgress = original.UntransfurProgress;
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

	public static final Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerVariables>() {
	});

	@Mod.EventBusSubscriber
	private static class PlayerVariablesProvider implements ICapabilitySerializable<Tag> {
		@SubscribeEvent
		public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer))
				event.addCapability(new ResourceLocation("changed_addon", "player_variables"), new PlayerVariablesProvider());
		}

		private final PlayerVariables playerVariables = new PlayerVariables();
		private final LazyOptional<PlayerVariables> instance = LazyOptional.of(() -> playerVariables);

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return cap == PLAYER_VARIABLES_CAPABILITY ? instance.cast() : LazyOptional.empty();
		}

		@Override
		public Tag serializeNBT() {
			return playerVariables.writeNBT();
		}

		@Override
		public void deserializeNBT(Tag nbt) {
			playerVariables.readNBT(nbt);
		}
	}

	public static class PlayerVariables {
		public boolean transfur = false;
		public String LatexForm = "any";
		public boolean showwarns = true;
		public double Progress_Transfur_Number = 0;
		public double consciousness_fight_progress = 0;
		public boolean concience_Fight = false;
		public String LatexEntitySummon = "any";
		public boolean organic_transfur = false;
		public boolean human_Form = true;
		public boolean reset_transfur_advancements = false;
		public boolean act_cooldown = false;
		public boolean ShowRecipes = false;
		public double UnifuserRecipePage = 1.0;
		public double CatlyzerRecipePage = 1.0;
		public boolean aredarklatex = false;
		public double LatexInfectionCooldown = 0.0;
		public double UntransfurProgress = 0.0;
		public boolean Exp009TransfurAllowed = false;
		public boolean Exp009Buff = false;
		public boolean consciousness_fight_give_up = false;
		public boolean Exp10TransfurAllowed = false;

		public void syncPlayerVariables(Entity entity) {
			if (entity instanceof ServerPlayer serverPlayer)
				ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerVariablesSyncMessage(this));
		}

		public Tag writeNBT() {
			CompoundTag nbt = new CompoundTag();
			nbt.putBoolean("transfur", transfur);
			nbt.putString("LatexForm", LatexForm);
			nbt.putBoolean("showwarns", showwarns);
			nbt.putDouble("Progress_Transfur_Number", Progress_Transfur_Number);
			nbt.putDouble("consciousness_fight_progress", consciousness_fight_progress);
			nbt.putBoolean("concience_Fight", concience_Fight);
			nbt.putString("LatexEntitySummon", LatexEntitySummon);
			nbt.putBoolean("organic_transfur", organic_transfur);
			nbt.putBoolean("human_Form", human_Form);
			nbt.putBoolean("reset_transfur_advancements", reset_transfur_advancements);
			nbt.putBoolean("act_cooldown", act_cooldown);
			nbt.putBoolean("ShowRecipes", ShowRecipes);
			nbt.putDouble("UnifuserRecipePage", UnifuserRecipePage);
			nbt.putDouble("CatlyzerRecipePage", CatlyzerRecipePage);
			nbt.putBoolean("aredarklatex", aredarklatex);
			nbt.putDouble("LatexInfectionCooldown", LatexInfectionCooldown);
			nbt.putDouble("UntransfurProgress", UntransfurProgress);
			nbt.putBoolean("Exp009TransfurAllowed", Exp009TransfurAllowed);
			nbt.putBoolean("Exp009Buff", Exp009Buff);
			nbt.putBoolean("consciousness_fight_give_up", consciousness_fight_give_up);
			nbt.putBoolean("Exp10TransfurAllowed", Exp10TransfurAllowed);
			return nbt;
		}

		public void readNBT(Tag Tag) {
			CompoundTag nbt = (CompoundTag) Tag;
			transfur = nbt.getBoolean("transfur");
			LatexForm = nbt.getString("LatexForm");
			showwarns = nbt.getBoolean("showwarns");
			Progress_Transfur_Number = nbt.getDouble("Progress_Transfur_Number");
			consciousness_fight_progress = nbt.getDouble("consciousness_fight_progress");
			concience_Fight = nbt.getBoolean("concience_Fight");
			LatexEntitySummon = nbt.getString("LatexEntitySummon");
			organic_transfur = nbt.getBoolean("organic_transfur");
			human_Form = nbt.getBoolean("human_Form");
			reset_transfur_advancements = nbt.getBoolean("reset_transfur_advancements");
			act_cooldown = nbt.getBoolean("act_cooldown");
			ShowRecipes = nbt.getBoolean("ShowRecipes");
			UnifuserRecipePage = nbt.getDouble("UnifuserRecipePage");
			CatlyzerRecipePage = nbt.getDouble("CatlyzerRecipePage");
			aredarklatex = nbt.getBoolean("aredarklatex");
			LatexInfectionCooldown = nbt.getDouble("LatexInfectionCooldown");
			UntransfurProgress = nbt.getDouble("UntransfurProgress");
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
			buffer.writeNbt((CompoundTag) message.data.writeNBT());
		}

		public static void handler(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> {
				if (!context.getDirection().getReceptionSide().isServer()) {
					PlayerVariables variables = ((PlayerVariables) Minecraft.getInstance().player.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
					variables.transfur = message.data.transfur;
					variables.LatexForm = message.data.LatexForm;
					variables.showwarns = message.data.showwarns;
					variables.Progress_Transfur_Number = message.data.Progress_Transfur_Number;
					variables.consciousness_fight_progress = message.data.consciousness_fight_progress;
					variables.concience_Fight = message.data.concience_Fight;
					variables.LatexEntitySummon = message.data.LatexEntitySummon;
					variables.organic_transfur = message.data.organic_transfur;
					variables.human_Form = message.data.human_Form;
					variables.reset_transfur_advancements = message.data.reset_transfur_advancements;
					variables.act_cooldown = message.data.act_cooldown;
					variables.ShowRecipes = message.data.ShowRecipes;
					variables.UnifuserRecipePage = message.data.UnifuserRecipePage;
					variables.CatlyzerRecipePage = message.data.CatlyzerRecipePage;
					variables.aredarklatex = message.data.aredarklatex;
					variables.LatexInfectionCooldown = message.data.LatexInfectionCooldown;
					variables.UntransfurProgress = message.data.UntransfurProgress;
					variables.Exp009TransfurAllowed = message.data.Exp009TransfurAllowed;
					variables.Exp009Buff = message.data.Exp009Buff;
					variables.consciousness_fight_give_up = message.data.consciousness_fight_give_up;
					variables.Exp10TransfurAllowed = message.data.Exp10TransfurAllowed;
				}
			});
			context.setPacketHandled(true);
		}
	}
}
