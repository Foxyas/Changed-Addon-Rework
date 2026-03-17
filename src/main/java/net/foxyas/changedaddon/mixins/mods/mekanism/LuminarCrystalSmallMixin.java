package net.foxyas.changedaddon.mixins.mods.mekanism;

import net.foxyas.changedaddon.block.LuminarCrystalSmall;
import net.ltxprogrammer.changed.extension.RequiredMods;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@RequiredMods("mekanism")
@Mixin(LuminarCrystalSmall.class)
public abstract class LuminarCrystalSmallMixin extends BushBlock {

    @Unique
    private static final ResourceLocation mekCardboardBox = ResourceLocation.parse("mekanism:cardboard_box");

    public LuminarCrystalSmallMixin(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"),
            method = "onRemove", cancellable = true)
    private void cancelLatexSpawn(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving, CallbackInfo ci) {
        if (newState.getBlock().getRegistryName().equals(mekCardboardBox)) {
            super.onRemove(oldState, level, pos, newState, isMoving);
            ci.cancel();
        }
    }
}
