package net.foxyas.changedaddon.entity.ai.goals.simple;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
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

public class CuddleWithOwnerGoal <T extends ChangedEntity & TamableLatexEntity> extends Goal {

    protected static final double MAX_DISTANCE_SQ = 32.0;

    GrabEntityAbilityInstance grab;
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

        if (!ChangedAddonVariables.ofOrDefault(owner).isCuddling || !player.isSleeping() || player.getSleepingPos().isEmpty()
                || !canGrabOwner()) return false;

        this.owner = player;
        bedPos = player.getSleepingPos().get();

        if (!player.level.getBlockState(bedPos).is(BlockTags.BEDS)) return false;

        // Verifica a distância entre o pet e a cama
        if (pet.distanceToSqr(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5) > MAX_DISTANCE_SQ) {
            return false; // Está muito longe, não tenta ir
        }

        return !pet.isSleeping();
    }

    protected boolean canGrabOwner() {
        return !ProcessTransfur.isPlayerTransfurred(owner) || ((GrabEntityAbilityExtensor)grab).canGrabEntity(owner);
    }

    @Override
    public void start() {
        if (bedPos != null) {
            if (owner.level().isClientSide()) {//FIXME never client in ai
                owner.level().addParticle(
                        ChangedParticles.emote(pet, Emote.IDEA),
                        pet.getX(),
                        pet.getY() + (double) pet.getDimensions(pet.getPose()).height + 0.65,
                        pet.getZ(),
                        0.0f,
                        0.0f,
                        0.0f
                );
            }
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
            pet.getNavigation().stop();
            return;
        }

        if (bedPos == null) return;

        double distanceToBed = pet.distanceToSqr(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5);

        if (distanceToBed >= 1.5f) {
            pet.getNavigation().moveTo(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5, 0.3);
        } else if(grab.grabbedEntity != owner) {
            ((GrabEntityAbilityExtensor)grab).setSafeMode(true);
            //TODO grab
        }
    }

    @Override
    public void stop() {
        if (pet.isSleeping()) {
            pet.stopSleeping();
        }

        if (grab.grabbedEntity == owner) grab.releaseEntity(false);

        this.owner = null;
        this.bedPos = null;
    }
}
