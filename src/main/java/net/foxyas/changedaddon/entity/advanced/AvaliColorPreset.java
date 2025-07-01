package net.foxyas.changedaddon.entity.advanced;

import net.ltxprogrammer.changed.util.Color3;

import java.util.Random;

public enum AvaliColorPreset {
    CLASSIC(new Color3(255, 230, 200), new Color3(150, 100, 50), new Color3(50, 25, 10), "male"),
    SNOW(Color3.WHITE, new Color3(200, 200, 200), new Color3(180, 180, 180), "female"),
    DESERT(new Color3(220, 180, 120), new Color3(190, 140, 80), new Color3(140, 100, 60), "male"),
    JUNGLE(new Color3(60, 120, 40), new Color3(40, 80, 30), new Color3(20, 40, 10), "female"),
    VOID(Color3.BLACK, new Color3(30, 30, 30), new Color3(60, 0, 60), "male"),
    SKY(new Color3(180, 220, 255), new Color3(120, 180, 255), new Color3(80, 120, 200), "female");

    public final Color3 primary;
    public final Color3 secondary;
    public final Color3 stripes;
    public final String style;

    AvaliColorPreset(Color3 primary, Color3 secondary, Color3 stripes, String style) {
        this.primary = primary;
        this.secondary = secondary;
        this.stripes = stripes;
        this.style = style;
    }

    public static AvaliColorPreset random(Random random) {
        AvaliColorPreset[] values = values();
        return values[random.nextInt(values.length)];
    }
}
