package net.foxyas.changedaddon.abilities;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbilityInstance;
import net.minecraft.nbt.CompoundTag;

public class PassiveAbilityInstance extends AbstractAbilityInstance {
    public boolean isActivated = true;

    public PassiveAbilityInstance(AbstractAbility<PassiveAbilityInstance> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    @Override
    public boolean canUse() {
        return false;
    }

    @Override
    public boolean canKeepUsing() {
        return false;
    }

    @Override
    public void startUsing() {
        isActivated = !isActivated;
    }

    @Override
    public void tick() {

    }

    @Override
    public void stopUsing() {

    }

    public void saveData(CompoundTag tag) {
        super.saveData(tag);
        tag.putBoolean("isActivated", this.isActivated());
    }


    public boolean isActivated() {
        return isActivated;
    }

    public void readData(CompoundTag tag) {
        super.readData(tag);
        if (tag.contains("isActivated")) this.isActivated = tag.getBoolean("isActivated");
    }

    @Override
    public void tickIdle() {
        super.tickIdle();
        if (this.getAbility() instanceof PassiveAbility passiveAbility) {
            if (isActivated()) {
                if (!entity.getEntity().getLevel().isClientSide()) {
                    passiveAbility.passiveAction.accept(this.entity);
                }
            }
        }
    }
}
