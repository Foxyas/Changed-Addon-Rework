package net.foxyas.changedaddon.abilities;

import net.ltxprogrammer.changed.ability.*;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class DissolveAbility extends AbstractAbility<DissolveAbilityInstance> {

    public DissolveAbility() {
        super(DissolveAbilityInstance::new);
    }


    @Override
    public TranslatableComponent getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.dissolve");
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

    @Override
    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/dodge_ability.png");
    }

}
