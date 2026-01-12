package net.foxyas.changedaddon.mixins.abilities;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.entity.api.ChangedEntityExtension;
import net.foxyas.changedaddon.network.packet.SafeGrabSyncPacket;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
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
public abstract class GrabEntityAbilityInstanceMixin extends AbstractAbilityInstance implements GrabEntityAbilityExtensor {

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

    @Shadow
    private int grabCooldown;

    @Unique
    private boolean safeMode = false;
    @Unique
    private int snuggleCooldown = 0;
    @Unique
    private boolean alreadySnuggledTight = false;


    // Right now it works but has a renderer bug. I don't recommend turning this to true
    @Unique
    private boolean allowGrabTransfured = false;

    public GrabEntityAbilityInstanceMixin(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    @Override
    @Unique
    public void setAllowGrabTransfured(boolean canGrabTransfured) {
        this.allowGrabTransfured = canGrabTransfured;
    }

    @Override
    @Unique
    public boolean allowGrabTransfured() {
        return allowGrabTransfured;
    }

    @Inject(method = "saveData", at = @At("TAIL"))
    private void injectCustomData(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("safeMode", safeMode);
        tag.putBoolean("alreadySnuggledTight", alreadySnuggledTight);
        tag.putBoolean("allowGrabTransfured", allowGrabTransfured);
    }

    @Inject(method = "readData", at = @At("TAIL"))
    private void readCustomData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("safeMode")) safeMode = tag.getBoolean("safeMode");
        if (tag.contains("alreadySnuggledTight")) alreadySnuggledTight = tag.getBoolean("alreadySnuggledTight");
        if (tag.contains("allowGrabTransfured")) allowGrabTransfured = tag.getBoolean("allowGrabTransfured");
    }

    @Unique
    private GrabEntityAbilityInstance getSelf() {
        return (GrabEntityAbilityInstance) (Object) this;
    }

    @Override
    public LivingEntity grabber() {
        return getSelf().entity.getEntity();
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
        if (!entity.getLevel().isClientSide) ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY.with(entity::getEntity), new SafeGrabSyncPacket(entity.getEntity().getId(), safeMode));
    }

    @Inject(method = "tickIdle", at = @At(value = "HEAD"), cancellable = true)
    private void tickSnuggleCooldown(CallbackInfo ci) {
        if (!isSafeMode()) return;
        if (snuggleCooldown > 0) snuggleCooldown--;
    }

    @Inject(method = "tickIdle", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F", remap = true, shift = At.Shift.BY), cancellable = true)
    private void cancelSuit(CallbackInfo ci) {
        if (!isSafeMode()) return;
        ci.cancel();

        if (getSelf().getController().getHoldTicks() >= 2) {
            this.suitTransition -= 0.25f;
        }

        if (this.suitTransition >= 3) {
            this.suitTransition = 3.0F;
            this.suited = false;
            if (getSelf().entity.getChangedEntity() instanceof ChangedEntityExtension changedEntityExtension && changedEntityExtension.shouldAlwaysHoldGrab(grabbedEntity)) {
                this.grabStrength = 1;
            }

            if (grabbedEntity != null) {
                if (!isAlreadySnuggledTight()) {
                    this.runTightHug(this.grabbedEntity);
                }
            }

        } else {
            this.alreadySnuggledTight = false;
        }
    }

    @Inject(method = "suitEntity", at = @At(value = "HEAD"), cancellable = true)
    private void cancelSuitEntity(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (this.safeMode) {
            cir.cancel();
            grabStrength = 1.0F;
        }
    }

    @ModifyExpressionValue(method = "tickIdle", at = @At(value = "INVOKE",
            target = "Lnet/ltxprogrammer/changed/entity/variant/TransfurVariantInstance;isTemporaryFromSuit()Z"))
    private boolean allowGrabTransfuredPlayers(boolean original) {
        if (this.allowGrabTransfured()) {
            return true;
        }
        return original;
    }

    @WrapOperation(
            method = "tickIdle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;ifPlayerTransfurred(Lnet/minecraft/world/entity/player/Player;Ljava/util/function/Consumer;)Z"
            )
    )
    private boolean wrapIfPlayerTransfurred(
            Player player,
            Consumer<TransfurVariantInstance<?>> consumer,
            Operation<Boolean> original
    ) {
        if (this.allowGrabTransfured()) {
            return false; // finge que nunca foi transfured
        }

        return original.call(player, consumer);
    }

    @Inject(
            method = "releaseEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/ltxprogrammer/changed/ability/IAbstractChangedEntity;getEntity()Lnet/minecraft/world/entity/LivingEntity;",
                    ordinal = 0
            )
    )
    private void beforeAttemptToSendPacket(CallbackInfo ci) {
        GrabEntityAbilityInstance self = getSelf();
        IAbstractChangedEntity entity = self.entity;
        if (!(entity.getEntity() instanceof Player) && grabbedEntity instanceof Player) {
            if (!grabbedEntity.level().isClientSide()) {
                Changed.PACKET_HANDLER.send(
                        PacketDistributor.TRACKING_ENTITY.with(entity::getEntity),
                        new GrabEntityPacket(entity.getEntity(), grabbedEntity, GrabEntityPacket.GrabType.RELEASE)
                );
            }
        }
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
    public boolean isAlreadySnuggledTight() {
        return alreadySnuggledTight;
    }

    @Override
    public void setSnuggledTight(boolean value) {
        this.alreadySnuggledTight = value;
    }

    @Inject(method = "handleInstructions", at = @At("HEAD"), cancellable = true)
    private void handleSafeModeInstructions(Level level, CallbackInfo ci) {
        if (level.isClientSide() && this.isSafeMode()) {
            ci.cancel();
            if (this.instructionTicks == 180) {
                getSelf().entity.displayClientMessage(Component.translatable("ability.changed_addon.grab_entity.extender.how_to_release", AbstractAbilityInstance.KeyReference.ABILITY.getName(level)), true);
            } else if (this.instructionTicks == 120) {
                getSelf().entity.displayClientMessage(Component.translatable("ability.changed_addon.grab_entity.extender.how_to_hug", AbstractAbilityInstance.KeyReference.ATTACK.getName(level)), true);
            } else if (this.instructionTicks == 60) {
                getSelf().entity.displayClientMessage(Component.translatable("ability.changed_addon.grab_entity.extender.how_to_hug.tightly", AbstractAbilityInstance.KeyReference.USE.getName(level)), true);
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
                    target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;progressTransfur(Lnet/minecraft/world/entity/LivingEntity;FLnet/ltxprogrammer/changed/entity/variant/TransfurVariant;Lnet/ltxprogrammer/changed/entity/TransfurContext;)Z"
            )
    )
    private boolean changedAddon$disableProgressTransfur(LivingEntity livingEntity, float amount, TransfurVariant<?> variant, TransfurContext context, Operation<Boolean> original) {
        if (safeMode && grabbedEntity != null) {
            // Safe mode -> nunca aplica transfur
            if (!isAlreadySnuggled()) {
                this.runHug(grabbedEntity);
            }
            return false;
        }
        // comportamento normal
        return original.call(livingEntity, amount, variant, context);
    }


    /**
     * Modify the computed keyStrength value during escape handling.
     * You can adjust or completely override it here.
     *
     * @param original The computed keyStrength value
     * @return The modified keyStrength
     */
    @ModifyVariable(
            method = "handleEscape",
            at = @At(
                    value = "STORE",
                    ordinal = 0 // ordinal 0 = first float stored in that method
            ),
            name = "keyStrength"
    )
    private float changedaddon$modifyKeyStrength(float original) {
        if (this.grabbedEntity != null) {
            //Todo New GrabResistance Enchantment or attribute

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
    public int getGrabCooldown() {
        return this.grabCooldown;
    }

    @Override
    public void setGrabCooldown(int cooldown) {
        this.grabCooldown = cooldown;
    }
}
