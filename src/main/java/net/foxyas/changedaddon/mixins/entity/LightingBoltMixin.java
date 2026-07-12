package net.foxyas.changedaddon.mixins.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.entity.api.IScalableLightingBolt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

@Mixin(LightningBolt.class)
public abstract class LightingBoltMixin extends Entity implements IScalableLightingBolt {

    public LightingBoltMixin(EntityType<?> pEntityType, Level pLevel) {
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

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectData(CallbackInfo ci) {
        this.entityData.define(SCALE, 1f);
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
