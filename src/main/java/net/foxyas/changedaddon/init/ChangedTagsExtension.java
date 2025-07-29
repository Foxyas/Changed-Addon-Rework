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

    // Accessories Tags
    public static class AccessoryEntityTags {
        public static final String HUMANOIDS = "humanoids";
        public static final String HEADLESS_TAURS = "headless_taurs";
        public static final String MER = "mer";
        public static final String NAGA = "naga";
        public static final String TAURS = "taurs";
    }
}
