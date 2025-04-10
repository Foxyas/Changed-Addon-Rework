package net.foxyas.changedaddon.client.renderer.items;

import net.foxyas.changedaddon.item.LaserPointer;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class LaserItemDynamicRender {
    public static void DynamicLaserColor(RegistryObject<Item> item){
        Minecraft.getInstance().getItemColors().register(
                (stack, tintIndex) -> {
                    if (tintIndex == 0) { // Só aplica a cor no layer certo
                        return LaserPointer.getColorAsColor3(stack).toInt();
                    }
                    return -1; // Cor padrão (branco)
                },
                item.get()
        );
    }
    public static void DynamicLaserColor(Item item){
        Minecraft.getInstance().getItemColors().register(
                (stack, tintIndex) -> {
                    if (tintIndex == 0) { // Só aplica a cor no layer certo
                        return LaserPointer.getColorAsColor3(stack).toInt();
                    }
                    return -1; // Cor padrão (branco)
                },
                item
        );
    }

}
