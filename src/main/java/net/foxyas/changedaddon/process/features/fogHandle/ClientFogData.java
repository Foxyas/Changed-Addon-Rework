package net.foxyas.changedaddon.process.features.fogHandle;

import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientFogData {

    public static final FogLerpState FOG = new FogLerpState();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean holding =
                isHolding(mc.player, ChangedAddonItems.EXPERIMENT_009_DNA.get()) ||
                        isHolding(mc.player, ChangedAddonItems.EXPERIMENT_10_DNA.get());

        FOG.setTarget(holding);
        FOG.tick(0.15f, 0.005f);
    }

    private static boolean isHolding(LivingEntity entity, Item item) {
        return entity.getMainHandItem().is(item) || entity.getOffhandItem().is(item);
    }
}
