package net.foxyas.changedaddon.abilities;

import net.foxyas.changedaddon.init.ChangedAddonAnimationEvents;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAnimationEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.Nullable;

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
        return dodgeActive;
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


    public void executeDodge(ServerLevel serverLevel, Player player, @Nullable LivingAttackEvent event) {
        this.subDodgeAmount();
        player.displayClientMessage(new TranslatableComponent("changed_addon.ability.dodge.dodge_amount_left", this.getDodgeStaminaRatio()), false);
        player.invulnerableTime = 20;
        player.hurtDuration = 20;
        player.hurtTime = player.hurtDuration;
        player.causeFoodExhaustion(8f);
        if (event != null) {
            event.setCanceled(true);
        }
        spawnDodgeParticles(serverLevel, player, 0.5f, 0.3f, 0.3f, 0.3f, 10, 0.25f);
        int randomValue = serverLevel.getRandom().nextInt(4);
        switch (randomValue) {
            case 0 -> ChangedAnimationEvents.broadcastEntityAnimation(player, ChangedAddonAnimationEvents.DODGE_LEFT.get(), null);
            case 1 -> ChangedAnimationEvents.broadcastEntityAnimation(player, ChangedAddonAnimationEvents.DODGE_RIGHT.get(), null);
            case 2 -> ChangedAnimationEvents.broadcastEntityAnimation(player, ChangedAddonAnimationEvents.DODGE_WEAVE.get(), null);
            case 3 -> ChangedAnimationEvents.broadcastEntityAnimation(player, ChangedAddonAnimationEvents.DODGE_DOWN.get(), null);
            //default -> ChangedAnimationEvents.broadcastEntityAnimation(player, ChangedAddonAnimationEvents.DODGE_LEFT.get(), null);
        }
    }

    public void executeDodge(Player player) {
        if (player.getLevel() instanceof ServerLevel serverLevel) {
            this.executeDodge(serverLevel, player, null);
        }
    }

    private void spawnDodgeParticles(ServerLevel level, Entity entity, float middle, float xV, float yV, float zV, int count, float speed) {
        level.sendParticles(ParticleTypes.POOF,
                entity.getX(), entity.getY() + middle, entity.getZ(), count, xV, yV, zV, speed);
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
        return dodgeAmount > 0 && !isSpectator(entity.getEntity());
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
        //super.tick();
        if (entity.getEntity() instanceof Player player) {
            if (!(player.getLevel().isClientSide())) {
                player.displayClientMessage(
                        new TranslatableComponent("changed_addon.ability.dodge.dodge_amount", getDodgeStaminaRatio()), true);
            }
        }
        setDodgeActivate(canUse());
    }

    @Override
    public void stopUsing() {
        //super.stopUsing();
        setDodgeActivate(false);
    }

    @Override
    public void tickIdle() {
        super.tickIdle();
        boolean nonHurtFrame = entity.getEntity().hurtTime <= 10 && entity.getEntity().invulnerableTime <= 10;
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
