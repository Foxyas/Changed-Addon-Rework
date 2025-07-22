package net.foxyas.changedaddon.abilities;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class ToggleClimbAbilityInstance extends AbstractAbilityInstance {

    public boolean isActivated = true;

    public ToggleClimbAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    @Override
    public boolean canUse() {
        return entity.getTransfurVariant() != null && entity.getTransfurVariant().canClimb;
    }

    @Override
    public boolean canKeepUsing() {
        return true;
    }

    @Override
    public void startUsing() {
        this.isActivated = !this.isActivated;
        if (!entity.getLevel().isClientSide()) {
            entity.displayClientMessage(new TextComponent("Climb = " + this.isActivated), true);
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
