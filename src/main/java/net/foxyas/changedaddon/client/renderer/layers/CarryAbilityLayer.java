package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.ability.ChangedAddonAbilitys;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.LatexHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.LatexHumanoidModelInterface;
import net.ltxprogrammer.changed.entity.LatexEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Items;

public class CarryAbilityLayer<M extends LatexHumanoidModel<T> & LatexHumanoidModelInterface<T,M>, T extends LatexEntity> extends RenderLayer<T, M> implements FirstPersonLayer<T> {
    private final M model;
    private final ResourceLocation texture;

    public CarryAbilityLayer(RenderLayerParent<T, M> parent, M model, ResourceLocation textureBase) {
        super(parent);
        this.model = model;
        this.texture = textureBase;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        // Não renderiza nada aqui, pois este Layer é só para as mãos em primeira pessoa
    }

    @Override
    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PoseStack stackCorrector) {
        this.getModel().setupAnim(entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        model.setupHand();

        if (shouldRenderHands(entity)) {
            // Renderiza ambas as mãos
            //renderHand(stack, bufferSource, packedLight, entity, HumanoidArm.RIGHT, stackCorrector);
            renderHand(stack, bufferSource, packedLight, entity, getOppositeArm(arm), stackCorrector);
        }
    }

    private boolean shouldRenderHands(T entity) {
        return entity.getUnderlyingPlayer() != null &&
                ProcessTransfur.getPlayerLatexVariant(entity.getUnderlyingPlayer()) != null &&
                ProcessTransfur.getPlayerLatexVariant(entity.getUnderlyingPlayer()).selectedAbility == ChangedAddonAbilitys.CARRY.get() &&
                entity.getItemInHand(InteractionHand.MAIN_HAND).is(Items.AIR) &&
                entity.getItemInHand(InteractionHand.OFF_HAND).is(Items.AIR);
    }

    private void renderHand(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PoseStack stackCorrector) {
        stack.pushPose();
        if(arm == HumanoidArm.RIGHT){
            stack.scale(1.0002F, 1.0002F, 1.0002F);
        } else if (arm == HumanoidArm.LEFT) {
            stack.scale(1.0002F, 1.0002F, 1.0002F);
            stack.translate(0.0f, 0.0f, 4.0f);
        }


        FormRenderHandler.renderModelPartWithTexture(this.getArm(arm), stackCorrector, stack, bufferSource.getBuffer(this.renderType()), packedLight, 1F);
        stack.popPose();
    }

    public LatexHumanoidModel<T> getModel() {
        return model;
    }

    public RenderType renderType() {
        return RenderType.entityCutoutNoCull(texture);
    }

    public ModelPart getArm(HumanoidArm arm) {
        return model.getArm(arm);
    }

	private HumanoidArm getOppositeArm(HumanoidArm arm) {
		return arm == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
	}
}
