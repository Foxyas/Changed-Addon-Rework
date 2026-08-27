package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.ForgeRegistries;

public final class ChangedAddonTags {

    public static final class Blocks {

        public static final TagKey<Block> DYEABLE_CRYSTAL = key("dyeable_crystal_blocks");
        public static final TagKey<Block> LAB_BIG_DOORS = key("lab_big_doors");
        public static final TagKey<Block> LAB_DOORS = key("lab_doors");
        public static final TagKey<Block> PASSABLE_BLOCKS = key("passable_blocks");
        public static final TagKey<Block> CAN_LUMINAR_CRYSTAL_SURVIVE = key("can_luminar_crystal_survive");
        public static final TagKey<Block> CAN_SPAWN_LUMINARCTIC_LEOPARDS_ON_CRYSTAL_BREAK = key("can_spawn_luminarctic_leopards_on_crystal_break");
        public static final TagKey<Block> DORMANT_LATEX_BLOCKS = key("dormant_latex_blocks");
        public static final TagKey<Block> CONDUCTIVE = key("conductive");

        private static TagKey<Block> key(String path) {
            return TagKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), ChangedAddonMod.resourceLoc(path));
        }
    }

    public static final class GameEvents {

        public static final TagKey<GameEvent> CAN_WAKE_UP_ALPHAS = key("can_wake_up_alphas");

        private static TagKey<GameEvent> key(String path) {
            return TagKey.create(Registries.GAME_EVENT, ChangedAddonMod.resourceLoc(path));
        }
    }

    public static final class Items {
        public static final TagKey<Item> AIR = vanillaKey("air");

        public static final TagKey<Item> METAL = key("metal");
        public static final TagKey<Item> PARTIAL_METAL = key("partial_metal");

        public static final TagKey<Item> SYRINGES = key("syringes");
        public static final TagKey<Item> UNTRANSFUR_ITEMS = key("untransfur_items");
        public static final TagKey<Item> GOOEY = key("gooey");
        public static final TagKey<Item> NOT_FOOD = key("is_not_food");
        public static final TagKey<Item> LATEX_SOLVENT_APPLICABLE = key("latex_solvent_applicable");
        public static final TagKey<Item> MEAT = key("meat");
        public static final TagKey<Item> BLOOD_TYPE_SYRINGE = key("blood_type_syringe");
        public static final TagKey<Item> TAME_ITEM = key("tame_items");

        public static final TagKey<Item> STABILIZER_TICKS = key("stabilizer_ticks");
        public static final TagKey<Item> MAKE_TRANSFUR_SAFE = key("transfur_safe_mode");

        public static final TagKey<Item> UNTRANSFUR_AGENTS = key("untransfur_agents");
        public static final TagKey<Item> UNTRANSFUR_CATALYZERS = key("untransfur_catalyzers");

        public static final TagKey<Item> AQUATIC_DIET = key("aquatic_diet_list");
        public static final TagKey<Item> SHARK_DIET = key("shark_diet_list");
        public static final TagKey<Item> CAT_DIET = key("cat_diet_list");
        public static final TagKey<Item> DRAGON_DIET = key("dragon_diet_list");
        public static final TagKey<Item> FOX_DIET = key("fox_diet_list");
        public static final TagKey<Item> SPECIAL_DIET = key("special_diet_list");
        public static final TagKey<Item> SWEET_DIET = key("sweet_tooth_list");
        public static final TagKey<Item> WOLF_DIET = key("wolf_diet_list");

        public static final TagKey<Item> UNIFUSER_RECIPE_CATALYST = key("unifuser_recipe_catalyst");

        public static final TagKey<Item> LUMINARA_LOGS = key("luminara_logs");

        private static TagKey<Item> key(String path) {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), ChangedAddonMod.resourceLoc(path));
        }

        private static TagKey<Item> vanillaKey(String path) {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), ResourceLocation.withDefaultNamespace(path));
        }
    }

    public static final class EntityTypes {

        public static final TagKey<EntityType<?>> ALPHA_BY_DEFAULT = key("alpha_by_default");
        public static final TagKey<EntityType<?>> CAN_CARRY = key("can_carry");
        public static final TagKey<EntityType<?>> PATABLE = key("patable");
        public static final TagKey<EntityType<?>> DRAGON_ENTITIES = key("dragon_entities");
        public static final TagKey<EntityType<?>> BEE_ENTITIES = key("bee_entities");
        public static final TagKey<EntityType<?>> CAN_USE_ACCESSORIES = key("can_use_accessories");
        public static final TagKey<EntityType<?>> PACIFY_IMMUNE = key("pacify_immune");
        public static final TagKey<EntityType<?>> PACIFY_HANDLE_IMMUNE = key("pacify_handle_immune");
        public static final TagKey<EntityType<?>> HAS_CLAWS = key("has_claws");
        public static final TagKey<EntityType<?>> CAN_GRAB = key("can_grab");
        public static final TagKey<EntityType<?>> CAN_GRAB_SUIT = key("can_grab/suit");
        public static final TagKey<EntityType<?>> IGNORE_GRABBED_TARGETS = key("ignore_grabber_targets");
        public static final TagKey<EntityType<?>> ALWAYS_CAUSE_GRAB_DAMAGE = key("always_cause_grab_damage");
        public static final TagKey<EntityType<?>> CANT_SPAWN_AS_ALPHA_ENTITY = key("cant_spawn_as_alpha_entity");
        public static final TagKey<EntityType<?>> CANT_USE_GRAB = key("cant_use_grab");
        public static final TagKey<EntityType<?>> CAN_ROAR = key("can_roar");
        public static final TagKey<EntityType<?>> HAS_BETTER_GROUND_PATHFIND = key("has_better_ground_pathfind");
        public static final TagKey<EntityType<?>> PROTOGENS = key("protogens");

        private static TagKey<EntityType<?>> key(String path) {
            return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), ChangedAddonMod.resourceLoc(path));
        }
    }

    public static final class TransfurVariants {

        // Transfur Details
        public static final TagKey<TransfurVariant<?>> ABLE_TO_CARRY = key("able_to_carry");
        public static final TagKey<TransfurVariant<?>> CAUSE_FREEZING = key("cause_freeze_dmg");
        public static final TagKey<TransfurVariant<?>> GLOWING_VARIANTS = key("glow_variants");
        public static final TagKey<TransfurVariant<?>> HAS_CLAWS = key("has_claws");

        // Transfur "Kind"
        public static final TagKey<TransfurVariant<?>> AQUATIC_LIKE = key("aquatic_like");
        public static final TagKey<TransfurVariant<?>> CAT_LIKE = key("cat_like");
        public static final TagKey<TransfurVariant<?>> DRAGON_LIKE = key("dragon_like");
        public static final TagKey<TransfurVariant<?>> FOX_LIKE = key("fox_like");
        public static final TagKey<TransfurVariant<?>> LEOPARD_LIKE = key("leopard_like");
        public static final TagKey<TransfurVariant<?>> SHARK_LIKE = key("shark_like");
        public static final TagKey<TransfurVariant<?>> WOLF_LIKE = key("wolf_like");
        public static final TagKey<TransfurVariant<?>> SPIDER_LIKE = key("spider_like");

        // Transfur Diets
        public static final TagKey<TransfurVariant<?>> HAS_CANINE_DIET = key("canine_diet");
        public static final TagKey<TransfurVariant<?>> HAS_FOX_DIET = key("canines/fox_diet");

        public static final TagKey<TransfurVariant<?>> HAS_FELINE_DIET = key("feline_diet");

        public static final TagKey<TransfurVariant<?>> HAS_DRACONIC_DIET = key("draconic_diet");

        public static final TagKey<TransfurVariant<?>> HAS_AQUATIC_DIET = key("aquatic_diet");
        public static final TagKey<TransfurVariant<?>> HAS_SHARK_DIET = key("shark_diet");

        public static final TagKey<TransfurVariant<?>> HAS_SWEET_DIET = key("sweet_tooth");
        public static final TagKey<TransfurVariant<?>> HAS_SPECIAL_DIET = key("special_diet");

        public static final TagKey<TransfurVariant<?>> HAS_NO_DIET = key("no_diet");

        // Bosses and stuff.
        public static final TagKey<TransfurVariant<?>> BOSS_VARIANTS = key("boss_variants");
        public static final TagKey<TransfurVariant<?>> REMOVED_FROM_GROUNDED_SYRINGES = key("removed_from/grounded_syringes");
        public static final TagKey<TransfurVariant<?>> REMOVED_FROM_RANDOM_VARIANT_FUNCTION = key("removed_from/random_variant_function");


        private static TagKey<TransfurVariant<?>> key(String path) {
            return TagKey.create(ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(), ChangedAddonMod.resourceLoc(path));
        }
    }

    public static class DamageTypes {

        public static final TagKey<DamageType> IS_LATEX_SOLVENT = key("is_latex_solvent");

        private static TagKey<DamageType> key(String path) {
            return TagKey.create(Registries.DAMAGE_TYPE, ChangedAddonMod.resourceLoc(path));
        }
    }
}
