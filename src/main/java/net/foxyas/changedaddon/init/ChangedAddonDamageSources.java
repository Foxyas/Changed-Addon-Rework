package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;

@ParametersAreNonnullByDefault
public class ChangedAddonDamageSources {

    public static final HashMap<DamageTypeHolder, DamageType> DAMAGE_TYPES_MAP = new HashMap<>();
    public static final DamageTypeHolder LATEX_SOLVENT = holder("latex_solvent");
    public static final DamageTypeHolder CONSCIENCE_LOSE = holder("conscience_lose");
    public static final DamageTypeHolder UNTRANSFUR_FAIL = holder("untransfur_fail");//TODO bypassArmor()
    public static final DamageTypeHolder CHOKE = holder("choke", new DamageType("choke", DamageScaling.NEVER, 0f));
    public static final DamageTypeHolder CONSTRICTION = holder("constriction", new DamageType("constriction", DamageScaling.NEVER, 0f));

    private static DamageTypeHolder holder(String name) {
        return holder(name, null);
    }

    private static DamageTypeHolder holder(String name, @Nullable DamageType damageType) {
        DamageTypeHolder damageTypeHolder = new DamageTypeHolder(ResourceKey.create(Registries.DAMAGE_TYPE, ChangedAddonMod.resourceLoc(name)));
        DAMAGE_TYPES_MAP.put(damageTypeHolder, damageType);
        return damageTypeHolder;
    }

    private static DamageTypeHolder holder(DamageType damageType) {
        DamageTypeHolder damageTypeHolder = new DamageTypeHolder(ResourceKey.create(Registries.DAMAGE_TYPE, ChangedAddonMod.resourceLoc(damageType.msgId())));
        DAMAGE_TYPES_MAP.put(damageTypeHolder, damageType);
        return damageTypeHolder;
    }

    public record DamageTypeHolder(ResourceKey<DamageType> key) {


        public @Nullable DamageType damageType() {
            return DAMAGE_TYPES_MAP.get(this);
        }

        public DamageSource source(Level level) {
            final Holder<DamageType> type = level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key);
            return new DamageSource(type);
        }

        public DamageSource source(Level level, Vec3 sourcePosition) {
            final Holder<DamageType> type = level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key);
            return new DamageSource(type, sourcePosition);
        }

        public DamageSource source(Entity sourceEntity) {
            final Holder<DamageType> type = sourceEntity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key);
            return new DamageSource(type, sourceEntity);
        }

        public DamageSource source(Entity sourceEntity, Vec3 sourcePosition) {
            final Holder<DamageType> type = sourceEntity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key);
            return new DamageSource(type, sourceEntity, sourceEntity, sourcePosition);
        }

        public DamageSource source(Entity projectile, Entity shooter) {
            final Holder<DamageType> type = projectile.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key);
            return new DamageSource(type, shooter, projectile);
        }

        public DamageSource source(Entity projectile, Entity shooter, Vec3 sourcePosition) {
            final Holder<DamageType> type = projectile.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key);
            return new DamageSource(type, shooter, projectile, sourcePosition);
        }
    }

}
