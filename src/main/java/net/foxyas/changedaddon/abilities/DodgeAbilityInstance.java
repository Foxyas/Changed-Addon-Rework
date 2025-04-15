package net.foxyas.changedaddon.abilities;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;

public class DodgeAbilityInstance extends AbstractAbilityInstance {
    private int dodgeAmount = 4;
    private int maxDodgeAmount = 4;
    private boolean dodgeActive = false;

    private final int defaultRegenCooldown = 20;
    private int dodgeRegenCooldown = defaultRegenCooldown;

    public DodgeAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    public DodgeAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity, int maxDodge) {
        this(ability, entity);
        this.maxDodgeAmount = maxDodge;
        this.dodgeAmount = maxDodge;
    }

    public boolean isDodgeActive() {
        return this.getController().getHoldTicks() > 0;
    }

    public void setDodgeActivate(boolean active) {
        this.dodgeActive = active;
    }

    public int getDodgeAmount() {
        return dodgeAmount;
    }

    public void setDodgeAmount(int amount) {
        dodgeAmount = Math.min(amount, maxDodgeAmount);
    }

    public void addDodgeAmount() {
        if (dodgeAmount < maxDodgeAmount) dodgeAmount++;
    }

    public void subDodgeAmount() {
        if (dodgeAmount > 0) dodgeAmount--;
    }

    public int getMaxDodgeAmount() {
        return maxDodgeAmount;
    }

    public void setMaxDodgeAmount(int max) {
        maxDodgeAmount = max;
        dodgeAmount = Math.min(dodgeAmount, max); // Adjust current amount if needed
    }

    public float getDodgeStaminaRatio() {
        return ((float) dodgeAmount / maxDodgeAmount) * 100f;
    }

    public static boolean isSpectator(Entity entity) {
        return entity instanceof Player player && player.isSpectator();
    }

    @Override
    public boolean canUse() {
        return dodgeAmount > 0 && !isSpectator(entity.getEntity());
    }

    @Override
    public boolean canKeepUsing() {
        return dodgeAmount > 0;
    }

    @Override
    public void startUsing() {
        if (entity.getEntity() instanceof Player player && this.getController().getHoldTicks() == 0) {
            player.displayClientMessage(
                    new TranslatableComponent("changed_addon.ability.dodge.dodge_amount", getDodgeStaminaRatio()),
                    true
            );
        }
    }

    @Override
    public void tick() {
        setDodgeActivate(canUse());
    }

    @Override
    public void stopUsing() {
        if (isDodgeActive()) {
            setDodgeActivate(false);
        }
    }

    @Override
    public void tickIdle() {
        boolean nonHurtFrame = entity.getEntity().hurtTime <= 5 && entity.getEntity().hurtDuration <= 5;
        if (nonHurtFrame && !isDodgeActive() && dodgeAmount < maxDodgeAmount) {
            if (dodgeRegenCooldown <= 0) {
                addDodgeAmount();
                dodgeRegenCooldown = 5;

                if (entity.getEntity() instanceof Player player) {
                    player.displayClientMessage(
                            new TranslatableComponent("changed_addon.ability.dodge.dodge_amount", getDodgeStaminaRatio()),
                            true
                    );
                }
            } else {
                dodgeRegenCooldown--;
            }
        }
    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);
        if (tag.contains("DodgeAmount")) dodgeAmount = tag.getInt("DodgeAmount");
        if (tag.contains("MaxDodgeAmount")) maxDodgeAmount = tag.getInt("MaxDodgeAmount");
        if (tag.contains("DodgeRegenCooldown")) dodgeRegenCooldown = tag.getInt("DodgeRegenCooldown");
        if (tag.contains("DodgeActivate")) dodgeActive = tag.getBoolean("DodgeActivate");
    }

    @Override
    public void saveData(CompoundTag tag) {
        super.saveData(tag);
        tag.putInt("DodgeAmount", dodgeAmount);
        tag.putInt("MaxDodgeAmount", maxDodgeAmount);
        tag.putInt("DodgeRegenCooldown", dodgeRegenCooldown);
        tag.putBoolean("DodgeActivate", dodgeActive);
    }
}
