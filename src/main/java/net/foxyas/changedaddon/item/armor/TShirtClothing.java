package net.foxyas.changedaddon.item.armor;

import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class TShirtClothing extends DyeableClothingItem {

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

    /*public enum ShirtType {
        TYPE1("type1"),
        TYPE2("type2");

        private final String id;

        ShirtType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public static ShirtType fromString(String name) {
            for (ShirtType type : values()) {
                if (type.id.equalsIgnoreCase(name)) return type;
            }
            return TYPE1; // default
        }
    }*/

    public TShirtClothing() {
        super();
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return super.canEquip(stack, armorType, entity);
    }

    public SoundEvent getEquipSound() {
        return ChangedSounds.EQUIP3;
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        this.setColor(stack, Color3.WHITE.toInt());
        return stack;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return super.getArmorTexture(stack, entity, slot, type);
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientInitializer {
        @SubscribeEvent
        public static void onItemColorsInit(ColorHandlerEvent.Item event) {
            event.getItemColors().register(
                    (stack, layer) -> ((DyeableLeatherItem)stack.getItem()).getColor(stack),
                    ChangedAddonItems.DYEABLE_SHIRT.get());
        }
    }

    /*public static ShirtType getShirtType(ItemStack stack) {
        String tag = stack.getOrCreateTag().getString("Style");
        return ShirtType.fromString(tag);
    }

    public static void setShirtType(ItemStack stack, ShirtType type) {
        stack.getOrCreateTag().putString("Style", type.getId());
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        ShirtType shirtType = getShirtType(stack);

        if (shirtType == ShirtType.TYPE2) {
            if ("overlay".equals(type)) {
                return "changed_additions:textures/models/armor/t_shirt_type_2_layer_1_overlay.png";
            }
            return "changed_additions:textures/models/armor/t_shirt_type_2_layer_1.png";
        }

        // Default TYPE1
        if ("overlay".equals(type)) {
            return "changed_additions:textures/models/armor/t_shirt_layer_1_overlay.png";
        }
        return "changed_additions:textures/models/armor/t_shirt_layer_1.png";
    }*/
}