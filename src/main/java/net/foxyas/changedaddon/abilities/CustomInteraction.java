package net.foxyas.changedaddon.abilities;

import net.foxyas.changedaddon.entity.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.entity.LatexSnepEntity;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class CustomInteraction extends AbstractAbility<CustomInteractionInstance> {

    public CustomInteraction() {
        super(CustomInteractionInstance::new);
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("changed_addon.ability.custom_interaction");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/normal_paw.png");
    }

    @Nullable
    @Override
    public Component getSelectedDisplayText(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof LatexSnepEntity || entity.getChangedEntity() instanceof AbstractLuminarcticLeopard) {
            return Component.translatable("changed_addon.ability.custom_interaction.have_interaction");
        }
        return super.getSelectedDisplayText(entity);
    }
}
