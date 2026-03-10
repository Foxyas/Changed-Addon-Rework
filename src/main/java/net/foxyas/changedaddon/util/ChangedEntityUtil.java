package net.foxyas.changedaddon.util;

import net.foxyas.changedaddon.client.gui.TransfurSoundsGuiScreen;
import net.foxyas.changedaddon.entity.simple.AbstractSnowFoxEntity;
import net.foxyas.changedaddon.event.TransfurEvents;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractAquaticEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexWolf;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ChangedEntityUtil {

    public static boolean isCatTransfur(ChangedEntity changedEntity) {
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        return variant.is(ChangedAddonTags.TransfurTypes.CAT_LIKE) ||
                variant.is(ChangedAddonTags.TransfurTypes.LEOPARD_LIKE);
    }

    public static boolean isWolfTransfur(ChangedEntity changedEntity) {
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        if (variant.is(ChangedAddonTags.TransfurTypes.WOLF_LIKE)) return true;

        String id = ForgeRegistries.ENTITY_TYPES.getKey(changedEntity.getType()).toString();
        return id.toString().contains("dog") ||
                id.contains("wolf") ||
                changedEntity instanceof AbstractLatexWolf;
    }

    public static boolean isFoxTransfur(ChangedEntity changedEntity) {
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        if (variant.is(ChangedAddonTags.TransfurTypes.FOX_LIKE)) return true;

        return ForgeRegistries.ENTITY_TYPES.getKey(changedEntity.getType()).toString().contains("fox") ||
                changedEntity instanceof AbstractSnowFoxEntity;
    }

    public static boolean isDragonTransfur(ChangedEntity changedEntity) {
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        if (variant.is(ChangedAddonTags.TransfurTypes.DRAGON_LIKE)) return true;
        return ForgeRegistries.ENTITY_TYPES.getKey(changedEntity.getType()).toString().contains("dragon");
    }

    public static boolean isAquaticTransfur(ChangedEntity changedEntity) {
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        if (variant.is(ChangedAddonTags.TransfurTypes.AQUATIC_LIKE)) return true;

        return changedEntity instanceof AbstractAquaticEntity;
    }

    public static boolean isSpiderTransfur(ChangedEntity changedEntity) {
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        return variant.is(ChangedAddonTags.TransfurTypes.SPIDER_LIKE);
    }

    public static boolean canRoar(ChangedEntity changedEntity) {
        return changedEntity.getType().is(ChangedAddonTags.EntityTypes.CAN_ROAR);
    }

    public static boolean isApexPredator(ChangedEntity changedEntity) {
        if (changedEntity.getSelfVariant() == null)
            return false;

        ResourceLocation id = changedEntity.getSelfVariant().getFormId();

        if (id == null)
            return false;

        String path = id.toString();

        return path.contains("lion")
                || path.contains("tiger")
                || path.startsWith("changed_addon:form_experiment009") || TransfurEvents.resolveChangedEntity(changedEntity).getType().is(ChangedAddonTags.EntityTypes.CAN_ROAR);
    }


    /* ------------------------------------------------------------
     * Titles & state
     * ------------------------------------------------------------ */
    public static List<Component> getEntitySubtitle(ChangedEntity changedEntity) {
        if (changedEntity.getSelfVariant() == null) {
            return List.of(Component.literal("§7N/A"));
        }

        List<Component> subtitles = new ArrayList<>();

        // Change "are a" to "is a" or use a cleaner "Species:" label
        subtitles.add(Component.literal("§fClassification: "));

        List<MutableComponent> species = new ArrayList<>();

        if (isCatTransfur(changedEntity)) species.add(Component.literal("§fCat"));
        if (isFoxTransfur(changedEntity)) species.add(Component.literal("§fFox"));
        if (isWolfTransfur(changedEntity)) species.add(Component.literal("§fCanine"));
        if (isDragonTransfur(changedEntity)) species.add(Component.literal("§fDragon"));
        if (isAquaticTransfur(changedEntity)) species.add(Component.literal("§fFish"));
        if (isSpiderTransfur(changedEntity)) species.add(Component.literal("§fSpider"));

        if (species.isEmpty()) {
            species.add(Component.literal("§8Unknown"));
        }

        // Joins the species (e.g., "Cat / Canine")
        subtitles.add(TransfurSoundsGuiScreen.joinWithSeparator(species, "§7 / "));

        // Special traits on a new line or added to the list
        if (isApexPredator(changedEntity)) {
            subtitles.add(Component.literal("§6[Apex Predator]"));
        }

        return subtitles;
    }
}
