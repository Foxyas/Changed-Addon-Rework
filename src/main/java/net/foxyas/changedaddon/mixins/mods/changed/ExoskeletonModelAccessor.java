package net.foxyas.changedaddon.mixins.mods.changed;

import net.ltxprogrammer.changed.client.renderer.model.ExoskeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ExoskeletonModel.class, remap = false)
public interface ExoskeletonModelAccessor {
    @Accessor("Torso")
    ModelPart getTorso();

    @Accessor("RightArm")
    ModelPart getRightArm();

    @Accessor("LeftArm")
    ModelPart getLeftArm();

    @Accessor("RightLeg")
    ModelPart getRightLeg();

    @Accessor("LeftLeg")
    ModelPart getLeftLeg();
}
