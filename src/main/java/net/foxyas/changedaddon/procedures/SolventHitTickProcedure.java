package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;
import net.foxyas.changedaddon.init.ChangedAddonModParticleTypes;
import net.foxyas.changedaddon.registers.ChangedAddonDamageSources;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@Mod.EventBusSubscriber
public class SolventHitTickProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		Entity target = event.getEntity();
		DamageSource source = event.getSource();
		String MsgId = source.getMsgId();

		// Verifica se o atacante possui o encantamento Solvent
		if (source == ChangedAddonDamageSources.SOLVENT || (source.getMsgId().equals("latex_solvent") || source.getMsgId().startsWith("latex_solvent"))) {
			playSoundAndParticles(target);
		}
	}

	private static void playSoundAndParticles(Entity entity) {
		Level level = entity.level;

		// Toca som de extinção de fogo
		if (entity instanceof Player player) {
			if (level instanceof ServerLevel serverLevel) {
				serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.MASTER, 0.5f, 0);
			} else {
				level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.MASTER, 0.5f, 0, true);
			}
		} else {
			entity.playSound(SoundEvents.FIRE_EXTINGUISH, 0.5f, 0);
		}

		// Emite partículas
		if (level instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(
					(SimpleParticleType) ChangedAddonModParticleTypes.SOLVENT_PARTICLE.get(),
					entity.getX(),
					entity.getY() + 1,
					entity.getZ(),
					10,
					0.2, 0.3, 0.2, 0.1
			);
		}
	}
}