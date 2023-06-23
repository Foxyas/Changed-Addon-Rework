package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Registry;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class LatexSolventOnEffectActiveTickProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		float TransfurProgress_local_var = ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) <= 0)
				? 0.1f
				: ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get())) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) * 0.8f;
		if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) : false) {
			if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == false) {
				if (new Object() {
					public double getValue() {
						CompoundTag dataIndex1 = new CompoundTag();
						entity.saveWithoutId(dataIndex1);
						return dataIndex1.getDouble("TransfurProgress");
					}
				}.getValue() > 0) {
					CompoundTag dataIndex1 = new CompoundTag();
					entity.saveWithoutId(dataIndex1);
					dataIndex1.putFloat("TransfurProgress", (new Object() {
						public float getValue() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							return dataIndex0.getFloat("TransfurProgress");
						}
					}.getValue() - 0.5f * TransfurProgress_local_var));
					entity.load(dataIndex1);
				}
			} else {
				if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_transfur == false) {
					if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > 1) {
						if (entity instanceof LivingEntity _entity)
							_entity.hurt(new DamageSource("latex_solvent").bypassArmor(),
									(float) ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) == 0
											? 0.5
											: (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) + 1));
					}
				}
			}
			if (entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:latexentity")))) {
				if (entity instanceof LivingEntity _entity)
					_entity.hurt(new DamageSource("latex_solvent").bypassArmor(),
							(float) ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) == 0
									? 0.5
									: (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) + 1));
			}
		}
	}
}
