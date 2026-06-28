package net.foxyas.changedaddon.entity.ai.goals.abilities;

import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.mixins.abilities.AbilityControllerAccessor;
import net.foxyas.changedaddon.util.EntityUtils;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.ability.GrabEntityAbility;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.Util;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PacketDistributor;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MayGrabTargetGoal extends Goal {

    public final Map<IGrabberEntity.GrabStrategy, BiConsumer<LivingEntity, GrabEntityAbilityInstance>> grabStrategyMap = Util.make(new HashMap<>(), map -> {
        map.put(IGrabberEntity.GrabStrategy.GRAB, this::grabEntity);
        map.put(IGrabberEntity.GrabStrategy.SUIT, this::suitEntity);
    });

    private final IGrabberEntity grabber;

    public MayGrabTargetGoal(IGrabberEntity grabber) {
        this.grabber = grabber;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean isInterruptable() {
        return super.isInterruptable();
    }

    @Override
    public boolean canUse() {
        if (!(grabber instanceof LivingEntity living)) return false;
        boolean canEntityGrab = grabber.canEntityGrab(living.getType(), living.level);
        if (!canEntityGrab) return false;

        GrabEntityAbilityInstance grabAbilityInstance = grabber.getGrabAbilityInstance();
        LivingEntity target = grabber.asMob().getTarget();
        if (target == null) return false;
        if (target instanceof Player player && ProcessTransfur.isPlayerTransfurred(player)) return false;

        double reachSqr = grabber.asMob().getMeleeAttackRangeSqr(target) * 0.7f; //Closer than a normal punch
        EntityDimensions dimensions = living.getDimensions(living.getPose()).scale(1.25f);
        AABB grabReach = dimensions.makeBoundingBox(living.position());
        if (grabAbilityInstance == null) return false;
        if (GrabEntityAbility.getGrabber(target) != null) return false;
        if (grabber.getGrabCooldown() > 0) return false;
        if (!target.getType().is(ChangedTags.EntityTypes.HUMANOIDS))
            return false;

        return (grabReach.contains(target.position()) || target.distanceToSqr(grabber.asMob()) <= reachSqr) && grabAbilityInstance.grabbedEntity == null;
    }

    @Override
    public void tick() {
        super.tick();
        tryGrabNearbyTarget();
    }

    @Override
    public void start() {
        super.start();
        tryGrabNearbyTarget();
    }

    @Override
    public void stop() {
        super.stop();
        GrabEntityAbilityInstance grabAbilityInstance = grabber.getGrabAbilityInstance();
        if (grabAbilityInstance != null && grabAbilityInstance.grabbedEntity == null) {
            grabAbilityInstance.getController().resetHoldTicks();
        }
    }

    private void tryGrabNearbyTarget() {
        PathfinderMob living = grabber.asMob();
        LivingEntity target = living.getTarget();
        if (!living.level().isClientSide()) {
            GrabEntityAbilityInstance grabAbilityInstance = grabber.getGrabAbilityInstance();
            EntityDimensions dimensions = living.getDimensions(living.getPose()).scale(1.25f);
            AABB grabReach = dimensions.makeBoundingBox(living.position());
            if (target != null && (grabReach.contains(target.position()) || target.distanceTo(living) <= EntityUtils.getAttributeValueSafe(living, ForgeMod.ENTITY_REACH.get()).orElse(2.5f))) {
                if (grabAbilityInstance != null) {
                    if (grabAbilityInstance.getController() instanceof AbilityControllerAccessor accessor)
                        accessor.setHoldTicks(20);
                    mayGrabEntity(target, grabAbilityInstance);
                }
            } else {
                if (grabAbilityInstance != null) {
                    if (grabAbilityInstance.getController() instanceof AbilityControllerAccessor accessor)
                        accessor.setHoldTicks(20);
                }
            }
        }
    }

    protected void mayGrabEntity(LivingEntity target, GrabEntityAbilityInstance grabAbilityInstance) {
        LivingEntity grabbedEntity = grabAbilityInstance.grabbedEntity;
        if (grabbedEntity == null && GrabEntityAbility.getGrabber(target) == null) {
            grabStrategyMap.get(this.grabber.getGrabStrategy()).accept(target, grabAbilityInstance);
        }
    }

    protected void suitEntity(LivingEntity target, GrabEntityAbilityInstance grabAbilityInstance) {
        if (grabAbilityInstance.suitEntity(target)) {
            Changed.PACKET_HANDLER.send(
                    PacketDistributor.TRACKING_ENTITY.with(grabber::asMob),
                    new GrabEntityPacket(grabber.asMob(), target, GrabEntityPacket.GrabType.SUIT)
            );

            ProcessTransfur.forceNearbyToRetarget(target.level(), target);

            grabber.asMob().setTarget(null);

            // som (opcional, pode mudar)
            ChangedSounds.broadcastSound(
                    grabber.asMob(),
                    ChangedSounds.LATEX_GRAB_ENTITY,
                    1.0f,
                    1.0f
            );

            grabber.applyGrabCooldown(0);
        }
    }

    protected void grabEntity(LivingEntity target, GrabEntityAbilityInstance grabAbilityInstance) {
        if (grabAbilityInstance.grabEntity(target)) {
            Changed.PACKET_HANDLER.send(
                    PacketDistributor.TRACKING_ENTITY.with(grabber::asMob),
                    new GrabEntityPacket(grabber.asMob(), target, GrabEntityPacket.GrabType.ARMS)
            );

            ProcessTransfur.forceNearbyToRetarget(target.level(), target);

            grabber.asMob().setTarget(null);

            // som (opcional, pode mudar)
            ChangedSounds.broadcastSound(
                    grabber.asMob(),
                    ChangedSounds.LATEX_GRAB_ENTITY,
                    1.0f,
                    1.0f
            );

            grabber.applyGrabCooldown(0);
        }
    }
}
