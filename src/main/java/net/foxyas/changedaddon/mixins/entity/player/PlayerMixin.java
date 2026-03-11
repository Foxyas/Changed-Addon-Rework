package net.foxyas.changedaddon.mixins.entity.player;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.ability.ClawsAbility;
import net.foxyas.changedaddon.client.renderer.items.HazardBodySuitClothingRenderer;
import net.foxyas.changedaddon.entity.api.LivingEntityDataExtensor;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.item.AbstractKatanaItem;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.variant.VariantExtraStats;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.data.AccessorySlotType;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements LivingEntityDataExtensor {

    @Shadow
    protected boolean wasUnderwater;

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    public abstract @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot equipmentSlot);

    @Override
    public boolean isInWater() {
        boolean inWater = super.isInWater();
        if (!inWater) {
            return this.overrideIsInWater();
        }
        return super.isInWater();
    }

    @Inject(method = "sweepAttack", at = @At("HEAD"), cancellable = true)
    private void customSweepAttackEffect(CallbackInfo ci) {
        if (this.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof AbstractKatanaItem abstractKatanaItem) {
            ci.cancel();
            abstractKatanaItem.spawnElectricSwingParticle((Player) (Object) this, 2);
        }
    }

    @Inject(method = "updateSwimming", at = @At("RETURN"))
    private void customUpdateSwimming(CallbackInfo ci) {
        Player self = ((Player) (Object) this);
        if (this.overrideSwimUpdate()) {
            self.setSwimming(self.isSprinting() && !self.isPassenger());
        }
    }


    @Inject(method = "updateIsUnderwater", at = @At("RETURN"), cancellable = true)
    private void customUpdateUnderwater(CallbackInfoReturnable<Boolean> cir) {
        Boolean returnValue = cir.getReturnValue();
        if (returnValue != null) {
            if (!returnValue) {
                this.wasUnderwater = overrideSwim();
                cir.setReturnValue(wasUnderwater);
            }
        }
    }

    @Inject(method = "isModelPartShown", at = @At("HEAD"), cancellable = true)
    private void hideModelParts(PlayerModelPart pPart, CallbackInfoReturnable<Boolean> cir) {
        final Player thisFixed = (Player) (Object) this;
        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(thisFixed);
        if (transfurVariantInstance != null && transfurVariantInstance.isTransfurring()) {
            return;
        }

        if (transfurVariantInstance != null
                && !ChangedAddonTransfurVariants.getHumanForms().contains(transfurVariantInstance.getParent())) {
            //Fixme Maybe Change How the Process of hide stuff works?
            //Fixme Right when the player is a latex human this handle it but when the player has a null transfur variant the PlayerRendererMixin handle it, maybe change to be just here?
        } else if (transfurVariantInstance != null && ChangedAddonTransfurVariants.getHumanForms().contains(transfurVariantInstance.getParent())) {
            for (EquipmentSlot slot : Arrays.stream(EquipmentSlot.values()).filter((equipmentSlot) -> equipmentSlot.getType() == EquipmentSlot.Type.ARMOR).toList()) {
                AccessorySlots accessorySlots = AccessorySlots.getForEntity(thisFixed).get();
                List<AccessorySlotType> list = accessorySlots.getSlotTypes().filter((slotType) -> slotType.getEquivalentSlot() == slot).toList();
                for (AccessorySlotType slotType : list) {
                    Optional<ItemStack> item = accessorySlots.getItem(slotType);
                    ItemStack itemStack = item.isPresent() ? item.get() : ItemStack.EMPTY;
                    if (itemStack.is(ChangedAddonItems.HAZARD_BODY_SUIT.get())) {
                        if (pPart == PlayerModelPart.HAT) {
                            cir.setReturnValue(!HazardBodySuitClothingRenderer.shouldHideHat(thisFixed));
                        } else {
                            cir.setReturnValue(false);
                        }
                    }
                }
            }
        }

    }

    /* // Maybe Fix the Blurp Sound After "drinking" a food item?
    @Inject(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), cancellable = true)
    private void stopSound(Level pLevel, ItemStack pFood, CallbackInfoReturnable<ItemStack> cir) {
        if (pFood.getUseAnimation().equals(UseAnim.DRINK) && pFood.isEdible()) {
            cir.cancel();
        }
    }*/

    @Inject(method = "attack", at = @At("HEAD"))
    private void CustomClawSweepAttack(Entity entity, CallbackInfo ci) {
        //System.out.println("O attack foi disparado!");
        Player player = (Player) (Object) this;
        //System.out.println("O ataque foi feito por um jogador: " + player.getName().getString());
        if (player.getAttackStrengthScale(0.0f) < 0.9)
            return;
        //System.out.println("Ataque carregado o suficiente!");
        ProcessTransfur.getPlayerTransfurVariantSafe(player).ifPresent((variantInstance -> {
            AbstractAbilityInstance abilityInstance = variantInstance.getAbilityInstance(ChangedAddonAbilities.CLAWS.get());
            if (abilityInstance != null) {
                AbstractAbility<?> clawAbility = variantInstance.getAbilityInstance(ChangedAddonAbilities.CLAWS.get()).ability;
                if (clawAbility instanceof ClawsAbility ability && ability.isActive && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                    //System.out.println("Habilidade ativada!");
                    // ⚔ Área de efeito: Raio de 1.5 blocos ao redor do alvo
                    double radius = 1;
                    AABB attackArea = entity.getBoundingBox().inflate(radius, 0.25, radius);
                    List<LivingEntity> nearbyEntities = player.level.getEntitiesOfClass(LivingEntity.class, attackArea);
                    //System.out.println("Entidades próximas: " + nearbyEntities.size());
                    float f = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    float f3 = nearbyEntities.isEmpty() ? f : f / nearbyEntities.size();
                    // 🔥 Knockback em todos os alvos próximos (exceto o atacante)
                    for (LivingEntity livingEntity : nearbyEntities) {
                        if (livingEntity != entity && livingEntity != player && (!(livingEntity instanceof ArmorStand) || !((ArmorStand) livingEntity).isMarker()) && player.canReach(livingEntity, 0)) {
                            livingEntity.knockback(0.4, Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(player.getYRot() * ((float) Math.PI / 180F)));
                            livingEntity.hurt(level().damageSources().playerAttack(player), f3);
                            //System.out.println("Dano causado em " + livingEntity.getName().getString());
                        }
                    }
                    // Efeito visual
                    double d0 = (double) (-Mth.sin(player.getYRot() * 0.017453292F)) * 1;
                    double d1 = (double) Mth.cos(player.getYRot() * 0.017453292F) * 1;
                    if (player.level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.5), player.getZ() + d1, 0, d0, 0.0, d1, 0.0);
                        serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.6), player.getZ() + d1, 0, d0, 0.0, d1, 0.0);
                        serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.7), player.getZ() + d1, 0, d0, 0.0, d1, 0.0);
                        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1f, 0.75f);
                    }
                }
            }
        }));
    }

    @WrapOperation(method = "causeFallDamage", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;mayfly:Z", opcode = Opcodes.GETFIELD))
    public boolean changed$shouldIgnoreFallDamage(Abilities instance, Operation<Boolean> original) {
        var self = (Player) (Object) this;
        TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(self);
        if (transfurVariant != null && transfurVariant.getChangedEntity() instanceof VariantExtraStats variantExtraStats)
            return original.call(instance) && !variantExtraStats.shouldTakeFallDamage();
        return original.call(instance);
    }

}
