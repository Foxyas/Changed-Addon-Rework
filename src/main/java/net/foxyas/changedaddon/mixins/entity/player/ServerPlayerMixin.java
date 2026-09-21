package net.foxyas.changedaddon.mixins.entity.player;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;
import net.foxyas.changedaddon.entity.api.IDynamicCamera;
import net.foxyas.changedaddon.entity.api.LivingEntityDataExtensor;
import net.foxyas.changedaddon.entity.api.SyncTrackMotion;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements SyncTrackMotion, LivingEntityDataExtensor, IDynamicCamera {


    public ServerPlayerMixin(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Shadow
    public abstract boolean isSpectator();

    @Shadow
    public abstract Entity getCamera();

    @Shadow
    @javax.annotation.Nullable
    private Entity camera;

    @Shadow
    public abstract ServerLevel serverLevel();

    @Shadow
    public ServerGamePacketListenerImpl connection;

    @Unique
    private boolean resetCameraOnShift = true;
    @Unique
    private boolean softSetCameraByDefault = false;

    @Unique
    public boolean isMoving = false;

    @Unique
    public Vec3 lastKnownMotion = null;

    private int ticksWithLastKnowMotion = 0;

    @Override
    public boolean isMoving() {
        return isMoving;
    }

    @Override
    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
        this.ticksWithLastKnowMotion = 0;
    }

    @Unique
    public Vec3 getLastKnownMotion() {
        return lastKnownMotion;
    }

    @Override
    public void setLastKnownMotion(@Nullable Vec3 vec3) {
        this.lastKnownMotion = vec3;
        this.ticksWithLastKnowMotion = 0;
    }

    @Unique
    private ServerPlayer getSelf() {
        return (ServerPlayer) (Object) this;
    }

    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    private void cleanKnownMotion(CallbackInfo ci) {
        if (getLastKnownMotion() != null || isMoving()) {
            ticksWithLastKnowMotion++;
            if (ticksWithLastKnowMotion % 40 == 0) {
                if (getLastKnownMotion() != null) {
                    setLastKnownMotion(null);
                }
                if (isMoving) {
                    this.isMoving = false;
                }
            }
        }
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;wantsToStopRiding()Z"))
    private boolean wantToStopRidingHook(boolean original) {
        return original && shouldResetCameraOnShift();
    }

    @WrapMethod(method = "setCamera")
    private void setCameraHook(Entity pEntityToSpectate, Operation<Void> original) {
        if (softSetCameraByDefault) {
            this.softSetCamera(pEntityToSpectate);
        } else {
            original.call(pEntityToSpectate);
        }
    }

    @Override
    public boolean shouldResetCameraOnShift() {
        return this.isSpectator() || resetCameraOnShift;
    }

    @Override
    public boolean shouldSoftSetCameraByDefault() {
        return softSetCameraByDefault;
    }

    @Override
    public void setSoftSetCameraByDefault(boolean softSetCameraByDefault) {
        this.softSetCameraByDefault = softSetCameraByDefault;
    }

    @Override
    public void setResetCameraOnShift(boolean resetCameraOnShift) {
        this.resetCameraOnShift = resetCameraOnShift;
    }

    @Override
    public void softSetCamera(Entity pEntityToSpectate) {
        Entity entity = this.getCamera();
        this.camera = pEntityToSpectate == null ? this : pEntityToSpectate;
        while (this.camera instanceof net.minecraftforge.entity.PartEntity<?> partEntity)
            this.camera = partEntity.getParent(); // FORGE: fix MC-46486
        if (entity != this.camera && camera != null) {
            if (pEntityToSpectate != null) {
                this.serverLevel().getChunkSource().move((ServerPlayer) (Object) this);
            }

            this.connection.send(new ClientboundSetCameraPacket(this.camera));
            this.connection.resetPosition();
        }
    }
}
