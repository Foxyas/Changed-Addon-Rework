package net.foxyas.changedaddon.mixins.abilities;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.ability.api.IWheelKeyPressHandler;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.entity.api.ChangedEntityExtension;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.network.packet.AbilityWheelKeyPressPacket;
import net.foxyas.changedaddon.network.packet.SafeGrabSyncPacket;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.ai.LatexAssimilationDecision;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Mixin(value = GrabEntityAbilityInstance.class, remap = false)
public abstract class GrabEntityAbilityInstanceMixin extends AbstractAbilityInstance implements GrabEntityAbilityExtensor, IWheelKeyPressHandler {

    @Shadow
    public boolean suited;
    @Shadow
    @Nullable
    public LivingEntity grabbedEntity;
    @Shadow
    public float suitTransition;
    @Shadow
    public float grabStrength;
    @Shadow
    int instructionTicks;

    @Shadow public boolean useDown;

    @Shadow public KeyReference currentEscapeKey;

    @Unique
    private boolean safeMode = false;
    @Unique
    private int snuggleCooldown = 0;
    @Unique
    private boolean isSnugglingTight = false;

    @Unique
    private boolean allowGrabTransfurred = false; // Default is false. it can be true using external code

    public GrabEntityAbilityInstanceMixin(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    @Override
    @Unique
    public void setAllowGrabTransfurred(boolean canGrabTransfurred) {
        this.allowGrabTransfurred = canGrabTransfurred;
    }

    @Override
    @Unique
    public boolean allowGrabTransfurred() {
        return allowGrabTransfurred;
    }

    @Inject(method = "saveData", at = @At("TAIL"))
    private void injectCustomData(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("safeMode", safeMode);
        tag.putBoolean("alreadySnuggledTight", isSnugglingTight);
        tag.putBoolean("allowGrabTransfurred", allowGrabTransfurred);
    }

    @Inject(method = "readData", at = @At("TAIL"))
    private void readCustomData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("safeMode")) safeMode = tag.getBoolean("safeMode");
        if (tag.contains("alreadySnuggledTight")) isSnugglingTight = tag.getBoolean("alreadySnuggledTight");
        if (tag.contains("allowGrabTransfurred")) allowGrabTransfurred = tag.getBoolean("allowGrabTransfurred");
    }

    @Unique
    private GrabEntityAbilityInstance ChangedAddon$getSelf() {
        return (GrabEntityAbilityInstance) (Object) this;
    }

    @Override
    public LivingEntity grabber() {
        return ChangedAddon$getSelf().entity.getEntity();
    }

    @Override
    public boolean isSafeMode() {
        return safeMode;
    }

    @Override
    public void setSafeMode(boolean safeMode) {
        this.safeMode = safeMode;
    }

    @Override
    public void setSafeModeAuthoritative(boolean safeMode) {
        if (this.safeMode == safeMode)
            return;

        this.safeMode = safeMode;
        if (!entity.getLevel().isClientSide)
            ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY.with(entity::getEntity), new SafeGrabSyncPacket(entity.getEntity().getId(), safeMode));
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/ability/GrabEntityAbilityInstance;releaseEntity(Z)V", ordinal = 1))
    private void stopDebuffsIfFriendlyMode(GrabEntityAbilityInstance instance, boolean debuffs, Operation<Void> original) {
        if (this.isSafeMode()) {
            original.call(instance, false);
            return;
        }
        original.call(instance, debuffs);
    }

    @Inject(method = "tickIdle", at = @At(value = "HEAD"), cancellable = true)
    private void tickIdleHook(CallbackInfo ci) {
        if (!isSafeMode()) return;
        if (this.isSnugglingTight()) {
            // TODO: maybe make hugs heal targets?
        }
    }

    @Inject(method = "tickIdle", at = @At(value = "HEAD"), cancellable = true)
    private void tickSendKeyBindInfo(CallbackInfo ci) {
        Level level = entity.getLevel();
        if (level.isClientSide()) {
            if (ChangedAddonClientConfiguration.GRAB_ABILITY_KEY_INFO.get()) {
                this.entity.displayClientMessage(this.currentEscapeKey.getName(level), true);
            }
        }
    }

    @Inject(method = "tickIdle", at = @At(value = "HEAD"), cancellable = true)
    private void tickSnuggleCooldown(CallbackInfo ci) {
        if (!isSafeMode()) return;
        if (snuggleCooldown > 0) snuggleCooldown--;
    }

    @Inject(method = "tickIdle", at = @At(value = "TAIL"), cancellable = true)
    private void setGrabStrengthAtMaxIfGrabberCanAlwaysHold(CallbackInfo ci) {
        if (ChangedAddon$getSelf().entity.getChangedEntity() instanceof ChangedEntityExtension changedEntityExtension
                && changedEntityExtension.shouldAlwaysHoldInGrab(grabbedEntity, ChangedAddon$getSelf())) {
            this.grabStrength = 1;
        }
    }

//    Todo: Uncomment this stuff when 0.16.0 release;
//    @ModifyReturnValue(method = "canSuit", at = @At("RETURN"))
//    private boolean cancelSuit(boolean original) {
//        if (this.isSafeMode()) {
//            return false;
//        } else {
//            return original;
//        }
//    }
//
//    @Inject(method = "tickIdle",
//            at = @At(
//                    value = "FIELD",
//                    target = "Lnet/ltxprogrammer/changed/ability/GrabEntityAbilityInstance;suitTransition:F",
//                    ordinal = 2, opcode = Opcodes.GETFIELD,
//                    shift = At.Shift.BY
//            ))
//    private void manuallyProgressSuit(CallbackInfo ci) {
//        if (!isSafeMode()) return;
//        if (useDown) {
//            this.suitTransition += 0.075F;
//        }
//    }

    @Deprecated(since = "since Changed 0.16.0, we gonna need to make other mixin to set \"canSuit\" to false ")
    // Todo: move this logic to canSuit.
    @Inject(method = "tickIdle", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F", remap = true, shift = At.Shift.BY), cancellable = true)
    private void cancelSuit(CallbackInfo ci) {
        if (!isSafeMode()) return;
        ci.cancel();

        if (ChangedAddon$getSelf().getController().getHoldTicks() >= 2) {
            this.suitTransition -= 0.25f;
        }

        if (this.suitTransition >= 3) {
            this.suitTransition = 3.0F;
            this.suited = false;
            if (ChangedAddon$getSelf().entity.getChangedEntity() instanceof ChangedEntityExtension changedEntityExtension && changedEntityExtension.shouldAlwaysHoldInGrab(grabbedEntity, ChangedAddon$getSelf())) {
                this.grabStrength = 1; //Todo: maybe remove this later?
            }

            if (grabbedEntity != null) {
                if (!isSnugglingTight()) {
                    this.runTightHug(this.grabbedEntity);
                }
            }

        } else {
            this.isSnugglingTight = false;
        }
    }

    @WrapOperation(method = "suitEntity", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;setPlayerTransfurVariant(Lnet/minecraft/world/entity/player/Player;Lnet/ltxprogrammer/changed/entity/variant/TransfurVariant;Lnet/ltxprogrammer/changed/entity/TransfurContext;FZLjava/util/function/Consumer;)Lnet/ltxprogrammer/changed/entity/variant/TransfurVariantInstance;"))
    private TransfurVariantInstance<?> syncAlphaGene(Player player, TransfurVariant<?> ogVariant, TransfurContext context, float progress, boolean temporaryFromSuit, Consumer<TransfurVariantInstance<?>> consumer, Operation<TransfurVariantInstance<?>> original) {
        if (this.entity.getChangedEntity() instanceof IAlphaAbleEntity alphaSource) {
            return ProcessTransfur.setPlayerTransfurVariant(player, ogVariant, context, progress, temporaryFromSuit, (transfurVariantInstance) -> {
                if (transfurVariantInstance.getChangedEntity() instanceof IAlphaAbleEntity alphaTarget) {
                    alphaTarget.setAlpha(alphaSource.isAlpha());
                    alphaTarget.setAlphaScale(alphaSource.alphaAdditionalScale());
                }
            });
        }
        return original.call(player, ogVariant, context, progress, temporaryFromSuit, consumer);
    }


    @Inject(method = "suitEntity", at = @At(value = "HEAD"), cancellable = true)
    private void cancelSuitEntity(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (this.safeMode) {
            cir.cancel();
            grabStrength = 1.0F;
        }
    }

    @ModifyExpressionValue(method = "isGrabbedInvalid", at = @At(value = "INVOKE",
            target = "Lnet/ltxprogrammer/changed/entity/variant/TransfurVariantInstance;isTemporaryFromSuit()Z"))
    private boolean allowGrabTransfurredPlayers(boolean original, @Local(name = "player") Player player) {
        if (canGrabEntity(player)) {
            return true;
        }
        return original;
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;isPlayerTransfurred(Lnet/minecraft/world/entity/player/Player;)Z"),
            method = "getHoveredEntity")
    private boolean allowTfedGrab(boolean original, @Local(name = "targetPlayer") Player player) {

        return !this.canGrabEntity(player) && original;
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;is(Lnet/minecraft/tags/TagKey;)Z", remap = true),
            method = "getHoveredEntity")
    private boolean ignoreTagCheck(boolean original, @Local(name = "livingEntity") LivingEntity livingEntity) {

        return this.canGrabEntity(livingEntity) || original;
    }

    @Override
    public boolean isAlreadySnuggled() {
        return snuggleCooldown > 0;
    }

    @Override
    public void setSnuggled(boolean value) {
        this.snuggleCooldown = value ? SNUGGLED_COOLDOWN : 0;
    }

    @Override
    public boolean isSnugglingTight() {
        return isSnugglingTight;
    }

    @Override
    public void setSnugglingTight(boolean value) {
        this.isSnugglingTight = value;
    }

    @Inject(method = "handleInstructions", at = @At("HEAD"), cancellable = true)
    private void handleSafeModeInstructions(Level level, CallbackInfo ci) {
        if (level.isClientSide() && this.isSafeMode()) {
            ci.cancel();
            if (this.instructionTicks == 180) {
                ChangedAddon$getSelf().entity.displayClientMessage(Component.translatable("ability.changed_addon.grab_entity.extender.how_to_release", AbstractAbilityInstance.KeyReference.ABILITY.getName(level)), true);
            } else if (this.instructionTicks == 120) {
                ChangedAddon$getSelf().entity.displayClientMessage(Component.translatable("ability.changed_addon.grab_entity.extender.how_to_hug", AbstractAbilityInstance.KeyReference.ATTACK.getName(level)), true);
            } else if (this.instructionTicks == 60) {
                ChangedAddon$getSelf().entity.displayClientMessage(Component.translatable("ability.changed_addon.grab_entity.extender.how_to_hug.tightly", AbstractAbilityInstance.KeyReference.USE.getName(level)), true);
            }

            if (this.instructionTicks > 0) {
                --this.instructionTicks;
            }

            if (this.instructionTicks < 0) {
                ++this.instructionTicks;
            }
        }
    }

    @WrapOperation(
            method = "tickIdle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;progressTransfur(Lnet/minecraft/world/entity/LivingEntity;Lnet/ltxprogrammer/changed/entity/ai/LatexAssimilationDecision;)Z"
            )
    )
    private boolean changedAddon$disableProgressTransfur(LivingEntity livingEntity, LatexAssimilationDecision<?> decision, Operation<Boolean> original) {
        if (safeMode && grabbedEntity != null) {
            // Safe mode -> nunca aplica transfur
            if (!isAlreadySnuggled()) {
                this.runHug(grabbedEntity);
            }
            return false;
        }
        // comportamento normal
        return original.call(livingEntity, decision);
    }


    /**
     * Modify the computed keyStrength value during escape handling.
     * You can adjust or completely override it here.
     *
     * @param original The computed keyStrength value
     * @return The modified keyStrength
     */
    @ModifyVariable(
            method = "lambda$handleEscape$10",
            at = @At(
                    value = "STORE",
                    ordinal = 0 // ordinal 0 = first float stored in that method
            ),
            name = "keyStrength"
    )
    private float changedaddon$modifyKeyStrength(float original) {
        if (this.grabbedEntity != null) {
            float analogicPercent = 0;

            List<EquipmentSlot> armorSlots = Arrays.stream(EquipmentSlot.values()).filter((equipmentSlot -> equipmentSlot.getType() == EquipmentSlot.Type.ARMOR)).toList();

            for (EquipmentSlot slot : armorSlots) {
                ItemStack itemBySlot = this.grabbedEntity.getItemBySlot(slot);
                int enchantmentLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.THORNS, itemBySlot);
                if (enchantmentLevel > 0) {
                    analogicPercent += (float) enchantmentLevel / Enchantments.THORNS.getMaxLevel();
                }
            }

            if (analogicPercent > 0) {
                return original * (1 + analogicPercent);
            } else {
                return original;
            }
        }


        return original;
    }

    @Override
    public boolean isWheelKeyPressedValid(Player player, boolean isMouse, int keyPressed, int action, int modifiers) {
        if (isMouse) {
            boolean isKeyValid = keyPressed == GLFW.GLFW_MOUSE_BUTTON_RIGHT || keyPressed == GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
            return isKeyValid && action == GLFW.GLFW_PRESS;
        }
        return false;
    }

    @Override
    public void onServerProcessWheelKeyPressed(Player player, boolean isMouse, int keyPressed, int action, int modifiers) {
        if (isWheelKeyPressedValid(player, isMouse, keyPressed, action, modifiers)) {
            this.setSafeMode(!this.isSafeMode());
            if (!player.level().isClientSide()) {
                player.displayClientMessage(Component.translatable("key.changed_addon.turn_off_transfur.grab_safe_mode", safeMode), true);
            }
        }
    }

    @Override
    public boolean onClientWheelKeyPressed(Player player, boolean isMouse, int keyPressed, int action, int modifiers) {
        if (isWheelKeyPressedValid(player, isMouse, keyPressed, action, modifiers)) {
            this.setSafeMode(!this.isSafeMode());
            ChangedAddonMod.PACKET_HANDLER.sendToServer(new AbilityWheelKeyPressPacket(keyPressed, action, modifiers, isMouse, ability));
            return true;
        }
        return false;
    }
}
