package net.foxyas.changedaddon.init;

import com.mojang.serialization.Codec;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.tree.condition.AlphaCondition;
import net.ltxprogrammer.changed.ability.tree.AbilityTree;
import net.ltxprogrammer.changed.ability.tree.condition.AbstractCondition;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ChangedAddonAbilityTreeCodecs {

    public static final DeferredRegister<Codec<? extends AbilityTree.NodeEffect>> NODE_EFFECT_REGISTRY = ChangedRegistry.ABILITY_NODE_EFFECTS.createDeferred(ChangedAddonMod.MODID);
    public static final DeferredRegister<Codec<? extends AbstractCondition>> EFFECT_CONDITION_REGISTRY = ChangedRegistry.ABILITY_EFFECT_CONDITIONS.createDeferred(ChangedAddonMod.MODID);

    public static final RegistryObject<Codec<AlphaCondition>> ALPHA_CONDITION = EFFECT_CONDITION_REGISTRY.register("alphaCondition", () -> AlphaCondition.CODEC);


    public static void register(IEventBus eventBus) {
        NODE_EFFECT_REGISTRY.register(eventBus);
        EFFECT_CONDITION_REGISTRY.register(eventBus);
    }
}
