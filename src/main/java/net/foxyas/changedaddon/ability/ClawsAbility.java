package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.api.IDynamicPawColor;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.client.AbilityColors;
import net.ltxprogrammer.changed.client.gui.AbstractRadialScreen;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.EntityShape;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ClawsAbility extends SimpleAbility {
    public boolean isActive = false;

    public ClawsAbility() {
        super();
    }

    public static Optional<Integer> getColor(AbstractAbilityInstance abilityInstance, int layer) {
        AbstractRadialScreen.ColorScheme scheme = AbilityColors.getAbilityColors(abilityInstance);
        if (abilityInstance.ability instanceof ClawsAbility) {
            ChangedEntity changedEntity = abilityInstance.entity.getChangedEntity();
            if (changedEntity instanceof IDynamicPawColor iDynamicPawColor && iDynamicPawColor.getPawStyle() != IDynamicPawColor.PawStyle.DEFAULT) {
                IDynamicPawColor.PawStyle pawStyle = iDynamicPawColor.getPawStyle();
                switch (pawStyle) {
                    case ANTHRO -> {
                        if (layer == 0) {
                            return Optional.of(scheme.foreground().toInt());
                        }
                    }
                    case FERAL -> {
                        if (layer == 1) {
                            return Optional.of(iDynamicPawColor.getPawColor().getRGB());
                        } else if (layer == 2) {
                            return Optional.of(iDynamicPawColor.getPawBeansColor().getRGB());
                        }
                    }
                }
            } else {
                if (changedEntity.getEntityShape() == EntityShape.ANTHRO) {
                    if (layer == 0) {
                        return Optional.of(scheme.foreground().toInt());
                    }
                } else if (changedEntity.getEntityShape() == EntityShape.FERAL) {
                    if (layer == 1) {
                        if (changedEntity instanceof IDynamicPawColor dynamicPawColor) {
                            return Optional.of(dynamicPawColor.getPawColor().getRGB());
                        }
                        return Optional.of(scheme.foreground().toInt());
                    } else if (layer == 2) {
                        if (changedEntity instanceof IDynamicPawColor dynamicPawColor) {
                            return Optional.of(dynamicPawColor.getPawBeansColor().getRGB());
                        }
                        return Optional.of(scheme.foreground().toInt());
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.saveData(tag, entity);
        tag.putBoolean("isActive", isActive);
    }

    @Override
    public void readData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.readData(tag, entity);
        if (tag.contains("isActive")) {
            this.isActive = tag.getBoolean("isActive");
        }
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/paw_with_claws.png");
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.claws");
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        Collection<Component> description = new ArrayList<>(super.getAbilityDescription(entity));
        description.add(Component.translatable("ability.changed_addon.claws.desc"));
        return description;
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.INSTANT;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 2;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        if (entity.getTransfurVariantInstance() == null) {
            return false;
        }
        return entity.getTransfurVariantInstance().getParent().getEntityType().is(ChangedAddonTags.EntityTypes.HAS_CLAWS) || entity.getTransfurVariantInstance().getParent().is(ChangedAddonTags.TransfurTypes.HAS_CLAWS) || entity.getTransfurVariantInstance().getParent().is(ChangedAddonTags.TransfurTypes.CAT_LIKE) || entity.getTransfurVariantInstance().getParent().is(ChangedAddonTags.TransfurTypes.LEOPARD_LIKE);
    }

    @Override
    public boolean canKeepUsing(IAbstractChangedEntity entity) {
        if (entity.getTransfurVariantInstance() == null) {
            return false;
        }
        return entity.getTransfurVariantInstance().getParent().getEntityType().is(ChangedAddonTags.EntityTypes.HAS_CLAWS) || entity.getTransfurVariantInstance().getParent().is(ChangedAddonTags.TransfurTypes.HAS_CLAWS) || entity.getTransfurVariantInstance().getParent().is(ChangedAddonTags.TransfurTypes.CAT_LIKE) || entity.getTransfurVariantInstance().getParent().is(ChangedAddonTags.TransfurTypes.LEOPARD_LIKE);
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        if (!entity.getLevel().isClientSide()) {
            TurnOnClaws();
            this.setDirty(entity);
        }
    }

    public void TurnOnClaws() {
        this.isActive = !this.isActive;
    }
}
