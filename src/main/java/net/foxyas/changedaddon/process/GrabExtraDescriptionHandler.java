package net.foxyas.changedaddon.process;

import com.mojang.blaze3d.platform.InputConstants;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collection;

public class GrabExtraDescriptionHandler {

    public static void addExtraDescriptions(ArrayList<Component> description, IAbstractChangedEntity entity, Collection<Component> baseDescription) {
        // 1. Get the Key instances for mouse buttons
        InputConstants.Key shift = InputConstants.Type.KEYSYM.getOrCreate(InputConstants.KEY_LSHIFT);
        InputConstants.Key mouseRightClick = InputConstants.Type.MOUSE.getOrCreate(InputConstants.MOUSE_BUTTON_RIGHT);
        InputConstants.Key middleMouseButton = InputConstants.Type.MOUSE.getOrCreate(InputConstants.MOUSE_BUTTON_MIDDLE);

        // 2. Get their translated Component representations
        Component shiftName = shift.getDisplayName();
        Component rightClickName = mouseRightClick.getDisplayName();
        Component middleButtonName = middleMouseButton.getDisplayName();

        // 3. Pass the translated components into your translation key
        description.add(Component.translatable(
                "ability.changed.grab_entity.desc.toggle_grab_safe_mode",
                rightClickName,
                middleButtonName
        ));

        description.add(Component.translatable(
                "ability.changed.grab_entity.desc.toggle_grab_transfur_damage_mode",
                shiftName,
                rightClickName,
                middleButtonName
        ));
    }
}
