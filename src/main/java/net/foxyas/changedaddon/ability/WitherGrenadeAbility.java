package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.bosses.Experiment10BossEntity;
import net.foxyas.changedaddon.entity.bosses.Experiment10Entity;
import net.foxyas.changedaddon.entity.projectile.WitherParticleProjectile;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class WitherGrenadeAbility extends SimpleAbility {

    public WitherGrenadeAbility() {
        super();
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return entity.getChangedEntity() instanceof Experiment10Entity || entity.getChangedEntity() instanceof Experiment10BossEntity;
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.CHARGE_TIME;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 60;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        return 40;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
        this.shootProjectile(entity.getEntity());
    }

    public void shootProjectile(LivingEntity livingEntity) {
        if (!(livingEntity.level() instanceof ServerLevel serverLevel)) return;

        Vec3 lookVector = livingEntity.getViewVector(1).scale(1.05f);
        WitherParticleProjectile projectile = new WitherParticleProjectile(ChangedAddonEntities.WITHER_PARTICLE_PROJECTILE.get(), serverLevel);
        projectile.setPos(livingEntity.getEyePosition().add(lookVector));
        Vec3 motion = lookVector.scale(1);
        projectile.shoot(motion.x, motion.y, motion.z, 3.5f, 0.0f);
        livingEntity.swing(InteractionHand.MAIN_HAND, true);

        serverLevel.addFreshEntity(projectile);
    }
}
