package net.foxyas.changedaddon.mixins.client.renderer;

import net.foxyas.changedaddon.client.renderer.layers.GlowySquidDogLayer;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.LatexSquidDogFemaleRenderer;
import net.ltxprogrammer.changed.client.renderer.LatexSquidDogMaleRenderer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModelPicker;
import net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AdvancedHumanoidRenderer.class, remap = false)
public abstract class GlowingSquidDogRendererMixin<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> extends MobRenderer<T, M> {
    public GlowingSquidDogRendererMixin(EntityRendererProvider.Context pContext, M pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }


    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/ltxprogrammer/changed/client/renderer/model/AdvancedHumanoidModel;Lnet/ltxprogrammer/changed/client/renderer/model/armor/ArmorModelPicker;F)V", at = @At("TAIL"))
    private void addCustomLayers(EntityRendererProvider.Context context, AdvancedHumanoidModel<T> main, ArmorModelPicker<T, ? extends LatexHumanoidArmorModel<? super T, ?>> modelPicker, float shadowSize, CallbackInfo ci) {
        AdvancedHumanoidRenderer self = (AdvancedHumanoidRenderer) (Object) this;
        if (self instanceof LatexSquidDogMaleRenderer renderer) {
            renderer.addLayer(new GlowySquidDogLayer<>(renderer));
        } else if (self instanceof LatexSquidDogFemaleRenderer renderer) {
            renderer.addLayer(new GlowySquidDogLayer<>(renderer));
        }
    }
}