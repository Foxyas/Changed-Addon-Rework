package net.foxyas.changedaddon.mixins.client.renderer.layer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.client.renderer.layers.api.IDynamicRenderLayer;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.client.LivingEntityRendererExtender;
import net.ltxprogrammer.changed.client.renderer.layers.LatexHeldEntityLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LatexHeldEntityLayer.class, remap = false)
public abstract class LatexHeldEntityLayerMixin<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> extends RenderLayer<T, M> implements IDynamicRenderLayer<T> {

    private boolean isLater = false;

    @Shadow
    public abstract void render(@NotNull PoseStack pose, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    public LatexHeldEntityLayerMixin(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.BEFORE),
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFFF)V",
            remap = true,
            cancellable = true)
    private void delaySameModel(PoseStack pose, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci, @Local(name = "ability") GrabEntityAbilityInstance ability) {
        //if (!((GrabEntityAbilityExtensor) ability).isSafeMode()) return;
        // lest just keep the "is same renderer" check for now.
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (!dispatcher.getRenderer(entity).equals(dispatcher.getRenderer(ability.grabbedEntity))) // grabbedEntity is never null in this context you can safely ignore the "null point" warning
            return;
        if (!isLater) {
            ci.cancel();
        }
    }

//    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.BEFORE),
//            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFFF)V",
//            cancellable = true)
//    private void delaySameModel(PoseStack pose, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci, @Local(name = "ability") GrabEntityAbilityInstance ability) {
//        if (!((GrabEntityAbilityExtensor) ability).isSafeMode()) return;
//        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
//        if (!dispatcher.getRenderer(entity).equals(dispatcher.getRenderer(ability.grabbedEntity))) return; // grabbedEntity is never null in this context you can safely ignore the "null point" warning
//
//        if (!ClientVars.delayedHeldEntityRender) {
//        ci.cancel();
//            ClientVars.delayedHeldEntityRender = true;
//        } else ClientVars.delayedHeldEntityRender = false;
//    }


    // Small notice: this is a VERY risky method if the "isLater" is not false after the call it will generate a stackOverFlow error.
    @Override
    public void renderAfter(PoseStack pose, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.isLater = true;
        // The call of this$render here is the correct way to handle due the other mixin injects.
        this.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        this.isLater = false; // NEVER CHANGE THIS TO TRUE.
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V"), index = 2,
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFFF)V",
            remap = true)
    private double moveHeldEntityCloser(double z, @Local(name = "ability") GrabEntityAbilityInstance grab, @Local(argsOnly = true, ordinal = 2) float partialTick) {
        if (!ChangedAddonClientConfiguration.SUIT_ANIM.get() || grab.suited || ((GrabEntityAbilityExtensor) grab).isSafeMode())
            return z;

        return Mth.lerp(grab.getSuitTransitionProgress(partialTick), -0.28125F, -0.05);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V", shift = At.Shift.AFTER),
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFFF)V",
            remap = true
    )
    private void flipHeldEntityForCuddle(PoseStack pose, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci, @Local(name = "ability") GrabEntityAbilityInstance ability) {
        if (!((GrabEntityAbilityExtensor) ability).isSafeMode()) return;

        Player player = entity.getUnderlyingPlayer();
        if (player == null || !player.isSleeping()) return;

        pose.mulPose(Axis.YP.rotationDegrees(180));
    }

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    shift = At.Shift.AFTER
            ),
            remap = true
    )
    private void scaleAfterPush(
            PoseStack pose, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci
    ) {
        if (entity instanceof IAlphaAbleEntity alpha && alpha.isAlpha() && alpha.alphaAdditionalScale() > 0) {
            float reduction = (1 / alpha.alphaScaleForRender());
            pose.scale(reduction, reduction, reduction);
            pose.translate(0, 0.3 * Math.min(1, alpha.alphaScalePercent()), 0);
        }
    }

    @WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;getRenderer(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;", remap = true))
    private EntityRenderer<? super T> mayGetUnderLyingEntityRender(EntityRenderDispatcher instance, Entity entity, Operation<EntityRenderer<? super T>> original) {
        if (entity instanceof LivingEntity livingEntity)
            return original.call(instance, EntityUtil.maybeGetOverlaying(livingEntity));
        return original.call(instance, entity);
    }

    @WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/client/LivingEntityRendererExtender;directRender(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void mayGetUnderLyingEntityRender(LivingEntityRendererExtender<?, ?> instance, LivingEntity entity, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, Operation<Void> original) {
        original.call(instance, EntityUtil.maybeGetOverlaying(entity), yRot, partialTicks, poseStack, bufferSource, light);
    }
}
