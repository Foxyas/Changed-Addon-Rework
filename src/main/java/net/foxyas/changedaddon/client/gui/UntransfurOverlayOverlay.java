
package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class UntransfurOverlayOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGameOverlayEvent.Pre event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			int w = event.getWindow().getGuiScaledWidth();
			int h = event.getWindow().getGuiScaledHeight();
			int posX = w;
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
			double progressUTF = (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).UntransfurProgress;
			double progress = progressUTF / 8.33;
			int progressint = (int)progress;



			boolean canshow = ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).UntransfurProgress > 0);

			if  (canshow){
				
				RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/untransfurprogress.png"));
				Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX + -103, posY + 36, 0, 0, 14, 5, 14, 5);

				RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/untransfurprogress_full.png"));
				Minecraft.getInstance().gui.blit(event.getMatrixStack(), posX + -102, posY + 37, 0, 0, progressint, 3, progressint, 3);

			}
			RenderSystem.depthMask(true);
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			RenderSystem.disableBlend();
			RenderSystem.setShaderColor(1, 1, 1, 1);
		}
	}
}
