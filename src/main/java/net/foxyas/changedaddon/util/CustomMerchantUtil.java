package net.foxyas.changedaddon.util;

import net.foxyas.changedaddon.menu.CustomMerchantOffer;
import net.foxyas.changedaddon.menu.CustomMerchantOffers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public final class CustomMerchantUtil {

    public static <T extends LivingEntity> CustomMerchantOffers makeOffers(T merchant, List<Function<T, CustomMerchantOffer>> buyOffers, int maxBuyOffers, List<Function<T, CustomMerchantOffer>> sellOffers, int maxSellOffers){
        CustomMerchantOffers offers = new CustomMerchantOffers();
        Random random = merchant.getRandom();

        List<Function<T, CustomMerchantOffer>> possibleOffers = new ArrayList<>(buyOffers);
        Collections.shuffle(possibleOffers, random);

        for(int i = 0; i < Math.min(maxBuyOffers, possibleOffers.size()); i++){
            offers.add(possibleOffers.get(i).apply(merchant));
        }

        possibleOffers.clear();
        possibleOffers.addAll(sellOffers);
        Collections.shuffle(possibleOffers, random);

        for(int i = 0; i < Math.min(maxSellOffers, possibleOffers.size()); i++){
            offers.add(possibleOffers.get(i).apply(merchant));
        }

        return offers;
    }

    public static ItemStack defStack(RegistryObject<? extends ItemLike> item){
        return item.get().asItem().getDefaultInstance();
    }

    public static Ingredient emeralds(int count){
        return NBTIngredient.of(new ItemStack(Items.EMERALD, count));
    }

    public static Ingredient single(ItemLike item){
        return Ingredient.of(item);
    }

    public static Ingredient single(RegistryObject<? extends ItemLike> item){
        return Ingredient.of(item.get());
    }

    public static Ingredient pairWithCount(ItemLike first, ItemLike second, int count){
        return CompoundIngredient.of(withCount(first, count), withCount(second, count));
    }

    public static Ingredient withCount(ItemLike item, int count){
        return NBTIngredient.of(new ItemStack(item, count));
    }

    public static Ingredient pairWithCount(RegistryObject<? extends ItemLike> first, RegistryObject<? extends ItemLike> second, int count){
        return CompoundIngredient.of(withCount(first, count), withCount(second, count));
    }

    public static Ingredient withCount(RegistryObject<? extends ItemLike> item, int count){
        return NBTIngredient.of(new ItemStack(item.get(), count));
    }
}
