package net.foxyas.changedaddon.client.renderer.layers.animation;

import net.foxyas.changedaddon.client.model.FemaleExp2Model;
import net.foxyas.changedaddon.client.model.MaleExp2Model;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleCatModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.world.entity.HumanoidArm;

public class DodgeAbilityAnimation {

    //DEV TEST ANIMATION IT DON'T WORKS
    public static void PlayDodgeAnimationForExp2(AdvancedHumanoidRenderer<?, ?, ?> renderer) {
        if (renderer == null) {
            return;
        }

        var model = renderer.getModel();
        var armorModel = renderer.getArmorLayer().getParentModel();

        if (model instanceof MaleExp2Model model1) {

            for (HumanoidArm arm : HumanoidArm.values()) {
                model1.getArm(arm).xRot = (float) -Math.PI / 2;
            }

            if (armorModel instanceof ArmorLatexMaleCatModel<?> model2) {
                for (HumanoidArm arm : HumanoidArm.values()) {
                    model2.getArm(arm).xRot = (float) -Math.PI / 2;
                }
            }
        } else if (model instanceof FemaleExp2Model model1) {

            for (HumanoidArm arm : HumanoidArm.values()) {
                model1.getArm(arm).xRot = (float) -Math.PI / 2;
            }
            if (armorModel instanceof ArmorLatexFemaleCatModel<?> model2) {
                for (HumanoidArm arm : HumanoidArm.values()) {
                    model2.getArm(arm).xRot = (float) -Math.PI / 2;
                }
            }
        }
    }
}

