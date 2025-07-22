package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.client.renderer.items.SimpleColorfulClothingRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.AccessoryLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonAccessoryRenderers {
    @SubscribeEvent
    public static void registerAccessoryRenderers(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            AccessoryLayer.registerRenderer(ChangedAddonItems.DYEABLE_SHORTS.get(), SimpleColorfulClothingRenderer.of(ArmorModel.CLOTHING_INNER, EquipmentSlot.LEGS));
            AccessoryLayer.registerRenderer(ChangedAddonItems.DYEABLE_SHIRT.get(), SimpleColorfulClothingRenderer.of(ArmorModel.CLOTHING_INNER, EquipmentSlot.CHEST));
        });
    }
}