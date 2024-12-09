package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiFunction;
import net.minecraft.network.chat.TextComponent;

public class WingFlapAbility extends AbstractAbility<WingFlapAbility.AbilityInstance> {

    public static final int TICK_HOLD_NEED = 30;
    public WingFlapAbility() {
        super(AbilityInstance::new);
    }

    @Override
    public TranslatableComponent getDisplayName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.wing_flap");
    }

    @Override
    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/leap_ability.png");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        if (entity.getEntity() instanceof Player player){
            if (player.getAbilities().flying){
                return UseType.CHARGE_RELEASE;
            } else if (player.isFallFlying()) {
                return UseType.HOLD;
            }
        }

        return UseType.CHARGE_RELEASE;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        if (entity.getEntity() instanceof Player player){
            if (player.getAbilities().flying){
                return 5;
            } else if (player.isFallFlying()) {
                return 30;
            }
        }
        return 30;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        if (entity.getEntity() instanceof Player player){
            if (player.getAbilities().flying){
                return 15;
            } else if (player.isFallFlying()) {
                return 20;
            }
        }
        return 15;
    }

    public static class AbilityInstance extends AbstractAbilityInstance {


    	public boolean ReadytoDash = false;


        public AbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
            super(ability, entity);
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
            return this.entity.getSelfVariant() != null && this.entity.getSelfVariant().canGlide;
        }

        @Override
        public boolean canKeepUsing() {
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
                player.setDeltaMovement(player.getDeltaMovement().add(player.getViewVector(1).multiply(speed,speed,speed)));
                playSound(player);
                exhaustPlayer(player, 0.8F);
            }
        }

        @Override
        public void tick() {
         if (!(entity.getEntity() instanceof Player player) || player.getFoodData().getFoodLevel() <= 6) {
                return;
            }
            if (getController().getHoldTicks() >= TICK_HOLD_NEED){
				this.ReadytoDash = true;
            }

			player.displayClientMessage(new TextComponent("ticks = " + getController().getHoldTicks()),true);
        }

        @Override
        public void stopUsing() {
            if (!(entity.getEntity() instanceof Player player) || player.getFoodData().getFoodLevel() <= 6) {
                return;
            }

            if (player.isInWater() || player.isSpectator()) {
                return;
            }
            if (player.isFallFlying() && !player.getAbilities().flying && ReadytoDash){
            	this.ReadytoDash = false;
                double speed = 2;
                player.setDeltaMovement(player.getDeltaMovement().add(player.getViewVector(1).multiply(speed,speed,speed)));
                playSound(player);
                exhaustPlayer(player, 4F);
            }
        }

        private static void playSound(Player player) {
            if (!player.level.isClientSide()) {
                player.level.playSound(null, player.blockPosition(), ChangedSounds.BOW2,
                        player.getSoundSource(), 2.5F, 1.0F);
            }
        }

        private static void exhaustPlayer(Player player, float exhaustion) {
            if (!player.isCreative()) {
                player.causeFoodExhaustion(exhaustion);
            }
        }
    }
}
