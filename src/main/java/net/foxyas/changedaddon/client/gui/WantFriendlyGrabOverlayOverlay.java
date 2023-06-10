
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

import net.foxyas.changedaddon.procedures.WantFriendlyGrabOverlayDisplayOverlayIngameProcedure;
import net.foxyas.changedaddon.procedures.ShowWantFriendlygrabtrueProcedure;
import net.foxyas.changedaddon.procedures.ShowWantFriendlygrabfalseProcedure;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class WantFriendlyGrabOverlayOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGameOverlayEvent.Pre event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			int w = event.getWindow().getGuiScaledWidth();
			int h = event.getWindow().getGuiScaledHeight();
			int posX = w / 2;
			int posY = h / 2;
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
			if (WantFriendlyGrabOverlayDisplayOverlayIngameProcedure.execute(entity)) {
				if (ShowWantFriendlygrabfalseProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/dont_want_friendly_grab.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX + -212, posY + 28, 0, 0, 16, 16, 16, 16);
				}
				if (ShowWantFriendlygrabtrueProcedure.execute(entity)) {
					RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/want_friendly_grab.png"));
					Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX + -212, posY + 28, 0, 0, 16, 16, 16, 16);
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
