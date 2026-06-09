package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.world.features.treedecorators.FlowerTreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChangedAddonTreeDecorators {
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS =
            DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, ChangedAddonMod.MODID);

    public static final RegistryObject<TreeDecoratorType<FlowerTreeDecorator>> FLOWER_DECORATOR =
            TREE_DECORATORS.register("flower_decorator", () -> new TreeDecoratorType<>(FlowerTreeDecorator.CODEC));
}