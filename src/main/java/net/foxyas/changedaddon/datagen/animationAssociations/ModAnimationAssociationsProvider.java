package net.foxyas.changedaddon.datagen.animationAssociations;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.data.PackOutput;

public class ModAnimationAssociationsProvider extends TransfurAnimationAssociationsProvider {

    public ModAnimationAssociationsProvider(PackOutput packOutput) {
        super(packOutput, ChangedAddonMod.MODID);
    }

    @Override
    protected void registerAssociations() {
    }
}