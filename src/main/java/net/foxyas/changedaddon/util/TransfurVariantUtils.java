package net.foxyas.changedaddon.util;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.util.Cacheable;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransfurVariantUtils {

    private static final Cacheable<AttributeMap> BASE_ATTRIBUTES = Cacheable.of(() -> new AttributeMap(Player.createAttributes().build()));

    public static List<ResourceLocation> getTransfurVariantsFormIdFromStringList(List<? extends String> stringList, @Nullable Level level, boolean detectTags, boolean detectMods) {
        return getTransfurVariantsFromStringList(stringList, level, detectTags, detectMods).stream().map(TransfurVariant::getFormId).toList();
    }

    public static List<TransfurVariant<?>> getTransfurVariantsFromStringList(List<? extends String> stringList, @Nullable Level level, boolean detectTags, boolean detectMods) {
        List<String> modIdsList = new ArrayList<>(), tagsList = new ArrayList<>(), formIdsList = new ArrayList<>();
        List<TransfurVariant<?>> transfurVariantList = new ArrayList<>();

        if (stringList.size() == 1 && stringList.get(0).equals("changed:random")) {
            return TransfurVariant.getPublicTransfurVariants().toList();
        } else if (stringList.size() > 1 && level != null) {
            stringList.removeIf((string) -> string.equals("changed:random"));
            transfurVariantList.add(Util.getRandom(TransfurVariant.getPublicTransfurVariants().toList(), level.random));
        } else {
            stringList.removeIf((string) -> string.equals("changed:random"));
        }

        for (String string : stringList) {
            if (string.startsWith("@") && detectMods) {
                modIdsList.add(string.substring(1));
            } else if (string.startsWith("#") && level != null && detectTags) {
                tagsList.add(string.substring(1));
            } else {
                formIdsList.add(string);
            }
        }


        // MOD ID
        for (String modId : modIdsList) {
            transfurVariantList.addAll(getVariantsFromModId(modId));
        }

        // TAG
        for (String tagId : tagsList) {
            if (ResourceLocation.isValidResourceLocation(tagId)) {
                transfurVariantList.addAll(
                        getVariantsFromTag(ResourceLocation.parse(tagId), level)
                );
            }
        }

        // ID Normal
        for (String formId : formIdsList) {
            TransfurVariant<?> variant =
                    ChangedRegistry.TRANSFUR_VARIANT.get().getValue(ResourceLocation.parse(formId));

            if (variant != null)
                transfurVariantList.add(variant);
        }

        return transfurVariantList;
    }

    public static List<TransfurVariant<?>> getVariantsFromModId(String modId) {
        return ChangedRegistry.TRANSFUR_VARIANT.get()
                .getValues()
                .stream()
                .filter((transfurVariant -> transfurVariant.getFormId().getNamespace().equals(modId))).toList();
    }

    public static List<TransfurVariant<?>> getVariantsFromTag(ResourceLocation tagId, Level level) {
        List<TransfurVariant<?>> list = new ArrayList<>();

        if (level == null) return list; // Precisa de um level para acessar tags

        // 1. Criar a tag key
        TagKey<TransfurVariant<?>> tagKey =
                TagKey.create(ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(), tagId);

        // 2. Obter o registry do nível
        Registry<TransfurVariant<?>> registry =
                level.registryAccess().registryOrThrow(ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey());

        // 3. Obter todos os entries da tag
        Optional<HolderSet.Named<TransfurVariant<?>>> optionalSet =
                registry.getTag(tagKey);

        if (optionalSet.isEmpty())
            return list;

        // 4. Jogar tudo para uma lista real
        for (Holder<TransfurVariant<?>> holder : optionalSet.get()) {
            list.add(holder.value());
        }

        return list;
    }

    private static ChangedEntity entity(TransfurVariant<?> variant, Level level) {
        return variant == null ? null : ChangedEntities.getCachedEntity(level, variant.getEntityType());
    }

    public static float getLandSpeedOfVariantBasedOnPlayer(TransfurVariant<?> variant, Player player) {
        ChangedEntity entity = entity(variant, player.level);
        if (entity == null) return 0;

        entity.setUnderlyingPlayer(player);
        return (float) (entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) * 0.1F / BASE_ATTRIBUTES.get().getBaseValue(Attributes.MOVEMENT_SPEED));
    }

    public static float getSwimSpeedOfVariantBasedOnPlayer(TransfurVariant<?> variant, Player player) {
        ChangedEntity entity = entity(variant, player.level);
        if (entity == null) return 0;

        entity.setUnderlyingPlayer(player);
        return (float) (entity.getAttributeBaseValue(ForgeMod.SWIM_SPEED.get()) / BASE_ATTRIBUTES.get().getBaseValue(ForgeMod.SWIM_SPEED.get()));
    }

    public static float getExtraHpOfVariantBasedOnPlayer(TransfurVariant<?> variant, Player player) {
        ChangedEntity entity = entity(variant, player.level);
        if (entity == null) return 0;

        entity.setUnderlyingPlayer(player);
        return entity.getMaxHealth() - Player.MAX_HEALTH;
    }

    public static String getMiningStrengthOfVariant(TransfurVariant<?> variant, Player player) {
        return variant == null ? "unknown" : variant.miningStrength.name().toLowerCase();
    }

//    public static int GetLegs(String stringvariant) {
//        try {
//            ResourceLocation form = ResourceLocation.parse(stringvariant);
//            if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getRegistryName).anyMatch(form::equals)) {
//                TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
//                return variant == null ? 0 : variant.legCount;
//            } else {
//                return 0;
//            }
//        } catch (Exception e) {
//            //System.err.println("Erro when processing GetLegs: " + e.getMessage());
//            return 0;
//        }
//    }

    public static boolean canVariantGlide(TransfurVariantInstance<?> variantInstance) {
        return variantInstance != null && variantInstance.canElytraGlide();
    }

    public static boolean canVariantFly(TransfurVariantInstance<?> variantInstance) {
        return variantInstance != null && variantInstance.canCreativeFly();
    }


    public static boolean CanClimb(TransfurVariant<?> variant) {
        return variant != null && variant.canClimb;
    }

    public static float GetJumpStrength(TransfurVariant<?> variant, Player player) {
        ChangedEntity entity = entity(variant, player.level);
        if (entity == null) return 0;

        entity.setUnderlyingPlayer(player);
        return (float) (entity.getAttributes().hasAttribute(ChangedAttributes.JUMP_STRENGTH.get()) ? entity.getAttributeValue(ChangedAttributes.JUMP_STRENGTH.get()) : 0);
    }
}
