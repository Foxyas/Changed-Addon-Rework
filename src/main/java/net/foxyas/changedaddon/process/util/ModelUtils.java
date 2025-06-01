package net.foxyas.changedaddon.process.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.*;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility class for working with model parts and transformations in entity models.
 */
public class ModelUtils {
    /**
     * Calculates the world position from a model part using a custom transform approach with rotation mirroring support.
     *
     * @param part                 The model part to use.
     * @param Offset               offset relative to the model part.
     * @param entity               The entity used for base position and orientation.
     * @param entityPosOffset      offset relative to the entity position part.
     * @param Rotation             Rotation vector (in degrees).
     * @param affectEntityViewXrot Whether to apply the entity's X rotation to the transformation.
     * @return The resulting world-space position as a {@link Vec3}.
     */

    public static Vec3 getWorldPositionFromModelPart(ModelPart part, Vector3f Offset, @NotNull Entity entity, @Nullable Vec3 entityPosOffset, @Nullable Vec3 Rotation, boolean affectEntityViewXrot) {
        // if using a player arms the best offset positions is Rotation.x() = 180, entityPosOffset.y = 0.45 if sneaking and -0.2 if not
        // Also Remember to make some offset checks for positions ofc
        if (Rotation == null) {
            Rotation = new Vec3(180, 0, 0);
        }

        if (entityPosOffset == null) {
            entityPosOffset = new Vec3(0, 0.2d, 0);
        }

        PoseStack stack = new PoseStack();

        // Aplica as transformações da entidade (opcional)
        stack.translate(entity.getX() + entityPosOffset.x(), entity.getEyeY() + entityPosOffset.y() + (entity instanceof Player player && player.isShiftKeyDown() ? 0.225d : 0d), entity.getZ() + entityPosOffset.z());
        stack.mulPose(Vector3f.YP.rotationDegrees(entity instanceof Player player ? -player.yBodyRotO : -entity.yRotO));
        if (affectEntityViewXrot) {
            stack.mulPose(Vector3f.XP.rotationDegrees(entity.getXRot()));
        }

        // Aplica as transformações do ModelPart
        part.xRot *= 1;
        part.yRot *= 1;
        part.zRot *= 1;
        stack.mulPose(Vector3f.YP.rotationDegrees((float) Rotation.y()));
        stack.mulPose(Vector3f.XP.rotationDegrees((float) Rotation.x()));
        stack.mulPose(Vector3f.ZP.rotationDegrees((float) Rotation.z()));
        part.translateAndRotate(stack);


        // Aplica o offset local
        Matrix4f matrix = stack.last().pose();
        Vector4f pos = new Vector4f(Offset.x(), Offset.y(), Offset.z(), 1.0F);
        pos.transform(matrix);

        return new Vec3(pos.x(), pos.y(), pos.z());
    }

    public static List<ModelPart> getTailFromModelIfAny(AdvancedHumanoidModel<?> model) {
        List<ModelPart> tailParts = new ArrayList<>();

        try {
            ModelPart tail = model.getTorso().getChild("Tail");
            tailParts.add(tail);

            ModelPart primary = tail.getChild("TailPrimary");
            tailParts.add(primary);

            ModelPart secondary = primary.getChild("TailSecondary");
            tailParts.add(secondary);

            ModelPart tertiary = secondary.getChild("TailTertiary");
            tailParts.add(tertiary);

            ModelPart quaternary = tertiary.getChild("TailQuaternary");
            tailParts.add(quaternary);
        } catch (Exception ignored) {
            // Se qualquer etapa falhar, retorna as partes válidas até o erro
        }

        return tailParts;
    }


    /**
     * Calculates the world position from a model part using a custom transform approach with rotation mirroring support.
     *
     * @param part                 The model part to use.
     * @param Offset               offset relative to the model part.
     * @param vec3                 variable used on the Matrix4f
     * @param entity               The entity used for base position and orientation.
     * @param entityPosOffset      offset relative to the entity position part.
     * @param Rotation             Rotation vector (in degrees).
     * @param ModelPoseStack       Last PoseStack From Model [Can Be Null if None]
     * @param affectEntityViewXrot Whether to apply the entity's X rotation to the transformation.
     * @return The resulting world-space position as a {@link Vec3}.
     */

    public static Vec3 getWorldSpaceFromModelPart(ModelPart part, Vector3f Offset, Vector3f vec3,
                                                  @NotNull Entity entity, @Nullable Vec3 entityPosOffset,
                                                  @Nullable Vec3 Rotation, @Nullable PoseStack ModelPoseStack,
                                                  boolean affectEntityViewXrot) {
        if (Rotation == null) {
            Rotation = new Vec3(180, 0, 0);
        }

        if (entityPosOffset == null) {
            entityPosOffset = new Vec3(0, -0.3, 0);
        }

        PoseStack stack = new PoseStack();

        // Aplica transformações da entidade
        stack.translate(
                entity.getX() + entityPosOffset.x(),
                entity.getEyeY() + entityPosOffset.y() + (entity instanceof Player player && player.isShiftKeyDown() ? 0.225d : 0d),
                entity.getZ() + entityPosOffset.z()
        );
        stack.mulPose(Vector3f.YP.rotationDegrees(entity instanceof LivingEntity livingEntity ? -livingEntity.yBodyRotO : -entity.yRotO));
        if (affectEntityViewXrot) {
            stack.mulPose(Vector3f.XP.rotationDegrees(entity.getXRot()));
        }

        // Aplica rotações personalizadas
        stack.mulPose(Vector3f.YP.rotationDegrees((float) Rotation.y()));
        stack.mulPose(Vector3f.XP.rotationDegrees((float) Rotation.x()));
        stack.mulPose(Vector3f.ZP.rotationDegrees((float) Rotation.z()));

        // Aplica transformações da ModelPart
        part.translateAndRotate(stack);

        // Aplica offset local
        stack.translate(Offset.x(), Offset.y(), Offset.z());

        if (ModelPoseStack != null) {
            stack.mulPoseMatrix(ModelPoseStack.last().pose());
        }

        // Converte posição final
        Matrix4f matrix = stack.last().pose();
        Vector4f pos = new Vector4f(vec3.x(), vec3.y(), vec3.z(), 1.0F);
        pos.transform(matrix);

        return new Vec3(pos.x(), pos.y(), pos.z());
    }


    /**
     * Calculates the world position from a model part using a custom transform approach with rotation mirroring support.
     *
     * @param part                 The model part to use.
     * @param Offset               offset relative to the model part.
     * @param vec3                 variable used on the Matrix4f
     * @param entity               The entity used for base position and orientation.
     * @param entityPosOffset      offset relative to the entity position part.
     * @param Rotation             Rotation vector (in degrees).
     * @param affectEntityViewXrot Whether to apply the entity's X rotation to the transformation.
     * @return The resulting world-space position as a {@link Vec3}.
     */

    public static Vec3 getWorldSpaceFromModelPart(ModelPart part, Vector3f Offset, Vector3f vec3,
                                                  @NotNull Entity entity, @Nullable Vec3 entityPosOffset,
                                                  @Nullable Vec3 Rotation,
                                                  boolean affectEntityViewXrot) {
        return getWorldSpaceFromModelPart(part, Offset, vec3, entity, entityPosOffset, Rotation, null, affectEntityViewXrot);
    }


    public static AABB getModelPartBounds(ModelPart part) {
        ModelPart.Cube partCube = part.getRandomCube(new Random());
        return new AABB(partCube.minX, partCube.minY, partCube.minZ, partCube.maxX, partCube.maxY, partCube.maxZ);
    }

    public static AABB getModelPartBounds(ModelPart part, ModelPart.Cube partCube) {
        return new AABB(partCube.minX, partCube.minY, partCube.minZ, partCube.maxX, partCube.maxY, partCube.maxZ);
    }


    public static Vec3 getPartWorldPosForParticles(Entity entity, ModelPart part, float partialTicks) {
        // Interpola posição da entidade
        double x = Mth.lerp(partialTicks, entity.xOld, entity.getX());
        double y = Mth.lerp(partialTicks, entity.yOld, entity.getEyeY());
        double z = Mth.lerp(partialTicks, entity.zOld, entity.getZ());

        // Interpola rotação da entidade (apenas yaw, já que é o corpo girando horizontalmente)
        float bodyYawRad = (float) Math.toRadians(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()));

        // Posição local da parte (convertida para escala real)
        Vec3 local = new Vec3(part.x, part.y, part.z).scale(0.0625f);

        // Aplica rotações do ModelPart (pitch, yaw, roll) — ordem Z (roll), Y (yaw), X (pitch)
        local = local.zRot(part.zRot);
        local = local.yRot(part.yRot);
        local = local.xRot(part.xRot);
        local = local.xRot((float) Math.toRadians(0)); //change 0 to something

        // Aplica rotação da entidade (corpo)
        local = local.yRot(-bodyYawRad); // negativa porque é coordenada local -> world

        // Soma com posição da entidade
        return new Vec3(x + local.x, y + local.y, z + local.z);
    }


    /**
     * Applies the inverse rotation of a {@link ModelPart} to the given {@link PoseStack}, effectively mirroring the part.
     *
     * @param part  The model part whose rotation should be mirrored.
     * @param stack The pose stack to apply transformations to.
     */

    public static void applyMirroredRotation(ModelPart part, PoseStack stack) {
        stack.mulPose(Vector3f.ZP.rotation(-part.zRot));
        stack.mulPose(Vector3f.YP.rotation(-part.yRot));
        stack.mulPose(Vector3f.XP.rotation(-part.xRot));
    }


    /**
     * Applies a Y-axis rotation to a vector.
     *
     * @param localPos The local position to rotate.
     * @param rotation Rotation in degrees around the Y axis.
     * @return The rotated vector as a {@link Vec3}.
     */
    private static Vec3 applyRotation(Vec3 localPos, float rotation) {
        // Aqui você pode aplicar uma rotação simples, dependendo da sua necessidade
        // Exemplo de rotação no eixo Y
        double rad = Math.toRadians(rotation);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        double x = localPos.x * cos - localPos.z * sin;
        double z = localPos.x * sin + localPos.z * cos;

        return new Vec3(x, localPos.y, z);
    }

    /**
     * Applies rotation to a vector using quaternion transformation based on Euler angles.
     *
     * @param local The original vector.
     * @param xRot  Rotation around the X axis in radians.
     * @param yRot  Rotation around the Y axis in radians.
     * @param zRot  Rotation around the Z axis in radians.
     * @return The rotated vector as a {@link Vec3}.
     */

    public static Vec3 applyRotationToVec3(Vec3 local, float xRot, float yRot, float zRot) {
        if (xRot == 0 && yRot == 0 && zRot == 0) {
            return local;
        }
        Quaternion q = Quaternion.ONE.copy(); // Identidade

        if (zRot != 0.0F) {
            q.mul(Vector3f.ZP.rotationDegrees(zRot));
        }
        if (yRot != 0.0F) {
            q.mul(Vector3f.YP.rotationDegrees(yRot));
        }
        if (xRot != 0.0F) {
            q.mul(Vector3f.XP.rotationDegrees(xRot));
        }
        Vector3f localVector = new Vector3f((float) local.x, (float) local.y, (float) local.z);
        localVector.transform(q);
        return new Vec3(localVector.x(), localVector.y(), localVector.z());
    }

    /**
     * Attempts to retrieve the EntityModel used to render the given entity.
     * This only works on the client side and only for LivingEntities with renderers.
     *
     * @param entity The entity to get the model for.
     * @return The EntityModel, or null if not available or not a LivingEntity.
     */
    @Nullable
    public static EntityModel<?> getModelOf(Entity entity) {
        if (!(entity instanceof LivingEntity living)) return null;

        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (!(dispatcher.getRenderer(living) instanceof LivingEntityRenderer<?, ?> livingRenderer)) return null;

        return livingRenderer.getModel();
    }

    /*
    public static Vec3 getViewVectorOLD(ModelPart part) {
        Quaternion rotation = getRotationQuaternion(part);
        Vector3f forward = new Vector3f(0, 0, 1); // Z é "para frente"
        forward.transform(rotation);
        return new Vec3(forward.x(), forward.y(), forward.z());
    }

    public static Vec3 getUpVectorOLD(ModelPart part) {
        Quaternion rotation = getRotationQuaternion(part);
        Vector3f up = new Vector3f(0, 1, 0); // Y é "para cima"
        up.transform(rotation);
        return new Vec3(up.x(), up.y(), up.z());
    }

    public static float getPointViewXRot(ModelPart part) {
        return part.xRot;
    }

    public static float getPointViewYRot(ModelPart part) {
        return part.yRot;
    }

    public static Vec3 getPointViewVector(ModelPart part) {
        return calculatePointViewVector(getPointViewXRot(part), getPointViewYRot(part));
    }

    public static Vec3 calculatePointViewVector(float p_20172_, float p_20173_) {
        float f = p_20172_ * ((float)Math.PI / 180F);
        float f1 = -p_20173_ * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }
    public static Vec3 getPointUpVector(ModelPart part) {
        return calculatePointViewUpVector(getPointViewXRot(part), getPointViewYRot(part));
    }

    public static Vec3 calculatePointViewUpVector(float p_20215_, float p_20216_) {
        return calculatePointViewVector(p_20215_ - 90.0F, p_20216_);
    }


    public static Quaternion getRotationQuaternion(ModelPart part) {
        // Cria o quaternário baseado nas rotações em ordem ZYX
        Quaternion q = Quaternion.ONE.copy(); // identidade
        if (part.zRot != 0.0F) {
            q.mul(Vector3f.ZP.rotation(part.zRot));
        }
        if (part.yRot != 0.0F) {
            q.mul(Vector3f.YP.rotation(part.yRot));
        }
        if (part.xRot != 0.0F) {
            q.mul(Vector3f.XP.rotation(part.xRot));
        }
        return q;
    }

    public static Vec3 getRelativePositionModel(Entity entity, ModelPart part, double deltaX, double deltaY, double deltaZ) {
        if (entity == null) {
            return Vec3.ZERO;
        }

        // Obtém os vetores locais da entidade
        Vec3 forward = getPointViewVector(part); // Direção que a entidade está olhando (Surge)
        Vec3 up = getPointUpVector(part); // Vetor "para cima" da entidade (Heave)
        Vec3 right = forward.cross(up).normalize(); // Calcula o vetor para a direita (Sway)

        // Combina os deslocamentos locais
        Vec3 offset = right.scale(deltaX) // Sway (esquerda/direita)
                .add(up.scale(deltaY)) // Heave (cima/baixo)
                .add(forward.scale(deltaZ)); // Surge (frente/trás)

        // Aplica rotação do corpo da entidade (yaw global)
        float yawRad = entity.getYRot() * ((float) Math.PI / 180F); // Inverso porque o Minecraft gira para esquerda com yaw positivo
        double cos = Math.cos(yawRad);
        double sin = Math.sin(yawRad);

        double x = offset.x * cos - offset.z * sin;
        double z = offset.x * sin + offset.z * cos;

        Vec3 rotatedOffset = new Vec3(x, offset.y, z);

        // Retorna a nova posição baseada no deslocamento local
        //offset = applyRotationToVec3(offset, HeadPosT, HeadPosV, HeadPosB);
        HeadPosK = part.x / 16;
        HeadPosL = part.y / 16;
        HeadPosJ = part.z / 16;
        return entity.position().add(HeadPosT, HeadPosV, HeadPosB).add(rotatedOffset);
    }*/


}
