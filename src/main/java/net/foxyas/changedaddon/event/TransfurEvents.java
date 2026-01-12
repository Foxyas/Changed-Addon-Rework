package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TransfurEvents {

    @SubscribeEvent
    public static void WhenTransfured(ProcessTransfur.EntityVariantAssigned changedVariantEvent) {
        TransfurVariant<?> variant = changedVariantEvent.originalVariant;
        if (variant == null) return;

        LivingEntity entity = changedVariantEvent.livingEntity;
        if (!entity.level.getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.NEED_PERMISSION_FOR_BOSS_TRANSFUR))
            return;

        if (variant.is(ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS) && !getPlayerVars(entity).Exp009TransfurAllowed) {
            changedVariantEvent.variant = ChangedAddonTransfurVariants.EXPERIMENT_009.get();
        }
        if (variant.is(ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS) && !getPlayerVars(entity).Exp10TransfurAllowed) {
            changedVariantEvent.variant = ChangedAddonTransfurVariants.EXPERIMENT_10.get();
        }
    }


    @SubscribeEvent
    public static void WhenTransfuredByAlpha(ProgressTransfurEvents.NewlyTransfurred changedVariantEvent) {
        TransfurVariantInstance<?> transfurVariantInstance = changedVariantEvent.getTransfurVariantInstance();
        if (transfurVariantInstance != null) {
            TransfurContext transfurContext = transfurVariantInstance.transfurContext;
            IAbstractChangedEntity source = transfurContext.source;
            if (source != null) {
                boolean wantAbsorption = source.wantAbsorption();
                if (wantAbsorption) {
                    if (source.getEntity() instanceof IAlphaAbleEntity alphaAbleEntity && transfurVariantInstance.getChangedEntity() instanceof IAlphaAbleEntity iAlphaAble) {
                        iAlphaAble.setAlpha(alphaAbleEntity.isAlpha());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void WhenKilledAfterTransfuredByAlpha(TransfurVariantEvents.SpawnAtTransfurredEntityEvent spawnAtTransfurredEntityEvent) {
        LivingEntity toReplace = spawnAtTransfurredEntityEvent.spawnAt;
        ChangedEntity source = spawnAtTransfurredEntityEvent.changedEntity;
        if (resolveChangedEntity(toReplace) instanceof IAlphaAbleEntity toReplaceAlpha) {
            if (source instanceof IAlphaAbleEntity alphaSource) {
                alphaSource.setAlpha(toReplaceAlpha.isAlpha());
            }
        }
    }

    public static Entity resolveChangedEntity(Entity entity) {
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> transfur = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfur != null) {
                return transfur.getChangedEntity();
            }
        }
        return entity;
    }


    @SubscribeEvent
    public static void CancelUntransfur(UntransfurEvent untransfurEvent) {
        Player player = untransfurEvent.getPlayer();
        if (ProcessTransfur.getPlayerTransfurVariant(player) instanceof TransfurVariantInstanceExtensor transfurVariantInstanceExtensor) {
            untransfurEvent.setCanceled(transfurVariantInstanceExtensor.getUntransfurImmunity(untransfurEvent.untransfurType));
        }
    }

    public static ChangedAddonVariables.PlayerVariables getPlayerVars(LivingEntity entity) {
        return entity.getCapability(ChangedAddonVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonVariables.PlayerVariables());
    }
}
