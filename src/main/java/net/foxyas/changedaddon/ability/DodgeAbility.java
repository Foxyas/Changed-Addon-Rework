package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

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

    @Override
    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/dodge_ability.png");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.HOLD;
    }


}
