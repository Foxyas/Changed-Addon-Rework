package net.foxyas.changedaddon.entity.CustomHandle;

import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class SleepingWithOwnerGoal extends Goal {
    private static final double MAX_DISTANCE_SQ = 100.0; // 10 blocos ao quadrado
    private final LivingEntity pet;
    private Player owner;
    private BlockPos bedPos;
    private int sleepTimer = 0;

    public SleepingWithOwnerGoal(LivingEntity pet) {
        this.pet = pet;
    }

    @Override
    public boolean canUse() {
        if (!(pet instanceof TamableLatexEntity tamablePet)) {
            return false; // Só pode ser usado por entidades domáveis
        }

        if (!tamablePet.isTame() || pet.isInWater() || pet.isVehicle()) {
            return false;
        }

        // Obtém o dono da entidade
        Entity ownerEntity = tamablePet.getOwner();
        if (!(ownerEntity instanceof Player player)) {
            return false;
        }

        // Verifica se o dono está dormindo e pega a posição da cama
        if (player.isSleeping()) {
            this.owner = player;
            this.bedPos = player.getSleepingPos().orElse(null);

            if (bedPos == null) {
                return false; // Se a cama não existir, cancela
            }

            // Verifica a distância entre o pet e a cama
            if (pet.distanceToSqr(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5) > MAX_DISTANCE_SQ) {
                return false; // Está muito longe, não tenta ir
            }

            return !pet.isSleeping();
        }

        return false;
    }

    @Override
    public void start() {
        if (bedPos != null && pet instanceof PathfinderMob pathfinderPet) {
            // Move o pet para a cama com velocidade reduzida
            pathfinderPet.getNavigation().moveTo(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5, 0.7);
            pet.playSound(SoundEvents.CAT_PURREOW, 1.0F, 1.0F); // Toca o som de ronronar
        }
    }

    @Override
    public boolean canContinueToUse() {
        // O pet só pode continuar dormindo se o dono ainda estiver dormindo
        return owner != null && owner.isSleeping();
    }

    @Override
    public void tick() {
        if (bedPos != null) {
            double distanceToBed = pet.distanceToSqr(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5);

            if (distanceToBed > 1 && pet instanceof PathfinderMob pathfinderPet) {
                // Reduz velocidade conforme se aproxima
                pathfinderPet.getNavigation().moveTo(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5, 0.3);
            } else {
                // Se está no bloco correto, começa a dormir
                sleepTimer++;
                //if (sleepTimer == 5) {
                    //pet.playSound(SoundEvents.CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
                //}
                if (sleepTimer == 10) { // Espera 0,5 segundo antes de dormir
                    //pet.playSound(SoundEvents.CAT_EAT, 1.0F, 1.0F);
                    pet.startSleeping(bedPos);
                }
            }
        }
    }

    @Override
    public void stop() {
        if (pet.isSleeping()) {
            pet.stopSleeping();
        }
        this.owner = null;
        this.bedPos = null;
        this.sleepTimer = 0;
    }
}
