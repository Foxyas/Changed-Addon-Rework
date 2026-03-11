package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class PsychicPulseAbility extends SimpleAbility {

    public static boolean Spectator(Entity entity) {
        if (entity instanceof Player player1) {
            return player1.isSpectator();
        }
        return true;
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.psychic_pulse");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/psychic_pulse.png"); //Place holder
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return !Spectator(entity.getEntity());
    }

    @Override
    public boolean canKeepUsing(IAbstractChangedEntity entity) {
        Player player = (Player) entity.getEntity();
        return player.getFoodData().getFoodLevel() > 10;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 0;
    }

    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.HOLD;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
    }

    @Override
    public void tick(IAbstractChangedEntity entity) {
        super.tick(entity);
        if (entity.getEntity() instanceof Player player) {
            final Vec3 position = player.position();
            List<Entity> entities = player.level.getEntitiesOfClass(Entity.class,
                            new AABB(position, position).inflate(10 / 2d),
                            projectile -> projectile instanceof FallingBlockEntity || projectile.getType().is(EntityTypeTags.IMPACT_PROJECTILES))
                    .stream()
                    .sorted(Comparator.comparingDouble(target -> target.distanceToSqr(position)))
                    .toList();
            for (Entity projectile : entities) {
                if (PlayerUtil.isProjectileMovingTowardsEntity(player, projectile)) {
                    if (projectile instanceof AbstractHurtingProjectile abstractHurtingProjectile) {
                        abstractHurtingProjectile.xPower *= -1.5;
                        abstractHurtingProjectile.yPower *= -1.5;
                        abstractHurtingProjectile.zPower *= -1.5;
                    } else {
                        Vec3 negativeMotion = projectile.getDeltaMovement().scale(-1);
                        Vec3 motion = negativeMotion.multiply(1.5, 1.5, 1.5);
                        projectile.setDeltaMovement(motion);
                    }

                    projectile.hasImpulse = true;
                    projectile.hurtMarked = true;
                    // Adicionar exaustão enquanto usa a habilidade
                    if (!player.isSpectator()) {
                        player.causeFoodExhaustion(0.025F); // Aumenta a exaustão do jogador enquanto usa a habilidade
                    }
                }
            }
        }
    }
}
