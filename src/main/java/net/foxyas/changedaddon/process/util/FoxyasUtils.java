package net.foxyas.changedaddon.process.util;

import com.ibm.icu.impl.Pair;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FoxyasUtils {

    public static String SpawnEggIdFromTransfurVariant(TransfurVariant latexVariant) {
        String Util = latexVariant.getFormId().toString().replace("changed_addon:form_", "");
        return Util + "_spawn_egg";
    }


    public static Stream<BlockPos> getBlockPositionsInSphere(BlockPos center, int radius) {
        return BlockPos.betweenClosedStream(
                center.offset(-radius, -radius, -radius),
                center.offset(radius, radius, radius)
        ).filter(pos -> pos.distSqr(center) <= radius * radius);
    }

    public static Stream<BlockPos> getBlockPositionsInSphere(AABB box) {
        return BlockPos.betweenClosedStream(box
        ).filter(pos -> pos.distSqr(new BlockPos(box.getCenter())) <= box.getSize() * box.getSize());
    }


    /**
     * Checks if one entity (eyeEntity) can see another (targetToSee), using raycasting and FOV.
     *
     * @param eyeEntity   The entity doing the looking.
     * @param targetToSee The target entity being looked at.
     * @param fovDegrees  Field of view angle in degrees (e.g., 90 means 45 degrees to each side).
     * @return true if visible and within FOV, false otherwise.
     */
    public static boolean canEntitySeeOther(LivingEntity eyeEntity, LivingEntity targetToSee, double fovDegrees) {
        Level level = eyeEntity.level;
        if (level != targetToSee.level) return false;

        Vec3 from = eyeEntity.getEyePosition(1.0F);
        Vec3 to = targetToSee.getEyePosition(1.0F);

        // First, check field of view using dot product
        Vec3 lookVec = eyeEntity.getLookAngle().normalize();
        Vec3 directionToTarget = to.subtract(from).normalize();

        double dot = lookVec.dot(directionToTarget);
        double requiredDot = Math.cos(Math.toRadians(fovDegrees / 2.0));
        if (dot < requiredDot)
            return false; // Outside of FOV

        // Then, raycast from eyeEntity to targetToSee to check if the view is blocked
        HitResult result = level.clip(new ClipContext(
                from, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, eyeEntity
        ));

        // If result is MISS or hit point is very close to target, it's considered visible
        return result.getType() == HitResult.Type.MISS ||
                result.getLocation().distanceToSqr(to) < 1.0;
    }

    /**
     * Verifica se eyeEntity consegue ver targetToSee com base na linha de visão.
     *
     * @param eyeEntity   A entidade que está observando.
     * @param targetToSee A entidade que deve ser visível.
     * @return true se for visível, false se houver obstrução.
     */
    public static boolean canEntitySeeOther(LivingEntity eyeEntity, LivingEntity targetToSee) {
        Level level = eyeEntity.level;
        if (level != targetToSee.level) return false;

        Vec3 from = eyeEntity.getEyePosition(1.0F);
        Vec3 to = targetToSee.getEyePosition(1.0F);

        HitResult result = level.clip(new ClipContext(
                from, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, eyeEntity
        ));

        // Se o hit for MISS (sem blocos no caminho), ou o bloco atingido estiver além da entidade-alvo
        return result.getType() == HitResult.Type.MISS || result.getLocation().distanceToSqr(to) < 1.0;
    }

    public static void renderTextInWorld(PoseStack poseStack, MultiBufferSource bufferSource, String text, double x, double y, double z) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        poseStack.pushPose();

        poseStack.translate(x, y, z);
        poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        poseStack.scale(-0.025F, -0.025F, 0.025F); // escala do texto

        Matrix4f matrix = poseStack.last().pose();
        float backgroundOpacity = mc.options.getBackgroundOpacity(0.25F);
        int backgroundColor = (int) (backgroundOpacity * 255.0F) << 24;

        font.drawInBatch(text, -font.width(text) / 2f, 0, 0xFFFFFF, false, matrix, bufferSource, false, backgroundColor, 15728880);

        poseStack.popPose();
    }

    /// CAREFUL USING THIS
    public static boolean isConnectedToSourceNoLimit(ServerLevel level, BlockPos start, LatexType latexType, Block targetBlock) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> toVisit = new ArrayDeque<>();
        toVisit.add(start);

        while (!toVisit.isEmpty()) {
            BlockPos current = toVisit.poll();
            if (!visited.add(current)) continue;

            BlockState state = level.getBlockState(current);
            if (state.is(targetBlock)) {
                return true;
            }

            if (AbstractLatexBlock.isLatexed(state) && AbstractLatexBlock.getLatexed(state) == latexType) {
                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = current.relative(dir);
                    if (!visited.contains(neighbor)) {
                        toVisit.add(neighbor);
                    }
                }
            }
        }

        return false;
    }

    public static boolean isConnectedToSource(ServerLevel level, BlockPos start, LatexType latexType, Block targetBlock, int maxDepth) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<Pair<BlockPos, Integer>> toVisit = new ArrayDeque<>();
        toVisit.add(Pair.of(start, 0));

        while (!toVisit.isEmpty()) {
            Pair<BlockPos, Integer> entry = toVisit.poll();
            BlockPos current = entry.first;
            int depth = entry.second;

            if (depth > maxDepth) {
                continue;
            }

            if (!visited.add(current)) {
                continue;
            }

            BlockState state = level.getBlockState(current);
            if (state.is(targetBlock)) {
                return true;
            }

            if (AbstractLatexBlock.isLatexed(state) && AbstractLatexBlock.getLatexed(state) == latexType) {
                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = current.relative(dir);
                    toVisit.add(Pair.of(neighbor, depth + 1));
                }
            }
        }

        return false;
    }


    public static void spreadFromSource(ServerLevel level, BlockPos source, int maxDepth) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<Pair<BlockPos, Integer>> queue = new ArrayDeque<>();

        queue.add(Pair.of(source, 0));
        visited.add(source);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            BlockPos pos = current.first;
            int depth = current.second;

            if (depth > maxDepth) continue;

            BlockState state = level.getBlockState(pos);
            if (!AbstractLatexBlock.isLatexed(state)) continue;

            // Simula "crescimento"
            state.randomTick(level, pos, level.getRandom());
            level.levelEvent(1505, pos, 1); // Partículas

            // Adiciona vizinhos se ainda dentro do limite
            if (depth < maxDepth) {
                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = pos.relative(dir);
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(Pair.of(neighbor, depth + 1));
                    }
                }
            }
        }
    }


    public static void repairArmor(LivingEntity entity, int amountPerPiece) {
        for (ItemStack armorPiece : entity.getArmorSlots()) {
            if (!armorPiece.isEmpty() && armorPiece.isDamageableItem()) {
                int damage = armorPiece.getDamageValue();
                if (damage > 0) {
                    int repaired = Math.min(damage, amountPerPiece);
                    armorPiece.setDamageValue(damage - repaired);
                }
            }
        }
    }


    public static ClipContext createClipContext(Player player, double distance, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
        Vec3 start = player.getEyePosition(1.0F);
        Vec3 end = start.add(player.getViewVector(1.0F).scale(distance));
        return new ClipContext(start, end, blockMode, fluidMode, player);
    }


    /**
     * Forked From TAC mod
     * that allows to pass a predicate to ignore certain blocks when checking for collisions.
     *
     * @param world           the world to perform the ray trace
     * @param context         the ray trace context
     * @param ignorePredicate the block state predicate
     * @return a result of the raytrace
     */
    public static BlockHitResult rayTraceBlocks(Level world, ClipContext context, Predicate<BlockState> ignorePredicate) {
        return performRayTrace(context, (rayTraceContext, blockPos) -> {
            BlockState blockState = world.getBlockState(blockPos);
            if (ignorePredicate.test(blockState)) return null;
            FluidState fluidState = world.getFluidState(blockPos);
            Vec3 startVec = rayTraceContext.getFrom();
            Vec3 endVec = rayTraceContext.getTo();
            VoxelShape blockShape = rayTraceContext.getBlockShape(blockState, world, blockPos);
            BlockHitResult blockResult = world.clipWithInteractionOverride(startVec, endVec, blockPos, blockShape, blockState);
            VoxelShape fluidShape = rayTraceContext.getFluidShape(fluidState, world, blockPos);
            BlockHitResult fluidResult = fluidShape.clip(startVec, endVec, blockPos);
            double blockDistance = blockResult == null ? Double.MAX_VALUE : rayTraceContext.getFrom().distanceToSqr(blockResult.getLocation());
            double fluidDistance = fluidResult == null ? Double.MAX_VALUE : rayTraceContext.getFrom().distanceToSqr(fluidResult.getLocation());
            /*if(wallBangPredicate.test(blockState))
            {
                return blockResult;
            }*/
            return blockDistance <= fluidDistance ? blockResult : fluidResult;
        }, (rayTraceContext) -> {
            Vec3 Vector3d = rayTraceContext.getFrom().subtract(rayTraceContext.getTo());
            return BlockHitResult.miss(rayTraceContext.getTo(), Direction.getNearest(Vector3d.x, Vector3d.y, Vector3d.z), new BlockPos(rayTraceContext.getTo()));
        });

        //return new BlockRayTraceMeta(r);
    }

    public static <T> T performRayTrace(ClipContext context, BiFunction<ClipContext, BlockPos, T> hitFunction, Function<ClipContext, T> missFactory) {
        Vec3 startVec = context.getFrom();
        Vec3 endVec = context.getTo();
        if (startVec.equals(endVec)) {
            return missFactory.apply(context);
        } else {
            double startX = Mth.lerp(-0.0000001, endVec.x, startVec.x);
            double startY = Mth.lerp(-0.0000001, endVec.y, startVec.y);
            double startZ = Mth.lerp(-0.0000001, endVec.z, startVec.z);
            double endX = Mth.lerp(-0.0000001, startVec.x, endVec.x);
            double endY = Mth.lerp(-0.0000001, startVec.y, endVec.y);
            double endZ = Mth.lerp(-0.0000001, startVec.z, endVec.z);
            int blockX = Mth.floor(endX);
            int blockY = Mth.floor(endY);
            int blockZ = Mth.floor(endZ);
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(blockX, blockY, blockZ);
            T t = hitFunction.apply(context, mutablePos);
            if (t != null) {
                return t;
            }

            double deltaX = startX - endX;
            double deltaY = startY - endY;
            double deltaZ = startZ - endZ;
            int signX = Mth.sign(deltaX);
            int signY = Mth.sign(deltaY);
            int signZ = Mth.sign(deltaZ);
            double d9 = signX == 0 ? Double.MAX_VALUE : (double) signX / deltaX;
            double d10 = signY == 0 ? Double.MAX_VALUE : (double) signY / deltaY;
            double d11 = signZ == 0 ? Double.MAX_VALUE : (double) signZ / deltaZ;
            double d12 = d9 * (signX > 0 ? 1.0D - Mth.frac(endX) : Mth.frac(endX));
            double d13 = d10 * (signY > 0 ? 1.0D - Mth.frac(endY) : Mth.frac(endY));
            double d14 = d11 * (signZ > 0 ? 1.0D - Mth.frac(endZ) : Mth.frac(endZ));

            while (d12 <= 1.0D || d13 <= 1.0D || d14 <= 1.0D) {
                if (d12 < d13) {
                    if (d12 < d14) {
                        blockX += signX;
                        d12 += d9;
                    } else {
                        blockZ += signZ;
                        d14 += d11;
                    }
                } else if (d13 < d14) {
                    blockY += signY;
                    d13 += d10;
                } else {
                    blockZ += signZ;
                    d14 += d11;
                }

                T t1 = hitFunction.apply(context, mutablePos.set(blockX, blockY, blockZ));
                if (t1 != null) {
                    return t1;
                }
            }

            return missFactory.apply(context);
        }
    }


    public static BlockHitResult manualRaycastIgnoringBlocks(Level level, Entity entity, double maxDistance, Set<Block> ignoredBlocks) {
        Vec3 start = entity.getEyePosition(1.0F);
        Vec3 lookVec = entity.getViewVector(1.0F);
        Vec3 end = start.add(lookVec.scale(maxDistance));

        double stepSize = 0.1;
        Vec3 currentPos = start;
        int steps = (int) (maxDistance / stepSize);

        for (int i = 0; i < steps; i++) {
            BlockPos blockPos = new BlockPos(currentPos);
            BlockState state = level.getBlockState(blockPos);

            if (!ignoredBlocks.contains(state.getBlock()) && state.isSolidRender(level, blockPos)) {
                Direction direction = Direction.getNearest(lookVec.x, lookVec.y, lookVec.z);
                //Vec3 hitPos = applyOffset(currentPos, direction, -0.05D);
                currentPos = applyOffset(currentPos, direction, 0.05D);
                return new BlockHitResult(currentPos, direction, blockPos, true);
            }

            currentPos = currentPos.add(lookVec.scale(stepSize));
        }

        Direction missDirection = Direction.getNearest(lookVec.x, lookVec.y, lookVec.z);
        Vec3 missPos = applyOffset(end, missDirection, -0.05D);
        return BlockHitResult.miss(missPos, missDirection, new BlockPos(end));
    }

    // Utilitário para aplicar deslocamento da face atingida
    public static Vec3 applyOffset(Vec3 hitPos, Direction face, double offset) {
        return hitPos.subtract(
                face.getStepX() * offset,
                face.getStepY() * offset,
                face.getStepZ() * offset
        );
    }

    public static Vec3 getRelativePositionEyes(Entity entity, float deltaX, float deltaY, float deltaZ) {
        // Obtém os vetores locais da entidade
        Vec3 forward = entity.getViewVector(1.0f); // Direção que a entidade está olhando (Surge)
        Vec3 up = entity.getUpVector(1.0F); // Vetor "para cima" da entidade (Heave)
        Vec3 right = forward.cross(up).normalize(); // Calcula o vetor para a direita (Sway)

        // Combina os deslocamentos locais
        Vec3 offset = right.scale(-deltaX) // Sway (esquerda/direita)
                .add(up.scale(deltaY)) // Heave (cima/baixo)
                .add(forward.scale(deltaZ)); // Surge (frente/trás)

        // Retorna a nova posição baseada no deslocamento local
        return entity.getEyePosition().add(offset);
    }

    public static Vec3 getRelativePositionEyes(Entity entity, double deltaX, double deltaY, double deltaZ) {
        // Obtém os vetores locais da entidade
        Vec3 forward = entity.getViewVector(1.0f); // Direção que a entidade está olhando (Surge)
        Vec3 up = entity.getUpVector(1.0F); // Vetor "para cima" da entidade (Heave)
        Vec3 right = forward.cross(up).normalize(); // Calcula o vetor para a direita (Sway)

        // Combina os deslocamentos locais
        Vec3 offset = right.scale(-deltaX) // Sway (esquerda/direita)
                .add(up.scale(deltaY)) // Heave (cima/baixo)
                .add(forward.scale(deltaZ)); // Surge (frente/trás)

        // Retorna a nova posição baseada no deslocamento local
        return entity.getEyePosition().add(offset);
    }

    public static Vec3 getRelativePositionEyes(Entity entity, Vec3 vec3) {
        double deltaX, deltaY, deltaZ;
        deltaX = vec3.x;
        deltaY = vec3.y;
        deltaZ = vec3.z;
        // Obtém os vetores locais da entidade
        Vec3 forward = entity.getViewVector(1.0f); // Direção que a entidade está olhando (Surge)
        Vec3 up = entity.getUpVector(1.0F); // Vetor "para cima" da entidade (Heave)
        Vec3 right = forward.cross(up).normalize(); // Calcula o vetor para a direita (Sway)

        // Combina os deslocamentos locais
        Vec3 offset = right.scale(-deltaX) // Sway (esquerda/direita)
                .add(up.scale(deltaY)) // Heave (cima/baixo)
                .add(forward.scale(deltaZ)); // Surge (frente/trás)

        // Retorna a nova posição baseada no deslocamento local
        return entity.getEyePosition().add(offset);
    }

    public static Vec3 getRelativePositionCommandStyle(Entity entity, double deltaX, double deltaY, double deltaZ) {
        Vec2 rotation = entity.getRotationVector(); // Obtém a rotação (Yaw e Pitch)
        Vec3 position = entity.position(); // Posição atual da entidade

        // Cálculo dos vetores locais
        float yawRad = (rotation.y + 90.0F) * ((float) Math.PI / 180F);
        float pitchRad = -rotation.x * ((float) Math.PI / 180F);
        float pitchRad90 = (-rotation.x + 90.0F) * ((float) Math.PI / 180F);

        float cosYaw = Mth.cos(yawRad);
        float sinYaw = Mth.sin(yawRad);
        float cosPitch = Mth.cos(pitchRad);
        float sinPitch = Mth.sin(pitchRad);
        float cosPitch90 = Mth.cos(pitchRad90);
        float sinPitch90 = Mth.sin(pitchRad90);

        Vec3 forward = new Vec3(cosYaw * cosPitch, sinPitch, sinYaw * cosPitch); // Vetor para frente (Surge)
        Vec3 up = new Vec3(cosYaw * cosPitch90, sinPitch90, sinYaw * cosPitch90); // Vetor para cima (Heave)
        Vec3 right = forward.cross(up).scale(-1.0D); // Vetor para direita (Sway)

        // Calcula nova posição baseada nos deslocamentos locais
        double newX = forward.x * deltaZ + up.x * deltaY + right.x * deltaX;
        double newY = forward.y * deltaZ + up.y * deltaY + right.y * deltaX;
        double newZ = forward.z * deltaZ + up.z * deltaY + right.z * deltaX;

        return new Vec3(position.x + newX, position.y + newY, position.z + newZ);
    }

    public static Vec3 getRelativePosition(Entity entity, double deltaX, double deltaY, double deltaZ, boolean onlyOffset) {
        if (entity == null) {
            return Vec3.ZERO;
        }
        // Obtém os vetores locais da entidade
        Vec3 forward = entity.getViewVector(1.0F); // Direção que a entidade está olhando (Surge)
        Vec3 up = entity.getUpVector(1.0F); // Vetor "para cima" da entidade (Heave)
        Vec3 right = forward.cross(up).normalize(); // Calcula o vetor para a direita (Sway)

        // Combina os deslocamentos locais
        Vec3 offset = right.scale(deltaX) // Sway (esquerda/direita)
                .add(up.scale(deltaY)) // Heave (cima/baixo)
                .add(forward.scale(deltaZ)); // Surge (frente/trás)

        if (onlyOffset) {
            return offset;
        }
        // Retorna a nova posição baseada no deslocamento local
        return entity.position().add(offset);
    }

    public static double getTorsoYOffset(ChangedEntity self) {
        float ageAdjusted = (float) self.tickCount * 0.33333334F * 0.25F * 0.15F;
        float ageSin = Mth.sin(ageAdjusted * 3.1415927F * 0.5F);
        float ageCos = Mth.cos(ageAdjusted * 3.1415927F * 0.5F);
        float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;
        return (double) (Mth.lerp(Mth.lerp(1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F), ageSin * ageSin * ageSin * ageSin, 1.0F - ageCos * ageCos * ageCos * ageCos), 0.95F, 0.87F) + bpiSize);
    }

    public static double getTorsoYOffset(ChangedEntity self, float scale) {
        float ageAdjusted = (float) self.tickCount * 0.33333334F * 0.25F * 0.15F;
        float ageSin = Mth.sin(ageAdjusted * (float) Math.PI * 0.5F);
        float ageCos = Mth.cos(ageAdjusted * (float) Math.PI * 0.5F);
        float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;

        float baseOscillation = Mth.lerp(
                Mth.lerp(
                        1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F),
                        ageSin * ageSin * ageSin * ageSin,
                        1.0F - ageCos * ageCos * ageCos * ageCos
                ),
                0.95F, 0.87F
        );

        return baseOscillation * scale + bpiSize;
    }


}
