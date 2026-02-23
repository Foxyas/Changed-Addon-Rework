package net.foxyas.changedaddon.mixins.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private Vec3 position;
    @Shadow
    @Final
    private Vector3f forwards;

    @Shadow
    private Entity entity;

    @Unique
    private static Entity resolveChangedEntity(Entity entity) {
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> transfur = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfur != null) {
                return transfur.getChangedEntity();
            }
        }
        return entity;
    }

    @Unique
    private float getEntityMaxZoomOffset(Entity entity) {
        Optional<Float> playerVariantOffset = ProcessTransfur.getPlayerTransfurVariantSafe(EntityUtil.playerOrNull(entity)).map(variant -> {
            if (variant.getChangedEntity() instanceof IAlphaAbleEntity iAlphaAbleEntity) {
                return iAlphaAbleEntity.alphaCameraOffset() * variant.getTransfurProgression(0);
            }
            return 0f;
        });

        if (entity instanceof LivingEntity living && living instanceof IAlphaAbleEntity iAlphaAbleEntity && iAlphaAbleEntity.isAlpha()) {
            return iAlphaAbleEntity.alphaCameraOffset();
        }

        return playerVariantOffset.orElse(0f);

    }

    @WrapOperation(
            method = "setup",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(D)D", ordinal = 0)
    )
    private double changedaddon$applyThirdPersonOffset(Camera instance, double f, Operation<Double> original) {
        double scale = 1 + getEntityMaxZoomOffset(this.entity); // ajuste aqui
        return original.call(instance, f * scale);
    }

//    @Inject(
//            method = "setup",
//            at = @At("TAIL")
//    )
//    private void changedaddon$applyThirdPersonOffset(BlockGetter pLevel, Entity pEntity, boolean detached, boolean pThirdPersonReverse, float pPartialTick, CallbackInfo ci) {
//        if (detached && !Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
//
//            double extraDistance = getEntityZOffset(pEntity); // ajuste aqui
//            if (extraDistance == 0) return;
//
//            Vec3 vec3Forwards = new Vec3(forwards);
//
//            this.position = this.position.add(vec3Forwards.scale(-extraDistance));
//        }
//    }

}
