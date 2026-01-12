package net.foxyas.changedaddon.menu;

import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

public class CustomMerchantOffer {

    /** The first input for this offer. */
    private final Ingredient costA;
    /** The second input for this offer. */
    private final Ingredient costB;
    private final ItemStack result;
    private int uses;
    private final int maxUses;
    private boolean shouldRewardExp = true;

    public CustomMerchantOffer(CompoundTag tag) {
        costA = Ingredient.fromJson(NbtOps.INSTANCE.convertTo(JsonOps.COMPRESSED, tag.get("costA")));
        if(tag.contains("costB")){
            costB = Ingredient.fromJson(NbtOps.INSTANCE.convertTo(JsonOps.COMPRESSED, tag.get("costB")));
        } else costB = Ingredient.EMPTY;

        result = ItemStack.of(tag.getCompound("sell"));
        uses = tag.getInt("uses");
        if (tag.contains("maxUses", 99)) {
            maxUses = tag.getInt("maxUses");
        } else {
            maxUses = 4;
        }

        if(tag.contains("shouldRewardExp")) shouldRewardExp = tag.getBoolean("shouldRewardExp");
    }

    public CustomMerchantOffer(Ingredient costA, ItemStack result, int maxUses) {
        this(costA, Ingredient.EMPTY, result, 0, maxUses);
    }

    public CustomMerchantOffer(Ingredient costA, Ingredient costB, ItemStack result, int maxUses) {
        this(costA, costB, result, 0, maxUses);
    }

    public CustomMerchantOffer(Ingredient costA, Ingredient costB, ItemStack result, int uses, int maxUses) {
        this.costA = costA;
        this.costB = costB;
        this.result = result;
        this.uses = uses;
        this.maxUses = maxUses;
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

    public int getUsesLeft(){
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

    public CompoundTag createTag() {
        CompoundTag tag = new CompoundTag();

        tag.put("costA", JsonOps.COMPRESSED.convertTo(NbtOps.INSTANCE, costA.toJson()));
        if(!costB.isEmpty()) tag.put("costB", JsonOps.COMPRESSED.convertTo(NbtOps.INSTANCE, costB.toJson()));

        tag.put("sell", result.save(new CompoundTag()));
        tag.putInt("uses", uses);
        tag.putInt("maxUses", maxUses);
        tag.putBoolean("shouldRewardExp", shouldRewardExp);
        return tag;
    }

    public boolean satisfiedBy(ItemStack playerOfferA, ItemStack playerOfferB) {
        if(!testWithCount(costA, playerOfferA)) return false;

        return costB.isEmpty() || testWithCount(costB, playerOfferB);
    }

    private boolean testWithCount(Ingredient ingredient, ItemStack stack) {
        if (ingredient instanceof StrictNBTIngredient strict) {
            return ingredient.test(stack) && strict.getItems()[0].getCount() <= stack.getCount();
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

        for(ItemStack stack : costA.getItems()){
            if(isRequiredItem(playerOfferA, stack)) playerOfferA.shrink(stack.getCount());
        }

        if(!costB.isEmpty()){
            for(ItemStack stack : costB.getItems()){
                if(isRequiredItem(playerOfferB, stack)) playerOfferB.shrink(stack.getCount());
            }
        }

        return true;
    }
}
