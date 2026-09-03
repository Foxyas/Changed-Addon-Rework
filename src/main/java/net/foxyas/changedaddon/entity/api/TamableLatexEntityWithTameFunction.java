package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@Deprecated
public interface TamableLatexEntityWithTameFunction extends TamableLatexEntity {

    void tameEntityForPlayer(Player player);

    default boolean isOwnedBy(LivingEntity livingEntity) {
        return livingEntity == this.getOwner();
    }
}
