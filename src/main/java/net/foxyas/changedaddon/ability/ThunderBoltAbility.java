package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ThunderBoltAbility extends AbstractAbility<ThunderBoltAbilityInstance> {


    public ThunderBoltAbility() {
        super(ThunderBoltAbilityInstance::new);
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.thunder");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/thunderbolt.png");
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        ThunderBoltAbilityInstance abilityInstance = entity.getAbilityInstance(this);
        if (abilityInstance != null) {
            float percent = 1 + abilityInstance.getCharge();
            return (int) (20 * percent);
        }
        return super.getCoolDown(entity);
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        TransfurVariant<?> variant = entity.getChangedEntity().getSelfVariant();
        if (variant == ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get()) {
            return 40;
        }
        return 80;
    }

    @Override
    public boolean shouldApplyCoolDown(IAbstractChangedEntity entity) {
        return false;
    }

    @Override
    public void tickCharge(IAbstractChangedEntity entity, float ticks) {
        super.tickCharge(entity, ticks);
        ThunderBoltAbilityInstance abilityInstance = entity.getAbilityInstance(this);
        if (abilityInstance != null) {
            abilityInstance.charge = ticks;
            LivingEntity livingEntity = entity.getEntity();
            int chargeTime = getChargeTime(entity);
            if (chargeTime != 0) {
                Level level = livingEntity.level();
                level.playSound(null, livingEntity, SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 1, ticks / chargeTime);
                if (ticks == chargeTime) {
                    level.playSound(null, livingEntity, SoundEvents.WARDEN_SONIC_CHARGE, SoundSource.PLAYERS, 1, ticks / chargeTime);
                }
            }
            entity.displayClientMessage(Component.literal("TICKS:" + ticks + " AND CHARGE:" + abilityInstance.charge), true);
        }
    }
}
