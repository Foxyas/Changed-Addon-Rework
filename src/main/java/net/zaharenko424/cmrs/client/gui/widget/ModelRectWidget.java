package net.zaharenko424.cmrs.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import org.apache.commons.lang3.function.ToBooleanBiFunction;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public abstract class ModelRectWidget extends ModelWidget {

    protected BiConsumer<ModelRectWidget, PoseStack> renderTransform;
    protected ToBooleanBiFunction<ModelRectWidget, Integer> onClick;


    /**
     * No need to rebuild mesh after this call.
     */
    public <T extends ModelRectWidget> T setRenderTransform(@Nullable BiConsumer<ModelRectWidget, PoseStack> renderTransform){
        this.renderTransform = renderTransform;
        return (T) this;
    }

    /**
     * No need to rebuild mesh after this call.
     * @param onClick Return true if click is consumed.
     */
    public  <T extends ModelRectWidget> T setOnClick(@Nullable ToBooleanBiFunction<ModelRectWidget, Integer> onClick){
        this.onClick = onClick;
        return (T) this;
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button) && onClick != null && onClick.applyAsBoolean(this, button);
    }

    @Override
    protected void renderModel(GuiGraphics guiGraphics) {
        if(renderTransform != null) renderTransform.accept(this, guiGraphics.pose());
    }
}
