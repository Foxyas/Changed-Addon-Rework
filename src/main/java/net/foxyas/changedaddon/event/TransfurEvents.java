package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.entity.advanced.DazedLatexEntity;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.simple.DarkLatexYufengQueenEntity;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.foxyas.changedaddon.variant.VariantExtraStats;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.foxyas.changedaddon.event.TransfurVariantEvents.OverrideSourceTransfurVariantEvent.TransfurType;

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
                if (wantAbsorption && transfurContext.cause != TransfurCause.GRAB_REPLICATE) {
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
//
//    @SubscribeEvent
//    public static void AfterPlayerTransfur(ProgressTransfurEvents.onPostProcessPlayerTransfur onPostProcessPlayerTransfur) {
//        onPostProcessPlayerTransfur.setCanceled(true);
//        onPostProcessPlayerTransfur.callDefault();
//        ChangedEntity changedEntity = onPostProcessPlayerTransfur.getTransfurVariantInstance().getChangedEntity();
//        if (changedEntity instanceof IAlphaAbleEntity iAlphaAbleEntity) {
//            iAlphaAbleEntity.setAlpha(true);
//            iAlphaAbleEntity.setAlphaScale(2);
//        }
//    }


    @SubscribeEvent
    public static void ModifyAbsorptionVariant(TransfurVariantEvents.OverrideSourceTransfurVariantEvent event) {
        TransfurVariant<?> original = event.getOriginal();
        ChangedEntity changedEntity = event.getChangedEntity();
        IAbstractChangedEntity source = event.getSource();

        if (source.getChangedEntity() instanceof DarkLatexYufengQueenEntity latexYufengQueenEntity) {
            TransfurVariant<?> selfVariant = latexYufengQueenEntity.getTransfurVariantFor(event.getTransfurType());
            if (original != selfVariant) {
                event.setVariant(selfVariant);
            }
        } else if (changedEntity instanceof DarkLatexYufengQueenEntity latexYufengQueenEntity) {
            TransfurVariant<?> selfVariant = latexYufengQueenEntity.getTransfurVariantFor(event.getTransfurType());
            if (original != selfVariant) {
                event.setVariant(selfVariant);
            }
        }
//        else if (changedEntity instanceof VariantExtraStats variantExtraStats) {
//            TransfurVariant<?> selfVariant = variantExtraStats.getTransfurVariantFor(event.getTransfurType());
//            if (original != selfVariant) {
//                event.setVariant(selfVariant);
//            }
//        }
    }

    @SubscribeEvent
    public static void syncFusionFromAlphaState(TransfurVariantEvents.KillAfterTransfurredFinalEvent event) {
        LivingEntity source = event.getSource();
        LivingEntity targetEntity = event.getTargetEntity();
        if (!(source instanceof IAlphaAbleEntity sourceAlpha)) {
            return;
        }

        if (targetEntity instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant != null && transfurVariant.getChangedEntity() instanceof IAlphaAbleEntity iAlphaAbleEntity) {
                iAlphaAbleEntity.setAlpha(sourceAlpha.isAlpha());
                iAlphaAbleEntity.setAlphaScale(sourceAlpha.alphaAdditionalScale());
            }
            return;
        }

        if (targetEntity instanceof IAlphaAbleEntity targetAlpha) {
            targetAlpha.setAlphaScale(sourceAlpha.alphaAdditionalScale());
            targetAlpha.setAlpha(sourceAlpha.isAlpha());
        }

    }

    @SubscribeEvent
    public static void syncFusionFromAlphaState(TransfurVariantEvents.OnPlayerFuseWithOther event) {
        LivingEntity source = event.getSource();
        ChangedEntity sourceChanged = event.getSourceChangedEntity();
        LivingEntity target = event.getTarget();

        if (!(sourceChanged instanceof IAlphaAbleEntity sourceAlpha)) {
            return;
        }

        TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(EntityUtil.playerOrNull(source));
        if (transfurVariant == null) {
            return;
        }

        if (!(transfurVariant.getChangedEntity() instanceof IAlphaAbleEntity finalAlpha)) {
            return;
        }

        // -------------------------
        // CASO TARGET SEJA PLAYER
        // -------------------------
        if (target instanceof Player player) {
            TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);

            if (variant != null && variant.getChangedEntity() instanceof IAlphaAbleEntity targetAlpha) {

                // Se o perdedor for alpha, passa pro outro
                if (targetAlpha.isAlpha()) {
                    finalAlpha.setAlpha(true);
                    finalAlpha.setAlphaScale(targetAlpha.alphaAdditionalScale());
                }

                // Se o source for alpha, passa pro target
                if (sourceAlpha.isAlpha()) {
                    finalAlpha.setAlpha(true);
                    finalAlpha.setAlphaScale(sourceAlpha.alphaAdditionalScale());
                }
            }

            return;
        }

        // -------------------------
        // CASO TARGET SEJA ENTITY NORMAL
        // -------------------------
        if (target instanceof IAlphaAbleEntity targetAlpha) {

            if (targetAlpha.isAlpha()) {
                finalAlpha.setAlpha(true);
                finalAlpha.setAlphaScale(targetAlpha.alphaAdditionalScale());
            }

            if (sourceAlpha.isAlpha()) {
                finalAlpha.setAlpha(true);
                finalAlpha.setAlphaScale(sourceAlpha.alphaAdditionalScale());
            }
        }
    }

    @SubscribeEvent
    public static void syncFusionFromAlphaState(TransfurVariantEvents.OnEntityFuseWithOther event) {
        ChangedEntity sourceChanged = event.getSource();
        LivingEntity target = event.getTarget();

        if (!(sourceChanged instanceof IAlphaAbleEntity sourceAlpha)) {
            return;
        }

        TransfurVariantInstance<?> transfurVariant = event.getOldVariantInstance();
        if (transfurVariant == null) {
            return;
        }

        if (!(transfurVariant.getChangedEntity() instanceof IAlphaAbleEntity oldAlphaState)) {
            return;
        }

        if (target instanceof Player player) {
            TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);

            if (variant != null && variant.getChangedEntity() instanceof IAlphaAbleEntity targetAlpha) {

                if (oldAlphaState.isAlpha()) {
                    targetAlpha.setAlpha(true);
                    targetAlpha.setAlphaScale(targetAlpha.alphaAdditionalScale());
                }

                if (sourceAlpha.isAlpha()) {
                    targetAlpha.setAlpha(true);
                    targetAlpha.setAlphaScale(sourceAlpha.alphaAdditionalScale());
                }
            }
        }
    }

    @SubscribeEvent
    public static void makeDazedLatexBuffAfterGrabAssimilation(TransfurVariantEvents.OverrideSourceTransfurVariantEvent event) {
        LivingEntity target = event.getTarget();
        IAbstractChangedEntity source = event.getSource();
        if (!(source.getChangedEntity() instanceof DazedLatexEntity)) return;
        
        if (event.getTransfurType() == TransfurType.ABSORPTION) {
            source.getAbilityInstanceSafe(ChangedAbilities.GRAB_ENTITY_ABILITY.get()).ifPresent((grabEntityAbilityInstance) -> {
                if (grabEntityAbilityInstance.grabbedEntity == target) {
                    event.setVariant(ChangedAddonTransfurVariants.BUFF_DAZED_LATEX.get());
                }
            });
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
