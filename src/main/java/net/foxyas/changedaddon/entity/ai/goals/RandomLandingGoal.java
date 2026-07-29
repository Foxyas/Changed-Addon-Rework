package net.foxyas.changedaddon.entity.ai.goals;

import net.foxyas.changedaddon.entity.api.IFlyableChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RandomLandingGoal<T extends PathfinderMob & IFlyableChangedEntity> extends Goal {
    private final T entity;
    private Vec3 targetLandPos;

    public RandomLandingGoal(T entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Só ativa se estiver voando e NÃO tiver alvo de ataque
        if (!this.entity.isFlyingMode() || this.entity.getTarget() != null) {
            return false;
        }

        // Chance aleatória de decidir pousar
        if (this.entity.getRandom().nextInt(100) != 0) {
            return false;
        }

        BlockPos currentPos = this.entity.blockPosition();

        // Procura o chão manualmente descendo bloco por bloco a uma distância máxima (ex: 32 blocos)
        int maxScanDistance = 32;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int i = 1; i <= maxScanDistance; i++) {
            mutablePos.set(currentPos.getX(), currentPos.getY() - i, currentPos.getZ());

            // Verifica se a posição limite inferior do mundo foi atingida
            if (mutablePos.getY() < this.entity.level().getMinBuildHeight()) {
                break;
            }

            // Checa se o bloco é sólido/pode andar por cima
            BlockState state = this.entity.level().getBlockState(mutablePos);
            if (!state.isAir() && GoalUtils.isSolid(entity, mutablePos)) {
                // Achou o primeiro chão direto abaixo da entidade!
                this.targetLandPos = Vec3.atBottomCenterOf(mutablePos.above());
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.isFlyingMode() 
                && this.entity.getTarget() == null 
                && !this.entity.onGround() 
                && this.targetLandPos != null;
    }

    @Override
    public void start() {
        if (this.targetLandPos != null) {
            this.entity.getMoveControl().setWantedPosition(
                this.targetLandPos.x, 
                this.targetLandPos.y, 
                this.targetLandPos.z, 1.0D
            );
        }
    }

    @Override
    public void tick() {
        if (this.targetLandPos != null) {
            this.entity.getMoveControl().setWantedPosition(
                this.targetLandPos.x, 
                this.targetLandPos.y, 
                this.targetLandPos.z, 1.0D
            );


            // Ao encostar no chão, desliga o modo voo e encerra a Goal
            double distance = this.entity.getY() - targetLandPos.y();
            if (this.entity.onGround() || distance <= 0.25f) {
                this.entity.setFlyingMode(false);
                return;
            }
        }

        if (this.entity.onGround()) {
            this.entity.setFlyingMode(false);
        }
    }

    @Override
    public void stop() {
        this.targetLandPos = null;
    }
}