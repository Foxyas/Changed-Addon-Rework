package net.foxyas.changedaddon.abilities;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.nbt.CompoundTag;

public class SoftenAbilityInstance extends AbstractAbilityInstance {


    public SoftenAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    @Override
    public boolean canUse() {
        return entity.getTransfurVariant() != null && entity.getTransfurVariant().getEntityType().is(ChangedTags.EntityTypes.LATEX);
    }

    @Override
    public boolean canKeepUsing() {
        return true;
    }

    @Override
    public void startUsing() {
    }

    public boolean isActivate() {
        return this.getController().getHoldTicks() > 0;
    }

    @Override
    public void tick() {
    }

    @Override
    public void stopUsing() {
    }

    @Override
    public void onRemove() {
        super.onRemove();
    }

    @Override
    public AbstractAbility.UseType getUseType() {
        return super.getUseType();
    }


    @Override
    public void saveData(CompoundTag tag) {
        super.saveData(tag);
    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);
    }

}
