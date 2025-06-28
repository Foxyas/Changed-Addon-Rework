package net.foxyas.changedaddon.process.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;

public class FoxyasUtils {

    public static List<Holder<EntityType<?>>> getEntitiesInTag(TagKey<EntityType<?>> tagKey, Level level) {
        return level.registryAccess()
                .registry(Registries.ENTITY_TYPE)
                .flatMap(reg -> reg.getTag(tagKey))
                .map(tag -> tag.stream().toList())
                .orElse(List.of());
    }

    public static void drawCenteredString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color) {
        guiGraphics.drawString(font, text, x - font.width(text) / 2, y, color);
    }

    public static void drawCenteredString(GuiGraphics guiGraphics, Font font, String text, int x, int y, int color) {
        guiGraphics.drawString(font, text, x - font.width(text) / 2, y, color);
    }

    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }


    public static double StringToDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
        }
        return 0;
    }

    public static float StringToFloat(String s) {
        try {
            return Float.parseFloat(s.trim());
        } catch (Exception e) {
        }
        return 0;
    }

    public static int StringToInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
        }
        return 0;
    }


    public static BlockHitResult manualRaycastIgnoringBlocks(Level level, Entity entity, double maxDistance, Set<Block> ignoredBlocks) {
        Vec3 start = entity.getEyePosition(1.0F);
        Vec3 lookVec = entity.getViewVector(1.0F);
        Vec3 end = start.add(lookVec.scale(maxDistance));

        double stepSize = 0.1;
        Vec3 currentPos = start;
        int steps = (int) (maxDistance / stepSize);

        for (int i = 0; i < steps; i++) {
            BlockPos blockPos = new BlockPos((int) currentPos.x, (int) currentPos.y, (int) currentPos.z);
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
        return BlockHitResult.miss(missPos, missDirection, new BlockPos((int) end.x, (int) end.y, (int) end.z));
    }

    // Utilitário para aplicar deslocamento da face atingida
    public static Vec3 applyOffset(Vec3 hitPos, Direction face, double offset) {
        return hitPos.subtract(
                face.getStepX() * offset,
                face.getStepY() * offset,
                face.getStepZ() * offset
        );
    }

    public static void grandPlayerAdvancement(Player player, String AdvancementId) {
        if (player instanceof ServerPlayer _player) {
            Advancement _adv = _player.server.getAdvancements().getAdvancement(ResourceLocation.parse(AdvancementId));
            assert _adv != null;
            if (_player.getAdvancements().getOrStartProgress(_adv).isDone()) {
                return;
            }
            AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
            if (!_ap.isDone()) {
                for (String string : _ap.getRemainingCriteria()) {
                    _player.getAdvancements().award(_adv, string);
                }
            }
        }
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

}
