package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class CarryAbility extends AbstractAbility<CarryAbilityInstance> {

    public CarryAbility() {
        super(CarryAbilityInstance::new);
    }

    public static boolean Spectator(Entity entity) {
        return entity instanceof Player player && player.isSpectator();
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.carry");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/carry_ability.png");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.INSTANT;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 5;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        if (Spectator(entity.getEntity()))
            return false;
        Optional<TransfurVariant<?>> variant = Optional.ofNullable(entity.getTransfurVariantInstance()).map(TransfurVariantInstance::getParent);
        return variant.filter(
                        v -> v.is(ChangedAddonTransfurVariants.Gendered.EXP2.getFemaleVariant()) || v.is(ChangedAddonTransfurVariants.Gendered.EXP2.getMaleVariant()) || v.is(ChangedAddonTransfurVariants.Gendered.ORGANIC_SNOW_LEOPARD.getFemaleVariant())
                                || v.is(ChangedAddonTransfurVariants.Gendered.ORGANIC_SNOW_LEOPARD.getMaleVariant()) || v.is(ChangedAddonTransfurVariants.Gendered.PURO_KIND.getFemaleVariant())
                                || v.is(ChangedAddonTransfurVariants.Gendered.PURO_KIND.getMaleVariant()) || v.is(ChangedAddonTags.TransfurVariants.ABLE_TO_CARRY))
                .isPresent();
    }

}
