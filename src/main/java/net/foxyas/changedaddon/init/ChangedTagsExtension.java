package net.foxyas.changedaddon.init;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ChangedTagsExtension extends ChangedTags {

    //Accessories Tags [is a ITEM TAG]
    public static class AccessoryItemsTags {
        public static final TagKey<Item> BODY = create("body");
        public static final TagKey<Item> LEGS = create("legs");

        private static TagKey<Item> create(String name) {
            return TagKey.create(Registry.ITEM_REGISTRY, Changed.modResource(name));
        }
    }
}
