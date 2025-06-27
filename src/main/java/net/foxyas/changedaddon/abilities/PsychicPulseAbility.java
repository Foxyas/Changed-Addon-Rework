package net.foxyas.changedaddon.abilities;

import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
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

    public static void execute(LevelAccessor world, Entity IAbstractChangedEntity) {
        if (IAbstractChangedEntity == null)
            return;
        if (IAbstractChangedEntity instanceof Player entity) {
            {
                final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
                        .toList();
                for (Entity entityiterator : _entfound) {
                    if (entityiterator != entity) {
                        if (entityiterator instanceof FallingBlockEntity || entityiterator.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("minecraft:impact_projectiles")))) {
                            if (PlayerUtilProcedure.isProjectileMovingTowardsPlayer(entity, entityiterator)) {
                                //if(!world.isClientSide()){}
                                Vec3 NegativeMotion = new Vec3((-(entityiterator.getDeltaMovement().x())), (-(entityiterator.getDeltaMovement().y())), (-(entityiterator.getDeltaMovement().z())));
                                Vec3 Motion = NegativeMotion.multiply(1.5, 1.5, 1.5);
                                entityiterator.setDeltaMovement(Motion);
                                entityiterator.hasImpulse = true;
                                // Adicionar exaustão enquanto usa a habilidade
                                if (!entity.isSpectator()) {
                                    entity.causeFoodExhaustion(0.025F); // Aumenta a exaustão do jogador enquanto usa a habilidade
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("changed_addon.ability.psychic_pulse");
    }

    @Override
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
        //execute(entity.level(),entity.getEntity());
    }

    @Override
    public void tick(IAbstractChangedEntity entity) {
        super.tick(entity);
        execute(entity.getLevel(), entity.getEntity());
    }
}
