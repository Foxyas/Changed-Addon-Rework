package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PsychicHoldAbility extends SimpleAbility {

	@Override
	public TranslatableComponent getDisplayName(IAbstractChangedEntity entity) {
		return new TranslatableComponent("changed_addon.ability.psychic_hold");
	}

	@Override
	public ResourceLocation getTexture(IAbstractChangedEntity entity) {
		return new ResourceLocation("changed_addon:textures/screens/psychic_hold.png"); //Place holder
	}

	@Override
	public boolean canUse(IAbstractChangedEntity entity) {
		return !Spectator(entity.getEntity());
	}

	public static boolean Spectator(Entity entity){
		if (entity instanceof Player player1){
			return player1.isSpectator();
		}
		return true;
	}


	public UseType getUseType(IAbstractChangedEntity entity) {
		return UseType.HOLD;
	}


	@Override
	public void startUsing(IAbstractChangedEntity entity) {
		super.startUsing(entity);
		//execute(entity.getLevel(),entity);
	}

	@Override
	public void tick(IAbstractChangedEntity entity) {
		super.tick(entity);
		execute(entity.getLevel(),entity.getEntity());
	}

	public static void execute(LevelAccessor world, Entity IAbstractChangedEntity) {
    if (IAbstractChangedEntity == null || !(IAbstractChangedEntity instanceof Player player)) {
        return;
    }

    final Vec3 playerPos = new Vec3(player.getX(), player.getY(), player.getZ());
    double maxRange = 14.0; // Raio máximo de efeito
    double stopRange = 6.0; // Distância para parar os projéteis
    double repelRange = 4.0; // Distância para repelir os projéteis

    List<Entity> nearbyEntities = world.getEntitiesOfClass(Entity.class, new AABB(playerPos, playerPos).inflate(maxRange / 2d), e -> true).stream()
            .sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(playerPos)))
            .collect(Collectors.toList());

    for (Entity projectile : nearbyEntities) {
        if (projectile instanceof FallingBlockEntity ||
                projectile.getType().is(EntityTypeTags.IMPACT_PROJECTILES)) {
            Vec3 projectilePos = projectile.position();
            Vec3 toPlayer = playerPos.subtract(projectilePos).normalize(); // Direção do jogador
            double distance = projectilePos.distanceTo(playerPos);

            if (distance <= stopRange && distance > repelRange) {
                // Parar completamente projéteis próximos, mas não muito próximos
                projectile.setDeltaMovement(Vec3.ZERO);
            } else if (distance <= repelRange) {
                // Repelir projéteis extremamente próximos
                Vec3 repelForce = toPlayer.scale(-1.0 * (repelRange - distance)); // Força inversamente proporcional
                projectile.setDeltaMovement(projectile.getDeltaMovement().add(repelForce));
            } else {
                // Diminuir velocidade de projéteis distantes
                double slowFactor = Math.max(0.1, 1.0 - (distance / maxRange)); // Fator de lentidão (mínimo 0.1)
                Vec3 currentMotion = projectile.getDeltaMovement();
                Vec3 reducedMotion = currentMotion.scale(slowFactor);

                // Verificar se projétil está indo na direção do jogador (produto escalar)
                double dotProduct = currentMotion.normalize().dot(toPlayer);
                if (player.isShiftKeyDown()) {
                    if (dotProduct > 0) {
                        projectile.setDeltaMovement(reducedMotion);
                    }
                } else {
                    projectile.setDeltaMovement(reducedMotion);
                	}
            	}
        	}
    	}
	}
}
