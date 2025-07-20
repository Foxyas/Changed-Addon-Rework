package net.foxyas.changedaddon.procedures;

import com.mojang.blaze3d.shaders.FogShape;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(value = {Dist.CLIENT})
public class Experiment009FogComputationProcedure {

    @SubscribeEvent
    public static void onRenderFog(EntityViewRenderEvent.RenderFogEvent event) {
        if (event.getMode() == FogRenderer.FogMode.FOG_TERRAIN) {
            try {
                ClientLevel clientLevel = Minecraft.getInstance().level;
                Entity entity = event.getCamera().getEntity();
                if (entity != null && clientLevel != null) {
                    applyFogEffect(clientLevel, entity.getX(), entity.getY(), entity.getZ(), entity, event);
                    event.setCanceled(true);
                }
            } catch (Exception ignored) {
                // You can log the error here if needed
            }
        }
    }

    public static void applyFogEffect(LevelAccessor world, double x, double y, double z, Entity entity, EntityViewRenderEvent viewport) {
        applyFogEffect(null, world, x, y, z, entity, viewport);
    }

    private static void applyFogEffect(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, EntityViewRenderEvent viewport) {
        if (entity == null || !(viewport instanceof EntityViewRenderEvent.RenderFogEvent fogEvent)) return;
        if (isCreativeOrSpectator(entity)) return;

        if (isHoldingItem(entity, ChangedAddonItems.EXPERIMENT_009_DNA.get()) ||
                isHoldingItem(entity, ChangedAddonItems.EXPERIMENT_10_DNA.get())) {

            fogEvent.setFogShape(FogShape.SPHERE);
            fogEvent.setNearPlaneDistance(1);
            fogEvent.setFarPlaneDistance(10);
        }
    }

    private static boolean isCreativeOrSpectator(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            GameType mode = serverPlayer.gameMode.getGameModeForPlayer();
            return mode == GameType.CREATIVE || mode == GameType.SPECTATOR;
        } else if (entity.level.isClientSide() && entity instanceof Player player) {
            var info = Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId());
            if (info != null) {
                GameType mode = info.getGameMode();
                return mode == GameType.CREATIVE || mode == GameType.SPECTATOR;
            }
        }
        return false;
    }

    private static boolean isHoldingItem(Entity entity, net.minecraft.world.item.Item item) {
        if (!(entity instanceof LivingEntity livingEntity)) return false;
        return livingEntity.getMainHandItem().getItem() == item ||
                livingEntity.getOffhandItem().getItem() == item;
    }
}
