package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;

@Mod.EventBusSubscriber
public class TransfurTotemItemIsDroppedByPlayerProcedure {

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingUpdateEvent event) {
        Level world = event.getEntity().level;
        double x = event.getEntity().getX();
        double y = event.getEntity().getY();
        double z = event.getEntity().getZ();

        if (world instanceof ServerLevel serverLevel) {
            ItemEntity nearestItemEntity = serverLevel.getEntitiesOfClass(ItemEntity.class,
                            AABB.ofSize(new Vec3(x, y, z), 4, 4, 4))
                    .stream()
                    .filter(itemEntity -> itemEntity.getItem().is(ChangedAddonItems.TRANSFUR_TOTEM.get()))
                    .min(Comparator.comparingDouble(entity -> entity.distanceToSqr(x, y, z)))
                    .orElse(null);

            if (nearestItemEntity != null) {
                updateItemEntity(nearestItemEntity);
            }
        }
    }

    private static void updateItemEntity(ItemEntity itemEntity) {
        itemEntity.setGlowingTag(true);
        if (itemEntity.lifespan == 6000) {
            itemEntity.lifespan = 10000;
        }
    }
}
