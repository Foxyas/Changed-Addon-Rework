package net.foxyas.changedaddon.item;

import com.google.common.collect.Multimap;
import net.foxyas.changedaddon.client.model.armors.DarkLatexCoatModel;
import net.foxyas.changedaddon.entity.Experiment10BossEntity;
import net.foxyas.changedaddon.entity.Experiment10Entity;
import net.foxyas.changedaddon.entity.KetExperiment009BossEntity;
import net.foxyas.changedaddon.entity.KetExperiment009Entity;
import net.foxyas.changedaddon.registers.ChangedAddonEntitys;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DarkLatexCoatItem extends ArmorItem {
    public DarkLatexCoatItem(EquipmentSlot slot, Properties properties) {
        super(new ArmorMaterial() {
            @Override
            public int getDurabilityForSlot(@NotNull EquipmentSlot p_40410_) {
                return 0;
            }

            @Override
            public int getDefenseForSlot(@NotNull EquipmentSlot slot) {
                return 0;
            }

            @Override
            public int getEnchantmentValue() {
                return 10;
            }

            @Override
            public @NotNull SoundEvent getEquipSound() {
                return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:armor_equip")) != null ? ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:armor_equip")) : ChangedSounds.EQUIP1;
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
        }, slot, properties);

    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot p_40390_) {
        return super.getDefaultAttributeModifiers(p_40390_);
    }

    @Override
    public int getDefense() {
        return 0; // Nenhuma proteção de armadura
    }

    @Override
    public float getToughness() {
        return 0f; // Nenhuma resistência
    }

    @Nullable
    @Override
    public SoundEvent getEquipSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:armor_equip"));
    }

    // Método para definir o modelo da armadura no lado do cliente
    public void initializeClient(java.util.function.@NotNull Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
        consumer.accept(new net.minecraftforge.client.IItemRenderProperties() {
            @Override
            @OnlyIn(Dist.CLIENT)
            public HumanoidModel<?> getArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
                // Criar o modelo de armadura com base na classe DarkLatexCoat
                HumanoidModel<?> armorModel = new HumanoidModel<>(new ModelPart(Collections.emptyList(), Map.of("head", new DarkLatexCoatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(DarkLatexCoatModel.LAYER_LOCATION)).getPuroCoatHead(),  // Para a parte da cabeça
                        "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new DarkLatexCoatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(DarkLatexCoatModel.LAYER_LOCATION)).getPuroCoatBody(), "left_arm", new DarkLatexCoatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(DarkLatexCoatModel.LAYER_LOCATION)).getPuroCoatLeftArm(), "right_arm", new DarkLatexCoatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(DarkLatexCoatModel.LAYER_LOCATION)).getPuroCoatRightArm(), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));

                // Ajustar os estados do modelo (agachado, montado, jovem)
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
        return "changed_addon:textures/entities/darklatexcoat.png";
    }

    public static class HeadPart extends DarkLatexCoatItem {
        public HeadPart(EquipmentSlot slot, Properties properties) {
            super(slot, properties);

        }

        @Override
        public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot p_40390_) {
            return super.getDefaultAttributeModifiers(p_40390_);
        }

        // Método para definir o modelo da armadura no lado do cliente
        public void initializeClient(java.util.function.@NotNull Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
            consumer.accept(new net.minecraftforge.client.IItemRenderProperties() {
                @Override
                @OnlyIn(Dist.CLIENT)
                public HumanoidModel<?> getArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
                    // Criar o modelo de armadura com base na classe DarkLatexCoat
                    HumanoidModel<?> armorModel = new HumanoidModel<>(new ModelPart(Collections.emptyList(), Map.of("head", new DarkLatexCoatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(DarkLatexCoatModel.LAYER_LOCATION)).getPuroCoatHead(),  // Para a parte da cabeça
                            "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new DarkLatexCoatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(DarkLatexCoatModel.LAYER_LOCATION)).getPuroCoatBody(), "left_arm", new DarkLatexCoatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(DarkLatexCoatModel.LAYER_LOCATION)).getPuroCoatLeftArm(), "right_arm", new DarkLatexCoatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(DarkLatexCoatModel.LAYER_LOCATION)).getPuroCoatRightArm(), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));

                    // Ajustar os estados do modelo (agachado, montado, jovem)
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
            return "changed_addon:textures/entities/darklatexcoat.png";
        }
    }
}
