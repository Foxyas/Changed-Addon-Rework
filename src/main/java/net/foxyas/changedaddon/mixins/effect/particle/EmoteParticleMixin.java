package net.foxyas.changedaddon.mixins.effect.particle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.ltxprogrammer.changed.effect.particle.EmoteParticle;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EmoteParticle.class, remap = false)
public abstract class EmoteParticleMixin extends TextureSheetParticle {

    @Shadow
    @Final
    private Entity track;

    protected EmoteParticleMixin(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
    }

    protected EmoteParticleMixin(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
    }

    @Inject(method = "tick", at = @At(value = "TAIL"), remap = true)
    private void scaleParticleBasedOnEntityScale(CallbackInfo ci) {
        if (track instanceof LivingEntity livingTrack) {
            float size = 0.3f * livingTrack.getScale();
            this.setSize(size, size);
            this.quadSize = 0.3f * livingTrack.getScale();
        }
    }


//    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lorg/joml/Vector3f;mul(F)Lorg/joml/Vector3f;", remap = true))
//    private Vector3f scaleEmoteBasedAlphaSize(Vector3f instance, float scalar, Operation<Vector3f> original) {
//        Vector3f call = original.call(instance, scalar);
//        if (track instanceof LivingEntity living && EntityUtil.maybeGetOverlaying(living) instanceof IAlphaAbleEntity alphaAbleEntity) {
//            call.mul(alphaAbleEntity.alphaScaleForRender());
//        }
//        return call;
//    }
}
