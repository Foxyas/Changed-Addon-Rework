package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.ability.handle.dodgeTypes.DodgeType;
import net.foxyas.changedaddon.ability.handle.dodgeTypes.CounterDodgeType;
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

    public DodgeAbility(int dodgeAmount, DodgeType dodgeType) {
        super((ab, ia) -> new DodgeAbilityInstance(ab, ia, dodgeAmount).withDodgeType(dodgeType));
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.dodge");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/dodge_ability.png");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        DodgeAbilityInstance instance = entity.getAbilityInstance(this);
        if (instance != null && instance.getDodgeType() instanceof CounterDodgeType) {
            return UseType.INSTANT;
        }
        return UseType.HOLD;
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
