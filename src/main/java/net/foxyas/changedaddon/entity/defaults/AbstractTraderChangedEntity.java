package net.foxyas.changedaddon.entity.defaults;

import net.foxyas.changedaddon.entity.api.CustomMerchant;
import net.foxyas.changedaddon.menu.CustomMerchantMenu;
import net.foxyas.changedaddon.menu.CustomMerchantOffer;
import net.foxyas.changedaddon.menu.CustomMerchantOffers;
import net.foxyas.changedaddon.util.CustomMerchantUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public abstract class AbstractTraderChangedEntity extends ChangedEntity implements CustomMerchant, MenuProvider {

    private static final List<Function<AbstractTraderChangedEntity, CustomMerchantOffer>> buyOffers = List.of(
    );

    private static final List<Function<AbstractTraderChangedEntity, CustomMerchantOffer>> sellOffers = List.of(
    );

    protected Player tradingPlayer;
    protected CustomMerchantOffers offers = new CustomMerchantOffers();
    protected long nextOfferReset;

    protected AbstractTraderChangedEntity(EntityType<? extends AbstractTraderChangedEntity> type, Level level) {
        super(type, level);
        offers = makeOffers();
        calculateNextReset();
    }

    public static LootTable.@NotNull Builder getLoot() {
        return LootTable.lootTable();
    }

    protected CustomMerchantOffers makeOffers() {
        return CustomMerchantUtil.makeOffers(this, buyOffers, 2, sellOffers, 2);
    }

    protected void calculateNextReset() {
        nextOfferReset = level.getGameTime() + 48000;
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (level.isClientSide || getTradingPlayer() != null) return InteractionResult.SUCCESS;

        if (level.getGameTime() >= nextOfferReset) {
            offers = makeOffers();
            calculateNextReset();
        }

        if (!getOffers().isEmpty()) {
            setTradingPlayer(player);
            NetworkHooks.openScreen((ServerPlayer) player, this, buf -> offers.writeToStream(buf));
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Custom Merchant Template");
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inv, @NotNull Player player) {
        return new CustomMerchantMenu(containerId, inv, this);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!offers.isEmpty()) {
            tag.put("Offers", offers.createTag());
        }

        tag.putLong("nextOfferReset", nextOfferReset);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Offers", 10)) {
            offers = new CustomMerchantOffers(tag.getCompound("Offers"));
        }

        nextOfferReset = tag.getLong("nextOfferReset");
    }

    @Override
    public @Nullable Player getTradingPlayer() {
        return tradingPlayer;
    }

    @Override
    public void setTradingPlayer(@Nullable Player tradingPlayer) {
        this.tradingPlayer = tradingPlayer;
    }

    @Override
    public @NotNull CustomMerchantOffers getOffers() {
        return offers;
    }

    @Override
    public void overrideOffers(@NotNull CustomMerchantOffers offers) {
    }

    @Override
    public void notifyTrade(CustomMerchantOffer offer) {
        offer.increaseUses();

        if (offer.shouldRewardExp()) {
            level.addFreshEntity(new ExperienceOrb(level, getX(), getY() + 0.5D, getZ(), 3 + random.nextInt(4)));
        }
    }

    @Override
    public void notifyTradeUpdated(@NotNull ItemStack stack) {
        //play sound
    }

    @Override
    public @NotNull SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }

    @Override
    public boolean isClientSide() {
        return level.isClientSide;
    }
}
