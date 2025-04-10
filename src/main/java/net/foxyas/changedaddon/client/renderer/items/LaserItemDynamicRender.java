package net.foxyas.changedaddon.client.renderer.items;

import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class LaserItemDynamicRender {
    public static void DynamicLaserColor(RegistryObject<Item> item){
        Minecraft.getInstance().getItemColors().register(
                (stack, tintIndex) -> {
                    if (tintIndex == 0) { // Só aplica a cor no layer certo
                        if (stack.hasTag() && stack.getTag().contains("Color")) {
                            return stack.getTag().getInt("Color");
                        }
                    }
                    return 0xFFFFFFF; // Cor padrão (branco)
                },
                item.get()
        );
    }
    public static void DynamicLaserColor(Item item){
        Minecraft.getInstance().getItemColors().register(
                (stack, tintIndex) -> {
                    if (tintIndex == 0) { // Só aplica a cor no layer certo
                        if (stack.hasTag() && stack.getTag().contains("Color")) {
                            return stack.getTag().getInt("Color");
                        }
                    }
                    return 0xFFFFFFF; // Cor padrão (branco)
                },
                item
        );
    }

}
