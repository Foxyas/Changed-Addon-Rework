/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.foxyas.changedaddon.datagen.customData;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Stub class to extend for item model data providers, eliminates some
 * boilerplate constructor parameters.
 */
public abstract class AbilityModelProvider extends ModelProvider<ItemModelBuilder> {

    public AbilityModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, "ability", ItemModelBuilder::new, existingFileHelper);
    }

    public ItemModelBuilder basicAbility(AbstractAbility<?> ability) {
        return basicAbility(Objects.requireNonNull(ChangedRegistry.ABILITY.getKey(ability)));
    }

    public ItemModelBuilder basicAbility$(AbstractAbility<?> ability, String path) {
        return basicAbility$(Objects.requireNonNull(ChangedRegistry.ABILITY.getKey(ability)));
    }

    public ItemModelBuilder basicAbility(ResourceLocation item) {
        return getBuilder(item.withPath("ability/" + item.getPath()).toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "ability/" + item.getPath()));
    }

    public ItemModelBuilder basicAbility$(ResourceLocation item) {
        return getBuilder(item.withPath("ability/" + item.getPath()).toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"));
    }


    @NotNull
    @Override
    public String getName() {
        return "Ability Models: " + modid;
    }
}
