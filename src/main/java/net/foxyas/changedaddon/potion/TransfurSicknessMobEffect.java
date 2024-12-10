
package net.foxyas.changedaddon.potion;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.foxyas.changedaddon.procedures.TransfurSicknessEffectExpiresProcedure;
import net.foxyas.changedaddon.procedures.TransfurSicknessOnEffectActiveTickProcedure;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class TransfurSicknessMobEffect extends MobEffect {
	public TransfurSicknessMobEffect() {
		super(MobEffectCategory.HARMFUL, Color3.BLACK.toInt());
		addAttributeModifier(ChangedAttributes.TRANSFUR_DAMAGE.get(),"d9eff2a7-bdff-4df7-b5dd-9a66e6133b1e",-0.4,AttributeModifier.Operation.ADDITION);
	}

	@Override
	public @NotNull String getDescriptionId() {
		return "effect.changed_addon.transfur_sickness";
	}

	@Override
	public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
		TransfurSicknessOnEffectActiveTickProcedure.execute(entity, amplifier);
	}

	@Override
	public void addAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
		super.addAttributeModifiers(entity, attributeMap, amplifier);
	}

	@Override
	public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
		super.removeAttributeModifiers(entity, attributeMap, amplifier);
		//TransfurSicknessEffectExpiresProcedure.execute(entity, amplifier);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
