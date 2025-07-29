package net.foxyas.changedaddon.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GatherData {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        BlockTagsProvider blocks = new BlockTagsProvider(generator, helper);
        generator.addProvider(blocks);
        generator.addProvider(new ItemTagsProvider(generator, blocks, helper));
        generator.addProvider(new FluidTagsProvider(generator, helper));

        generator.addProvider(new EntityTypeTagsProvider(generator, helper));
        generator.addProvider(new TFTagsProvider(generator, helper));
        generator.addProvider(new AccessoryEntityProvider(generator));

        //generator.addProvider(new RecipeProvider(generator));

        //generator.addProvider(new LootTableProvider(generator));

        //generator.addProvider(new AdvancementProvider(generator, helper));
    }
}
