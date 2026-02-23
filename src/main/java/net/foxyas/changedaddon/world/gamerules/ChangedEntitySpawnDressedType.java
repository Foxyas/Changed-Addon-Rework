package net.foxyas.changedaddon.world.gamerules;

import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public enum ChangedEntitySpawnDressedType {
    NON_LATEX(entitytype -> !entitytype.is(ChangedTags.EntityTypes.LATEX)),
    LATEX(entitytype -> entitytype.is(ChangedTags.EntityTypes.LATEX)),
    ANY(entityType -> true),
    NONE(entityType -> false);

    private final Predicate<EntityType<?>> predicate;

    ChangedEntitySpawnDressedType(Predicate<EntityType<?>> predicate) {
        this.predicate = predicate;
    }

    public static ChangedEntitySpawnDressedType fromString(String value) {
        try {
            return ChangedEntitySpawnDressedType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }

    public Predicate<EntityType<?>> getPredicate() {
        return predicate;
    }

    public boolean isMatch(LivingEntity livingEntity) {
        return this.predicate.test(livingEntity.getType());
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
