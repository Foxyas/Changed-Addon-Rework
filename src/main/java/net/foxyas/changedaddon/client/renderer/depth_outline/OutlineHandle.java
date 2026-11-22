package net.foxyas.changedaddon.client.renderer.depth_outline;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/** Custom (depth-tested) outline of an entity. color = RGB; -1 uses the team color. */
@OnlyIn(Dist.CLIENT)
public final class OutlineHandle {
    private boolean enabled;
    private int color = -1;

    public OutlineHandle enable(int rgb) { enabled = true; color = rgb; return this; }
    public OutlineHandle enable()        { enabled = true; color = -1;  return this; }
    public OutlineHandle disable()       { enabled = false; return this; }
    public boolean enabled() { return enabled; }
    public int color()       { return color; }
}