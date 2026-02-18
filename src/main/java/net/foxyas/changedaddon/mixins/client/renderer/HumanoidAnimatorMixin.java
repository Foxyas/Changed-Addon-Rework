package net.foxyas.changedaddon.mixins.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.client.model.animations.CarryAbilityAnimation;
import net.foxyas.changedaddon.client.model.animations.MagicAttackCastingAnimator;
import net.foxyas.changedaddon.client.model.animations.PsychicGrabAbilityAnimation;
import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedArmedModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.LatexStigerModel;
import net.ltxprogrammer.changed.client.renderer.model.TorsoedModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HumanoidAnimator.class, remap = false)
public abstract class HumanoidAnimatorMixin<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> {

    @Shadow public abstract HumanoidAnimator<T, M> addAnimator(HumanoidAnimator.Animator<T, M> animator);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomAnimations(M constructorEntityModel, CallbackInfo ci) {
        this.addAnimator(new CarryAbilityAnimation<>(constructorEntityModel.getHead(), constructorEntityModel.getArm(HumanoidArm.LEFT), constructorEntityModel.getArm(HumanoidArm.RIGHT)));
        this.addAnimator(new PsychicGrabAbilityAnimation<>(constructorEntityModel.getHead(), constructorEntityModel.getArm(HumanoidArm.LEFT), constructorEntityModel.getArm(HumanoidArm.RIGHT)));
        this.addAnimator(new MagicAttackCastingAnimator<>(constructorEntityModel.getHead(), constructorEntityModel.getArm(HumanoidArm.RIGHT), constructorEntityModel.getArm(HumanoidArm.LEFT), constructorEntityModel.getLeg(HumanoidArm.RIGHT), constructorEntityModel.getLeg(HumanoidArm.LEFT), (entity) -> {
            if (!(entity instanceof Experiment009BossEntity experiment009BossEntity)) return false;
            return experiment009BossEntity.isCastingAttack();
        }));
    }
}