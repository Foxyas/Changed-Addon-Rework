package net.foxyas.changedaddon.world.gamerules;

import net.foxyas.changedaddon.util.ChangedEntityUtil;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public enum ChangedEntitySpawnDressedType {
    NON_LATEX(entity -> !ChangedEntityUtil.isGoo(entity)),
    LATEX(ChangedEntityUtil::isGoo),
    ANY(entity -> true),
    NONE(entity -> false);

    private final Predicate<Entity> predicate;

    ChangedEntitySpawnDressedType(Predicate<Entity> predicate) {
        this.predicate = predicate;
    }

    public static ChangedEntitySpawnDressedType fromString(String value) {
        try {
            return ChangedEntitySpawnDressedType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }

    public Predicate<Entity> getPredicate() {
        return predicate;
    }

    public boolean isMatch(LivingEntity livingEntity) {
        return this.predicate.test(livingEntity);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
