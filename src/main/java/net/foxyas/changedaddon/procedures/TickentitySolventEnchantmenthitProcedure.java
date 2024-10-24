package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.Experiment009Entity;
import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TickentitySolventEnchantmenthitProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		Entity entity = event.getEntity();
		if (entity != null) {
			execute(entity, event.getSource().getDirectEntity(), event.getAmount());
		}
	}
	private static void execute(Entity entity, Entity immediatesourceentity, double amount) {
		if (entity == null || immediatesourceentity == null)
			return;
		double damage_amount;
		double math;
		float EnchantLevel;

		DamageSource SolventDmg = null;
		EnchantLevel = EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), (immediatesourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY));
		damage_amount = amount;
		SolventDmg = new DamageSource("latex_solvent");
		math = SolventMath(EnchantLevel);



		if (!((immediatesourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() instanceof BowItem)
				&& !((immediatesourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() instanceof CrossbowItem)) {
			if (!(immediatesourceentity instanceof Projectile)) {
				if (EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), (immediatesourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
					if (entity instanceof Player player && ProcessTransfur.isPlayerLatex(player)){
						entity.hurt(SolventDmg, (float) (amount + math));
					}
				}
				if (EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), (immediatesourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
					if (entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:latexentity")))
							|| entity instanceof ChangedEntity) {
						if (entity.getType().is(ChangedTags.EntityTypes.LATEX)) {
							entity.hurt(SolventDmg, (float) (amount + math));
						}
					}
					if (entity instanceof Experiment009Entity) {
						entity.hurt(SolventDmg, (float) (amount + math));
					}
				}
			} else if (immediatesourceentity instanceof ThrownTrident trident) {
				CompoundTag tag = new CompoundTag();
				trident.save(tag);
				ItemStack ItemfromTrident = tag.contains("Trident") ? ItemStack.of(tag.getCompound("Trident")) : new ItemStack(Items.TRIDENT);
					if (EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), ItemfromTrident) != 0) {
						if (entity instanceof Player player && ProcessTransfur.isPlayerLatex(player)){
							entity.hurt(SolventDmg, (float) (amount + math));
						}
						if (entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:latexentity")))
								|| entity instanceof ChangedEntity) {
							if (entity.getType().is(ChangedTags.EntityTypes.LATEX)) {
								entity.hurt(SolventDmg, (float) (amount + math));
							}
						}
						if (entity instanceof Experiment009Entity) {
							entity.hurt(SolventDmg, (float) (amount + math));
						}
					}
			}
		}
	}

	private static float SolventMath(float EnchantLevel){
		float math;
		if (EnchantLevel == 1) {
			math = 1.5f;
		} else if (EnchantLevel == 0) {
			math = 1f;
		} else if (EnchantLevel == 2) {
			math = 3;
		} else if (EnchantLevel == 3) {
			math = 4;
		} else if (EnchantLevel == 4) {
			math = 4.5f;
		} else if (EnchantLevel == 5) {
			math = 5;
		} else {
			math = EnchantLevel - 0.5f;
		}
		return math;
	}
}
