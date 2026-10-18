package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@Deprecated(since = "Since changed 0.16.0, they added the tame function inside of the TamableLatexEntity")
public interface TamableLatexEntityWithTameFunction extends TamableLatexEntity {

    void tameEntityForPlayer(Player player);

    default boolean isOwnedBy(LivingEntity livingEntity) {
        return livingEntity == this.getOwner();
    }
}
