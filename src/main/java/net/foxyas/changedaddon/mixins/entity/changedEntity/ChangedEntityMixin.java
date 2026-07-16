package net.foxyas.changedaddon.mixins.entity.changedEntity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.entity.ai.advanced.AdvancedGroundPathNavigation;
import net.foxyas.changedaddon.entity.api.ChangedEntityExtension;
import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.entity.simple.WolfyEntity;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.item.armor.DarkLatexCoatItem;
import net.foxyas.changedaddon.util.TagKeyUtil;
import net.ltxprogrammer.changed.ability.GrabEntityAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(value = ChangedEntity.class, remap = false)
public abstract class ChangedEntityMixin extends Monster implements ChangedEntityExtension {

    @Unique
    protected boolean pacified = false;

    protected ChangedEntityMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Unique
    private static boolean isDarkLatexCoat(ItemStack itemStack) {
        return itemStack != null
                && !itemStack.isEmpty()
                && itemStack.getItem() instanceof DarkLatexCoatItem;
    }

    @Override
    public boolean canSwimInFluidType(FluidType type) {
        List<FluidType> lavaFluids = TagKeyUtil.getTagContents(level, FluidTags.LAVA).map(Fluid::getFluidType).toList();

        var transfurVariant = getSelfVariant();
        if (transfurVariant != null && (this.hasEffect(MobEffects.FIRE_RESISTANCE) && lavaFluids.contains(type))) {
            boolean aquaticLike = transfurVariant.is(ChangedAddonTags.TransfurVariants.AQUATIC_LIKE);
            boolean fastSwimSpeed = this.getAttributeValue(ForgeMod.SWIM_SPEED.get()) > 1;
            boolean aquaticBreath = transfurVariant.breatheMode.canBreatheWater();
            boolean aquaticAffinity = transfurVariant.breatheMode.hasAquaAffinity();

            return aquaticLike || fastSwimSpeed || aquaticBreath || aquaticAffinity;
        }

        return super.canSwimInFluidType(type);
    }

    @Shadow
    public abstract LivingEntity maybeGetUnderlying();

    @Shadow
    public abstract float computeHealthRatio();

    @Shadow
    protected abstract boolean targetSelectorTest(LivingEntity livingEntity);

    @Shadow
    public abstract LatexType getLatexType();

    @Shadow
    public abstract TransfurContext getReplicateContext();

    @Shadow public abstract TransfurVariant<?> getSelfVariant();

    @Shadow public abstract @Nullable Player getUnderlyingPlayer();

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return !isPacified();
    }

    @Override
    public boolean isPacified() {
        return pacified;
    }

    @Override
    public void setPacified(boolean pacified) {
        this.pacified = pacified;
    }

    @Override
    public boolean isNeutralTo(@NotNull LivingEntity target) {
        if (hasEffect(ChangedAddonMobEffects.PACIFIED.get())) return true;
        if (this.isPacified()) return true;

        Optional<IAbstractChangedEntity> grabberSafe = GrabEntityAbility.getGrabberSafe(target);
        return grabberSafe.isPresent() && grabberSafe.get() instanceof IGrabberEntity changedEntity;
    }

    @Inject(at = @At("HEAD"), method = "variantTick", cancellable = true)
    private void failSafePacified(Level level, CallbackInfo ci) {
        if (level.isClientSide()) return;
        Player player = getUnderlyingPlayer();
        if (this.isPacified() && player != null) {
            TransfurVariantInstance<?> tf = ProcessTransfur.getPlayerTransfurVariant(player);
            if (tf == null) return;
            if (tf.getParent().transfurMode != TransfurMode.NONE) {
                if (tf.transfurMode != TransfurMode.NONE) {
                    tf.transfurMode = TransfurMode.NONE;
                }
            }
            this.setPacified(false);
        }
    }

    @Inject(at = @At("HEAD"), method = "targetSelectorTest", cancellable = true)
    private void onTargetSelectorTest(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity instanceof WolfyEntity) {
            cir.setReturnValue(true);
        } else if (livingEntity instanceof Player player) {
            boolean isWolfyForm = ProcessTransfur.getPlayerTransfurVariantSafe(player)
                    .map(instance -> instance.getChangedEntity() instanceof WolfyEntity)
                    .orElse(false);
            if (isWolfyForm) {
                cir.setReturnValue(true);
            }
        }

        if (isNeutralTo(livingEntity)) cir.setReturnValue(false);

        var entityVariant = ProcessTransfur.getEntityVariant(livingEntity);
        if (getLatexType() == ChangedLatexTypes.WHITE_LATEX.get()
                && entityVariant.isPresent()
                && entityVariant.get() == ChangedAddonTransfurVariants.DARK_LATEX_YUFENG_QUEEN.get()) {
            cir.setReturnValue(false);
        }
    }

    @ModifyReturnValue(method = "getDripRate", at = @At("RETURN"))
    private float modify(float original, @Local(argsOnly = true) float damage) {
        LivingEntity selfOrPlayer = ChangedAddonChangedEntityMixin$getSelf().maybeGetUnderlying();
        if (selfOrPlayer.hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
            return 0f; // Never going to drip.
        }
        if (selfOrPlayer.hasEffect(ChangedAddonMobEffects.LATEX_SOLVENT.get()) || selfOrPlayer.hasEffect(ChangedAddonMobEffects.UNTRANSFUR.get())) {
            return 1f; // Always going to drip.
        }

        return original;
    }

    @Inject(at = @At("TAIL"), method = "registerGoals", remap = true, cancellable = true)
    private void goalsHook(CallbackInfo ci) {
        var self = ChangedAddonChangedEntityMixin$getSelf();
        if (!(self instanceof WolfyEntity)) {
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ChangedEntity.class, true, this::targetSelectorTest));
        }
    }

    @Unique
    private ChangedEntity ChangedAddonChangedEntityMixin$getSelf() {
        var self = (ChangedEntity) (Object) this;
        return self;
    }

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData", remap = true)
    private void saveExtraData(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("isPacified", isPacified());
    }

    @Inject(at = @At("HEAD"), method = "readAdditionalSaveData", remap = true)
    private void readExtraData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("isPacified")) setPacified(tag.getBoolean("isPacified"));
    }

    @Inject(method = "targetSelectorTest", at = @At("HEAD"), cancellable = true)
    private void CancelTarget(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        ItemStack Head = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack Chest = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (ChangedAddonServerConfiguration.DL_COAT_AFFECT_ALL.get()) {
            if (isDarkLatexCoat(Head) && isDarkLatexCoat(Chest)) {
                cir.setReturnValue(false);
            } else if (isDarkLatexCoat(Head) ^ isDarkLatexCoat(Chest)) {
                if (livingEntity.distanceTo((ChangedEntity) (Object) this) >= 4) {
                    cir.setReturnValue(false);
                }
            }
        } else {
            if ((ChangedEntity) (Object) this instanceof AbstractDarkLatexWolf) {
                if (isDarkLatexCoat(Head) && isDarkLatexCoat(Chest)) {
                    cir.setReturnValue(false);
                } else if (isDarkLatexCoat(Head) ^ isDarkLatexCoat(Chest)) {
                    if (livingEntity.distanceTo((ChangedEntity) (Object) this) >= 4) {
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void changedAiPathNavigator(EntityType<?> type, Level level, CallbackInfo ci) {
        if (type.is(ChangedAddonTags.EntityTypes.HAS_BETTER_GROUND_PATHFIND)) {
            this.navigation = new AdvancedGroundPathNavigation(this, level);
        }
    }

}
