package net.foxyas.changedaddon.mixins.mods.changed;

import net.ltxprogrammer.changed.block.entity.CardboardBoxTallBlockEntity;
import net.ltxprogrammer.changed.block.entity.SeatableBlockEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CardboardBoxTallBlockEntity.class)
@Deprecated
public abstract class CardboardBoxTallBlockEntityMixin implements SeatableBlockEntity {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/block/entity/CardboardBoxTallBlockEntity;getSeatedEntity()Lnet/minecraft/world/entity/LivingEntity;", shift = At.Shift.BEFORE),
            method = "hideEntity", remap = false, cancellable = true)
    private void ejectCurrentHiddenEntity(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity seatedEntity = getSeatedEntity();
        if (seatedEntity != null) {
            seatedEntity.stopRiding();
            cir.cancel();
        }
//        if (seatedEntity instanceof ChangedEntity e) {
//            e.stopRiding();
//            cir.cancel();
//        }
    }
}
