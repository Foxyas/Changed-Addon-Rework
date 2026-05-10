package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.client.renderer.items.HazardBodySuitClothingRenderer;
import net.foxyas.changedaddon.client.renderer.items.SimpleColorfulClothingRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.AccessoryLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonAccessoryRenderers {

    @SubscribeEvent
    public static void registerAccessoryRenderers(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            //AccessoryLayer.registerRenderer(ChangedAddonItems.DYEABLE_SPORTS_BRA.get(), SimpleColorfulClothingRenderer.of(ArmorModel.CLOTHING_INNER, EquipmentSlot.CHEST));
            AccessoryLayer.registerRenderer(ChangedAddonItems.DYEABLE_TSHIRT.get(), SimpleColorfulClothingRenderer.of(ArmorModel.CLOTHING_INNER, EquipmentSlot.CHEST));
            AccessoryLayer.registerRenderer(ChangedAddonItems.DYEABLE_SHORTS.get(), SimpleColorfulClothingRenderer.of(ArmorModel.CLOTHING_INNER, EquipmentSlot.LEGS));
            AccessoryLayer.registerRenderer(ChangedAddonItems.HAZARD_BODY_SUIT.get(),
                    HazardBodySuitClothingRenderer.of(ArmorModel.CLOTHING_INNER,
                            Set.of(new HazardBodySuitClothingRenderer.ModelComponent(ArmorModel.CLOTHING_INNER, EquipmentSlot.HEAD),
                                    new HazardBodySuitClothingRenderer.ModelComponent(ArmorModel.CLOTHING_INNER, EquipmentSlot.CHEST),
                                    new HazardBodySuitClothingRenderer.ModelComponent(ArmorModel.CLOTHING_INNER, EquipmentSlot.LEGS),
                                    new HazardBodySuitClothingRenderer.ModelComponent(ArmorModel.ARMOR_INNER, EquipmentSlot.FEET),
                                    new HazardBodySuitClothingRenderer.ModelComponent(ArmorModel.CLOTHING_OUTER, EquipmentSlot.FEET)
                            )
                    )
            );
        });
    }
}