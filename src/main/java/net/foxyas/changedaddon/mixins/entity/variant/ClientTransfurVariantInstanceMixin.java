package net.foxyas.changedaddon.mixins.entity.variant;

import net.ltxprogrammer.changed.client.ClientTransfurVariantInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(value = ClientTransfurVariantInstance.class,remap = false)
public abstract class ClientTransfurVariantInstanceMixin<T extends ChangedEntity> extends TransfurVariantInstance<T> {

    public ClientTransfurVariantInstanceMixin(TransfurVariant<T> parent, Player host) {
        super(parent, host);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
