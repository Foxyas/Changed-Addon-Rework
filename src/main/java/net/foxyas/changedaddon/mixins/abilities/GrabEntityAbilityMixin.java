package net.foxyas.changedaddon.mixins.abilities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.InputConstants;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.GrabEntityAbility;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiFunction;

@Mixin(value = GrabEntityAbility.class, remap = false)
public abstract class GrabEntityAbilityMixin extends AbstractAbility<GrabEntityAbilityInstance> {

    private GrabEntityAbilityMixin(BiFunction<AbstractAbility<GrabEntityAbilityInstance>, IAbstractChangedEntity, GrabEntityAbilityInstance> ctor) {
        super(ctor);
    }

    @ModifyReturnValue(method = "getAbilityDescription", at = @At("RETURN"))
    private Collection<Component> getToggleSafeModeDescription(Collection<Component> baseDescription, @Local(argsOnly = true) IAbstractChangedEntity entity) {
        ArrayList<Component> description = new ArrayList<>(baseDescription);

        // 1. Get the Key instances for mouse buttons
        InputConstants.Key mouseRightClick = InputConstants.Type.MOUSE.getOrCreate(InputConstants.MOUSE_BUTTON_RIGHT);
        InputConstants.Key middleMouseButton = InputConstants.Type.MOUSE.getOrCreate(InputConstants.MOUSE_BUTTON_MIDDLE);

        // 2. Get their translated Component representations
        Component rightClickName = mouseRightClick.getDisplayName();
        Component middleButtonName = middleMouseButton.getDisplayName();

        // 3. Pass the translated components into your translation key
        description.add(Component.translatable(
                "ability.changed.grab_entity.desc.toggle_grab_safe_mode",
                rightClickName,
                middleButtonName
        ));

        return description;
    }
}
