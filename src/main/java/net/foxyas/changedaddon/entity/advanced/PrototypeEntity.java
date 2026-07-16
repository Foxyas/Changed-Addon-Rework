package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.entity.ai.LatexAttackType;
import net.foxyas.changedaddon.entity.ai.LatexFavor;
import net.foxyas.changedaddon.entity.ai.goals.prototype.*;
import net.foxyas.changedaddon.entity.api.ICustomPatReaction;
import net.foxyas.changedaddon.entity.api.IDynamicPawColor;
import net.foxyas.changedaddon.entity.api.ItemHandlerHolder;
import net.foxyas.changedaddon.entity.defaults.AbstractCanTameChangedEntityFavors;
import net.foxyas.changedaddon.menu.PrototypeMenu;
import net.foxyas.changedaddon.util.ColorUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.EyeStyle;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.ai.LatexAssimilationDecision;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EntityArmorInvWrapper;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.function.Predicate;

public class PrototypeEntity extends AbstractCanTameChangedEntityFavors implements MenuProvider, ICustomPatReaction, IDynamicPawColor, ItemHandlerHolder {

    // Constants
    public static final int MAX_HARVEST_TIMES = 32;
    private static final TagKey<Item> FORGE_FRUITS = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "fruits"));
    // Fields
    protected final IItemHandlerModifiable hands = new EntityHandsInvWrapper(this);
    protected final ItemStackHandler inv = new ItemStackHandler(9);
    protected final CombinedInvWrapper handsInv = new CombinedInvWrapper(hands, inv);
    protected final CombinedInvWrapper combinedInv = new CombinedInvWrapper(new EntityArmorInvWrapper(this), hands, inv);
    protected int harvestsTimes = 0;
    protected DepositType depositType = DepositType.BOTH;

    // Constructors
    public PrototypeEntity(EntityType<PrototypeEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setPersistenceRequired();
        setUpDefaultBPI();
    }

    // Static methods
    public static void init() {  //Maybe be usefully in future... idfk
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 0f);
        builder.add(Attributes.MOVEMENT_SPEED, 1.05f);
        builder.add(Attributes.MAX_HEALTH, 24);
        builder.add(Attributes.ARMOR, 0);
        builder.add(Attributes.ARMOR_TOUGHNESS, 0);
        builder.add(Attributes.KNOCKBACK_RESISTANCE, 0);
        builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder.add(Attributes.FOLLOW_RANGE, 40);
        builder.add(ForgeMod.SWIM_SPEED.get(), 0.95f);
        return builder;
    }

    public static SlotAccess getSlotAccess(final IItemHandlerModifiable pInventory, final int pSlot, final Predicate<ItemStack> pStackFilter) {
        return new SlotAccess() {
            public @NotNull ItemStack get() {
                return pInventory.getStackInSlot(pSlot).copy();
            }

            public boolean set(@NotNull ItemStack itemStack) {
                if (!pStackFilter.test(itemStack)) {
                    return false;
                } else {
                    pInventory.setStackInSlot(pSlot, itemStack);
                    return true;
                }
            }
        };
    }

    @Nullable
    protected static EquipmentSlot getEquipmentSlot(int pIndex) {
        if (pIndex == 100 + EquipmentSlot.HEAD.getIndex()) {
            return EquipmentSlot.HEAD;
        } else if (pIndex == 100 + EquipmentSlot.CHEST.getIndex()) {
            return EquipmentSlot.CHEST;
        } else if (pIndex == 100 + EquipmentSlot.LEGS.getIndex()) {
            return EquipmentSlot.LEGS;
        } else if (pIndex == 100 + EquipmentSlot.FEET.getIndex()) {
            return EquipmentSlot.FEET;
        } else if (pIndex == 98) {
            return EquipmentSlot.MAINHAND;
        } else {
            return pIndex == 99 ? EquipmentSlot.OFFHAND : null;
        }
    }

    @Override
    public IItemHandler getItemHandler() {
        return combinedInv;
    }

    public IItemHandler getHandsAndInv() {
        return handsInv;
    }

    /**
     * @param stack    ItemStack to insert.
     * @param simulate If true, the insertion is only simulated.
     * @return Remaining ItemStack.
     */
    public ItemStack addToInventory(ItemStack stack, boolean simulate) {///Do not set, for armor {@link Mob#equipItemIfPossible}
        return ItemHandlerHelper.insertItem(handsInv, stack, simulate);
    }

    @Override
    protected float getEquipmentDropChance(@NotNull EquipmentSlot pSlot) {
        return 2;
    }

    @Override
    public void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(15, new PruningOrangeLeavesGoal(this));
        goalSelector.addGoal(15, new FindAndHarvestCropsGoal(this));
        goalSelector.addGoal(20, new PlantSeedsGoal(this));
        goalSelector.addGoal(25, new DepositToChestGoal(this, 8));
        goalSelector.addGoal(30, new TryGrabItemsGoal(this));
        goalSelector.addGoal(30, new ApplyBonemealGoal(this));
    }

    @Override
    public void whenPattedReaction(LivingEntity patter, InteractionHand hand) {
        ICustomPatReaction.super.whenPattedReaction(patter, hand);
        if (patter.level().isClientSide) return;
        if (!(patter instanceof Player player)) {
            return;
        }

        if (!isTame()) {
            tame(player);
            return;
        }

        InteractionResult interactionresult = super.mobInteract(player, hand);
        if ((interactionresult.consumesAction() && !isBaby()) || !isOwnedBy(patter)) return;

        boolean shouldFollow = !isFollowingOwner();
        setFollowOwner(shouldFollow);

        player.displayClientMessage(Component.translatable(shouldFollow ? "text.changed.tamed.follow" : "text.changed.tamed.wander", getDisplayName()), false);
        jumping = false;
        navigation.stop();
        setTarget(null);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Inv", inv.serializeNBT());
        tag.putInt("harvestDone", harvestsTimes);
        tag.putString("DepositType", depositType.toString());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("Inventory")) {//DataFix
            SimpleContainer container = new SimpleContainer(9);
            container.fromTag(tag.getList("Inventory", 10));
            for (int i = 0; i < 9; i++) {
                inv.setStackInSlot(i, container.getItem(i));
            }
        } else inv.deserializeNBT(tag.getCompound("Inv"));

        if (tag.contains("harvestDone")) {
            harvestsTimes = tag.getInt("harvestsTimes");
        }
        if (tag.contains("DepositeType")) {//DataFix
            depositType = DepositType.valueOf(tag.getString("DepositeType").toUpperCase());
        } else if (tag.contains("DepositType")) {
            depositType = DepositType.valueOf(tag.getString("DepositType"));
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return false;
    }

    @Override
    public boolean tryTransfurTarget(Entity entity) {
        return false;
    }

    @Override
    public @Nullable LatexAssimilationDecision<?> makeLatexAssimilationDecision(TransfurCause cause, LivingEntity targetEntity) {
        return null;
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.NONE;
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        Color3 firstColor = Color3.getColor("#AEBBF7");
        Color3 secondColor = Color3.getColor("#71FFFF");
        return ColorUtil.lerpTFColor(firstColor, secondColor, getUnderlyingPlayer());
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        SpawnGroupData ret = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);

        setUpDefaultBPI();
        return ret;
    }

    public void setUpDefaultBPI() {
        getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
        getBasicPlayerInfo().setRightIrisColor(Color3.getColor("#59c5ff"));
        getBasicPlayerInfo().setLeftIrisColor(Color3.getColor("#59c5ff"));
    }

    @Override
    public @NotNull InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 vec, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
        if (interactionresult.consumesAction()) {
            return interactionresult;
        }

        if (isTame()) {
            if (isTameItem(itemstack) && getHealth() < getMaxHealth()) {
                itemstack.shrink(1);
                heal(2.0F);
                if (level() instanceof ServerLevel _level) {
                    _level.sendParticles(ParticleTypes.HEART, (this.getX()), (this.getY() + 1), (this.getZ()), 7, 0.3, 0.3, 0.3, 1); //Spawn Heal Particles
                }
                this.gameEvent(GameEvent.ENTITY_INTERACT, this);
                player.swing(hand);
                return InteractionResult.SUCCESS;
            }
        }

        if (!player.isShiftKeyDown()) {
            if (!level().isClientSide) {
                depositType = depositType.nextDepositType();
                player.displayClientMessage(Component.translatable("entity.changed_addon.prototype.deposit_type.switch", depositType.getFormatedName()), true);
            }
        } else {
            if (!level().isClientSide) {
                NetworkHooks.openScreen((ServerPlayer) player, this, buf -> buf.writeVarInt(getId()));
            }
        }

        player.swing(hand);
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (tickCount % 120 != 0) return;

        if (harvestsTimes >= MAX_HARVEST_TIMES) {
            harvestsTimes = 0;
        }
    }

    @Override
    public boolean isTameItem(ItemStack stack) {
        return stack.is(Tags.Items.INGOTS_IRON);
    }

    @Override
    protected void dropAllDeathLoot(@NotNull DamageSource pDamageSource) {
        super.dropAllDeathLoot(pDamageSource);

        ItemStack stack;
        ItemEntity itemEntity;
        for (int i = 0; i < combinedInv.getSlots(); i++) {
            stack = combinedInv.extractItem(i, combinedInv.getSlotLimit(i), false);
            if (stack.isEmpty()) continue;

            itemEntity = new ItemEntity(level(), getX(), getY() + 0.5, getZ(), stack);
            itemEntity.setDeltaMovement(
                    (random.nextDouble() - 0.5) * 0.2, 0.2, (random.nextDouble() - 0.5) * 0.2
            );
            level().addFreshEntity(itemEntity);
        }
    }

    // Inventory related methods
    @Override
    public boolean canTakeItem(@NotNull ItemStack pItemstack) {
        if (pItemstack.isEmpty()) return false;
        if (canTakeItemNoArmor(pItemstack)) {
            return true;
        }

        EquipmentSlot equipmentslot = getEquipmentSlotForItem(pItemstack);
        return equipmentslot.getType() != EquipmentSlot.Type.HAND && this.getItemBySlot(equipmentslot).isEmpty() && this.canPickUpLoot();
    }

    public boolean canTakeItemNoArmor(@NotNull ItemStack stack) {
        return stack.is(Tags.Items.CROPS) || (stack.is(FORGE_FRUITS) || stack.is(Tags.Items.SEEDS) || stack.is(Tags.Items.SHEARS) || pickAbleItems().contains(stack.getItem()));
    }

    @Override
    public boolean canPickUpLoot() {
        return true;
    }

    @Override
    public boolean canHoldItem(@NotNull ItemStack pStack) {
        return hasSpaceInInvOrHands() && canTakeItem(pStack);
    }

    @Override
    protected void pickUpItem(@NotNull ItemEntity pItemEntity) {
        ItemStack pStack = pItemEntity.getItem();
        if (canTakeItemNoArmor(pStack)) {
            ItemStack remainder = addToInventory(pStack, false);

            if (remainder.isEmpty()) {
                pItemEntity.discard();
            } else pItemEntity.setItem(remainder);
            return;
        }
        super.pickUpItem(pItemEntity);
    }

    @Override
    public boolean canTrample(@NotNull BlockState state, @NotNull BlockPos pos, float fallDistance) {
        return false;
    }

    // compatibility with the "/item replace" command
    @Override
    public @NotNull SlotAccess getSlot(int slot) {
        if (getEquipmentSlot(slot) == null) {
            if (slot >= 0 && slot < inv.getSlots()) {
                return getSlotAccess(inv, slot, (itemStack) -> true);
            }
        }

        return super.getSlot(slot);
    }

    // MenuProvider implementation
    @Override
    public @NotNull Component getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new PrototypeMenu(id, playerInventory, this);
    }

    // Inventory management
    public boolean isInventoryFull() {
        for (int i = 0; i < handsInv.getSlots(); i++) {
            if (handsInv.getStackInSlot(i).isEmpty()) return false;
        }

        return true;
    }

    public boolean hasSpaceInInvOrHands() {
        return !isInventoryFull();
    }

    public boolean wantsToDeposit() {
        int crops = 0;
        ItemStack stack;
        for (int i = 0; i < handsInv.getSlots(); i++) {
            stack = handsInv.getStackInSlot(i);
            if (depositType.test(stack)) crops++;
            if (crops >= 4) return true;
        }

        return false;
    }

    // Getters and setters
    public List<Item> pickAbleItems() {
        return List.of(Items.BONE_MEAL, Items.SHEARS);
    }

    public DepositType getDepositType() {
        return depositType;
    }

    public void setDepositType(DepositType depositType) {
        this.depositType = depositType;
    }

    public int getHarvestsTimes() {
        return this.harvestsTimes;
    }

    public void setHarvestsTimes(int harvestsTimes) {
        this.harvestsTimes = harvestsTimes;
    }

    public void addHarvestsTime() {
        this.harvestsTimes++;
    }

    @Override
    public Color getPawBeansColor() {
        return Color.CYAN;
    }

    @Override
    public boolean canDoFavor(LatexFavor favor) {
        if (favor == LatexFavor.SUIT_OWNER) {
            return false;
        }
        return super.canDoFavor(favor);
    }

    @Override
    public LatexAttackType getAttackType() {
        return LatexAttackType.ALWAYS_KILL;
    }

    // Enums
    public enum DepositType implements Predicate<ItemStack> {
        SEEDS(Tags.Items.SEEDS),
        CROPS(FORGE_FRUITS, Tags.Items.CROPS),
        BOTH(FORGE_FRUITS, Tags.Items.CROPS, Tags.Items.SEEDS);

        final List<TagKey<Item>> tagKeys;

        DepositType(TagKey<Item> crops, TagKey<Item> seeds) {
            this.tagKeys = List.of(crops, seeds);
        }

        DepositType(TagKey<Item> typeTag) {
            this.tagKeys = List.of(typeTag);
        }

        DepositType(TagKey<Item> fruits, TagKey<Item> crops, TagKey<Item> seeds) {
            this.tagKeys = List.of(fruits, crops, seeds);
        }

        public List<TagKey<Item>> getTagKeys() {
            return tagKeys;
        }

        public String getFormatedName() {
            String normalName = name();
            String lowerCaseName = name().substring(1).toLowerCase();
            return normalName.toUpperCase().charAt(0) + lowerCaseName;
        }

        public boolean test(ItemStack stack) {
            return tagKeys.stream().anyMatch(stack::is);
        }

        public DepositType nextDepositType() {
            int next = ordinal() + 1;
            DepositType[] types = values();
            return next >= types.length ? types[0] : types[next];
        }

        public boolean willDepositSeeds() {
            return this == SEEDS || this == BOTH;
        }
    }
}
