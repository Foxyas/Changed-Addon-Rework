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

import net.foxyas.changedaddon.init.ChangedAddonModVillagerProfessions;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.init.ChangedAddonModPotions;
import net.foxyas.changedaddon.init.ChangedAddonModParticleTypes;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.init.ChangedAddonModFluids;
import net.foxyas.changedaddon.init.ChangedAddonModFeatures;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.init.ChangedAddonModBlockEntities;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;

@Mod("changed_addon")
public class ChangedAddonMod {
	public static final Logger LOGGER = LogManager.getLogger(ChangedAddonMod.class);
	public static final String MODID = "changed_addon";
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(ResourceLocation.parse(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int messageID = 0;

	public ChangedAddonMod() {
		ChangedAddonModTabs.load();
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ChangedAddonModBlocks.REGISTRY.register(bus);
		ChangedAddonModItems.REGISTRY.register(bus);
		ChangedAddonModEntities.REGISTRY.register(bus);
		ChangedAddonModBlockEntities.REGISTRY.register(bus);
		ChangedAddonModFeatures.REGISTRY.register(bus);
		ChangedAddonModEnchantments.REGISTRY.register(bus);
		ChangedAddonModMobEffects.REGISTRY.register(bus);
		ChangedAddonModPotions.REGISTRY.register(bus);

		ChangedAddonModParticleTypes.REGISTRY.register(bus);
		ChangedAddonModVillagerProfessions.PROFESSIONS.register(bus);
		ChangedAddonModFluids.REGISTRY.register(bus);
	}

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
		PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
		messageID++;
	}
}
