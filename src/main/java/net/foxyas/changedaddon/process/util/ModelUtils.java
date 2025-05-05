package net.foxyas.changedaddon.process.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * Utility class for working with model parts and transformations in entity models.
 */
public class ModelUtils {
    public static Vec3 getModelPartWorldPosition(ModelPart part, Entity entity, Vec3 offset) {
        // Posição local (pode ser modificada dependendo de onde você quer o ponto no ModelPart)

        // Obtém as transformações do ModelPart (posição e rotação)
        // A rotação e a escala podem ser aplicadas manualmente
        Vec3 rotatedPos = applyRotation(offset, part.yRot);  // Parte rotacionada do ModelPart

        // A posição do ModelPart no mundo é a posição da entidade + as transformações do ModelPart

        // Aplica o offset
        return new Vec3(
                rotatedPos.x + entity.getX(),
                rotatedPos.y + entity.getY(),
                rotatedPos.z + entity.getZ()
        );
    }
    public static Vec3 getModelPartWorldPosition(ModelPart part, float offsetX, float offsetY, float offsetZ, @Nullable Entity entity, boolean relative, boolean rotate, float[] floats) {
        Vector3f local = new Vector3f(offsetX, offsetY, offsetZ);

        if (entity == null) {
            return new Vec3(local.x(), local.y(), local.z());
        }

        if (rotate) {
            local.transform(Quaternion.fromXYZ(part.xRot * floats[0], part.yRot *  floats[1], part.zRot *  floats[2]));
        }

        Quaternion entityRot = Quaternion.fromYXZ(-(entity.yRotO * ((float) Math.PI / 180f)), 0f, 0f);
        local.transform(entityRot);

        if (relative) {
            return new Vec3(local);
        }

        return entity.position().add(local.x(), local.y(), local.z());
    }

    /**
     * Calculates the world position from a model part using a custom transform approach with rotation mirroring support.
     *
     * @param part             The model part to use.
     * @param offsetX          X offset relative to the model part.
     * @param offsetY          Y offset relative to the model part.
     * @param offsetZ          Z offset relative to the model part.
     * @param entity           The entity used for base position and orientation.
     * @param v                Rotation vector (in degrees).
     * @param affectEntityXrot Whether to apply the entity's X rotation to the transformation.
     * @return The resulting world-space position as a {@link Vec3}.
     */

    public static Vec3 getWorldPositionFromModelPart(ModelPart part, float offsetX, float offsetY, float offsetZ, Entity entity, Vec3 v, boolean affectEntityXrot) {
		// if using a player the best offset positions is v.x() = 180, offsetX = 0.65 if right hand and -0.65 if not, offsetY being -1.1

    
        PoseStack stack = new PoseStack();

        // Aplica as transformações da entidade (opcional)
        stack.translate(entity.getX(), entity.getEyeY(), entity.getZ());
        stack.mulPose(Vector3f.YP.rotationDegrees(entity instanceof Player player ? -player.yBodyRotO : -entity.yRotO));
        if (affectEntityXrot){
			stack.mulPose(Vector3f.XP.rotationDegrees(entity.getXRot()));
        }

        // Aplica as transformações do ModelPart
        part.xRot *= -1;
        part.yRot *= -1;
        part.zRot *= 1;
        stack.mulPose(Vector3f.YP.rotationDegrees((float) v.x()));
        part.translateAndRotate(stack);
        

        // Aplica o offset local
        Matrix4f matrix = stack.last().pose();
        Vector4f pos = new Vector4f(offsetX, offsetY, offsetZ, 1.0F);
        pos.transform(matrix);

        return new Vec3(pos.x(), pos.y(), pos.z());
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
        Quaternion rotation = Quaternion.fromYXZ(yRot, xRot, zRot); // Ordem mais correta
        Vector3f localVector = new Vector3f((float) local.x, (float) local.y, (float) local.z);
        localVector.transform(rotation);
        return new Vec3(localVector.x(), localVector.y(), localVector.z());
    }

    public static Vec3 getRelativePositionModelPartStyle(Entity entity, ModelPart part, double deltaX, double deltaY, double deltaZ) {
        Vec3 position = entity.position(); // Posição base da entidade

        // Obtém rotações do ModelPart (em radianos)
        float pitch = part.xRot;
        float yaw = part.yRot;
        float roll = part.zRot;

        // Matriz de rotação baseada nas rotações locais (pitch, yaw, roll)
        // Aqui você pode criar vetores base com base nisso

        // Cálculo similar ao anterior, adaptado
        float yawRad = yaw + (float) Math.PI / 2;
        float pitchRad = -pitch;
        float pitchRad90 = -pitch + (float) Math.PI / 2;

        float cosYaw = Mth.cos(yawRad);
        float sinYaw = Mth.sin(yawRad);
        float cosPitch = Mth.cos(pitchRad);
        float sinPitch = Mth.sin(pitchRad);
        float cosPitch90 = Mth.cos(pitchRad90);
        float sinPitch90 = Mth.sin(pitchRad90);

        Vec3 forward = new Vec3(cosYaw * cosPitch, sinPitch, sinYaw * cosPitch);
        Vec3 up = new Vec3(cosYaw * cosPitch90, sinPitch90, sinYaw * cosPitch90);
        Vec3 right = forward.cross(up).scale(-1.0D);

        double newX = forward.x * deltaZ + up.x * deltaY + right.x * deltaX;
        double newY = forward.y * deltaZ + up.y * deltaY + right.y * deltaX;
        double newZ = forward.z * deltaZ + up.z * deltaY + right.z * deltaX;

        return new Vec3(position.x + newX, position.y + newY, position.z + newZ);

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
}
