package net.foxyas.changedaddon.mixins.client.renderer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(value = LivingEntityRenderer.class)
public interface LivingEntityRendererAccessor {

    @Accessor("layers")
    <T extends LivingEntity, M extends EntityModel<T>> List<RenderLayer<T, M>> getLayers();
}
