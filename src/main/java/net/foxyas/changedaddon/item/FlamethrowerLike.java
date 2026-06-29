package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.block.LatexCoverBlock;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.foxyas.changedaddon.util.GasAreaUtil;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.latex.SpreadingLatexType;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.item.SpecializedAnimations;
import net.ltxprogrammer.changed.util.Color3;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class FlamethrowerLike extends Item implements SpecializedAnimations {

    public FlamethrowerLike(Properties pProperties) {
        super(pProperties);
    }

    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().isUsingItem())
            return InteractionResult.PASS;
        return super.useOn(context);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        if (player != null) {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.pass(itemstack);
    }

    public void shoot(Level level, Player player, double maxRange, int horizontalRadius, int verticalRadius) {
        if (level instanceof ServerLevel serverLevel) {
            shoot(serverLevel, player, maxRange, horizontalRadius, verticalRadius);
        }
    }

    protected abstract ParticleOptions particle();

    protected void shoot(ServerLevel level, Player player, double range, int horizontalRadius, int verticalRadius) {
        spawnParticles(level, player, range);

        List<GasAreaUtil.GasHit> hits = GasAreaUtil.getGasConeHits(
                level,
                player,
                range,
                0.35,   // spread
                2       // density
        );

        hits = GasAreaUtil.dedupe(hits);

        applyEffectToLatexCovers(level, hits);

        List<GasAreaUtil.GasHitBlock> blockHits = GasAreaUtil.getGasConeHitsNormalBlocks(level,
                player,
                range,
                0.35,  // spread
                2   // density
        );

        applyEffectToBlocks(level, blockHits);

        List<Vec3> gasVolume = GasAreaUtil.sampleGasCone(
                player,
                range * 1.5,
                0.01f,
                0.6
        );

        applyEffectToEntities(level, player, gasVolume);
    }

    protected void spawnParticles(ServerLevel level, Player player, double range) {
        Vec3 eye = player.getEyePosition(1.0F);
        Vec3 look = player.getLookAngle().normalize();
        InteractionHand hand = player.getUsedItemHand();


        for (int i = 1; i <= range; i++) {
            double deltaX = hand == InteractionHand.MAIN_HAND ? 0.25 : -0.25;
            if (player.getMainArm() == HumanoidArm.LEFT) deltaX = -deltaX;

            Vec3 relativePosition = FoxyasUtil.getRelativePosition(player, deltaX, 0, i * 0.5 + 1f, true);
            Vec3 maxRelativePosition = FoxyasUtil.getRelativePosition(player, deltaX, 0, range * 0.5, true);
            ParticlesUtil.sendParticlesWithMotionAndOffset(player, this.particle(), player.getEyePosition().add(relativePosition), new Vec3(0.15f, 0.15f, 0.15f), maxRelativePosition, new Vec3(0.25f, 0.25f, 0.25f), 2, 0.10f);


            //            FoxyasUtils.getRelativePosition(player, 0, 0, i, true);
//            Vec3 pos = eye.add(look.scale(i * 0.6));
//            ParticlesUtil.sendParticlesWithMotion(
//                    level,
//                    this.particle(),
//                    pos,
//                    new Vec3(0.15f, 0.15f, 0.15f),
//                    look.scale(i * 0.5d),
//                    2,
//                    0.10f
//            );
        }
    }

    protected void applyEffectToLatexCovers(ServerLevel level, List<GasAreaUtil.GasHit> hits) {
        for (GasAreaUtil.GasHit hit : hits) {
            BlockPos pos = hit.pos();

            LatexCoverState state = LatexCoverState.getAt(level, pos);
            if (state.isAir())
                continue;

            if (!(state.getType() instanceof SpreadingLatexType spreading))
                continue;

            Integer saturationValue = hit.state().getValue(SpreadingLatexType.SATURATION);
            BooleanProperty faceProp = SpreadingLatexType.FACES.get(hit.face().getOpposite());
            if (faceProp == null)
                continue;

            if (!state.hasProperty(faceProp))
                continue;

            if (!state.getValue(faceProp))
                continue; // já limpo

            LatexCoverState newState = state.setValue(faceProp, false).setValue(SpreadingLatexType.SATURATION, saturationValue);

            if (newState != state) {
                var sides = SpreadingLatexType.FACES.values().stream().map((newState::getValue));
                if (sides.noneMatch(value -> value)) {
                    newState = ChangedLatexTypes.NONE.get().defaultCoverState();
                }

                LatexCoverState.setAtAndUpdate(level, pos, newState);


                // partículas no impacto
                ParticleOptions particle = ChangedParticles.gas(
                        Color3.fromInt(new Color(93, 93, 93).getRGB())
                );

                ParticlesUtil.sendParticles(
                        level,
                        particle,
                        pos,
                        0.25f, 0.25f, 0.25f,
                        1,
                        0f
                );
            }
        }
    }

    protected void applyEffectToBlocks(ServerLevel level, List<GasAreaUtil.GasHitBlock> hits) {
        for (GasAreaUtil.GasHitBlock hit : hits) {
            BlockPos pos = hit.pos();
            BlockState state = hit.state();

            if (state.isAir())
                continue;

            if (!(state.getBlock() instanceof LatexCoverBlock coverBlock))
                continue;

            BlockState newState = Blocks.AIR.defaultBlockState();

            if (newState != state) {
                level.setBlockAndUpdate(pos, newState);

                // partículas no impacto
                ParticleOptions particle = ChangedParticles.gas(
                        Color3.fromInt(new Color(93, 93, 93).getRGB())
                );

                ParticlesUtil.sendParticles(
                        level,
                        particle,
                        pos,
                        0.25f, 0.25f, 0.25f,
                        1,
                        0f
                );
            }
        }
    }

    protected void applyEffectToEntities(
            ServerLevel level,
            Player player,
            List<Vec3> gasVolume
    ) {
        Set<ChangedEntity> affected = new HashSet<>();

        for (Vec3 pos : gasVolume) {
            AABB area = new AABB(pos, pos).inflate(1);

            List<ChangedEntity> entities = level.getEntitiesOfClass(
                    ChangedEntity.class,
                    area,
                    e -> e.getType().is(ChangedTags.EntityTypes.LATEX)
            );

            for (ChangedEntity entity : entities) {
                if (!player.canAttack(entity)) continue;
                if (player.isAlliedTo(entity)) continue;
                if (player.is(entity)) continue;

                // evita dano duplicado exagerado
                if (!affected.add(entity)) continue;
                affectEntity(player, entity);
            }
        }
    }

    protected abstract void affectEntity(Player shooter, LivingEntity entity);

    @Nullable
    @Override
    public AnimationHandler getAnimationHandler() {
        return ANIMATION_CACHE.computeIfAbsent(this, Animator::new);
    }


    public static class Animator extends AnimationHandler {
        public Animator(Item item) {
            super(item);
        }

        @Override
        public void setupUsingAnimation(ItemStack itemStack, EntityStateContext entity, UpperModelContext model, HumanoidArm arm, float progress) {
            super.setupUsingAnimation(itemStack, entity, model, arm, progress);

            // Sets the correct arm depending on the hand used
            // HumanoidArm ContextArm = entity.livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? arm : arm.getOpposite();

            // Sets the arm rotation based on the player's head
            LivingEntity livingEntity = entity.livingEntity;
            model.getArm(arm).xRot = model.head.xRot - 1.570796f - (livingEntity.isCrouching() ? 0.2617994F : 0.0F);
            model.getArm(arm).yRot = model.head.yRot;

            // Silly animation [Intentionally not smooth due to lack of partial ticks and design]
        }

    }
}
