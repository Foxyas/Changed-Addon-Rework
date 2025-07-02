package net.foxyas.changedaddon.abilities;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.List;

public class SoftenAbility extends AbstractAbility<SoftenAbilityInstance> {
    public SoftenAbility() {
        super(SoftenAbilityInstance::new);
    }

    @Override
    public TranslatableComponent getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.soften");
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        Collection<Component> Description = List.of(new TranslatableComponent("changed_addon.ability.soften.description"));
        return Description;
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/gooey_paw.png");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.HOLD;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return entity.getTransfurVariant() != null && entity.getTransfurVariant().getEntityType().is(ChangedTags.EntityTypes.LATEX);
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
    }

    @Override
    public void onRemove(IAbstractChangedEntity entity) {
        super.onRemove(entity);
    }
}
