package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TickentitySolventEnchantmenthitProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		Entity entity = event.getEntity();
		if (event != null && entity != null) {
			execute(event, entity, event.getSource().getDirectEntity(), event.getSource().getEntity(), event.getAmount());
		}
	}

	public static void execute(Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
		execute(null, entity, immediatesourceentity, sourceentity, amount);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
		if (entity == null || immediatesourceentity == null || sourceentity == null)
			return;
		double damage_amount = 0;
		double math = 0;
		double EnchantLevel = 0;
		DamageSource SolventDmg = null;
		EnchantLevel = EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY));
		damage_amount = amount;
		SolventDmg = new DamageSource("latex_solvent");
		if (EnchantLevel == 1) {
			math = 0;
		} else if (EnchantLevel == 0) {
			math = 0;
		} else if (EnchantLevel == 2) {
			math = 1;
		} else if (EnchantLevel == 3) {
			math = 2;
		} else if (EnchantLevel == 4) {
			math = 3;
		} else {
			math = EnchantLevel - 1;
		}
		if (!((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() instanceof BowItem)
				&& !((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() instanceof CrossbowItem)) {
			if (!(immediatesourceentity instanceof ThrowableProjectile)) {
				if (EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
					if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true
							&& (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_transfur == false) {
						if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.LATEX_SOLVENT.get(), 60, (int) EnchantLevel, false, false));
					}
				}
				if (EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
					if (entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:latexentity")))) {
						if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.LATEX_SOLVENT.get(), 60, (int) EnchantLevel, false, false));
					}
				}
			}
		}
	}
}
