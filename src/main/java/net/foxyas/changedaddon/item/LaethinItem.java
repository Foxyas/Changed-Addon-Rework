package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.item.api.IDynamicCreativeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LaethinItem extends Item implements IDynamicCreativeTab {

    public enum Type implements StringRepresentable {
        WHITE_LATEX("white_latex", 0f),
        DARK_LATEX("dark_latex", 1f);

        private final String serializedName;
        private final float value;

        Type(String serializedName, float value) {
            this.serializedName = serializedName;
            this.value = value;
        }

        @Override
        public @NotNull String getSerializedName() {
            return serializedName;
        }

        public Component getFormatedName() {
            return Component.translatable("item.changed_addon.laethin.type", serializedName);
        }

        public float getValue() {
            return value;
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

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        LaethinItem.Type laethinTypeOfStack = getLaethinTypeOfStack(pStack);
        pTooltipComponents.add(laethinTypeOfStack.getFormatedName());
    }

    public static void setLaethinTypeForStack(ItemStack itemStack, Type type) {
        itemStack.getOrCreateTag().putString("type", type.serializedName);
    }

    public static Type getLaethinTypeOfStack(ItemStack itemstack) {
        if (itemstack.getOrCreateTag().getString("type").equals("dark_latex")) {
            return Type.DARK_LATEX;
        }
        return Type.WHITE_LATEX;
    }
}
