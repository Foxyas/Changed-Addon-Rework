package net.foxyas.changedaddon.util;

import net.foxyas.changedaddon.client.gui.TransfurSoundsGuiScreen;
import net.foxyas.changedaddon.entity.api.TamableLatexEntityWithTameFunction;
import net.foxyas.changedaddon.entity.defaults.AbstractUnfuseableChangedEntity;
import net.foxyas.changedaddon.entity.defaults.tamable.AbstractExp2SnepChangedEntity;
import net.foxyas.changedaddon.entity.defaults.tamable.AbstractTamableLatexEntity;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ChangedEntityUtil {


    public static void mayChangedEntity(ChangedEntity changedEntity, Player player) {
        if (changedEntity instanceof AbstractTamableLatexEntity abstractTamableLatexEntity) {
            abstractTamableLatexEntity.tame(player);
        } else if (changedEntity instanceof AbstractExp2SnepChangedEntity abstractExp2SnepChangedEntity) {
            abstractExp2SnepChangedEntity.tame(player);
        } else if (changedEntity instanceof AbstractUnfuseableChangedEntity unfuseableChangedEntity) {
            unfuseableChangedEntity.tame(player);
        } else if (changedEntity instanceof TamableLatexEntityWithTameFunction tamableLatexEntityWithTameFunction) {
            tamableLatexEntityWithTameFunction.tameEntityForPlayer(player);
        }
    }

    /**
     * Dynamically calculates the real 3D mouth position using local offset scales
     * (Forward, Up/Down, Left/Right relative to the entity's orientation).
     *
     * @param target          The target living entity.
     * @param forwardOffset   How far forward from the eyes the mouth is.
     * @param verticalOffset  The vertical offset from the eyes (negative is down, positive is up).
     * @param sidewaysOffset  The lateral offset (positive for right, negative for left).
     * @return A Vec3 representing the precise 3D mouth position in the world.
     */
    public static Vec3 getDynamicMouthPosition(LivingEntity target, double forwardOffset, double verticalOffset, double sidewaysOffset) {
        Vec3 eyePos = target.getEyePosition();

        // Get the entity's look direction (forward)
        Vec3 lookVec = target.getViewVector(0).normalize();

        // Approximate the world 'up' vector (or use entity's exact up vector if available)
        Vec3 upVec = target.getUpVector(0).normalize();

        // Calculate the side (right/left) vector using cross product between look and up
        // (In Minecraft/Forge, cross product order ensures the correct lateral axis)
        Vec3 sideVec = lookVec.cross(upVec).normalize();

        // Start from the eye position
        Vec3 mouthPos = eyePos;

        // Apply vertical offset along the up vector
        mouthPos = mouthPos.add(upVec.scale(verticalOffset));

        // Apply forward offset along the look vector
        mouthPos = mouthPos.add(lookVec.scale(forwardOffset));

        // Apply sideways offset along the side vector (useful if an entity has an asymmetric mouth)
        mouthPos = mouthPos.add(sideVec.scale(sidewaysOffset));

        return mouthPos;
    }

    public static void setFlying(ChangedEntity changedEntity, boolean value) {
        changedEntity.setChangedEntityFlag(0, value);
    }

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
            return List.of(Component.translatable("gui.changed_addon.bestiary.na"));
        }

        List<Component> subtitles = new ArrayList<>();

        subtitles.add(Component.translatable("gui.changed_addon.bestiary.classification_prefix"));

        List<MutableComponent> species = new ArrayList<>();

        if (isCatTransfur(changedEntity)) species.add(Component.translatable("gui.changed_addon.bestiary.species.cat"));
        if (isFoxTransfur(changedEntity)) species.add(Component.translatable("gui.changed_addon.bestiary.species.fox"));
        if (isWolfTransfur(changedEntity)) species.add(Component.translatable("gui.changed_addon.bestiary.species.canine"));
        if (isDragonTransfur(changedEntity)) species.add(Component.translatable("gui.changed_addon.bestiary.species.dragon"));
        if (isAquaticTransfur(changedEntity)) species.add(Component.translatable("gui.changed_addon.bestiary.species.fish"));
        if (isSpiderTransfur(changedEntity)) species.add(Component.translatable("gui.changed_addon.bestiary.species.spider"));

        if (species.isEmpty()) {
            species.add(Component.translatable("gui.changed_addon.bestiary.unknown"));
        }

        // Joins the species (e.g., "Cat / Canine")
        subtitles.add(GuiUtils.joinWithSeparator(species, "§7 / "));

        // Special traits on a new line or added to the list
        if (isApexPredator(changedEntity)) {
            subtitles.add(Component.translatable("gui.changed_addon.bestiary.trait.apex_predator"));
        }

        return subtitles;
    }
}
