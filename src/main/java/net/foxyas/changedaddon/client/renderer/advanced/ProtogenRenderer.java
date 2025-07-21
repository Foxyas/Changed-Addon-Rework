
package net.foxyas.changedaddon.client.renderer.advanced;

import net.foxyas.changedaddon.client.model.advanced.ProtogenModel;
import net.foxyas.changedaddon.client.renderer.layers.CustomDisplay;
import net.foxyas.changedaddon.client.renderer.layers.ProtogenDisplay;
import net.foxyas.changedaddon.entity.advanced.ProtogenEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ProtogenRenderer extends AdvancedHumanoidRenderer<ProtogenEntity, ProtogenModel, ArmorLatexMaleWolfModel<ProtogenEntity>> {
    public ProtogenRenderer(EntityRendererProvider.Context context) {
        super(context, new ProtogenModel(context.bakeLayer(ProtogenModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel::new, ArmorLatexMaleWolfModel.INNER_ARMOR, ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
        this.addLayer(new ProtogenDisplay<>(this, getModel(),
                new ResourceLocation("changed_addon:textures/entities/protogen/protogen_eyes_display.png"),
                new ResourceLocation("changed_addon:textures/entities/protogen/protogen_display.png")));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(GasMaskLayer.forLargeSnouted(this, context.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ProtogenEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/protogen/protogen.png");
    }
}
