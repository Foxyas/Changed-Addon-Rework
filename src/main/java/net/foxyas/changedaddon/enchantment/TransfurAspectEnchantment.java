package net.foxyas.changedaddon.enchantment;

import net.foxyas.changedaddon.init.ChangedAddonEnchantments;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.ai.LatexAssimilationDecision;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TransfurAspectEnchantment extends Enchantment {

    public TransfurAspectEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public static float getTransfurDamage(@NotNull LivingEntity attacker, @Nullable Entity target, int enchantLevel) {
        AttributeInstance instance = attacker.getAttribute(ChangedAttributes.TRANSFUR_DAMAGE.get());
        if (instance == null) return 0f;

        double baseValue = instance.getValue();

        // Formula notes:
        //   - Divide by 4.0 to keep numbers small
        //   - Multiply by enchant level
        //   - Extra multiplier (0.75) to soften scaling
        return (float) (baseValue * 0.75f * enchantLevel / 4.0);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canApplyAtEnchantingTable(@NotNull ItemStack itemstack) {
        return super.canApplyAtEnchantingTable(itemstack)
                || itemstack.getItem() instanceof SwordItem
                || itemstack.getItem() instanceof AxeItem;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack itemstack) {
        return super.canEnchant(itemstack) // Default Behavior
                || itemstack.getItem() instanceof SwordItem
                || itemstack.getItem() instanceof AxeItem;
    }

    @Override
    public void doPostAttack(@NotNull LivingEntity pAttacker, @NotNull Entity pTarget, int pLevel) {
        super.doPostAttack(pAttacker, pTarget, pLevel);
        if (pAttacker.level().isClientSide()) return;

        if (!(pAttacker instanceof Player player)) {
            if (pAttacker instanceof ChangedEntity changedEntity) {
                if (!(pTarget instanceof LivingEntity livingEntity)) return;

                TransfurVariant<?> variant = changedEntity.getSelfVariant();
                if (variant == null) return;

                // Only works if the variant is latex-based
                boolean isLatex = changedEntity.getType().is(ChangedTags.EntityTypes.LATEX);
                if (!isLatex) return;

                // Context requires the changed entity representation
                IAbstractChangedEntity iAbstractChangedEntity = IAbstractChangedEntity.forEntity(changedEntity);

                // Pick a random transfur cause
                TransfurCause[] causes = TransfurCause.values();
                TransfurCause randomCause = causes[changedEntity.getRandom().nextInt(causes.length)];

                // Build context with random cause
                TransfurContext context = TransfurContext.latexHazard(iAbstractChangedEntity, randomCause);

                // Calculate transfur damage
                float transfurDamage = getTransfurDamage(changedEntity, livingEntity, pLevel);
                if (transfurDamage <= 0) return;

                // Apply transfur progress
                LatexAssimilationDecision<?> latexAssimilationDecision = LatexAssimilationDecision.strong(context.cause() == TransfurCause.GRAB_ABSORB ? LatexAssimilationDecision.Method.ABSORPTION : LatexAssimilationDecision.Method.REPLICATION, variant, context, transfurDamage);
                ProcessTransfur.progressTransfur(livingEntity, latexAssimilationDecision);
                return;
            }
            return;
        }


        if (!(pTarget instanceof LivingEntity livingEntity)) return;

        // Get player transfur variant
        TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (variant == null) return;

        // Only works if the variant is latex-based
        boolean isLatex = variant.getChangedEntity().getType().is(ChangedTags.EntityTypes.LATEX);
        if (!isLatex) return;

        // Context requires the changed entity representation
        IAbstractChangedEntity changedEntity = IAbstractChangedEntity.forPlayer(player);

        // Pick a random transfur cause
        TransfurCause[] causes = TransfurCause.values();
        TransfurCause randomCause = causes[player.getRandom().nextInt(causes.length)];

        // Build context with random cause
        TransfurContext context = TransfurContext.latexHazard(changedEntity, randomCause);

        // Calculate transfur damage
        float transfurDamage = getTransfurDamage(player, livingEntity, pLevel);
        if (transfurDamage <= 0) return;

        // Apply transfur progress
        LatexAssimilationDecision<?> latexAssimilationDecision = LatexAssimilationDecision.strong(context.cause() == TransfurCause.GRAB_ABSORB ? LatexAssimilationDecision.Method.ABSORPTION : LatexAssimilationDecision.Method.REPLICATION, variant.getParent(), context, transfurDamage);
        ProcessTransfur.progressTransfur(livingEntity, latexAssimilationDecision);
    }

    @Mod.EventBusSubscriber
    public static class ShowTransfurDmg {

        @SubscribeEvent
        public static void onItemTooltip(ItemTooltipEvent event) {
            Player player = event.getEntity();
            if (player == null) return;

            // Get the Transfur Aspect enchantment level from the item
            int enchantLevel = EnchantmentHelper.getTagEnchantmentLevel(
                    ChangedAddonEnchantments.TRANSFUR_ASPECT.get(), event.getItemStack()
            );
            if (enchantLevel <= 0) return; // Exit if the item doesn't have the enchantment

            List<Component> tooltip = event.getToolTip();
            if (!Screen.hasShiftDown()) {
                // If Shift not held, show hint
                tooltip.add(Component.literal("Press §e<Shift>§r for show tooltip"));
                return;
            }

            // Use base value of 1.0 here; can be dynamically adjusted for the actual player stats
            float baseValue = getTransfurDamage(event.getEntity(), null, enchantLevel);

            // Calculate the Transfur Damage using the same formula as in doPostAttack
            float damage = baseValue * 0.75f * enchantLevel / 4f;

            // Add tooltip showing the damage
            tooltip.add(Component.literal("§r§e+" + String.format("%.2f", damage) + "§r Transfur Damage to Humanoids"));
        }
    }
}
