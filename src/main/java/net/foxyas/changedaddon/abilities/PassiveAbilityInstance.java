package net.foxyas.changedaddon.abilities;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbilityInstance;

public class PassiveAbilityInstance extends AbstractAbilityInstance {
    public PassiveAbilityInstance(AbstractAbility<PassiveAbilityInstance> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canKeepUsing() {
        return true;
    }

    @Override
    public void startUsing() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void stopUsing() {

    }

    @Override
    public void tickIdle() {
        super.tickIdle();
        if (this.getAbility() instanceof PassiveAbility passiveAbility) {
            if (passiveAbility.isActivated()) {
                passiveAbility.passiveAction.accept(this.entity);
            }
        }
    }
}
