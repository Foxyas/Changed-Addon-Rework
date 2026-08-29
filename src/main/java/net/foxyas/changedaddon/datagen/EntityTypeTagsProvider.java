package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.foxyas.changedaddon.init.ChangedAddonEntities.*;
import static net.ltxprogrammer.changed.init.ChangedEntities.*;

public class EntityTypeTagsProvider extends net.minecraft.data.tags.EntityTypeTagsProvider {

    public EntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ChangedAddonMod.MODID, existingFileHelper);
    }

    private static EntityType<?>[] getCanGlideEntitiesArray() {
        List<EntityType<?>> canGlideEntities = new ArrayList<>();
        Collection<TransfurVariant<?>> transfurVariants = ChangedRegistry.TRANSFUR_VARIANT.get().getValues();
        transfurVariants.forEach((transfurVariant) -> {
            if (transfurVariant.canGlide) {
                canGlideEntities.add(transfurVariant.getEntityType());
            }
        });

        return canGlideEntities.toArray(new EntityType[0]);
    }

    private static EntityType<?>[] getBeeEntities() {
        Stream<EntityType<?>> beeEntities = ForgeRegistries.ENTITY_TYPES.getValues().stream().filter((entityType) -> ForgeRegistries.ENTITY_TYPES.getKey(entityType).toString().contains("latex_bee"));
        List<EntityType<?>> canGlideEntities = new ArrayList<>(beeEntities.toList());
        return canGlideEntities.toArray(new EntityType[0]);
    }

    @Override
    public void addTags(HolderLookup.@NotNull Provider pProvider) {
        tag(ChangedTags.EntityTypes.HUMANOIDS).add(
                ERIK.get());

        tag(ChangedAddonTags.EntityTypes.CAN_GRAB);
        tag(ChangedAddonTags.EntityTypes.CAN_GRAB_SUIT);

        tag(ChangedTags.EntityTypes.LATEX).add(LatexEntities.stream().map(Supplier::get)
                .sorted(Comparator.comparing(entityType -> ForgeRegistries.ENTITY_TYPES.getKey(entityType).getPath()))
                .toList().toArray(new EntityType[0]));

        tag(ChangedTags.EntityTypes.ORGANIC_LATEX).add(
                BUNY.get(),
                MIRROR_WHITE_TIGER.get(),
                REYN.get());
        tag(ChangedTags.EntityTypes.PARTIAL_LATEX).add(
                SNOW_LEOPARD_PARTIAL.get(),
                LATEX_SNEP.get());

        tag(ChangedAddonTags.EntityTypes.ALPHA_BY_DEFAULT);

        tag(ChangedAddonTags.EntityTypes.CAN_CARRY).add(
                EntityType.WANDERING_TRADER,
                ChangedEntities.DARK_LATEX_WOLF_PUP.get(),
                EntityType.WOLF);

        tag(ChangedAddonTags.EntityTypes.PATABLE).add(
                EntityType.OCELOT,
                EntityType.PARROT,
                EntityType.CAT,
                EntityType.RABBIT,
                EntityType.WOLF,
                EntityType.FOX,
                LATEX_SNOW_FOX_FOXYAS.get(),
                PROTOTYPE.get(),
                ERIK.get());

        tag(ChangedAddonTags.EntityTypes.DRAGON_ENTITIES).add(
                getCanGlideEntitiesArray()
        );

        tag(ChangedAddonTags.EntityTypes.BEE_ENTITIES).add(
                getBeeEntities()
        );

        tag(ChangedAddonTags.EntityTypes.PACIFY_HANDLE_IMMUNE)
                .add(EXPERIMENT_009.get())
                .add(EXPERIMENT_10.get())
                .add(EXPERIMENT_10_BOSS.get())
                .add(EXPERIMENT_009_BOSS.get());

        tag(ChangedAddonTags.EntityTypes.PACIFY_IMMUNE);

        tag(EntityTypeTags.IMPACT_PROJECTILES).add(
                PARTICLE_PROJECTILE.get(), WITHER_PARTICLE_PROJECTILE.get());

        tag(ChangedAddonTags.EntityTypes.CAN_USE_ACCESSORIES).add(
                ChangedAddonEntities.canUseAccessories().toArray(new EntityType[0])
        );

        tag(ChangedAddonTags.EntityTypes.HAS_CLAWS).add(VOID_FOX.get());
        //noinspection unchecked
        tag(ChangedAddonTags.EntityTypes.CANT_SPAWN_AS_ALPHA_ENTITY).add(DARK_LATEX_WOLF_PUP.get(), PURE_WHITE_LATEX_WOLF_PUP.get(), GAS_WOLF_PUP.get()).addOptionalTags(ChangedTags.EntityTypes.PUDDING, ChangedTags.EntityTypes.PARTIAL_LATEX);
        tag(ChangedAddonTags.EntityTypes.CANT_USE_GRAB);
        tag(ChangedAddonTags.EntityTypes.CAN_ROAR).add(
                LATEX_TIGER_SHARK.get(),
                EXPERIMENT_009.get(),
                EXPERIMENT_10.get(),
                EXPERIMENT_009_BOSS.get(),
                EXPERIMENT_10_BOSS.get()
        );
        tag(ChangedAddonTags.EntityTypes.HAS_BETTER_GROUND_PATHFIND);

        tag(ChangedAddonTags.EntityTypes.PROTOGENS).add(getProtogensEntities().toArray(new EntityType[0]));

        tag(ChangedTags.EntityTypes.CAN_WEAR_EXOSKELETON).add(canUseExoskeleton().toArray(new EntityType[0])).remove(ChangedAddonTags.EntityTypes.PROTOGENS);
        tag(ChangedAddonTags.EntityTypes.CARDBOARD_BOX_HIDER).add(LATEX_SNOW_LEOPARD_MALE.get(), LATEX_SNOW_LEOPARD_FEMALE.get(), LATEX_HYPNO_CAT.get());
    }
}
