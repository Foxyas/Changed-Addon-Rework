package net.foxyas.changedaddon.procedures;

import org.checkerframework.checker.units.qual.A;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.entity.Experiment009Entity;

import javax.annotation.Nullable;

import java.util.Comparator;

import com.mojang.blaze3d.shaders.FogShape;

@Mod.EventBusSubscriber(value = {Dist.CLIENT})
public class TestProcedure {
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
		double A = 0;
		double deltaZ = 0;
		double distance = 0;
		double deltaX = 0;
		double deltaY = 0;
		if (!(((Entity) world.getEntitiesOfClass(Experiment009Entity.class, AABB.ofSize(new Vec3(x, y, z), 50, 50, 50), e -> true).stream().sorted(new Object() {
			Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
				return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
			}
		}.compareDistOf(x, y, z)).findFirst().orElse(null)) == (null))) {
			if (viewport instanceof EntityViewRenderEvent.RenderFogEvent _renderFogEvent) {
				_renderFogEvent.setFogShape(FogShape.CYLINDER);
			}
			if (viewport instanceof EntityViewRenderEvent.FogColors _fogColors) {
				_fogColors.setRed(0 / 255.0F);
				_fogColors.setGreen(0 / 255.0F);
				_fogColors.setBlue(0 / 255.0F);
			}
			deltaX = ((Entity) world.getEntitiesOfClass(Experiment009Entity.class, AABB.ofSize(new Vec3(x, y, z), 50, 50, 50), e -> true).stream().sorted(new Object() {
				Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
					return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
				}
			}.compareDistOf(x, y, z)).findFirst().orElse(null)).getX() - entity.getX();
			deltaY = ((Entity) world.getEntitiesOfClass(Experiment009Entity.class, AABB.ofSize(new Vec3(x, y, z), 50, 50, 50), e -> true).stream().sorted(new Object() {
				Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
					return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
				}
			}.compareDistOf(x, y, z)).findFirst().orElse(null)).getY() - entity.getY();
			deltaZ = ((Entity) world.getEntitiesOfClass(Experiment009Entity.class, AABB.ofSize(new Vec3(x, y, z), 50, 50, 50), e -> true).stream().sorted(new Object() {
				Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
					return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
				}
			}.compareDistOf(x, y, z)).findFirst().orElse(null)).getZ() - entity.getZ();
			distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
			if (viewport instanceof EntityViewRenderEvent.RenderFogEvent _renderFogEvent) {
				_renderFogEvent.setNearPlaneDistance(1);
				_renderFogEvent.setFarPlaneDistance((float) Math.max(5, distance));
			}
		}
	}
}
