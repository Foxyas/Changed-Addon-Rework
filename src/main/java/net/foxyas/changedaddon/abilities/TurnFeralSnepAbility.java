package net.foxyas.changedaddon.abilities;

import net.foxyas.changedaddon.entity.advanced.LatexSnepEntity;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class TurnFeralSnepAbility extends AbstractAbility<TurnFeralSnepAbilityInstance> {
    public TurnFeralSnepAbility() {
        super(TurnFeralSnepAbilityInstance::new);
    }

    @Override
    public TranslatableComponent getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.turn_feral");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/normal_paw.png");
    }

    @Nullable
    @Override
    public Component getSelectedDisplayText(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof LatexSnepEntity){
            return new TranslatableComponent("changed_addon.ability.turn_feral.can");
        }
        return super.getSelectedDisplayText(entity);
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        return 32;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 30;
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.CHARGE_TIME;
    }
}
