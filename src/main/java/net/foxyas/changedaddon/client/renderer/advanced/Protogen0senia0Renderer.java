package net.foxyas.changedaddon.client.renderer.advanced;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.advanced.Protogen0senia0Model;
import net.foxyas.changedaddon.client.model.armors.ArmorProtogen0senia0;
import net.foxyas.changedaddon.client.renderer.layers.ProtogenDisplay;
import net.foxyas.changedaddon.entity.advanced.Protogen0senia0Entity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Protogen0senia0Renderer extends AdvancedHumanoidRenderer<Protogen0senia0Entity, Protogen0senia0Model> {

    public Protogen0senia0Renderer(EntityRendererProvider.Context context) {
        super(context, new Protogen0senia0Model(context.bakeLayer(Protogen0senia0Model.LAYER_LOCATION)), ArmorProtogen0senia0.ARMOR, 0.5f);
        this.addLayer(new ProtogenDisplay<>(this, getModel(),
                ChangedAddonMod.textureLoc("textures/entities/0senia0/protogen_display_eyes"),
                ChangedAddonMod.textureLoc("textures/entities/0senia0/protogen_display")) {
            @Override
            public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull Protogen0senia0Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (!entity.isInvisible()) {
                    Color3 displayColor = Color3.WHITE;  // Cor do display
                    Color3 eyeColor = Color3.WHITE;    // Cor dos olhos
                    int overlay = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);

                    this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(getNormalDisplayRender()), packedLight, overlay, displayColor.red(), displayColor.green(), displayColor.blue(), 1.0F);
                    this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(getGlowEyeRender()), packedLight, overlay, eyeColor.red(), eyeColor.green(), eyeColor.blue(), 1.0F);
                }
            }
        });
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(GasMaskLayer.forLargeSnouted(this, context.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Protogen0senia0Entity entity) {
        return ChangedAddonMod.textureLoc("textures/entities/0senia0/0senia0_skin");
    }
}
