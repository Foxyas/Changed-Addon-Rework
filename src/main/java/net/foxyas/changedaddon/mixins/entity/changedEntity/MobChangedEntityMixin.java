package net.foxyas.changedaddon.mixins.entity.changedEntity;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.entity.api.ChangedEntityExtension;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Mob.class)
public abstract class MobChangedEntityMixin extends LivingEntity {

    protected MobChangedEntityMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void spawnHook(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, SpawnGroupData pSpawnData, CompoundTag pDataTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (!(ChangedAddon$selfMixin() instanceof ChangedEntity changedEntity)) return;
        if (changedEntity instanceof IAlphaAbleEntity iAlphaAbleEntity) {
            boolean gamerule = pLevel.getLevel().getGameRules().getBoolean(ChangedAddonGameRules.DO_ALPHAS_SPAWN);
            if (!gamerule) return;

            float chance = iAlphaAbleEntity.chanceToSpawnAsAlpha();
            if (random.nextFloat() <= chance) {
                iAlphaAbleEntity.setAlpha(true);
                float rand = random.nextFloat();
                iAlphaAbleEntity.setAlphaScale(rand * rand * 2 + .5f);
            }
        }

        if (changedEntity instanceof ChangedEntityExtension changedEntityExtension) {
            boolean flag = pLevel.getLevel().getGameRules().getBoolean(ChangedAddonGameRules.CHANGED_ENTITIES_SPAWN_DRESSED);
            boolean match = ChangedAddonServerConfiguration.CHANGED_SPAWN_DRESS_MODE.get().isMatch(this);
            if (flag && match) changedEntityExtension.setDefaultClothing(changedEntity);
        }
    }

    @Unique
    private Mob ChangedAddon$selfMixin() {
        return (Mob) (Object) this;
    }
}
