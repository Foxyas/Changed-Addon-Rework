package net.foxyas.changedaddon.abilities;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DodgeAbility extends AbstractAbility<DodgeAbilityInstance> {

    public DodgeAbility() {
        super(DodgeAbilityInstance::new);
    }

    public DodgeAbility(int Dodges) {
        super((ab, ia) -> new DodgeAbilityInstance(ab, ia, Dodges));
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("changed_addon.ability.dodge");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/dodge_ability.png");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.HOLD;
    }


}
