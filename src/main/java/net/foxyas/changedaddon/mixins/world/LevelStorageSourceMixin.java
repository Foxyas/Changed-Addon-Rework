package net.foxyas.changedaddon.mixins.world;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;

@Mixin(LevelStorageSource.class)
public abstract class LevelStorageSourceMixin {

    @Inject(
            method = "readLightweightData",
            at = @At("RETURN")
    )
    private static void onReadLightweightData(Path pFile, CallbackInfoReturnable<Tag> cir) {
        if (cir.getReturnValue() == null) return;

        Tag tag = cir.getReturnValue();
        if (!(tag instanceof CompoundTag rootTag)) return;

        if (ChangedAddonMod.dataFixer != null) {
            //ChangedAddonMod.dataFixer.updateCompoundTag(DataFixTypes.LEVEL, rootTag);
        }
    }
}
