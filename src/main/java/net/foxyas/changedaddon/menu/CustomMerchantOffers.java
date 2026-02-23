package net.foxyas.changedaddon.menu;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CustomMerchantOffers extends ArrayList<CustomMerchantOffer> {

    public CustomMerchantOffers() {
    }

    public CustomMerchantOffers(CompoundTag tag) {
        ListTag listtag = tag.getList("Recipes", 10);

        for (int i = 0; i < listtag.size(); ++i) {
            add(new CustomMerchantOffer(listtag.getCompound(i)));
        }
    }

    public static CustomMerchantOffers createFromStream(FriendlyByteBuf buf) {
        CustomMerchantOffers merchantoffers = new CustomMerchantOffers();
        int i = buf.readByte() & 255;

        for (int j = 0; j < i; ++j) {
            Ingredient costA = Ingredient.fromNetwork(buf);
            ItemStack result = buf.readItem();
            Ingredient costB = Ingredient.EMPTY;
            if (buf.readBoolean()) {
                costB = Ingredient.fromNetwork(buf);
            }

            boolean flag = buf.readBoolean();
            int k = buf.readVarInt();
            int l = buf.readVarInt();
            CustomMerchantOffer merchantoffer = new CustomMerchantOffer(costA, costB, result, k, l);
            if (flag) {
                merchantoffer.setToOutOfStock();
            }

            merchantoffers.add(merchantoffer);
        }

        return merchantoffers;
    }

    @Nullable
    public CustomMerchantOffer getRecipeFor(ItemStack stackA, ItemStack stackB, int index) {
        if (index > 0 && index < size()) {
            CustomMerchantOffer merchantoffer1 = get(index);
            return merchantoffer1.satisfiedBy(stackA, stackB) ? merchantoffer1 : null;
        } else {
            for (CustomMerchantOffer merchantoffer : this) {
                if (merchantoffer.satisfiedBy(stackA, stackB)) {
                    return merchantoffer;
                }
            }

            return null;
        }
    }

    public void writeToStream(FriendlyByteBuf buf) {
        buf.writeByte((byte) (this.size() & 255));

        for (CustomMerchantOffer merchantoffer : this) {
            merchantoffer.getCostA().toNetwork(buf);
            buf.writeItem(merchantoffer.getResult());
            Ingredient costB = merchantoffer.getCostB();
            buf.writeBoolean(!costB.isEmpty());
            if (!costB.isEmpty()) {
                costB.toNetwork(buf);
            }

            buf.writeBoolean(merchantoffer.isOutOfStock());
            buf.writeVarInt(merchantoffer.getUses());
            buf.writeVarInt(merchantoffer.getMaxUses());
        }
    }

    public CompoundTag createTag() {
        CompoundTag compoundtag = new CompoundTag();
        ListTag listtag = new ListTag();

        for (int i = 0; i < this.size(); ++i) {
            CustomMerchantOffer merchantoffer = this.get(i);
            listtag.add(merchantoffer.createTag());
        }

        compoundtag.put("Recipes", listtag);
        return compoundtag;
    }
}
