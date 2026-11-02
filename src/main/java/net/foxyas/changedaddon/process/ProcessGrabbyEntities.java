package net.foxyas.changedaddon.process;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.world.gamerules.WorldDifficulty;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.boss.Behemoth;
import net.ltxprogrammer.changed.entity.beast.boss.BehemothHand;
import net.ltxprogrammer.changed.entity.beast.boss.BehemothHead;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ProcessGrabbyEntities {
    public static boolean canSpawnWithGrabAbility(ChangedEntity changedEntity) {
        return isAffectedByGrabAbility(changedEntity);
    }

    public static boolean isAffectedByGrabAbility(ChangedEntity changedEntity) {
        if (changedEntity instanceof IGrabberEntity.IGrabberCondition iGrabberCondition) {
            return iGrabberCondition.isAffectedByGrab();
        }

        return true;
    }

    public static boolean isAbleToUseGrab(ChangedEntity changedEntity) {
        EntityType<?> type = changedEntity.getType();
        if (type == ChangedEntities.BEHEMOTH_HEAD.get() || type == ChangedEntities.BEHEMOTH_HAND_LEFT.get() || type == ChangedEntities.BEHEMOTH_HAND_RIGHT.get()) {
            if (changedEntity instanceof IGrabberEntity grabberEntity) {
                WorldDifficulty worldDifficulty = ChangedAddonServerConfiguration.BEHEMOTH_CAN_USE_GRAB_IN_DIFFICULTY.get();
                Level level = changedEntity.level();
                Difficulty levelDifficulty = level.getDifficulty();
                if (worldDifficulty == WorldDifficulty.HARDCORE) {
                    grabberEntity.setCanUseGrab(level.getLevelData().isHardcore());
                } else {
                    grabberEntity.setCanUseGrab(levelDifficulty.getId() >= worldDifficulty.getAsLevel());
                }
            }
        }

        return changedEntity instanceof IAlphaAbleEntity iAlphaAbleEntity && iAlphaAbleEntity.isAlpha();
    }

    public static int getMaxGrabCooldownOf(ChangedEntity changedEntity) {
        if (changedEntity instanceof Behemoth behemoth) {
            int appliedCooldown = 200;
            if (behemoth instanceof BehemothHand hand && (IAlphaAbleEntity.isEntityAlpha(hand.head) || IAlphaAbleEntity.isEntityAlpha(hand))) {
                return (int) (appliedCooldown * 1.5f);
            }
            if (behemoth instanceof BehemothHead head && IAlphaAbleEntity.isEntityAlpha(head)) {
                return (int) (appliedCooldown * 1.5f);
            }
            return appliedCooldown;
        }

        if (IAlphaAbleEntity.isEntityAlpha(changedEntity)) {
            return (int) (120 * 1.5);
        }

        return 120;
    }
}
