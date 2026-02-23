package net.foxyas.changedaddon.datagen.ability_tree;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.data.PackOutput;

public class AbilityTreeProviderImpl extends AbilityTreeProvider {

    public AbilityTreeProviderImpl(PackOutput output) {
        super(output, ChangedAddonMod.MODID);
    }

    @Override
    protected void addTrees() {
//        addTree(Changed.modResource("feline"), List.of(RegistryElementPredicate.forTag(ChangedRegistry.TRANSFUR_VARIANT.get(),
//                Changed.modResource("feline_light"))))
//                .withNode(Changed.modResource("root"), new AbilityTree.Node(
//                        AbilityTree.ROOT_NAME,             // parent
//                        List.of(),                         // occludes
//                        "ability.changed.root",            // titleId
//                        "ability.changed.root.desc",       // descriptionId
//                        0,                                 // price
//                        0,                                 // group discount
//                        List.of(),                         // acquiredEffects
//                        List.of()                          // missingEffects
//                ))
//                .withNode(Changed.modResource("claws"), new AbilityTree.Node(
//                        AbilityTree.ROOT_NAME,
//                        List.of(),
//                        "ability.changed.claws",
//                        "ability.changed.claws.desc",
//                        5,
//                        0,
//                        List.of(new AttributeModifierNodeEffect(
//                                        new StandingOnCondition(List.of(RegistryElementPredicate.forID(ForgeRegistries.BLOCKS, ForgeRegistries.BLOCKS.getKey(Blocks.GRASS_BLOCK)))),
//                                        ForgeMod.SWIM_SPEED.get(), 1
//                                ),
//                                new AttributeModifierNodeEffect(TrueCondition.INSTANCE, Attributes.ATTACK_DAMAGE, 0.5)
//                        )
//                        ,
//                        List.of()
//                ));
    }
}
