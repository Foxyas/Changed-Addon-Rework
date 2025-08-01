package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.bosses.Experiment10BossEntity;
import net.foxyas.changedaddon.entity.bosses.KetExperiment009BossEntity;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = {Dist.CLIENT})
public class Experiment10FogColorProcessProcedure {

    private static final float[] COLOR_10 = {61 / 255.0f, 0f, 0f};
    private static final float[] COLOR_009 = {0f, 194 / 255.0f, 219 / 255.0f};
    private static final float[] COLOR_MIX = {126 / 255.0f, 0f, 217 / 255.0f};

    @SubscribeEvent
    public static void computeFogColor(EntityViewRenderEvent.FogColors event) {
        try {
            ClientLevel clientLevel = Minecraft.getInstance().level;
            Entity entity = event.getCamera().getEntity();
            if (entity != null) {
                applyFogColor(clientLevel, entity.position(), entity, event);
            }
        } catch (Exception ignored) {
        }
    }

    private static void applyFogColor(LevelAccessor world, Vec3 pos, Entity entity, EntityViewRenderEvent.FogColors viewport) {
        if (!(entity instanceof LivingEntity living)) return;
        if (isInCreativeOrSpectator(entity)) return;

        boolean has10DNA = hasItem(living, ChangedAddonItems.EXPERIMENT_10_DNA.get());
        boolean has009DNA = hasItem(living, ChangedAddonItems.EXPERIMENT_009_DNA.get());

        // Fog by item
        if (has10DNA && !has009DNA) {
            setFogColor(viewport, COLOR_10);
        } else if (has009DNA && !has10DNA) {
            setFogColor(viewport, COLOR_009);
        } else if (has10DNA && has009DNA) {
            setFogColor(viewport, COLOR_MIX);
        }

        // Fog by nearby boss entities
        if (!entity.getPersistentData().getBoolean("NoAI")) {
            if (isEntityNearby(world, pos, Experiment10BossEntity.class, 50)) {
                setFogColor(viewport, COLOR_10);
            }
            if (isEntityNearby(world, pos, KetExperiment009BossEntity.class, 50)) {
                setFogColor(viewport, COLOR_009);
            }
        }
    }

    private static boolean hasItem(LivingEntity entity, net.minecraft.world.item.Item item) {
        return entity.getMainHandItem().is(item) || entity.getOffhandItem().is(item);
    }

    private static boolean isEntityNearby(LevelAccessor world, Vec3 pos, Class<? extends Entity> clazz, double range) {
        AABB box = AABB.ofSize(pos, range, range, range);
        return world.getEntitiesOfClass(clazz, box, e -> true).stream().findAny().isPresent();
    }

    private static void setFogColor(EntityViewRenderEvent.FogColors fog, float[] rgb) {
        fog.setRed(rgb[0]);
        fog.setGreen(rgb[1]);
        fog.setBlue(rgb[2]);
    }

    private static boolean isInCreativeOrSpectator(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            GameType type = serverPlayer.gameMode.getGameModeForPlayer();
            return type == GameType.CREATIVE || type == GameType.SPECTATOR;
        } else if (entity.level.isClientSide() && entity instanceof Player player) {
            var info = Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId());
            if (info != null) {
                GameType type = info.getGameMode();
                return type == GameType.CREATIVE || type == GameType.SPECTATOR;
            }
        }
        return false;
    }
}
