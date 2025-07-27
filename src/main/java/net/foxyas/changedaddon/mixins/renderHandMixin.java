package net.foxyas.changedaddon.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.foxyas.changedaddon.ChangedAddonTags;
import net.foxyas.changedaddon.abilities.CarryAbility;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.ltxprogrammer.changed.client.renderer.model.CorrectorType;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.ltxprogrammer.changed.client.FormRenderHandler.renderModelPartWithTexture;

@Mixin(value = FormRenderHandler.class, remap = false)
public class renderHandMixin {

    @Inject(method = "renderHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IFZ)V",
            at = @At("TAIL"), cancellable = true)
    private static void renderBothHand(LivingEntity living, HumanoidArm arm, PoseStack stack, MultiBufferSource buffer, int light, float partialTick, boolean layers, CallbackInfo ci) {
        if (!(living instanceof ChangedEntity changedEntity) || changedEntity.getUnderlyingPlayer() == null) {
            return;
        }

        Player player = changedEntity.getUnderlyingPlayer();
        TransfurVariantInstance<?> variantInstance = ProcessTransfur.getPlayerTransfurVariant(player);

        if (!shouldRenderBothHands(player, variantInstance)) {
            return;
        }

        EntityRenderer<? super LivingEntity> entRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(living);
        if (!(entRenderer instanceof LivingEntityRenderer<?, ?> livingRenderer) || !(livingRenderer instanceof AdvancedHumanoidRenderer<?, ?, ?> advRenderer)) {
            return;
        }

        AdvancedHumanoidModel model = advRenderer.getModel(changedEntity);
        AdvancedHumanoidModelInterface modelInterface = (AdvancedHumanoidModelInterface<?,?>) model;

        ModelPart handPart = model.getArm(arm.getOpposite());
        model.setupAnim(changedEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        modelInterface.setupHand(changedEntity);

        PoseStack stackCorrector = modelInterface.getPlacementCorrectors(CorrectorType.fromArm(arm.getOpposite()));
        stack.pushPose();
        stack.mulPose(new Quaternion(-35.0F, 130.0F, -6.0F, true)); // Rotação
        handPart.setPos(5.325F, 2.5F, 4.5F);
        //handPart.setRotation(DEBUG.HeadPosX * Mth.DEG_TO_RAD, DEBUG.HeadPosY * Mth.DEG_TO_RAD, DEBUG.HeadPosZ * Mth.DEG_TO_RAD);

        ResourceLocation texture = entRenderer.getTextureLocation(changedEntity);
        renderModelPartWithTexture(handPart, stackCorrector, stack, buffer.getBuffer(RenderType.entityCutout(texture)), light, 1.0F);
        stack.popPose();
    }


    /**
     * Determine if both hands should be rendered based on the player's state and abilities.
     */
    @Unique
    private static boolean shouldRenderBothHands(Player player, TransfurVariantInstance<?> variantInstance) {
        if (variantInstance == null) {
            return false;
        }

        if (!ChangedAddonClientConfiguration.SHOW_EXTRA_HAND.get()){
            return false;
        }

        // Check if the player is gliding with a variant that can glide and has sufficient speed
        if (variantInstance.getParent().canGlide && player.isFallFlying()) {
            double speed = player.getDeltaMovement().length(); // Velocidade do jogador
            if (speed > 1.5 || player.getFallFlyingTicks() >= 5) {
                return true;
            }
        }

        // Check if the player has the Carry ability and is carrying a valid entity
        if (variantInstance.hasAbility(ChangedAddonAbilities.CARRY.get())
                && variantInstance.selectedAbility == ChangedAddonAbilities.CARRY.get()) {
            CarryAbility carryAbility = (CarryAbility) variantInstance.getAbilityInstance(ChangedAddonAbilities.CARRY.get()).ability;
            Entity carryTarget = carryAbility.CarryTarget(player);

            if (carryTarget instanceof LivingEntity) {
                boolean isHumanoidOrTagValid = carryTarget.getType().is(ChangedTags.EntityTypes.HUMANOIDS) ||
                        carryTarget.getType().is(ChangedAddonTags.EntityTypes.CAN_CARRY);

                return isHumanoidOrTagValid && (player.getFirstPassenger() == null || player.getFirstPassenger() != carryTarget);
            }
        }

        return false;
    }
}
