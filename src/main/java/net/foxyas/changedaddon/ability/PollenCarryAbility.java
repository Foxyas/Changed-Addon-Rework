package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.List;

public class PollenCarryAbility extends AbstractAbility<PollenCarryAbilityInstance> {

    public PollenCarryAbility() {
        super(PollenCarryAbilityInstance::new);
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.pollen_carry");
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        return List.of(Component.translatable("ability.changed_addon.pollen_carry.description"));
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        if (entity.getEntity().isSpectator() || entity.getSelfVariant() == null) return false;

        return entity.getSelfVariant().is(ChangedTransfurVariants.LATEX_BEE);
    }

    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.CHARGE_RELEASE;
    }
}
