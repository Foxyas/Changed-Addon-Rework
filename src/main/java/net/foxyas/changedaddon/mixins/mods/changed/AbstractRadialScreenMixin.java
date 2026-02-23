package net.foxyas.changedaddon.mixins.mods.changed;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.network.packet.VariantSecondAbilityActivate;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.client.gui.AbilityRadialScreen;
import net.ltxprogrammer.changed.client.gui.AbstractRadialScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Optional;

@Mixin(value = AbstractRadialScreen.class, remap = false)
public abstract class AbstractRadialScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public AbstractRadialScreenMixin(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Shadow
    public abstract Optional<Integer> getSectionAt(int mouseX, int mouseY);

    @Inject(method = "mouseClicked", at = @At("HEAD"), remap = true, cancellable = true)
    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!ChangedAddonServerConfiguration.ALLOW_SECOND_ABILITY_USE.get()
                || button != GLFW.GLFW_MOUSE_BUTTON_RIGHT) return;

        Optional<Integer> section = this.getSectionAt((int) mouseX, (int) mouseY);
        if (section.isEmpty()) return;

        assert this.minecraft != null;
        LocalPlayer localPlayer = this.minecraft.player;
        Objects.requireNonNull(localPlayer);
        if (ChangedAddonPlus$handleRightClicked(section.get())) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            localPlayer.closeContainer();
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(true);
    }

    @Unique
    public boolean ChangedAddonPlus$handleRightClicked(int section) {
        if ((Object) this instanceof AbilityRadialScreen abilityRadialScreen) {
            AbstractAbility<?> ability = abilityRadialScreen.abilities.get(section);
            if (abilityRadialScreen.variant instanceof TransfurVariantInstanceExtensor variantInstanceExtensor) {
                variantInstanceExtensor.setSecondSelectedAbility(ability);
                ChangedAddonMod.PACKET_HANDLER.sendToServer(new VariantSecondAbilityActivate(minecraft.player, variantInstanceExtensor.getSecondAbilityKey().isEffectivelyDown(), ability));
                return true;
            }
        }
        return false;
    }
}
