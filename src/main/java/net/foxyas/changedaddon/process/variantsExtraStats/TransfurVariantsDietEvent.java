package net.foxyas.changedaddon.process.variantsExtraStats;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TransfurVariantsDietEvent {

    @SubscribeEvent
    public static void HaydenFoodStats(FormDietEvent event) {
        if (event.getVariant() != null && event.getVariant().is(ChangedAddonTransfurVariants.HAYDEN_FENNEC_FOX)) {
            if (event.getItemStack().is(Items.COOKIE)) {
                event.getEntity().addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 5, 1));
                event.additionalFood *= 4;
                event.additionalSaturation *= 8;
            }
        }
    }
}
