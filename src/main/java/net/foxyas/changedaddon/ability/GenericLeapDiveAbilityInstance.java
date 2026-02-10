package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.util.DelayedTask;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GenericLeapDiveAbilityInstance extends AbstractAscendDiveAbilityInstance {

    public GenericLeapDiveAbilityInstance(
            AbstractAbility<?> ability,
            IAbstractChangedEntity entity
    ) {
        super(ability, entity);
    }

    /* ---------------- Config ---------------- */

    @Override
    public int getAscendNeededTicks() {
        return 15;
    }

    @Override
    public double getAscendSpeed() {
        return 0.7;
    }

    @Override
    public double getDiveSpeed() {
        return 1.6;
    }

    @Override
    public double getVerticalDiveSpeed() {
        return 0.8;
    }

    /* ---------------- Impact ---------------- */

    @Override
    protected void onImpact() {
        if (!(entity.getLevel() instanceof ServerLevel level)) return;

        BlockPos center = entity.getEntity().blockPosition();

        // knockback simples
        for (LivingEntity e : level.getEntitiesOfClass(
                LivingEntity.class,
                new AABB(center).inflate(6),
                target -> target != entity.getEntity()
        )) {
            Vec3 dir = e.position()
                    .subtract(entity.getEntity().position())
                    .normalize();

            e.push(
                    dir.x * 1.2,
                    0.6,
                    dir.z * 1.2
            );
        }

        // delay opcional para efeitos encadeados
        DelayedTask.schedule(2, () -> {
            // partículas / som / efeitos extras
        });
    }
}