package net.foxyas.changedaddon.mixins.world;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelStorageSource.LevelStorageAccess.class)
public class LevelStorageAccessMixin {

    // For Future Stuff

    @Inject(method = "saveDataTag(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/level/storage/WorldData;Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    private void saveHook(RegistryAccess pRegistries, WorldData pServerConfiguration, CompoundTag pHostPlayerNBT, CallbackInfo ci) {
    }
}
