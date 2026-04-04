package net.foxyas.changedaddon.mixins.mods.changed;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.entity.api.ChangedEntityExtension;
import net.ltxprogrammer.changed.client.ChangedClient;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ChangedClient.class, remap = false)
public class ChangedClientMixin {

    @WrapOperation(
            method = {"addLatexParticleToChangedEntity", "addLatexParticleToAssimilatedEntity"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/ltxprogrammer/changed/client/renderer/layers/LatexParticlesLayer;createNewDripParticle(Lnet/minecraft/world/entity/LivingEntity;)V"
            )
    )
    private static void applyParticleMultiplier(LatexParticlesLayer<?, ?> instance, LivingEntity entity, Operation<Void> original) {
        int count = 1;

        if (entity instanceof ChangedEntityExtension extension) {
            count = Math.max(1, extension.getDripParticleMultiplier());
        }

        for (int i = 0; i < count; i++) {
            original.call(instance, entity);
        }
    }
}
