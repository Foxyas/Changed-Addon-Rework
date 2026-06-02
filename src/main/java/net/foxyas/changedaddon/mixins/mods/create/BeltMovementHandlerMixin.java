package net.foxyas.changedaddon.mixins.mods.create;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.kinetics.belt.transport.BeltMovementHandler;
import net.foxyas.changedaddon.compatibility.create.IDynamicBeltMovementEntity;
import net.foxyas.changedaddon.extension.RequiredMods;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BeltMovementHandler.class, remap = false)
@RequiredMods("create")
public class BeltMovementHandlerMixin {

    @ModifyReturnValue(method = "canBeTransported", at = @At("RETURN"))
    private static boolean overrideCanBeTransported(boolean original, Entity entity) {
        if (entity instanceof IDynamicBeltMovementEntity iDynamicBeltMovementEntity) {
            return iDynamicBeltMovementEntity.canBeTransportedByBelts(original);
        }
        return original;
    }
}
