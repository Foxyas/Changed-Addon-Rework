package net.foxyas.changedaddon;

import net.foxyas.changedaddon.compatibility.ChangedAddonModCompatEvents;
import net.foxyas.changedaddon.init.*;
import net.foxyas.changedaddon.network.syncher.ChangedAddonEntityDataSerializers;
import net.foxyas.changedaddon.world.datafixer.ChangedAddonDataFixer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod("changed_addon")
public class ChangedAddonMod {

    public static final Logger LOGGER = LogManager.getLogger(ChangedAddonMod.class);
    public static final String MODID = "changed_addon";
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(ChangedAddonMod.resourceLoc("network"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    public static ChangedAddonDataFixer dataFixer;
    private static int messageID = 0;

    public ChangedAddonMod(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        ChangedAddonConfigs.register(context);
        ChangedAddonBlocks.REGISTRY.register(bus);
        ChangedAddonPaintingVariants.PAINTING_TYPES.register(bus);
        ChangedAddonCreativeTabs.TABS.register(bus);

        ChangedAddonItemTiers.init();
        ChangedAddonAttributes.ATTRIBUTES.register(bus);
        ChangedAddonItems.REGISTRY.register(bus);
        ChangedAddonMenus.REGISTRY.register(bus);
        ChangedAddonEntityDataSerializers.SERIALIZERS.register(bus);
        ChangedAddonTreeDecorators.TREE_DECORATORS.register(bus);

        ChangedAddonEntities.REGISTRY.register(bus);
        ChangedAddonAbilities.REGISTRY.register(bus);
        ChangedAddonBlockEntities.REGISTRY.register(bus);
        ChangedAddonFeatures.REGISTRY.register(bus);
        ChangedAddonEnchantments.REGISTRY.register(bus);
        ChangedAddonMobEffects.REGISTRY.register(bus);
        ChangedAddonPotions.REGISTRY.register(bus);
        ChangedAddonAnimationEvents.REGISTRY.register(bus);
        ChangedAddonRecipeTypes.REGISTRY.register(bus);
        ChangedAddonRecipeTypes.SERIALIZERS.register(bus);
        ChangedAddonSoundEvents.SOUNDS.register(bus);

        ChangedAddonParticleTypes.REGISTRY.register(bus);
        ChangedAddonPoiTypes.POI_TYPES.register(bus);
        ChangedAddonVillagerProfessions.PROFESSIONS.register(bus);
        ChangedAddonFluids.FLUID_TYPES.register(bus);
        ChangedAddonFluids.FLUIDS.register(bus);
        ChangedAddonAbilityTreeCodecs.register(bus);

        ChangedAddonBiomeModifiers.BIOME_MODIFIERS.register(bus);
        ChangedAddonStructureTypes.STRUCTURE_TYPES.register(bus);
        ChangedAddonProcessors.PROCESSORS.register(bus);

        ChangedAddonStatRegistry.STATS.register(bus);

        ChangedAddonTransfurVariants.REGISTRY.register(bus);
        dataFixer = new ChangedAddonDataFixer();
        ChangedAddonModCompatEvents.registerOptionalEvents();
    }

    //Thanks :D
    public static ResourceLocation resourceLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static String resourceLocString(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path).toString();
    }

    public static String resourceLocStringStyle(String path) {
        return MODID + ":" + path;
    }

    public static ResourceLocation textureLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path + ".png");
    }

    public static ModelLayerLocation layerLocation(String path, String layer) {
        return new ModelLayerLocation(resourceLoc(path), layer);
    }

    public static <T> @NotNull ResourceKey<T> resourceKey(ResourceKey<? extends Registry<T>> registry, String str) {
        return ResourceKey.create(registry, ResourceLocation.fromNamespaceAndPath(net.foxyas.changedaddon.ChangedAddonMod.MODID, str));
    }

    public static <T extends Event> boolean postEvent(T event) {
        return MinecraftForge.EVENT_BUS.post(event);
    }

    public static <T extends Event & IModBusEvent> void postModLoadingEvent(T event) {
        ModLoader.get().postEvent(event);
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer, NetworkDirection direction) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer, Optional.of(direction));
        messageID++;
    }
}
