package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.api.ExtraConditions;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;


public class ToggleClimbAbilityInstance extends AbstractAbilityInstance {

    public boolean isActivated = true;

    public ToggleClimbAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    @Override
    public boolean canUse() {
        TransfurVariant<?> transfurVariant = entity.getSelfVariant();
        if (transfurVariant != null && transfurVariant.canClimb && entity.getEntity() instanceof ExtraConditions.Climb extraConditions) {
            return extraConditions.canClimb();
        }
        return transfurVariant != null && transfurVariant.canClimb;
    }

    @Override
    public boolean canKeepUsing() {
        return canUse();
    }

    @Override
    public void startUsing() {
        this.isActivated = !this.isActivated;
        if (!entity.getLevel().isClientSide()) {
            entity.displayClientMessage(Component.literal("Climb = " + this.isActivated), true);
        }
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

    public boolean isActivated() {
        return isActivated;
    }

    @Override
    public void saveData(CompoundTag tag) {
        super.saveData(tag);
        tag.putBoolean("isActivated", this.isActivated());
    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);
        if (tag.contains("isActivated")) this.isActivated = tag.getBoolean("isActivated");
    }
}
