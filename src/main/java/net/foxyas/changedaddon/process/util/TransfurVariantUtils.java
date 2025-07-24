package net.foxyas.changedaddon.process.util;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.util.Cacheable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

public class TransfurVariantUtils {

    private static final Cacheable<AttributeMap> BASE_ATTRIBUTES = Cacheable.of(() -> new AttributeMap(Player.createAttributes().build()));

    private static ChangedEntity entity(ResourceLocation loc, Level level){
        TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(loc);

        return variant == null ? null : variant.getEntityType().create(level);
    }

    public static float GetLandSpeed(ResourceLocation form, Player player) {
        ChangedEntity entity = entity(form, player.level);
        if(entity == null) return 0;

        entity.setUnderlyingPlayer(player);
        return (float) (entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) * 0.1F / BASE_ATTRIBUTES.get().getBaseValue(Attributes.MOVEMENT_SPEED));
    }

    public static float GetSwimSpeed(ResourceLocation form, Player player) {
        ChangedEntity entity = entity(form, player.level);
        if(entity == null) return 0;

        entity.setUnderlyingPlayer(player);
        return (float) (entity.getAttributeBaseValue(ForgeMod.SWIM_SPEED.get()) / BASE_ATTRIBUTES.get().getBaseValue(ForgeMod.SWIM_SPEED.get()));
    }

    public static float GetExtraHp(ResourceLocation form, Player player) {
        ChangedEntity entity = entity(form, player.level);
        if(entity == null) return 0;

        entity.setUnderlyingPlayer(player);
        return entity.getMaxHealth() - Player.MAX_HEALTH;
    }

    public static String getMiningStrength(ResourceLocation form) {
        TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
        return variant == null ? "unknown" : variant.miningStrength.name().toLowerCase();
    }

//    public static int GetLegs(String stringvariant) {
//        try {
//            ResourceLocation form = new ResourceLocation(stringvariant);
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

    public static boolean CanGlideandFly(ResourceLocation form) {
        TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
        return variant != null && variant.canGlide;
    }

    public static boolean CanClimb(ResourceLocation form) {
        TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
        return variant != null && variant.canClimb;
    }

    public static float GetJumpStrength(ResourceLocation form) {
        TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
        return variant == null ? 0 : variant.jumpStrength;
    }
}
