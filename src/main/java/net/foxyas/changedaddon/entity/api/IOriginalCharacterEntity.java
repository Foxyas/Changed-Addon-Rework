package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IOriginalCharacterEntity {

    default List<Component> getOcVariantComponents() {
        if (this instanceof ChangedEntity changedEntity) {
            TransfurVariant<?> selfVariant = changedEntity.getSelfVariant();
            if (selfVariant == null) return List.of();
            String usableInfo = isFreeForUse() ? "Free for use, " : "Not free for use, ";
            Component ownerName = hasOwner() ? getOwnerName() : Component.literal("Unknow owner");
            if (ownerName != null) {
                return List.of(Component.literal(usableInfo).append(ownerName));
            }
        }
        return List.of();
    }

    @Nullable
    Component getOwnerName();

    default boolean hasOwner() {
        return getOwnerName() != null;
    }

    default boolean isFreeForUse() {
        return false;
    }
}
