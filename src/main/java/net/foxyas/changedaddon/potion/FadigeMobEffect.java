
package net.foxyas.changedaddon.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import org.jetbrains.annotations.NotNull;

public class FadigeMobEffect extends MobEffect {

	public FadigeMobEffect() {
		super(MobEffectCategory.NEUTRAL, -1);
	}

	@Override
	public @NotNull String getDescriptionId() {
		return "effect.changed_addon.fadige";
	}
}
