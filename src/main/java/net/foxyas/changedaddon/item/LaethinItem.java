package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.item.api.IDynamicCreativeTab;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class LaethinItem extends Item implements IDynamicCreativeTab {

    public enum Type implements StringRepresentable {
        WHITE_LATEX("white_latex"),
        DARK_LATEX("dark_latex");

        private final String serializedName;

        Type(String serializedName) {
            this.serializedName = serializedName;
        }

        @Override
        public @NotNull String getSerializedName() {
            return serializedName;
        }
    }

    public LaethinItem() {
        super(new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .stacksTo(64).rarity(Rarity.RARE));
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack defaultInstance = super.getDefaultInstance();
        setLaethinTypeForStack(defaultInstance, Type.WHITE_LATEX);
        return defaultInstance;
    }

    @Override
    public void fillItemCategory(CreativeModeTab.@NotNull Output tab) {
        for (Type type : Type.values()) {
            ItemStack stack = new ItemStack(this);
            setLaethinTypeForStack(stack, type);
            tab.accept(stack);
        }
    }

    public static void setLaethinTypeForStack(ItemStack itemStack, Type type) {
        itemStack.getOrCreateTag().putString("type", type.serializedName);
    }

    public static float getLaethinTypeOfStack(ItemStack itemstack) {
        if (itemstack.getOrCreateTag().getString("type").equals("dark_latex")) {
            return 1;
        }
        return 0;
    }
}
