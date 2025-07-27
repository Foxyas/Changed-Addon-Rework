package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ChangedAddonTags;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import static net.foxyas.changedaddon.init.ChangedAddonEntities.*;

public class EntityTypeTagsProvider extends net.minecraft.data.tags.EntityTypeTagsProvider {

    public EntityTypeTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ChangedTags.EntityTypes.HUMANOIDS).add(ERIK.get());
        tag(ChangedTags.EntityTypes.LATEX).add(DAZED.get(), PURO_KIND.get(), PURO_KIND_FEMALE.get(), KET_EXPERIMENT_009.get(), EXPERIMENT_10.get(), KET_EXPERIMENT_009_BOSS.get(), EXPERIMENT_10_BOSS.get(), EXP_1_MALE.get(), EXP_1_FEMALE.get(), EXP_2_MALE.get(), EXP_2_FEMALE.get(), WOLFY.get(), EXP_6.get(), LATEX_SNOW_FOX.get(), LATEX_SNOW_FOX_FEMALE.get(), SNOW_LEOPARD_FEMALE_ORGANIC.get(), SNOW_LEOPARD_MALE_ORGANIC.get(), SNOW_LEOPARD_PARTIAL.get(), LATEX_SNEP.get(), LUMINARCTIC_LEOPARD.get(), FEMALE_LUMINARCTIC_LEOPARD.get(), LATEX_SQUID_TIGER_SHARK.get(), LATEX_DRAGON_SNOW_LEOPARD_SHARK.get(), CRYSTAL_GAS_CAT_MALE.get(), CRYSTAL_GAS_CAT_FEMALE.get(), VOID_FOX.get(), LATEX_KITSUNE_MALE.get(), LATEX_KITSUNE_FEMALE.get(), LATEX_CALICO_CAT.get());
        tag(ChangedTags.EntityTypes.ORGANIC_LATEX).add(BUNY.get(), MIRROR_WHITE_TIGER.get(), REYN.get());
        tag(ChangedTags.EntityTypes.PARTIAL_LATEX).add(SNOW_LEOPARD_PARTIAL.get(), LATEX_SNEP.get());

        tag(ChangedAddonTags.EntityTypes.CAN_CARRY).add(EntityType.WANDERING_TRADER, ChangedEntities.DARK_LATEX_WOLF_PUP.get(), EntityType.WOLF);
        tag(ChangedAddonTags.EntityTypes.PATABLE).add(EntityType.OCELOT, EntityType.PARROT, EntityType.CAT, EntityType.RABBIT, EntityType.WOLF, EntityType.FOX, FOXYAS.get(), PROTOTYPE.get(), ERIK.get());

        tag(EntityTypeTags.IMPACT_PROJECTILES).add(PARTICLE_PROJECTILE.get());
    }
}
