package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WindControlAbility extends SimpleAbility {

    public WindControlAbility() {
        super();
    }

    @Override
    public void saveData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.saveData(tag, entity);
    }

    @Override
    public void readData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.readData(tag, entity);
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.wind_control");
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        Collection<Component> list = new ArrayList<>(super.getAbilityDescription(entity));
        list.add(Component.translatable("ability.changed_addon.wind_control.desc"));
        list.add(Component.translatable("ability.changed_addon.wind_control.desc2"));
        return list;
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.INSTANT;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return entity.isCrouching() ? 40 : 20;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return entity.getTransfurVariantInstance() != null;
    }

    @Override
    public boolean canKeepUsing(IAbstractChangedEntity entity) {
        return entity.getTransfurVariantInstance() != null;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        if (!entity.getLevel().isClientSide()) {
            runAbility(entity.getEntity());
            this.setDirty(entity);
        }
    }

    public void runAbility(LivingEntity living) {
        if (living.isShiftKeyDown()) {
            runAbilityShift(living);
        } else {
            runDash(living);
        }
    }

    public void runDash(LivingEntity living) {
        if (living.level().isClientSide()) {
            return; // Only run on server
        }

        double dashStrength = 2.0D; // Strength of the dash

        // Get the direction the entity is looking
        Vec3 look = living.getLookAngle().normalize();

        // Apply motion in the look direction (slightly upwards to prevent clipping)
        living.push(look.x * dashStrength, 0.1, look.z * dashStrength);
        living.hurtMarked = true; // Force motion update

        // Optional: play sound effect
        living.level().playSound(null, living.blockPosition(),
                ChangedSounds.CARDBOARD_BOX_OPEN.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

        // Optional: spawn particles
        if (living.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    living.getX(), living.getY(), living.getZ(),
                    10, 0.3, 0.2, 0.3, 0.05);
        }
    }


    public void runAbilityShift(LivingEntity living) {
        Level level = living.level();
        if (level.isClientSide()) {
            return;
        }

        double radius = 5.0D;
        double strength = 1.5D;

        List<LivingEntity> nearby = level.getEntitiesOfClass(
                LivingEntity.class,
                living.getBoundingBox().inflate(radius),
                e -> e != living
        );

        for (LivingEntity target : nearby) {
            double dx = target.getX() - living.getX();
            double dz = target.getZ() - living.getZ();
            double dist = Math.max(0.1, Math.sqrt(dx * dx + dz * dz));

            target.push((dx / dist) * strength, 0.5, (dz / dist) * strength);
            target.hurtMarked = true;
        }

        living.swing(InteractionHand.MAIN_HAND, true);
        level.playSound(null, living.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (living.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    living.getX(), living.getY(), living.getZ(),
                    10, 0.3, 0.2, 0.3, 0.05);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, living.getX(), living.getY(), living.getZ(), 10, 1.0, 0.5, 1.0, 0.1);
        }
    }

}
