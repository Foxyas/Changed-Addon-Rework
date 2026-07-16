package net.foxyas.changedaddon.init;

import com.google.common.collect.ImmutableSet;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ChangedAddonVillagerProfessions {

    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, ChangedAddonMod.MODID);

    public static final RegistryObject<VillagerProfession> SCIENTIST = registerProfession("scientist", ChangedAddonPoiTypes.SCIENTIST, () -> SoundEvents.BREWING_STAND_BREW);

    private static RegistryObject<VillagerProfession> registerProfession(String name, ResourceKey<PoiType> poiTypeResourceKey, Supplier<SoundEvent> soundEvent) {
        Predicate<Holder<PoiType>> predicate = holder -> holder.is(poiTypeResourceKey);
        return PROFESSIONS.register(name, () -> new VillagerProfession(ChangedAddonMod.MODID + ":" + name, predicate, predicate, ImmutableSet.of(), ImmutableSet.of(), soundEvent.get()));
    }

    private static RegistryObject<VillagerProfession> registerProfession(String name, Supplier<PoiType> jobPoi, Supplier<SoundEvent> soundEvent) {
        Predicate<Holder<PoiType>> predicate = holder -> holder.is(ForgeRegistries.POI_TYPES.getKey(jobPoi.get()));
        return PROFESSIONS.register(name, () -> new VillagerProfession(ChangedAddonMod.MODID + ":" + name, predicate, predicate, ImmutableSet.of(), ImmutableSet.of(), soundEvent.get()));
    }

    private static RegistryObject<VillagerProfession> registerProfessionAndPoiType(String name, Supplier<? extends Block> block, Supplier<SoundEvent> soundEvent) {
        RegistryObject<PoiType> jobPoi = ChangedAddonPoiTypes.POI_TYPES.register(name, () -> new PoiType(ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates()), 1, 1));
        Predicate<Holder<PoiType>> predicate = holder -> holder.is(jobPoi.getKey());
        return PROFESSIONS.register(name, () -> new VillagerProfession(ChangedAddonMod.MODID + ":" + name, predicate, predicate, ImmutableSet.of(), ImmutableSet.of(), soundEvent.get()));
    }
}
