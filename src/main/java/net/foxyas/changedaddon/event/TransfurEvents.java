package net.foxyas.changedaddon.event;

import com.mojang.datafixers.util.Either;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.item.armor.HazardBodySuit;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.ILatexAssimilatedEntity;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.ai.LatexAssimilationDecision;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAccessorySlots;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

import static net.ltxprogrammer.changed.process.TransfurEvents.*;

@Mod.EventBusSubscriber
public class TransfurEvents {

    @SubscribeEvent
    public static void WhenTransfurred(ProcessTransfur.EntityVariantAssigned changedVariantEvent) {
        TransfurVariant<?> variant = changedVariantEvent.originalVariant;
        if (variant == null) return;

        LivingEntity entity = changedVariantEvent.livingEntity;
        if (!entity.level.getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.NEED_PERMISSION_FOR_BOSS_TRANSFUR))
            return;

        if (variant.is(ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS) && !getVarsIfPlayerOrDef(entity).Exp009TransfurAllowed) {
            changedVariantEvent.variant = ChangedAddonTransfurVariants.EXPERIMENT_009.get();
        }
        if (variant.is(ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS) && !getVarsIfPlayerOrDef(entity).Exp10TransfurAllowed) {
            changedVariantEvent.variant = ChangedAddonTransfurVariants.EXPERIMENT_10.get();
        }
    }


    @SubscribeEvent
    public static void WhenTransfurredByAlpha(AssimilationDecisionEvent event) {
        event.appendTransfurListener(newEntity -> {
            if (newEntity == null) return;

            TransfurVariantInstance<?> instance = newEntity.getTransfurVariantInstance();

            if (instance == null || instance.transfurContext == null) return;

            TransfurContext context = instance.transfurContext;
            Either<IAbstractChangedEntity, ILatexAssimilatedEntity> contextSource = context.source();
            if (contextSource == null) return;

            contextSource.ifLeft(source -> {
                if (!source.wantAbsorption() || context.cause() == TransfurCause.GRAB_REPLICATE) return;

                if (source.getChangedEntity() instanceof IAlphaAbleEntity alphaSource && newEntity.getChangedEntity() instanceof IAlphaAbleEntity targetAlpha) {
                    targetAlpha.setAlpha(alphaSource.isAlpha());
                    targetAlpha.setAlphaScale(alphaSource.alphaAdditionalScale());
                }
            });
        });
    }

    @SubscribeEvent
    public static void HazardSuitTryAbsorbTarget(LatexAssimilationDecisionEvent event) {
        LatexAssimilationDecision<?> original = event.getOriginalDecision();
        if (original.method() != LatexAssimilationDecision.Method.ABSORPTION) return;

        LivingEntity sourceEntity = event.getSourceEntity();
        Optional<AccessorySlots> forEntity = AccessorySlots.getForEntity(EntityUtil.maybeGetUnderlying(sourceEntity));
        if (forEntity.isEmpty()) return;

        AccessorySlots accessorySlots = forEntity.get();
        Optional<ItemStack> item = accessorySlots.getItem(ChangedAccessorySlots.FULL_BODY.get());
        if (item.isEmpty()) return;

        ItemStack stack = item.get();
        if (stack.getItem() instanceof HazardBodySuit) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void cancelNonAlphaFusions(LatexFusionDecisionEvent event) {
        // Obtém as entidades subjacentes (importante para lidar com overlays/variantes)
        LivingEntity sourceEntity = EntityUtil.maybeGetUnderlying(event.getSourceEntity());
        LivingEntity targetEntity = EntityUtil.maybeGetUnderlying(event.getTargetEntity());

        // Se a origem da fusão for um jogador, ignoramos a regra de hierarquia
        if (sourceEntity instanceof Player) {
            return;
        }

        // Verifica se ambos possuem a capacidade de serem Alpha
        if (!(sourceEntity instanceof IAlphaAbleEntity sourceAlpha) || !(targetEntity instanceof IAlphaAbleEntity targetAlpha)) {
            return;
        }

        // Lógica: Se o que inicia a fusão NÃO é alpha, mas o alvo É alpha, o instinto impede
        if (!sourceAlpha.isAlpha() && targetAlpha.isAlpha()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void WhenKilledAfterTransfurredByAlpha(ReplaceEntityEvent event) {
        LivingEntity toReplace = event.getEntityToReplace();
        ChangedEntity replacement = event.getReplacementEntity();

        if (!(resolveChangedEntity(toReplace) instanceof IAlphaAbleEntity toReplaceAlpha)) return;

        if (replacement instanceof IAlphaAbleEntity alphaReplacement) {
            alphaReplacement.setAlpha(toReplaceAlpha.isAlpha());
            alphaReplacement.setAlphaScale(toReplaceAlpha.alphaAdditionalScale());
        }
    }

    @SubscribeEvent
    public static void syncFusionFromAlphaState(LatexFusionEvent event) {
        LivingEntity source = EntityUtil.maybeGetOverlaying(event.getSourceEntity());
        if (!(source instanceof IAlphaAbleEntity sourceAlpha)) {
            return;
        }

        LivingEntity targetEntity = EntityUtil.maybeGetOverlaying(event.getTargetEntity());
        if (targetEntity instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant != null && transfurVariant.getChangedEntity() instanceof IAlphaAbleEntity iAlphaAbleEntity) {
                iAlphaAbleEntity.setAlpha(sourceAlpha.isAlpha());
                iAlphaAbleEntity.setAlphaScale(sourceAlpha.alphaAdditionalScale());
            }
            return;
        }

        if (targetEntity instanceof IAlphaAbleEntity targetAlpha) {
            targetAlpha.setAlpha(sourceAlpha.isAlpha());
            targetAlpha.setAlphaScale(sourceAlpha.alphaAdditionalScale());
        }
    }

//  Foxyas: Keep this code as an Example code on how to use the new Event. check DazedLatexEntity$makeLatexAssimilationDecision to see how to use on a entity
//    @SubscribeEvent
//    public static void makeDazedLatexBuffAfterGrabAssimilation(LatexAssimilationDecisionEvent event) {
//        LatexAssimilationDecision<?> decision = event.getDecision();
//        LivingEntity target = event.getEntity();
//        Either<IAbstractChangedEntity, ILatexAssimilatedEntity> source = decision.context().source();
//        if (source == null) return;
//        if (target.level().isClientSide()) return;
//        source.ifLeft(sourceEntity -> {
//            if (!(sourceEntity.getChangedEntity() instanceof DazedLatexEntity) || decision.method() != LatexAssimilationDecision.Method.ABSORPTION) return;
//            sourceEntity.getAbilityInstanceSafe(ChangedAbilities.GRAB_ENTITY_ABILITY.get()).ifPresent(abilityInstance -> {
//                if (abilityInstance.grabbedEntity == target)
//                    event.setTransfurVariant(ChangedAddonTransfurVariants.BUFF_DAZED_LATEX.get());
//            });
//        });
//    }

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
        if (ProcessTransfur.getPlayerTransfurVariant(player) instanceof TransfurVariantInstanceExtensor ext) {
            untransfurEvent.setCanceled(ext.getUntransfurImmunity(untransfurEvent.untransfurType));
        }
    }

    public static ChangedAddonVariables.PlayerVariables getVarsIfPlayerOrDef(LivingEntity entity) {
        return entity.getCapability(ChangedAddonVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonVariables.PlayerVariables());
    }
}
