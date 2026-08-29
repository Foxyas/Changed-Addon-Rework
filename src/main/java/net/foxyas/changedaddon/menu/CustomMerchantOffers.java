package net.foxyas.changedaddon.menu;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

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

    @Nullable
    public CustomMerchantOffer getRecipeFor(ItemStack stackA, ItemStack stackB, int index) {
        if (index > 0 && index < size()) {
            CustomMerchantOffer offer = get(index);
            return offer.satisfiedBy(stackA, stackB) ? offer : null;
        } else {
            for (CustomMerchantOffer offer : this) {
                if (offer.satisfiedBy(stackA, stackB)) {
                    return offer;
                }
            }

            return null;
        }
    }

    public static CustomMerchantOffers createFromStream(FriendlyByteBuf buf) {
        CustomMerchantOffers offers = new CustomMerchantOffers();
        int i = buf.readByte() & 255;

        for (int j = 0; j < i; ++j) {
            offers.add(new CustomMerchantOffer(buf));
        }

        return offers;
    }

    public void writeToStream(FriendlyByteBuf buf) {
        buf.writeByte((byte) (this.size() & 255));

        for (CustomMerchantOffer offer : this) {
            offer.writeToStream(buf);
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
