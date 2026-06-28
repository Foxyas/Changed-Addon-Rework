package net.foxyas.changedaddon.client.renderer.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.model.clothes.HazardBodySuitLayers;
import net.foxyas.changedaddon.client.model.clothes.LatexHumanHazardBodySuitModel;
import net.foxyas.changedaddon.client.model.clothes.PlayerModelVisibilityModifier;
import net.foxyas.changedaddon.item.armor.HazardBodySuit;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.accessory.AccessoryRenderer;
import net.ltxprogrammer.changed.client.renderer.accessory.TransitionalAccessory;
import net.ltxprogrammer.changed.client.renderer.layers.LatexHumanoidArmorLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.LatexHumanModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorHumanModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel;
import net.ltxprogrammer.changed.data.AccessorySlotContext;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.LatexHuman;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.extension.ChangedCompatibility;
import net.ltxprogrammer.changed.init.ChangedAccessorySlots;
import net.ltxprogrammer.changed.item.Clothing;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("ALL")
public class HazardBodySuitClothingRenderer implements AccessoryRenderer, TransitionalAccessory, PlayerModelVisibilityModifier {
    protected final HumanoidModel clothingModel;
    protected final Set<ModelComponent> components;
    protected HumanoidModel playerClothingModel;

    public HazardBodySuitClothingRenderer(ArmorModel humanoid, Set<ModelComponent> components) {
        this.components = components;
        this.clothingModel = new HumanoidModel(Minecraft.getInstance().getEntityModels().bakeLayer(ArmorHumanModel.MODEL_SET.getModelName(humanoid)));
    }

    public static Supplier<AccessoryRenderer> of(ArmorModel armorModel, EquipmentSlot renderAs) {
        return () -> new HazardBodySuitClothingRenderer(armorModel, Set.of(new ModelComponent(armorModel, renderAs)));
    }

    public static Supplier<AccessoryRenderer> of(ArmorModel humanoidModel, Set<ModelComponent> components) {
        return () -> new HazardBodySuitClothingRenderer(humanoidModel, components);
    }

    public static boolean shouldHideHat(LivingEntity entity) {
        if (entity instanceof Player player) {
            if (ProcessTransfur.isPlayerTransfurred(player)
                    && !ChangedAddonTransfurVariants.getHumanForms().contains(ProcessTransfur.getPlayerTransfurVariant(player).getParent()))
                return false;

            if (AccessorySlots.getForEntity(player).isPresent()) {
                AccessorySlots accessorySlots = AccessorySlots.getForEntity(player).get();
                Optional<ItemStack> item = accessorySlots.getItem(ChangedAccessorySlots.FULL_BODY.get());
                if (item.isPresent()) {
                    ItemStack stack = item.get();
                    if (stack.getItem() instanceof HazardBodySuit hazardBodySuit) {
                        return hazardBodySuit.getClothingState(stack).getValue(HazardBodySuit.HELMET);
                    }
                }
            }
        }


        return false;
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

    private <T extends LivingEntity> HumanoidModel<?> getPlayerModel(T entity) {
        ModelLayerLocation layer = HazardBodySuitLayers.PLAYER;
        boolean slim = false;

        if (entity instanceof AbstractClientPlayer player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant != null && transfurVariant.isTransfurring()) {
                return clothingModel;
            } else if (transfurVariant != null && !transfurVariant.isTransfurring() && ChangedAddonTransfurVariants.getHumanForms().contains(transfurVariant.getParent())) {
                // Verifica se o jogador está usando o skin tipo "slim" (Alex)
                layer = LatexHumanHazardBodySuitModel.LATEX_PLAYER;
                slim = player.getModelName().equals("slim");
                if (slim) layer = LatexHumanHazardBodySuitModel.LATEX_PLAYER;
                return new LatexHumanHazardBodySuitModel(Minecraft.getInstance().getEntityModels().bakeLayer(layer));
            }

            // Verifica se o jogador está usando o skin tipo "slim" (Alex)
            slim = player.getModelName().equals("slim");
            if (slim) layer = HazardBodySuitLayers.PLAYER_SLIM;

        } else if (entity instanceof LatexHuman latexHuman) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(EntityUtil.playerOrNull(latexHuman.maybeGetUnderlying()));
            if (transfurVariant != null && transfurVariant.isTransfurring()) {
                return clothingModel;
            } else {
                // Verifica se o jogador está usando o skin tipo "slim" (Alex)
                layer = LatexHumanHazardBodySuitModel.LATEX_PLAYER;
                slim = latexHuman.getModelName().equals("slim");
                if (slim) layer = LatexHumanHazardBodySuitModel.LATEX_PLAYER;
                return new LatexHumanHazardBodySuitModel(Minecraft.getInstance().getEntityModels().bakeLayer(layer));
            }
        }

        return new PlayerModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(layer), slim);
    }

    /// For First Person is Recommended to only use the Default player models.
    private <T extends LivingEntity> HumanoidModel<?> getPlayerModelForFirstPerson(T entity) {
        ModelLayerLocation layer = HazardBodySuitLayers.PLAYER;
        boolean slim = false;

        if (entity instanceof AbstractClientPlayer player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant != null && transfurVariant.isTransfurring()) {
                return clothingModel;
            }

            // Verifica se o jogador está usando o skin tipo "slim" (Alex)
            slim = player.getModelName().equals("slim");
            if (slim) layer = HazardBodySuitLayers.PLAYER_SLIM;
        }

        return new PlayerModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(layer), slim);
    }

    // Util Method.
    public void copyPlayerModelProperties(PlayerModel<?> copyPlayerModel) {
        if (this.playerClothingModel instanceof PlayerModel<?> playerModel) {
            playerModel.hat.visible = copyPlayerModel.hat.visible;
            playerModel.jacket.visible = copyPlayerModel.jacket.visible;
            playerModel.leftPants.visible = copyPlayerModel.leftPants.visible;
            playerModel.rightPants.visible = copyPlayerModel.rightPants.visible;
            playerModel.leftSleeve.visible = copyPlayerModel.leftSleeve.visible;
            playerModel.rightSleeve.visible = copyPlayerModel.rightSleeve.visible;
        }
    }

    public void copyPlayerModelProperties(PlayerModel<?> target, PlayerModel<?> data) {
        target.hat.visible = data.hat.visible;
        target.jacket.visible = data.jacket.visible;
        target.leftPants.visible = data.leftPants.visible;
        target.rightPants.visible = data.rightPants.visible;
        target.leftSleeve.visible = data.leftSleeve.visible;
        target.rightSleeve.visible = data.rightSleeve.visible;
    }

    public void setHelmetFirstPersonVisibility(ModelPart helmet) {
        helmet.visible = !ChangedCompatibility.isFirstPersonRendering();
    }

    public <T extends PlayerModel<?>> void setHelmetFirstPersonVisibility(T clothingModel) {
        clothingModel.getHead().visible = !ChangedCompatibility.isFirstPersonRendering();
        clothingModel.hat.visible = !ChangedCompatibility.isFirstPersonRendering();

        if (clothingModel instanceof LatexHumanHazardBodySuitModel latexHumanHazardBodySuitModel) {
            latexHumanHazardBodySuitModel.getHat().visible = !ChangedCompatibility.isFirstPersonRendering();
        }
    }

    @SuppressWarnings({"unchecked"})
    public <T extends LivingEntity, M extends EntityModel<T>> void render(AccessorySlotContext<T> slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = slotContext.stack();
        Item var14 = stack.getItem();
        if (var14 instanceof Clothing clothing) {
            Color color = Color.WHITE;
            if (clothing instanceof DyeableLeatherItem dyeableLeatherItem) {
                color = new Color(dyeableLeatherItem.getColor(stack));
            }

            T entity = slotContext.wearer();
            ResourceLocation texture = clothing.getTexture(stack, entity);
            if (texture == null) {
                return;
            }

            if (mayRenderTransfuringModel(poseStack, renderLayerParent, renderTypeBuffer, light, entity, texture, stack, color))
                return;

            if (mayRenderNonHumanTransfurModel(poseStack, renderLayerParent, renderTypeBuffer, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, entity, stack, texture, color))
                return;

            if (mayRenderHumanTransfurModel(poseStack, renderLayerParent, renderTypeBuffer, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, entity, texture, stack, color))
                return;

            renderBasicPlayerModel(poseStack, renderLayerParent, renderTypeBuffer, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, entity, texture, stack, color);

        }

    }

    private <T extends LivingEntity, M extends EntityModel<T>> void renderBasicPlayerModel(PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, T entity, ResourceLocation texture, ItemStack stack, Color color) {
        EntityModel layer = renderLayerParent.getModel();
        if (layer instanceof HumanoidModel<?> baseModel) {
            this.playerClothingModel = getPlayerModel(entity);
            if (playerClothingModel == null) return;
            if (playerClothingModel instanceof LatexHumanHazardBodySuitModel) return;
            if (playerClothingModel instanceof PlayerModel playerModel && entity instanceof AbstractClientPlayer player) {
                baseModel.copyPropertiesTo(playerModel);
                playerModel.prepareMobModel(player, limbSwing, limbSwingAmount, partialTicks);
                playerModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

                this.setHelmetFirstPersonVisibility(playerModel.getHead());
                this.setHelmetFirstPersonVisibility(playerModel.hat);
                playerModel.renderToBuffer(poseStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, OverlayTexture.NO_OVERLAY, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
            }
            return;
        }
    }

    private <T extends LivingEntity, M extends EntityModel<T>> boolean mayRenderHumanTransfurModel(PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, T entity, ResourceLocation texture, ItemStack stack, Color color) {
        if (entity instanceof LatexHuman latexHuman && renderLayerParent instanceof AdvancedHumanoidRenderer advancedHumanoidRenderer) {
            AdvancedHumanoidModel baseModel = advancedHumanoidRenderer.getModel(latexHuman);
            if (baseModel instanceof LatexHumanModel latexHumanModel) {
                this.playerClothingModel = getPlayerModel(latexHuman.maybeGetUnderlying());
                if (this.playerClothingModel == null) return true;
                if (playerClothingModel instanceof LatexHumanHazardBodySuitModel latexHumanHazardBodySuitModel) {
                    latexHumanHazardBodySuitModel.defaultModelProperties();
                    latexHumanModel.copyPropertiesTo(latexHumanHazardBodySuitModel);
                    latexHumanHazardBodySuitModel.prepareMobModel(latexHuman, limbSwing, limbSwingAmount, partialTicks);
                    latexHumanHazardBodySuitModel.setupAnim(latexHuman, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

                    this.setHelmetFirstPersonVisibility(latexHumanHazardBodySuitModel.getHead());
                    this.setHelmetFirstPersonVisibility(latexHumanHazardBodySuitModel.getHat());
                    latexHumanHazardBodySuitModel.renderToBuffer(poseStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, OverlayTexture.NO_OVERLAY, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
                    return true;
                }
            }
        }
        return false;
    }

    private <T extends LivingEntity, M extends EntityModel<T>> boolean mayRenderNonHumanTransfurModel(PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, T entity, ItemStack stack, ResourceLocation texture, Color color) {
        if (entity instanceof ChangedEntity changedEntity && !(changedEntity instanceof LatexHuman)) {
            if (renderLayerParent instanceof AdvancedHumanoidRenderer advancedHumanoidRenderer) {
                LatexHumanoidArmorLayer layer = advancedHumanoidRenderer.getArmorLayer();

                for (ModelComponent component : this.components) {
                    LatexHumanoidArmorModel model = (LatexHumanoidArmorModel) layer.modelPicker.getModelSetForSlot(changedEntity, component.renderAs).get(component.armorModel);
                    AdvancedHumanoidModel other = advancedHumanoidRenderer.getModel(changedEntity);
                    model.getAnimator(changedEntity).copyProperties(other.getAnimator(changedEntity));
                    model.prepareMobModel(changedEntity, limbSwing, limbSwingAmount, partialTicks);
                    model.setupAnim(changedEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                    model.prepareVisibility(component.renderAs, stack);
                    model.renderForSlot(changedEntity, advancedHumanoidRenderer, stack, component.renderAs, poseStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, OverlayTexture.NO_OVERLAY, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
                }

                return true;
            }
        }
        return false;
    }

    private <T extends LivingEntity, M extends EntityModel<T>> boolean mayRenderNonHumanTransfurModelFullBody(PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, T entity, ItemStack stack, ResourceLocation texture, Color color) {
        if (entity instanceof ChangedEntity changedEntity && !(changedEntity instanceof LatexHuman)) {
            if (renderLayerParent instanceof AdvancedHumanoidRenderer advancedHumanoidRenderer) {
                LatexHumanoidArmorLayer layer = advancedHumanoidRenderer.getArmorLayer();

                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot.getType() != EquipmentSlot.Type.ARMOR) {
                        continue;
                    }

                    LatexHumanoidArmorModel model = (LatexHumanoidArmorModel) layer.modelPicker.getModelSetForSlot(changedEntity, slot).get(ArmorModel.CLOTHING_INNER);
                    AdvancedHumanoidModel other = advancedHumanoidRenderer.getModel(changedEntity);
                    model.getAnimator(changedEntity).copyProperties(other.getAnimator(changedEntity));
                    model.prepareMobModel(changedEntity, limbSwing, limbSwingAmount, partialTicks);
                    model.setupAnim(changedEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                    model.prepareVisibility(slot, stack);
                    model.renderForSlot(changedEntity, advancedHumanoidRenderer, stack, slot, poseStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, OverlayTexture.NO_OVERLAY, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
                }

                return true;
            }
        }
        return false;
    }

    private <T extends LivingEntity, M extends EntityModel<T>> boolean mayRenderTransfuringModel(PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, T entity, ResourceLocation texture, ItemStack stack, Color color) {
        if (entity instanceof AbstractClientPlayer player) {
            TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariantInstance != null && transfurVariantInstance.isTransfurring()) {
                EntityModel layer = renderLayerParent.getModel();
                if (layer instanceof HumanoidModel<?> baseModel) {
                    baseModel.copyPropertiesTo(this.clothingModel);
                    this.clothingModel.renderToBuffer(poseStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, OverlayTexture.NO_OVERLAY, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void renderFirstPersonOnArms(AccessorySlotContext<T> slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, HumanoidArm arm, PartPose armPose, float partialTicks) {
        ItemStack stack = slotContext.stack();
        Item var11 = stack.getItem();
        if (var11 instanceof Clothing clothing) {
            Color color = Color.WHITE;
            if (clothing instanceof DyeableLeatherItem dyeableLeatherItem) {
                color = new Color(dyeableLeatherItem.getColor(stack));
            }
            T entity = slotContext.wearer();
            ResourceLocation texture = clothing.getTexture(stack, entity);
            if (texture == null) {
                return;
            }

            if (mayRenderTransfurredArmModel(poseStack, renderLayerParent, renderTypeBuffer, light, arm, armPose, partialTicks, entity, texture, stack, color))
                return;

            renderNonTransfurredArmModel(poseStack, renderLayerParent, renderTypeBuffer, light, arm, armPose, entity, texture, stack, color);
        }

    }

    private <T extends LivingEntity, M extends EntityModel<T>> void renderNonTransfurredArmModel(PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, HumanoidArm arm, PartPose armPose, T entity, ResourceLocation texture, ItemStack stack, Color color) {
        EntityModel layer = renderLayerParent.getModel();
        if (layer instanceof HumanoidModel<?> baseModel) {
            this.playerClothingModel = getPlayerModelForFirstPerson(entity);
            if (this.playerClothingModel == null) return;
            baseModel.copyPropertiesTo(this.playerClothingModel);
            ModelPart armPart = arm == HumanoidArm.RIGHT ? this.playerClothingModel.rightArm : this.playerClothingModel.leftArm;
            armPart.loadPose(armPose);
            FormRenderHandler.renderVanillaModelPartWithTexture(armPart, poseStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
            if (this.playerClothingModel instanceof PlayerModel<?> playerModel) {
                ModelPart sleevePart = arm == HumanoidArm.RIGHT ? playerModel.rightSleeve : playerModel.leftSleeve;
                sleevePart.loadPose(armPose);
                FormRenderHandler.renderVanillaModelPartWithTexture(sleevePart, poseStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
            }
        }
    }

    private <T extends LivingEntity, M extends EntityModel<T>> boolean mayRenderTransfurredArmModel(PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, HumanoidArm arm, PartPose armPose, float partialTicks, T entity, ResourceLocation texture, ItemStack stack, Color color) {
        if (entity instanceof ChangedEntity changedEntity) {
            if (renderLayerParent instanceof AdvancedHumanoidRenderer advancedHumanoidRenderer) {
                LatexHumanoidArmorLayer layer = advancedHumanoidRenderer.getArmorLayer();
                EntityModel baseModel = advancedHumanoidRenderer.getModel(changedEntity);

                if (entity instanceof LatexHuman latexHuman && latexHuman.maybeGetUnderlying() instanceof AbstractClientPlayer player) {
                    if (baseModel instanceof LatexHumanModel playerModel) {
                        this.playerClothingModel = getPlayerModel(player);
                        if (this.playerClothingModel == null) return true;
                        if (this.playerClothingModel instanceof AdvancedHumanoidModel advancedHumanoidModel) {
                            playerModel.copyPropertiesTo(advancedHumanoidModel);

                            ModelPart armPart = advancedHumanoidModel.getArm(arm);
                            armPart.loadPose(armPose);
                            FormRenderHandler.renderVanillaModelPartWithTexture(armPart, poseStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);

                            if (advancedHumanoidModel instanceof LatexHumanHazardBodySuitModel latexHumanHazardBodySuitModel) {
                                ModelPart sleevePart = latexHumanHazardBodySuitModel.getSleeve(arm);
                                sleevePart.loadPose(armPose);
                                FormRenderHandler.renderVanillaModelPartWithTexture(sleevePart, poseStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
                            }

                        } else if (this.playerClothingModel instanceof PlayerModel) {
                            baseModel.copyPropertiesTo(this.playerClothingModel);

                            ModelPart armPart = arm == HumanoidArm.RIGHT ? this.playerClothingModel.rightArm : this.playerClothingModel.leftArm;
                            armPart.loadPose(armPose);
                            FormRenderHandler.renderVanillaModelPartWithTexture(armPart, poseStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);

                            ModelPart sleevePart = arm == HumanoidArm.RIGHT ? playerModel.rightSleeve : playerModel.leftSleeve;
                            FormRenderHandler.renderVanillaModelPartWithTexture(sleevePart, poseStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
                        }
                        return true;
                    }
                }

                for (ModelComponent component : this.components) {
                    if (component.renderAs == EquipmentSlot.CHEST) {
                        LatexHumanoidArmorModel model = (LatexHumanoidArmorModel) layer.modelPicker.getModelSetForSlot(changedEntity, component.renderAs).get(component.armorModel);
                        model.prepareMobModel(changedEntity, 0.0F, 0.0F, partialTicks);
                        model.prepareVisibility(component.renderAs, stack);
                        ModelPart armPart = model.getArm(arm);
                        armPart.loadPose(armPose);
                        FormRenderHandler.renderModelPartWithTexture(model.getArm(arm), poseStack, ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(texture), false, stack.hasFoil()), light, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
                    }
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public void setPlayerModelProperties(AbstractClientPlayer clientPlayer, PlayerModel<AbstractClientPlayer> playerModel) {
        if (!ProcessTransfur.isPlayerTransfurred(clientPlayer)) {
            if (!clientPlayer.isSpectator()) {
                playerModel.setAllVisible(true);
                playerModel.hat.visible = !shouldHideHat(clientPlayer);
                playerModel.jacket.visible = false;
                playerModel.leftPants.visible = false;
                playerModel.rightPants.visible = false;
                playerModel.leftSleeve.visible = false;
                playerModel.rightSleeve.visible = false;
            }
        } else if (ProcessTransfur.isPlayerTransfurred(clientPlayer)) {
            if (ChangedAddonTransfurVariants.getHumanForms().contains(ProcessTransfur.getPlayerTransfurVariant(clientPlayer).getParent())) {
                if (!clientPlayer.isSpectator()) {
                    playerModel.setAllVisible(true);
                    playerModel.hat.visible = !shouldHideHat(clientPlayer);
                    playerModel.jacket.visible = false;
                    playerModel.leftPants.visible = false;
                    playerModel.rightPants.visible = false;
                    playerModel.leftSleeve.visible = false;
                    playerModel.rightSleeve.visible = false;
                }
            }
        }
    }

    @Override
    public boolean isVisible() {
        return PlayerModelVisibilityModifier.super.isVisible();
    }

    public static record ModelComponent(ArmorModel armorModel, EquipmentSlot renderAs) {
    }
}
