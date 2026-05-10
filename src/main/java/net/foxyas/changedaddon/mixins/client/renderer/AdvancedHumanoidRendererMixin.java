package net.foxyas.changedaddon.mixins.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexHumanoidArmorLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.NoSuchElementException;

@Mixin(value = AdvancedHumanoidRenderer.class, remap = false)
public abstract class AdvancedHumanoidRendererMixin {

    @Shadow
    private LatexHumanoidArmorLayer<ChangedEntity, AdvancedHumanoidModel<ChangedEntity>> armorLayer;

    @Shadow
    public abstract AdvancedHumanoidModel<ChangedEntity> getModel(ChangedEntity entity);

    @Inject(method = "render(Lnet/ltxprogrammer/changed/entity/ChangedEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("TAIL"))
    private void turnOffPlantoids(ChangedEntity entity, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        var torso = this.getModel(entity).getTorso();
        try {
            ModelPart plantoidsPart = torso.getChild("Plantoids");
            plantoidsPart.visible = !ChangedAddonClientConfiguration.PLANTOIDS_VISIBILITY.get();
        } catch (NoSuchElementException e) {
            // A parte "Plantoids" não existe no modelo principal, ignoramos
        }

        if (this.armorLayer != null) {
            try {
                var armorTorso = this.armorLayer.getArmorModel(entity, EquipmentSlot.CHEST).getTorso();
                ModelPart armorPlantoidsPart = armorTorso.getChild("Plantoids");
                armorPlantoidsPart.visible = !ChangedAddonClientConfiguration.PLANTOIDS_VISIBILITY.get();
            } catch (NoSuchElementException ignored) {
            }
        }
    }
}


