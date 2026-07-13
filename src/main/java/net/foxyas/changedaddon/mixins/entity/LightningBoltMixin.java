package net.foxyas.changedaddon.mixins.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.entity.api.IDynamicThunderBolt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;
import java.util.function.Predicate;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin extends Entity implements IDynamicThunderBolt {

    public LightningBoltMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public float getScale() {
        return this.entityData.hasItem(SCALE) ? this.entityData.get(SCALE) : 1f;
    }

    @Override
    public void setScale(float scale) {
        this.entityData.set(SCALE, scale);
    }

    @Override
    public Vector3f getRenderScale() {
        return this.entityData.hasItem(VISUAL_SCALE) ? this.entityData.get(VISUAL_SCALE) : new Vector3f(getScale(), getScale(), getScale());
    }

    @Override
    public void setRenderScale(Vector3f renderScale) {
        this.entityData.set(VISUAL_SCALE, renderScale);
    }

    @Override
    public void setThunderColor(Color color) {
        this.entityData.set(COLOR_INT, color.getRGB());
    }

    @Override
    public Color getThunderColor() {
        return this.entityData.hasItem(COLOR_INT) ? new Color(this.entityData.get(COLOR_INT)) : DEFAULT_COLOR;
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectData(CallbackInfo ci) {
        this.entityData.define(SCALE, 1f);
        this.entityData.define(VISUAL_SCALE, new Vector3f(1f, 1f, 1f));
        this.entityData.define(COLOR_INT, DEFAULT_COLOR.getRGB());
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", ordinal = 0))
    private List<Entity> scaleHitBox(Level level, Entity entity, AABB aabb, Predicate<? super Entity> predicate, Operation<List<Entity>> original) {
        AABB scaledBoundingBox = new AABB(
                this.getX() - 15.0D,
                this.getY() - 15.0D * this.getScale(),
                this.getZ() - 15.0D * this.getScale(),
                this.getX() + 15.0D * this.getScale(),
                this.getY() + (6.0D + 15.0D) * this.getScale(),
                this.getZ() + 15.0D * this.getScale()
        );
        return original.call(level, entity, scaledBoundingBox, predicate);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", ordinal = 1))
    private List<Entity> scaleDamageHitbox(Level level, Entity entity, AABB aabb, Predicate<? super Entity> predicate, Operation<List<Entity>> original) {
        AABB scaledBoundingBox = new AABB(
                this.getX() - 3.0D * this.getScale(),
                this.getY() - 3.0D * this.getScale(),
                this.getZ() - 3.0D * this.getScale(),
                this.getX() + 3.0D * this.getScale(),
                this.getY() + (6.0D + 3.0D) * this.getScale(),
                this.getZ() + 3.0D * this.getScale()
        );
        return original.call(level, entity, scaledBoundingBox, predicate);
    }
}
