package net.foxyas.changedaddon.mixins.world;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(PrimaryLevelData.class)
public abstract class PrimaryLevelDataMixin {
    @Shadow
    @Final
    private int playerDataVersion;

    @Shadow
    @Nullable
    private CompoundTag loadedPlayerTag;

    @Inject(method = "updatePlayerTag", at = @At("RETURN"))
    private void updateChangedAddonPLayerTag(CallbackInfo callback) {
        if (this.playerDataVersion >= SharedConstants.getCurrentVersion().getDataVersion().getVersion() && ChangedAddonMod.dataFixer != null)
            ChangedAddonMod.dataFixer.updateCompoundTag(DataFixTypes.PLAYER, this.loadedPlayerTag);
    }

    @ModifyReturnValue(method = "createTag", at = @At("RETURN"))
    private CompoundTag updateChangedAddonLevelData(CompoundTag tag) {
        if (tag != null && ChangedAddonMod.dataFixer != null) {
            ChangedAddonMod.dataFixer.updateCompoundTag(DataFixTypes.LEVEL, tag);
        }
        return tag;
    }
}