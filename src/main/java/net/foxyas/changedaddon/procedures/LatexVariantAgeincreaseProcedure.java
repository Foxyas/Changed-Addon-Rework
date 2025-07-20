package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class LatexVariantAgeIncreaseProcedure {
    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (event != null && event.getEntity() != null) {
            execute(event, event.getEntity().level, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getItem());
        }
    }

    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
        execute(null, world, x, y, z, entity, itemstack);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        double number = 0;
        if (itemstack.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:white_latex_goo"))) {
            if (entity instanceof Player player) {
                TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
                if (instance != null) {
                    if (instance.getFormId().toString().equals("changed:form_dark_latex_pup")) {
                        ReturnTransfurModeProcedure.setPlayerLatexAge.execute(player, 5000, true);
                        if (world instanceof ServerLevel _level)
                            _level.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 5, 0.3, 0.5, 0.3, 1);
                    }
                }
            }
        }
    }
}