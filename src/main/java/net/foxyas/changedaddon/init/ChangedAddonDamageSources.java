package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ChangedAddonDamageSources {

    public static final DamageHolder LATEX_SOLVENT = holder("latex_solvent");
    public static final DamageHolder CONSCIENCE_LOSE = holder("conscience_lose");
    public static final DamageHolder UNTRANSFUR_FAIL = holder("untransfur_fail");//TODO bypassArmor()

    private static DamageHolder holder(String name) {
        return new DamageHolder(ResourceKey.create(Registries.DAMAGE_TYPE, ChangedAddonMod.resourceLoc(name)));
    }

    public record DamageHolder(ResourceKey<DamageType> key) {

        public DamageSource source(Level level) {
            final Holder<DamageType> type = level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key);
            return new DamageSource(type);
        }

        public DamageSource source(Entity sourceEntity) {
            final Holder<DamageType> type = sourceEntity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key);
            return new DamageSource(type, sourceEntity);
        }

        public DamageSource source(Entity projectile, Entity shooter) {
            final Holder<DamageType> type = projectile.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key);
            return new DamageSource(type, shooter, projectile);
        }
    }

}
