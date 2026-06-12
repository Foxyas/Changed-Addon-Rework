package net.foxyas.changedaddon.item.clothes;

import net.foxyas.changedaddon.item.api.ColorHolder;
import net.foxyas.changedaddon.item.api.IDynamicCreativeTab;
import net.ltxprogrammer.changed.item.ClothingItem;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class DyeableClothingItem extends ClothingItem implements DyeableLeatherItem, ColorHolder, IDynamicCreativeTab {

    public DyeableClothingItem() {
        super();
        CauldronInteraction.WATER.put(this, CauldronInteraction.DYED_ITEM);
    }

    public void fillItemCategory(@NotNull CreativeModeTab.Output tab) {
        for (DefaultColors color : DefaultColors.values()) {
            ItemStack stack = new ItemStack(this);
            this.setColor(stack, color.getColorToInt());
            tab.accept(stack);
        }
    }

    @Override
    public int getColor(ItemStack pStack) {
        CompoundTag tag = pStack.getTagElement("display");
        return tag != null && tag.contains("color", 99) ? tag.getInt("color") : 0xffffff;
    }

    @Override
    public void registerCustomColors(RegisterColorHandlersEvent.Item event, RegistryObject<Item> item) {
        if (item.get() instanceof DyeableClothingItem) {
            event.register((stack, layer) -> getColor(stack), item.get());
        }
    }

    public enum DefaultColors {
        RED(new Color(255, 0, 0)),
        GREEN(new Color(0, 255, 0)),
        BLUE(new Color(0, 0, 255)),
        YELLOW(new Color(255, 255, 0)),
        CYAN(new Color(0, 255, 255)),
        MAGENTA(new Color(255, 0, 255)),
        ORANGE(new Color(255, 165, 0)),
        PINK(new Color(255, 105, 180)),
        WHITE(new Color(255, 255, 255));

        public final Color color;

        DefaultColors(Color color) {
            this.color = color;
        }

        // Construtor sem argumentos, caso queira usar valores padrão depois
        DefaultColors() {
            this.color = new Color(255, 255, 255); // fallback: branco
        }

        public Color getColor() {
            return color;
        }

        public int getColorToInt() {
            return color.getRGB();
        }
    }
}