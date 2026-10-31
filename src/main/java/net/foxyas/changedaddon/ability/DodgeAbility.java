package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.ability.handle.dodgeTypes.CounterDodgeType;
import net.foxyas.changedaddon.ability.handle.dodgeTypes.DodgeType;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DodgeAbility extends AbstractAbility<DodgeAbilityInstance> {

    public DodgeAbility() {
        super(DodgeAbilityInstance::new);
    }

    public DodgeAbility(int dodgeAmount) {
        super((ab, ia) -> new DodgeAbilityInstance(ab, ia, dodgeAmount));
    }

    public DodgeAbility(DodgeType dodgeType) {
        super((ab, ia) -> new DodgeAbilityInstance(ab, ia).withDodgeType(dodgeType));
    }

    public DodgeAbility(float maxDodgeStamina, DodgeType dodgeType) {
        super((ab, ia) -> new DodgeAbilityInstance(ab, ia, maxDodgeStamina).withDodgeType(dodgeType));
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.dodge");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/dodge_ability.png");
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        DodgeAbilityInstance instance = entity.getAbilityInstance(this);
        if (instance != null && instance.getDodgeType() instanceof CounterDodgeType) {
            return 90;
        }
        return super.getCoolDown(entity);
    }

    @Override
    public boolean shouldApplyCoolDown(IAbstractChangedEntity entity) {
        return isCounterDodgeType(entity);
    }

    public boolean isCounterDodgeType(IAbstractChangedEntity entity) {
        DodgeAbilityInstance instance = entity.getAbilityInstance(this);
        return instance != null && instance.getDodgeType() instanceof CounterDodgeType;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
        this.setDirty(entity);
    }

    @Override
    public void tick(IAbstractChangedEntity entity) {
        super.tick(entity);
        this.setDirty(entity);
    }

    @Override
    public void stopUsing(IAbstractChangedEntity entity) {
        super.stopUsing(entity);
        this.setDirty(entity);
    }
}
