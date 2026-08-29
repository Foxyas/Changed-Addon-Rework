package net.foxyas.changedaddon.menu;

import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

public class CustomMerchantOffer {

    /**
     * The first input for this offer.
     */
    private final Ingredient costA;
    /**
     * The second input for this offer.
     */
    private final Ingredient costB;
    private final ItemStack result;
    private final int maxUses;
    private int uses;
    private boolean shouldRewardExp = true;
    private int specialPriceDiff;
    private float discountMultiplier = 0.05f;

    public CustomMerchantOffer(FriendlyByteBuf buf) {
        costA = Ingredient.fromNetwork(buf);
        result = buf.readItem();
        if (buf.readBoolean()) {
            costB = Ingredient.fromNetwork(buf);
        } else costB = Ingredient.EMPTY;

        uses = buf.readVarInt();
        maxUses = buf.readVarInt();
        specialPriceDiff = buf.readVarInt();
    }

    public CustomMerchantOffer(CompoundTag tag) {
        costA = Ingredient.fromJson(NbtOps.INSTANCE.convertTo(JsonOps.COMPRESSED, tag.get("costA")));
        if (tag.contains("costB")) {
            costB = Ingredient.fromJson(NbtOps.INSTANCE.convertTo(JsonOps.COMPRESSED, tag.get("costB")));
        } else costB = Ingredient.EMPTY;

        result = ItemStack.of(tag.getCompound("sell"));
        uses = tag.getInt("uses");
        if (tag.contains("maxUses", 99)) {
            maxUses = tag.getInt("maxUses");
        } else {
            maxUses = 4;
        }

        if (tag.contains("shouldRewardExp")) shouldRewardExp = tag.getBoolean("shouldRewardExp");
        discountMultiplier = tag.getFloat("priceMultiplier");
    }

    public CustomMerchantOffer(Ingredient costA, ItemStack result, int maxUses) {
        this(costA, Ingredient.EMPTY, result, maxUses, 0.05f);
    }

    public CustomMerchantOffer(Ingredient costA, ItemStack result, int maxUses, float discountMultiplier) {
        this(costA, Ingredient.EMPTY, result, maxUses, discountMultiplier);
    }

    public CustomMerchantOffer(Ingredient costA, Ingredient costB, ItemStack result, int maxUses) {
        this(costA, costB, result, maxUses, 0.05f);
    }

    public CustomMerchantOffer(Ingredient costA, Ingredient costB, ItemStack result, int maxUses, float discountMultiplier) {
        this.costA = costA;
        this.costB = costB;
        this.result = result;
        this.maxUses = maxUses;
        this.discountMultiplier = discountMultiplier;
    }

    public Ingredient getCostA() {
        return costA;
    }

    public Ingredient getCostB() {
        return costB;
    }

    public ItemStack getResult() {
        return result;
    }

    public ItemStack assemble() {
        return result.copy();
    }

    public int getUses() {
        return uses;
    }

    public int getUsesLeft() {
        return Math.max(maxUses - uses, 0);
    }

    public void resetUses() {
        uses = 0;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public void increaseUses() {
        ++uses;
    }

    public boolean isOutOfStock() {
        return uses >= maxUses;
    }

    public void setToOutOfStock() {
        uses = maxUses;
    }

    public boolean needsRestock() {
        return uses > 0;
    }

    public boolean shouldRewardExp() {
        return shouldRewardExp;
    }

    public void setSpecialPriceDiff(int value) {
        specialPriceDiff = value;
    }

    public float getDiscountMultiplier() {
        return discountMultiplier;
    }

    public int specialPriceCount(ItemStack required) {
        return Mth.clamp(required.getCount() /*+ demand*/ + this.specialPriceDiff, 1, required.getMaxStackSize());
    }

    public boolean satisfiedBy(ItemStack playerOfferA, ItemStack playerOfferB) {
        if (!testWithCount(costA, playerOfferA)) return false;

        return costB.isEmpty() || testWithCount(costB, playerOfferB);
    }

    private boolean testWithCount(Ingredient ingredient, ItemStack stack) {
        if (ingredient instanceof StrictNBTIngredient strict) {
            return ingredient.test(stack) && specialPriceCount(strict.getItems()[0]) <= stack.getCount();
        }

        return ingredient.test(stack);
    }

    private boolean isRequiredItem(ItemStack offer, ItemStack cost) {
        if (cost.isEmpty() && offer.isEmpty()) return true;

        ItemStack itemstack = offer.copy();
        if (itemstack.getItem().isDamageable(itemstack)) {
            itemstack.setDamageValue(itemstack.getDamageValue());
        }

        return ItemStack.isSameItem(itemstack, cost) && (!cost.hasTag() || itemstack.hasTag() && NbtUtils.compareNbt(cost.getTag(), itemstack.getTag(), false));
    }

    public boolean take(ItemStack playerOfferA, ItemStack playerOfferB) {
        if (!satisfiedBy(playerOfferA, playerOfferB)) return false;

        for (ItemStack stack : costA.getItems()) {
            if (isRequiredItem(playerOfferA, stack)) playerOfferA.shrink(specialPriceCount(stack));
        }

        if (!costB.isEmpty()) {
            for (ItemStack stack : costB.getItems()) {
                if (isRequiredItem(playerOfferB, stack)) playerOfferB.shrink(specialPriceCount(stack));
            }
        }

        return true;
    }

    public void writeToStream(FriendlyByteBuf buf) {
        costA.toNetwork(buf);
        buf.writeItem(result);

        buf.writeBoolean(!costB.isEmpty());
        if (!costB.isEmpty()) {
            costB.toNetwork(buf);
        }

        buf.writeVarInt(uses);
        buf.writeVarInt(maxUses);
        buf.writeVarInt(specialPriceDiff);
    }

    public CompoundTag createTag() {
        CompoundTag tag = new CompoundTag();

        tag.put("costA", JsonOps.COMPRESSED.convertTo(NbtOps.INSTANCE, costA.toJson()));
        if (!costB.isEmpty()) tag.put("costB", JsonOps.COMPRESSED.convertTo(NbtOps.INSTANCE, costB.toJson()));

        tag.put("sell", result.save(new CompoundTag()));
        tag.putInt("uses", uses);
        tag.putInt("maxUses", maxUses);
        tag.putBoolean("shouldRewardExp", shouldRewardExp);
        tag.putFloat("priceMultiplier", discountMultiplier);
        return tag;
    }
}
