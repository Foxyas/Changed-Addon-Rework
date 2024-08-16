package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SolventHitTickProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		Entity entity = event.getEntity();
		DamageSource damageSource = event.getSource();
		Level level = event.getEntity().level;
		if (damageSource.getMsgId().contains("latex_solvent")){
			if (entity instanceof Player player){
				if(player.getLevel() instanceof ServerLevel serverLevel){
					serverLevel.playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.FIRE_EXTINGUISH, SoundSource.MASTER,2.5f,0);
				} else{
					player.getLevel().playLocalSound(entity.getX(), entity.getY(),entity.getZ(),SoundEvents.FIRE_EXTINGUISH, SoundSource.MASTER,2.5f,0,true);
				}
			} else {
				if(entity.getLevel() instanceof ServerLevel){
					entity.playSound(SoundEvents.FIRE_EXTINGUISH,2.5f,0);
				} else {
					entity.getLevel().playLocalSound(entity.getX(), entity.getY(),entity.getZ(),SoundEvents.FIRE_EXTINGUISH, SoundSource.MASTER,2.5f,0,true);
				}

			}
			if (level instanceof ServerLevel serverLevel){
				serverLevel.sendParticles(((SimpleParticleType) ChangedAddonModParticleTypes.SOLVENT_PARTICLE.get()), entity.getX(), entity.getY() + 1, entity.getZ(), 10, 0.2, 0.3, 0.2, 0.1);
			}
		}
	}
}
