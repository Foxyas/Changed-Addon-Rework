package net.foxyas.changedaddon.item.armor;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.foxyas.changedaddon.client.model.armors.DarkLatexCoatModel;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.util.ArmorPartMapper;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DarkLatexCoatItem extends ArmorItem {

    public DarkLatexCoatItem(ArmorItem.Type type, Properties properties) {
        super(new ArmorMaterial() {

            @Override
            public int getDurabilityForType(@NotNull Type pType) {
                return 0;
            }

            @Override
            public int getDefenseForType(@NotNull Type pType) {
                return 0;
            }

            @Override
            public int getEnchantmentValue() {
                return 10;
            }

            @Override
            public @NotNull SoundEvent getEquipSound() {
                return ChangedAddonSoundEvents.ARMOR_EQUIP.get();
            }

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of(ChangedItems.DARK_LATEX_GOO.get(), ChangedItems.LATEX_BASE.get());
            }

            @Override
            public @NotNull String getName() {
                return "DarkLatexCoat";
            }

            @Override
            public float getToughness() {
                return 0f;
            }

            @Override
            public float getKnockbackResistance() {
                return 0f;
            }
        }, type, properties);
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        return ImmutableMultimap.of();
    }

    // Method para definir o modelo da armadura no lado do cliente
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            final Supplier<HumanoidModel<?>> MODEL = Suppliers.memoize(() -> {
                DarkLatexCoatModel<?> coat = new DarkLatexCoatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(DarkLatexCoatModel.LAYER_LOCATION));
                ArmorPartMapper armorPartMapper = new ArmorPartMapper()
                        .mapHead(coat.getPuroCoatHead())
                        .mapBody(coat.getPuroCoatBody())
                        .mapLeftArm(coat.getPuroCoatHead())
                        .mapRightArm(coat.getPuroCoatHead());
                return armorPartMapper.build();
//                        new HumanoidModel<>(new ModelPart(Collections.emptyList(), Map.of("head", coat.getPuroCoatHead(),
//                        "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
//                        "body", coat.getPuroCoatBody(),
//                        "left_arm", coat.getPuroCoatLeftArm(),
//                        "right_arm", coat.getPuroCoatRightArm(),
//                        "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
//                        "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
            });

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

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "changed_addon:textures/entities/dark_latex_coat.png";
    }
}
