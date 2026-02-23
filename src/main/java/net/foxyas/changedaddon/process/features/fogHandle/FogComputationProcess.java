package net.foxyas.changedaddon.process.features.fogHandle;

import com.mojang.blaze3d.shaders.FogShape;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.foxyas.changedaddon.process.features.fogHandle.FogLerpState.lerp;

@Mod.EventBusSubscriber(value = {Dist.CLIENT})
public class FogComputationProcess {

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        //if(event.getMode() != FogRenderer.FogMode.FOG_TERRAIN) return;

        try {
            ClientLevel clientLevel = Minecraft.getInstance().level;
            Entity entity = event.getCamera().getEntity();
            if (!(entity instanceof LivingEntity living)) return;
            if (clientLevel != null) {
                applyFogEffect(living, event);
            }
        } catch (Exception ignored) {
            // You can log the error here if needed
        }
    }

    protected static void applyFogEffect(LivingEntity entity, ViewportEvent viewport) {
        if (entity == null || !(viewport instanceof ViewportEvent.RenderFog fogEvent)) return;
        if (entity.isSpectator() || entity instanceof Player player && player.isCreative()) return;

        boolean isHolding = isHoldingItem(entity, ChangedAddonItems.EXPERIMENT_009_DNA.get()) ||
                isHoldingItem(entity, ChangedAddonItems.EXPERIMENT_10_DNA.get());

        float partialTicks = ClientFogData.FOG.get();

        float farPlaneDistance = fogEvent.getFarPlaneDistance();
        float nearPlaneDistance = fogEvent.getNearPlaneDistance();

        float farPlane = lerp(farPlaneDistance, 10, partialTicks);
        float nearPlane = lerp(nearPlaneDistance, -1, partialTicks);

        if (!isHolding) {
            farPlane = lerp(farPlaneDistance, 20, partialTicks);
            nearPlane = lerp(nearPlaneDistance, 10, partialTicks);
        }

        fogEvent.setFogShape(FogShape.SPHERE);
        fogEvent.setNearPlaneDistance(nearPlane);
        fogEvent.setFarPlaneDistance(farPlane);

        if (ClientFogData.FOG.isActive()) {
            fogEvent.setCanceled(true);
        }
    }

    protected static boolean isHoldingItem(LivingEntity entity, Item item) {
        return entity.getMainHandItem().getItem() == item ||
                entity.getOffhandItem().getItem() == item;
    }
}
