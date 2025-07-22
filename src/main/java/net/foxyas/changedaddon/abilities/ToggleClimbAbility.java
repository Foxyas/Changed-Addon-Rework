package net.foxyas.changedaddon.abilities;

import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.nbt.CompoundTag;

public class ToggleClimbAbility extends SimpleAbility {

    public boolean isActivated = true;

    public ToggleClimbAbility() {
        super();
    }

    @Override
    public void saveData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.saveData(tag, entity);
        tag.putBoolean("isActivated", this.isActivated());
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return true;
    }

    @Override
    public void readData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.readData(tag, entity);
        if (tag.contains("isActivated")) this.isActivated = tag.getBoolean("isActivated");
    }

    public boolean isActivated() {
        return isActivated;
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.INSTANT;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 5;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        this.isActivated =! this.isActivated;
        super.startUsing(entity);
    }
}
