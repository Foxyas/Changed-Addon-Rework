package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.init.ChangedAddonModParticleTypes;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class WhenEntityIshurtProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		Entity entity = event.getEntity();
		if (event != null && entity != null) {
			execute(event, entity.getLevel(), entity.getX(), entity.getY(), entity.getZ(), event.getSource(), entity);
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity) {
		execute(null, world, x, y, z, damagesource, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity) {
		if (entity == null)
			return;
		if (damagesource.getMsgId().equals("latex_solvent")) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.extinguish_fire")), SoundSource.NEUTRAL, (float) 0.8, 0);
				} else {
					_level.playLocalSound((entity.getX()), (entity.getY() + 1), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.extinguish_fire")), SoundSource.NEUTRAL, (float) 0.8, 0, false);
				}
			}
			if (world instanceof ServerLevel _level)
				_level.sendParticles((SimpleParticleType) (ChangedAddonModParticleTypes.SOLVENT_PARTICLE.get()), x, (y + 1), z, 10, 0.2, 0.35, 0.2, 0.1);
		}
	}
}
