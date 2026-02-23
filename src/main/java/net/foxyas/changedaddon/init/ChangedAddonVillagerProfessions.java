package net.foxyas.changedaddon.init;

import com.google.common.collect.ImmutableSet;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ChangedAddonVillagerProfessions {

    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE.key(), ChangedAddonMod.MODID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION.key(), ChangedAddonMod.MODID);

    public static final RegistryObject<VillagerProfession> SCIENTIST = registerProfession("scientist", ChangedAddonBlocks.UNIFUSER, () -> SoundEvents.BREWING_STAND_BREW);

    private static RegistryObject<VillagerProfession> registerProfession(String name, Supplier<? extends Block> block, Supplier<SoundEvent> soundEvent) {
        RegistryObject<PoiType> jobPoi = POI_TYPES.register(name, () -> new PoiType(ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates()), 1, 1));
        Predicate<Holder<PoiType>> predicate = holder -> holder.is(jobPoi.getKey());
        return PROFESSIONS.register(name, () -> new VillagerProfession(ChangedAddonMod.MODID + ":" + name, predicate, predicate, ImmutableSet.of(), ImmutableSet.of(), soundEvent.get()));
    }
}
