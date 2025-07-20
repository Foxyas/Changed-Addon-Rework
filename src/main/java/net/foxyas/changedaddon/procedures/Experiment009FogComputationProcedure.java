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
    public static void renderFog(EntityViewRenderEvent.RenderFogEvent event) {
        try {
            if (event.getMode() == FogRenderer.FogMode.FOG_TERRAIN) {
                ClientLevel clientLevel = Minecraft.getInstance().level;
                Entity entity = event.getCamera().getEntity();
                execute(null, clientLevel, entity.getX(), entity.getY(), entity.getZ(), entity, event);
                event.setCanceled(true);
            }
        } catch (Exception e) {
        }
    }

    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, EntityViewRenderEvent viewport) {
        execute(null, world, x, y, z, entity, viewport);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, EntityViewRenderEvent viewport) {
        if (entity == null || viewport == null)
            return;
        if (!(new Object() {
            public boolean checkGamemode(Entity _ent) {
                if (_ent instanceof ServerPlayer _serverPlayer) {
                    return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                } else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
                    return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
                }
                return false;
            }
        }.checkGamemode(entity) || new Object() {
            public boolean checkGamemode(Entity _ent) {
                if (_ent instanceof ServerPlayer _serverPlayer) {
                    return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
                } else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
                    return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
                }
                return false;
            }
        }.checkGamemode(entity))) {
            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ChangedAddonItems.EXPERIMENT_009_DNA.get()
                    || (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == ChangedAddonItems.EXPERIMENT_009_DNA.get()) {
                if (viewport instanceof EntityViewRenderEvent.RenderFogEvent _renderFogEvent) {
                    _renderFogEvent.setFogShape(FogShape.SPHERE);
                }
                if (viewport instanceof EntityViewRenderEvent.RenderFogEvent _renderFogEvent) {
                    _renderFogEvent.setNearPlaneDistance(1);
                    _renderFogEvent.setFarPlaneDistance(10);
                }
            }
            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ChangedAddonItems.EXPERIMENT_10_DNA.get()
                    || (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == ChangedAddonItems.EXPERIMENT_10_DNA.get()) {
                if (viewport instanceof EntityViewRenderEvent.RenderFogEvent _renderFogEvent) {
                    _renderFogEvent.setFogShape(FogShape.SPHERE);
                }
                if (viewport instanceof EntityViewRenderEvent.RenderFogEvent _renderFogEvent) {
                    _renderFogEvent.setNearPlaneDistance(1);
                    _renderFogEvent.setFarPlaneDistance(10);
                }
            }
        }
    }
}
