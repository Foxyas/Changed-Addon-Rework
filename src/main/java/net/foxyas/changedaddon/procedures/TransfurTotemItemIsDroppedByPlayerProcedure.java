package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

@Mod.EventBusSubscriber
public class TransfurTotemItemIsDroppedByPlayerProcedure {
    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingUpdateEvent event) {
        execute(event.getEntity().level, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
    }

    private static void execute(Level world, double x, double y, double z) {
        if (world instanceof ServerLevel serverLevel) {
            serverLevel.getEntities(null, entity -> entity instanceof ItemEntity && entity.distanceToSqr(x, y, z) < 1).forEach(entity -> {
                ItemEntity itemEntity = (ItemEntity) entity;
                ItemStack itemStack = itemEntity.getItem();
                if (itemStack.is(ChangedAddonModItems.TRANSFUR_TOTEM.get())) {
                    itemEntity.setGlowing(true);
                    itemEntity.lifespan = 10000;
                }
            });
        }
    }
}
