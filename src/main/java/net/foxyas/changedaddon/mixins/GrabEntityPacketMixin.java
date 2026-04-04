package net.foxyas.changedaddon.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GrabEntityPacket.class)
public abstract class GrabEntityPacketMixin {

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;is(Lnet/minecraft/tags/TagKey;)Z"),
            method = "lambda$handle$4")
    private boolean ignoreTagCheck(boolean original, @Local(name = "livingTarget") LivingEntity livingTarget) {

        return livingTarget instanceof ChangedEntity || original;//TODO add allowGrabTransfurred check?
    }
}
