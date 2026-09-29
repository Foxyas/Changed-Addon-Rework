package net.foxyas.changedaddon.entity.projectile;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class LuminarCrystalShardProjectile extends CrystalShardProjectile {

    public LuminarCrystalShardProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public LuminarCrystalShardProjectile(Level level) {
        super(ChangedAddonEntities.LUMINAR_CRYSTAL_SHARD.get(), level);
    }
}
