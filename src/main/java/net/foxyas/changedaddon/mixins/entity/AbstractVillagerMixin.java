package net.foxyas.changedaddon.mixins.entity;

import net.foxyas.changedaddon.entity.ai.goals.simple.PatNearbyEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class AbstractVillagerMixin extends LivingEntity {

    @Shadow @Final public GoalSelector goalSelector;

    @Shadow public abstract InteractionResult interact(Player pPlayer, InteractionHand pHand);

    public AbstractVillagerMixin(EntityType<? extends AbstractVillager> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void addCustomGoals(CallbackInfo ci) {
        Mob self = (Mob) (Object) this;
        if (self instanceof AbstractVillager abstractVillager) {
            this.goalSelector.addGoal(0, new PatNearbyEntity(abstractVillager, 0.5f));
        }
    }
}
