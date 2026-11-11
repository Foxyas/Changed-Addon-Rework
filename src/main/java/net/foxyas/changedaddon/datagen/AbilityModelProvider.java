package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;

import static net.foxyas.changedaddon.init.ChangedAddonItems.*;

public class AbilityModelProvider extends net.foxyas.changedaddon.datagen.customData.AbilityModelProvider {

    public AbilityModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicAbility$(ChangedAddonAbilities.CRYSTAL_SHARD_BARRAGE.get(), "crystal_shard_barrage")
                .texture("layer0", ChangedAddonMod.resourceLocString("abilities/crystal_shard_barrage/crystal_shard"));
    }
}
