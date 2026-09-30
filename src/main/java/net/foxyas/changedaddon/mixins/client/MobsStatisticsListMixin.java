package net.foxyas.changedaddon.mixins.client;

import net.minecraft.client.gui.screens.achievement.StatsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StatsScreen.MobsStatisticsList.class)
public abstract class MobsStatisticsListMixin {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ObjectSelectionList;<init>(Lnet/minecraft/client/Minecraft;IIIII)V"), index = 5, method = "<init>")
    private static int modifyHeight(int height) {
        return height + 9;
    }
}
