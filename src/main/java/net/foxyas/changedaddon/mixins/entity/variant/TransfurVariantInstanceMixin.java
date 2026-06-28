package net.foxyas.changedaddon.mixins.entity.variant;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.event.UntransfurEvent;
import net.foxyas.changedaddon.item.armor.DarkLatexCoatItem;
import net.foxyas.changedaddon.variant.ILatexVariantExtraStats;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.util.KeyStateTracker;
import net.ltxprogrammer.changed.util.TagUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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

    @Shadow
    public abstract TransfurVariant<?> getParent();

    @Shadow
    public abstract boolean shouldApplyAbilities();

    @Shadow
    public abstract ChangedEntity getChangedEntity();

    @Shadow
    public abstract boolean isTemporaryFromSuit();

    @Shadow
    public abstract Player getHost();

    @Inject(at = @At("HEAD"), method = "lambda$onBlockRightClick$13", cancellable = true)
    private static void allowCuddleInteract(PlayerInteractEvent.RightClickBlock event, TransfurVariantInstance<?> variant, CallbackInfo ci) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        if (!level.getBlockState(pos).isBed(level, pos, variant.getHost())) return;

        GrabEntityAbilityInstance instance = variant.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
        if (instance != null && instance.grabbedEntity != null) ci.cancel();
    }

    @Override
    public boolean getUntransfurImmunity(UntransfurEvent.UntransfurType type) {
        return type == UntransfurEvent.UntransfurType.SURVIVAL ? untransfurImmunity : untransfurImmunityCommand;
    }

    @Override
    public void setUntransfurImmunity(UntransfurEvent.UntransfurType type, boolean value) {
        if (type == UntransfurEvent.UntransfurType.SURVIVAL) untransfurImmunity = value;
        if (type == UntransfurEvent.UntransfurType.COMMAND) untransfurImmunityCommand = value;
        maySendDataUpdate();
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
        if (!abilityInstances.containsKey(secondSelectedAbility)) return;

        this.resetTicksSinceSecondAbilityActivity();
        AbstractAbilityInstance instance = this.abilityInstances.get(secondSelectedAbility);
        if (instance == null) return;
        if (instance.getUseType() == AbstractAbility.UseType.MENU) return;

        if (this.secondSelectedAbility != secondSelectedAbility) {
            instance.onSelected();
        }

        this.secondSelectedAbility = secondSelectedAbility;
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
        if (!ChangedAddonServerConfiguration.ALLOW_SECOND_ABILITY_USE.get()) return;

        if (this.isTemporaryFromSuit() || !this.shouldApplyAbilities()) return;

        if (this.secondSelectedAbility == null) return;

        AbstractAbilityInstance instance = this.abilityInstances.get(this.secondSelectedAbility);
        if (instance == null) return;

        AbstractAbility.Controller controller = instance.getController();
        secondAbilityKey.handleStateUpdates((isDown, wasDown, unique) -> {
            boolean oldState = controller.exchangeKeyState(isDown);
            if (isDown || instance.getController().isCoolingDown())
                this.resetTicksSinceSecondAbilityActivity();
            if (host.containerMenu == host.inventoryMenu && !host.isUsingItem() && !instance.getController().isCoolingDown())
                instance.getUseType().check(isDown, oldState, unique, controller);
        });
    }

    @Inject(method = "saveAbilities", at = @At("TAIL"))
    private void changedAddon$saveAbilities(CallbackInfoReturnable<CompoundTag> cir) {
        if (!ChangedAddonServerConfiguration.ALLOW_SECOND_ABILITY_USE.get()) return;

        CompoundTag returnValue = cir.getReturnValue();
        if (returnValue == null) return;

        ResourceLocation selectedKey = ChangedRegistry.ABILITY.get().getKey(this.secondSelectedAbility);
        if (selectedKey != null) {
            TagUtil.putResourceLocation(returnValue, "secondSelectedAbility", selectedKey);
        }
    }

    @Inject(method = "loadAbilities", at = @At("TAIL"))
    private void changedAddon$loadAbilities(CompoundTag tagAbilities, CallbackInfo ci) {
        if (!ChangedAddonServerConfiguration.ALLOW_SECOND_ABILITY_USE.get()) return;

        if (!tagAbilities.contains("secondSelectedAbility")) return;

        AbstractAbility<?> savedSelected = ChangedRegistry.ABILITY.get().getValue(TagUtil.getResourceLocation(tagAbilities, "secondSelectedAbility"));
        if (this.abilityInstances.containsKey(savedSelected)) {
            this.secondSelectedAbility = savedSelected;
        }
    }

    @Inject(method = "canWear", at = @At("HEAD"), cancellable = true)
    private void negateArmor(Player player, ItemStack itemStack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.getItem() instanceof DarkLatexCoatItem && slot.getType() == EquipmentSlot.Type.ARMOR) {
            cir.setReturnValue(false);
        }
    }


    @ModifyReturnValue(method = "canElytraGlide", at = @At("RETURN"))
    private boolean canElytraGlideHook(boolean original) {
        if (this.getChangedEntity() instanceof ILatexVariantExtraStats ILatexVariantExtraStats) {
            return ILatexVariantExtraStats.getFlyType().canGlide();
        }
        return original;
    }

    @ModifyReturnValue(method = "canCreativeFly", at = @At("RETURN"))
    private boolean canCreativeFlyHook(boolean original) {
        if (this.getChangedEntity() instanceof ILatexVariantExtraStats ILatexVariantExtraStats) {
            return ILatexVariantExtraStats.getFlyType().canFly();
        }
        return original;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickHook(CallbackInfo cir) {
        if (this.shouldApplyAbilities()) {
            ++this.ticksSinceSecondAbilityActivity;
        }
    }

    @Inject(method = "unhookAll", at = @At("TAIL"))
    private void injectUnHookALl(Player player, CallbackInfo ci) {
        ChangedEntity changedEntity = this.getChangedEntity();
        if (changedEntity instanceof IAlphaAbleEntity iAlphaAbleEntity) {
            iAlphaAbleEntity.cleanAlphaAttributesFromHost(changedEntity);
        }
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void InjectData(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag returnValue = cir.getReturnValue();
        if (this.getChangedEntity() instanceof ILatexVariantExtraStats stats) {
            stats.saveExtraData(returnValue);
        }

        returnValue.putBoolean("untransfurImmunity", getUntransfurImmunity(UntransfurEvent.UntransfurType.SURVIVAL));
        if (!getUntransfurImmunity(UntransfurEvent.UntransfurType.COMMAND)) {
            returnValue.putBoolean("untransfurImmunityCommand", getUntransfurImmunity(UntransfurEvent.UntransfurType.COMMAND));
        }
    }

    @Inject(method = "load", at = @At("RETURN"))
    private void readInjectedData(CompoundTag tag, CallbackInfo cir) {
        if (this.getChangedEntity() instanceof ILatexVariantExtraStats ILatexVariantExtraStats) {
            ILatexVariantExtraStats.readExtraData(tag);
        }

        if (tag.contains("untransfurImmunity"))
            setUntransfurImmunity(UntransfurEvent.UntransfurType.SURVIVAL, tag.getBoolean("untransfurImmunity"));
        if (tag.contains("untransfurImmunityCommand"))
            setUntransfurImmunity(UntransfurEvent.UntransfurType.COMMAND, tag.getBoolean("untransfurImmunityCommand"));
    }
}
