package net.foxyas.changedaddon.entity.CustomHandle;

import net.ltxprogrammer.changed.util.Cacheable;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class AttributesHandle {
    public static final Cacheable<AttributeMap> BASE_ATTRIBUTES = Cacheable.of(() -> {
        return new AttributeMap(Player.createAttributes().build());
    });

    public static AttributeMap DefaultPlayerAttributes(){
        return new AttributeMap(Player.createAttributes().build());
    }
}
