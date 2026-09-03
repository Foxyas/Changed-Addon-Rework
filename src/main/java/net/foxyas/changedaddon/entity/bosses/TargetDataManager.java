package net.foxyas.changedaddon.entity.bosses;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.UUID;
import java.util.function.Function;

public class TargetDataManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Codec<UUID> UUID_CODEC = UUIDUtil.CODEC;

    private final Mob selfEntity;
    private final Function<LivingEntity, Boolean> canTargetBeSaved;
    private UUID savedTargetUUID;

    public TargetDataManager(Mob selfEntity, Function<LivingEntity, Boolean> canEntityBeSaved) {
        this.selfEntity = selfEntity;
        this.canTargetBeSaved = canEntityBeSaved;
    }

    // Defines if the current target is eligible to be saved to NBT
    public static boolean basicCanTargetBeSaved(Entity entity) {
        if (!(entity instanceof LivingEntity currentTarget)) return false;

        // Only save if the target is alive and valid
        return currentTarget.isAlive() && !currentTarget.isRemoved();
    }

    public boolean canTargetBeSaved(LivingEntity target) {
        return this.canTargetBeSaved.apply(target);
    }

    // Utility method to be called in your entity's addAdditionalSaveData
    public void saveTarget(CompoundTag nbt) {
        LivingEntity currentTarget = this.selfEntity.getTarget();

        if (currentTarget != null && canTargetBeSaved(currentTarget)) {
            this.savedTargetUUID = currentTarget.getUUID();

            UUID_CODEC.encodeStart(NbtOps.INSTANCE, this.savedTargetUUID)
                    .resultOrPartial(err -> LOGGER.error("Failed to serialize target UUID: {}", err))
                    .ifPresent(tag -> nbt.put("SavedTargetUUID", tag));
        }
    }

    // Utility method to be called in your entity's readAdditionalSaveData
    public void loadTarget(CompoundTag nbt) {
        if (!nbt.contains("SavedTargetUUID")) {
            return;
        }

        UUID_CODEC.parse(NbtOps.INSTANCE, nbt.get("SavedTargetUUID"))
                .resultOrPartial(err -> LOGGER.error("Failed to read target UUID: {}", err))
                .ifPresent(uuid -> {
                    this.savedTargetUUID = uuid;
                });
    }

    public void loadAndResolveTarget(CompoundTag nbt) {
        if (!nbt.contains("SavedTargetUUID")) {
            return;
        }

        UUID_CODEC.parse(NbtOps.INSTANCE, nbt.get("SavedTargetUUID"))
                .resultOrPartial(err -> LOGGER.error("Failed to read target UUID: {}", err))
                .ifPresent(uuid -> {
                    this.savedTargetUUID = uuid;
                    this.resolveTarget();
                });
    }

    // Resolves the real entity in the world or clears it if invalid
    public void resolveTarget() {
        if (this.savedTargetUUID == null) return;

        if (this.selfEntity.level() instanceof ServerLevel serverLevel) {
            var entity = serverLevel.getEntity(this.savedTargetUUID);

            if (entity instanceof LivingEntity livingTarget && (selfEntity.canAttack(livingTarget) || selfEntity.canAttack(livingTarget, TargetingConditions.forCombat()))) {
                this.selfEntity.setTarget(livingTarget);
            } else {
                // Failure: The target became invalid for some reason (does not exist in the world or died)
                LOGGER.info("The saved target ({}) for entity {} became invalid or was not found. Target forgotten.",
                        this.savedTargetUUID, this.selfEntity.getName().getString());

                this.savedTargetUUID = null;
                this.selfEntity.setTarget(null); // Clears the current target
            }
        }
    }

    @Nullable
    public Entity getSavedTarget() {
        if (this.savedTargetUUID == null || !(this.selfEntity.level() instanceof ServerLevel serverLevel)) {
            return null;
        }
        return serverLevel.getEntity(this.savedTargetUUID);
    }
}