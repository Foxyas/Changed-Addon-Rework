package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DazedBurnUnderTheSun {

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level;

        if (!level.getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.DO_DAZED_LATEX_BURN)
                || entity.hasEffect(MobEffects.FIRE_RESISTANCE)) return;

        if (!ProcessTransfur.getEntityVariant(entity).map(var -> var.is(ChangedAddonTransfurVariants.DAZED_LATEX) || var.is(ChangedAddonTransfurVariants.BUFF_DAZED_LATEX))
                .orElse(Boolean.FALSE)) return;

        if (level.canSeeSkyFromBelowWater(entity.blockPosition()) && level.isDay() && !entity.isInWaterRainOrBubble()) {
            if (entity.getHealth() / entity.getMaxHealth() >= 0.4) {
                if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == Blocks.AIR.asItem()) {
                    entity.setSecondsOnFire(2);
                }
            }
        }
    }
}
