package net.foxyas.changedaddon.process.util;

import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.init.ChangedAddonDamageSources;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

import static net.foxyas.changedaddon.process.util.FoxyasUtils.getRelativePosition;

public class ChangedAddonLaethinminatorUtil {

    public static void shootDynamicLaser(Level world, Player player, int maxRange, int horizontalRadius, int verticalRadius) {
        if (world instanceof ServerLevel serverLevel) {
            shootDynamicLaser(serverLevel, player, maxRange, horizontalRadius, verticalRadius);
        }
    }

    public static void spawnDirectionalParticle(ServerLevel level, Player player, ParticleOptions particleType, float speed) {
        if (level == null || player == null) return;

        // Posição base nos olhos da entidade
        Vec3 eyePos = player.getEyePosition();

        // Vetores de direção
        Vec3 frontVector = player.getViewVector(0.5F);
        Vec3 rightVector = frontVector.cross(player.getUpVector(0.5F));

        // Define deslocamento lateral com base na mão usada
        float dir = (player.getUsedItemHand() == InteractionHand.MAIN_HAND == (player.getMainArm() == HumanoidArm.RIGHT) ? 1.0F : -1.0F) * 0.33F;

        // Adiciona deslocamentos na posição inicial
        Vec3 particlePos = eyePos
                .add(frontVector.scale(0.75))  // Empurra para frente
                .add(rightVector.scale(dir))   // Desloca para o lado
                .add(0.0, -0.5, 0.0);          // Ajusta altura

        // Gera um ângulo de disparo aleatório
        //float randX = (level.random.nextFloat(90.0F) - 45.0F) * 0.5F;
        //float randY = (level.random.nextFloat(90.0F) - 45.0F) * 0.5F;

        // Converte os ângulos de rotação (pitch e yaw) em um vetor direcional
        float pitch = (player.getXRot()) * ((float) Math.PI / 180F); // Converte para radianos
        float yaw = (player.getYRot()) * ((float) Math.PI / 180F);   // Converte para radianos

        // Calcula a direção baseada nos ângulos
        Vec3 shootDirection = new Vec3(
                -Math.sin(yaw) * Math.cos(pitch), // Direção X
                -Math.sin(pitch),                 // Direção Y
                Math.cos(yaw) * Math.cos(pitch)   // Direção Z
        ).scale(speed); // Aplica velocidade

        // Envia a partícula para o nível
        PlayerUtil.ParticlesUtil.sendParticles(level, particleType, particlePos, 0.15f, 0.15f, 0.15f, 2, 0f);
    }


    public static void shootDynamicLaser(ServerLevel world, Player player, int maxRange, int horizontalRadius, int verticalRadius) {
        Vec3 eyePosition = player.getEyePosition(1.0F); // Posição dos olhos do jogador
        //Vec3 lookDirection = player.getLookAngle().normalize();    // Direção para onde o jogador está olhando
        //aplicar um efeito de particulas de "gas"
        Color StartColor = new Color(255, 255, 255, 255);
        Color EndColor = new Color(255, 179, 179, 255);
        ParticleOptions particleOptions = getParticleOptions(StartColor, EndColor);

        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookDir = player.getLookAngle().normalize();

        for (int i = 1; i <= maxRange; i++) {
            // Calcula a posição do bloco na trajetória do laser
            Vec3 targetVec = eyePosition.add(getRelativePosition(player, 0, 0, i, true));
            BlockPos targetPos = new BlockPos(targetVec);

            Vec3 particlePos = eyePos.add(lookDir.scale(i * 0.5));
            PlayerUtil.ParticlesUtil.sendParticles(world, particleOptions, particlePos, 0.25f, 0.25f, 0.25f, 2, 0f);

            // Verifica se o bloco é ar; se for, ignora essa fileira
            if (world.getBlockState(targetPos).isAir()) {
                // Afeta os blocos ao redor do ponto atual
                affectSurroundingEntities(world, player, targetVec, 4 * ((double) i / maxRange));
                continue;
            } else {
                affectSurroundingEntities(world, player, targetVec, 4 * ((double) i / maxRange));
            }
            affectSurroundingBlocks(world, targetPos, horizontalRadius, verticalRadius);
        }
    }

    public static void affectSurroundingEntities(ServerLevel world, Player player, Vec3 targetPos, double area) {
        List<ChangedEntity> entityList = world.getEntitiesOfClass(ChangedEntity.class, new AABB(targetPos, targetPos).inflate(area), (changedEntity) -> changedEntity.getType().is(ChangedTags.EntityTypes.LATEX));
        for (ChangedEntity en : entityList) {
            boolean isAllied = player.isAlliedTo(en);
            if (player.canAttack(en)
                    && player.canHit(en, 0)
                    && !isAllied) {
                en.hurt(ChangedAddonDamageSources.mobAttack(player).setProjectile(), 6f);
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
            if (!stage.hasProperty(AbstractLatexBlock.COVERED)) {
                return;
            }
            if (stage.getValue(AbstractLatexBlock.COVERED) != LatexType.NEUTRAL) {
                BlockState NewStage = world.getBlockState(pos).setValue(AbstractLatexBlock.COVERED, LatexType.NEUTRAL);
                world.setBlock(pos, NewStage, 3);
                Color StartColor = new Color(255, 255, 255, 255);
                Color EndColor = new Color(93, 93, 93, 255);
                ParticleOptions particleOptions = getParticleOptions(StartColor, EndColor);

                // Adicionar partículas no bloco afetado
                PlayerUtil.ParticlesUtil.sendParticles(world, particleOptions, pos, 0.25f, 0.25f, 0.25f, 1, 0f);
            }
        }
    }

    @NotNull
    private static ParticleOptions getParticleOptions(Color StartColor, Color EndColor) {
        Vector3f startColor = new Vector3f((float) StartColor.getRed() / 255, (float) StartColor.getGreen() / 255, (float) StartColor.getBlue() / 255);
        Vector3f endColor = new Vector3f((float) EndColor.getRed() / 255, (float) EndColor.getGreen() / 255, (float) EndColor.getBlue() / 255);
        return new DustColorTransitionOptions(startColor, endColor, 1);
    }
}
