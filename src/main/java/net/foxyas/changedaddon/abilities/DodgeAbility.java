package net.foxyas.changedaddon.abilities;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class DodgeAbility extends AbstractAbility<DodgeAbilityInstance> {

    public DodgeAbility() {
        super(DodgeAbilityInstance::new);
    }

    public DodgeAbility(int Dodges) {
        super((ab, ia) -> new DodgeAbilityInstance(ab, ia, Dodges));
    }

    @Override
    public TranslatableComponent getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.dodge");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/dodge_ability.png");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.HOLD;
    }


}
