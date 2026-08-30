package net.foxyas.changedaddon.entity.defaults;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.foxyas.changedaddon.ChangedAddonMod;
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
import net.minecraft.util.Mth;
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
import java.util.UUID;
import java.util.function.Function;

public abstract class AbstractTraderChangedEntity extends ChangedEntity implements CustomMerchant, MenuProvider {

    private static final List<Function<AbstractTraderChangedEntity, CustomMerchantOffer>> buyOffers = List.of(
    );

    private static final List<Function<AbstractTraderChangedEntity, CustomMerchantOffer>> sellOffers = List.of(
    );

    protected Player tradingPlayer;
    protected CustomMerchantOffers offers = new CustomMerchantOffers();
    protected long nextOfferReset;
    protected final Object2IntMap<UUID> tradeValues = new Object2IntOpenHashMap<>();
    protected long nextTradeValuesDecay;

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
            updatePersonalizedPrices(player);
            setTradingPlayer(player);
            NetworkHooks.openScreen((ServerPlayer) player, this, buf -> offers.writeToStream(buf));
        }

        return InteractionResult.CONSUME;
    }

    protected void updatePersonalizedPrices(Player player) {
        int i = tradeValues.getOrDefault(player.getUUID(), 0);
        if (i != 0) {
            for (CustomMerchantOffer offer : getOffers()) {
                offer.setSpecialPriceDiff(-Mth.floor(i * offer.getDiscountMultiplier()));
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        long time = level.getGameTime();
        if (time >= nextTradeValuesDecay) {
            nextTradeValuesDecay = time + 24000;
            if (tradeValues.isEmpty()) return;

            tradeValues.replaceAll((uuid, value) -> value - 2);
            tradeValues.values().removeIf(value -> value <= 0);
        }
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

        if (!tradeValues.isEmpty()) {
            CompoundTag map = new CompoundTag();
            for (Object2IntMap.Entry<UUID> entry : tradeValues.object2IntEntrySet()) {
                map.putInt(entry.getKey().toString(), entry.getIntValue());
            }
            tag.put("tradeValues", map);
        }

        tag.putLong("nextOfferReset", nextOfferReset);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Offers", 10)) {
            offers = new CustomMerchantOffers(tag.getCompound("Offers"));
        }

        if (tag.contains("tradeValues")) {
            tradeValues.clear();
            CompoundTag map = tag.getCompound("tradeValues");
            UUID uuid;
            for (String str : map.getAllKeys()) {
                try {
                    uuid = UUID.fromString(str);
                } catch (IllegalArgumentException e) {
                    ChangedAddonMod.LOGGER.warn("Failed to parse uuid {}", str);
                    continue;
                }
                tradeValues.put(uuid, map.getInt(str));
            }
        }

        nextOfferReset = tag.getLong("nextOfferReset");
    }

    @Override
    public @Nullable Player getTradingPlayer() {
        return tradingPlayer;
    }

    @Override
    public void setTradingPlayer(@Nullable Player tradingPlayer) {
        if (this.tradingPlayer != null && tradingPlayer == null) {
            getOffers().forEach(offer -> offer.setSpecialPriceDiff(0));
        }
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

        if (tradingPlayer != null) {
            tradeValues.mergeInt(tradingPlayer.getUUID(), 2, (old, new_) -> Math.min(25, old + new_));
        }

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
