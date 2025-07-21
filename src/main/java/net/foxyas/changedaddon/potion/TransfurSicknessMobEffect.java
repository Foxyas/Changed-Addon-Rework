
package net.foxyas.changedaddon.potion;

import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
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

	@Override
	public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
		double levelPotion = 0;
		if (entity instanceof Player player && ProcessTransfur.isPlayerTransfurred(player)) {
			TransfurVariantInstance<?> tf = ProcessTransfur.getPlayerTransfurVariant(player);
			TransfurMode mode = tf.getParent().transfurMode;

			if (mode != TransfurMode.NONE) {
				//ReturnTransfurModeProcedure.setPlayerTransfurMode.execute(player, 3);
			}
			/*levelPotion = amplifier;
			TransfurSicknessHandleProcedure.addAttributeMod(entity, levelPotion);*/
		}
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
