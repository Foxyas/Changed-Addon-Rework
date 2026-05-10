package net.foxyas.changedaddon.mixins.client.renderer;

import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.NoSuchElementException;

@Mixin(value = LatexHumanoidArmorModel.class)
public class LatexHumanoidArmorModelMixin {

//    @Inject(method = "prepareVisibility", at = @At("RETURN"), remap = false)
//    private void turnOffPlantoids(EquipmentSlot armorSlot, ItemStack item, CallbackInfo ci) {
//        var self = (AdvancedHumanoidModel<?>) (Object) this;
//        var torso = self.getTorso();
//        try {
//            ModelPart plantoidsPart = torso.getChild("Plantoids");
//            plantoidsPart.visible = !ChangedAddonClientConfiguration.PLANTOIDS_VISIBILITY.get();
//        } catch (NoSuchElementException ignored) {
//        }
//    }
}
