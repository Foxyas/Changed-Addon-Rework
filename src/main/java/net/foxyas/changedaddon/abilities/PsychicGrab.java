package net.foxyas.changedaddon.abilities;

import net.foxyas.changedaddon.mixins.AbstractArrowAccessor;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.foxyas.changedaddon.process.util.FoxyasUtils;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.ability.SimpleAbilityInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import net.minecraft.client.multiplayer.ClientLevel;

public class PsychicGrab extends SimpleAbility {
    public Vec3 offset = Vec3.ZERO;
    public Vec3 look = Vec3.ZERO;
    public UUID TargetID = UUID.fromString("0-0-0-0-0"); //Fail Safe

    private AbstractAbilityInstance abilityInstance;
    private Controller controller;

    @Override
    public SimpleAbilityInstance makeInstance(IAbstractChangedEntity entity) {
        offset = new Vec3(0, 0, 3);
        if (entity.getEntity() instanceof Player player) {
            look = FoxyasUtils.getRelativePositionEyes(player, offset.scale(0.1));
        }
        SimpleAbilityInstance simpleAbilityInstance = super.makeInstance(entity);
        this.abilityInstance = simpleAbilityInstance;
        return simpleAbilityInstance;
    }

    @Nullable
    @Override
    public Component getSelectedDisplayText(IAbstractChangedEntity entity) {
        return super.getSelectedDisplayText(entity);
    }

    @Override
    public TranslatableComponent getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.psychic_grab");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/psychic_hold.png"); // Placeholder
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        Collection<Component> descriptions = new ArrayList<>(super.getAbilityDescription(entity));
        descriptions.add(new TranslatableComponent("changed_addon.ability.psychic_grab.description"));
        return descriptions;
    }

    public @Nullable Entity getTargetByID(Level level, UUID uuid) {
        if (level instanceof ServerLevel serverLevel) {
            return PlayerUtilProcedure.GlobalEntityUtil.getEntityByUUID(serverLevel, uuid.toString());
        }
        return null;
    }

    public @Nullable Entity getTargetByIDInClientSide(Level level, UUID uuid) {
        if (level instanceof ClientLevel clientLevel) {
            return PlayerUtilProcedure.GlobalEntityUtil.getEntityByUUID(clientLevel, uuid.toString());
        }
        return null;
    }

    public @Nullable Entity getTarget(Level level, UUID uuid) {
        return PlayerUtilProcedure.GlobalEntityUtil.getEntityByUUID(level, uuid.toString());
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        Entity target = getTarget(entity.getEntity().getLevel(), TargetID);
        return (target != null) ? UseType.HOLD : UseType.INSTANT;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        Entity target = getTarget(entity.getLevel(), TargetID);
        return (target == null) ? 15 : 0;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        Entity target = getTarget(entity.getLevel(), TargetID);
        LivingEntity self = entity.getEntity();
        if (target != null) {
            if (entity.getEntity().distanceTo(target) > 10) {
            	if (self.isShiftKeyDown()){
            		return true;
            	}
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
    public boolean canKeepUsing(IAbstractChangedEntity entity) {
        Entity target = getTarget(entity.getLevel(), TargetID);
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
    public void startUsing(IAbstractChangedEntity entity) {
        if (entity.getLevel().isClientSide()) {
            return;
        }
        Entity target = getTarget(entity.getLevel(), TargetID);
        if (entity.getEntity().isShiftKeyDown() || getTargetByID(entity.getLevel(), TargetID) == null) {
            if (PlayerUtilProcedure.getEntityLookingAt(entity.getEntity(), 6) == null) {
                return;
            }
            TargetID = PlayerUtilProcedure.getEntityLookingAt(entity.getEntity(), 6).getUUID();
        } else {
            if (!target.isAlive()) {
                if (PlayerUtilProcedure.getEntityLookingAt(entity.getEntity(), 6) == null) {
                    return;
                }
                TargetID = PlayerUtilProcedure.getEntityLookingAt(entity.getEntity(), 6).getUUID();
            }

            if (target instanceof Projectile projectile) {
                if (projectile instanceof AbstractArrow arrow) {
                    if (arrow instanceof AbstractArrowAccessor arrowAccessor) {
                        if (arrowAccessor.inGround()) {
                            arrowAccessor.setInGround(false);
                            arrow.setDeltaMovement((look.subtract(target.position())));
                        }
                    }
                    /*CompoundTag tag = new CompoundTag();
                    arrow.addAdditionalSaveData(tag);
                    boolean inGround = tag.contains("inGround") && tag.getBoolean("inGround");
                    if (inGround){
                        tag.putBoolean("inGround",false);
                        arrow.readAdditionalSaveData(tag);
                    }*/
                }
            }
        }
        super.startUsing(entity);
    }

    @Override
    public void tick(IAbstractChangedEntity entity) {
        super.tick(entity);
        /*if (entity.getEntity() instanceof Player player) {
            this.controller = abilityInstance.getController();
            player.displayClientMessage(new TextComponent("Hold Ticks:" + controller.getHoldTicks()), true);
        }*/ // it works
        Entity target = getTarget(entity.getLevel(), TargetID);
        if (target != null) {
            if (entity.getEntity().isShiftKeyDown()) {
                if (entity.getAbilityInstance(this) != null && entity.getAbilityInstance(this).getController().getHoldTicks() <= 3) {
                    if (PlayerUtilProcedure.getEntityLookingAt(entity.getEntity(), 6) != null
                            && PlayerUtilProcedure.getEntityLookingAt(entity.getEntity(), 6) != getTargetByID(entity.getLevel(), TargetID)) {
                        TargetID = PlayerUtilProcedure.getEntityLookingAt(entity.getEntity(), 6).getUUID();
                    }
                    return;
                }
            }
            /*if (target instanceof Projectile projectile) {
                if (projectile instanceof AbstractArrow arrow) {
                    if (arrow instanceof AbstractArrowAccessor arrowAccessor) {
                        if (arrowAccessor.inGround()) {
                            arrowAccessor.setInGround(false);
                        }
                    }
                    /*CompoundTag tag = new CompoundTag();
                    arrow.addAdditionalSaveData(tag);
                    boolean inGround = tag.contains("inGround") && tag.getBoolean("inGround");
                    if (inGround){
                        tag.putBoolean("inGround",false);
                        arrow.readAdditionalSaveData(tag);
                    }*\/
                    target.setDeltaMovement((look.subtract(target.position())));
                }
            }*/
            look = FoxyasUtils.getRelativePositionEyes(entity.getEntity(), offset.add(0, 0, 2));
            target.setDeltaMovement((look.subtract(target.position())).scale(0.1));
        }
    }

    @Override
    public void saveData(CompoundTag tag, IAbstractChangedEntity entity) {
        ListTag offsetList = new ListTag();
        offsetList.add(DoubleTag.valueOf(offset.x()));
        offsetList.add(DoubleTag.valueOf(offset.y()));
        offsetList.add(DoubleTag.valueOf(offset.z()));
        tag.put("Offset", offsetList);
        tag.putUUID("TargetUUID", TargetID);
        super.saveData(tag, entity);
    }

    @Override
    public void readData(CompoundTag tag, IAbstractChangedEntity entity) {
        if (tag.contains("Offset", 9)) { // TAG_List
            ListTag list = tag.getList("Offset", 6); // TAG_Double
            if (list.size() == 3) {
                this.offset = new Vec3(
                        list.getDouble(0),
                        list.getDouble(1),
                        list.getDouble(2)
                );
            }
        }
        if (tag.hasUUID("TargetUUID")) {
            this.TargetID = tag.getUUID("TargetUUID");
        }
        super.readData(tag, entity);
    }

    public void setOffset(Vec3 offset) {
        this.offset = offset;
    }

    public void addOffset(int keyCode, Player player) {
        double dx = 0, dy = 0, dz = 0;
        boolean shift = player.isShiftKeyDown();

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

        Vec3 newOffset = offset.add(dx, dy, dz);
        this.offset = new Vec3(
                Mth.clamp(newOffset.x, -3, 3),
                Mth.clamp(newOffset.y, -3, 3),
                Mth.clamp(newOffset.z, 0, 4)
        );
    }

    public static boolean isSpectator(Entity entity) {
        return entity instanceof Player player && player.isSpectator();
    }

    public static final Set<Integer> Keys = Set.of(
            GLFW.GLFW_KEY_UP,
            GLFW.GLFW_KEY_DOWN,
            GLFW.GLFW_KEY_LEFT,
            GLFW.GLFW_KEY_RIGHT
    );
}