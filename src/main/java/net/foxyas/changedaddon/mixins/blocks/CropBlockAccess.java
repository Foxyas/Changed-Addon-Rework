package net.foxyas.changedaddon.mixins.blocks;

import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CropBlock.class)
public interface CropBlockAccess {

    @Invoker("getAgeProperty")
    IntegerProperty getAgeProp();
}
