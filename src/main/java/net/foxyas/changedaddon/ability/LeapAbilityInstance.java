package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.ability.api.AbilityInstanceExtension;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class LeapAbilityInstance extends AbstractAbilityInstance implements AbilityInstanceExtension {

    private boolean justJumpWhileHolding = false;

    public LeapAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    @Override
    public AbstractAbility.UseType getUseType() {
        return AbstractAbility.UseType.HOLD;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canKeepUsing() {
        return canUse();
    }

    @Override
    public void startUsing() {
    }

    @Override
    public void tick() {
    }

    public void tickCharge() {
        this.tick();
    }

    @Override
    public void stopUsing() {
        if (entity.getLevel().isClientSide()) return;
        if (!(entity.getEntity() instanceof Player)) {
            return;
        }

        // Trigger leap when releasing the button (if not already triggered by jump)
        makeEntityLeap(this.entity, false);
    }

    @Override
    public void tickIdle() {
        super.tickIdle();
    }

    @Override
    public void onEntityJump() {
        if (entity.getLevel().isClientSide()) return;
        if (this.getController().getHoldTicks() > 0) {
            this.justJumpWhileHolding = true;
        }


        if (justJumpWhileHolding) {
            makeEntityLeap(this.entity, true);
            this.justJumpWhileHolding = false;
            // End ability charge usage
            AbstractAbility.Controller controller = this.getController();
            getUseType().check(false, true, true, controller);
            controller.resetCharge();
            controller.resetHoldTicks();
            controller.applyCoolDown();
        }

    }

    public static void makeEntityLeap(IAbstractChangedEntity iAbstractChangedEntity, boolean isFromJumping) {
        if (!(iAbstractChangedEntity.getEntity() instanceof Player player) || player.getFoodData().getFoodLevel() <= 6) {
            return;
        }

        if (player.isInWater() || player.isSpectator()) {
            return;
        }

        if (!player.onGround() && !isFromJumping) {
            return;
        }

        float speed = isFromJumping ? 0.45f : 0.6f; // Slightly reduce base speed if triggered by jumping
        double motionX, motionY, motionZ;

        if (!player.isShiftKeyDown()) {
            // Normal Leap
            Vec3 leapVec = player.getViewVector(1).multiply(speed, speed, speed);

            // If jumping, dampen the Y component to prevent stacking full jump + full leap Y momentum
            if (isFromJumping) {
                leapVec = new Vec3(leapVec.x, leapVec.y * 0.025D, leapVec.z);
            }

            Vec3 newMotion = player.getDeltaMovement().add(leapVec);

            // Cap maximum upward Y velocity (e.g. max 0.25D)
            if (isFromJumping && newMotion.y > 0.25D) {
                newMotion = new Vec3(newMotion.x, 0.25D, newMotion.z);
            }

            player.setDeltaMovement(newMotion);
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer.getId(), serverPlayer.getDeltaMovement()));
            }
            player.hasImpulse = true;
            playSound(player);
            exhaustPlayer(player, 0.5F);

        } else {
            // Precision Leap
            double targetY = player.getViewVector(1).y;
            motionX = -Math.sin(Math.toRadians(player.getYRot())) * 0.15;
            motionY = targetY * 0.8F;

            if (isFromJumping) {
                motionY *= 0.015F; // Reduce Y scaling on jump
            }

            motionZ = Math.cos(Math.toRadians(player.getYRot())) * 0.15;
            float entityAlphaScale = IAlphaAbleEntity.getEntityAlphaScaleWithCheck(iAbstractChangedEntity.getChangedEntity());
            float multiplier = (iAbstractChangedEntity.getSelfVariant() == ChangedAddonTransfurVariants.LATEX_SNEP_FERAL.get()
                    || iAbstractChangedEntity.getSelfVariant() == ChangedAddonTransfurVariants.LATEX_SNEP_FERAL_FORM.get() ? 1.3F : 1) + entityAlphaScale;

            Vec3 newMotion = player.getDeltaMovement().add(motionX, motionY * multiplier, motionZ);

            // Cap max Y motion for precision leap when jumping (e.g. max 0.6D)
            if (isFromJumping && newMotion.y > 0.6D) {
                newMotion = new Vec3(newMotion.x, 0.6D, newMotion.z);
            }

            player.setDeltaMovement(newMotion);
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer.getId(), serverPlayer.getDeltaMovement()));
            }
            player.hasImpulse = true;
            playSound(player);
            applyFatigue(player, motionY);

            // Grant Advancement
            if (motionY * multiplier >= 0.75) {
                grantAdvancement(player, "changed_addon:leaper");
            }
        }
    }

    public static void playSound(Player player) {
        if (!player.level.isClientSide()) {
            player.level.playSound(null, player.blockPosition(), ChangedSounds.CARDBOARD_BOX_OPEN.get(),
                    player.getSoundSource(), 2.5F, 1.0F);
        }
    }

    public static void exhaustPlayer(Player player, float exhaustion) {
        if (!player.isCreative()) {
            player.causeFoodExhaustion(exhaustion);
        }
    }

    public static void applyFatigue(Player player, double motionY) {
        if (!player.isCreative()) {
            player.causeFoodExhaustion((float) (motionY * 0.25));
        }
    }

    public static void grantAdvancement(Player player, String advancementId) {
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
}