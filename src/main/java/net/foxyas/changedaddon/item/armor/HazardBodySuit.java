package net.foxyas.changedaddon.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.item.clothes.AccessoryItemExtension;
import net.foxyas.changedaddon.util.ComponentUtil;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.data.AccessorySlotContext;
import net.ltxprogrammer.changed.data.AccessorySlotType;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.LatexHuman;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.*;
import net.ltxprogrammer.changed.item.ClothingItem;
import net.ltxprogrammer.changed.item.ClothingState;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public class HazardBodySuit extends ClothingItem implements AccessoryItemExtension {
    public static final BooleanProperty HELMET = BooleanProperty.create("helmet");

    public HazardBodySuit() {
        super();
    }

    @Override
    public ClothingState defaultClothingState() {
        return super.defaultClothingState().setValue(HELMET, false);
    }

    @Override
    public SoundEvent getEquipSound(ItemStack itemStack) {
        return ChangedAddonSoundEvents.ARMOR_EQUIP.get();
    }

    @Override
    public void accessoryInteract(AccessorySlotContext<?> slotContext) {
        super.accessoryInteract(slotContext);
        LivingEntity wearer = slotContext.wearer();
        boolean canChange = true;

        if (wearer instanceof ChangedEntity changedEntity) {
            TransfurVariant<?> selfVariant = changedEntity.getSelfVariant();
            if (selfVariant != null && !selfVariant.is(ChangedTransfurVariants.LATEX_HUMAN.get())
                    && !selfVariant.is(ChangedTransfurVariants.LATEX_HUMAN.get())) {
                canChange = false;
            }
        } else if (wearer instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant != null && !transfurVariant.is(ChangedTransfurVariants.LATEX_HUMAN.get())
                    && !transfurVariant.is(ChangedTransfurVariants.LATEX_HUMAN.get())) {
                player.displayClientMessage(Component.translatable("text.changed_addon.display.hazard_body_suit.cant_have_helmet"), true);
                canChange = false;
            }
        }

        if (canChange) {
            changeHelmetState(slotContext);
        } else {
            negateHelmetChange(slotContext);
        }
    }

    public void changeHelmetState(AccessorySlotContext<?> slotContext) {
        this.setClothingState(
                slotContext.stack(),
                this.getClothingState(slotContext.stack()).cycle(HELMET)
        );

        SoundEvent changeSound = this.getEquipSound(slotContext.stack());
        if (changeSound != null) {
            slotContext.wearer().playSound(changeSound, 1.0F, 1.0F);
        }
    }

    @Override
    protected void createClothingStateDefinition(StateDefinition.Builder<ClothingItem, ClothingState> builder) {
        super.createClothingStateDefinition(builder);
        builder.add(HELMET);
    }

    @Override
    protected void addInteractInstructions(Consumer<Component> builder) {
        builder.accept(Component.translatable(
                INTERACT_INSTRUCTIONS,
                Minecraft.getInstance().options.keyUse.getTranslatedKeyMessage()
        ).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 525;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    //For future api
    @Override
    public float accessoryHurt(AccessorySlotContext<?> slotContext, DamageSource source, float amount) {
        damageSuit(slotContext, source, amount);
        return super.accessoryHurt(slotContext, source, amount);
    }

    public void damageSuit(AccessorySlotContext<?> slotContext, DamageSource source, float amount) {
        LivingEntity wearer = slotContext.wearer();
        if (amount <= 0) return;
        if (wearer.isDamageSourceBlocked(source)) return;

        wearer.getCombatTracker().recheckStatus();
        if (!wearer.getCombatTracker().takingDamage) return;

        if (!source.is(DamageTypeTags.BYPASSES_ARMOR) && !(source.is(ChangedDamageSources.TRANSFUR.key()))) {
            this.applyDamage(source, amount, slotContext);
        } else if (source.is(ChangedDamageSources.TRANSFUR.key())) {
            this.applyDamage(source, amount, slotContext);
        }
    }

    @Override
    public SoundEvent getBreakSound(ItemStack itemStack) {
        return super.getBreakSound(itemStack);
    }

    @Override
    public boolean isConsideredByEnchantment(AccessorySlotContext<?> slotContext, Enchantment enchantment) {
        AccessorySlotType slotType = slotContext.slotType();
        ItemStack itemStack = slotContext.stack();
        LivingEntity wearer = slotContext.wearer();
        if (enchantment == ChangedEnchantments.TRANSFUR_RESISTANCE.get()) {
            return slotType.canHoldItem(itemStack, wearer);
        }

        if (enchantment == Enchantments.ALL_DAMAGE_PROTECTION) {
            return slotType.canHoldItem(itemStack, wearer);
        }

        if (enchantment == Enchantments.MENDING) {
            return slotType.canHoldItem(itemStack, wearer);
        }

        if (enchantment == Enchantments.THORNS) {
            return slotType.canHoldItem(itemStack, wearer);
        }

        return super.isConsideredByEnchantment(slotContext, enchantment);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == ChangedEnchantments.TRANSFUR_RESISTANCE.get()) {
            return true;
        }

        if (enchantment == Enchantments.ALL_DAMAGE_PROTECTION) {
            return true;
        }

        if (enchantment == Enchantments.THORNS) {
            return true;
        }


        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public void accessoryTick(AccessorySlotContext<?> slotContext) {
        super.accessoryTick(slotContext);

        LivingEntity wearer = slotContext.wearer();
        ItemStack stack = slotContext.stack();

        if (!this.getClothingState(stack).getValue(HELMET)) return;

        if (wearer instanceof ChangedEntity changedEntity) {
            TransfurVariant<?> selfVariant = changedEntity.getSelfVariant();
            if (selfVariant != null && !selfVariant.is(ChangedTransfurVariants.LATEX_HUMAN.get())
                    && !selfVariant.is(ChangedTransfurVariants.LATEX_HUMAN.get())) {
                setHelmetStage(slotContext, false);
            }
        } else if (wearer instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant != null && !transfurVariant.is(ChangedTransfurVariants.LATEX_HUMAN.get())) {
                setHelmetStage(slotContext, false);
                player.displayClientMessage(ComponentUtil.translatable("text.changed_addon.display.hazard_body_suit.cant_have_helmet"), true);
            }
        }
    }

    public void setHelmetStage(AccessorySlotContext<?> slotContext, boolean value) {
        this.setClothingState(
                slotContext.stack(),
                this.getClothingState(slotContext.stack()).setValue(HELMET, value)
        );

        SoundEvent changeSound = this.getEquipSound(slotContext.stack());
        if (changeSound != null) {
            slotContext.wearer().playSound(changeSound, 1.0F, 1.0F);
        }
    }

    public void negateHelmetChange(AccessorySlotContext<?> slotContext) {
        this.setClothingState(
                slotContext.stack(),
                this.getClothingState(slotContext.stack()).setValue(HELMET, false)
        );

        SoundEvent changeSound = ChangedSounds.EXOSKELETON_CHIME.get();
        slotContext.wearer().playSound(changeSound, 1.0F, 0.5F);
    }

    public void applyDamage(DamageSource damageSource, float amount, AccessorySlotContext<?> slotContext) {
        if (amount <= 0) return;
        if (damageSource.is(DamageTypeTags.IS_FIRE) && !damageSource.is(DamageTypes.LAVA)) return;

        amount /= 4.0F;
        if (amount < 1.0F) {
            amount = 1.0F;
        }
        ItemStack itemStack = slotContext.stack();
        LivingEntity player = slotContext.wearer();
        if ((!damageSource.is(DamageTypeTags.IS_FIRE) || !itemStack.getItem().isFireResistant())) {
            itemStack.hurtAndBreak((int) amount, player, (livingEntity) -> {
                if (!itemStack.isEmpty()) {
                    if (!livingEntity.isSilent()) {
                        livingEntity.level().playSound(null, livingEntity,
                                this.getBreakSound(itemStack),
                                livingEntity.getSoundSource(),
                                0.8F,
                                0.8F + livingEntity.getRandom().nextFloat() * 0.4F);
                    }

                    //livingEntity.spawnItemParticles(pStack, 5);

                    for (int i = 0; i < 5; ++i) {
                        Vec3 vec3 = new Vec3(((double) livingEntity.getRandom().nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
                        vec3 = vec3.xRot(-livingEntity.getXRot() * ((float) Math.PI / 180F));
                        vec3 = vec3.yRot(-livingEntity.getYRot() * ((float) Math.PI / 180F));
                        double d0 = (double) (-livingEntity.getRandom().nextFloat()) * 0.6D - 0.3D;
                        Vec3 vec31 = new Vec3(((double) livingEntity.getRandom().nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
                        vec31 = vec31.xRot(-livingEntity.getXRot() * ((float) Math.PI / 180F));
                        vec31 = vec31.yRot(-livingEntity.getYRot() * ((float) Math.PI / 180F));
                        vec31 = vec31.add(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
                        if (livingEntity.level() instanceof ServerLevel serverLevel) //Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server specific variant
                        {
                            serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemStack), vec31.x, vec31.y, vec31.z, 1, vec3.x, vec3.y + 0.05D, vec3.z, 0.0D);
                        }
                    }

                }
                //livingEntity.broadcastBreakEvent(EquipmentSlot.CHEST);
            });
        }

    }

    @Override
    public boolean allowedInSlot(ItemStack itemStack, LivingEntity wearer, AccessorySlotType slot) {
        return slot == ChangedAccessorySlots.FULL_BODY.get();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.CHEST) return super.getAttributeModifiers(slot, stack);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        Boolean helmeted = this.getClothingState(stack).getValue(HELMET);
        float multiplier = helmeted ? 1f : 0.75f;
        builder.put(
                ChangedAttributes.TRANSFUR_TOLERANCE.get(),
                new AttributeModifier(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        "Hazard Transfur Tolerance Buff",
                        multiplier,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
        );

        builder.put(
                ChangedAttributes.TRANSFUR_DAMAGE.get(),
                new AttributeModifier(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        "Hazard Transfur Tolerance Buff",
                        -1,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
        );

        builder.put(
                ChangedAddonAttributes.LATEX_RESISTANCE.get(),
                new AttributeModifier(
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        "Hazard Armor Speed Debuff",
                        0.05 * multiplier,
                        AttributeModifier.Operation.ADDITION
                )
        );

        builder.put(
                Attributes.ARMOR,
                new AttributeModifier(
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        "Hazard Armor Buff",
                        2 * multiplier,
                        AttributeModifier.Operation.ADDITION
                )
        );

        builder.put(
                Attributes.MOVEMENT_SPEED,
                new AttributeModifier(
                        UUID.fromString("00000000-0000-0000-0000-000000000003"),
                        "Hazard Armor Speed Debuff",
                        -0.05,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
        );

        builder.put(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                        UUID.fromString("00000000-0000-0000-0000-000000000004"),
                        "Hazard Armor Attack Speed Debuff",
                        -0.09,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
        );

        return builder.build();
    }

    public String getHelmetState(ItemStack stack) {
        Boolean flag = this.getClothingState(stack).getValue(HELMET);
        return flag ? "helmeted" : "no_helmet";
    }

    @OnlyIn(Dist.CLIENT)
    private <T extends Entity> String getPlayerModelStyle(T entity) {
        if (entity instanceof AbstractClientPlayer player) {
            return player.getModelName();
        }
        return "default";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null) return null; // Shutup the warnings

        if (entity instanceof ChangedEntity changedEntity) {
            if (changedEntity instanceof LatexHuman latexHuman && latexHuman.getUnderlyingPlayer() instanceof AbstractClientPlayer abstractClientPlayer) {
                return String.format("%s:textures/models/hazard_suit/%s_%s_%s.png", itemId.getNamespace(), itemId.getPath(), getHelmetState(stack), getPlayerModelStyle(abstractClientPlayer));
            }

            return String.format("%s:textures/models/hazard_suit/%s_%s_tf.png", itemId.getNamespace(), itemId.getPath(), getHelmetState(stack));
        }
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant != null && transfurVariant.isTransfurring()) {
                return String.format("%s:textures/models/hazard_suit/%s_%s_tf.png", itemId.getNamespace(), itemId.getPath(), getHelmetState(stack));
            } else if (transfurVariant != null && ChangedAddonTransfurVariants.getHumanForms().contains(transfurVariant.getParent())) {
                return String.format("%s:textures/models/hazard_suit/%s_%s_%s.png", itemId.getNamespace(), itemId.getPath(), getHelmetState(stack), getPlayerModelStyle(entity));
            } else if (transfurVariant != null) {
                return String.format("%s:textures/models/hazard_suit/%s_%s_tf.png", itemId.getNamespace(), itemId.getPath(), getHelmetState(stack));
            }
        }

        return String.format("%s:textures/models/hazard_suit/%s_%s_%s.png", itemId.getNamespace(), itemId.getPath(), getHelmetState(stack), getPlayerModelStyle(entity));
    }
}
