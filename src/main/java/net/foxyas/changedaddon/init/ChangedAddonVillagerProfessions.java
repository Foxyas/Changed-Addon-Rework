package net.foxyas.changedaddon.init;

import com.google.common.collect.ImmutableSet;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonVillagerProfessions {
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, ChangedAddonMod.MODID);
    private static final Map<String, ProfessionPoiType> POI_TYPES = new HashMap<>();
    public static final RegistryObject<VillagerProfession> SCIENTIST = registerProfession("scientist", () -> ChangedAddonBlocks.UNIFUSER.get(), () -> ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.brewing_stand.brew")));

    private static RegistryObject<VillagerProfession> registerProfession(String name, Supplier<Block> block, Supplier<SoundEvent> soundEvent) {
        POI_TYPES.put(name, new ProfessionPoiType(block, null));
        return PROFESSIONS.register(name, () -> {
            PoiType poiPredicate = POI_TYPES.get(name).poiType;
            return new VillagerProfession(ChangedAddonMod.MODID + ":" + name, poiPredicate, ImmutableSet.of(), ImmutableSet.of(), soundEvent.get());
        });
    }

    @SubscribeEvent
    public static void registerProfessionPointsOfInterest(RegistryEvent.Register<PoiType> event) {
        for (Map.Entry<String, ProfessionPoiType> entry : POI_TYPES.entrySet()) {
            Block block = entry.getValue().block.get();
            String name = entry.getKey();
            Optional<PoiType> existingCheck = PoiType.forState(block.defaultBlockState());
            if (existingCheck.isPresent()) {
                ChangedAddonMod.LOGGER.error("Skipping villager profession " + name + " that uses POI block " + block + " that is already in use by " + existingCheck);
                continue;
            }
            PoiType poiType = new PoiType(name, ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates()), 1, 1).setRegistryName(ChangedAddonMod.MODID + ":" + name);
            event.getRegistry().register(poiType);
            entry.getValue().poiType = poiType;
        }
    }

    private static class ProfessionPoiType {
        final Supplier<Block> block;
        PoiType poiType;

        ProfessionPoiType(Supplier<Block> block, PoiType poiType) {
            this.block = block;
            this.poiType = poiType;
        }
    }
}
