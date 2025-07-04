package net.foxyas.changedaddon.registers;


import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.*;
import net.foxyas.changedaddon.block.advanced.TimedKeypad;
import net.foxyas.changedaddon.block.advanced.TimedKeypadBlockEntity;
import net.foxyas.changedaddon.block.entity.ContainmentContainerBlockEntity;
import net.foxyas.changedaddon.block.entity.SnepPlushBlockEntity;
import net.foxyas.changedaddon.client.renderer.blockEntitys.ContainmentContainerRenderer;
import net.foxyas.changedaddon.client.renderer.blockEntitys.SnepPlushBlockEntityRenderer;
import net.foxyas.changedaddon.client.renderer.blockEntitys.TimedKeypadBlockEntityRenderer;
import net.foxyas.changedaddon.client.renderer.items.LaserItemDynamicRender;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.item.DarkLatexCoatItem;
import net.foxyas.changedaddon.item.GoldenOrange;
import net.foxyas.changedaddon.item.LaserPointer;
import net.foxyas.changedaddon.item.armor.DyeableShorts;
import net.foxyas.changedaddon.network.packets.KeyPressPacket;
import net.foxyas.changedaddon.network.packets.SyncTransfurVisionsPacket;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.util.Color3;
import net.ltxprogrammer.changed.world.features.structures.FacilityPieces;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityPieceCollection;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityRoomPiece;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonRegisters extends ChangedAddonModItems {

    public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ChangedAddonMod.MODID);

    public static final RegistryObject<Item> GOLDEN_ORANGE = REGISTRY.register("golden_orange", GoldenOrange::new);

    public static final RegistryObject<Item> LATEX_SNOW_FOX_SPAWN_EGG = ITEMS_REGISTRY.register("latex_snow_fox_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.LATEX_SNOW_FOX, 0xFFFFFFF, 0xfD6DDF7, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> LATEX_SNOW_FOX_FEMALE_SPAWN_EGG = ITEMS_REGISTRY.register("latex_snow_fox_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.LATEX_SNOW_FOX_FEMALE, 0xFFFFFFF, 0xfD6DDF7, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> DAZED_LATEX_SPAWN_EGG = ITEMS_REGISTRY.register("latex_dazed_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.DAZED, 0xFFFFFFF, 0xffCFCFCF, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> PURO_KIND = ITEMS_REGISTRY.register("puro_kind_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.PURO_KIND, Color3.getColor("#393939").toInt(), Color3.getColor("#303030").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> PURO_KIND_FEMALE = ITEMS_REGISTRY.register("puro_kind_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.PURO_KIND_FEMALE, Color3.getColor("#393939").toInt(), Color3.getColor("#303030").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> BUNY = ITEMS_REGISTRY.register("buny_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.BUNY, Color3.getColor("#fee9c8").toInt(), Color3.getColor("#9c8c73").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> ORGANIC_SNOW_LEOPARD_MALE_SPAWN_EGG = ITEMS_REGISTRY.register("snow_leopard_male_organic_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.SNOW_LEOPARD_MALE_ORGANIC, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#292929").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> ORGANIC_SNOW_LEOPARD_FEMALE_SPAWN_EGG = ITEMS_REGISTRY.register("snow_leopard_female_organic_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.SNOW_LEOPARD_FEMALE_ORGANIC, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#292929").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> MIRROR_WHITE_TIGER = ITEMS_REGISTRY.register("mirror_white_tiger_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.MIRROR_WHITE_TIGER, Color3.getColor("#FFFFFF").toInt(), Color3.getColor("#ACACAC").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> WOLFY_SPAWN_EGG = ITEMS_REGISTRY.register("wolfy_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.WOLFY, Color3.getColor("#393939").toInt(), Color3.getColor("#303030").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> EXP1_MALE_SPAWN_EGG = ITEMS_REGISTRY.register("exp_1_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXP_1_MALE, 0xFFFFFFF, 0xffb6b9b9, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> EXP1_FEMALE_SPAWN_EGG = ITEMS_REGISTRY.register("exp_1_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXP_1_FEMALE, 0xFFFFFFF, 0xffb6b9b9, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> EXP2_MALE_SPAWN_EGG = ITEMS_REGISTRY.register("exp_2_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXP_2_MALE, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> EXP2_FEMALE_SPAWN_EGG = ITEMS_REGISTRY.register("exp_2_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXP_2_FEMALE, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> LATEX_SNEP_SPAWN_EGG = ITEMS_REGISTRY.register("latex_snep_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.LATEX_SNEP, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> EXP6_SPAWN_EGG = ITEMS_REGISTRY.register("exp_6_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXP_6, Color3.getColor("#B2B1B9").toInt(), Color3.getColor("#CAA2E6").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> EXP10_SPAWN_EGG = ITEMS_REGISTRY.register("experiment_10_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXPERIMENT_10, Color3.getColor("#181818").toInt(), Color3.getColor("#ed1c24").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> EXP10_BOSS_SPAWN_EGG = ITEMS_REGISTRY.register("experiment_10_boss_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXPERIMENT_10_BOSS, Color3.getColor("#181818").toInt(), Color3.getColor("#ed1c24").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> KET_SPAWN_EGG = ITEMS_REGISTRY.register("ket_experiment_009_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.KET_EXPERIMENT_009, Color3.getColor("#E9E9E9").toInt(), Color3.getColor("#66FFFF").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> KET_BOSS_SPAWN_EGG = ITEMS_REGISTRY.register("ket_experiment_009_boss_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.KET_EXPERIMENT_009_BOSS, Color3.getColor("#E9E9E9").toInt(), Color3.getColor("#66FFFF").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> PARTIAL_SNOW_LEOPARD_SPAWN_EGG = ITEMS_REGISTRY.register("latex_snow_leopard_partial_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.SNOW_LEOPARD_PARTIAL, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> REYN_TRANSFUR_SPAWN_EGG = ITEMS_REGISTRY.register("reyn_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.REYN, Color3.getColor("#4C4C4C").toInt(), Color3.getColor("#464646").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> LUMINARCTIC_LEOPARD_SPAWN_EGG = ITEMS_REGISTRY.register("luminarctic_leopard_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.LUMINARCTIC_LEOPARD, Color3.getColor("#414141").toInt(), Color3.getColor("#FFFFFF").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> LUMINARCTIC_FEMALE_LEOPARD_SPAWN_EGG = ITEMS_REGISTRY.register("female_luminarctic_leopard_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.FEMALE_LUMINARCTIC_LEOPARD, Color3.getColor("#414141").toInt(), Color3.getColor("#FFFFFF").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> LATEX_SQUID_TIGER_SHARK_SPAWN_EGG = ITEMS_REGISTRY.register("latex_squid_tiger_shark_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.LATEX_SQUID_TIGER_SHARK, Color3.getColor("#969696").toInt(), Color3.BLACK.toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> LYNX_SPAWN_EGG = ITEMS_REGISTRY.register("lynx_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.LYNX, Color3.getColor("#ebd182").toInt(), Color3.getColor("eace7a").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> FOXTA_FOXY_SPAWN_EGG = ITEMS_REGISTRY.register("foxta_foxy_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.FOXTA_FOXY, Color3.getColor("#FF8F33").toInt(), Color3.getColor("#FFBC85").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> SNEPSI_LEOPARD_SPAWN_EGG = ITEMS_REGISTRY.register("snepsi_leopard_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.SNEPSI_LEOPARD, Color3.getColor("#95D161").toInt(), Color3.getColor("#B5DF90").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> FENGQI_WOLF_SPAWN_EGG = ITEMS_REGISTRY.register("fengqi_wolf_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.FENGQI_WOLF, Color3.getColor("#93c6fd").toInt(), Color3.getColor("#FAC576").toInt(), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> BAGEL_SPAWN_EGG = ITEMS_REGISTRY.register("bagel_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.BAGEL, 0xFFFFFFF, 0xfD6DDF7, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> LATEX_SNEP_SHARK_SPAWN_EGG = ITEMS_REGISTRY.register("latex_dragon_snow_leopard_shark_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.LATEX_DRAGON_SNOW_LEOPARD_SHARK, 0x969696, 0x292929, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> CRYSTAL_GAS_CAT_MALE_SPAWN_EGG = ITEMS_REGISTRY.register("crystal_gas_cat_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.CRYSTAL_GAS_CAT_MALE, 0x9c9c9c, 0x262626, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> CRYSTAL_GAS_CAT_FEMALE_SPAWN_EGG = ITEMS_REGISTRY.register("crystal_gas_cat_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.CRYSTAL_GAS_CAT_FEMALE, 0x9c9c9c, 0x262626, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> VOID_FOX_SPAWN_EGG = ITEMS_REGISTRY.register("void_fox_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.VOID_FOX, 0x393939, 0xffffff, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> HAYDEN_FENNEC_FOX_SPAWN_EGG = ITEMS_REGISTRY.register("hayden_fennec_fox_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.HAYDEN_FENNEC_FOX, 0xF6DC70, 0xF0E4B9, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> BLUE_LIZARD_SPAWN_EGG = ITEMS_REGISTRY.register("blue_lizard_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.BLUE_LIZARD, 0x00F3FF, 0xffffff, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> AVALI_SPAWN_EGG = ITEMS_REGISTRY.register("avali_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.AVALI, 0xffffff, 0xffffff, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> LATEX_KITSUNE_MALE_SPAWN_EGG = ITEMS_REGISTRY.register("latex_kitsune_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_KITSUNE_MALE, 0xffffff, 0xffffff, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> LATEX_KITSUNE_FEMALE_SPAWN_EGG = ITEMS_REGISTRY.register("latex_kitsune_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_KITSUNE_FEMALE, 0xffffff, 0xffffff, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> DARK_LATEX_COAT = ITEMS_REGISTRY.register("dark_latex_coat",
            () -> new DarkLatexCoatItem(EquipmentSlot.CHEST, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));

    public static final RegistryObject<Item> DARK_LATEX_HEAD_CAP = ITEMS_REGISTRY.register("dark_latex_coat_cap",
            () -> new DarkLatexCoatItem.HeadPart(EquipmentSlot.HEAD, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));
    public static final RegistryObject<Item> LASER_POINTER = ITEMS_REGISTRY.register("laser_pointer", LaserPointer::new);

    public static final RegistryObject<Item> DYEABLE_SHORTS = ITEMS_REGISTRY.register("dyeable_shorts", DyeableShorts::new);

    public static final RegistryObject<Item> TIMED_KEYPAD = RegisterBlockItem(ITEMS_REGISTRY, ChangedAddonBlocks.TIMED_KEYPAD, ChangedAddonModTabs.TAB_CHANGED_ADDON);

    @SubscribeEvent
    public static void registerItems(FMLConstructModEvent event) {
        ChangedAddonRegisters.ITEMS_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(LUMINAR_CRYSTAL_SPEAR.get(), new ResourceLocation("throwing"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F));

        // Dynamic Color
        LaserItemDynamicRender.DynamicLaserColor(LASER_POINTER);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientSideHandler {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            LuminarCrystalBlockBlock.registerRenderLayer();
        }
    }

	/*
	*
	*
	@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ChangedAddonItems{
		public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ChangedAddonMod.MODID);
		@SubscribeEvent
		public static void registerItems(FMLConstructModEvent event) {
			ChangedAddonRegisters.ChangedAddonItems.ITEMS_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		}
	}*/


    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ChangedAddonBlocks extends ChangedAddonModBlocks {
        public static final RegistryObject<Block> TIMED_KEYPAD = REGISTRY.register("timed_keypad", TimedKeypad::new);

        @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
        public static class ClientSideHandler {
            @SubscribeEvent
            public static void clientSetup(FMLClientSetupEvent event) {
                //TimedKeypad.registerRenderLayer();
            }
        }
    }

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ChangedAddonBlockEntities {
        public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ChangedAddonMod.MODID);
        public static final RegistryObject<BlockEntityType<SnepPlushBlockEntity>> SNEP_PLUSH = REGISTRY.register("snep_plushe", () -> BlockEntityType.Builder.of(SnepPlushBlockEntity::new, ChangedAddonModBlocks.SNEP_PLUSH.get()).build(null));
        public static final RegistryObject<BlockEntityType<ContainmentContainerBlockEntity>> CONTAINMENT_CONTAINER = REGISTRY.register("containment_container_block_entity", () -> BlockEntityType.Builder.of(ContainmentContainerBlockEntity::new, ChangedAddonModBlocks.CONTAINMENT_CONTAINER.get()).build(null));
        public static final RegistryObject<BlockEntityType<TimedKeypadBlockEntity>> TIMED_KEYPAD_BLOCK_ENTITY = REGISTRY.register("timed_keypad_block_entity", () -> BlockEntityType.Builder.of(TimedKeypadBlockEntity::new, ChangedAddonBlocks.TIMED_KEYPAD.get()).build(null));


        @SubscribeEvent
        public static void registerBlockEntitiesRender(FMLConstructModEvent event) {
            // Registro do renderizador de BlockEntity
            REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        @SubscribeEvent
        public static void registerBlockEntitiesRender(EntityRenderersEvent.RegisterRenderers event) {
            // Registro do renderizador de BlockEntity
            event.registerBlockEntityRenderer(SNEP_PLUSH.get(), SnepPlushBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(CONTAINMENT_CONTAINER.get(), ContainmentContainerRenderer::new);
            event.registerBlockEntityRenderer(TIMED_KEYPAD_BLOCK_ENTITY.get(), TimedKeypadBlockEntityRenderer::new);
        }
    }

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ChangedAddonFacilityPieces extends FacilityPieces {
        public static final FacilityPieceCollection Exp9FacilityRoom = FacilityPieces.ROOMS.register(new FacilityRoomPiece(new ResourceLocation("changed_addon:exp009room"), new ResourceLocation("changed_addon:chests/experiment_009_loot_dna")));
        public static final FacilityPieceCollection Exp10FacilityRoom = FacilityPieces.ROOMS.register(new FacilityRoomPiece(new ResourceLocation("changed_addon:exp10room"), new ResourceLocation("changed_addon:chests/experiment_10_loot_normal")));
        public static final FacilityPieceCollection LuminarCrystalsFacilityRoom = FacilityPieces.ROOMS.register(new FacilityRoomPiece(new ResourceLocation("changed_addon:luminar_crystal_room"), new ResourceLocation("changed:chests/high_tier_lab")));

    }

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ChangedAddonPacketsRegistry {
        @SubscribeEvent
        public static void registerPackets(FMLConstructModEvent event) {
            ChangedAddonMod.addNetworkMessage(KeyPressPacket.class, KeyPressPacket::encode, KeyPressPacket::decode, KeyPressPacket::handle);
            ChangedAddonMod.addNetworkMessage(SyncTransfurVisionsPacket.class, SyncTransfurVisionsPacket::encode, SyncTransfurVisionsPacket::decode, SyncTransfurVisionsPacket::handle);
        }
    }

    private static RegistryObject<Item> RegisterBlockItem(DeferredRegister<Item> REGISTRY, RegistryObject<Block> block, CreativeModeTab tab) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    private static RegistryObject<Item> RegisterBlockItem(RegistryObject<Block> block, CreativeModeTab tab) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static String SpawnEggIdFromTransfurVariant(TransfurVariant latexVariant) {
        String Util = latexVariant.getFormId().toString().replace("changed_addon:form_", "");
        return Util + "_spawn_egg";
    }

	 /*
	 *
	 * @SubscribeEvent
		public static void clientLoad(FMLClientSetupEvent event) {
			event.enqueueWork(() -> {
				//Add code
			});
		}*/

}





