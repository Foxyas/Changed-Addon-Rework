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

            // Calculate the position behind the attacker
            double distanceBehind = 2; // Distance to teleport behind the attacker
            Vec3 newPos = attackerPos.subtract(lookDirection.scale(distanceBehind));
            int DodgeAmount = DodgeAbility.getDodgeAmount();
            int MaxDodgeAmount = DodgeAbility.getMaxDodgeAmount();
            boolean IsDodgeActivate = DodgeAbility.isDodgeActivate();
            if(IsDodgeActivate){
                if(DodgeAmount > 0){
                    // Teleport the target to the new position
                    if (world instanceof ServerLevel serverLevel) {
                        player.displayClientMessage(new TextComponent(new TranslatableComponent("changed_addon.ability.dodge.dodge_amount_left").toString() + DodgeAmount),true);
                        DodgeAbility.subDodgeAmount();
                        target.teleportTo(newPos.x(), target.getY(), newPos.z());
                        event.setCanceled(true);
                    }
                }
            }
	    }
    }
}
