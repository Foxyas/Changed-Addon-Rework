package net.foxyas.changedaddon.entity.ai;

import net.foxyas.changedaddon.entity.api.TamableLatexEntityFavors;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraftforge.network.PacketDistributor;

@Deprecated
public class LatexSuitOwnerGoal extends MeleeAttackGoal {
    protected final ChangedEntity entity;
    private final TamableLatexEntityFavors iEntity;

    public LatexSuitOwnerGoal(TamableLatexEntityFavors latexEntityFavors, double speedModifier, boolean visualPersistence) {
        super(latexEntityFavors.getSelf(), speedModifier, visualPersistence);

        this.entity = latexEntityFavors.getSelf();
        this.iEntity = latexEntityFavors;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double distanceSquared) {
        var ability = iEntity.getGrabAbility();
        if (ability == null) {
            entity.setTarget(null);
            iEntity.setFavor(LatexFavor.NONE);
            return;
        }

        if (target == iEntity.getOwner()) {
            double reachSqr = this.getAttackReachSqr(target) * 0.9;

            if (distanceSquared <= reachSqr && this.getTicksUntilNextAttack() <= 0) {
                this.resetAttackCooldown();

                if (ability.suitEntity(target)) {
                    ability.grabbedHasControl = true;
                    Changed.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                            new GrabEntityPacket(entity, target, GrabEntityPacket.GrabType.SUIT));
                    ChangedSounds.broadcastSound(entity, ChangedSounds.LATEX_SUIT_ENTITY, 1.0f, 1.0f);
                }
            }
        } else {
            // Re-evaluate nearby entities
            entity.setTarget(null);
        }
    }

    @Override
    public boolean canUse() {
        if (this.iEntity.getCurrentFavor() != LatexFavor.SUIT_OWNER)
            return false;
        var owner = this.iEntity.getOwner();
        if (owner == null)
            return false;

        var ability = iEntity.getGrabAbility();
        if (ability == null || ability.grabbedEntity == owner)
            return false;

        if (ProcessTransfur.isPlayerTransfurred(EntityUtil.playerOrNull(owner)))
            return false;

        this.entity.setTarget(owner);
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.iEntity.getCurrentFavor() != LatexFavor.SUIT_OWNER)
            return false;
        var owner = this.iEntity.getOwner();
        if (owner == null)
            return false;

        var ability = iEntity.getGrabAbility();
        if (ability == null || ability.grabbedEntity == owner)
            return false;

        if (ProcessTransfur.isPlayerTransfurred(EntityUtil.playerOrNull(owner)))
            return false;

        this.entity.setTarget(owner);
        return super.canContinueToUse();
    }
}