package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.variant.IVariantExtraStats;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.client.AbilityColors;
import net.ltxprogrammer.changed.client.gui.AbstractRadialScreen;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Optional;


public class WingFlapAbility extends AbstractAbility<WingFlapAbility.AbilityInstance> {

    public static final int MAX_TICK_HOLD = 30;
    public static final int TICK_HOLD_NEED = 10;

    public static final float AVALI_WING_FLAP_TARGET_Z = (float) Math.toRadians(90);
    public static final float AVALI_WING_FLAP_TARGET_Y = (float) Math.toRadians(90);

    public WingFlapAbility() {
        super(WingFlapAbility.AbilityInstance::new);
    }

    public static Optional<Integer> getColor(AbstractAbilityInstance abilityInstance, int layer) {
        AbstractRadialScreen.ColorScheme scheme = AbilityColors.getAbilityColors(abilityInstance);
        if (abilityInstance instanceof WingFlapAbility.AbilityInstance Instance) {
            if (Instance.dashPower < 0.3f && layer == 0) {
                return Optional.of(scheme.foreground().toInt());
            } else if (Instance.dashPower >= 0.3f && Instance.dashPower < 0.95F && layer == 1) {
                return Optional.of(scheme.foreground().toInt());
            } else if (Instance.dashPower >= 0.95F && layer == 2) {
                return Optional.of(scheme.foreground().toInt());
            }
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        if (entity.getEntity() instanceof Player player) {
            AbilityInstance Instance = ProcessTransfur.getPlayerTransfurVariant(player).getAbilityInstance(this);
            if (Instance.dashPower <= 0.1f) {
                return ResourceLocation.parse("changed_addon:textures/screens/wing_flap_ability_start.png");
            } else if (Instance.dashPower >= 0.3f && Instance.dashPower < 0.95F) {
                return ResourceLocation.parse("changed_addon:textures/screens/wing_flap_ability_mid.png");
            } else if (Instance.dashPower >= 0.95F) {
                return ResourceLocation.parse("changed_addon:textures/screens/wing_flap_ability_final.png");
            }
        }

        return ResourceLocation.parse("changed_addon:textures/screens/wing_flap_ability_start.png");
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.wing_flap");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        if (entity.getEntity() instanceof Player player) {
            if (player.getAbilities().flying) {
                return UseType.CHARGE_TIME;
            } else if (player.isFallFlying()) {
                return UseType.HOLD;
            } else if (player.onGround()) {
                return UseType.HOLD;
            }
        }

        return UseType.INSTANT;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        if (entity.getEntity() instanceof Player player) {
            if (player.getAbilities().flying) {
                return 10;
            } else if (player.isFallFlying()) {
                return 45;
            } else if (player.onGround()) {
                return 10;
            }
        }
        return 30;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        if (entity.getEntity() instanceof Player player) {
            if (player.getAbilities().flying) {
                return 15;
            } else if (player.isFallFlying()) {
                return 25;
            } else if (player.onGround()) {
                return 20;
            }
        }
        return 15;
    }

    @Override
    public boolean shouldApplyCoolDown(IAbstractChangedEntity entity) {
        WingFlapAbility.AbilityInstance abilityInstance = entity.getAbilityInstance(this);
        if (abilityInstance != null) return abilityInstance.shouldApplyCooldown();

        return super.shouldApplyCoolDown(entity);
    }

    public static class AbilityInstance extends AbstractAbilityInstance {

        public boolean readyToDash = false;
        public float dashPower = 0;
        private float lastDashPower = dashPower;

        public AbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
            super(ability, entity);
        }

        private static void playSound(Player player) {
            if (!player.level().isClientSide()) {
                player.level().playSound(null, player.blockPosition(), ChangedSounds.CARDBOARD_BOX_OPEN.get(),
                        player.getSoundSource(), 2.5F, 1.0F);
            }
        }

        private static void playFlapSound(Player player) {
            playFlapSound(player, 1);
        }

        private static void playFlapSound(Player player, float pitch) {
            if (!player.level().isClientSide()) {
                player.level().playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_FLAP,
                        player.getSoundSource(), 2.5F, pitch);
                player.gameEvent(GameEvent.ELYTRA_GLIDE);
            }
        }

        private static void exhaustPlayer(Player player, float exhaustion) {
            if (!player.isCreative()) {
                player.causeFoodExhaustion(exhaustion);
            }
        }

        private static float capLevel(float value, float min, float max) {
            if (value < min) {
                return min;
            } else if (value > max) {
                return max;
            }
            return value;
        }

        @Override
        public void saveData(CompoundTag tag) {
            super.saveData(tag);
        }

        @Override
        public void readData(CompoundTag tag) {
            super.readData(tag);
        }

        @Override
        public boolean canUse() {
            if (this.entity.getChangedEntity() instanceof IVariantExtraStats extraStats) {
                if (extraStats.getFlyType() == IVariantExtraStats.FlyType.NONE) {
                    return false;
                }
            }

            if (entity.getEntity() instanceof Player player) {
                if (!player.onGround() && !player.getAbilities().flying && !player.isFallFlying()) {
                    return false;
                }
            }

            return this.entity.getSelfVariant() != null && this.entity.getSelfVariant().canGlide;
        }

        @Override
        public boolean canKeepUsing() {
            if (this.entity.getChangedEntity() instanceof IVariantExtraStats extraStats) {
                if (extraStats.getFlyType() == IVariantExtraStats.FlyType.NONE) {
                    return false;
                }
            }

            if (entity.getEntity() instanceof Player player) {
                if (!player.onGround() && !player.getAbilities().flying && !player.isFallFlying()) {
                    return false;
                }
            }

            return this.entity.getSelfVariant() != null && this.entity.getSelfVariant().canGlide;
        }

        @Override
        public void startUsing() {
            if (!(entity.getEntity() instanceof Player player) || player.getFoodData().getFoodLevel() <= 6) {
                return;
            }

            if (player.isInWater() || player.isSpectator()) {
                return;
            }

            if (player.getAbilities().flying && !player.isFallFlying()) {
                double speed = 2;
                player.setDeltaMovement(player.getDeltaMovement().add(player.getViewVector(1).multiply(speed, speed, speed)));
                playFlapSound(player);
                exhaustPlayer(player, 0.8F);
            }
        }

        @Override
        public void tick() {
            if (!(entity.getEntity() instanceof Player player) || player.getFoodData().getFoodLevel() <= 6) {
                return;
            }

            this.dashPower = capLevel((float) getController().getHoldTicks() / MAX_TICK_HOLD, 0, 1);
            if (getController().getHoldTicks() >= TICK_HOLD_NEED) {
                this.readyToDash = true;
            }


            if (this.dashPower >= 1 && getController().getHoldTicks() == MAX_TICK_HOLD) {
                player.playSound(SoundEvents.ENDER_DRAGON_FLAP, 1, 2F);
            }


            if (player.level().isClientSide() && ChangedAddonClientConfiguration.WING_FLAP_INFO.get()) {
                player.displayClientMessage(Component.literal("Ticks = " + getController().getHoldTicks()), true);
            }
        }

        public boolean shouldApplyCooldown() {
            return this.lastDashPower > 0;
        }

        @Override
        public void stopUsing() {
            if (!(entity.getEntity() instanceof Player player) || player.getFoodData().getFoodLevel() <= 6) {
                return;
            }

            if (player.isInWater() || player.isSpectator()) {
                return;
            }
            if (player.isFallFlying() && !player.getAbilities().flying && readyToDash) {
                this.readyToDash = false;
                double speed = 0.25f * dashPower;
                player.setDeltaMovement(player.getDeltaMovement().add(player.getViewVector(1).multiply(speed, speed, speed)));
                playFlapSound(player);
                exhaustPlayer(player, 4F * dashPower);
                this.dashPower = 0;
            } else if (player.onGround() && player.getXRot() <= -45 && readyToDash) {
                this.readyToDash = false;
                double speed = 2 * dashPower;
                player.setDeltaMovement(player.getDeltaMovement().add(player.getViewVector(1).multiply(0, speed, 0)));
                playFlapSound(player, 0.5F);
                exhaustPlayer(player, 4F * dashPower);
                this.dashPower = 0;
            }

            this.dashPower = 0;
        }

        @Override
        public void tickIdle() {
            super.tickIdle();
            this.lastDashPower = dashPower;
        }
    }
}
