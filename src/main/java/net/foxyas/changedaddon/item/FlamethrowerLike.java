package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.block.LatexCoverBlock;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.item.SpecializedAnimations;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static net.foxyas.changedaddon.util.FoxyasUtils.getRelativePosition;

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
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.pass(itemstack);
    }

    public void shoot(Level level, Player player, int maxRange, int horizontalRadius, int verticalRadius) {
        if (level instanceof ServerLevel serverLevel) {
            shoot(serverLevel, player, maxRange, horizontalRadius, verticalRadius);
        }
    }

    protected abstract ParticleOptions particle();

    protected void shoot(ServerLevel level, Player player, int maxRange, int horizontalRadius, int verticalRadius) {
        Vec3 eyePosition = player.getEyePosition(1.0F); // Posição dos olhos do jogador
        //Vec3 lookDirection = player.getLookAngle().normalize();    // Direção para onde o jogador está olhando
        //aplicar um efeito de particulas de "gas"
        InteractionHand hand = player.getUsedItemHand();

        ParticleOptions particle = particle();
        for (int i = 1; i <= maxRange; i++) {
            // Calcula a posição do bloco na trajetória do laser
            Vec3 targetVec = eyePosition.add(getRelativePosition(player, 0, 0, i, true));
            BlockPos targetPos = new BlockPos(targetVec);

            double deltaX = hand == InteractionHand.MAIN_HAND ? 0.25 : -0.25;
            if (player.getMainArm() == HumanoidArm.LEFT) deltaX = -deltaX;

            Vec3 relativePosition = getRelativePosition(player, deltaX, 0, i * 0.5 + 1f, true);
            Vec3 maxRelativePosition = getRelativePosition(player, deltaX, 0, maxRange * 0.5, true);
            ParticlesUtil.sendParticlesWithMotionAndOffset(player, particle, player.getEyePosition().add(relativePosition), new Vec3(0.15f, 0.15f, 0.15f), maxRelativePosition, new Vec3(0.25f, 0.25f, 0.25f), 2, 0.10f);

            // Verifica se o bloco é ar; se for, ignora essa fileira
            if (level.getBlockState(targetPos).isAir()) {
                // Afeta os blocos ao redor do ponto atual
                affectSurroundingEntities(level, player, targetVec, 4 * ((double) i / maxRange));
                continue;
            } else {
                affectSurroundingEntities(level, player, targetVec, 4 * ((double) i / maxRange));
            }
            affectSurroundingBlocks(level, targetPos, horizontalRadius, verticalRadius, particle);
        }
    }

    protected void affectSurroundingEntities(ServerLevel level, Player player, Vec3 targetPos, double area) {
        List<LivingEntity> entityList = level.getEntitiesOfClass(LivingEntity.class, new AABB(targetPos, targetPos).inflate(area));
        for (LivingEntity en : entityList) {
            boolean isAllied = player.isAlliedTo(en);
            if (player.canAttack(en) && !isAllied) {
                affectEntity(player, en);
            }
        }
    }

    protected abstract void affectEntity(Player shooter, LivingEntity entity);

    protected void affectSurroundingBlocks(Level level, BlockPos center, int horizontalRadius, int verticalRadius, ParticleOptions particle) {
        int horizontalRadiusSphere = horizontalRadius - 1;
        int verticalRadiusSphere = verticalRadius - 1;

        for (int y = -verticalRadiusSphere; y <= verticalRadiusSphere; y++) {
            for (int x = -horizontalRadiusSphere; x <= horizontalRadiusSphere; x++) {
                for (int z = -horizontalRadiusSphere; z <= horizontalRadiusSphere; z++) {
                    // Calcula a distância ao centro para uma forma esférica
                    double distanceSq = (x * x) / (double) (horizontalRadiusSphere * horizontalRadiusSphere) +
                            (y * y) / (double) (verticalRadiusSphere * verticalRadiusSphere) +
                            (z * z) / (double) (horizontalRadiusSphere * horizontalRadiusSphere);

                    if (distanceSq <= 1.0) { // Dentro da área de efeito
                        BlockPos affectedPos = center.offset(x, y, z);
                        if (level.getBlockState(affectedPos).isAir()) {
                            break;
                        }
                        // Insira a lógica para afetar os blocos
                        affectBlock(level, affectedPos);
                    }
                }
            }
        }
    }

    protected void affectBlock(Level level, BlockPos pos) {
        if (!level.getBlockState(pos).isAir()) {
            // Substituir bloco por vidro como exemplo
            BlockState stage = level.getBlockState(pos);
            if (stage.getBlock() instanceof LatexCoverBlock) {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

                // Adicionar partículas no bloco afetado
                ParticlesUtil.sendParticles(level, particle(), pos, 0.25f, 0.25f, 0.25f, 1, 0f);
                return;
            }

            if (!stage.hasProperty(AbstractLatexBlock.COVERED)) {
                return;
            }
            if (stage.getValue(AbstractLatexBlock.COVERED) != LatexType.NEUTRAL) {
                BlockState NewStage = level.getBlockState(pos).setValue(AbstractLatexBlock.COVERED, LatexType.NEUTRAL);
                level.setBlock(pos, NewStage, 3);

                // Adicionar partículas no bloco afetado
                ParticlesUtil.sendParticles(level, particle(), pos, 0.25f, 0.25f, 0.25f, 1, 0f);
            }
        }
    }

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
