package net.foxyas.changedaddon.client.renderer.mobs;

import net.foxyas.changedaddon.client.model.ModelProtoTypeMob;
import net.foxyas.changedaddon.entity.mobs.PrototypeEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class PrototypeRenderer extends MobRenderer<PrototypeEntity, ModelProtoTypeMob<PrototypeEntity>> {
    public PrototypeRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelProtoTypeMob(context.bakeLayer(ModelProtoTypeMob.LAYER_LOCATION)), 0.5f);
        this.addLayer(new EyesLayer<PrototypeEntity, ModelProtoTypeMob<PrototypeEntity>>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(new ResourceLocation("changed_addon:textures/entities/glowprototype.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(PrototypeEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/prototype.png");
    }
}
