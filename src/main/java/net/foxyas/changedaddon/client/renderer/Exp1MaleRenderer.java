package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.MaleExp1Model;
import net.foxyas.changedaddon.entity.Exp1MaleEntity;
import net.foxyas.changedaddon.entity.Exp1MaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class Exp1MaleRenderer extends AdvancedHumanoidRenderer<Exp1MaleEntity, MaleExp1Model, ArmorLatexMaleWolfModel<Exp1MaleEntity>> {
	public Exp1MaleRenderer(EntityRendererProvider.Context context) {
		super(context, new MaleExp1Model(context.bakeLayer(MaleExp1Model.LAYER_LOCATION)),ArmorLatexMaleWolfModel::new,ArmorLatexMaleWolfModel.INNER_ARMOR,ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new LatexParticlesLayer<>(this, getModel()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(Exp1MaleEntity entity) {
		return ResourceLocation.parse("changed_addon:textures/entities/latex_snowfox_male_new.png");
	}
}