package net.foxyas.changedaddon.process.util;

import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.foxyas.changedaddon.registers.ChangedAddonDamageSources;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class ChangedAddonLaethinminatorUtil {

    public static void shootDynamicLaser(Level world, Player player, int maxRange, int horizontalRadius, int verticalRadius) {
        if (world instanceof ServerLevel serverLevel) {
            shootDynamicLaser(serverLevel, player, maxRange, horizontalRadius, verticalRadius);
        }
    }

    public static void shootDynamicLaser(ServerLevel world, Player player, int maxRange, int horizontalRadius, int verticalRadius) {
        Vec3 eyePosition = player.getEyePosition(1.0F); // Posição dos olhos do jogador
        Vec3 lookDirection = player.getLookAngle();    // Direção para onde o jogador está olhando
        //aplicar um efeito de particulas de "gas"
        Color StartColor = new Color(255, 255, 255, 255);
        Color EndColor = new Color(255, 179, 179, 255);
        ParticleOptions particleOptions = getParticleOptions(StartColor, EndColor);

        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookDir = player.getLookAngle().normalize();

        for (int i = 1; i <= maxRange; i++) {
            // Calcula a posição do bloco na trajetória do laser
            Vec3 targetVec = eyePosition.add(lookDirection.scale(i));
            BlockPos targetPos = new BlockPos(targetVec);

            Vec3 particlePos = eyePos.add(lookDir.scale(i * 0.5));
            PlayerUtilProcedure.ParticlesUtil.sendParticles(world, particleOptions, particlePos, 0.25f, 0.25f, 0.25f, 2, 0f);

            // Verifica se o bloco é ar; se for, ignora essa fileira
            if (world.getBlockState(targetPos).isAir()) {
                continue;
            }

            // Afeta os blocos ao redor do ponto atual
            affectSurroundingEntities(world, player, targetPos, 4);
            affectSurroundingBlocks(world, targetPos, horizontalRadius, verticalRadius);
        }
    }

    public static void affectSurroundingEntities(ServerLevel world, Player player, BlockPos targetPos, double area) {
        List<ChangedEntity> entityList = world.getEntitiesOfClass(ChangedEntity.class, new AABB(targetPos).inflate(area), (changedEntity) -> changedEntity.getType().is(ChangedTags.EntityTypes.LATEX));
        for (ChangedEntity en: entityList) {
            boolean isAllied = player.isAlliedTo(en);
            if (player.canAttack(en)
                    && player.canHit(en,0)
                    && !isAllied){
                en.hurt(ChangedAddonDamageSources.mobAttack(player).setProjectile(),6f);
            }
        }
    }

    private static void affectSurroundingBlocks(Level world, BlockPos center, int horizontalRadius, int verticalRadius) {
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
                        if (world.getBlockState(affectedPos).isAir()) {
                            break;
                        }
                        // Insira a lógica para afetar os blocos
                        affectBlock(world, affectedPos);
                    }
                }
            }
        }
    }

    private static void affectBlock(Level world, BlockPos pos) {
        // Exemplo de lógica personalizada para afetar blocos
        if (!world.getBlockState(pos).isAir()) {
            // Substituir bloco por vidro como exemplo
            BlockState stage = world.getBlockState(pos);
            if (!stage.hasProperty(AbstractLatexBlock.COVERED)){
                return;
            }
            if (stage.getValue(AbstractLatexBlock.COVERED) != LatexType.NEUTRAL){
                BlockState NewStage = world.getBlockState(pos).setValue(AbstractLatexBlock.COVERED, LatexType.NEUTRAL);
                world.setBlock(pos, NewStage, 3);
                Color StartColor = new Color(255, 255, 255, 255);
                Color EndColor = new Color(93, 93, 93, 255);
                ParticleOptions particleOptions = getParticleOptions(StartColor,EndColor);

                // Adicionar partículas no bloco afetado
                PlayerUtilProcedure.ParticlesUtil.sendParticles(world, particleOptions, pos, 0.25f, 0.25f, 0.25f, 1, 0f);
            }
        }
    }

    @NotNull
    private static ParticleOptions getParticleOptions(Color StartColor ,Color EndColor) {
        Vector3f startColor = new Vector3f((float) StartColor.getRed() / 255, (float) StartColor.getGreen() / 255, (float) StartColor.getBlue() / 255);
        Vector3f endColor = new Vector3f((float) EndColor.getRed() / 255, (float) EndColor.getGreen() / 255, (float) EndColor.getBlue() / 255);
        return new DustColorTransitionOptions(startColor, endColor, 1);
    }
}
