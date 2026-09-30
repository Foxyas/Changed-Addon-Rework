package net.foxyas.changedaddon.mixins.client;

import net.foxyas.changedaddon.init.ChangedAddonStatRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatsScreen.MobsStatisticsList.MobRow.class)
public abstract class MobRowMixin {

    @Unique
    private int pattedTimes;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void onInit(StatsScreen.MobsStatisticsList pEntityType, EntityType<?> $$1, CallbackInfo ci) {
        pattedTimes = Minecraft.getInstance().player.getStats().getValue(ChangedAddonStatRegistry.ENTITY_PATTED.get().get($$1));
    }

    @Inject(at = @At("RETURN"), method = "render")
    private void onRender(GuiGraphics pGuiGraphics, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pHovering, float pPartialTick, CallbackInfo ci) {
        pGuiGraphics.drawString(Minecraft.getInstance().font, Component.literal("Pats given: " + pattedTimes), pLeft + 2 + 10, pTop + 1 + 9 * 3, pattedTimes > 0 ? 9474192 : 6316128);
    }
}
