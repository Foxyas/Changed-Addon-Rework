package net.foxyas.changedaddon.abilities;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;

public class ThunderPathAbility extends AbstractAbility<ThunderPathAbility.Instance> {

    public ThunderPathAbility() {
        super(ThunderPathAbility.Instance::new);
    }

    public static class Instance extends AbstractAbilityInstance {

        private final IAbstractChangedEntity owner;

        public Instance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
            super(ability, entity);
            owner = entity;
        }

        private int thunderIndex = 1;

        public int MaxThunderIndex = 10;

        public Vec3 startPos = Vec3.ZERO;

        @Override
        public void saveData(CompoundTag tag) {
            super.saveData(tag);
            tag.putInt("MaxThunderReach", MaxThunderIndex);
            tag.putInt("ThunderPhase", thunderIndex);
        }

        @Override
        public void readData(CompoundTag tag) {
            super.readData(tag);
            if (tag.contains("ThunderPhase")) {
                thunderIndex = tag.getInt("ThunderPhase");
            }
            if (tag.contains("MaxThunderReach")) {
                MaxThunderIndex = tag.getInt("MaxThunderReach");
            }
        }

        @Override
        public boolean canUse() {
            this.MaxThunderIndex = (int) ReachAmount(entity);
            Player player = (Player) entity.getEntity();
            TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
            return player.getFoodData().getFoodLevel() >= 10 && (Variant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009.get() || Variant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get()) && !Spectator(entity.getEntity());
        }

        public static boolean Spectator(Entity entity) {
            if (entity instanceof Player player1) {
                return player1.isSpectator();
            }
            return true;
        }

        public UseType getUseType() {
            return UseType.HOLD;
        }


        public float ReachAmount(IAbstractChangedEntity entity) {
            TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
            if (Variant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get()) {
                return 15;
            }
            return 10;
        }

        public int MaxAmount(IAbstractChangedEntity entity) {
            TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
            if (Variant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get()) {
                return 8;
            }
            return 5;
        }


        @Override
        public boolean canKeepUsing() {
            return thunderIndex < MaxThunderIndex;
        }

        @Override
        public void startUsing() {
        	startPos = owner.getEntity().position();
        }

        @Override
        public void tick() {
            if (!(owner.getEntity() instanceof Player player) || !(owner.getLevel() instanceof ServerLevel serverLevel))
                return;

            AbstractAbilityInstance abilityInstance = this;

            int ticks = abilityInstance.getController().getHoldTicks();

            Vec3 forward = owner.getEntity().getLookAngle().scale(ReachAmount(owner)); // pega só uma vez no início
            Vec3 forwardCap = owner.getEntity().getLookAngle().scale(2f); // pega só uma vez no início


            if (ticks % 4 == 0 && thunderIndex < MaxThunderIndex) {
                Vec3 currentPos = this.startPos.add(forwardCap).add(forward.scale((double) thunderIndex / MaxThunderIndex));

                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
                if (bolt != null) {
                    bolt.moveTo(Vec3.atBottomCenterOf(new BlockPos(currentPos)));
                    bolt.setVisualOnly(false);
                    serverLevel.addFreshEntity(bolt);
                }

                thunderIndex++;
            }

            if (thunderIndex >= MaxThunderIndex) {
                thunderIndex = 0;
                abilityInstance.getController().deactivateAbility(); // Para a habilidade
                abilityInstance.getController().applyCoolDown();
            }
        }

        @Override
        public void stopUsing() {

			if (thunderIndex > 0) {
                thunderIndex = 0;
                this.getController().applyCoolDown();
            }
        }

        @Override
        public void tickIdle() {
            super.tickIdle();
            if (this.getController().isCoolingDown()) {
                thunderIndex = 0;
            }
        }
    }

    @Override
    public TranslatableComponent getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.thunder_path");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/thunderbolt.png");
    }

    public static boolean Spectator(Entity entity) {
        if (entity instanceof Player player1) {
            return player1.isSpectator();
        }
        return true;
    }

    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.HOLD;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
        if (Variant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get()) {
            return 15;
        }
        return 20;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
        if (Variant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get()) {
            return 30;
        }
        return 45;
    }


    public float ReachAmount(IAbstractChangedEntity entity) {
        TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
        if (Variant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get()) {
            return 8;
        }
        return 5;
    }


    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
    }


    private static boolean isHandEmpty(Entity entity, InteractionHand hand) {
        return entity instanceof LivingEntity livingEntity && livingEntity.getItemInHand(hand).getItem() == Blocks.AIR.asItem();
    }

    private static InteractionHand getSwingHand(Entity entity) {
        return isHandEmpty(entity, InteractionHand.MAIN_HAND) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }
}
