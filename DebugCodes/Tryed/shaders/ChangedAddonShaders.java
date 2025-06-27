package net.foxyas.changedaddon.client.renderer.shaders;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonShaders {

    public static class PrefixedShaderShard extends RenderStateShard.ShaderStateShard {
        private final Consumer<ShaderInstance> onBindShader;

        public PrefixedShaderShard(Supplier<ShaderInstance> shader, Consumer<ShaderInstance> onBindShader) {
            super(shader);
            this.onBindShader = onBindShader;
        }

        @Override
        public void setupRenderState() {
            super.setupRenderState();
            onBindShader.accept(RenderSystem.getShader());
        }
    }

    public static Optional<Uniform> getUniform(ShaderInstance shader, String name) {
        return Optional.ofNullable(shader.getUniform(name));
    }


    @SubscribeEvent
    public static void initShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(
                        event.getResourceManager(),
                        ResourceLocation.parse(ChangedAddonMod.MODID, "light_ray"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shader -> lightRayShaders = shader
        );
        ChangedAddonMod.LOGGER.info("The Event Got called and the shader is now " + (lightRayShaders == null ? "null" : "not null"));
    }

    public static ShaderInstance lightRayShaders;

    public static @NotNull ShaderInstance getRenderTypeLightRayShader() {
        return Objects.requireNonNull(lightRayShaders, "Attempted to call getRenderTypeLightRayShader before shaders have finished loading.");
    }

    protected static final RenderStateShard.ShaderStateShard LIGHT_RAY_SHADER = new RenderStateShard.ShaderStateShard(ChangedAddonShaders::getRenderTypeLightRayShader);
}
