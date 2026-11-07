package net.foxyas.changedaddon.entity.projectile;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class LuminarCrystalShardProjectile extends CrystalShardProjectile {

    public LuminarCrystalShardProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public LuminarCrystalShardProjectile(Level level) {
        super(ChangedAddonEntities.LUMINAR_CRYSTAL_SHARD.get(), level);
    }

    @Override
    public boolean increaseArrowCount(LivingEntity livingentity, EntityHitResult pResult) {
        return false;
    }
}
