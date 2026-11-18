package net.foxyas.changedaddon.mixins.abilities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.process.GrabExtraDescriptionHandler;
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

        if (entity.getLevel().isClientSide()) {
            GrabExtraDescriptionHandler.addExtraDescriptions(description, entity, baseDescription);
        }

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
