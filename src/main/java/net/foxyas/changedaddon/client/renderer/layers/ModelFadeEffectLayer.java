package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import net.foxyas.changedaddon.client.particle.EntityModelFadeParticleOptions;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.function.Function;

public class ModelFadeEffectLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> {

    private final Either<Function<T, Color>, Boolean> colorFunction;

    public ModelFadeEffectLayer(RenderLayerParent<T, M> pRenderer, Either<Function<T, Color>, Boolean> colorFunction) {
        super(pRenderer);
        this.colorFunction = colorFunction;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, @NotNull T changedEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    }

    public void addFadeEffect(@NotNull T changedEntity, float pAgeInTicks) {
        Level level = changedEntity.level();

        // Map the color safely using pAgeInTicks instead of tickCount for smooth visuals
        int rgb = this.colorFunction.map(
                function -> function.apply(changedEntity).getRGB(),
                isRainbow -> isRainbow ? this.getDynamicRainbowColorInt(pAgeInTicks, 0.05f) : Color.WHITE.getRGB()
        );

        EntityModelFadeParticleOptions particleOptions = ChangedAddonParticleTypes.entityModelFade(changedEntity, rgb, 1f);
        level.addParticle(particleOptions,
                changedEntity.getX(),
                changedEntity.getY(),
                changedEntity.getZ(),
                0,
                0,
                0
        );
    }

    /**
     * Calculates a dynamic rainbow RGB integer based on a continuous float counter (FPS independent).
     */
    public int getDynamicRainbowColorInt(float ageInTicks, float speed) {
        // Sine wave calculations shifted by 120 and 240 degrees for RGB mixing
        int r = (int) (Math.sin(ageInTicks * speed + 0.0f) * 127 + 128);
        int g = (int) (Math.sin(ageInTicks * speed + 2.0f * Math.PI / 3.0f) * 127 + 128);
        int b = (int) (Math.sin(ageInTicks * speed + 4.0f * Math.PI / 3.0f) * 127 + 128);

        // Combine them into a packed 32-bit RGB integer
        return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }
}