package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexEntity;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class SummonDLPupAbility extends SimpleAbility {
    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        if (entity.getEntity() instanceof Player player && player.getFoodData().getFoodLevel() < 8.0) {
            return false;
        }

        return !entity.isInWaterOrBubble();
    }

    protected Stream<BlockPos> findLandNearby(Level level, BlockPos near) {
        var referenceEntity = ChangedEntities.DARK_LATEX_WOLF_PUP.get().create(level);
        assert referenceEntity != null;

        return BlockPos.betweenClosedStream(near.offset(-4, -2, -4), near.offset(4, 2, 4)).filter(
                pos -> level.getBlockState(pos.offset(0, -1, 0)).entityCanStandOnFace(level, pos, referenceEntity, Direction.UP) && level.getBlockState(pos).isAir()
        );
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        var level = entity.getLevel();

        var livingEntity = entity.getEntity();

        List<AbstractDarkLatexEntity> nearbyDarkLatexEntities = level.getEntitiesOfClass(
                AbstractDarkLatexEntity.class,
                livingEntity.getBoundingBox().inflate(70.0),
                e -> e != livingEntity
        );

        var possibleSpawnPositions = findLandNearby(level, entity.getBlockPosition())
                .map(BlockPos::immutable).toList();
        int attempts = Math.min(possibleSpawnPositions.size(), 1);

        if (attempts == 0 || nearbyDarkLatexEntities.size() > 3) { // Spawn failed, grant reduced cooldown
            entity.getAbilityInstanceSafe(this)
                    .map(AbstractAbilityInstance::getController)
                    .ifPresent(controller -> controller.forceCooldown(60));
            return;
        }

        if (level.isClientSide)
            return;

        while (attempts > 0) {
            var blockPos = possibleSpawnPositions.get(level.random.nextInt(possibleSpawnPositions.size()));

            var pup = ChangedEntities.DARK_LATEX_WOLF_PUP.get().create(level);
            assert pup != null;

            if (entity.getEntity() instanceof Player player) {
                pup.tame(player);
            }

            pup.setTarget(entity.getEntity().getLastHurtByMob());
            pup.moveTo(blockPos, 0.0F, 0.0F);
            level.addFreshEntity(pup);

            attempts--;
        }

        if (entity.getEntity() instanceof Player player) {
            player.causeFoodExhaustion((float) 30.0);
        }

        ChangedSounds.broadcastSound(entity.getEntity(), ChangedSounds.DARK_LATEX_PUP_FORM_PUDDLE, 1.0f, 1.0f);
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.CHARGE_TIME;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        return 40;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 4 * 60 * 20; // 4 Minutes
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.summon_dl_pup");
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        Collection<Component> description = new ArrayList<>(super.getAbilityDescription(entity));
        description.add(Component.translatable("ability.changed_addon.summon_dl_pup.desc"));
        return description;
    }
}
