package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.entity.defaults.AbstractBasicOrganicChangedEntity;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class AbstractProtogenEntity extends AbstractBasicOrganicChangedEntity {

    public AbstractProtogenEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.ABSORPTION;
    }

    public abstract boolean isOrganic();

    @Override
    public void variantTick(Level level) {
        super.variantTick(level);
        if (isOrganic()) {
            Player player = this.getUnderlyingPlayer();
            if (player != null) {
                TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
                GrabEntityAbilityInstance grabEntityAbilityInstance = transfurVariantInstance.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
                if (grabEntityAbilityInstance instanceof GrabEntityAbilityExtensor abilityExtensor) {
                    abilityExtensor.setSafeMode(true);
                }
            }
        }
    }
}
