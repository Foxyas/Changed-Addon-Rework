package net.foxyas.changedaddon.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.joml.Matrix4f;

import java.util.*;
import java.util.stream.Stream;

import static net.foxyas.changedaddon.util.DynamicClipContext.IGNORE_TRANSLUCENT;

public class FoxyasUtils {

    public static void sendPlayerLikeChat(Component chatComponent, LivingEntity talker, ServerPlayer player, boolean filtered) {
        OutgoingChatMessage chatMessage = new OutgoingChatMessage.Disguised(chatComponent);
        ChatType.Bound bound = ChatType.bind(ChatType.CHAT, talker);
        player.sendChatMessage(chatMessage, filtered, bound);
    }


    public static Stream<BlockPos> getBlockPositionsInSphere(BlockPos center, int radius) {
        return BlockPos.betweenClosedStream(
                center.offset(-radius, -radius, -radius),
                center.offset(radius, radius, radius)
        ).filter(pos -> pos.distSqr(center) <= radius * radius);
    }


    public static Stream<BlockPos> betweenClosedStreamSphere(BlockPos center, int horizontalRadiusSphere, int verticalRadiusSphere) {
        List<BlockPos> blockPosList = new ArrayList<>();
        for (int i = -verticalRadiusSphere; i <= verticalRadiusSphere; i++) {
            for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                    double distanceSq = (xi * xi) / (double) (horizontalRadiusSphere * horizontalRadiusSphere) + (i * i) / (double) (verticalRadiusSphere * verticalRadiusSphere)
                            + (zi * zi) / (double) (horizontalRadiusSphere * horizontalRadiusSphere);
                    if (distanceSq <= 1.0) {
                        BlockPos pos = center.offset(xi, i, zi);
                        blockPosList.add(pos);
                    }
                }
            }
        }

        return blockPosList.stream();
    }

    public static Stream<BlockPos> betweenClosedStreamSphere(BlockPos center, int horizontalRadiusSphere, int verticalRadiusSphere, float troubleshot) {
        List<BlockPos> blockPosList = new ArrayList<>();
        for (int i = -verticalRadiusSphere; i <= verticalRadiusSphere; i++) {
            for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                    double distanceSq = (xi * xi) / (double) (horizontalRadiusSphere * horizontalRadiusSphere) + (i * i) / (double) (verticalRadiusSphere * verticalRadiusSphere)
                            + (zi * zi) / (double) (horizontalRadiusSphere * horizontalRadiusSphere);
                    if (distanceSq <= troubleshot) {
                        BlockPos pos = center.offset(xi, i, zi);
                        blockPosList.add(pos);
                    }
                }
            }
        }

        return blockPosList.stream();
    }

    public static List<BlockPos> getOutlineBlocks(BoundingBox box) {
        List<BlockPos> outline = new ArrayList<>();

        for (int x = box.minX(); x <= box.maxX(); x++) {
            for (int y = box.minY(); y <= box.maxY(); y++) {
                for (int z = box.minZ(); z <= box.maxZ(); z++) {
                    boolean onXFace = x == box.minX() || x == box.maxX();
                    boolean onYFace = y == box.minY() || y == box.maxY();
                    boolean onZFace = z == box.minZ() || z == box.maxZ();

                    // Se estiver em pelo menos uma face, adiciona ao outline
                    if (onXFace || onYFace || onZFace) {
                        outline.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

        return outline;
    }

    public static Stream<BlockPos> betweenClosedStreamSphereOutline(BlockPos center, int horizontalRadius, int verticalRadius) {
        List<BlockPos> outline = new ArrayList<>();
        double tolerance = 0.25; // ajusta a espessura da borda

        for (int y = -verticalRadius; y <= verticalRadius; y++) {
            for (int x = -horizontalRadius; x <= horizontalRadius; x++) {
                for (int z = -horizontalRadius; z <= horizontalRadius; z++) {
                    double dx = x / (double) horizontalRadius;
                    double dy = y / (double) verticalRadius;
                    double dz = z / (double) horizontalRadius;

                    double distanceSq = dx * dx + dy * dy + dz * dz;

                    if (distanceSq >= 1.0 - tolerance && distanceSq <= 1.0) {
                        outline.add(center.offset(x, y, z));
                    }
                }
            }
        }

        return outline.stream();
    }


    public static Stream<BlockPos> betweenClosedStreamSphereOutline(BlockPos center, int horizontalRadius, int verticalRadius, double borderDistance) {
        List<BlockPos> outline = new ArrayList<>();
        double tolerance = 0.25; // ajusta a espessura da borda

        for (int y = -verticalRadius; y <= verticalRadius; y++) {
            for (int x = -horizontalRadius; x <= horizontalRadius; x++) {
                for (int z = -horizontalRadius; z <= horizontalRadius; z++) {
                    double dx = x / (double) horizontalRadius;
                    double dy = y / (double) verticalRadius;
                    double dz = z / (double) horizontalRadius;

                    double distanceSq = dx * dx + dy * dy + dz * dz;

                    if (distanceSq >= borderDistance - tolerance && distanceSq <= borderDistance) {
                        outline.add(center.offset(x, y, z));
                    }
                }
            }
        }

        return outline.stream();
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
     * Checks if one entity (eyeEntity) can see another (targetToSee), using raycasting and FOV.
     *
     * @param eyeEntity  The entity doing the looking.
     * @param to         The target pos to be looked at.
     * @param fovDegrees Field of view angle in degrees (e.g., 90 means 45 degrees to each side).
     * @return true if visible and within FOV, false otherwise.
     */
    public static boolean canEntitySeePos(LivingEntity eyeEntity, Vec3 to, double fovDegrees) {
        Level level = eyeEntity.level;
        Vec3 from = eyeEntity.getEyePosition(1.0F);

        // First, check field of view using dot product
        Vec3 lookVec = eyeEntity.getLookAngle().normalize();
        Vec3 directionToTarget = to.subtract(from).normalize();

        double dot = lookVec.dot(directionToTarget);
        double requiredDot = Math.cos(Math.toRadians(fovDegrees / 2.0));
        if (dot < requiredDot)
            return false; // Outside of FOV

        // Then, raycast from eyeEntity to targetToSee to check if the view is blocked
        HitResult result = level.clip(new ClipContext(
                from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, eyeEntity
        ));

        // If result is MISS or hit point is very close to target, it's considered visible
        return result.getType() == HitResult.Type.MISS ||
                result.getLocation().distanceToSqr(to) < 1.0;
    }


    /**
     * Checks if one entity (eyeEntity) can see another (targetToSee), using raycasting and FOV.
     *
     * @param eyeEntity  The entity doing the looking.
     * @param to         The target pos to be looked at.
     * @param fovDegrees Field of view angle in degrees (e.g., 90 means 45 degrees to each side).
     * @return true if visible and within FOV, false otherwise.
     */
    public static boolean canEntitySeePosIgnoreGlass(LivingEntity eyeEntity, Vec3 to, double fovDegrees) {
        Level level = eyeEntity.level;
        Vec3 from = eyeEntity.getEyePosition(1.0F);

        // First, check field of view using dot product
        Vec3 lookVec = eyeEntity.getLookAngle().normalize();
        Vec3 directionToTarget = to.subtract(from).normalize();

        double dot = lookVec.dot(directionToTarget);
        double requiredDot = Math.cos(Math.toRadians(fovDegrees / 2.0));
        if (dot < requiredDot)
            return false; // Outside of FOV

        // Then, raycast from eyeEntity to targetToSee to check if the view is blocked
        HitResult result = level.clip(new DynamicClipContext(from, to,
                IGNORE_TRANSLUCENT, ClipContext.Fluid.NONE::canPick, CollisionContext.of(eyeEntity))
        );

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
    public static boolean canEntitySeeOther(Entity eyeEntity, Entity targetToSee) {
        Level level = eyeEntity.level;
        if (level != targetToSee.level) return false;

        Vec3 from = eyeEntity.getEyePosition(1.0F);
        Vec3 to = targetToSee.getEyePosition(1.0F);

        HitResult result = level.clip(new ClipContext(
                from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, eyeEntity
        ));

        // Se o hit for MISS (sem blocos no caminho), ou o bloco atingido estiver além da entidade-alvo
        return result.getType() == HitResult.Type.MISS || result.getLocation().distanceToSqr(to) < 1.0;
    }

    /**
     * Verifica se eyeEntity consegue ver targetToSee com base na linha de visão.
     *
     * @param eyeEntity   A entidade que está observando.
     * @param targetToSee A entidade que deve ser visível.
     * @return true se for visível, false se houver obstrução.
     */
    public static boolean canEntitySeeOtherIgnoreGlass(Entity eyeEntity, Entity targetToSee) {
        Level level = eyeEntity.level;
        if (level != targetToSee.level) return false;

        Vec3 from = eyeEntity.getEyePosition(1.0F);
        Vec3 to = targetToSee.getEyePosition(1.0F);

        HitResult result = level.clip(new DynamicClipContext(from, to,
                IGNORE_TRANSLUCENT, ClipContext.Fluid.NONE::canPick, CollisionContext.of(eyeEntity))
        );

        // Se o hit for MISS (sem blocos no caminho), ou o bloco atingido estiver além da entidade-alvo
        return result.getType() == HitResult.Type.MISS || result.getLocation().distanceToSqr(to) < 1.0;
    }


    /**
     * Checks if one entity (eyeEntity) can see another (targetToSee), using raycasting and FOV.
     *
     * @param eyeEntity   The entity doing the looking.
     * @param targetToSee The target entity being looked at.
     * @param fovDegrees  Field of view angle in degrees (e.g., 90 means 45 degrees to each side).
     * @return true if visible and within FOV, false otherwise.
     */
    public static boolean canEntitySeeOtherIgnoreGlass(Entity eyeEntity, Entity targetToSee, double fovDegrees) {
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
            return false; // Outside FOV

        // Then, raycast from eyeEntity to targetToSee to check if the view is blocked
        HitResult result = level.clip(new DynamicClipContext(from, to,
                IGNORE_TRANSLUCENT, ClipContext.Fluid.NONE::canPick, CollisionContext.of(eyeEntity))
        );

        // If result is MISS or hit point is very close to target, it's considered visible
        return result.getType() == HitResult.Type.MISS ||
                result.getLocation().distanceToSqr(to) < 1.0;
    }

    /**
     * Verifica se eyeEntity consegue ver targetToSee com base na linha de visão.
     *
     * @param eyeEntity A entidade que está observando.
     * @param to        Target position
     * @return true se for visível, false se houver obstrução.
     */
    public static boolean canEntitySeePosIgnoreGlass(Entity eyeEntity, Vec3 to) {
        Level level = eyeEntity.level;

        Vec3 from = eyeEntity.getEyePosition(1.0F);

        HitResult result = level.clip(new DynamicClipContext(from, to,
                IGNORE_TRANSLUCENT, ClipContext.Fluid.NONE::canPick, CollisionContext.of(eyeEntity))
        );

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
        font.drawInBatch(text, -font.width(text) / 2f, 0, 0xFFFFFF, false, matrix, bufferSource, Font.DisplayMode.NORMAL, backgroundColor, 15728880);

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

            if (LatexCoverState.getAt(level, current).getType() == latexType) {
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
            BlockPos current = entry.getFirst();
            int depth = entry.getSecond();

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

            if (LatexCoverState.getAt(level, current).getType() == latexType) {
                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = current.relative(dir);
                    toVisit.add(Pair.of(neighbor, depth + 1));
                }
            }
        }

        return false;
    }

    public static boolean isConnectedToSource(ServerLevel level, BlockPos start, Block targetBlock, int maxDepth) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<Pair<BlockPos, Integer>> toVisit = new ArrayDeque<>();
        toVisit.add(Pair.of(start, 0));

        while (!toVisit.isEmpty()) {
            Pair<BlockPos, Integer> entry = toVisit.poll();
            BlockPos current = entry.getFirst();
            int depth = entry.getSecond();

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

            for (Direction dir : Direction.values()) {
                BlockPos neighbor = current.relative(dir);
                toVisit.add(Pair.of(neighbor, depth + 1));
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
            BlockPos pos = current.getFirst();
            int depth = current.getSecond();

            if (depth > maxDepth) continue;

            BlockState state = level.getBlockState(pos);
            if (!LatexCoverState.getAt(level, pos).isAir()) continue;

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

    public static boolean isFluidConnectedTo(Level level, BlockPos start, BlockPos target, int maxSearch) {
        FluidState startFluid = level.getFluidState(start);
        FluidState targetFluid = level.getFluidState(target);

        if (startFluid.isEmpty() || targetFluid.isEmpty()) return false;
        if (!startFluid.getType().isSame(targetFluid.getType())) return false;

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        queue.add(start);
        visited.add(start);

        int searched = 0;

        while (!queue.isEmpty() && searched < maxSearch) {
            BlockPos current = queue.poll();
            searched++;

            if (current.equals(target)) {
                return true;
            }

            for (Direction dir : Direction.values()) {
                BlockPos next = current.relative(dir);

                if (!visited.contains(next)) {
                    FluidState fluid = level.getFluidState(next);

                    if (!fluid.isEmpty() && fluid.getType().isSame(startFluid.getType())) {
                        visited.add(next);
                        queue.add(next);
                    }
                }
            }
        }

        return false;
    }

    public static Set<BlockPos> getConnectedFluids(Level level, BlockPos start, int maxSearch) {
        FluidState startFluid = level.getFluidState(start);

        if (startFluid.isEmpty()) return Collections.emptySet();

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        queue.add(start);
        visited.add(start);

        int searched = 0;

        while (!queue.isEmpty() && searched < maxSearch) {
            BlockPos current = queue.poll();
            searched++;

            for (Direction dir : Direction.values()) {
                BlockPos next = current.relative(dir);

                if (!visited.contains(next)) {
                    FluidState fluid = level.getFluidState(next);

                    if (!fluid.isEmpty() && fluid.getType().isSame(startFluid.getType())) {
                        visited.add(next);
                        queue.add(next);
                    }
                }
            }
        }

        return visited;
    }

    public static boolean isBlockConnectedTo(Level level, BlockPos start, BlockPos target, int maxSearch) {
        BlockState startState = level.getBlockState(start);
        BlockState targetState = level.getBlockState(target);

        if (!startState.is(targetState.getBlock())) return false;

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        queue.add(start);
        visited.add(start);

        int searched = 0;

        while (!queue.isEmpty() && searched < maxSearch) {
            BlockPos current = queue.poll();
            searched++;

            if (current.equals(target)) {
                return true;
            }

            for (Direction dir : Direction.values()) {
                BlockPos next = current.relative(dir);

                if (!visited.contains(next)) {
                    BlockState state = level.getBlockState(next);

                    if (state.is(startState.getBlock())) {
                        visited.add(next);
                        queue.add(next);
                    }
                }
            }
        }

        return false;
    }

    public static Set<BlockPos> getConnectedBlocks(Level level, BlockPos start, int maxSearch) {
        BlockState startState = level.getBlockState(start);

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        queue.add(start);
        visited.add(start);

        int searched = 0;

        while (!queue.isEmpty() && searched < maxSearch) {
            BlockPos current = queue.poll();
            searched++;

            for (Direction dir : Direction.values()) {
                BlockPos next = current.relative(dir);

                if (!visited.contains(next)) {
                    BlockState state = level.getBlockState(next);

                    if (state.is(startState.getBlock())) {
                        visited.add(next);
                        queue.add(next);
                    }
                }
            }
        }

        return visited;
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

    public static void repairHandItem(LivingEntity entity, int amountPerPiece) {
        for (ItemStack handItem : entity.getHandSlots()) {
            if (!handItem.isEmpty() && handItem.isDamageableItem()) {
                int damage = handItem.getDamageValue();
                if (damage > 0) {
                    int repaired = Math.min(damage, amountPerPiece);
                    handItem.setDamageValue(damage - repaired);
                }
            }
        }
    }

    public static void repairAllItems(LivingEntity entity, int amountPerPiece) {
        repairArmor(entity, amountPerPiece);
        repairHandItem(entity, amountPerPiece);
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
        float bpiSize = (self.getBasicPlayerInfo().getSize(self) - 1.0F) * 2.0F;
        return Mth.lerp(Mth.lerp(1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F), ageSin * ageSin * ageSin * ageSin, 1.0F - ageCos * ageCos * ageCos * ageCos), 0.95F, 0.87F) + bpiSize;
    }

    public static double getTorsoYOffset(ChangedEntity self, float scale) {
        float ageAdjusted = (float) self.tickCount * 0.33333334F * 0.25F * 0.15F;
        float ageSin = Mth.sin(ageAdjusted * (float) Math.PI * 0.5F);
        float ageCos = Mth.cos(ageAdjusted * (float) Math.PI * 0.5F);
        float bpiSize = (self.getBasicPlayerInfo().getSize(self) - 1.0F) * 2.0F;

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
