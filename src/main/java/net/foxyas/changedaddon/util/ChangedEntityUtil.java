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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ChangedEntityUtil {


    public static double getTorsoYOffset(ChangedEntity self) {
        float scale = self.getScale();

        float ageAdjusted = (float) self.tickCount * 0.33333334F * 0.25F * 0.15F;
        float ageSin = Mth.sin(ageAdjusted * 3.1415927F * 0.5F);
        float ageCos = Mth.cos(ageAdjusted * 3.1415927F * 0.5F);
        float bpiSize = (self.getBasicPlayerInfo().getSize(self) - 1.0F) * 2.0F;

        double base = Mth.lerp(
                Mth.lerp(
                        1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F),
                        ageSin * ageSin * ageSin * ageSin,
                        1.0F - ageCos * ageCos * ageCos * ageCos
                ),
                0.95F,
                0.87F
        ) + bpiSize;

        return base * scale;
    }

    public static double getTorsoYOffsetForFallFly(ChangedEntity self) {
        float bpiSize = (self.getBasicPlayerInfo().getSize(self) - 1.0F) * 2.0F;
        return 0.375 + bpiSize;
    }

    public static double getPassengersRidingOffset(ChangedEntity self, double defaultValue) {
        if (self.getPose() == Pose.STANDING || self.getPose() == Pose.CROUCHING) {
            return defaultValue + getTorsoYOffset(self) + (self.isCrouching() ? 1.2 : 1.15);
        }
        return getTorsoYOffsetForFallFly(self);
    }

    public static boolean isCatTransfur(ChangedEntity changedEntity) {
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        return variant.is(ChangedAddonTags.TransfurVariants.CAT_LIKE) ||
                variant.is(ChangedAddonTags.TransfurVariants.LEOPARD_LIKE);
    }

    public static boolean isWolfTransfur(ChangedEntity changedEntity) {
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        if (variant.is(ChangedAddonTags.TransfurVariants.WOLF_LIKE)) return true;

        String id = ForgeRegistries.ENTITY_TYPES.getKey(changedEntity.getType()).toString();
        return id.toString().contains("dog") ||
                id.contains("wolf") ||
                changedEntity instanceof AbstractLatexWolf;
    }

    public static boolean isFoxTransfur(ChangedEntity changedEntity) {
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        if (variant.is(ChangedAddonTags.TransfurVariants.FOX_LIKE)) return true;

        return ForgeRegistries.ENTITY_TYPES.getKey(changedEntity.getType()).toString().contains("fox") ||
                changedEntity instanceof AbstractSnowFoxEntity;
    }

    public static boolean isDragonTransfur(ChangedEntity changedEntity) {
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        if (variant.is(ChangedAddonTags.TransfurVariants.DRAGON_LIKE)) return true;
        return ForgeRegistries.ENTITY_TYPES.getKey(changedEntity.getType()).toString().contains("dragon");
    }

    public static boolean isAquaticTransfur(ChangedEntity changedEntity) {
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        if (variant.is(ChangedAddonTags.TransfurVariants.AQUATIC_LIKE)) return true;

        return changedEntity instanceof AbstractAquaticEntity;
    }

    public static boolean isSpiderTransfur(ChangedEntity changedEntity) {
        TransfurVariant<?> variant = changedEntity.getSelfVariant();
        return variant.is(ChangedAddonTags.TransfurVariants.SPIDER_LIKE);
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
