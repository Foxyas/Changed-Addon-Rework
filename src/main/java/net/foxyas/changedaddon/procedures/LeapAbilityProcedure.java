/*
package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractLatex;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;

import javax.annotation.Nullable;

public class LeapAbilityProcedure extends SimpleAbility {

	private boolean Work;

	@Override
	public TranslatableComponent getDisplayName(IAbstractLatex entity) {
		return new TranslatableComponent("changed_addon.ability.leap");
	}

	@Override
	public ResourceLocation getTexture(IAbstractLatex entity) {
		return new ResourceLocation("changed_addon:textures/screens/friendlymodeicon.png");
	}

	@Override
	public boolean canUse(IAbstractLatex entity) {
		Player player = (Player) entity.getEntity();
		LatexVariantInstance LatexInstace = ProcessTransfur.getPlayerLatexVariant(player);
		LatexVariant Variant = LatexVariant.getEntityVariant(entity.getLatexEntity());
		if (Variant.is(ChangedTags.LatexVariants.CAT_LIKE )|| Variant.is(ChangedTags.LatexVariants.LEOPARD_LIKE)){
			return true;
		}
		return false;
	}

	public UseType getUseType(IAbstractLatex entity) {
		return UseType.CHARGE_RELEASE;
	}
	@Override
	public int getChargeTime(IAbstractLatex entity) {
		return 25;
	}

	@Override
	public int getCoolDown(IAbstractLatex entity) {
		return 15;
	}

	@Override
	public void startUsing(IAbstractLatex entity) {
		super.startUsing(entity);
		if(Work){
		LeapAbility(entity.getEntity());
		}
	}

	@Override
	public void tick(IAbstractLatex entity) {
		super.tick(entity);
	}

	@Override
	public void tickCharge(IAbstractLatex entity, float ticks) {
		super.tickCharge(entity, ticks);
		if (ticks == 25) {
			Work = true;
		}
	}

	public static void LeapAbility(Entity entity) {
		if (entity == null)
			return;
		double motionZ = 0;
		double deltaZ = 0;
		double deltaX = 0;
		double motionY = 0;
		double deltaY = 0;
		double motionX = 0;
		double maxSpeed = 0;
		double speed = 0;
		double Yspeed = 0;
		if (!entity.isShiftKeyDown()) {
			deltaX = -Math.sin((entity.getYRot() / 180) * (float) Math.PI);
			deltaY = -Math.sin((entity.getXRot() / 180) * (float) Math.PI);
			deltaZ = Math.cos((entity.getYRot() / 180) * (float) Math.PI);
			speed = 1;
			motionX = deltaX * speed;
			motionY = deltaY * speed;
			motionZ = deltaZ * speed;
			entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
		}
		if (entity.isShiftKeyDown()) {
			deltaX = -Math.sin((entity.getYRot() / 180) * (float) Math.PI);
			deltaY = entity.level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(1)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY() - entity.getY();
			deltaZ = Math.cos((entity.getYRot() / 180) * (float) Math.PI);
			speed = 0.25;
			Yspeed = 0.5;
			motionX = deltaX * speed;
			motionY = deltaY * Yspeed;
			motionZ = deltaZ * speed;
			entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
		}
	}

}
*/