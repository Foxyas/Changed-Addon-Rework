package net.foxyas.changedaddon.mixins.client;

import net.foxyas.changedaddon.client.model.api.IPublicRootModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.function.Function;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> extends EntityModel<T> implements IPublicRootModel {
    @Unique
    @Nullable
    protected ModelPart changed_Addon_Rework$root;

    @Inject(method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;Ljava/util/function/Function;)V", at = @At("TAIL"))
    private void setUpRoot(ModelPart pRoot, Function<ResourceLocation, RenderType> pRenderType, CallbackInfo ci) {
        this.changed_Addon_Rework$root = pRoot;
    }

    @Override
    public @Nullable ModelPart getModelRoot() {
        return changed_Addon_Rework$root;
    }
}
