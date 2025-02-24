package net.foxyas.changedaddon.entity.CustomHandle;

import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.*;

public class SleepingWithOwnerGoal extends Goal {
    private static final double MAX_DISTANCE_SQ = 32.0; // 10 blocos ao quadrado
    private final LivingEntity pet;
    private Player owner;
    private BlockPos bedPos;
    private int sleepTimer = 0;
    private final boolean isDogOrCat;

    public static class BipedSleepGoal extends Goal {

        public static class BedSearchType {
            public static final BedSearchType NEAREST = new BedSearchType(0,10);
            public static final BedSearchType FURTHER = new BedSearchType(10,10);
            private final double MinScale, MaxScale;

            public BedSearchType(double[] scale){
                super();
                this.MinScale = scale[0];
                this.MaxScale = scale[1];
            }

            public BedSearchType(double MinScale,double MaxScale){
                super();
                this.MinScale = MinScale;
                this.MaxScale = MaxScale;
            }

            public double getMaxScale() {
                return MaxScale;
            }

            public double getMinScale() {
                return MinScale;
            }
        }
        private static final int BED_SEARCH_RADIUS = 5; // Raio de busca ao redor do dono
        private static final double MAX_DISTANCE_SQ = 32.0; // Distância máxima permitida para a cama

        private final LivingEntity pet;
        private Player owner;
        private BlockPos bedPos;
        private int sleepTimer = 0;
        private final boolean isDogOrCat;
        private final BedSearchType bedSearchType;

        public BipedSleepGoal(LivingEntity pet) {
            this.pet = pet;
            this.isDogOrCat = false;
            this.bedSearchType = BedSearchType.NEAREST;
        }

        public BipedSleepGoal(LivingEntity pet, boolean isDogOrCat) {
            this.pet = pet;
            this.isDogOrCat = isDogOrCat;
            this.bedSearchType = BedSearchType.NEAREST;
        }
        public BipedSleepGoal(LivingEntity pet, boolean isDogOrCat,BedSearchType bedSearchType) {
            this.pet = pet;
            this.isDogOrCat = isDogOrCat;
            this.bedSearchType = bedSearchType;
        }

        public BipedSleepGoal(LivingEntity pet, boolean isDogOrCat, Random PersonalityBased) {
            this.pet = pet;
            this.isDogOrCat = isDogOrCat;
            this.bedSearchType = selectRandom(PersonalityBased);
        }

        private static BedSearchType selectRandom(Random random){
            var value = random.nextInt(3);
            if (value == 0){
                return BedSearchType.NEAREST;
            } else if (value == 1){
                return BedSearchType.FURTHER;
            } else {
                return new BedSearchType(random.nextDouble(11),10);
            }
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

            // Verifica se o dono está dormindo e obtém a posição da cama
            if (player.isSleeping()) {
                this.owner = player;
                if (this.bedSearchType == BedSearchType.NEAREST){
                    this.bedPos = getNearestBedFromList(findBedsNearOwner(player));
                } else if (this.bedSearchType == BedSearchType.FURTHER) {
                    this.bedPos = getFurtherAwayBedFromList(findBedsNearOwner(player));
                } else {
                    this.bedPos = getBestBed(findBedsNearOwner(player),
                            bedSearchType.getMinScale(),
                            bedSearchType.getMaxScale())
                            .orElse(null);
                }

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
                pathfinderPet.getNavigation().moveTo(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5, 0.7);
                if (!isDogOrCat) {
                    pet.playSound(SoundEvents.CAT_PURREOW, 1.0F, 1.0F); // Toca o som de ronronar
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return owner != null && owner.isSleeping();
        }

        @Override
        public void tick() {
            if (pet.isSleeping()) {
                // Ajusta a posição da entidade na cama caso não esteja corretamente posicionada
                if (bedPos != null && bedPos.distToCenterSqr(pet.position()) >= 1.25f) {
                    pet.setPos(bedPos.getX() + 0.5f, bedPos.getY() + 0.9f, bedPos.getZ() + 0.5f);
                }

                // Para a movimentação da entidade
                if (pet instanceof PathfinderMob pathfinderPet) {
				pathfinderPet.getNavigation().stop();
                }
                return; // Se já está dormindo, não faz mais nada
            }

            if (bedPos != null) {
                BlockState bedState = pet.level.getBlockState(bedPos);

                // Verifica se a cama ainda existe e não está ocupada antes de continuar indo até ela
                if (!(bedState.getBlock() instanceof BedBlock) || bedState.getValue(BedBlock.OCCUPIED)) {
                    bedPos = null;
                    sleepTimer = 0;
                    return;
                }

                double distanceToBed = pet.distanceToSqr(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5);

                if (distanceToBed >= 1.5f && pet instanceof PathfinderMob pathfinderPet) {
                    pathfinderPet.getNavigation().moveTo(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5, 0.3);
                } else {
                    sleepTimer++;
                    if (sleepTimer >= 10) {
                        // Verifica novamente antes de dormir
                        if (!bedState.getValue(BedBlock.OCCUPIED)) {
                            pet.startSleeping(bedPos);

                            // Atualiza o estado da cama para ocupada
                            pet.level.setBlockAndUpdate(bedPos, bedState.setValue(BedBlock.OCCUPIED, true));

                            // Para a movimentação da entidade
                            if (pet instanceof PathfinderMob pathfinderPet) {
                                pathfinderPet.getNavigation().stop();
                            }
                        } else {
                            // Cama ocupada, resetar busca
                            bedPos = null;
                            sleepTimer = 0;
                        }
                    }
                }
            }

            // Se a entidade não tiver uma cama ou a cama anterior foi invalidada, busca uma nova
            if (bedPos == null) {
                bedPos = findNewBed();
            }
        }

        /**
         * Encontra uma nova cama disponível para a entidade dormir.
         */
        private BlockPos findNewBed() {
            List<BlockPos> beds = findBedsNearOwner(this.owner);

            if (beds.isEmpty()) {
                return null; // Nenhuma cama encontrada
            }

            return this.bedSearchType == BedSearchType.NEAREST ? getNearestBedFromList(beds)
                    : this.bedSearchType == BedSearchType.FURTHER ? getFurtherAwayBedFromList(beds)
                    : getBestBed(beds, bedSearchType.getMinScale(), bedSearchType.getMaxScale()).orElse(null);
        }



        @Override
        public void stop() {
            if (pet.isSleeping()) {
                pet.stopSleeping();

                // Verifica se bedPos não é nulo antes de acessar o bloco
                if (bedPos != null) {
                    BlockState bedState = pet.level.getBlockState(bedPos);
                    if (bedState.getBlock() instanceof BedBlock) {
                        pet.level.setBlockAndUpdate(bedPos, bedState.setValue(BedBlock.OCCUPIED, false));
                    }
                }
            }
            this.owner = null;
            this.bedPos = null;
            this.sleepTimer = 0;
        }

        /**
         * Procura a cama mais próxima do dono, ignorando a cama do próprio jogador.
         */
        private List<BlockPos> findBedsNearOwner(Player player) {
            if (player == null) return Collections.emptyList(); // Retorna uma lista vazia se o jogador for nulo

            Level world = pet.level;
            BlockPos ownerPos = player.blockPosition();
            BlockPos playerBed = player.getSleepingPos().orElse(null);
            List<BlockPos> nearbyBeds = new ArrayList<>();

            for (int x = -BED_SEARCH_RADIUS; x <= BED_SEARCH_RADIUS; x++) {
                for (int y = -BED_SEARCH_RADIUS; y <= BED_SEARCH_RADIUS; y++) { // Permite variação de altura
                    for (int z = -BED_SEARCH_RADIUS; z <= BED_SEARCH_RADIUS; z++) {
                        BlockPos checkPos = ownerPos.offset(x, y, z);
                        BlockState state = world.getBlockState(checkPos);

                        if (state.getBlock() instanceof BedBlock bedBlock
                                && (!state.getValue(BedBlock.OCCUPIED)
                                && state.getValue(BedBlock.PART) == BedPart.HEAD)) {

                            // Ignora a cama do jogador
                            if (playerBed != null && checkPos.equals(playerBed)) {
                                continue;
                            }

                            // Adiciona a cama encontrada à lista
                            nearbyBeds.add(checkPos);
                        }
                    }
                }
            }

            return nearbyBeds; // Retorna a lista de camas encontradas
        }

        private BlockPos getFurtherAwayBedFromList(List<BlockPos> bedList) {
            BlockPos nearestBed = null;
            double nearestDistanceSq = Double.MIN_VALUE;

            for (BlockPos bedPos : bedList) {
                double distanceSq = owner.distanceToSqr(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5);
                if (distanceSq > nearestDistanceSq) {
                    nearestDistanceSq = distanceSq;
                    nearestBed = bedPos;
                }
            }

            return nearestBed;
        }

        private BlockPos getNearestBedFromList(List<BlockPos> bedList) {
            BlockPos nearestBed = null;
            double nearestDistanceSq = Double.MAX_VALUE;

            for (BlockPos bedPos : bedList) {
                double distanceSq = owner.distanceToSqr(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5);
                if (distanceSq < nearestDistanceSq) {
                    nearestDistanceSq = distanceSq;
                    nearestBed = bedPos;
                }
            }

            return nearestBed;
        }

        private Optional<BlockPos> getBestBed(List<BlockPos> bedList, double minScale, double maxScale) {
            if (minScale >= maxScale){
                minScale = maxScale;
            }
            double maxDistance = BED_SEARCH_RADIUS * BED_SEARCH_RADIUS * maxScale; // Distância máxima ao quadrado
            double minDistance = BED_SEARCH_RADIUS * BED_SEARCH_RADIUS * minScale; // Distância mínima ao quadrado

            return bedList.stream()
                    .map(bedPos -> Map.entry(bedPos, pet.distanceToSqr(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5)))
                    .filter(entry -> entry.getValue() >= minDistance && entry.getValue() <= maxDistance) // Filtro por distância desejada
                    .min(Comparator.comparingDouble(Map.Entry::getValue)) // Seleciona a mais próxima dentro do intervalo
                    .map(Map.Entry::getKey);
        }


    }


    public SleepingWithOwnerGoal(LivingEntity pet, boolean isDogOrCat) {
        this.pet = pet;
        this.isDogOrCat = isDogOrCat;
    }

    public SleepingWithOwnerGoal(LivingEntity pet) {
        this.pet = pet;
        this.isDogOrCat = false;
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
            if (!isDogOrCat) {
                pet.playSound(SoundEvents.CAT_PURREOW, 1.0F, 1.0F); // Toca o som de ronronar
            }
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

            if (distanceToBed >= 1.5f && pet instanceof PathfinderMob pathfinderPet) {
                // Reduz velocidade conforme se aproxima
                pathfinderPet.getNavigation().moveTo(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5, 0.3);
            } else {
                // Se está no bloco correto, começa a dormir
                sleepTimer++;
                //if (sleepTimer == 5) {
                    //pet.playSound(SoundEvents.CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
                //}
                if (sleepTimer >= 10) {
                    if (!pet.isSleeping()) {
                        pet.startSleeping(bedPos);
                    }
                    // Para a movimentação da entidade
                    if (pet instanceof PathfinderMob pathfinderPet) {
                        pathfinderPet.getNavigation().stop();
                    }

                    //set pos
                    if (bedPos.distToCenterSqr(pet.position()) >= 1.25f){
                            pet.setPos(bedPos.getX() + 0.5f, bedPos.getY() + 0.9f, bedPos.getZ() + 0.5f);
                    }
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
