package net.foxyas.changedaddon.mixins;

import com.mojang.blaze3d.shaders.Uniform;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.process.variantsExtraStats.visions.TransfurVariantVision;
import net.foxyas.changedaddon.process.variantsExtraStats.visions.TransfurVariantVisions;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
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
public abstract class GameRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Unique
    private TransfurVariantVision changed_Addon_Rework$transfurVariantVision;

    @Shadow
    public abstract void render(float p_109094_, long p_109095_, boolean p_109096_);

    @Unique
    private PostChain changed_Addon_Rework$colorblindChain;

    //@Unique
    //private PostChain changed_Addon_Rework$lightBloomEffectChain;

    @Unique
    private int changed_Addon_Rework$prevWidth = -1, changed_Addon_Rework$prevHeight = -1;


    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    private void verdant$addColorblindFilter(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {
        Player player = this.minecraft.player;
        if (player != null) {
            if (changed_Addon_Rework$ShouldWork(player)) {
                if (this.changed_Addon_Rework$colorblindChain == null) {
                    changed_Addon_Rework$loadShaders();
                } else {
                    int w = this.minecraft.getMainRenderTarget().width;
                    int h = this.minecraft.getMainRenderTarget().height;
                    if (w != changed_Addon_Rework$prevWidth || h != changed_Addon_Rework$prevHeight) {
                        this.changed_Addon_Rework$colorblindChain.resize(w, h);
                        changed_Addon_Rework$prevWidth = w;
                        changed_Addon_Rework$prevHeight = h;
                    }

                    this.changed_Addon_Rework$colorblindChain.process(partialTicks);
                }
            } /*else if (player.getMainHandItem().is(Items.DEBUG_STICK)) {
                if (this.changed_Addon_Rework$lightBloomEffectChain == null) {
                    changed_Addon_Rework$loadLightBloomShader();
                } else {
                    int w = this.minecraft.getMainRenderTarget().width;
                    int h = this.minecraft.getMainRenderTarget().height;
                    if (w != changed_Addon_Rework$prevWidth || h != changed_Addon_Rework$prevHeight) {
                        this.changed_Addon_Rework$lightBloomEffectChain.resize(w, h);
                        changed_Addon_Rework$prevWidth = w;
                        changed_Addon_Rework$prevHeight = h;
                    }

                    this.changed_Addon_Rework$lightBloomEffectChain.process(partialTicks);
                }
            }*/
        }
    }

    @Unique
    private boolean changed_Addon_Rework$ShouldWork(Player player) {
        TransfurVariantInstance<?> variantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (variantInstance != null) {
            this.changed_Addon_Rework$transfurVariantVision = TransfurVariantVisions.getTransfurVariantVision(player.getLevel(), variantInstance.getFormId());
            return TransfurVariantVisions.getTransfurVariantVision(player.getLevel(), variantInstance.getFormId()) != null;
        }


        return false;
    }

    @Unique
    private void ignore(Player player) {
        if (this.changed_Addon_Rework$colorblindChain instanceof PostChainMixin postChainMixin) {
            List<PostPass> postPassList = postChainMixin.getPasses();
            if (postPassList != null && !postPassList.isEmpty()) {
                for (PostPass postPass : postPassList) {
                    Uniform uniform = postPass.getEffect().getUniform("Saturation");
                    Uniform uniform2 = postPass.getEffect().getUniform("Contrast");
                    if (uniform != null) {
                        if (player.isShiftKeyDown()) {
                            uniform.set(1);
                        } else {
                            uniform.set(2);
                        }
                    }
                    if (uniform2 != null) {
                        if (player.isShiftKeyDown()) {
                            uniform2.set(1);
                        } else {
                            uniform2.set(2);
                        }
                    }
                }
            }

        }
    }

    @Unique
    private void changed_Addon_Rework$loadShaders() {
        try {
            this.changed_Addon_Rework$colorblindChain = new PostChain(
                    this.minecraft.getTextureManager(),
                    this.minecraft.getResourceManager(),
                    this.minecraft.getMainRenderTarget(),
                    new ResourceLocation(changed_Addon_Rework$transfurVariantVision.getVisionEffect().toString())
            );
            this.changed_Addon_Rework$colorblindChain.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*@Unique
    private void changed_Addon_Rework$loadLightBloomShader() {
        try {
            this.changed_Addon_Rework$lightBloomEffectChain = new PostChain(
                    this.minecraft.getTextureManager(),
                    this.minecraft.getResourceManager(),
                    this.minecraft.getMainRenderTarget(),
                    new ResourceLocation(ChangedAddonMod.MODID, "shaders/post/light_bloom.json")
            );
            this.changed_Addon_Rework$lightBloomEffectChain.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}