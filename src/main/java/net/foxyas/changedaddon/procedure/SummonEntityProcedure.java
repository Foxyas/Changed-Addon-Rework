package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;


public class SummonEntityProcedure {

    public static void execute(Level world, Player player) {
        if (player.level.isClientSide() || !(world instanceof ServerLevel level)) return;

        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (instance == null) return;

        ChangedEntity fakeEntity = instance.getChangedEntity();

        Entity entityToSpawn = fakeEntity.getType().create(level);
        assert entityToSpawn != null;
        entityToSpawn.moveTo(player.getX(), player.getY(), player.getZ(), 0, 0);
        entityToSpawn.setYBodyRot(0);
        entityToSpawn.setYHeadRot(0);

        if (entityToSpawn instanceof Mob mob) {
            ForgeEventFactory.onFinalizeSpawn(mob, level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        }

        if (fakeEntity instanceof IAlphaAbleEntity original && entityToSpawn instanceof IAlphaAbleEntity alphaAble) {
            alphaAble.setAlpha(original.isAlpha());
            alphaAble.setAlphaScale(original.alphaAdditionalScale());
        }

        world.addFreshEntity(entityToSpawn);
    }
}

