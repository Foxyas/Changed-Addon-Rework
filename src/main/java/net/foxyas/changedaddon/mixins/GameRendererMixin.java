/*
package net.foxyas.changedaddon.mixins;

import com.mojang.blaze3d.shaders.Uniform;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.List;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private PostChain changed_Addon_Rework$colorblindChain;

    @Unique
    private int changed_Addon_Rework$prevWidth = -1, changed_Addon_Rework$prevHeight = -1;


    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    private void verdant$addColorblindFilter(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {
        if (this.changed_Addon_Rework$colorblindChain == null) {
            changed_Addon_Rework$loadShaders();
        }

        int w = this.minecraft.getMainRenderTarget().width;
        int h = this.minecraft.getMainRenderTarget().height;


        if (w != changed_Addon_Rework$prevWidth || h != changed_Addon_Rework$prevHeight) {
            this.changed_Addon_Rework$colorblindChain.resize(w, h);
            changed_Addon_Rework$prevWidth = w;
            changed_Addon_Rework$prevHeight = h;
        }

        Player player = this.minecraft.player;
        if (player != null && player.getMainHandItem().is(Items.DEBUG_STICK)) {
            if (this.changed_Addon_Rework$colorblindChain instanceof PostChainMixin postChainMixin){
                List<PostPass> postPassList = postChainMixin.getPasses();
                if (postPassList != null && !postPassList.isEmpty()){
                    for (PostPass postPass : postPassList) {
                        Uniform uniform = postPass.getEffect().getUniform("Saturation");
                        if (uniform != null) {
                           if (player.isShiftKeyDown()) {
                               uniform.set(2f);
                           } else {
                               uniform.set(0f);
                           }
                        }
                    }
                }

            }

            this.changed_Addon_Rework$colorblindChain.process(partialTicks);
        }
    }

    @Unique
    private void changed_Addon_Rework$loadShaders() {
        try {
            this.changed_Addon_Rework$colorblindChain = new PostChain(
                    this.minecraft.getTextureManager(),
                    this.minecraft.getResourceManager(),
                    this.minecraft.getMainRenderTarget(),
                    new ResourceLocation(ChangedAddonMod.MODID, "shaders/post/vision_coloring.json")
            );
            this.changed_Addon_Rework$colorblindChain.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/