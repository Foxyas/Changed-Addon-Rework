package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.nbt.CompoundTag;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.entity.Experiment009Entity;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class Experiment009latexdmgProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		Entity entity = event.getEntity();
		if (event != null && entity != null) {
			execute(event, entity, event.getSource().getEntity());
		}
	}

	public static void execute(Entity entity, Entity sourceentity) {
		execute(null, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (sourceentity instanceof Experiment009Entity) {
			if (entity instanceof Player) {
				if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == false) {
					if (!(entity instanceof LivingEntity _livEnt ? _livEnt.isBlocking() : false)) {
						float Math = 0.8f;
						float PlayerTransfurProgress = new Object() {
							public float getValue() {
								CompoundTag dataIndex0 = new CompoundTag();
								entity.saveWithoutId(dataIndex0);
								return dataIndex0.getFloat("TransfurProgress");
							}
						}.getValue();
						CompoundTag dataIndex1 = new CompoundTag();
						entity.saveWithoutId(dataIndex1);
						dataIndex1.putFloat("TransfurProgress", PlayerTransfurProgress + Math);
						entity.load(dataIndex1);
						CompoundTag dataIndex3 = new CompoundTag();
						entity.saveWithoutId(dataIndex3);
						dataIndex3.putString("TransfurProgressType", "changed:form_light_latex_wolf/male");
						entity.load(dataIndex3);
						if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get(), 200, 2, false, true));
					}
				}
			}
		}
	}
}
