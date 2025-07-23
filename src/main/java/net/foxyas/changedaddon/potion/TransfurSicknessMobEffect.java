
package net.foxyas.changedaddon.potion;

import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class TransfurSicknessMobEffect extends MobEffect {

	public TransfurSicknessMobEffect() {
		super(MobEffectCategory.HARMFUL, Color3.BLACK.toInt());
		addAttributeModifier(ChangedAttributes.TRANSFUR_DAMAGE.get(),"d9eff2a7-bdff-4df7-b5dd-9a66e6133b1e",-0.4,AttributeModifier.Operation.ADDITION);
	}

	@Override
	public @NotNull String getDescriptionId() {
		return "effect.changed_addon.transfur_sickness";
	}
}
