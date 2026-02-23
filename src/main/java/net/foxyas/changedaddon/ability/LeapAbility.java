package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.advanced.LatexSnepEntity;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class LeapAbility extends SimpleAbility {

    public LeapAbility() {
        super();
    }

    private static void leapAbility(Entity entity, IAbstractChangedEntity iAbstractChangedEntity) {
        if (!(entity instanceof Player player) || player.getFoodData().getFoodLevel() <= 6) {
            return;
        }

        if (!player.onGround() || player.isInWater() || player.isSpectator()) {
            return;
        }

        double speed = 0.6;
        double motionX, motionY, motionZ;

        if (!player.isShiftKeyDown()) {
            // Normal Leap
            player.setDeltaMovement(player.getDeltaMovement().add(player.getViewVector(1).multiply(speed, speed, speed)));
            playSound(player);
            exhaustPlayer(player, 0.5F);

        } else {
            // Precision Leap
            double targetY = player.getViewVector(1).y;
            motionX = -Math.sin(Math.toRadians(player.getYRot())) * 0.15;
            motionY = targetY * 0.8F;
            motionZ = Math.cos(Math.toRadians(player.getYRot())) * 0.15;
            float multiplier = (iAbstractChangedEntity.getSelfVariant() == ChangedAddonTransfurVariants.LATEX_SNEP.get()
                    || iAbstractChangedEntity.getSelfVariant() == ChangedAddonTransfurVariants.LATEX_SNEP_FERAL_FORM.get() ? 1.3F : 1) + IAlphaAbleEntity.getEntityAlphaScale(iAbstractChangedEntity.getChangedEntity());

            player.setDeltaMovement(player.getDeltaMovement().add(motionX, motionY * multiplier, motionZ));
            playSound(player);
            applyFatigue(player, motionY);

            // Grant Advancement
            if (motionY * multiplier >= 0.75) {
                //player.displayClientMessage(Component.literal("Message" + motionY * multiplier), true);
                grantAdvancement(player, "changed_addon:leaper");
            }
        }
    }

    private static void playSound(Player player) {
        if (!player.level.isClientSide()) {
            player.level.playSound(null, player.blockPosition(), ChangedSounds.CARDBOARD_BOX_OPEN.get(),
                    player.getSoundSource(), 2.5F, 1.0F);
        }
    }

    private static void exhaustPlayer(Player player, float exhaustion) {
        if (!player.isCreative()) {
            player.causeFoodExhaustion(exhaustion);
        }
    }

    private static void applyFatigue(Player player, double motionY) {
        if (!player.isCreative()) {
            player.causeFoodExhaustion((float) (motionY * 0.25));
        }
    }

    private static void grantAdvancement(Player player, String advancementId) {
        if (!(player instanceof ServerPlayer serverPlayer) ||
                !(serverPlayer.level instanceof ServerLevel)) {
            return;
        }

        Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(ResourceLocation.parse(advancementId));
        if (advancement == null) return;


        AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
        if (!progress.isDone()) {
            for (String criterion : progress.getRemainingCriteria()) {
                serverPlayer.getAdvancements().award(advancement, criterion);
            }
        }
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.leap");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/leap_ability.png");
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        TransfurVariant<?> variant = entity.getChangedEntity().getSelfVariant();
        return variant.is(ChangedAddonTags.TransfurTypes.CAT_LIKE) ||
                variant.is(ChangedAddonTags.TransfurTypes.LEOPARD_LIKE);
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof LatexSnepEntity) {
            return UseType.INSTANT;
        }
        return UseType.CHARGE_RELEASE;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        return 2;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 15;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        leapAbility(entity.getEntity(), entity);
    }
}
