package net.foxyas.changedaddon.item.armor;

import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.item.LaserPointer;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTabs;
import net.ltxprogrammer.changed.item.DyeableAbdomenArmor;
import net.ltxprogrammer.changed.item.ExtendedItemProperties;
import net.ltxprogrammer.changed.item.Shorts;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorfulShorts extends DyeableArmorItem implements Wearable, Shorts, ExtendedItemProperties {
    public ColorfulShorts() {
        super(MATERIAL, EquipmentSlot.LEGS, (new Item.Properties()).tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
    }

    @Override
    public boolean hasCustomColor(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("color", 99);
    }

    @Override
    public int getColor(@NotNull ItemStack stack) {
        return hasCustomColor(stack) ? stack.getTag().getInt("color") : 0xFFFFFF; // branco por padrão
    }
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    public SoundEvent getEquipSound() {
        return ChangedSounds.EQUIP3;
    }

    public SoundEvent getBreakSound(ItemStack itemStack) {
        return ChangedSounds.SLASH10;
    }

    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int var10000 = stack.getDamageValue() - 1;
        return Changed.modResourceStr("textures/models/benign_pants_" + Mth.clamp(var10000, 0, 4) + ".png");
    }

    public static void DynamicColor(RegistryObject<Item> item){
        Minecraft.getInstance().getItemColors().register(
                (stack, tintIndex) -> {
                    if (stack.getItem() instanceof ColorfulShorts colorfulShorts){
                        return colorfulShorts.getColor(stack);
                    }
                    return -1; // Cor padrão (branco)
                },
                item.get()
        );
    }
}
