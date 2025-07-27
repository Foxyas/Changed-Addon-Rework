package net.foxyas.changedaddon.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class AdvancementProvider extends net.minecraft.data.advancements.AdvancementProvider {

    public AdvancementProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    @Override
    protected void registerAdvancements(@NotNull Consumer<Advancement> consumer, @NotNull ExistingFileHelper fileHelper) {

    }
}
