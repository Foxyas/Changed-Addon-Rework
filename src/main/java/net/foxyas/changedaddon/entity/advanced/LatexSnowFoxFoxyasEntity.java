package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.entity.defaults.AbstractTraderChangedEntityWithInventory;
import net.foxyas.changedaddon.entity.goals.generic.LookAndFollowTradingPlayerSink;
import net.foxyas.changedaddon.entity.goals.generic.TradeWithPlayerGoal;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.item.clothes.DyeableClothingItem;
import net.foxyas.changedaddon.item.clothes.DyeableShortsItem;
import net.foxyas.changedaddon.menu.CustomMerchantOffer;
import net.foxyas.changedaddon.menu.CustomMerchantOffers;
import net.foxyas.changedaddon.menu.FoxyasInventoryMenu;
import net.foxyas.changedaddon.util.CustomMerchantUtil;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.init.ChangedAccessorySlots;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static net.foxyas.changedaddon.util.CustomMerchantUtil.*;

public class LatexSnowFoxFoxyasEntity extends AbstractTraderChangedEntityWithInventory {

    public static final float FOXYAS_SCALE = 0.85f;
    private static final List<Function<LatexSnowFoxFoxyasEntity, CustomMerchantOffer>> buyOffers = List.of(
            (entity) ->
                    new CustomMerchantOffer(withCount(ChangedItems.ORANGE, 3), single(Items.GLASS_BOTTLE), defStack(ChangedAddonItems.ORANGE_JUICE), 16)
    );
    private static final List<Function<LatexSnowFoxFoxyasEntity, CustomMerchantOffer>> sellOffers = List.of(
            (entity) ->
                    new CustomMerchantOffer(emeralds(4), defStack(ChangedAddonItems.OPENED_CANNED_SOUP), 16),
            (entity) ->
                    new CustomMerchantOffer(emeralds(6), defStack(ChangedAddonItems.FOXTA), 6),
            (entity) ->
                    new CustomMerchantOffer(emeralds(8), defStack(ChangedAddonItems.SNEPSI), 4),
            (entity) ->
                    new CustomMerchantOffer(emeralds(12), defStack(ChangedAddonItems.GOLDEN_ORANGE), 8)
    );

    public LatexSnowFoxFoxyasEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.LATEX_SNOW_FOX_FOXYAS.get(), world);
    }

    public LatexSnowFoxFoxyasEntity(EntityType<LatexSnowFoxFoxyasEntity> type, Level world) {
        super(type, world, 36);
        xpReward = 10;
        setNoAi(false);
        setPersistenceRequired();
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

    protected CustomMerchantOffers makeOffers() {
        return CustomMerchantUtil.makeOffers(this, buyOffers, buyOffers.size(), sellOffers, sellOffers.size());
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.NONE;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(3, new LookAndFollowTradingPlayerSink(this, 0.25f));
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    public void die(@NotNull DamageSource source) {
        super.die(source);

        if (source.getEntity() instanceof ServerPlayer player) {
            Advancement _adv = player.server.getAdvancements().getAdvancement(ResourceLocation.parse("changed_addon:foxyas_advancement"));
            assert _adv != null;
            AdvancementProgress _ap = player.getAdvancements().getOrStartProgress(_adv);
            if (!_ap.isDone()) {
                for (String s : _ap.getRemainingCriteria()) player.getAdvancements().award(_adv, s);
            }
        }
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return false;
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (getOffers().isEmpty() || player.isShiftKeyDown()) {
            if (level.isClientSide) return InteractionResult.SUCCESS;
            NetworkHooks.openScreen((ServerPlayer) player, getMenuProvider(), buf -> buf.writeVarInt(getId()));
            return InteractionResult.CONSUME;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public float getScale() {
        return super.getScale() * FOXYAS_SCALE;
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inv, @NotNull Player player) {
        if (player.isShiftKeyDown()) {
            return new FoxyasInventoryMenu(containerId, inv, this);
        }
        return super.createMenu(containerId, inv, player);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        SpawnGroupData spawnGroupData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        this.setDefaultClothing();
        return spawnGroupData;
    }

    public void setDefaultClothing() {
        Optional<AccessorySlots> accessorySlots = AccessorySlots.getForEntity(this);
        accessorySlots.ifPresent((slots) -> {
            if (slots.hasSlot(ChangedAccessorySlots.BODY.get())) {
                Optional<ItemStack> item = slots.getItem(ChangedAccessorySlots.BODY.get());
                if (item.isEmpty() || item.get().isEmpty()) {
                    ItemStack stack = new ItemStack(ChangedAddonItems.DYEABLE_TSHIRT.get());
                    if (stack.getItem() instanceof DyeableClothingItem dyeableShorts) {
                        boolean flag = dyeableShorts.allowedInSlot(stack, this, ChangedAccessorySlots.BODY.get());
                        DyeableClothingItem.DefaultColors color = Util.getRandom(DyeableClothingItem.DefaultColors.values(), this.random);
                        dyeableShorts.setColor(stack, color.getColorToInt());
                        if (flag) slots.setItem(ChangedAccessorySlots.BODY.get(), stack);
                    }
                }
            }
            if (slots.hasSlot(ChangedAccessorySlots.LEGS.get())) {
                Optional<ItemStack> item = slots.getItem(ChangedAccessorySlots.LEGS.get());
                if (item.isEmpty() || item.get().isEmpty()) {
                    ItemStack stack = new ItemStack(ChangedAddonItems.DYEABLE_SHORTS.get());
                    if (stack.getItem() instanceof DyeableShortsItem dyeableShortsItem) {
                        boolean flag = dyeableShortsItem.allowedInSlot(stack, this, ChangedAccessorySlots.LEGS.get());
                        DyeableClothingItem.DefaultColors color = Util.getRandom(DyeableClothingItem.DefaultColors.values(), this.random);
                        dyeableShortsItem.setColor(stack, color.getColorToInt());
                        if (flag) slots.setItem(ChangedAccessorySlots.LEGS.get(), stack);
                    }
                }
            }
        });
    }

    @Override
    public void baseTick() {
        super.baseTick();
    }
}
