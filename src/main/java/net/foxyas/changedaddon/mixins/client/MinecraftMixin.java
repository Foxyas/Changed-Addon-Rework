package net.foxyas.changedaddon.mixins.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.block.DarkLatexPuddleBlock;
import net.foxyas.changedaddon.block.entity.DarkLatexPuddleBlockEntity;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    @Nullable
    public Entity cameraEntity;

    @Shadow
    @Nullable
    public LocalPlayer player;

    @WrapOperation(at = @At(value = "NEW", target = "net/minecraft/client/gui/screens/InBedChatScreen"), method = "tick")
    private InBedChatScreen stopSettingSleepScreenWhenCuddling(Operation<InBedChatScreen> original) {
        return ChangedAddonVariables.ofOrDefault(player).isCuddling ? null : original.call();
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z"),
            method = "startUseItem")
    private boolean makeHandsNotBusyForCuddle(boolean original) {
        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (instance == null) return original;

        GrabEntityAbilityInstance grab = instance.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
        if (grab == null || grab.grabbedEntity == null || !((GrabEntityAbilityExtensor)grab).isSafeMode()) return original;

        return false;
    }

    @Inject(method = "shouldEntityAppearGlowing", at = @At("HEAD"), cancellable = true)
    public void isEntityMovingOnWhiteLatex(Entity entity, CallbackInfoReturnable<Boolean> callback) {
        if (!(entity instanceof LivingEntity livingEntity))
            return;
        if (this.cameraEntity == null)
            return;
        if (LatexType.getEntityLatexType(this.cameraEntity) != ChangedLatexTypes.DARK_LATEX.get())
            return;
        if (LatexType.getEntityLatexType(livingEntity) != null && LatexType.getEntityLatexType(livingEntity) == ChangedLatexTypes.DARK_LATEX.get())
            return;
        BlockState feetBlockState = livingEntity.getFeetBlockState();
        if (feetBlockState.isAir())
            return;
        if (feetBlockState.getBlock() instanceof DarkLatexPuddleBlock) {
            BlockEntity blockEntity = livingEntity.level().getBlockEntity(livingEntity.blockPosition());
            if (blockEntity instanceof DarkLatexPuddleBlockEntity darkLatexPuddleBlockEntity) {
                callback.setReturnValue(darkLatexPuddleBlockEntity.cooldown <= 0 && livingEntity.distanceTo(this.cameraEntity) <= 15);
            }
        }
    }
}