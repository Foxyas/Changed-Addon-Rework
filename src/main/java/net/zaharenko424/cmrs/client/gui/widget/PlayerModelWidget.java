package net.zaharenko424.cmrs.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;

public class PlayerModelWidget extends ModelWidget {

    protected AbstractClientPlayer player;

    @Override
    public PlayerModelWidget setOrigin(float x, float y, float z) {
        super.setOrigin(x, y, z);
        return this;
    }

    @Override
    public PlayerModelWidget setScale(float x, float y, float z) {
        super.setScale(x, y, z);
        return this;
    }

    @Override
    public PlayerModelWidget setSize(float width, float height) {
        super.setSize(width, height);
        return this;
    }

    @Override
    public PlayerModelWidget setRotation(float radX, float radY) {
        super.setRotation(radX, radY);
        return this;
    }

    @Override
    public PlayerModelWidget setZoom(float zoom) {
        super.setZoom(zoom);
        return this;
    }

    public PlayerModelWidget setPlayer(AbstractClientPlayer player){
        this.player = player;
        return this;
    }

    public AbstractClientPlayer getPlayer() {
        return player;
    }

    protected void renderModel(GuiGraphics guiGraphics, float partialTick){
        if(player == null) return;
        Minecraft.getInstance().getEntityRenderDispatcher().render(player, 0, 0, 0, 0, partialTick, guiGraphics.pose(), guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT);
    }
}
