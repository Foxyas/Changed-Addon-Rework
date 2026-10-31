package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.advanced.LatexSnepFeralEntity;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class LeapAbility extends AbstractAbility<LeapAbilityInstance> {

    public LeapAbility() {
        super(LeapAbilityInstance::new);
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.leap");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/leap_ability.png");
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return true;
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof LatexSnepFeralEntity) {
            return UseType.INSTANT;
        }
        return UseType.CHARGE_RELEASE;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        return 2;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 15;
    }

}
