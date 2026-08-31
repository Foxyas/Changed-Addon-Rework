package net.foxyas.changedaddon.mixins.entity.changedEntity;

import com.mojang.serialization.Dynamic;
import net.foxyas.changedaddon.entity.ai.advanced.brainProviders.FelinesBrainChangedEntityAi;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ChangedEntity.class, remap = false)
public abstract class FelinesBrainAIChangedEntityMixin extends Monster {

    //TODO: make this class some kind of auto brain injector...
    protected FelinesBrainAIChangedEntityMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected Brain.@NotNull Provider<ChangedEntity> brainProvider() {
        if (this.getType().is(ChangedAddonTags.EntityTypes.HAS_BETTER_GROUND_PATHFIND)) {
            return Brain.provider(FelinesBrainChangedEntityAi.MEMORY_TYPES, FelinesBrainChangedEntityAi.SENSOR_TYPES);
        } else {
            return (Brain.Provider<ChangedEntity>) super.brainProvider();
        }
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> pDynamic) {
        Brain<?> brain = super.makeBrain(pDynamic);
        if (this.getType().is(ChangedAddonTags.EntityTypes.HAS_BETTER_GROUND_PATHFIND)) {
            return FelinesBrainChangedEntityAi.makeBrain(this.brainProvider().makeBrain(pDynamic));
        } else {
            return brain;
        }
    }

    @Override
    public @NotNull Brain<ChangedEntity> getBrain() {
        return (Brain<ChangedEntity>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        ChangedEntity self = (ChangedEntity) (Object) this;
        if (self.level() instanceof ServerLevel serverLevel) {
            Level level = self.level();
            level.getProfiler().push("changedEntityBrain");
            getBrain().tick(serverLevel, self);
            level.getProfiler().pop();
            level.getProfiler().push("changedEntityActivityUpdate");
            FelinesBrainChangedEntityAi.updateActivity(self);
            level.getProfiler().pop();
        }
        super.customServerAiStep();
    }
}
