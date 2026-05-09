package net.foxyas.changedaddon.entity.defaults;

import net.foxyas.changedaddon.entity.api.CustomPatReaction;
import net.foxyas.changedaddon.entity.api.ICoatLikeEntity;
import net.foxyas.changedaddon.entity.api.ISafeChangedEntity;
import net.foxyas.changedaddon.entity.defaults.AbstractTamableLatexEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.ai.LatexFollowOwnerGoal;
import net.ltxprogrammer.changed.entity.ai.LatexOwnerHurtByTargetGoal;
import net.ltxprogrammer.changed.entity.ai.LatexOwnerHurtTargetGoal;
import net.ltxprogrammer.changed.init.ChangedCriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

public abstract class AbstractCoatEntity extends AbstractTamableLatexEntity implements ICoatLikeEntity, CustomPatReaction, ISafeChangedEntity {
    protected static final EntityDataAccessor<Boolean> UNFUSED_FROM_HOST = SynchedEntityData.defineId(AbstractCoatEntity.class, EntityDataSerializers.BOOLEAN);

    public AbstractCoatEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    public static LootTable.@NotNull Builder getLoot() {
        return LootTable.lootTable();
    }

    @Override
    public boolean shouldScareVillagers(ChangedEntity entity, AbstractVillager villager) {
        return ISafeChangedEntity.super.shouldScareVillagers(entity, villager);
    }

    protected void registerGoals() {
        super.registerGoals();
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

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("UnfusedFromHost"))
            this.setIsUnfusedFromHost(tag.getBoolean("UnfusedFromHost"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("UnfusedFromHost", this.isUnfusedFromHost());
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return super.targetSelectorTest(livingEntity) && livingEntity != this.getOwner();
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
            this.level().addParticle(particleoptions, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
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
        if (tryFuseBack(player, this)) return InteractionResult.SUCCESS;
        return super.mobInteract(player, hand);
    }

    @Override
    public void checkDespawn() {
        if (isTame())
            return;
        super.checkDespawn();
    }

    @Override
    public void WhenPattedReactionSpecific(LivingEntity patter, InteractionHand hand, Vec3 pattedLocation) {
        CustomPatReaction.super.WhenPattedReactionSpecific(patter, hand, pattedLocation);
    }

    @Override
    public void WhenPatEvent(LivingEntity patter, InteractionHand hand, LivingEntity patTarget) {
        CustomPatReaction.super.WhenPatEvent(patter, hand, patTarget);
    }

    //TameType Use Type
    public boolean isTameItem(ItemStack stack, TameType tameType) {
        return false;
    }

    //Default Use Type
    public boolean isTameItem(ItemStack stack) {
        return false;
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
