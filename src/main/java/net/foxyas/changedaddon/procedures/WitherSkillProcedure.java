package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.bosses.Experiment10BossEntity;
import net.foxyas.changedaddon.entity.bosses.Experiment10Entity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class WitherSkillProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        if (entity != null) {
            execute(event, entity, event.getSource().getDirectEntity());
        }
    }

    public static void execute(Entity entity, Entity immediatesourceentity) {
        execute(null, entity, immediatesourceentity);
    }

    private static void execute(@Nullable Event event, Entity entity, Entity immediatesourceentity) {
        if (entity == null || immediatesourceentity == null)
            return;
        if (immediatesourceentity instanceof Experiment10Entity a && a.isPhase2()) {
            if (a.getMainHandItem().getItem() == Blocks.AIR.asItem()) {
                if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                    _entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 90, 2, false, true));
            }
        } else if (immediatesourceentity instanceof Experiment10Entity a && !a.isPhase2()) {
            if (a.getMainHandItem().getItem() == Blocks.AIR.asItem()) {
                if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                    _entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 90, 0, false, true));
            }
        }
        if (immediatesourceentity instanceof Experiment10BossEntity a && a.isPhase2()) {
            if (a.getMainHandItem().getItem() == Blocks.AIR.asItem()) {
                if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                    _entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 90, 2, false, true));
            }
        } else if (immediatesourceentity instanceof Experiment10BossEntity a && !a.isPhase2()) {
            if (a.getMainHandItem().getItem() == Blocks.AIR.asItem()) {
                if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                    _entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 90, 0, false, true));
            }
        }
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance != null) {
                if (instance.getFormId().toString().equals("changed_addon:form_experiment_10")) {
                    if ((immediatesourceentity instanceof LivingEntity _livEnt && _livEnt.getMainHandItem().getItem() == Blocks.AIR.asItem())) {
                        if (!player.level.isClientSide())
                            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 90, 0, false, true));
                    }
                }
            }
        }
    }
}
