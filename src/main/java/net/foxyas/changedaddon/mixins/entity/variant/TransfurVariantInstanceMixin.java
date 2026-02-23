package net.foxyas.changedaddon.mixins.entity.variant;

import com.google.common.collect.ImmutableMap;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.customHandle.AttributesHandle;
import net.foxyas.changedaddon.event.UntransfurEvent;
import net.foxyas.changedaddon.item.armor.DarkLatexCoatItem;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.foxyas.changedaddon.variant.VariantExtraStats;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.util.KeyStateTracker;
import net.ltxprogrammer.changed.util.TagUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TransfurVariantInstance.class, remap = false)
public abstract class TransfurVariantInstanceMixin implements TransfurVariantInstanceExtensor {

    @Shadow
    public int ticksFlying;
    @Shadow
    @Final
    public ImmutableMap<AbstractAbility<?>, AbstractAbilityInstance> abilityInstances;
    @Unique
    public int ticksSinceSecondAbilityActivity;
    public KeyStateTracker secondAbilityKey = new KeyStateTracker();
    @Unique
    public boolean untransfurImmunity = false;
    @Unique
    public boolean untransfurImmunityCommand = false;
    @Unique
    public AbstractAbility<?> secondSelectedAbility;
    @Shadow
    @Final
    protected TransfurVariant<ChangedEntity> parent;
    @Shadow
    @Final
    private Player host;
    @Unique
    private boolean appliedFlySpeed;

    @Shadow
    public abstract TransfurVariant<?> getParent();

    @Shadow
    public abstract boolean shouldApplyAbilities();

    @Shadow
    public abstract ChangedEntity getChangedEntity();

    @Shadow
    public abstract boolean isTemporaryFromSuit();

    @Override
    public boolean getUntransfurImmunity(UntransfurEvent.UntransfurType type) {
        return type == UntransfurEvent.UntransfurType.SURVIVAL ? untransfurImmunity : untransfurImmunityCommand;
    }

    @Override
    public void setUntransfurImmunity(UntransfurEvent.UntransfurType type, boolean value) {
        if (type == UntransfurEvent.UntransfurType.SURVIVAL) untransfurImmunity = value;
        if (type == UntransfurEvent.UntransfurType.COMMAND) untransfurImmunityCommand = value;
    }

    @Override
    public KeyStateTracker getSecondAbilityKey() {
        return secondAbilityKey;
    }

    @Override
    public void setSecondAbilityKey(KeyStateTracker secondAbilityKey) {
        this.secondAbilityKey = secondAbilityKey;
    }

    @Override
    public AbstractAbility<?> getSecondSelectedAbility() {
        return secondSelectedAbility;
    }

    @Override
    public void setSecondSelectedAbility(AbstractAbility<?> secondSelectedAbility) {
        if (this.abilityInstances.containsKey(secondSelectedAbility)) {
            this.resetTicksSinceSecondAbilityActivity();
            AbstractAbilityInstance instance = this.abilityInstances.get(secondSelectedAbility);
            if (instance.getUseType() != AbstractAbility.UseType.MENU) {
                if (this.secondSelectedAbility != secondSelectedAbility) {
                    instance.onSelected();
                }

                this.secondSelectedAbility = secondSelectedAbility;
            }
        }
    }

    @Override
    public int getTicksSinceSecondAbilityActivity() {
        return ticksSinceSecondAbilityActivity;
    }

    @Override
    public void resetTicksSinceSecondAbilityActivity() {
        this.ticksSinceSecondAbilityActivity = 0;
    }

    @Override
    public AbstractAbilityInstance getSecondSelectedAbilityInstance() {
        return this.abilityInstances.get(this.secondSelectedAbility);
    }

    @Inject(method = "tickAbilities", at = @At(value = "FIELD",
            target = "Lnet/ltxprogrammer/changed/entity/variant/TransfurVariantInstance;selectedAbility:Lnet/ltxprogrammer/changed/ability/AbstractAbility;",
            ordinal = 0,
            opcode = Opcodes.GETFIELD,
            shift = At.Shift.BY)
    )
    private void changedAddon$onTickAbilities(CallbackInfo ci) {
        if (ChangedAddonServerConfiguration.ALLOW_SECOND_ABILITY_USE.get()) {
            if (!this.isTemporaryFromSuit() && this.shouldApplyAbilities()) {
                if (this.secondSelectedAbility != null) {
                    AbstractAbilityInstance instance = this.abilityInstances.get(this.secondSelectedAbility);
                    if (instance != null) {
                        AbstractAbility.Controller controller = instance.getController();
                        secondAbilityKey.handleStateUpdates((isDown, wasDown, unique) -> {
                            boolean oldState = controller.exchangeKeyState(isDown);
                            if (isDown || instance.getController().isCoolingDown())
                                this.resetTicksSinceSecondAbilityActivity();
                            if (host.containerMenu == host.inventoryMenu && !host.isUsingItem() && !instance.getController().isCoolingDown())
                                instance.getUseType().check(isDown, oldState, unique, controller);
                        });
                    }
                }
            }
        }
    }

    @Inject(method = "saveAbilities", at = @At("TAIL"))
    private void changedAddon$saveAbilities(CallbackInfoReturnable<CompoundTag> cir) {
        if (ChangedAddonServerConfiguration.ALLOW_SECOND_ABILITY_USE.get()) {
            CompoundTag returnValue = cir.getReturnValue();

            if (returnValue != null) {
                ResourceLocation selectedKey = ChangedRegistry.ABILITY.get().getKey(this.secondSelectedAbility);
                if (selectedKey != null) {
                    TagUtil.putResourceLocation(returnValue, "secondSelectedAbility", selectedKey);
                }
            }
        }
    }

    @Inject(method = "loadAbilities", at = @At("TAIL"))
    private void changedAddon$loadAbilities(CompoundTag tagAbilities, CallbackInfo ci) {
        if (ChangedAddonServerConfiguration.ALLOW_SECOND_ABILITY_USE.get()) {
            if (tagAbilities.contains("secondSelectedAbility")) {
                AbstractAbility<?> savedSelected = ChangedRegistry.ABILITY.get().getValue(TagUtil.getResourceLocation(tagAbilities, "secondSelectedAbility"));
                if (this.abilityInstances.containsKey(savedSelected)) {
                    this.secondSelectedAbility = savedSelected;
                }
            }
        }
    }

    @Inject(method = "canWear", at = @At("HEAD"), cancellable = true)
    private void negateArmor(Player player, ItemStack itemStack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.getItem() instanceof DarkLatexCoatItem && slot.getType() == EquipmentSlot.Type.ARMOR) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "meetsCriteriaForFlying", at = @At(value = "RETURN"), cancellable = true)
    private void negateFly(CallbackInfoReturnable<Boolean> cir) {
        if (!this.host.isCreative() && !this.host.isSpectator()) {
            if (getChangedEntity() instanceof VariantExtraStats variantExtraStats) {
                if (!variantExtraStats.getFlyType().canFly()) {
//                    if (host.getAbilities().flying || host.getAbilities().mayfly) {
//                        host.getAbilities().mayfly = false;
//                        host.getAbilities().flying = false;
//                        host.onUpdateAbilities();
//                    }
//
//                    ticksFlying = 0;
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void negateFlyInTick(CallbackInfo cir) {
        if (this.parent.canGlide && this.shouldApplyAbilities()) {
            if (!this.host.isCreative() && !this.host.isSpectator()) {
                if (this.getChangedEntity() instanceof VariantExtraStats variantExtraStats) {
                    if (!variantExtraStats.getFlyType().canFly()) {
                        if (this.host.getAbilities().flying || this.host.getAbilities().mayfly) {
                            this.host.getAbilities().mayfly = false;
                            this.host.getAbilities().flying = false;
                            this.host.onUpdateAbilities();
                        }
                    }
                }
            }

            if (!this.host.isSpectator()) { // Spectator Can have multiple fly speeds
                if (getChangedEntity() instanceof VariantExtraStats variantExtraStats) {
                    if (variantExtraStats.getFlySpeed() != 0) {
                        if (variantExtraStats.getFlyType().canFly()) {
                            if (!this.appliedFlySpeed) {
                                this.appliedFlySpeed = true;
                                this.host.getAbilities().setFlyingSpeed(variantExtraStats.getFlySpeed());
                                this.host.onUpdateAbilities();
                            }
                        }
                    }
                }
            }
        }

        if (this.shouldApplyAbilities()) {
            ++this.ticksSinceSecondAbilityActivity;
        }
    }

    @Inject(method = "unhookAll", at = @At("TAIL"))
    private void injectUnHookALl(Player player, CallbackInfo ci) {
        ChangedEntity changedEntity = this.getChangedEntity();
        if (changedEntity instanceof VariantExtraStats stats) {
            if (this.appliedFlySpeed) {
                this.appliedFlySpeed = false;
                this.host.getAbilities().setFlyingSpeed(AttributesHandle.DefaultPlayerFlySpeed);
                this.host.onUpdateAbilities();
            }
        }
        if (changedEntity instanceof IAlphaAbleEntity iAlphaAbleEntity) {
            iAlphaAbleEntity.cleanAlphaAttributesFromHost(changedEntity);
        }
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void InjectData(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag returnValue = cir.getReturnValue();
        if (this.getChangedEntity() instanceof VariantExtraStats stats) {
            stats.saveExtraData(returnValue);
        }

        returnValue.putBoolean("untransfurImmunity", getUntransfurImmunity(UntransfurEvent.UntransfurType.SURVIVAL));
        if (!getUntransfurImmunity(UntransfurEvent.UntransfurType.COMMAND)) {
            returnValue.putBoolean("untransfurImmunityCommand", getUntransfurImmunity(UntransfurEvent.UntransfurType.COMMAND));
        }
    }

    @Inject(method = "load", at = @At("RETURN"))
    private void readInjectedData(CompoundTag tag, CallbackInfo cir) {
        if (this.getChangedEntity() instanceof VariantExtraStats variantExtraStats) {
            variantExtraStats.readExtraData(tag);
        }

        if (tag.contains("untransfurImmunity"))
            setUntransfurImmunity(UntransfurEvent.UntransfurType.SURVIVAL, tag.getBoolean("untransfurImmunity"));
        if (tag.contains("untransfurImmunityCommand"))
            setUntransfurImmunity(UntransfurEvent.UntransfurType.COMMAND, tag.getBoolean("untransfurImmunity"));
    }

    /*@Inject(method = "canWear", at = @At("HEAD"), cancellable = true)
    private void negateArmorForms(Player player, ItemStack itemStack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir){
        if (this.getParent() == ChangedAddonTransfurVariants.LATEX_SNEP_FERAL_FORM.get() || this.getParent() == ChangedAddonTransfurVariants.LATEX_SNEP.get()){
            cir.setReturnValue(false);
        }
    }*/
}
