package net.foxyas.changedaddon.mixins.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import net.foxyas.changedaddon.entity.ai.behaviors.PatNearbyEntityBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Villager.class)
public abstract class VillagerMixin extends LivingEntity {

    public VillagerMixin(EntityType<? extends AbstractVillager> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @WrapOperation(method = "registerBrainGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/behavior/VillagerGoalPackages;getPlayPackage(F)Lcom/google/common/collect/ImmutableList;"))
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> addCustomGoals(
            float pSpeedModifier,
            Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {

        // 1. Get the original list of behaviors
        ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> originalList = original.call(pSpeedModifier);
        List<Pair<Integer, ? extends BehaviorControl<? super Villager>>> modifiableList = new ArrayList<>(originalList);

        // 2. Create a new RunOne containing the missing villager/cat/bed behaviors + your pat behavior
        BehaviorControl<Villager> updatedRunOne = new RunOne<>(
                ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_ABSENT),
                ImmutableList.of(
                        Pair.of(InteractWith.of(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, pSpeedModifier, 2), 2),
                        Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, pSpeedModifier, 2), 1),
                        Pair.of(VillageBoundRandomStroll.create(pSpeedModifier), 1),
                        Pair.of(SetWalkTargetFromLookTarget.create(pSpeedModifier, 2), 1),
                        Pair.of(new JumpOnBed(pSpeedModifier), 2),
                        Pair.of(new PatNearbyEntityBehavior(pSpeedModifier, 8), 2), // <--- Added here alongside JumpOnBed
                        Pair.of(new DoNothing(20, 40), 2)
                )
        );

        // 3. Replace or append the priority 5 RunOne task in the package list
        var updatedRunOnePair = Pair.of(5, updatedRunOne);
        modifiableList.replaceAll(normalTask -> {
            if (normalTask.getFirst() == 5 && normalTask.getSecond() instanceof RunOne<?> runOne) {
                return updatedRunOnePair;
            } else return normalTask;
        });

        return ImmutableList.copyOf(modifiableList);
    }
}
