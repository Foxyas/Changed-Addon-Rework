package net.foxyas.changedaddon.mixins.client.renderer;

import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.HoldEntityAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.NoSuchElementException;

@Mixin(value = AdvancedHumanoidModel.class, remap = false)
public abstract class AdvancedHumanoidModelMixin <T extends ChangedEntity> {

    @Shadow
    public abstract HumanoidAnimator<T, ?> getAnimator(T t);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/client/renderer/model/AdvancedHumanoidModel;syncPropertyModel(Lnet/ltxprogrammer/changed/entity/ChangedEntity;)V"),
            method = "setupAnim(Lnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFF)V", remap = false)
    private void animateCuddle(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        Player player = entity.getUnderlyingPlayer();
        if (player == null || !player.isSleeping()) return;

        GrabEntityAbilityInstance ability = ProcessTransfur.getPlayerTransfurVariant(player).getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
        if (ability == null || ability.grabbedEntity == null) return;

        HoldEntityAnimator<T,?> anim = (HoldEntityAnimator<T, ?>) getAnimator(entity).getAnimators(HumanoidAnimator.AnimateStage.FINAL).filter(a -> a instanceof HoldEntityAnimator<T,?>).findFirst().orElse(null);
        if (anim == null) return;

        anim.rightArm.xRot = anim.torso.xRot + -1.2566371F;
        anim.rightArm.yRot = anim.torso.yRot + -0.9424779F;
        anim.leftArm.xRot = anim.torso.xRot + -1.6493361F;
        anim.leftArm.yRot = anim.torso.yRot + 0.9424779F;
        ModelPart arm = anim.rightArm;
        arm.y += 2.0F;

        arm = anim.leftArm;
        arm.y += 2.0F;
    }

    @Inject(method = "shouldPartTransfur", at = @At("RETURN"), cancellable = true, remap = false)
    private void turnOffPlantoids(ModelPart part, CallbackInfoReturnable<Boolean> cir) {
        var self = (AdvancedHumanoidModel<?>) (Object) this;
        var torso = self.getTorso();
        try {
            ModelPart plantoidsPart = torso.getChild("Plantoids");
            if (part == plantoidsPart) {
                cir.setReturnValue(!ChangedAddonClientConfiguration.PLANTOIDS_VISIBILITY.get());
            }
            //plantoidsPart.visible = !ChangedAddonClientConfiguration.PLANTOIDS_VARIABLE.get();
        } catch (NoSuchElementException ignored) {
        }
    }
}
