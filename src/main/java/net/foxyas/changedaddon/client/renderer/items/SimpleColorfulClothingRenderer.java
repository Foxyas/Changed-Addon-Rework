package net.foxyas.changedaddon.client.renderer.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.accessory.AccessoryRenderer;
import net.ltxprogrammer.changed.client.renderer.accessory.TransitionalAccessory;
import net.ltxprogrammer.changed.client.renderer.layers.LatexHumanoidArmorLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorHumanModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel;
import net.ltxprogrammer.changed.data.AccessorySlotContext;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.item.Clothing;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("ALL")
public class SimpleColorfulClothingRenderer implements AccessoryRenderer, TransitionalAccessory {
    protected final HumanoidModel clothingModel;
    protected final Set<ModelComponent> components;

    public SimpleColorfulClothingRenderer(ArmorModel humanoid, Set<ModelComponent> components) {
        this.components = components;
        this.clothingModel = new HumanoidModel(Minecraft.getInstance().getEntityModels().bakeLayer(ArmorHumanModel.MODEL_SET.getModelName(humanoid)));
    }

    public static Supplier<AccessoryRenderer> of(ArmorModel armorModel, EquipmentSlot renderAs) {
        return () -> new SimpleColorfulClothingRenderer(armorModel, Set.of(new ModelComponent(armorModel, renderAs)));
    }

    public static Supplier<AccessoryRenderer> of(ArmorModel humanoidModel, Set<ModelComponent> components) {
        return () -> new SimpleColorfulClothingRenderer(humanoidModel, components);
    }

    public Optional<HumanoidModel<?>> getBeforeModel(AccessorySlotContext<?> slotContext, RenderLayerParent<?, ?> renderLayerParent) {
        return Optional.of(this.clothingModel);
    }

    public Stream<AdvancedHumanoidModel<?>> getAfterModels(AccessorySlotContext<?> slotContext, RenderLayerParent<?, ?> renderLayerParent) {
        if (renderLayerParent instanceof AdvancedHumanoidRenderer advancedHumanoidRenderer) {
            LivingEntity var5 = EntityUtil.maybeGetOverlaying(slotContext.wearer());
            if (var5 instanceof ChangedEntity wearer) {
                LatexHumanoidArmorLayer layer = advancedHumanoidRenderer.getArmorLayer();
                return this.components.stream().map((component) -> Optional.of((LatexHumanoidArmorModel) layer.modelPicker.getModelSetForSlot(wearer, component.renderAs).get(component.armorModel))).filter(Optional::isPresent).map(Optional::get);
            }
        }

        return Stream.empty();
    }

    public Optional<ResourceLocation> getModelTexture(AccessorySlotContext<?> slotContext) {
        Item var3 = slotContext.stack().getItem();
        if (var3 instanceof Clothing clothing) {
            return Optional.ofNullable(clothing.getTexture(slotContext.stack(), slotContext.wearer()));
        } else {
            return Optional.empty();
        }
    }

    @SuppressWarnings({"unchecked"})
    public <T extends LivingEntity, M extends EntityModel<T>> void render(AccessorySlotContext<T> slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = slotContext.stack();
        Item var14 = stack.getItem();
        if (var14 instanceof Clothing clothing) {
            if (clothing instanceof DyeableLeatherItem dyeableLeatherItem) {
                Color color = new Color(dyeableLeatherItem.getColor(stack));
                T entity = slotContext.wearer();
                ResourceLocation texture = clothing.getTexture(stack, entity);
                if (texture == null) {
                    return;
                }

                if (entity instanceof ChangedEntity changedEntity) {
                    if (renderLayerParent instanceof AdvancedHumanoidRenderer advancedHumanoidRenderer) {
                        LatexHumanoidArmorLayer layer = advancedHumanoidRenderer.getArmorLayer();

                        for (ModelComponent component : this.components) {
                            LatexHumanoidArmorModel model = (LatexHumanoidArmorModel) layer.modelPicker.getModelSetForSlot(changedEntity, component.renderAs).get(component.armorModel);
                            AdvancedHumanoidModel var24 = advancedHumanoidRenderer.getModel(changedEntity);
                            if (var24 instanceof AdvancedHumanoidModelInterface advancedModel) {
                                model.getAnimator(changedEntity).copyProperties(advancedModel.getAnimator(changedEntity));
                            }

                            model.prepareMobModel(changedEntity, limbSwing, limbSwingAmount, partialTicks);
                            model.setupAnim(changedEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                            model.prepareVisibility(component.renderAs, stack);
                            model.renderForSlot(changedEntity, advancedHumanoidRenderer, stack, component.renderAs, matrixStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, OverlayTexture.NO_OVERLAY, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
                            model.unprepareVisibility(component.renderAs, stack);
                        }

                        return;
                    }
                }

                EntityModel layer = renderLayerParent.getModel();
                if (layer instanceof HumanoidModel<?> baseModel) {
                    baseModel.copyPropertiesTo(this.clothingModel);
                    this.clothingModel.renderToBuffer(matrixStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, OverlayTexture.NO_OVERLAY, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
                }
            }
        }

    }

    @SuppressWarnings("unchecked")
    public <T extends LivingEntity, M extends EntityModel<T>> void renderFirstPersonOnArms(AccessorySlotContext<T> slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, HumanoidArm arm, PoseStack stackCorrector, float partialTicks) {
        ItemStack stack = slotContext.stack();
        Item var11 = stack.getItem();
        if (var11 instanceof Clothing clothing) {
            T entity = slotContext.wearer();
            ResourceLocation texture = clothing.getTexture(stack, entity);
            if (texture == null) {
                return;
            }

            if (entity instanceof ChangedEntity changedEntity) {
                if (renderLayerParent instanceof AdvancedHumanoidRenderer advancedHumanoidRenderer) {
                    LatexHumanoidArmorLayer layer = advancedHumanoidRenderer.getArmorLayer();

                    for (ModelComponent component : this.components) {
                        if (component.renderAs == EquipmentSlot.CHEST) {
                            LatexHumanoidArmorModel model = (LatexHumanoidArmorModel) layer.modelPicker.getModelSetForSlot(changedEntity, component.renderAs).get(component.armorModel);
                            model.prepareMobModel(changedEntity, 0.0F, 0.0F, partialTicks);
                            model.setupAnim(changedEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                            model.setupHand(changedEntity);
                            model.prepareVisibility(component.renderAs, stack);
                            FormRenderHandler.renderModelPartWithTexture(model.getArm(arm), stackCorrector, matrixStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, 1.0F);
                            model.unprepareVisibility(component.renderAs, stack);
                        }
                    }

                    return;
                }
            }

            EntityModel layer = renderLayerParent.getModel();
            if (layer instanceof HumanoidModel<?> baseModel) {
                baseModel.copyPropertiesTo(this.clothingModel);
                FormRenderHandler.renderVanillaModelPartWithTexture(arm == HumanoidArm.RIGHT ? this.clothingModel.rightArm : this.clothingModel.leftArm, stackCorrector, matrixStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, 1.0F);
            }
        }

    }

    public static record ModelComponent(ArmorModel armorModel, EquipmentSlot renderAs) {
    }
}