package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.ability.api.IKeyPressHandler;
import net.foxyas.changedaddon.mixins.entity.projectiles.AbstractArrowAccessor;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.GrabEntityAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class PsychicGrabInstance extends AbstractAbilityInstance implements IKeyPressHandler {

    public static final String TARGET_UUID_TAG = "psychicGrabTarget";
    public Vec3 offset;
    public Vec3 look = Vec3.ZERO;
    public Entity entityCache = null;

    public PsychicGrabInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
        offset = new Vec3(0, 0, 3);
        if (entity.getEntity() instanceof Player player) {
            look = FoxyasUtil.getRelativePositionEyes(player, offset.scale(0.1));
        }
    }

    public static boolean isSpectator(Entity entity) {
        return entity instanceof Player player && player.isSpectator();
    }

    public @Nullable Entity getTarget(Level level) {
        return entityCache;
    }

    @Override
    public boolean canUse() {
        Entity target = getTarget(entity.getEntity().level());
        LivingEntity self = entity.getEntity();
        if (target != null) {
            if (target instanceof LivingEntity livingTarget) {
                IAbstractChangedEntity grabberOfTarget = GrabEntityAbility.getGrabber(livingTarget);
                if (grabberOfTarget != null) {
                    if (grabberOfTarget.getEntity().is(self)) {
                        return false;
                    }
                    return false;
                }
            }
            if (entity.getEntity().distanceTo(target) > 10) {
                return self.isShiftKeyDown();
            } else if (target instanceof Player player && isSpectator(player)) {
                return false;
            }
            if (self.hurtTime > 0 && self.getLastHurtByMob() == target) {
                return false;
            }
        }

        return !isSpectator(entity.getEntity());
    }

    @Override
    public boolean canKeepUsing() {
        Entity target = getTarget(entity.getLevel());
        LivingEntity self = entity.getEntity();
        if (target != null) {
            if (entity.getEntity().distanceTo(target) > 10) {
                return false;
            } else if (target instanceof Player player && isSpectator(player)) {
                return false;
            }
            if (self.hurtTime > 0 && self.getLastHurtByMob() == target) {
                return false;
            }
        }

        return !isSpectator(entity.getEntity());
    }

    @Override
    public void startUsing() {
        if (entity.getLevel().isClientSide()) {
            return;
        }
        Entity target = getTarget(entity.getLevel());
        Entity lookingAt = PlayerUtil.getEntityLookingAt(entity.getEntity(), 6, PlayerUtil.BLOCK_COLLISION);
        if (entity.getEntity().isShiftKeyDown() || target == null) {
            if (lookingAt == null) {
                return;
            }
            entityCache = lookingAt;
            ability.setDirty(entity);
            return;
        }

        if (!target.isAlive()) {
            if (lookingAt == null) {
                return;
            }
            entityCache = lookingAt;
            ability.setDirty(entity);
        }
    }

    @Override
    public void tickIdle() {
        super.tickIdle();
        Entity target = getTarget(entity.getLevel());
        if (target == null || target.isRemoved()) {
            entityCache = null;
            ability.setDirty(entity);
        }
    }

    @Override
    public void tick() {
        Entity target = getTarget(entity.getLevel());
        AbstractAbility.Controller controller = this.getController();
        if (target != null) {
            if (entity.getEntity().isShiftKeyDown()) {
                if (controller.getHoldTicks() <= 3) {
                    Entity lookingAt = PlayerUtil.getEntityLookingAt(entity.getEntity(), 6, PlayerUtil.BLOCK_COLLISION);
                    if (lookingAt != null && lookingAt != target) {
                        entityCache = lookingAt;
                        ability.setDirty(entity);
                    }
                    return;
                }
            }
            look = FoxyasUtil.getRelativePositionEyes(entity.getEntity(), offset.add(0, 0, 2));
            Vec3 scale = (look.subtract(target.position())).scale(0.25);
            if (target instanceof AbstractArrow projectile && projectile instanceof AbstractArrowAccessor accessor && accessor.inGround()) {
                projectile.move(MoverType.PLAYER, scale);
            } else {
                if (target.position().distanceTo(look) <= 0.05) {
                    target.setDeltaMovement(Vec3.ZERO);
                }
                target.setDeltaMovement(scale);
            }
            target.hurtMarked = true;
            target.hasImpulse = true;
            if (target instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(
                        serverPlayer.getId(),
                        serverPlayer.getDeltaMovement())
                );
            }
            entity.causeFoodExhaustion(2f);
            ability.setDirty(entity);
        } else {
            controller.deactivateAbility();
            int coolDown = this.ability.getCoolDown(entity);
            controller.forceCooldown(coolDown);
            ability.setDirty(entity);
        }
    }

    @Override
    public void stopUsing() {
        Entity target = getTarget(entity.getLevel());

        if (target != null) {
            target.resetFallDistance();
        }
    }

    @Override
    public void saveData(CompoundTag tag) {
        ListTag offsetList = new ListTag();
        offsetList.add(DoubleTag.valueOf(offset.x()));
        offsetList.add(DoubleTag.valueOf(offset.y()));
        offsetList.add(DoubleTag.valueOf(offset.z()));
        tag.put("offset", offsetList);
        if (entityCache != null) {
            tag.putUUID(TARGET_UUID_TAG, this.entityCache.getUUID());
        }
        super.saveData(tag);
    }

    @Override
    public void readData(CompoundTag tag) {
        if (tag.contains("offset", 9)) { // TAG_List
            ListTag list = tag.getList("offset", 6); // TAG_Double
            if (list.size() == 3) {
                this.offset = new Vec3(
                        list.getDouble(0),
                        list.getDouble(1),
                        list.getDouble(2)
                );
            }
        }
        if (tag.hasUUID(TARGET_UUID_TAG)) {
            entityCache = PlayerUtil.GlobalEntityUtil.getEntityByUUID(entity.getLevel(), tag.getUUID(TARGET_UUID_TAG));
        }
        super.readData(tag);
    }

    @Override
    public AbstractAbility.UseType getUseType() {
        Entity target = getTarget(entity.getEntity().level());
        return (target != null) ? AbstractAbility.UseType.HOLD : AbstractAbility.UseType.INSTANT;
    }

    public void addOffset(int keyCode, boolean shift, LivingEntity livingEntity) {
        double dx = 0, dy = 0, dz = 0;

        switch (keyCode) {
            case GLFW.GLFW_KEY_UP -> {
                dz = shift ? 0.5 : 0;
                dy = shift ? 0 : 0.5;
            }
            case GLFW.GLFW_KEY_DOWN -> {
                dz = shift ? -0.5 : 0;
                dy = shift ? 0 : -0.5;
            }
            case GLFW.GLFW_KEY_LEFT -> dx = 0.5;
            case GLFW.GLFW_KEY_RIGHT -> dx = -0.5;
            default -> {
                return;
            }
        }

        IAbstractChangedEntity abstractChangedEntity = IAbstractChangedEntity.forEither(livingEntity);
        if (abstractChangedEntity == null) return;
        Vec3 newOffset = offset.add(dx, dy, dz);
        this.offset = new Vec3(
                Mth.clamp(newOffset.x, -3, 3),
                Mth.clamp(newOffset.y, -3, 3),
                Mth.clamp(newOffset.z, 0, 4)
        );
    }

    @Override
    public void onServerProcessKeyPressed(Player player, int keyPressed, int action, int modifiers) {
        addOffset(keyPressed, modifiers == GLFW.GLFW_MOD_SHIFT, entity.getEntity());
    }

    @Override
    public boolean isKeyPressedValid(Player player, int keyPressed, int action, int modifiers) {
        return PsychicGrab.Keys.contains(keyPressed);
    }
}
