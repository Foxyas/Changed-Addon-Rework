package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.ability.ChangedAddonAbilitys;
import net.foxyas.changedaddon.client.renderer.layers.animation.CarryAbilityAnimation;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AdvancedHumanoidModel.class,remap = false)
public class AnimationsMixin {
    @Inject(method = "setupAnim(Lnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFF)V",at = @At("HEAD"),cancellable = true)
    private void addCustomAnimation(
            @NotNull ChangedEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
        if (entity.getUnderlyingPlayer() != null && ProcessTransfur.getPlayerTransfurVariant(entity.getUnderlyingPlayer()) != null
                && ProcessTransfur.getPlayerTransfurVariant(entity.getUnderlyingPlayer()).hasAbility(ChangedAddonAbilitys.CARRY.get())){
            CarryAbilityAnimation.playAnimation(entity,(AdvancedHumanoidModel<?>) (Object) this);
        }
        if (entity.getSelfVariant() != null && entity.getSelfVariant().abilities.stream().anyMatch(entityTypeFunction -> {
            // Supondo que a função aceite algum EntityType e você esteja verificando seu resultado:
            AbstractAbility<?> ability = entityTypeFunction.apply(entity.getType());
            return ability != null && ability.equals(ChangedAddonAbilitys.CARRY.get());
        })) {
            CarryAbilityAnimation.playAnimation(entity, (AdvancedHumanoidModel<?>) (Object) this);
        }
    }
}