package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.GameEventTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GameEventTagsProvider extends net.minecraft.data.tags.GameEventTagsProvider {
    public GameEventTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        super.addTags(pProvider);
        tag(ChangedAddonTags.GameEvents.CAN_WAKE_UP_ALPHAS).addTag(GameEventTags.VIBRATIONS);
    }

    public String getName() {
        return "Game Event Tags";
    }
}