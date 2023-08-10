package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModAttributes;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class LatexResistancetickProcedure {
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
		double TransfurProgress = 0;
		AttributeInstance attributeInstance = ((LivingEntity) entity).getAttribute(ChangedAddonModAttributes.LATEXRESISTANCE.get());
		double LatexResistence_local_var = attributeInstance.getValue();
		float TransfurProgress_local_var = (float) LatexResistence_local_var;
		if (((LivingEntity) entity).getAttribute(ChangedAddonModAttributes.LATEXRESISTANCE.get()).getValue() > 0) {
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
				if (new Object() {
					public double getValue() {
						CompoundTag dataIndex2 = new CompoundTag();
						entity.saveWithoutId(dataIndex2);
						return dataIndex2.getDouble("TransfurProgress");
					}
				}.getValue() < 0) {
					{
						CompoundTag dataIndex3 = new CompoundTag();
						entity.saveWithoutId(dataIndex3);
						dataIndex3.putFloat("TransfurProgress", 0);
						entity.load(dataIndex3);
					}
				}
			}
		}
	}
}
