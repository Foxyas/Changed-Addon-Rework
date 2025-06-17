package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.HaydenFennecFoxModel;
import net.foxyas.changedaddon.entity.HaydenFennecFoxEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class HaydenFennecFoxRenderer extends AdvancedHumanoidRenderer<HaydenFennecFoxEntity, HaydenFennecFoxModel, ArmorLatexMaleWolfModel<HaydenFennecFoxEntity>> {
    public HaydenFennecFoxRenderer(EntityRendererProvider.Context context) {
        super(context, new HaydenFennecFoxModel(context.bakeLayer(HaydenFennecFoxModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel::new, ArmorLatexMaleWolfModel.INNER_ARMOR, ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5F);
        //this.addLayer(new LatexParticlesLayer(this, this.getModel()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer(this, context.getModelSet()));
        this.addLayer(GasMaskLayer.forSnouted(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(HaydenFennecFoxEntity entity) {
        return new ResourceLocation(ChangedAddonMod.MODID,"textures/hayden_fennec_fox.png");
    }
}
