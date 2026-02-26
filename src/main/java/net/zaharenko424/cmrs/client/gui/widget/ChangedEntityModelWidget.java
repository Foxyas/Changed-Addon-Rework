package net.zaharenko424.cmrs.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ChangedEntityModelWidget extends ModelRectWidget {

    protected ChangedEntity changedEntity;

    protected boolean scrollAble = false;

    @Override
    public ChangedEntityModelWidget setOrigin(float x, float y, float z) {
        super.setOrigin(x, y, z);
        return this;
    }

    @Override
    public ChangedEntityModelWidget setScale(float x, float y, float z) {
        super.setScale(x, y, z);
        return this;
    }

    @Override
    public ChangedEntityModelWidget setSize(float width, float height) {
        super.setSize(width, height);
        return this;
    }

    @Override
    public ChangedEntityModelWidget setRotation(float radX, float radY) {
        super.setRotation(radX, radY);
        return this;
    }

    @Override
    public ChangedEntityModelWidget setZoom(float zoom) {
        super.setZoom(zoom);
        return this;
    }

    public ChangedEntityModelWidget setChangedEntity(ChangedEntity changedEntity){
        this.changedEntity = changedEntity;
        return this;
    }

    public ChangedEntity getChangedEntity() {
        return changedEntity;
    }

    public void setScrollAble(boolean scrollAble) {
        this.scrollAble = scrollAble;
    }

    public boolean isScrollAble() {
        return scrollAble;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        return isScrollAble() && super.mouseScrolled(mouseX, mouseY, scrollY);
    }

    protected void renderModel(PoseStack stack){
        super.renderModel(stack);
        if(changedEntity == null) return;
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadow(false);
        entityRenderDispatcher.render(changedEntity, 0, 0, 0, 0, 1, stack, Minecraft.getInstance().renderBuffers().bufferSource(), LightTexture.FULL_BRIGHT);
        entityRenderDispatcher.setRenderShadow(true);

    }
}
