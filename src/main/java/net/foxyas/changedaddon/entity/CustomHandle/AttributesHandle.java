package net.foxyas.changedaddon.entity.CustomHandle;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Cacheable;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

public class AttributesHandle {
    public static final Cacheable<AttributeMap> BASE_ATTRIBUTES = Cacheable.of(() -> {
        return new AttributeMap(Player.createAttributes().build());
    });

    public static AttributeMap DefaultPlayerAttributes() {
        return new AttributeMap(Player.createAttributes().build());
    }
}
