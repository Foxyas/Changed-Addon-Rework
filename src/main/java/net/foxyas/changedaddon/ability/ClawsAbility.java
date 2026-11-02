package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.api.IDynamicPawColor;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.variant.IVariantExtraStats;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.client.AbilityColors;
import net.ltxprogrammer.changed.client.gui.AbstractRadialScreen;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.EntityShape;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ClawsAbility extends AbstractAbility<ClawsAbility.Instance> {

    public ClawsAbility() {
        super(Instance::new);
    }

    public static class Instance extends AbstractAbilityInstance {
        public boolean isActive = false;

        public Instance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
            super(ability, entity);
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean canKeepUsing() {
            return canUse();
        }

        @Override
        public void startUsing() {
            if (!entity.getLevel().isClientSide()) {
                toggleClaws();
                this.ability.setDirty(entity);
            }
        }

        public void toggleClaws() {
            this.isActive = !this.isActive;
            Component message = Component.translatable("ability.changed_addon.claws.desc.info.toggle" + (isActive ? ".on" : ".off"));
            this.entity.displayClientMessage(message, true);
        }

        @Override
        public void tick() {

        }

        @Override
        public void stopUsing() {

        }

        @Override
        public void saveData(CompoundTag tag) {
            super.saveData(tag);
            tag.putBoolean("isActive", isActive);
        }

        @Override
        public void readData(CompoundTag tag) {
            super.readData(tag);
            if (tag.contains("isActive")) {
                this.isActive = tag.getBoolean("isActive");
            }
        }
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
        Instance data = entity.getAbilityInstance(this);
        if (data != null) {
            description.add(Component.translatable("ability.changed_addon.claws.desc.info." + (data.isActive ? "enabled" : "disabled")));
        }
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

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
    public static class EventHandler {

        @SubscribeEvent
        public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();
            TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariantInstance == null || !player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) { // Only works if the player use the hands instead of a tool
                return;
            }

            BlockState state = event.getState();
            if (!state.is(BlockTags.DIRT) && !state.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                return;
            }

            Instance clawsAbilityInstance = transfurVariantInstance.getAbilityInstance(ChangedAddonAbilities.CLAWS.get());
            if (clawsAbilityInstance != null && clawsAbilityInstance.isActive) {
                event.setNewSpeed(event.getNewSpeed() * 1.25f);
            }
        }
    }
}
