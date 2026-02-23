package net.foxyas.changedaddon.mixins.mods.changed;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.client.gui.AbilityRadialScreen;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.world.inventory.AbilityRadialMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = AbilityRadialScreen.class, remap = false)
public abstract class AbilityRadialScreenMixin {

    @Inject(method = "tooltipsFor", at = @At("RETURN"), remap = false, cancellable = true)
    private void toolTipsHook(int section, CallbackInfoReturnable<List<Component>> cir) {
        if (ChangedAddonServerConfiguration.ALLOW_SECOND_ABILITY_USE.get()) {
            List<Component> toolTips = cir.getReturnValue();
            if (toolTips != null) {
                List<Component> list = new ArrayList<>(toolTips);
                AbilityRadialMenu menu = getSelf().getMenu();


                boolean itAdded = false;
                for (int i = 0; i < list.size(); i++) {
                    Component component = list.get(i);
                    AbstractAbilityInstance abilityInstance = menu.variant.getAbilityInstance(getSelf().abilities.get(section));

                    if (abilityInstance != null) {
                        ResourceLocation registryName = ChangedRegistry.ABILITY.getKey(abilityInstance.getAbility());
                        if (registryName == null) {
                            list.add(Component.translatable("gui.changed_addon.abilities_radial_screen.mouse.right_click").withStyle((s) -> s.withItalic(true).withColor(ChatFormatting.BLUE)));
                            break;
                        }

                        boolean contains = component.toString().contains(registryName.toString());
                        if (contains) {
                            // BEFORE ID
                            list.add(i, Component.translatable("gui.changed_addon.abilities_radial_screen.mouse.right_click").withStyle((s) -> s.withItalic(true).withColor(ChatFormatting.BLUE)));
                            itAdded = true;
                            break;
                        }
                    }
                }

                if (!itAdded)
                    list.add(Component.translatable("gui.changed_addon.abilities_radial_screen.mouse.right_click").withStyle((s) -> s.withItalic(true).withColor(ChatFormatting.BLUE)));

                cir.setReturnValue(list);
            }
        }
    }

    @Unique
    private AbilityRadialScreen getSelf() {
        return ((AbilityRadialScreen) (Object) this);
    }

}
