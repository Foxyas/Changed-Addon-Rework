package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.util.Color3;
import org.jetbrains.annotations.Nullable;

public interface IDynamicColors {

    @Nullable("leave null if you want to keep the default color handle")
    DynamicColorScheme getColorScheme();

    // Side Safe Version of AbstractRadialScreen.ColorScheme
    record DynamicColorScheme(Color3 background, Color3 foreground) {
        public DynamicColorScheme setForegroundToBright() {
            Color3 newBack = this.background;
            Color3 newFore = this.foreground;
            if (this.background.brightness() > this.foreground.brightness()) {
                newBack = this.foreground;
                newFore = this.background;
            }

            if (newBack.brightness() < 0.0625F) {
                newBack = newBack.add(0.0625F).clamp();
            }

            if (newFore.brightness() - newBack.brightness() < 0.125F) {
                newFore = newFore.add(0.125F).clamp();
            }

            return new DynamicColorScheme(newBack, newFore);
        }
    }
}
