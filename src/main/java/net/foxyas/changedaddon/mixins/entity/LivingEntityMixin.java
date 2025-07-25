package net.foxyas.changedaddon.mixins.entity;

import com.google.common.collect.ImmutableMap;
import net.foxyas.changedaddon.abilities.ToggleClimbAbility;
import net.foxyas.changedaddon.abilities.ToggleClimbAbilityInstance;
import net.foxyas.changedaddon.entity.interfaces.ExtraConditions;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.ability.*;
import net.ltxprogrammer.changed.block.StasisChamber;
import net.ltxprogrammer.changed.block.ThreeXThreeSection;
import net.ltxprogrammer.changed.block.WearableBlock;
import net.ltxprogrammer.changed.block.WhiteLatexTransportInterface;
import net.ltxprogrammer.changed.block.entity.StasisChamberBlockEntity;
import net.ltxprogrammer.changed.data.AccessorySlotContext;
import net.ltxprogrammer.changed.data.AccessorySlotType;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.robot.Exoskeleton;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.fluid.AbstractLatexFluid;
import net.ltxprogrammer.changed.fluid.Gas;
import net.ltxprogrammer.changed.fluid.TransfurGas;
import net.ltxprogrammer.changed.init.*;
import net.ltxprogrammer.changed.item.AccessoryItem;
import net.ltxprogrammer.changed.item.ExoskeletonItem;
import net.ltxprogrammer.changed.item.ExtendedItemProperties;
import net.ltxprogrammer.changed.item.SpecializedAnimations;
import net.ltxprogrammer.changed.network.packet.AccessorySyncPacket;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "onClimbable", at = @At("HEAD"), cancellable = true)
    public void onClimbable(CallbackInfoReturnable<Boolean> callback) {
        LivingEntity self = (LivingEntity) (Object) this;
        ProcessTransfur.ifPlayerTransfurred(EntityUtil.playerOrNull(self), (variant) -> {
            AbstractAbilityInstance instance = variant.getAbilityInstance(ChangedAddonAbilities.TOGGLE_CLIMB.get());
            if (variant.getParent().canClimb && self.horizontalCollision) {
                if (instance instanceof ToggleClimbAbilityInstance abilityInstance) {
                    if (variant.getChangedEntity() instanceof ExtraConditions.Climb climb) {
                        if (climb.canClimb()) {
                            callback.setReturnValue(abilityInstance.isActivated());
                        }
                    }
                }
            }
        });
    }
}