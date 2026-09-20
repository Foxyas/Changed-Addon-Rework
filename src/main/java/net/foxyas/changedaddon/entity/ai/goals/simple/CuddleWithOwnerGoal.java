package net.foxyas.changedaddon.entity.ai.goals.simple;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.util.GrabAbilityUtil;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Emote;
import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class CuddleWithOwnerGoal<T extends ChangedEntity & TamableLatexEntity> extends Goal {

    protected static final double MAX_DISTANCE_SQ = 32.0;

    protected GrabEntityAbilityInstance grab;
    protected final T pet;
    protected Player owner;
    protected BlockPos bedPos;

    public CuddleWithOwnerGoal(T pet) {
        this.pet = pet;
    }

    @Override
    public boolean canUse() {
        if (!pet.isTame() || pet.isInWater() || pet.isVehicle()) {
            return false;
        }

        grab = pet.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
        if (grab == null) return false;

        Entity ownerEntity = pet.getOwner();
        if (!(ownerEntity instanceof Player player)) {
            return false;
        }

        this.owner = player;

        if (!ChangedAddonVariables.ofOrDefault(owner).isCuddling || !player.isSleeping() || player.getSleepingPos().isEmpty()
                || !canGrabOwner()) return false;

        bedPos = player.getSleepingPos().get();

        if (!player.level.getBlockState(bedPos).is(BlockTags.BEDS)) return false;

        // Verifica a distância entre o pet e a cama
        if (pet.distanceToSqr(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5) > MAX_DISTANCE_SQ) {
            return false; // Está muito longe, não tenta ir
        }

        return !pet.isSleeping();
    }

    protected boolean canGrabOwner() {
        return !ProcessTransfur.isPlayerTransfurred(owner) || ((GrabEntityAbilityExtensor) grab).canGrabEntity(owner);
    }

    @Override
    public void start() {
        if (bedPos != null) {
            ParticlesUtil.sendParticles(owner.level(),
                    ChangedParticles.emote(pet, Emote.IDEA),
                    new Vec3(
                            pet.getX(),
                            pet.getY() + (double) pet.getDimensions(pet.getPose()).height + 0.65,
                            pet.getZ()
                    ),
                    Vec3.ZERO,
                    0,
                    1
            );
            pet.getNavigation().moveTo(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5, 0.7);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return ChangedAddonVariables.ofOrDefault(owner).isCuddling && owner != null && owner.isSleeping() && canGrabOwner();
    }

    @Override
    public void tick() {
        if (pet.isSleeping()) {
            ((GrabEntityAbilityExtensor) grab).setSafeMode(true);
            pet.getNavigation().stop();
            Vec3 pVec = Vec3.atCenterOf(pet.getSleepingPos().orElse(bedPos));
            double distanceToBed = pet.distanceToSqr(pVec);

            if (distanceToBed >= 1.5f) {
                pet.setPos(pVec);
            }
            return;
        }

        if (bedPos == null) return;

        double distanceToBed = pet.distanceToSqr(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5);

        if (distanceToBed >= 1.5f) {
            pet.getNavigation().moveTo(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5, 0.3);
        } else if (grab.grabbedEntity != owner) {
            pet.startSleeping(owner.getSleepingPos().orElse(bedPos));
            ((GrabEntityAbilityExtensor) grab).setSafeMode(true);
            GrabAbilityUtil.grabEntity(owner, pet, grab);
        }
    }

    @Override
    public void stop() {
        if (pet.isSleeping()) {
            pet.stopSleeping();
        }

        if (grab.grabbedEntity == owner) {
            GrabAbilityUtil.releaseEntity(owner, pet, grab, false);
            ((GrabEntityAbilityExtensor) grab).setSafeMode(false);
        }

        this.owner = null;
        this.bedPos = null;
    }
}
