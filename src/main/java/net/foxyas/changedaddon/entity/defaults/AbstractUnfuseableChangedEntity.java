package net.foxyas.changedaddon.entity.defaults;

import net.foxyas.changedaddon.entity.api.CustomPatReaction;
import net.foxyas.changedaddon.entity.api.ICoatLikeEntity;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.ai.LatexFollowOwnerGoal;
import net.ltxprogrammer.changed.entity.ai.LatexOwnerHurtByTargetGoal;
import net.ltxprogrammer.changed.entity.ai.LatexOwnerHurtTargetGoal;
import net.ltxprogrammer.changed.init.ChangedCriteriaTriggers;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.common.IExtensibleEnum;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractUnfuseableChangedEntity extends ChangedEntity implements ICoatLikeEntity, CustomPatReaction {
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(AbstractUnfuseableChangedEntity.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID_ID = SynchedEntityData.defineId(AbstractUnfuseableChangedEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Boolean> UNFUSED_FROM_HOST = SynchedEntityData.defineId(AbstractUnfuseableChangedEntity.class, EntityDataSerializers.BOOLEAN);

    public AbstractUnfuseableChangedEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    public static LootTable.@NotNull Builder getLoot() {
        return LootTable.lootTable();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(6, new LatexFollowOwnerGoal<>(this, 0.35D, 10.0F, 2.0F, false));
        this.targetSelector.addGoal(1, new LatexOwnerHurtByTargetGoal<>(this));
        this.targetSelector.addGoal(2, new LatexOwnerHurtTargetGoal<>(this));
    }

    @Override
    public boolean isUnfusedFromHost() {
        return this.entityData.get(UNFUSED_FROM_HOST);
    }

    @Override
    public void setIsUnfusedFromHost(boolean value) {
        this.entityData.set(UNFUSED_FROM_HOST, value);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
        this.entityData.define(DATA_OWNER_UUID_ID, Optional.empty());
        this.entityData.define(UNFUSED_FROM_HOST, false);

    }

    @Override
    public void stopSleeping() {
        super.stopSleeping();
    }

    @Override
    public void startSleeping(@NotNull BlockPos blockPos) {
        super.startSleeping(blockPos);
    }

    public boolean isBiped() {
        return true;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        UUID uuid;
        if (tag.hasUUID("Owner")) {
            uuid = tag.getUUID("Owner");
        } else {
            String s = tag.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (tag.contains("FollowOwner"))
            this.setFollowOwner(tag.getBoolean("FollowOwner"));

        if (tag.contains("UnfusedFromHost"))
            this.setIsUnfusedFromHost(tag.getBoolean("UnfusedFromHost"));

        if (uuid != null) {
            try {
                this.setOwnerUUID(uuid);
                this.setTame(true);
            } catch (Throwable throwable) {
                this.setTame(false);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        if (this.getOwnerUUID() != null) {
            tag.putUUID("Owner", this.getOwnerUUID());
        }
        tag.putBoolean("UnfusedFromHost", this.isUnfusedFromHost());
        tag.putBoolean("FollowOwner", this.isFollowingOwner());
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return super.targetSelectorTest(livingEntity) && livingEntity != this.getOwner();
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UUID_ID).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_OWNER_UUID_ID, Optional.ofNullable(uuid));
    }

    public boolean isPreventingPlayerRest(Player player) {
        if (isTame() && player.getUUID().equals(getOwnerUUID()))
            return false;
        return super.isPreventingPlayerRest(player);
    }

    protected void spawnTamingParticles(boolean success) {
        ParticleOptions particleoptions = ParticleTypes.HEART;
        if (!success) {
            particleoptions = ParticleTypes.SMOKE;
        }

        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(particleoptions, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
        }

    }

    public void handleEntityEvent(byte event) {
        if (event == 7) {
            this.spawnTamingParticles(true);
        } else if (event == 6) {
            this.spawnTamingParticles(false);
        } else {
            super.handleEntityEvent(event);
        }

    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public void tame(Player player) {
        this.setTame(true);
        this.setFollowOwner(true);
        this.setOwnerUUID(player.getUUID());
        if (player instanceof ServerPlayer serverPlayer) {
            ChangedCriteriaTriggers.TAME_LATEX.trigger(serverPlayer, this);
        }

    }

    @Override
    protected @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        if (this.level.isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (this.isTame()) {
                if (this.isTame() && this.isTameItem(itemstack) && this.getHealth() < this.getMaxHealth()) {
                    itemstack.shrink(1);
                    this.heal(2.0F);
                    if (this.level instanceof ServerLevel _level) {
                        _level.sendParticles(ParticleTypes.HEART, (this.getX()), (this.getY() + 1), (this.getZ()), 7, 0.3, 0.3, 0.3, 1); //Spawn Heal Particles
                    }
                    this.level().gameEvent(this, GameEvent.ENTITY_INTERACT, this.getEyePosition());
                    return InteractionResult.SUCCESS;
                } else {
                    InteractionResult interactionresult = super.mobInteract(player, hand);
                    if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) {
                        boolean shouldFollow = !this.isFollowingOwner();
                        this.setFollowOwner(shouldFollow);

                        player.displayClientMessage(Component.translatable(shouldFollow ? "text.changed.tamed.follow" : "text.changed.tamed.wander", this.getDisplayName()), true);
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget(null);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFollowingOwner() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    @Override
    public void setFollowOwner(boolean value) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (value) {
            this.entityData.set(DATA_FLAGS_ID, (byte) (b0 | 1));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte) (b0 & -2));
        }

    }

    @Override
    public void checkDespawn() {
        if (isTame())
            return;
        super.checkDespawn();
    }

    @Override
    public void WhenPattedReactionSpecific(Player patter, InteractionHand hand, Vec3 pattedLocation) {
        CustomPatReaction.super.WhenPattedReactionSpecific(patter, hand, pattedLocation);
    }

    @Override
    public void WhenPatEvent(LivingEntity patter, InteractionHand hand, LivingEntity patTarget) {
        CustomPatReaction.super.WhenPatEvent(patter, hand, patTarget);
    }

    public boolean isTame() {
        return (this.entityData.get(DATA_FLAGS_ID) & 4) != 0;
    }

    public void setTame(boolean tame) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (tame) {
            this.entityData.set(DATA_FLAGS_ID, (byte) (b0 | 4));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte) (b0 & -5));
        }

        this.reassessTameGoals();
    }

    protected void reassessTameGoals() {
    }

    public boolean isOwnedBy(LivingEntity entity) {
        return entity == this.getOwner();
    }

    public boolean canAttack(@NotNull LivingEntity entity) {
        return !this.isOwnedBy(entity) && super.canAttack(entity);
    }

    public Team getTeam() {
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (livingentity != null) {
                return livingentity.getTeam();
            }
        }

        return super.getTeam();
    }

    public boolean isAlliedTo(@NotNull Entity entity) {
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (entity == livingentity) {
                return true;
            }

            if (livingentity != null) {
                return livingentity.isAlliedTo(entity);
            }
        }

        return super.isAlliedTo(entity);
    }

    public void die(@NotNull DamageSource source) {
        // FORGE: Super moved to top so that death message would be cancelled properly
        net.minecraft.network.chat.Component deathMessage = this.getCombatTracker().getDeathMessage();
        super.die(source);

        if (this.dead)
            if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer) {
                ((ServerPlayer) this.getOwner()).displayClientMessage(deathMessage, false);
            }
    }

    //TameType Use Type
    public boolean isTameItem(ItemStack stack, TameType tameType) {
        return stack.is(Items.COD)
                || stack.is(ChangedItems.ORANGE.get())
                || stack.is(Items.COOKED_COD)
                || stack.is(Items.SALMON)
                || stack.is(Items.COOKED_SALMON)
                || stack.is(ItemTags.create(ResourceLocation.parse(tameType.Tag)));
    }

    //Default Use Type
    public boolean isTameItem(ItemStack stack) {
        return stack.is(Items.COD)
                || stack.is(ChangedItems.ORANGE.get())
                || stack.is(Items.COOKED_COD)
                || stack.is(Items.SALMON)
                || stack.is(Items.COOKED_SALMON)
                || stack.is(ChangedAddonTags.Items.TAME_ITEM);
    }

    //Public enum TameType that just hold a string for the Items tag Logic
    public enum TameType implements IExtensibleEnum {
        CAT("changed_addon:cat_tame_items"),
        DOG("changed_addon:dog_tame_items");

        public final String Tag;

        TameType(String tag) {
            this.Tag = tag;
        }

        public static TameType create(String name, String tag) {
            throw new NotImplementedException("Not extended");
        }
    }
}
