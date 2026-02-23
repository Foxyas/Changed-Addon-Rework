package net.foxyas.changedaddon.enchantment;

import com.google.common.base.Suppliers;
import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.foxyas.changedaddon.init.ChangedAddonEnchantments;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class LatexSolventEnchantment extends Enchantment {

    private static final Supplier<AttributeInstance> ATTRIB = Suppliers.memoize(() ->
            new AttributeInstance(ChangedAddonAttributes.LATEX_SOLVENT_DAMAGE_MULTIPLIER.get(), a -> {
            })
    );

    public LatexSolventEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    private static double getAttributeValueSafe(LivingEntity livingEntity, Attribute attribute) {
        AttributeInstance attributeInstance = livingEntity.getAttribute(attribute);
        if (attributeInstance != null) {
            return attributeInstance.getValue();
        }
        return 0;
    }

    public static double getLatexSolventLevelOfEntity(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            return getAttributeValueSafe(livingEntity, ChangedAddonAttributes.LATEX_SOLVENT_DAMAGE_MULTIPLIER.get());
        }

        if (entity instanceof ThrownTrident trident) {
            AttributeInstance attribute = ATTRIB.get();

            // Copia o item do tridente
            CompoundTag tag = new CompoundTag();
            trident.save(tag);
            ItemStack tridentItem = tag.contains("Trident")
                    ? ItemStack.of(tag.getCompound("Trident"))
                    : new ItemStack(Items.TRIDENT);

            tridentItem.getAttributeModifiers(EquipmentSlot.MAINHAND).get(ChangedAddonAttributes.LATEX_SOLVENT_DAMAGE_MULTIPLIER.get()).stream().filter((mod) -> !attribute.hasModifier(mod)).forEach(attribute::addTransientModifier);

            if (trident.getOwner() instanceof LivingEntity owner) {
                AttributeInstance entityAttrib = owner.getAttribute(ChangedAddonAttributes.LATEX_SOLVENT_DAMAGE_MULTIPLIER.get());
                if (entityAttrib != null) {
                    attribute.setBaseValue(entityAttrib.getBaseValue());
                    entityAttrib.getModifiers().stream().filter((mod) -> !attribute.hasModifier(mod)).forEach(attribute::addTransientModifier);
                }
            }

            double val = attribute.getValue();

            //reset attribute
            attribute.removeModifiers();
            attribute.setBaseValue(ChangedAddonAttributes.LATEX_SOLVENT_DAMAGE_MULTIPLIER.get().getDefaultValue());
            return val;
        }

        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment enchantment) {
        return this != enchantment && !Objects.equals(Enchantments.SHARPNESS, enchantment);
    }

    @Override
    public boolean canApplyAtEnchantingTable(@NotNull ItemStack itemstack) {
        return super.canApplyAtEnchantingTable(itemstack)
                || itemstack.is(ChangedAddonTags.Items.LATEX_SOLVENT_APPLICABLE)
                || itemstack.getItem() instanceof SwordItem
                || itemstack.getItem() instanceof AxeItem;
    }

    @Mod.EventBusSubscriber
    public static class LatexSolventEnchantmentEventHandle {

        // UUID fixo para garantir que o atributo não duplique
        private static final UUID LATEX_SOLVENT_DAMAGE_UUID = UUID.fromString("abcde123-4567-890a-bcde-f1234567890a");

        @SubscribeEvent
        public static void onItemAttributeModifiers(ItemAttributeModifierEvent event) {
            ItemStack stack = event.getItemStack();
            if (stack.isEmpty() || stack.is(Items.ENCHANTED_BOOK)) return;

            int level = stack.getEnchantmentLevel(ChangedAddonEnchantments.LATEX_SOLVENT.get());
            if (level <= 0) return;

            // Só aplica se estiver na mão principal
            if (event.getSlotType() == EquipmentSlot.MAINHAND) {
                double bonus = 0.2 * level; // +20% de dano por nível

                // Cria o modificador
                AttributeModifier modifier = new AttributeModifier(
                        LATEX_SOLVENT_DAMAGE_UUID,
                        "Latex Solvent Bonus",
                        bonus,
                        AttributeModifier.Operation.ADDITION // multiplica o dano base
                );

                // Adiciona o bônus de ataque
                event.addModifier(ChangedAddonAttributes.LATEX_SOLVENT_DAMAGE_MULTIPLIER.get(), modifier);
            }
        }

        /*@SubscribeEvent
        public static void onItemTooltip(ItemTooltipEvent event) {
            ItemStack stack = event.getItemStack();
            List<Component> tooltip = event.getToolTip();

            int EnchantLevel = EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonEnchantments.LATEX_SOLVENT.get(), stack);
            double math = 0 + EnchantLevel * 0.2;
            if (!(stack.getItem() instanceof BowItem) && !(stack.getItem() instanceof CrossbowItem)) {
                if (EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonEnchantments.LATEX_SOLVENT.get(), stack) != 0) {
                    TextComponent spaceText = Component.literal(" ");
                    String mathInText = "§r§e+" + String.format("%.2f", math * 100);
                    TextComponent latexSolventDamageInText = Component.literal((mathInText + "%§r §nLatex Solvent Damage"));

                    int idx = -1;
                    for (int i = 0; i < tooltip.size(); i++) {
                        if (tooltip.get(i).toString().contains("item.modifiers.mainhand")) {
                            idx = i;
                            break;
                        }
                    }

                    if (idx != -1) {
                        tooltip.add(idx, spaceText.append(latexSolventDamageInText));
                    } else {
                        //Fall Back
                        if (Screen.hasShiftDown()) {
                            tooltip.add(Component.literal(("§r§e+" + String.format("%.2f", math * 100) + "%§r §nLatex Solvent Damage")));
                        } else {
                            tooltip.add(Component.literal("Press §e<Shift>§r for show tooltip"));
                        }
                    }
                }
            }
        }*/

        @SubscribeEvent
        public static void onEntityAttacked(LivingHurtEvent event) {
            Entity target = event.getEntity();
            Entity directEntity = event.getSource().getDirectEntity();

            if (target == null || directEntity == null) {
                return;
            }

            // Verifica o nível do encantamento "Solvent"
            double latexSolventLevel = LatexSolventEnchantment.getLatexSolventLevelOfEntity(directEntity);
            if (latexSolventLevel <= 0) {
                return;
            }

            // Verifica se a entidade deve ser afetada
            if (shouldAffectEntity(target) && isValidWeapon(directEntity)) {
                applyEffects(event, target, (float) latexSolventLevel);
            }
        }

        private static boolean shouldAffectEntity(Entity entity) {
            return entity instanceof Player player && ProcessTransfur.isPlayerLatex(player)
                    || (entity.getType().is(ChangedTags.EntityTypes.LATEX)
                    && entity instanceof ChangedEntity);
        }

        private static boolean isValidWeapon(Entity entity) {
            if (entity instanceof LivingEntity livingEntity) {
                ItemStack mainHandItem = livingEntity.getMainHandItem();
                return !(mainHandItem.getItem() instanceof BowItem || mainHandItem.getItem() instanceof CrossbowItem);
            }
            return entity instanceof ThrownTrident || !(entity instanceof Projectile);
        }

        private static void applyEffects(LivingHurtEvent event, Entity target, float attributeValue) {
            // Multiplica o dano baseado no nível do encantamento
            float multiplier = event.getAmount() * calculateDamageMultiplier(attributeValue);
            event.setAmount(multiplier);

            // Aplica som e partículas
            playSoundAndParticles(target);
        }

        private static float calculateDamageMultiplier(float attributeValue) {
            return 1.0f + (attributeValue);
        }

        private static void playSoundAndParticles(Entity entity) {
            Level level = entity.level;

            // Toca som de extinção de fogo
            level.playSound(null, entity, SoundEvents.FIRE_EXTINGUISH, SoundSource.MASTER, 2.5f, 0);

            // Emite partículas
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        (SimpleParticleType) ChangedAddonParticleTypes.SOLVENT_PARTICLE.get(),
                        entity.getX(),
                        entity.getY() + 1,
                        entity.getZ(),
                        10,
                        0.2, 0.3, 0.2, 0.1
                );
            }
        }
    }
}
