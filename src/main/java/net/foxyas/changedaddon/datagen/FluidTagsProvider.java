package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import static net.foxyas.changedaddon.init.ChangedAddonFluids.FLOWING_LITIX_CAMONIA_FLUID;
import static net.foxyas.changedaddon.init.ChangedAddonFluids.LITIX_CAMONIA_FLUID;

public class FluidTagsProvider extends net.minecraft.data.tags.FluidTagsProvider {

    public FluidTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(FluidTags.WATER).add(LITIX_CAMONIA_FLUID.get(), FLOWING_LITIX_CAMONIA_FLUID.get());
    }
}
