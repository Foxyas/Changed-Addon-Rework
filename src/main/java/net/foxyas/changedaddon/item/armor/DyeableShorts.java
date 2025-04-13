package net.foxyas.changedaddon.item.armor;

import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.registers.ChangedAddonRegisters;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.item.ExtendedItemProperties;
import net.ltxprogrammer.changed.item.Shorts;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class DyeableShorts extends DyeableArmorItem implements Shorts, ExtendedItemProperties {
    public DyeableShorts() {
        super(MATERIAL, EquipmentSlot.LEGS, (new Item.Properties()).tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
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

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (this.allowdedIn(tab)) {
            ItemStack stack = new ItemStack(this);
            this.setColor(stack, Color3.WHITE.toInt());
            items.add(stack);
        }
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if ("overlay".equals(type)) {
            return "changed_addon:textures/models/armor/dyeable_shorts_layer_1_overlay.png"; // totalmente invisível
        }
        return "changed_addon:textures/models/armor/dyeable_shorts_layer_1.png";
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientInitializer {
        @SubscribeEvent
        public static void onItemColorsInit(ColorHandlerEvent.Item event) {
            event.getItemColors().register(
                    (stack, layer) -> ((DyeableLeatherItem)stack.getItem()).getColor(stack),
                    ChangedAddonRegisters.DYEABLE_SHORTS.get());
        }
    }

    public static void DynamicColor(RegistryObject<Item> item){
        Minecraft.getInstance().getItemColors().register(
                (stack, tintIndex) -> {
                    if (stack.getItem() instanceof DyeableShorts colorfulShorts){
                        return colorfulShorts.getColor(stack);
                    }
                    return -1; // Cor padrão (branco)
                },
                item.get()
        );
    }
}