package net.foxyas.changedaddon.init;

import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.foxyas.changedaddon.ChangedAddonMod.MODID;

public class ChangedAddonPaintingVariants {

    public static final DeferredRegister<PaintingVariant> PAINTING_TYPES =
            DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, MODID);

    private static final Map<RegistryObject<PaintingVariant>, PaintingVariantInfo> PAINTING_DATA =
            new HashMap<>();

    // ----- Registry -----

    public static final RegistryObject<PaintingVariant> LUMINARCTIC_LEOPARD_FEMALE_SELFIE =
            register("luminarctic_leopard_female_selfie", 48, 48,
                    new PaintingVariantInfo("Selfie Female Luminarctic Leopard", "@Smoopa"));

    public static final RegistryObject<PaintingVariant> LUMINARCTIC_LEOPARD_MALE_SELFIE =
            register("luminarctic_leopard_male_selfie", 48, 48,
                    new PaintingVariantInfo("Selfie Male Luminarctic Leopard", "@Smoopa"));

    public static final RegistryObject<PaintingVariant> LUMINARCTIC_LEOPARD_MALE_MAD_SELFIE =
            register("luminarctic_leopard_male_mad_selfie", 48, 48,
                    new PaintingVariantInfo("Selfie Male Luminarctic Leopard Mad", "@Smoopa"));

    public static final RegistryObject<PaintingVariant> FIGHTING_EXP_9_POV =
            register("fighting_exp9_pov", 48, 48,
                    new PaintingVariantInfo("Fighting Experiment 009", "@Smoopa"));

    // ----- Glow paintings -----

    public static List<PaintingVariant> glowPaintings() {
        return List.of(
                LUMINARCTIC_LEOPARD_MALE_MAD_SELFIE.get(),
                FIGHTING_EXP_9_POV.get()
        );
    }

    // ----- Painting info lookup -----

    public static Optional<PaintingVariantInfo> getPaintingVariantInfo(RegistryObject<PaintingVariant> variant) {
        return Optional.ofNullable(PAINTING_DATA.get(variant));
    }

    // ----- Record info -----

    private static RegistryObject<PaintingVariant> register(String name, int width, int height) {
        return PAINTING_TYPES.register(name, () -> new PaintingVariant(width, height));
    }

    // ----- Registry with extra info -----

    private static RegistryObject<PaintingVariant> register(
            String name,
            int width,
            int height,
            PaintingVariantInfo info
    ) {
        RegistryObject<PaintingVariant> registry = register(name, width, height);
        PAINTING_DATA.put(registry, info);
        return registry;
    }

    public record PaintingVariantInfo(String title, String author) {
    }
}
