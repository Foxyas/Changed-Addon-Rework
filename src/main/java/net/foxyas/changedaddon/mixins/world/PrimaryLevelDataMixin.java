package net.foxyas.changedaddon.mixins.world;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.SharedConstants;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(PrimaryLevelData.class)
public abstract class PrimaryLevelDataMixin {
    @Shadow
    @Final
    private int playerDataVersion;

    @Shadow
    @Nullable
    private CompoundTag loadedPlayerTag;

    @Shadow
    @Final
    private int version;

    @Inject(method = "updatePlayerTag", at = @At("RETURN"))
    private void updateChangedAddonPLayerTag(CallbackInfo callback) {
        if (this.playerDataVersion >= SharedConstants.getCurrentVersion().getDataVersion().getVersion() && ChangedAddonMod.dataFixer != null)
            ChangedAddonMod.dataFixer.updateCompoundTag(DataFixTypes.PLAYER, this.loadedPlayerTag);
    }

    @Inject(method = "createTag", at = @At("RETURN"))
    private void updateChangedAddonLevelData(RegistryAccess pRegistries, CompoundTag pHostPlayerNBT, CallbackInfoReturnable<CompoundTag> cir) {
        if (cir.getReturnValue() != null && ChangedAddonMod.dataFixer != null) {
            ChangedAddonMod.dataFixer.updateCompoundTag(DataFixTypes.LEVEL, cir.getReturnValue());
        }
    }
}