package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.ability.DodgeAbility;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DodgeAbilityHandleProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		LivingEntity target = event.getEntityLiving();
        Entity attacker = event.getSource().getEntity();
        if(target instanceof Player player){
            Level world = target.level;
            if(target == null){
                return;
            }
            if (attacker == null) {
                return;
            }
            
            // Get the position of the attacker
            Vec3 attackerPos = attacker.position();

            // Get the look direction of the attacker
            Vec3 lookDirection = attacker.getLookAngle().normalize();

            // Get the look direction of the target
            Vec3 targetlookDirection = target.getLookAngle();
            
            // Calculate the position behind the attacker
            double distanceBehind = 2; // Distance to teleport behind the attacker
            Vec3 Dodgetype1 = attackerPos.subtract(lookDirection.scale(distanceBehind));
            Vec3 Dodgetype2 = targetlookDirection.scale(-1);
            int DodgeAmount = DodgeAbility.getDodgeAmount();
            int MaxDodgeAmount = DodgeAbility.getMaxDodgeAmount();
            double distance = attacker.distanceTo(target);
            boolean IsDodgeActivate = DodgeAbility.isDodgeActivate();
            boolean nonAnyIframe = player.invulnerableTime <= 0 && player.hurtTime <= 0;
            boolean nonIframe = player.invulnerableTime <= 0;
            boolean nonHurtFrame = player.hurtTime <= 0;
            if(nonIframe || nonHurtFrame || nonAnyIframe && IsDodgeActivate){
                if(DodgeAmount > 0){
                    if(distance <= 4){
                        // Teleport the target to the new position
                        if (world instanceof ServerLevel serverLevel) {
                            player.displayClientMessage(new TranslatableComponent("changed_addon.ability.dodge.dodge_amount_left",DodgeAmount),true);
                            DodgeAbility.subDodgeAmount();
                            target.teleportTo(Dodgetype1.x(), target.getY(), Dodgetype1.z());
                            player.invulnerableTime = 20;
                            player.hurtDuration = 10;
                            player.hurtTime = player.hurtDuration;
                            event.setCanceled(true);
                        }
                    } else {
                    	if (world instanceof ServerLevel serverLevel) {
                        	player.displayClientMessage(new TranslatableComponent("changed_addon.ability.dodge.dodge_amount_left",DodgeAmount),true);
                        	DodgeAbility.subDodgeAmount();
                        	player.invulnerableTime = 20;
                        	player.hurtDuration = 10;
                        	player.hurtTime = player.hurtDuration;
                        	event.setCanceled(true);
                    	}
                    	DodgeAttack(player,attacker);
                    }
                } else {
					 DodgeAbility.SetDodgeActivate(false);
                }
            }
	    }
    }


    public static void DodgeDash(LivingEntity target,boolean set){
        double deltaX = -Math.sin((target.getYRot() / 180) * (float) Math.PI);
        double deltaY = -Math.sin((target.getXRot() / 180) * (float) Math.PI);
        double deltaZ = Math.cos((target.getYRot() / 180) * (float) Math.PI);
        double speed = 1.05;
        double motionX = deltaX * speed;
        double motionY = deltaY * speed;
        double motionZ = deltaZ * speed;
        target.setDeltaMovement(target.getDeltaMovement().add(motionX, motionY, motionZ));
    }

    public static void DodgeDash(Player target,boolean set){
        double deltaX = -Math.sin((target.getYRot() / 180) * (float) Math.PI);
        double deltaY = -Math.sin((target.getXRot() / 180) * (float) Math.PI);
        double deltaZ = Math.cos((target.getYRot() / 180) * (float) Math.PI);
        double speed = 1.05;
        double motionX = deltaX * speed;
        double motionY = deltaY * speed;
        double motionZ = deltaZ * speed;
        if(!set) {
            target.setDeltaMovement(target.getDeltaMovement().add(-motionX, 0, -motionZ));
        } else {
            target.setDeltaMovement(-motionX, target.getDeltaMovement().y, -motionZ);
        }
    }

    public static void DodgeDashtype2(Player target,boolean set){
        Vec3 targetlookDirection = target.getLookAngle().normalize();
        Vec3 MotionApply = targetlookDirection.scale(1.25);
        double motionX = MotionApply.x;
        double motionY = MotionApply.y;
        double motionZ = MotionApply.z;
        if(!set) {
            target.setDeltaMovement(target.getDeltaMovement().add(-motionX, 0, -motionZ));
        } else {
            target.setDeltaMovement(-motionX, target.getDeltaMovement().y, -motionZ);
        }
    }

    public static void DodgeAttack(Entity Dodger,Entity attacker){
        Vec3 attackerPos = attacker.position();
        Vec3 DodgerPos = Dodger.position();
        Vec3 DodgeMotion = attackerPos.subtract(DodgerPos).scale(-0.25);
        Dodger.setDeltaMovement(DodgeMotion.x,Dodger.getDeltaMovement().y,DodgeMotion.z);
    }
}
