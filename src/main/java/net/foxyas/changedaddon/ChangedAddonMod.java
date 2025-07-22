/*
 *    MCreator note:
 *
 *    If you lock base mod element files, you can edit this file and it won't get overwritten.
 *    If you change your modid or package, you need to apply these changes to this file MANUALLY.
 *
 *    Settings in @Mod annotation WON'T be changed in case of the base mod element
 *    files lock too, so you need to set them manually here in such case.
 *
 *    If you do not lock base mod element files in Workspace settings, this file
 *    will be REGENERATED on each build.
 *
 */
package net.foxyas.changedaddon;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;

import net.foxyas.changedaddon.init.ChangedAddonVillagerProfessions;
import net.foxyas.changedaddon.init.ChangedAddonTabs;
import net.foxyas.changedaddon.init.ChangedAddonPotions;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonFluids;
import net.foxyas.changedaddon.init.ChangedAddonFeatures;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonEnchantments;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;

@Mod("changed_addon")
public class ChangedAddonMod {
	public static final Logger LOGGER = LogManager.getLogger(ChangedAddonMod.class);
	public static final String MODID = "changed_addon";
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(ChangedAddonMod.resourceLoc(MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int messageID = 0;

	public ChangedAddonMod() {
		ChangedAddonTabs.load();
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ChangedAddonBlocks.REGISTRY.register(bus);
		ChangedAddonItems.REGISTRY.register(bus);
		ChangedAddonEntities.REGISTRY.register(bus);
		ChangedAddonBlockEntities.REGISTRY.register(bus);
		ChangedAddonFeatures.REGISTRY.register(bus);
		ChangedAddonEnchantments.REGISTRY.register(bus);
		ChangedAddonMobEffects.REGISTRY.register(bus);
		ChangedAddonPotions.REGISTRY.register(bus);

		ChangedAddonParticleTypes.REGISTRY.register(bus);
		ChangedAddonVillagerProfessions.PROFESSIONS.register(bus);
		ChangedAddonFluids.REGISTRY.register(bus);
	}

	public static ResourceLocation resourceLoc(String path){
		return new ResourceLocation(MODID, path);
	}

	public static ResourceLocation textureLoc(String path){
		return new ResourceLocation(MODID, path + ".png");
	}

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
		PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
		messageID++;
	}
}
