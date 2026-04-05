package net.foxyas.changedaddon.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.ltxprogrammer.changed.init.ChangedEffects;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.item.SpecializedItemRendering;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractKatanaItem extends SwordItem implements SpecializedItemRendering {

    public AbstractKatanaItem(Tier tier, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(tier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public static void applyShockEffect(Entity entity, Entity sourceentity) {
        if (entity == null) {
            return;
        }
        LivingEntity enemy = (LivingEntity) entity;
        ChangedSounds.broadcastSound(enemy, ChangedSounds.TSC_WEAPON_SHOCK, 1.0F, 1.0F);
        enemy.addEffect(new MobEffectInstance(ChangedEffects.SHOCK.get(), 6, 0, false, false, true));
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        if (entity instanceof Player player) {
            var Variant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (Variant != null && Variant.canWear(player, new ItemStack(Items.DIAMOND_HELMET), EquipmentSlot.HEAD)) {
                return true;
            }
        }

        return super.canEquip(stack, armorType, entity);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemstack, @NotNull LivingEntity entity, @NotNull LivingEntity sourceentity) {
        boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
        applyShockEffect(entity, sourceentity);
        return retval;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) {
        Multimap<Attribute, AttributeModifier> baseModifiers = super.getDefaultAttributeModifiers(pEquipmentSlot);
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create(baseModifiers);
        if (pEquipmentSlot == EquipmentSlot.MAINHAND) {
            modifiers.put(ForgeMod.ENTITY_REACH.get(),
                    new AttributeModifier("Weapon modifier", 1.0, AttributeModifier.Operation.MULTIPLY_BASE));
        }

        return modifiers;
    }

    public void spawnElectricSwingParticle(LivingEntity source, float attackRange) {
        if (source == null) {
            return;
        }
        double d0 = (double) (-Mth.sin(source.getYRot() * 0.017453292F)) * attackRange;
        double d1 = (double) Mth.cos(source.getYRot() * 0.017453292F) * attackRange;
        Level var7 = source.level;
        if (var7 instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ChangedParticles.TSC_SWEEP_ATTACK.get(), source.getX() + d0, source.getY(0.5), source.getZ() + d1, 0, d0, 0.0, d1, 0.0);
        }

    }
}
