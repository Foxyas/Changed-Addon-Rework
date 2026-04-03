package net.foxyas.changedaddon.mixins.entity.projectiles;

import net.foxyas.changedaddon.item.armor.HazardBodySuit;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.entity.projectile.GasParticle;
import net.ltxprogrammer.changed.init.ChangedAccessorySlots;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = GasParticle.class, remap = false)
public class GasParticleMixin {

    @Inject(method = "onHitEntity",
            remap = true,
            at = @At(value = "INVOKE",
                    target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;progressTransfur(Lnet/minecraft/world/entity/LivingEntity;Lnet/ltxprogrammer/changed/entity/ai/NonLatexAssimilationDecision;)Z",
                    remap = false,
                    shift = At.Shift.BY
            ),
            cancellable = true
    )
    protected void cancelTransfurDmg(EntityHitResult result, CallbackInfo ci) {
        Entity entity = result.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            Optional<AccessorySlots> slots = AccessorySlots.getForEntity(livingEntity);
            slots.ifPresent((accessorySlots) -> {
                Optional<ItemStack> item = accessorySlots.getItem(ChangedAccessorySlots.FULL_BODY.get());
                if (item.isPresent()) {
                    ItemStack itemStack = item.get();
                    if (itemStack.getItem() instanceof HazardBodySuit hazardBodySuit) {
                        if (hazardBodySuit.getClothingState(itemStack).getValue(HazardBodySuit.HELMET)) {
                            ci.cancel();
                        }
                    }
                }
            });
        }
    }
}
