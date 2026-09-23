package net.foxyas.changedaddon.mixins.abilities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.InputConstants;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.GrabEntityAbility;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
        InputConstants.Key shift = InputConstants.Type.KEYSYM.getOrCreate(InputConstants.KEY_LSHIFT);
        InputConstants.Key mouseRightClick = InputConstants.Type.MOUSE.getOrCreate(InputConstants.MOUSE_BUTTON_RIGHT);
        InputConstants.Key middleMouseButton = InputConstants.Type.MOUSE.getOrCreate(InputConstants.MOUSE_BUTTON_MIDDLE);

        // 2. Get their translated Component representations
        Component shiftName = shift.getDisplayName();
        Component rightClickName = mouseRightClick.getDisplayName();
        Component middleButtonName = middleMouseButton.getDisplayName();

        // 3. Pass the translated components into your translation key
        description.add(Component.translatable(
                "ability.changed.grab_entity.desc.toggle_grab_safe_mode",
                rightClickName,
                middleButtonName
        ));

        description.add(Component.translatable(
                "ability.changed.grab_entity.desc.toggle_grab_transfur_damage_mode",
                shiftName,
                rightClickName,
                middleButtonName
        ));

        return description;
    }

    @ModifyReturnValue(method = "getControllingEntity", at = @At("RETURN"))
    private static LivingEntity getControllingEntityHook(LivingEntity original, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player && !player.isSpectator() && player.level().isClientSide()) {
            TransfurVariantInstance<?> variantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (variantInstance instanceof TransfurVariantInstanceExtensor instanceExtensor) {
                ChangedEntity changedEntityInControl = instanceExtensor.getChangedEntityInControl();
                if (!instanceExtensor.hasControlOverBody() && changedEntityInControl != null) {
                    return changedEntityInControl;
                }
            }
        }
        return original;
    }

    @ModifyReturnValue(method = "isEntityNoControl", at = @At("RETURN"))
    private static boolean isEntityNoControlHook(boolean original, Entity entity) {
        if (entity instanceof Player player && !player.isSpectator()) {
            TransfurVariantInstance<?> variantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (variantInstance instanceof TransfurVariantInstanceExtensor instanceExtensor) {
                if (!instanceExtensor.hasControlOverBody()) {
                    return true;
                }
            }
        }
        return original;
    }
}
