package net.foxyas.changedaddon.entity.projectile;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class LuminarCrystalShardProjectile extends CrystalShardProjectile {

    public LuminarCrystalShardProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public LuminarCrystalShardProjectile(Level level) {
        super(ChangedAddonEntities.LUMINAR_CRYSTAL_SHARD.get(), level);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        ParticlesUtil.sendParticles(this.level, new BlockParticleOption(ParticleTypes.BLOCK, ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState()), this.position(), 0.05f, 0.05f, 0.05f, 8, 0.5f);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        ParticlesUtil.sendParticles(this.level, new BlockParticleOption(ParticleTypes.BLOCK, ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState()), this.position(), 0.05f, 0.05f, 0.05f, 8, 0.5f);
    }

    @Override
    public boolean increaseArrowCount(LivingEntity livingentity, EntityHitResult pResult) {
        return false;
    }
}
