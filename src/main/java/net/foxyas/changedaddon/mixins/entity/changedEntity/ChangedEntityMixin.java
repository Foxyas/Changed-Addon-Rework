package net.foxyas.changedaddon.mixins.entity.changedEntity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.entity.api.ChangedEntityExtension;
import net.foxyas.changedaddon.entity.api.IConditionalFuseEntity;
import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.entity.simple.WolfyEntity;
import net.foxyas.changedaddon.event.TransfurVariantEvents;
import net.foxyas.changedaddon.event.TransfurVariantEvents.OverrideSourceTransfurVariantEvent.TransfurType;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.item.armor.DarkLatexCoatItem;
import net.foxyas.changedaddon.item.armor.HazardBodySuit;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.GrabEntityAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAccessorySlots;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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
        LivingEntity selfOrPlayer = getSelf().maybeGetUnderlying();
        if (selfOrPlayer.hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
            return 0f; // Never going to drip.
        }
        if (selfOrPlayer.hasEffect(ChangedAddonMobEffects.LATEX_SOLVENT.get()) || selfOrPlayer.hasEffect(ChangedAddonMobEffects.UNTRANSFUR.get())) {
            return 1f; // Always going to drip.
        }

        return original;
    }

    @Inject(at = @At("HEAD"), method = "tryAbsorbTarget", cancellable = true)
    private void tryAbsorbTargetInjector(LivingEntity target, IAbstractChangedEntity source, float amount, @Nullable List<TransfurVariant<?>> possibleMobFusions, CallbackInfoReturnable<Boolean> cir) {
        Optional<AccessorySlots> forEntity = AccessorySlots.getForEntity(maybeGetUnderlying());
        if (forEntity.isPresent()) {
            AccessorySlots accessorySlots = forEntity.get();
            Optional<ItemStack> item = accessorySlots.getItem(ChangedAccessorySlots.FULL_BODY.get());
            if (item.isPresent()) {
                ItemStack stack = item.get();
                if (stack.getItem() instanceof HazardBodySuit) {
                    ChangedAddonMod.LOGGER.info("Event Canceled Happened, value has been set to:{}", false);
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "registerGoals", remap = true, cancellable = true)
    private void goalsHook(CallbackInfo ci) {
        var self = getSelf();
        if (!(self instanceof WolfyEntity)) {
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ChangedEntity.class, true, this::targetSelectorTest));
        }
    }

    private ChangedEntity getSelf() {
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

    @Inject(at = @At("HEAD"), method = "tryFuseWithTarget", cancellable = true)
    private void conditionalFuseEntities(LivingEntity targetToFuse, IAbstractChangedEntity source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!(targetToFuse instanceof IConditionalFuseEntity conditionalFuseEntity)) {
            return;
        }

        if (!conditionalFuseEntity.canBeFusedBy(targetToFuse, source, amount)) {
            cir.cancel();
        }
    }

    @ModifyVariable(
            method = "tryAbsorbTarget",
            at = @At(
                    value = "STORE",
                    ordinal = -1
            ),
            name = "sourceTfVariant"
    )
    private TransfurVariant<?> changedaddon$overrideSourceTfAbsorptionVariant(
            TransfurVariant<?> original,
            LivingEntity target,
            IAbstractChangedEntity source,
            float amount,
            @Nullable List<TransfurVariant<?>> possibleMobFusions
    ) {
        TransfurVariantEvents.OverrideSourceTransfurVariantEvent event = new TransfurVariantEvents.OverrideSourceTransfurVariantEvent(TransfurType.ABSORPTION, original, getSelf(), target, source, amount, possibleMobFusions);
        if (ChangedAddonMod.postEvent(event)) {
            return original;
        } else {
            return event.getVariant();
        }
    }

    @ModifyVariable(
            method = "tryTransfurTarget",
            at = @At(
                    value = "STORE",
                    ordinal = -1
            ),
            name = "variant"
    )
    private TransfurVariant<?> changedaddon$overrideSourceTfReplicationVariant(
            TransfurVariant<?> original, Entity entity
    ) {
        if (!(entity instanceof LivingEntity target)) {
            return original;
        }

        IAbstractChangedEntity abstractChangedEntity = IAbstractChangedEntity.forEither(this.maybeGetUnderlying());
        float amount = (float) this.maybeGetUnderlying().getAttributeValue(ChangedAttributes.TRANSFUR_DAMAGE.get());
        amount = ProcessTransfur.difficultyAdjustTransfurAmount(entity.level().getDifficulty(), amount, abstractChangedEntity);
        TransfurContext context = this.getReplicateContext();
        IAbstractChangedEntity source = context.source;

        TransfurVariantEvents.OverrideSourceTransfurVariantEvent event = new TransfurVariantEvents.OverrideSourceTransfurVariantEvent(TransfurType.REPLICATION, original, getSelf(), target, source, amount, null);
        if (ChangedAddonMod.postEvent(event)) {
            return original;
        } else {
            return event.getVariant();
        }
    }

}
