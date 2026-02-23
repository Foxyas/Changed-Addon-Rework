package net.foxyas.changedaddon.util;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeableLeatherItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorUtil {

    private static final Object2IntMap<DyeColor> COLOR_CACHE = new Object2IntArrayMap<>();

    public static int dyeToARGB(DyeColor dye) {
        return COLOR_CACHE.computeIfAbsent(dye, ignored -> {
            float[] channels = dye.getTextureDiffuseColors();
            return ((int) (channels[0] * 255)) << 16 | ((int) (channels[1] * 255) << 8) | ((int) (channels[2] * 255));
        });
    }

    /**
     * A copy of vanilla dye mixing code from {@link DyeableLeatherItem#dyeArmor}
     *
     * @param colors list of ARGB colors
     * @return mixed ARGB color
     */
    public static int mixColors(IntList colors) {
        if (colors.isEmpty()) return 0xFFFFFF; // fallback (branco)

        int[] accumulator = {0, 0, 0};
        int count = 0;
        int max = 0;

        int r, g, b;
        for (int color : colors) {
            r = color >> 16 & 0xFF;
            g = color >> 8 & 0xFF;
            b = color & 0xFF;
            max += Math.max(r, Math.max(g, b));
            accumulator[0] += r;
            accumulator[1] += g;
            accumulator[2] += b;

            count++;
        }

        int avgR = accumulator[0] / count;
        int avgG = accumulator[1] / count;
        int avgB = accumulator[2] / count;
        float maxAvg = Math.max(avgR, Math.max(avgG, avgB));

        float avgMax = (float) max / count;
        float corrector = avgMax / maxAvg;

        avgR = (int) (avgR * corrector);
        avgG = (int) (avgG * corrector);
        avgB = (int) (avgB * corrector);

        return (avgR << 16) | (avgG << 8) | avgB;
    }


    public static Color3 lerpTFColor(@NotNull Color3 start, @NotNull Color3 end, @Nullable Player player) {
        if (player == null) return start;

        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (transfurVariantInstance == null) return start;

        return start.lerp(transfurVariantInstance.getTransfurProgression(1), end);
    }

    public static Color3 lerpTFColor(@NotNull Color3 start, @NotNull Color3 end, float delta) {
        return start.lerp(delta, end);
    }

    public static float getPlayerTransfurProgressSafe(@Nullable Player player, float partialTick) {
        if (player == null) return 0;

        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (transfurVariantInstance == null) return 0;

        return transfurVariantInstance.getTransfurProgression(partialTick);
    }


    public static Color3 lerpTFColors(LivingEntity livingEntity, float partialTicks, Color3... colors) {
        if (colors == null || colors.length == 0)
            return new Color3(1.0f, 1.0f, 1.0f); // fallback branco

        if (colors.length == 1)
            return colors[0]; // só uma cor, nada pra interpolar

        if (!(livingEntity instanceof Player player)) return colors[0];

        int amountOfColors = colors.length;
        float progress = getPlayerTransfurProgressSafe(player, partialTicks);

        // Garante que o valor fique entre 0 e 1
        progress = Mth.clamp(progress, 0.0f, 1.0f);

        // Divide o progresso igualmente entre as cores
        float segment = 1.0f / (amountOfColors - 1);

        // Identifica entre quais cores o progresso atual está
        int index = (int) Math.floor(progress / segment);
        if (index < 0) index = 0;
        if (index >= amountOfColors - 1) index = amountOfColors - 2;

        float localProgress = (progress - (index * segment)) / segment;

        // Faz o lerp entre as duas cores do segmento atual
        return lerpTFColor(colors[index], colors[index + 1], localProgress);
    }
}
