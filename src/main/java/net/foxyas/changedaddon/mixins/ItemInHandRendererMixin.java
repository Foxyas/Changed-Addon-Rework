package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.ability.CarryAbility;
import net.foxyas.changedaddon.ability.ChangedAddonAbilitys;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Inject(method = "evaluateWhichHandsToRender", at = @At("HEAD"),cancellable = true)
    private static void BothHandsRender(LocalPlayer localPlayer, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> cir) {
        var LocalPlayerVariant = ProcessTransfur.getPlayerTransfurVariant(localPlayer);
        if (LocalPlayerVariant != null){
            LocalPlayerVariant.ifHasAbility(ChangedAddonAbilitys.CARRY.get(), (abilityInstance) -> {
                if (abilityInstance.ability instanceof CarryAbility carryAbility && carryAbility.CarryTarget(localPlayer) != null){
                    cir.setReturnValue(ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS);
                }
            });
        }
    }
}
