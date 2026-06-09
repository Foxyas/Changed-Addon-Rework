package net.foxyas.changedaddon.mixins.abilities;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = AbstractAbility.Controller.class, remap = false)
public interface AbilityControllerAccessor {

    @Accessor("coolDownTicksRemaining")
    int getCooldownTicksRemaining();

    @Accessor("holdTicks")
    void setHoldTicks(int holdTicks);
}
