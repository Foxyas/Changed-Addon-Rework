package net.foxyas.changedaddon.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.entity.LivingEntityDataExtension;
import net.ltxprogrammer.changed.entity.UseItemMode;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.item.SpecializedAnimations;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.UniversalDist;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class DontRenderHandMixin {

    @Inject(method = "renderHandsWithItems", at = @At("HEAD"), cancellable = true)
    public void renderHandsWithItems(float partialTicks, PoseStack pose, MultiBufferSource.BufferSource bufferSource, LocalPlayer player, int color, CallbackInfo callback) {
        ProcessTransfur.ifPlayerTransfurred(player, variant -> {
            if (variant.is(ChangedAddonTransfurVariants.LATEX_SNEP.get()) || variant.is(ChangedAddonTransfurVariants.LATEX_SNEP_FERAL_FORM.get())){
                callback.cancel();
            }
            bufferSource.endBatch();
        });
    }
}
