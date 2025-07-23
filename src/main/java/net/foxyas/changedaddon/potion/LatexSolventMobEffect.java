
package net.foxyas.changedaddon.potion;

import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class LatexSolventMobEffect extends MobEffect {

	public LatexSolventMobEffect() {
		super(MobEffectCategory.NEUTRAL, -1);
		addAttributeModifier(ChangedAddonAttributes.LATEX_RESISTANCE.get(),"a0ca1f84-b8cc-43da-baea-0e6a6af71a7a",0.1, AttributeModifier.Operation.ADDITION);
	}

	@Override
	public @NotNull String getDescriptionId() {
		return "effect.changed_addon.latex_solvent";
	}
}
