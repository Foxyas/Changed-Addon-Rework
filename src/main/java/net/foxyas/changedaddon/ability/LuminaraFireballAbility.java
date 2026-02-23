package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.advanced.LuminaraFlowerBeastEntity;
import net.foxyas.changedaddon.network.PacketUtil;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.client.AbilityColors;
import net.ltxprogrammer.changed.client.gui.AbstractRadialScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class LuminaraFireballAbility extends SimpleAbility {

    public LuminaraFireballAbility() {
        super();
    }

    /**
     * Returns the ability color depending on which fireball would be spawned.
     */
    public static Optional<Integer> getColor(AbstractAbilityInstance abilityInstance, int layer) {
        AbstractRadialScreen.ColorScheme scheme = AbilityColors.getAbilityColors(abilityInstance);
        IAbstractChangedEntity entity = abilityInstance.entity;

        if (!(entity.getChangedEntity() instanceof LuminaraFlowerBeastEntity holder))
            return Optional.of(scheme.foreground().toInt());

        // Same decision tree as createFireball()
        if (holder.isAwakened()) {
            if (holder.isShiftKeyDown() && layer == 0) {
                return Optional.of(scheme.foreground().toInt());
            } else if (layer == 1 && !holder.isShiftKeyDown()) {
                return Optional.of(scheme.foreground().toInt());
            }
        } else {
            if (layer == 0) {
                return Optional.of(scheme.foreground().toInt());
            }
        }

        return Optional.empty();
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.fireball");
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        Collection<Component> description = new ArrayList<>(super.getAbilityDescription(entity));
        description.add(Component.translatable("ability.changed_addon.fireball.desc"));
        return description;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return entity.getChangedEntity() instanceof LuminaraFlowerBeastEntity;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 80;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        if (!(entity.getChangedEntity() instanceof LuminaraFlowerBeastEntity holder)) return;

        Level level = entity.getLevel();
        AbstractHurtingProjectile fireball;

        Vec3 view = holder.getLookAngle();

        if (holder.isAwakened()) {
            if (holder.isShiftKeyDown()) {
                fireball = new LargeFireball(level, holder, view.x, view.y, view.z, 1);
            } else fireball = new DragonFireball(level, holder, view.x, view.y, view.z);
        } else fireball = new SmallFireball(level, entity.getEntity(), view.x, view.y, view.z);
        Vec3 eyesPosition = holder.getEyePosition();

        if (level instanceof ServerLevel serverLevel) {
            PacketUtil.playSound(serverLevel, (serverPlayer -> true), eyesPosition, SoundEvents.GHAST_SHOOT, SoundSource.NEUTRAL, 1, 1);
        } else {
            level.playSound(null, eyesPosition.x, eyesPosition.y, eyesPosition.z, SoundEvents.GHAST_SHOOT, SoundSource.NEUTRAL, 1, 1);
        }

        fireball.moveTo(holder.getMouthPosition().add(view.scale(0.25f))); //Mouth position
        level.addFreshEntity(fireball);
    }
}
