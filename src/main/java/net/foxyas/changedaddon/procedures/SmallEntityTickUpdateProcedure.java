package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.advanced.DazedEntity;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class SmallEntityTickUpdateProcedure {
    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingUpdateEvent event) {
        execute(event, event.getEntityLiving().level, event.getEntityLiving());
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null)
            return;
        if (world.getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.DO_DAZED_LATEX_BURN)) {
            if (entity instanceof DazedEntity livEnt) {
                if (world.canSeeSkyFromBelowWater(new BlockPos(entity.getX(), entity.getY(), entity.getZ())) && world instanceof Level _lvl6 && _lvl6.isDay() && !entity.isInWaterRainOrBubble()) {
                    if (livEnt.getHealth() / livEnt.getMaxHealth() >= 0.4) {
                        if (livEnt.getItemBySlot(EquipmentSlot.HEAD).getItem() == Blocks.AIR.asItem()) {
                            if (!livEnt.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                                entity.setSecondsOnFire(2);
                            }
                        }
                    }
                }
            } else if (entity instanceof Player player) {
                if (ProcessTransfur.getPlayerTransfurVariant(player) != null && ProcessTransfur.getPlayerTransfurVariant(player).getFormId().toString().equals("changed_addon:form_dazed_latex")) {
                    if (world.canSeeSkyFromBelowWater(new BlockPos(entity.getX(), entity.getY(), entity.getZ())) && world instanceof Level _lvl19 && _lvl19.isDay() && !entity.isInWaterRainOrBubble()) {
                        if (player.getHealth() / player.getMaxHealth() >= 0.25) {
                            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == Blocks.AIR.asItem()) {
                                if (!player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                                    entity.setSecondsOnFire(2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
