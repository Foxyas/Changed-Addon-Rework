package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.foxyas.changedaddon.entity.advanced.LatexSnepEntity;
import net.foxyas.changedaddon.entity.advanced.LuminaraFlowerBeastEntity;
import net.foxyas.changedaddon.entity.defaults.AbstractLuminarcticLeopard;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class CustomInteraction extends AbstractAbility<CustomInteractionInstance> {

    public CustomInteraction() {
        super(CustomInteractionInstance::new);
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.custom_interaction");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity ignoredEntity) {
        return ResourceLocation.parse("changed_addon:textures/screens/normal_paw.png");
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof AvaliEntity) {
            return 160;
        }

        return super.getCoolDown(entity);
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        ArrayList<Component> description = new ArrayList<>(super.getAbilityDescription(entity));
        if (entity.getChangedEntity() instanceof LatexSnepEntity) {
                description.add(Component.translatable("ability.changed_addon.custom_interaction.have_interaction.latex_snep"));
            } else if (entity.getChangedEntity() instanceof AvaliEntity avaliEntity) {
                description.add(Component.translatable("ability.changed_addon.custom_interaction.have_interaction.avali"));
                description.add(Component.translatable("ability.changed_addon.custom_interaction.have_interaction.avali.extra", avaliEntity.getDimensionScale()));
            } else if (entity.getChangedEntity() instanceof LuminaraFlowerBeastEntity luminaraFlowerBeast) {
                Component luminaraBeastDescription = Component.translatable("ability.changed_addon.custom_interaction.have_interaction.luminara_beast");
                String string = luminaraBeastDescription.getString();
                description.add(Component.literal(string).withStyle((style -> style.withObfuscated(!luminaraFlowerBeast.isHyperAwakened()))));
                if (luminaraFlowerBeast.isHyperAwakened()) {
                    Component luminaraBeastDescriptionExtra = Component.translatable("ability.changed_addon.custom_interaction.have_interaction.luminara_beast.extra", luminaraFlowerBeast.spawnParticles);
                    description.add(luminaraBeastDescriptionExtra);
                }
            } else if (entity.getChangedEntity() instanceof AbstractLuminarcticLeopard) {
                description.add(Component.translatable("ability.changed_addon.custom_interaction.have_interaction.luminarctic_leopards"));
            }
        return description;
    }

    @Nullable
    @Override
    public Component getSelectedDisplayText(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof LatexSnepEntity || entity.getChangedEntity() instanceof AbstractLuminarcticLeopard) {
            return Component.translatable("ability.changed_addon.custom_interaction.have_interaction");
        }
        if (entity.getChangedEntity() instanceof AvaliEntity) {
            return Component.translatable("ability.changed_addon.custom_interaction.have_interaction.avali");
        }
        return super.getSelectedDisplayText(entity);
    }
}
