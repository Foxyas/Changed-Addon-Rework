package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.ai.goals.luminarcticLeopard.CircleShootLuminarCrystalShardGoal;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.client.AbilityColors;
import net.ltxprogrammer.changed.client.gui.AbstractRadialScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Player version of {@link CircleShootLuminarCrystalShardGoal}.
 * <p>
 * HOLD the ability key: crystal shards spawn and orbit you while charging, then are fired one by one
 * (or all at once) toward whatever you are looking at. Releasing the key early discards the shards if the
 * charge isn't done yet, or dumps the remaining ones at once if it is.
 */
public class CrystalShardBarrageAbility extends AbstractAbility<CrystalShardBarrageAbilityInstance> {

    public enum OrbitType {
        HORIZONTAL_AROUND, // Full horizontal ring at eye level
        VERTICAL_BEHIND,   // Vertical half-circle arc shifted behind the player
        VERTICAL_CENTER    // Vertical half-circle arc centered on the player
    }

    public enum FireMode {
        SEQUENTIAL,  // One shard every FIRING_INTERVAL ticks
        ALL_AT_ONCE  // Every shard on the same tick
    }

    public CrystalShardBarrageAbility() {
        super(CrystalShardBarrageAbilityInstance::new);
    }

    public static Optional<Integer> getColor(AbstractAbilityInstance instance, int layer) {
        if (!(instance instanceof CrystalShardBarrageAbilityInstance abilityInstance)) return Optional.empty();
        AbstractRadialScreen.ColorScheme scheme = AbilityColors.getAbilityColors(abilityInstance);

        boolean isCrouching = abilityInstance.entity.isCrouching();
        if (layer == 0 && !isCrouching || layer == 1 && isCrouching) {
            return Optional.of(scheme.foreground().toInt());
        }

        return Optional.empty();
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.HOLD;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return CrystalShardBarrageAbilityInstance.COOLDOWN;
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        return List.of(Component.translatable(this.getAbilityName(entity).getString() + ".desc"));
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        LivingEntity living = entity.getEntity();
        return living.isAlive();
    }

    @Override
    public boolean shouldApplyCoolDown(IAbstractChangedEntity entity) {
        return false;
    }

}