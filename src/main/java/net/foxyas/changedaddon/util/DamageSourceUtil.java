package net.foxyas.changedaddon.util;

import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class DamageSourceUtil {

    public static DamageSource projectileDamageSourceOfType(Holder<DamageType> pType, @Nullable Entity pEntity) {
        return new DamageSource(pType, pEntity) {
            @Override
            public boolean is(@NotNull TagKey<DamageType> pDamageTypeKey) {
                if (pDamageTypeKey == DamageTypeTags.IS_PROJECTILE) {
                    return true;
                }
                return super.is(pDamageTypeKey);
            }
        };
    }
}
