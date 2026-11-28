package net.foxyas.changedaddon.mixins.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.client.renderer.api.LivingEntityRendererExtensor;
import net.foxyas.changedaddon.client.renderer.layers.GlowySquidDogLayer;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexSquidDog;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public abstract class GlowingSquidDogRenderer$TextureChangerMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M>, LivingEntityRendererExtensor<T, M> {

    protected GlowingSquidDogRenderer$TextureChangerMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @WrapOperation(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation injectDynamicTextures(LivingEntityRenderer<T, M> instance, Entity entity, Operation<ResourceLocation> original) {
        ResourceLocation location = original.call(instance, entity);
        if (entity instanceof AbstractLatexSquidDog abstractLatexSquidDog) {
            if (GlowySquidDogLayer.isGlowy(abstractLatexSquidDog)) {
                return GlowySquidDogLayer.getResourceLocation(abstractLatexSquidDog);
            }
        }
        return location;
    }
}
