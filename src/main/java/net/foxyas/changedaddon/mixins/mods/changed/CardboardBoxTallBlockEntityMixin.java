package net.foxyas.changedaddon.mixins.mods.changed;

import net.ltxprogrammer.changed.block.entity.CardboardBoxTallBlockEntity;
import net.ltxprogrammer.changed.block.entity.SeatableBlockEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CardboardBoxTallBlockEntity.class)
public abstract class CardboardBoxTallBlockEntityMixin implements SeatableBlockEntity {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/block/entity/CardboardBoxTallBlockEntity;getSeatedEntity()Lnet/minecraft/world/entity/LivingEntity;", shift = At.Shift.BEFORE),
            method = "hideEntity", cancellable = true)
    private void ejectLatex(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (getSeatedEntity() instanceof ChangedEntity e) {
            e.stopRiding();
            cir.cancel();
        }
    }
}
