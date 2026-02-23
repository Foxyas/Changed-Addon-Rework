package net.foxyas.changedaddon.variant;

import com.google.common.base.Suppliers;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.network.chat.Component;
import org.antlr.v4.runtime.misc.MultiMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants.*;

public class TransfurVariantsInfo {

    public static final Supplier<VariantWithOwnerMap> OCS = Suppliers.memoize(() -> {
        VariantWithOwnerMap variants = new VariantWithOwnerMap();
        TransfurVariantsInfo.addUnknownOwnerName(variants, new TransfurVariant<?>[]{BOREALIS_MALE.get(), BOREALIS_FEMALE.get()});
        TransfurVariantsInfo.addUnknownOwnerName(variants, new TransfurVariant<?>[]{HIMALAYAN_CRYSTAL_GAS_CAT_MALE.get(), HIMALAYAN_CRYSTAL_GAS_CAT_FEMALE.get()});

        TransfurVariantsInfo.addWithOwnerName(variants, new TransfurVariant<?>[]{LATEX_WIND_CAT_MALE.get(), LATEX_WIND_CAT_FEMALE.get()}, Component.literal("Species by @BrownBakers"));

        TransfurVariantsInfo.addWithOwnerName(variants, MONGOOSE.get(), "@nopemom (PHOBOS)");
        TransfurVariantsInfo.addWithOwnerName(variants, BLUE_LIZARD.get(), "@V");

        TransfurVariantsInfo.addUnknownOwnerName(variants, FENGQI_WOLF.get());

        TransfurVariantsInfo.addWithOwnerName(variants, FOXTA_FOXY.get(), Component.literal("Free for use but made By @Foxyas"));
        TransfurVariantsInfo.addWithOwnerName(variants, SNEPSI_LEOPARD.get(), Component.literal("Free for use but made By @Foxyas"));

        TransfurVariantsInfo.addWithOwnerName(variants, HAYDEN_FENNEC_FOX.get(), Component.literal("@haydenfencfoxo / @hayden_fencfoxo"));
        TransfurVariantsInfo.addWithOwnerName(variants, REYN.get(), Component.literal("@reyn"));
        TransfurVariantsInfo.addWithOwnerName(variants, LYNX.get(), Component.literal("@Smoopa"));
        TransfurVariantsInfo.addWithOwnerName(variants, LATEX_KAYLA_SHARK.get(), Component.literal("@kaylathelatexsharky"));
        TransfurVariantsInfo.addWithOwnerName(variants, AVALI_ZERGODMASTER.get(), Component.literal("@zerggodmaster"));

        TransfurVariantsInfo.addWithOwnerName(variants, EXPERIMENT_009.get(), Component.literal("Free for use but made By @Foxyas"));
        TransfurVariantsInfo.addWithOwnerNameFrom(variants, EXPERIMENT_009.get(), EXPERIMENT_009_BOSS.get());
        TransfurVariantsInfo.addWithOwnerName(variants, EXPERIMENT_10.get(), Component.literal("@SuperNovaDragon"));
        TransfurVariantsInfo.addWithOwnerNameFrom(variants, EXPERIMENT_10.get(), EXPERIMENT_10_BOSS.get());
        return variants;
    });

    public static void addNoOwnerName(VariantWithOwnerMap map, TransfurVariant<?> variant) {
        map.put(variant, List.of(Component.literal("Free For Use, No Owner")));
    }

    public static void addUnknownOwnerName(VariantWithOwnerMap map, TransfurVariant<?> variant) {
        map.put(variant, List.of(Component.literal("Not free for use, Unknown owner")));
    }

    public static void addUnknownOwnerName(VariantWithOwnerMap map, TransfurVariant<?>[] transfurVariants) {
        for (TransfurVariant<?> variant : transfurVariants) {
            map.put(variant, List.of(Component.literal("Not free for use, Unknown owner")));
        }
    }

    public static void addUnknownOwnerName(VariantWithOwnerMap map, ArrayList<TransfurVariant<?>> transfurVariants) {
        for (TransfurVariant<?> variant : transfurVariants) {
            map.put(variant, List.of(Component.literal("Not free for use, Unknown owner")));
        }
    }

    public static void addWithOwnerName(VariantWithOwnerMap map, TransfurVariant<?> variant, Component... components) {
        map.put(variant, Arrays.stream(components).toList());
    }

    public static void addWithOwnerName(VariantWithOwnerMap map, ArrayList<TransfurVariant<?>> variants, Component... components) {
        for (TransfurVariant<?> variant : variants) {
            map.put(variant, Arrays.stream(components).toList());
        }
    }

    public static void addWithOwnerName(VariantWithOwnerMap map, TransfurVariant<?>[] variants, Component... components) {
        for (TransfurVariant<?> variant : variants) {
            map.put(variant, Arrays.stream(components).toList());
        }
    }

    public static void addWithOwnerName(VariantWithOwnerMap map, TransfurVariant<?> variant, String component) {
        addWithOwnerName(map, variant, Component.literal(component));
    }

    public static void addWithOwnerName(VariantWithOwnerMap map, ArrayList<TransfurVariant<?>> variants, String component) {
        for (TransfurVariant<?> variant : variants) {
            addWithOwnerName(map, variant, Component.literal(component));
        }
    }

    public static void addWithOwnerName(VariantWithOwnerMap map, TransfurVariant<?>[] variants, String component) {
        for (TransfurVariant<?> variant : variants) {
            addWithOwnerName(map, variant, Component.literal(component));
        }
    }

    public static void addWithOwnerNameFrom(VariantWithOwnerMap map, @NotNull TransfurVariant<?> from, @NotNull TransfurVariant<?> to) {
        List<Component> componentList = map.get(from);
        if (componentList != null) {
            map.put(to, componentList);
        }
    }

    // Just For organization.
    public static class VariantWithOwnerMap extends MultiMap<TransfurVariant<?>, Component> {
    }
}
