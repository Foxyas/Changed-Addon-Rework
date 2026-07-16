package net.foxyas.changedaddon.init;

import com.google.common.collect.ImmutableSet;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ChangedAddonPoiTypes {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, ChangedAddonMod.MODID);

    public static final RegistryObject<PoiType> SCIENTIST = registerPoi("scientist", ChangedAddonBlocks.UNIFUSER);

    private static RegistryObject<PoiType> registerPoi(String name, Supplier<? extends Block> block) {
        return POI_TYPES.register(name, () -> new PoiType(ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates()), 1, 1));
    }
}
