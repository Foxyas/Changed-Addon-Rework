package net.foxyas.changedaddon.client.renderer.basic;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.BlueLizardModel;
import net.foxyas.changedaddon.entity.simple.BlueLizard;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleDragonModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BlueLizardRenderer extends AdvancedHumanoidRenderer<BlueLizard, BlueLizardModel, ArmorLatexMaleDragonModel<BlueLizard>> {
    public BlueLizardRenderer(EntityRendererProvider.Context context) {
        super(context, new BlueLizardModel(context.bakeLayer(BlueLizardModel.LAYER_LOCATION)), ArmorLatexMaleDragonModel.MODEL_SET, 0.5F);
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        //this.addLayer(new CustomCoatLayer(this, (BlueLizardModel)this.getModel(), Changed.modResource("textures/green_lizard_hair")));
        //this.addLayer(new CustomEyesLayer(this, context.getModelSet()));
        this.addLayer(GasMaskLayer.forSnouted(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(BlueLizard p_114482_) {
        return new ResourceLocation(ChangedAddonMod.MODID, "textures/entities/blue_lizard.png");
    }
}
