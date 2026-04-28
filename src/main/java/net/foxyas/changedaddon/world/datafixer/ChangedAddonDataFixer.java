package net.foxyas.changedaddon.world.datafixer;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonEnchantments;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.DataFixTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChangedAddonDataFixer {

    public static final int DATAFIX_ID = 1; // used to internal version
    private static final Map<DataFixTypes, Consumer<CompoundTag>> DATA_FIXERS = new HashMap<>();
    private static final Consumer<CompoundTag> NULL_OP = (tag) -> {
    };
    // Data Fixers
    private final Map<ResourceLocation, ResourceLocation> ENCHANTMENT_REMAP = new HashMap<>();
    // Future Data fixers
    private final Map<ResourceLocation, ResourceLocation> ENTITY_ID_REMAP = Util.make(new HashMap<>(), map -> {
    });
    private final Map<ResourceLocation, ResourceLocation> ITEM_ID_REMAP = Util.make(new HashMap<>(), map -> {
        map.put(ChangedAddonMod.resourceLoc("lunarrose_helmet"), ChangedAddonItems.LUNAR_ROSE.getId());
    });
    private final Map<ResourceLocation, ResourceLocation> BLOCK_ID_REMAP = Util.make(new HashMap<>(), map -> {
    });
    private final Map<ResourceLocation, ResourceLocation> BLOCK_ITEM_ID_REMAP = Util.make(new HashMap<>(), map -> {
    });
    private final Map<ResourceLocation, ResourceLocation> VARIANT_ID_REMAP = Util.make(new HashMap<>(), map -> {
        map.put(ChangedAddonMod.resourceLoc("form_ket_experiment009"), ChangedAddonTransfurVariants.EXPERIMENT_009.getId());
        map.put(ChangedAddonMod.resourceLoc("form_ket_experiment009_boss"), ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.getId());
    });
    private final Map<String, String> ENUM_REMAP = Util.make(new HashMap<>(), map -> {
    });
    private final Map<String, String> TAG_REMAP = Util.make(new HashMap<>(), map -> {
    });
    private final Map<String, String> GAMERULES_REMAP = Util.make(new HashMap<>(), map -> {
//        map.put("doLatexInfection", ChangedAddonGameRules.DO_LATEX_INFECTION.getId());
//        map.put("painiteGeneration", ChangedAddonGameRules.PAINITE_GENERATION.getId());
//        map.put("doDazedLatexBurn", ChangedAddonGameRules.DO_DAZED_LATEX_BURN.getId());
//        map.put("doDarkLatexMaskTransfur", ChangedAddonGameRules.TICKS_TO_DARK_LATEX_MASK_TRANSFUR.getId());
    });

    public ChangedAddonDataFixer() {
        // define remapeamentos de IDs antigos para novos
        ENCHANTMENT_REMAP.put(
                ChangedAddonMod.resourceLoc("solvent"),
                ChangedAddonEnchantments.LATEX_SOLVENT.getId()
        );

        // adiciona um tipo de fix (aqui corrigimos inventário de player)
        DATA_FIXERS.put(DataFixTypes.PLAYER, this::fixPlayerData);
        DATA_FIXERS.put(DataFixTypes.LEVEL, this::fixLevelData);
    }

    private void fixPlayerData(CompoundTag tag) {
        this.updateTagNames(tag);

        if (!tag.contains("Inventory")) return;

        ListTag inv = tag.getList("Inventory", Tag.TAG_COMPOUND);
        inv.forEach(itemTag -> updateItem((CompoundTag) itemTag));
    }

    private void fixLevelData(@NotNull CompoundTag rootTag) {

        // IF there's no data maybe the level data is already the CompoundTag "Data"
        if (rootTag.getAllKeys().size() > 1) {
            this.fixData(rootTag);
        } else {
            if (rootTag.contains("Data")) {
                CompoundTag dataTag = rootTag.getCompound("Data");
                this.fixData(dataTag);
            }

            // Do other Stuff
        }
    }

    private void fixData(@NotNull CompoundTag dataTag) {

        if (!dataTag.contains("GameRules")) return;
        CompoundTag gameRules = dataTag.getCompound("GameRules");

        for (String oldRule : gameRules.getAllKeys()) {
            String newRule = GAMERULES_REMAP.get(oldRule);
            if (newRule == null || newRule.equals(oldRule)) continue;

            String value = gameRules.getString(oldRule);
            ChangedAddonMod.LOGGER.info("[Changed Addon DataFix] Remapping gamerule {} → {} (value: {})", oldRule, newRule, value);

            // Copia o valor e remove a antiga
            gameRules.putString(newRule, value);
            gameRules.remove(oldRule);
        }
    }

    private void fixEnchantments(CompoundTag tag) {
        if (!tag.contains("Enchantments", Tag.TAG_LIST)) return;

        ListTag enchantments = tag.getList("Enchantments", Tag.TAG_COMPOUND);
        CompoundTag ench;
        ResourceLocation oldId, newId;
        for (int i = 0; i < enchantments.size(); i++) {
            ench = enchantments.getCompound(i);
            if (!ench.contains("id")) continue;

            oldId = ResourceLocation.tryParse(ench.getString("id"));
            if (oldId == null) continue;

            if (ENCHANTMENT_REMAP.containsKey(oldId)) {
                newId = ENCHANTMENT_REMAP.get(oldId);
                ChangedAddonMod.LOGGER.info("[Changed Addon DataFix] Remapping enchantment {} → {}", oldId, newId);
                ench.putString("id", newId.toString());
            }
        }
    }

    private void updateTagNames(@NotNull CompoundTag tag) {
        String newKey;
        Tag subTag;
        for (String key : tag.getAllKeys()) {
            if (!this.TAG_REMAP.containsKey(key)) continue;

            newKey = this.TAG_REMAP.get(key);
            subTag = tag.get(key);
            if (subTag != null && !newKey.equals(key)) {
                tag.put(newKey, subTag);
                tag.remove(key);
            }
        }
    }

    private void updateID(@NotNull Map<ResourceLocation, ResourceLocation> remap, @NotNull CompoundTag tag, String idName) {
        if (tag.contains(idName)) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString(idName));
            if (id != null) {
                tag.putString(idName, this.updateID(remap, id).toString());
            }

        }
    }

    private void updateName(@NotNull Map<String, String> remap, @NotNull CompoundTag tag, String idName) {
        if (tag.contains(idName)) {
            String id = tag.getString(idName);
            tag.putString(idName, this.updateName(remap, id));
        }
    }

    private void updateItem(@NotNull CompoundTag itemStack) {
        this.updateID(this.ITEM_ID_REMAP, itemStack, "id");
        this.updateID(this.BLOCK_ITEM_ID_REMAP, itemStack, "id");
        if (itemStack.contains("tag")) {
            this.updateItemTag(itemStack.getCompound("tag"));
        }

    }

    private void updateItemTag(@NotNull CompoundTag itemTag) {
        this.updateID(this.VARIANT_ID_REMAP, itemTag, "form");

        this.fixEnchantments(itemTag);
    }

    /**
     * Aplica correção dependendo do tipo
     */
    public void updateCompoundTag(@NotNull DataFixTypes type, @Nullable CompoundTag tag) {
        if (tag != null) {
            DATA_FIXERS.getOrDefault(type, NULL_OP).accept(tag);
        }
    }

    // Helpers
    private ResourceLocation updateID(@NotNull Map<ResourceLocation, ResourceLocation> remap, ResourceLocation id) {
        return remap.getOrDefault(id, id);
    }

    private String updateName(@NotNull Map<String, String> remap, String name) {
        return remap.getOrDefault(name, name);
    }
}
