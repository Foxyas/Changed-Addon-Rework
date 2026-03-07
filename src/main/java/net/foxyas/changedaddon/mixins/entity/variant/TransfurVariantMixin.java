package net.foxyas.changedaddon.mixins.entity.variant;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.entity.api.ChangedEntityExtension;
import net.foxyas.changedaddon.entity.api.ISafeChangedEntity;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.world.entity.npc.AbstractVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TransfurVariant.class, remap = false)
public class TransfurVariantMixin {

    @ModifyReturnValue(method = "shouldScareVillager", at = @At("RETURN"))
    private static boolean shouldScareVillagersHook(boolean original, ChangedEntity entity, AbstractVillager villager) {
        if (entity.getType().is(ChangedTags.EntityTypes.LATEX) && entity.hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
            return false;
        } else if (entity.getType().is(ChangedTags.EntityTypes.LATEX) && ChangedEntityExtension.of(entity).isPacified()) {
            return false;
        } else if (entity instanceof ISafeChangedEntity safeChanged) {
            return safeChanged.shouldScareVillagers(entity, villager);
        }
        return original;
    }
}
