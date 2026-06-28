package net.foxyas.changedaddon.item;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.foxyas.changedaddon.client.model.ModelNewHyperFlower;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LunarRoseItem extends ArmorItem {

    public LunarRoseItem() {
        super(new ArmorMaterial() {
            @Override
            public int getDurabilityForType(Type pType) {
                return 1100;
            }

            @Override
            public int getDefenseForType(Type pType) {
                return 0;
            }

            @Override
            public int getEnchantmentValue() {
                return 100;
            }

            @Override
            public @NotNull SoundEvent getEquipSound() {
                return SoundEvents.ARMOR_EQUIP_LEATHER;
            }

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of();
            }

            @Override
            public @NotNull String getName() {
                return "lunar_rose";
            }

            @Override
            public float getToughness() {
                return 0f;
            }

            @Override
            public float getKnockbackResistance() {
                return 0f;
            }
        }, Type.HELMET, new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .fireResistant().durability(-1));
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "changed_addon:textures/entities/new_moon_rose.png";
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) {
        return ImmutableMultimap.of();
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {

        // For compatibility reasons we have to use non-local index values, I think this is a vanilla bug but lets maintain compatibility
        var inv = player.getInventory();
        int vanillaIndex = slotIndex;
        if (slotIndex >= inv.items.size()) {
            vanillaIndex -= inv.items.size();
            if (!(vanillaIndex >= inv.armor.size())) {
                onArmorTicks(stack, level, player);
            }
        }


        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }

    public void onArmorTicks(ItemStack itemstack, Level level, Player player) {
        if (player == null || level.isClientSide) return;

        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (instance != null && !instance.is(ChangedAddonTransfurVariants.PURO_KIND_MALE)
                && !instance.is(ChangedAddonTransfurVariants.PURO_KIND_FEMALE))
            return;//!instance.getFormId().toString().equals("changed_addon:form_light_latex_wolf");

        if (!player.hasEffect(MobEffects.REGENERATION)) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1, false, false));
        }

        if ((player.hasEffect(MobEffects.REGENERATION) ? player.getEffect(MobEffects.REGENERATION).getAmplifier() : 0) >= 3)
            return;

        final Vec3 center = player.position();
        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(1 / 2d), e -> e != player && !e.hasEffect(MobEffects.REGENERATION));
        for (LivingEntity entity : list) {
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 3, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 270, 1, false, false));
        }
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            final Supplier<HumanoidModel<?>> MODEL = Suppliers.memoize(() ->
                    new HumanoidModel<>(new ModelPart(Collections.emptyList(), Map.of("head", new ModelNewHyperFlower<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelNewHyperFlower.LAYER_LOCATION)).HyperFlowerModel, "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_arm",
                            new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_leg",
                            new ModelPart(Collections.emptyList(), Collections.emptyMap())))));

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
                HumanoidModel<?> armorModel = MODEL.get();
                armorModel.crouching = living.isShiftKeyDown();
                armorModel.riding = defaultModel.riding;
                armorModel.young = living.isBaby();
                return armorModel;
            }
        });
    }
}
