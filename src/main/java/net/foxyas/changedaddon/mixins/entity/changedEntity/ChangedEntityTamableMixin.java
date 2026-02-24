package net.foxyas.changedaddon.mixins.entity.changedEntity;

import net.foxyas.changedaddon.entity.ai.*;
import net.foxyas.changedaddon.entity.api.TamableLatexEntityFavors;
import net.foxyas.changedaddon.menu.TamedLatexInventoryMenu;
import net.foxyas.changedaddon.menu.TamedLatexMenu;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Mixin(value = ChangedEntity.class, remap = false)
public abstract class ChangedEntityTamableMixin extends Monster implements TamableLatexEntityFavors {

    @Unique
    protected @Nullable LatexInventory inventory;
    @Unique
    protected @Nullable GrabEntityAbilityInstance grabEntityAbilityInstance;

    protected ChangedEntityTamableMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "<init>", at = @At("TAIL"), cancellable = false)
    private void init(EntityType<? extends Monster> type, Level level, CallbackInfo ci) {
        this.inventory = null;
        this.grabEntityAbilityInstance = null;
    }

    // --- Implementação da Interface ---

    @Override
    public GrabEntityAbilityInstance createGrabAbility() {
        return new GrabEntityAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get(), IAbstractChangedEntity.forEntity((ChangedEntity) (Object) this));
    }

    @Override
    public LatexInventory createInventory() {
        return new LatexInventory(this);
    }

    @Override
    public @Nullable LatexInventory getInventory() {
        return inventory;
    }

    @Override
    public LatexFavor getCurrentFavor() {
        if (this.entityData.get(DATA_FAVOR) == null) return LatexFavor.NONE;
        LatexFavor favor = this.entityData.get(DATA_FAVOR);
        return canDoFavor(favor) ? favor : LatexFavor.NONE;
    }

    @Override
    public @Nullable GrabEntityAbilityInstance getGrabAbility() {
        return grabEntityAbilityInstance;
    }

    @Override
    public void setFavor(LatexFavor value) {
        if (!this.canDoFavor(value)) value = LatexFavor.NONE;
        this.entityData.set(DATA_FAVOR, value);

        LivingEntity owner = this.getOwner();
        if (value != LatexFavor.SUIT_OWNER) {
            if (owner != null && grabEntityAbilityInstance != null && grabEntityAbilityInstance.grabbedEntity == owner) {
                grabEntityAbilityInstance.releaseEntity(false);
                Changed.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY.with(() -> this),
                        new GrabEntityPacket((ChangedEntity) (Object) this, owner, GrabEntityPacket.GrabType.RELEASE));
                ChangedSounds.broadcastSound(this, ChangedSounds.LATEX_UNSUIT_ENTITY, 1.0f, 1.0f);
            }
        }
    }

    @Override
    public LatexAttackCondition getAttackCondition() {
        return this.entityData.get(DATA_ATTACK_CONDITION);
    }

    @Override
    public void setAttackCondition(LatexAttackCondition condition) {
        this.entityData.set(DATA_ATTACK_CONDITION, condition);
    }

    @Override
    public LatexAttackType getAttackType() {
        return this.entityData.get(DATA_ATTACK_TYPE);
    }

    @Override
    public void setAttackType(LatexAttackType type) {
        this.entityData.set(DATA_ATTACK_TYPE, type);
    }

    @Override
    public LatexTargetType getTargetType() {
        return this.entityData.get(DATA_TARGET_TYPE);
    }

    @Override
    public void setTargetType(LatexTargetType type) {
        this.entityData.set(DATA_TARGET_TYPE, type);
    }

    @Override
    public boolean canDoFavor(LatexFavor favor) {
        return true;
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public boolean isFollowingOwner() {
        return (this.entityData.get(DATA_FLAGS) & 1) != 0;
    }

    @Override
    public void setFollowOwner(boolean value) {
        byte b0 = this.entityData.get(DATA_FLAGS);
        if (value) {
            this.entityData.set(DATA_FLAGS, (byte) (b0 | 1));
        } else {
            this.entityData.set(DATA_FLAGS, (byte) (b0 & -2));
        }

    }

    public boolean isTame() {
        return (this.entityData.get(DATA_FLAGS) & 4) != 0;
    }

    public void reassessTameGoals() {
    }

    public void setTame(boolean tame) {
        byte b0 = this.entityData.get(DATA_FLAGS);
        if (tame) {
            this.entityData.set(DATA_FLAGS, (byte) (b0 | 4));
        } else {
            this.entityData.set(DATA_FLAGS, (byte) (b0 & -5));
        }

        this.reassessTameGoals();
        if (tame && this.inventory == null)
            this.inventory = this.createInventory();
        if (tame && this.grabEntityAbilityInstance == null)
            this.grabEntityAbilityInstance = createGrabAbility();
    }

    // --- Injections ---

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void onDefineSynchedData(CallbackInfo ci) {
        this.entityData.define(DATA_FLAGS, (byte) 0);
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
        this.entityData.define(DATA_TARGET_TYPE, LatexTargetType.TRANSFURABLE_ENTITIES);
        this.entityData.define(DATA_ATTACK_TYPE, LatexAttackType.TRY_TRANSFUR);
        this.entityData.define(DATA_ATTACK_CONDITION, LatexAttackCondition.ALWAYS);
        this.entityData.define(DATA_FAVOR, LatexFavor.NONE);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void onAddAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getOwnerUUID() != null) {
            tag.putUUID("Owner", this.getOwnerUUID());
        }

        tag.putBoolean("FollowOwner", this.isFollowingOwner());


        if (this.inventory != null) {
            tag.put("Inventory", this.inventory.save(new ListTag()));
            tag.putInt("SelectedItemSlot", this.inventory.selected);
            tag.putString("TargetType", getTargetType().getSerializedName());
            tag.putString("AttackType", getAttackType().getSerializedName());
            tag.putString("AttackCondition", getAttackCondition().getSerializedName());
            tag.putString("FavorToOwner", getCurrentFavor().getSerializedName());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void onReadAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {

        UUID uuid;
        if (tag.hasUUID("Owner")) {
            uuid = tag.getUUID("Owner");
        } else {
            String s = tag.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (tag.contains("FollowOwner"))
            this.setFollowOwner(tag.getBoolean("FollowOwner"));

        if (uuid != null) {
            try {
                this.setOwnerUUID(uuid);
                this.setTame(true);
            } catch (Throwable throwable) {
                this.setTame(false);
            }
        }

        if (tag.contains("Inventory")) {
            ListTag listtag = tag.getList("Inventory", 10);
            this.inventory = this.createInventory();
            this.inventory.load(listtag);
            this.inventory.selected = tag.getInt("SelectedItemSlot");
            this.grabEntityAbilityInstance = createGrabAbility();

            LatexTargetType.fromSerial(tag.getString("TargetType")).result().ifPresent(this::setTargetType);
            LatexAttackType.fromSerial(tag.getString("AttackType")).result().ifPresent(this::setAttackType);
            LatexAttackCondition.fromSerial(tag.getString("AttackCondition")).result().ifPresent(this::setAttackCondition);
            LatexFavor.fromSerial(tag.getString("FavorToOwner")).result().ifPresent(this::setFavor);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(CallbackInfo ci) {
        if (!this.level().isClientSide && this.inventory != null) {
            // Lógica de atualização de itens (simplificada para o exemplo)
            if (grabEntityAbilityInstance != null) {
                grabEntityAbilityInstance.tickIdle();
                if (grabEntityAbilityInstance.grabbedEntity == this.getOwner() && grabEntityAbilityInstance.grabbedEntity != null) {
                    grabEntityAbilityInstance.grabbedHasControl = true;
                    grabEntityAbilityInstance.suited = true;
                }
            }
        }
    }

    @Unique
    public void swapSlotWithOffhand(int swapWith) {
        if (this.inventory == null) return;
        ItemStack currentOffhand = this.inventory.getItem(LatexInventory.SLOT_OFFHAND);
        this.inventory.setItem(LatexInventory.SLOT_OFFHAND, this.inventory.getItem(swapWith));
        this.inventory.setItem(swapWith, currentOffhand);
    }

    @Unique
    public boolean isInteractingWith(LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player.containerMenu instanceof TamedLatexMenu menu) return menu.tamedLatex == (Object) this;
            if (player.containerMenu instanceof TamedLatexInventoryMenu menu) return menu.tamedLatex == (Object) this;
        }
        return false;
    }
}