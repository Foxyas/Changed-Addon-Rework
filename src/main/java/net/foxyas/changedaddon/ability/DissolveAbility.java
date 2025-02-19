package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.*;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class DissolveAbility extends AbstractAbility<DissolveAbilityInstance> {

    public DissolveAbility() {
        super(DissolveAbilityInstance::new);
    }


    @Override
    public TranslatableComponent getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.dissolve");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        if (Objects.requireNonNull(entity.getAbilityInstance(this)).isSet()) {
            return UseType.INSTANT;
        }
        return UseType.CHARGE_TIME;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        return Objects.requireNonNull(entity.getAbilityInstance(this)).isSet() ? 0 : 20;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return Objects.requireNonNull(entity.getAbilityInstance(this)).isSet() ? 600 : 20;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return true;
    }

    @Override
    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/dodge_ability.png");
    }

}
