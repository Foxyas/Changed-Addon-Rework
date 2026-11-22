package net.foxyas.changedaddon.client.renderer.depth_outline;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

@OnlyIn(Dist.CLIENT)
public final class GlowHandles {
    private static final Map<Entity, OutlineHandle> MAP = new WeakHashMap<>();
    private static final Map<Entity, Integer> VANILLA_COLORS = new WeakHashMap<>();

    public static OutlineHandle of(Entity e) { return MAP.computeIfAbsent(e, k -> new OutlineHandle()); }
    @Nullable public static OutlineHandle get(Entity e) { return MAP.get(e); }

    // ---- util: VANILLA outline color ----

    /** Sets a color (RGB) for this entity's vanilla outline. */
    public static void setVanillaColor(Entity e, int rgb) { VANILLA_COLORS.put(e, rgb); }

    /** Removes the override and falls back to the team color. */
    public static void clearVanillaColor(Entity e) { VANILLA_COLORS.remove(e); }

    /** Color the vanilla outline should use: the override if present, otherwise the team color. */
    public static int vanillaColor(Entity e) {
        Integer c = VANILLA_COLORS.get(e);
        return c != null ? c : e.getTeamColor();
    }
}