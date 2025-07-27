package net.foxyas.changedaddon;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ChangedAddonTags {

    public static final class Blocks {

        public static final TagKey<Block> DYEABLE_CRYSTAL = key("dyeable_crystal_blocks");
        public static final TagKey<Block> LAB_BIG_DOORS = key("lab_big_doors");
        public static final TagKey<Block> LAB_DOORS = key("lab_doors");
        public static final TagKey<Block> PASSABLE_BLOCKS = key("passable_blocks");

        private static TagKey<Block> key(String path){
            return TagKey.create(Registry.BLOCK_REGISTRY, ChangedAddonMod.resourceLoc(path));
        }
    }

    public static final class Items {

        public static final TagKey<Item> SYRINGES = key("syringes");
        public static final TagKey<Item> UNTRANSFURS = key("untransfur_items");
        public static final TagKey<Item> GOOEY = key("gooey");
        public static final TagKey<Item> NOT_FOOD = key("is_not_food");
        public static final TagKey<Item> LATEX_SOLVENT_APPLICABLE = key("latex_solvent_applicable");
        public static final TagKey<Item> MEAT = key("meat");
        public static final TagKey<Item> BLOOD_TYPE_SYRINGE = key("bloodtype_syringe");//TODO fix names after recipe datagen
        public static final TagKey<Item> TAME_ITEM = key("tame_items");

        public static final TagKey<Item> AQUATIC_DIET = key("aquatic_diet_list");
        public static final TagKey<Item> SHARK_DIET = key("shark_diet_list");
        public static final TagKey<Item> CAT_DIET = key("cat_diet_list");
        public static final TagKey<Item> DRAGON_DIET = key("dragon_diet_list");
        public static final TagKey<Item> FOX_DIET = key("fox_diet_list");
        public static final TagKey<Item> SPECIAL_DIET = key("special_diet_list");
        public static final TagKey<Item> SWEET_DIET = key("sweet_tooth_list");
        public static final TagKey<Item> WOLF_DIET = key("wolf_diet_list");

        private static TagKey<Item> key(String path){
            return TagKey.create(Registry.ITEM_REGISTRY, ChangedAddonMod.resourceLoc(path));
        }
    }

    public static final class EntityTypes {

        public static final TagKey<EntityType<?>> CAN_CARRY = key("can_carry");
        public static final TagKey<EntityType<?>> CHANGED_CREATURE = key("changed_creature");
        public static final TagKey<EntityType<?>> PATABLE = key("patable");

        private static TagKey<EntityType<?>> key(String path){
            return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, ChangedAddonMod.resourceLoc(path));
        }
    }

    public static final class TransfurTypes {

        public static final TagKey<TransfurVariant<?>> ABLE_TO_CARRY = key("able_to_carry");
        public static final TagKey<TransfurVariant<?>> CAUSE_FREEZING = key("cause_freeze_dmg");
        public static final TagKey<TransfurVariant<?>> GLOWING = key("glow_variants");
        public static final TagKey<TransfurVariant<?>> HAS_CLAWS = key("has_claws");

        public static final TagKey<TransfurVariant<?>> AQUATIC_LIKE = key("aquatic_like");
        public static final TagKey<TransfurVariant<?>> CAT_LIKE = key("cat_like");
        public static final TagKey<TransfurVariant<?>> DRAGON_LIKE = key("dragon_like");
        public static final TagKey<TransfurVariant<?>> FOX_LIKE = key("fox_like");
        public static final TagKey<TransfurVariant<?>> LEOPARD_LIKE = key("leopard_like");
        public static final TagKey<TransfurVariant<?>> SHARK_LIKE = key("shark_like");
        public static final TagKey<TransfurVariant<?>> WOLF_LIKE = key("wolf_like");

        public static final TagKey<TransfurVariant<?>> AQUATIC_DIET = key("aquatic_diet");
        public static final TagKey<TransfurVariant<?>> SHARK_DIET = key("shark_diet");
        public static final TagKey<TransfurVariant<?>> CAT_DIET = key("cat_diet");
        public static final TagKey<TransfurVariant<?>> DRAGON_DIET = key("dragon_diet");
        public static final TagKey<TransfurVariant<?>> FOX_DIET = key("fox_diet");
        public static final TagKey<TransfurVariant<?>> SPECIAL_DIET = key("special_diet");
        public static final TagKey<TransfurVariant<?>> SWEET_DIET = key("sweet_tooth");
        public static final TagKey<TransfurVariant<?>> WOLF_DIET = key("wolf_diet");
        public static final TagKey<TransfurVariant<?>> NO_DIET = key("no_diet");

        private static TagKey<TransfurVariant<?>> key(String path){
            return TagKey.create(ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(), ChangedAddonMod.resourceLoc(path));
        }
    }
}
