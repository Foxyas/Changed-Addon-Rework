
package net.foxyas.changedaddon.client.gui;

import org.checkerframework.checker.units.qual.h;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.procedures.ShowfriendlymodeofficonProcedure;
import net.foxyas.changedaddon.procedures.ShowfriendlymodeIconProcedure;
import net.foxyas.changedaddon.procedures.ShowcangrabiconProcedure;
import net.foxyas.changedaddon.procedures.ShowassimilationoffbuttonProcedure;
import net.foxyas.changedaddon.procedures.ShowassimilationiconProcedure;
import net.foxyas.changedaddon.procedures.ShowOrganicFriendlyModeonProcedure;
import net.foxyas.changedaddon.procedures.ShowOrganicFriendlyModeOffProcedure;
import net.foxyas.changedaddon.procedures.ShowGrabInFriendlyModeoffProcedure;
import net.foxyas.changedaddon.procedures.ShowGrabInFriendlyModeOnProcedure;
import net.foxyas.changedaddon.procedures.ShocantgrabiconProcedure;
import net.foxyas.changedaddon.procedures.FriendlymodeoverlayDisplayOverlayIngameProcedure;
import net.foxyas.changedaddon.procedures.*;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class GrabModeOverlaysOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGameOverlayEvent.Pre event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			int w = event.getWindow().getGuiScaledWidth();
			int h = event.getWindow().getGuiScaledHeight();
			int posX = 5;
			int posY = h;
			/* 
			 *  posY-numero = up
			 *  posY+numero = down
			 *  h/2 = Y mid of screen 
			 *  w/2 = X mid of screen
			 *  with h/2
			 *	61 = 44 + 17
			 *	44 = 27 + 17
			 *	27 = area base cool :D
			 *	without h/2
			 *	89
			 *	78
			 *	61 <-> 59
			*/
			Level world = null;
			double x = 0;
			double y = 0;
			double z = 0;
			Player entity = Minecraft.getInstance().player;
			if (entity != null) {
				world = entity.level;
				x = entity.getX();
				y = entity.getY();
				z = entity.getZ();
			}
			RenderSystem.disableDepthTest();
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			if (false/*FriendlymodeoverlayDisplayOverlayIngameProcedure.execute(entity)*/) {
				if (ShowassimilationiconProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/assimilation.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX , posY - 78, 0, 0, 16, 16, 16, 16);
				}
				if (ShowGrabInFriendlyModeoffProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/friendly_transfur_false.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX , posY - 98, 0, 0, 18, 18, 18, 18);
				}
				if (ShowGrabInFriendlyModeOnProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/friendly_transfur_true.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX , posY - 98, 0, 0, 18, 18, 18, 18);
				}
				if (ShowassimilationoffbuttonProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/grab_overlay_v2.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX , posY - 78, 0, 0, 16, 16, 16, 16);
				}
				if (ShowfriendlymodeIconProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/friendlymodeicon.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX, posY - 61, 0, 0, 16, 16, 16, 16);
				}
				if (ShowfriendlymodeofficonProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/friendlymodeofficon.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX, posY - 61, 0, 0, 16, 16, 16, 16);
				}
				if (ShowcangrabiconProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/can_grab_icon_outline.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX, posY - 95, 0, 0, 16, 16, 16, 16);
				}
				if (ShocantgrabiconProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/cant_grab_icon_outline.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX , posY - 95, 0, 0, 16, 16, 16, 16);
				}
				if (ShowOrganicFriendlyModeOffProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/organic_friendly_mode_off_v2.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX , posY - 61, 0, 0, 16, 16, 16, 16);
				}
				if (ShowOrganicFriendlyModeonProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/organic_friendly_mode_on_v2.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX , posY - 61, 0, 0, 16, 16, 16, 16);
				}
				if (ShowOrganicOverLayProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/organic_form_overlay.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX, posY - 78, 0, 0, 16, 16, 16, 16);
				}

			}
			RenderSystem.depthMask(true);
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			RenderSystem.disableBlend();
			RenderSystem.setShaderColor(1, 1, 1, 1);
		}
	}
};

 @Mod.EventBusSubscriber({Dist.CLIENT})
  class WantFriendlyGrabOverlayOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGameOverlayEvent.Pre event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			int w = event.getWindow().getGuiScaledWidth();
			int h = event.getWindow().getGuiScaledHeight();
			int posX = 5;
			int posY = h;
			Level world = null;
			double x = 0;
			double y = 0;
			double z = 0;
			Player entity = Minecraft.getInstance().player;
			if (entity != null) {
				world = entity.level;
				x = entity.getX();
				y = entity.getY();
				z = entity.getZ();
			}
			RenderSystem.disableDepthTest();
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			if (false/*WantFriendlyGrabOverlayDisplayOverlayIngameProcedure.execute(entity)*/) {
				if (ShowWantFriendlygrabfalseProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/dont_want_friendly_grab.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX , posY - 61, 0, 0, 16, 16, 16, 16);
				}
				if (ShowWantFriendlygrabtrueProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/want_friendly_grab.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX, posY - 61, 0, 0, 16, 16, 16, 16);
				}
			}
			RenderSystem.depthMask(true);
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			RenderSystem.disableBlend();
			RenderSystem.setShaderColor(1, 1, 1, 1);
		}
	}
}

