package net.foxyas.changedaddon.abilities;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class DissolveAbility extends AbstractAbility<DissolveAbilityInstance> {

    public DissolveAbility() {
        super(DissolveAbilityInstance::new);
    }


    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("changed_addon.ability.dissolve");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        if (Objects.requireNonNull(entity.getAbilityInstance(this)).isSet()) {
            return UseType.INSTANT;
        }
        return UseType.CHARGE_TIME;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        return Objects.requireNonNull(entity.getAbilityInstance(this)).isSet() ? 0 : 20;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return Objects.requireNonNull(entity.getAbilityInstance(this)).isSet() ? 200 : 20;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return true;
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/dodge_ability.png");
    }

}
