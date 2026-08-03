package net.foxyas.changedaddon.mixins.mods.changed;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.InputConstants;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.client.gui.AbilityRadialScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Collection;

@Mixin(value = AbilityRadialScreen.class, remap = false)
public abstract class AbilityRadialScreenMixin {

    @WrapOperation(method = "tooltipsFor", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/ability/AbstractAbilityInstance;getAbilityDescription()Ljava/util/Collection;"), remap = false)
    private Collection<Component> toolTipsHook(AbstractAbilityInstance instance, Operation<Collection<Component>> original, @Local(argsOnly = true) int section) {
        Collection<Component> descriptions = original.call(instance);
        if (ChangedAddonServerConfiguration.ALLOW_SECOND_ABILITY_USE.get()) {
            ArrayList<Component> extraDescription = new ArrayList<>(descriptions);
            InputConstants.Key mouseRightClick = InputConstants.Type.MOUSE.getOrCreate(InputConstants.MOUSE_BUTTON_RIGHT);
            extraDescription.add(Component.translatable("gui.changed_addon.abilities_radial_screen.mouse.right_click", mouseRightClick.getDisplayName()).withStyle((s) -> s.withItalic(true).withColor(ChatFormatting.BLUE)));
            return extraDescription;
        }
        return descriptions;
    }

//    @Inject(method = "tooltipsFor", at = @At("RETURN"), remap = false, cancellable = true)
//    private void toolTipsHook(int section, CallbackInfoReturnable<List<Component>> cir) {
//        if (ChangedAddonServerConfiguration.ALLOW_SECOND_ABILITY_USE.get()) {
//            List<Component> toolTips = cir.getReturnValue();
//            if (toolTips != null) {
//                List<Component> list = new ArrayList<>(toolTips);
//                AbilityRadialMenu menu = getSelf().getMenu();
//
//
//                boolean itAdded = false;
//                for (int i = 0; i < list.size(); i++) {
//                    Component component = list.get(i);
//                    AbstractAbilityInstance abilityInstance = menu.variant.getAbilityInstance(getSelf().abilities.get(section));
//
//                    if (abilityInstance != null) {
//                        ResourceLocation registryName = ChangedRegistry.ABILITY.getKey(abilityInstance.getAbility());
//                        if (registryName == null) {
//                            list.add(Component.translatable("gui.changed_addon.abilities_radial_screen.mouse.right_click").withStyle((s) -> s.withItalic(true).withColor(ChatFormatting.BLUE)));
//                            break;
//                        }
//
//                        boolean contains = component.toString().contains(registryName.toString());
//                        if (contains) {
//                            // BEFORE ID
//                            list.add(i, Component.translatable("gui.changed_addon.abilities_radial_screen.mouse.right_click").withStyle((s) -> s.withItalic(true).withColor(ChatFormatting.BLUE)));
//                            itAdded = true;
//                            break;
//                        }
//                    }
//                }
//
//                if (!itAdded)
//                    list.add(Component.translatable("gui.changed_addon.abilities_radial_screen.mouse.right_click").withStyle((s) -> s.withItalic(true).withColor(ChatFormatting.BLUE)));
//
//                cir.setReturnValue(list);
//            }
//        }
//    }

    @Unique
    private AbilityRadialScreen getSelf() {
        return ((AbilityRadialScreen) (Object) this);
    }

}
