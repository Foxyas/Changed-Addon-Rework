package net.foxyas.changedaddon.item;

import com.google.common.base.Suppliers;
import net.foxyas.changedaddon.client.model.ModelAccessories;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AccessoriesItem extends ArmorItem {

    public AccessoriesItem(ArmorItem.Type type, Item.Properties properties) {
        super(new ArmorMaterial() {

            final int[] durability = new int[]{13, 15, 16, 11};

            @Override
            public int getDurabilityForType(@NotNull Type pType) {
                return durability[pType.getSlot().getIndex()] * 45;
            }

            @Override
            public int getDefenseForType(@NotNull Type pType) {
                return pType.getSlot() == EquipmentSlot.CHEST ? 2 : 0;
            }

            @Override
            public int getEnchantmentValue() {
                return 35;
            }

            @Override
            public @NotNull SoundEvent getEquipSound() {
                return ChangedAddonSoundEvents.ARMOR_EQUIP.get();
            }

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of(new ItemStack(ChangedAddonItems.PAINITE.get()));
            }

            @Override
            public @NotNull String getName() {
                return "accessories";
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

    public static class Chestplate extends AccessoriesItem {

        public Chestplate() {
            super(Type.CHESTPLATE, new Item.Properties() //.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                    .fireResistant());
        }

        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {

                final Supplier<HumanoidModel<?>> MODEL = Suppliers.memoize(() -> {
                    ModelAccessories<?> ma = new ModelAccessories<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelAccessories.LAYER_LOCATION));
                    return new HumanoidModel<>(new ModelPart(Collections.emptyList(), Map.of("body", ma.Colar, "left_arm",
                            ma.LeftArmBracelet, "right_arm",
                            ma.RightArmBracelet, "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat",
                            new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
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

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "changed_addon:textures/entities/painite.png";
        }
    }
}
