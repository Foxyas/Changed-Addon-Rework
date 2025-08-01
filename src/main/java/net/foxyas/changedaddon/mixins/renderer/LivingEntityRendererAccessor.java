package net.foxyas.changedaddon.mixins.renderer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = LivingEntityRenderer.class)
public interface LivingEntityRendererAccessor {

    @Accessor("layers")
    List<RenderLayer<LivingEntity, EntityModel<LivingEntity>>> getLayers();
}
