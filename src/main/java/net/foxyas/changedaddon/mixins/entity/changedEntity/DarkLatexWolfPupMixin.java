package net.foxyas.changedaddon.mixins.entity.changedEntity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexWolf;
import net.ltxprogrammer.changed.entity.beast.DarkLatexWolfPup;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DarkLatexWolfPup.class, remap = false)
public class DarkLatexWolfPupMixin extends AbstractDarkLatexEntity {

    @Shadow
    protected int age;

    public DarkLatexWolfPupMixin(EntityType<? extends AbstractLatexWolf> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @WrapOperation(method = "variantTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V", remap = true))
    private void syncAlphaData(LivingEntity instance, float healAmount, Operation<Void> original) {
        original.call(instance, healAmount);
        if (level instanceof ServerLevel serverLevel) {
            ChangedAddon$tryMakeItAlpha(serverLevel, this);
        }
    }

    @Unique
    private void ChangedAddon$tryMakeItAlpha(ServerLevelAccessor pLevel, ChangedEntity changedEntity) {
        if (changedEntity instanceof IAlphaAbleEntity iAlphaAbleEntity) {
            boolean gamerule = pLevel.getLevel().getGameRules().getBoolean(ChangedAddonGameRules.DO_ALPHAS_SPAWN);
            if (!gamerule) return;

            float chance = iAlphaAbleEntity.chanceToSpawnAsAlpha();
            if (random.nextFloat() <= chance) {
                iAlphaAbleEntity.setAlpha(true);
            }
        }
    }

    @Inject(
            method = "mobInteract",
            at = @At("HEAD"),
            cancellable = true,
            remap = true
    )
    private void onWhiteLatexGooUse(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.isTameItem(stack) && this.isTame()) {
            DarkLatexWolfPup self = (DarkLatexWolfPup) (Object) this;

            if (!self.level.isClientSide) {
                this.age += 5000;

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }

            // Cancela vanilla e retorna sucesso
            cir.setReturnValue(InteractionResult.sidedSuccess(self.level.isClientSide));
        }
    }
}
