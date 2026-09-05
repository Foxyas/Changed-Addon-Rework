package net.foxyas.changedaddon.process.features;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.api.ICustomPatReaction;
import net.foxyas.changedaddon.entity.api.SpecialPatLatex;
import net.foxyas.changedaddon.init.ChangedAddonCriteriaTriggers;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonStatRegistry;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.util.DelayedTask;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.ability.GrabEntityAbility;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Objects;

public class PatFeatureHandle {

    public static boolean canPlayerPat(Player player) {
        return GrabEntityAbility.getControllingEntity(player) == player;
    }

    public static void run(Level level, Player player) {
        if (player == null || player.isSpectator() || !canPlayerPat(player)) return;

        ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.of(player);
        if (vars != null && vars.isPatInCooldown()) return;

        InteractionHand emptyHand = getEmptyHand(player);
        if (emptyHand == null) return;

        EntityHitResult targetEntityResult = PlayerUtil.getEntityHitLookingAt(player, (float) player.getEntityReach(), PlayerUtil.BLOCK_COLLISION, e -> {
            if (e.isSpectator()) return false;
            if (!(e instanceof LivingEntity le)) return false;
            if (GrabEntityAbility.getGrabber(le) == null) return true;

            LivingEntity livingEntity = Objects.requireNonNull(GrabEntityAbility.getGrabber(le)).getEntity();
            return livingEntity != player;
        });
        if (targetEntityResult == null) return;

        Entity targetEntity = targetEntityResult.getEntity();
        if (!(targetEntity instanceof LivingEntity living)) return;


        patEntity(player, living, emptyHand, targetEntityResult);
    }

    public static void patEntity(LivingEntity player, LivingEntity targetEntity, InteractionHand hand) {
        patEntity(player, targetEntity, hand, new EntityHitResult(targetEntity));
    }

    public static void patEntity(LivingEntity player, LivingEntity targetEntity, InteractionHand emptyHand, EntityHitResult targetEntityResult) {
        Level level = player.level;
        if (targetEntity instanceof SpecialPatLatex specialPatLatex) {
            handleSpecialEntities(player, emptyHand, targetEntity, targetEntityResult);
            return;
        }

        if (targetEntity instanceof ChangedEntity changed) {
            handleLatexEntity(player, emptyHand, changed, targetEntityResult, level);
            return;
        }

        if (targetEntity instanceof Player target) {
            handlePlayerEntity(player, emptyHand, target, targetEntityResult, level);
            return;
        }

        if (targetEntity.getType().is(ChangedAddonTags.EntityTypes.PATABLE)) {
            handlePatableEntity(player, emptyHand, targetEntityResult, level);
        }
    }

    private static void handleSpecialEntities(LivingEntity player, InteractionHand emptyHand, LivingEntity target, EntityHitResult entityHitResult) {
        player.swing(emptyHand);
        if (!(target instanceof ICustomPatReaction pat)) return;

        pat.whenPattedReactionSpecific(player, emptyHand, entityHitResult.getLocation());
        pat.whenPattedReaction(player, emptyHand);
        pat.whenPattedReactionSimple();

        if (player instanceof ServerPlayer sPlayer) onPat(sPlayer);
    }

    private static void handleLatexEntity(LivingEntity livingEntity, InteractionHand emptyHand, ChangedEntity target, EntityHitResult entityHitResult, Level level) {
        livingEntity.swing(emptyHand);

        ProcessPatFeature.GlobalPatReactionEvent globalPatReactionEvent = new ProcessPatFeature.GlobalPatReactionEvent(level, livingEntity, emptyHand, target, entityHitResult.getLocation());
        if (ChangedAddonMod.postEvent(globalPatReactionEvent)) {
            return;
        }

        TransfurVariantInstance<?> selfTF = ProcessTransfur.getPlayerTransfurVariant(EntityUtil.playerOrNull(livingEntity));
        if (selfTF != null && selfTF.getChangedEntity() instanceof ICustomPatReaction playerPat) {
            playerPat.whenPatEvent(livingEntity, emptyHand, target);
            playerPat.whenPatEventSpecific(livingEntity, emptyHand, target, entityHitResult);
        }

        if (target instanceof ICustomPatReaction e) {
            e.whenPattedReactionSpecific(livingEntity, emptyHand, entityHitResult.getLocation());
            e.whenPattedReaction(livingEntity, emptyHand);
            e.whenPattedReactionSimple();
        }

        if (livingEntity instanceof ServerPlayer sp) {
            GiveStealthPatAdvancement(sp, target);
            onPat(sp);
        }
    }

    private static void handlePlayerEntity(LivingEntity player, InteractionHand emptyHand, Player target, EntityHitResult entityHitResult, Level level) {
        TransfurVariantInstance<?> selfTF = ProcessTransfur.getPlayerTransfurVariant(EntityUtil.playerOrNull(player));
        TransfurVariantInstance<?> targetTF = ProcessTransfur.getPlayerTransfurVariant(target);

        if (selfTF == null && targetTF == null) {
            return;
        }//Be Able to Pet if at least one is Transfur :P

        player.swing(emptyHand);

        if (selfTF != null && selfTF.getChangedEntity() instanceof ICustomPatReaction playerPat) {
            playerPat.whenPatEvent(player, emptyHand, target);
        }

        if (targetTF != null && targetTF.getChangedEntity() instanceof ICustomPatReaction targetPat) {
            targetPat.whenPattedReactionSpecific(player, emptyHand, entityHitResult.getLocation());
            targetPat.whenPattedReaction(player, emptyHand);
            targetPat.whenPattedReactionSimple();
        }

        ProcessPatFeature.GlobalPatReactionEvent globalPatReactionEvent = new ProcessPatFeature.GlobalPatReactionEvent(level, player, emptyHand, target, entityHitResult.getLocation());
        if (ChangedAddonMod.postEvent(globalPatReactionEvent)) {
            return;
        }

        if (player instanceof ServerPlayer sPlayer)
            onPat(sPlayer);
        if (target instanceof ServerPlayer sPlayer)
            sPlayer.awardStat(ChangedAddonStatRegistry.PATS_RECEIVED.get());

        if (targetTF == null || !(level instanceof ServerLevel)) return;

        if (player instanceof ServerPlayer sPlayer) {
            if (sPlayer.getRandom().nextFloat() > 0.1f + sPlayer.getLuck() * 0.05f) return;
            healAndGiveRarePatAdvancement(sPlayer, target);
        }
    }

    private static void handlePatableEntity(LivingEntity entity, InteractionHand emptyHand, EntityHitResult entityHitResult, Level level) {
        Entity target = entityHitResult.getEntity();
        entity.swing(emptyHand);

        if (target instanceof LivingEntity livingTarget) {//assume that target is always livingEntity or allow entity in the event?
            ChangedAddonMod.postEvent(new ProcessPatFeature.GlobalPatReactionEvent(level, entity, emptyHand, livingTarget, entityHitResult.getLocation()));
            return;
        }

        if (level instanceof ServerLevel serverLevel && entity instanceof Player player) {
            player.displayClientMessage(Component.translatable("key.changed_addon.pat_message", target.getDisplayName().getString()), true);
            serverLevel.sendParticles(ParticleTypes.HEART, target.getX(), target.getY() + 1, target.getZ(), 7, 0.3, 0.3, 0.3, 1);
        }
    }

    private static InteractionHand getEmptyHand(Player player) {
        if (player.getMainHandItem().isEmpty()) return InteractionHand.MAIN_HAND;

        return player.getOffhandItem().isEmpty() ? InteractionHand.OFF_HAND : null;
    }

    public static boolean shouldBeConfused(LivingEntity player, ChangedEntity entity) {
        if (entity instanceof AbstractDarkLatexWolf) {
            // Verificando se o jogador usa a armadura correta
            return player.getItemBySlot(EquipmentSlot.HEAD).is(ChangedAddonItems.DARK_LATEX_HEAD_CAP.get())
                    && player.getItemBySlot(EquipmentSlot.CHEST).is(ChangedAddonItems.DARK_LATEX_COAT.get());
        }
        return false;
    }

    public static void healAndGiveRarePatAdvancement(ServerPlayer player, LivingEntity target) {
        target.heal(6f);
        ChangedAddonCriteriaTriggers.PAT_ENTITY_TRIGGER.trigger(player, target, "chance");
    }

    public static void GiveStealthPatAdvancement(ServerPlayer player, Entity target) {
        ChangedAddonCriteriaTriggers.PAT_ENTITY_TRIGGER.trigger(player, target, "stealth");
    }

    private static void onPat(ServerPlayer player) {
        player.awardStat(ChangedAddonStatRegistry.PATS_GIVEN.get());

        ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.nonNullOf(player);
        vars.patCooldown = 5;
        vars.syncPlayerVariables(player);
    }
}
