
package net.foxyas.changedaddon.potion;

import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class LatexContaminationMobEffect extends MobEffect {

	public LatexContaminationMobEffect() {
		super(MobEffectCategory.HARMFUL, Color3.getColor("#ffffff").toInt());
		addAttributeModifier(ChangedAddonAttributes.LATEX_INFECTION.get(),"2971dbcb-1aba-4ae4-8726-3025cc7c2dd7",0.1, AttributeModifier.Operation.ADDITION);
	}

	@Override
	public @NotNull String getDescriptionId() {
		return "effect.changed_addon.latex_contamination";
	}

	@Override
	public void addAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
		super.addAttributeModifiers(entity, attributeMap, amplifier);

		if(!(entity instanceof Player player)) return;

		float Math = (amplifier + 1 * 0.1F) / 2;
		ProcessTransfur.setPlayerTransfurProgress(player, ProcessTransfur.getPlayerTransfurProgress(player) + Math);
	}
}
