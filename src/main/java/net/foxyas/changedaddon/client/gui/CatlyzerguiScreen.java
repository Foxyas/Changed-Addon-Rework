
package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.procedures.*;
import net.foxyas.changedaddon.world.inventory.CatlyzerGuiMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CatlyzerguiScreen extends AbstractContainerScreen<CatlyzerGuiMenu> {
    private final Level world;
    private final int x, y, z;
    private final Player entity;

    public CatlyzerguiScreen(CatlyzerGuiMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.world = container.world;
        this.x = container.x;
        this.y = container.y;
        this.z = container.z;
        this.entity = container.entity;
        this.imageWidth = 200;
        this.imageHeight = 170;
    }

    private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/catlyzergui.png");

    @Override
    public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderTooltip(ms, mouseX, mouseY);
        if (IfisEmptyProcedure.execute(entity))
            if (mouseX > leftPos + 18 && mouseX < leftPos + 42 && mouseY > topPos + 40 && mouseY < topPos + 64)
                this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.catlyzergui.tooltip_put_the_powders_or_syringe"), mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, texture);
        blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/catlyzergui_new.png"));
        blit(ms, this.leftPos + 0, this.topPos + 0, 0, 0, 200, 170, 200, 170);

        BlockEntity be = world.getBlockEntity(new BlockPos(x, y, z));
        int progressInt = be != null ? (int) (be.getTileData().getDouble("recipe_progress") / 3.57) : -1;

        RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/empty_bar.png"));
        blit(ms, this.leftPos + 83, this.topPos + 46, 0, 0, 32, 12, 32, 12);

        RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_full.png"));
        blit(ms, this.leftPos + 83 + 2, this.topPos + 46 + 2, 0, 0, progressInt, 8, progressInt, 8);

        assert this.minecraft != null;
        assert this.minecraft.level != null;
        long gameTime = this.minecraft.level.getGameTime();
        int animationPeriod = 40; // ticks (2 segundos)
        boolean showSyringe = (gameTime % animationPeriod) < (animationPeriod / 2);

        ResourceLocation icon = showSyringe
                ? new ResourceLocation("changed_addon:textures/screens/syringes.png")
                : new ResourceLocation("changed_addon:textures/screens/dusts.png");

        int yOffset = showSyringe ? 44 : 45;
        RenderSystem.setShaderTexture(0, icon);
        blit(ms, this.leftPos + 23, this.topPos + yOffset, 0, 0, 16, 16, 16, 16);

        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            assert this.minecraft != null;
            assert this.minecraft.player != null;
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack,

                CatlyzerguiValueProcedure.execute(world, x, y, z), 6, 8, -12829636);
        this.font.draw(poseStack,

                BlockstartinfoProcedure.execute(world, x, y, z), 6, 20, -12829636);
        if (IfBlockisfullProcedure.execute(world, x, y, z))
            this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.catlyzergui.label_full"), 151, 65, -12829636);
        this.font.draw(poseStack,

                RecipeProgressProcedure.execute(world, x, y, z), 90, 34, -12829636);
    }
}
