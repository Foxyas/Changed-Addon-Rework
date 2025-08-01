package net.foxyas.changedaddon.entity.mobs;

import io.netty.buffer.Unpooled;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.procedures.*;
import net.foxyas.changedaddon.world.inventory.FoxyasGuiMenu;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EntityArmorInvWrapper;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FoxyasEntity extends Monster {
    private final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }
    };
    private final CombinedInvWrapper combined = new CombinedInvWrapper(inventory, new EntityHandsInvWrapper(this), new EntityArmorInvWrapper(this));

    public FoxyasEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.FOXYAS.get(), world);
    }

    public FoxyasEntity(EntityType<FoxyasEntity> type, Level world) {
        super(type, world);
        maxUpStep = 0.6f;
        xpReward = 10;
        setNoAi(false);
        setPersistenceRequired();
    }

    public static void init() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 24);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 5);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.getNavigation().getNodeEvaluator().setCanOpenDoors(true);
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2, false) {
            @Override
            protected double getAttackReachSqr(LivingEntity entity) {
                return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
            }
        });
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, ServerPlayer.class, (float) 6));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, (float) 6));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1) {
            @Override
            public boolean canUse() {
                double x = FoxyasEntity.this.getX();
                double y = FoxyasEntity.this.getY();
                double z = FoxyasEntity.this.getZ();
                Entity entity = FoxyasEntity.this;
                Level world = FoxyasEntity.this.level;
                return super.canUse() && IfnotInWaterProcedure.execute(entity);
            }

            @Override
            public boolean canContinueToUse() {
                double x = FoxyasEntity.this.getX();
                double y = FoxyasEntity.this.getY();
                double z = FoxyasEntity.this.getZ();
                Entity entity = FoxyasEntity.this;
                Level world = FoxyasEntity.this.level;
                return super.canContinueToUse() && IfnotInWaterProcedure.execute(entity);
            }
        });
        this.goalSelector.addGoal(7, new RandomSwimmingGoal(this, 1, 40) {
            @Override
            public boolean canUse() {
                double x = FoxyasEntity.this.getX();
                double y = FoxyasEntity.this.getY();
                double z = FoxyasEntity.this.getZ();
                Entity entity = FoxyasEntity.this;
                Level world = FoxyasEntity.this.level;
                return super.canUse() && IfInWaterProcedure.execute(entity);
            }

            @Override
            public boolean canContinueToUse() {
                double x = FoxyasEntity.this.getX();
                double y = FoxyasEntity.this.getY();
                double z = FoxyasEntity.this.getZ();
                Entity entity = FoxyasEntity.this;
                Level world = FoxyasEntity.this.level;
                return super.canContinueToUse() && IfInWaterProcedure.execute(entity);
            }
        });
        this.targetSelector.addGoal(8, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new FloatGoal(this));
        this.goalSelector.addGoal(11, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(12, new OpenDoorGoal(this, false));
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        FoxyasEntityDiesProcedure.execute(source.getEntity());
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (this.isAlive() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side == null)
            return LazyOptional.of(() -> combined).cast();
        return super.getCapability(capability, side);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        for (int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                this.spawnAtLocation(itemstack);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("InventoryCustom", inventory.serializeNBT());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        Tag inventoryCustom = compound.get("InventoryCustom");
        if (inventoryCustom instanceof CompoundTag inventoryTag)
            inventory.deserializeNBT(inventoryTag);
    }

    @Override
    public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
        ItemStack itemstack = sourceentity.getItemInHand(hand);
        InteractionResult retval = InteractionResult.sidedSuccess(this.level.isClientSide());
        if (sourceentity instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openGui(serverPlayer, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return new TextComponent("Foxyas");
                }

                @Override
                public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                    FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
                    packetBuffer.writeBlockPos(sourceentity.blockPosition());
                    packetBuffer.writeByte(0);
                    packetBuffer.writeVarInt(FoxyasEntity.this.getId());
                    return new FoxyasGuiMenu(id, inventory, packetBuffer);
                }
            }, buf -> {
                buf.writeBlockPos(sourceentity.blockPosition());
                buf.writeByte(0);
                buf.writeVarInt(this.getId());
            });
        }
        super.mobInteract(sourceentity, hand);
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        Entity entity = this;
        Level world = this.level;

        FoxyasRightClickedOnEntityProcedure.execute(world, x, y, z, entity, sourceentity);
        return retval;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        FoxyasOnEntityTickUpdateProcedure.execute(this.level, this);
    }
}
