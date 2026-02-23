package net.foxyas.changedaddon.mixins.client;

import net.foxyas.changedaddon.client.model.api.IHierModel;
import net.foxyas.changedaddon.entity.api.LivingEntityDataExtensor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Optional;

@Mixin(PlayerModel.class)
public class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> implements IHierModel {

    @Shadow
    @Final
    public ModelPart leftSleeve;
    @Shadow
    @Final
    public ModelPart rightSleeve;
    @Shadow
    @Final
    public ModelPart leftPants;
    @Shadow
    @Final
    public ModelPart rightPants;
    @Shadow
    @Final
    public ModelPart jacket;
    @Unique
    @Nullable
    protected ModelPart root;
    @Shadow
    @Final
    private ModelPart ear;
    @Shadow
    @Final
    private ModelPart cloak;
    private final HashMap<String, ModelPart> partMap = new HashMap<>();

    public PlayerModelMixin(ModelPart pRoot) {
        super(pRoot);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setUpRoot(ModelPart pRoot, boolean pSlim, CallbackInfo ci) {
        this.root = pRoot;

        partMap.put("root", pRoot);
        partMap.put("head", head);
        partMap.put("hat", hat);
        partMap.put("body", body);
        partMap.put("right_arm", rightArm);
        partMap.put("left_arm", leftArm);
        partMap.put("right_leg", rightLeg);
        partMap.put("left_leg", leftLeg);

        partMap.put("ear", ear);
        partMap.put("cloak", cloak);
        partMap.put("left_sleeve", leftSleeve);
        partMap.put("right_sleeve", rightSleeve);
        partMap.put("left_pants", leftPants);
        partMap.put("right_pants", rightPants);
        partMap.put("jacket", jacket);
    }

    public HashMap<String, ModelPart> getDefaultPlayerParts(PlayerModel<?> playerModel) {
        return partMap;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void setupAnimHook(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo ci) {
        if (pEntity instanceof LivingEntityDataExtensor livingEntityDataExtensor) {
//            if (getRootPart() == null) return;
//            AnimationState customAnimationState = livingEntityDataExtensor.getCustomAnimationState(0);
//            if (customAnimationState == null) return;
//            if (!customAnimationState.isStarted()) return;
//
//            customAnimationState.ifStarted((animationState) -> {
//                CustomPlayerAnimations.HERO_LANDING.boneAnimations().forEach((string, channel) -> {
//                    getAnyChild(string).ifPresent((part) -> {
//                        part.resetPose();
//                        part.getAllParts().forEach(ModelPart::resetPose);
//                    });
//                });
//                this.animate(customAnimationState, CustomPlayerAnimations.HERO_LANDING, pAgeInTicks, 1);
//            });
        }
    }


    @Override
    public ModelPart getRootPart() {
        return root;
    }

    @Override
    public Optional<ModelPart> getAnyChild(String name) {
        if (name.equals("root")) {
            return Optional.ofNullable(root);
        }

        if (root == null) {
            return Optional.ofNullable(partMap.get(name));
        }

        return root.getAllParts()
                .filter(p -> p.hasChild(name))
                .findFirst()
                .map(p -> p.getChild(name));
    }
}
