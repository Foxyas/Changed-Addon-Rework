package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.entity.InformantBlockEntity;
import net.foxyas.changedaddon.client.renderer.blockEntitys.InformantBlockEntityRenderer;
import net.foxyas.changedaddon.menu.InformantGuiMenu;
import net.foxyas.changedaddon.network.packet.InformantBlockGuiKeyPacket;
import net.foxyas.changedaddon.util.TransfurVariantUtils;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InformantGuiScreen extends AbstractContainerScreen<InformantGuiMenu> {

    private static final ResourceLocation texture = ChangedAddonMod.textureLoc("textures/screens/informant_gui");
    public final EditBox form;
    private final Player player;
    private final List<String> allSuggestions = TransfurVariant.getPublicTransfurVariants()
            .map(v -> v.getEntityType().getDescription().getString())
            .toList();
    private final Map<String, List<TransfurVariant<?>>> nameToVariants = TransfurVariant.getPublicTransfurVariants()
            .collect(Collectors.groupingBy(
                    v -> v.getEntityType().getDescription().getString()
            ));
    private final List<String> filteredSuggestions = new ArrayList<>();
    private int suggestionIndex = -1;

    public InformantGuiScreen(InformantGuiMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.player = container.player;
        this.imageWidth = 176;
        this.imageHeight = 195;

        form = new EditBox(Minecraft.getInstance().font, this.leftPos + 44, this.topPos + 13, 90, 10,
                Component.translatable("gui.changed_addon.informant_gui.form"));
        form.setTextColor(new Color(0, 205, 255).getRGB());
        //form.setBordered(false);
        //form.setAlpha(0.25f);
        form.setMaxLength(128);

        form.setResponder(text -> {
            InformantBlockEntity blockEntity = menu.blockEntity;

            if (blockEntity.getText().equals(form.getValue())) return;

            updateSuggestions(text);

            List<TransfurVariant<?>> variants = nameToVariants.get(text);
            TransfurVariant<?> variant = null;
            if (variants != null && !variants.isEmpty()) {
                variant = variants.get(0);
            }

            ChangedAddonMod.PACKET_HANDLER.sendToServer(new InformantBlockGuiKeyPacket(text, variant, menu.blockEntity.getBlockPos()));
            blockEntity.updateInternal(text, variant);
        }); // sempre que o valor mudar, atualiza sugestões

        form.setValue(menu.blockEntity.getText());
        updateSuggestions(form.getValue());
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        form.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(pGuiGraphics, mouseX, mouseY);

        if (mouseX > leftPos + 25 && mouseX < leftPos + 34 && mouseY > topPos + 4 && mouseY < topPos + 13) {
            pGuiGraphics.renderTooltip(font, Component.translatable("gui.changed_addon.informant_gui.tooltip_type_the_form"), mouseX, mouseY);
        }

        if (menu.getStackInSlot().isEmpty()) {
            if (mouseX > leftPos + 151 && mouseX < leftPos + 168 && mouseY > topPos + 88 && mouseY < topPos + 105) {
                pGuiGraphics.renderTooltip(font, Component.translatable("gui.changed_addon.informant_gui.tooltip_put_a_syringe_with_a_form"), mouseX, mouseY);
            }
        }

        if (!filteredSuggestions.isEmpty() && form.isFocused()) {
            int x = form.getX();
            int y = form.getY() + form.getHeight() + 2;
            int width = form.getWidth();
            int height = 12;

            for (int i = 0; i < filteredSuggestions.size(); i++) {
                String suggestion = filteredSuggestions.get(i);
                Color color = new Color(0x8840BEFF, true);
                Color Bgcolor = new Color(0x881DAAEC, true);
                int bgColor = i == suggestionIndex ? Bgcolor.getRGB() : color.getRGB();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                pGuiGraphics.fill(x, y + i * height, x + width, y + (i + 1) * height, bgColor);
                pGuiGraphics.drawString(font, suggestion, x + 2, y + i * height + 2, 0xFFFFFF);
                RenderSystem.disableBlend();
            }
        }

        // Exemplo: posições relativas aos seus ícones no lado esquerdo
        int iconX = leftPos + 9;
        int iconSize = 16; // Tamanho de cada ícone (ajuste conforme necessário)

        int iconYHealth = topPos + 18;
        int iconYLandSpeed = topPos + 40;
        int iconYSwimSpeed = topPos + 62;
        int iconYJump = topPos + 84;

        // TODO Rewrite this to look more clean & render the entity in the middle of the GUI (spinning or looking at the mouse)

        InformantBlockEntity blockEntity = menu.blockEntity;
        TransfurVariant<?> tf = blockEntity.getDisplayTf();

        ChangedEntity entity = InformantBlockEntityRenderer.getDisplayEntity(tf);

        if (entity != null) {
            assert Minecraft.getInstance().player != null;
            entity.tickCount = Minecraft.getInstance().player.tickCount;

            int centerX = leftPos + imageWidth / 2;
            int centerY = topPos + imageHeight / 2;
            InventoryScreen.renderEntityInInventoryFollowsMouse(pGuiGraphics, centerX, centerY, 30, centerX - mouseX, centerY - (26 + 26) - mouseY, entity);
        }
        TransfurVariantInstance<?> instance = TransfurVariantInstance.variantFor(tf, player);

        float hp = TransfurVariantUtils.getExtraHpOfVariantBasedOnPlayer(tf, player);
        float swimSpeed = TransfurVariantUtils.getSwimSpeedOfVariantBasedOnPlayer(tf, player);
        float landSpeed = TransfurVariantUtils.getLandSpeedOfVariantBasedOnPlayer(tf, player);
        float jumpStrength = TransfurVariantUtils.GetJumpStrength(tf, player);
        boolean canFlyOrGlide = TransfurVariantUtils.canVariantGlide(instance);
        String miningStrength = TransfurVariantUtils.getMiningStrengthOfVariant(tf, player);
        float extraHp = hp / 2f;
        float landSpeedPct = landSpeed == 0 ? 0 : (landSpeed - 1) * 100;
        float swimSpeedPct = swimSpeed == 0 ? 0 : (swimSpeed - 1) * 100;
        float jumpStrengthPct = jumpStrength == 0 ? 0 : (jumpStrength - 1) * 100;

        MutableComponent landSpeedInfo = Component.translatable("text.changed_addon.land_speed",
                landSpeedPct == 0
                        ? Component.literal("§7None§r")
                        : Component.literal((landSpeedPct > 0 ? "§a+" : "§c") + (int) landSpeedPct + "%"));

        MutableComponent swimSpeedInfo = Component.translatable("text.changed_addon.swim_speed",
                swimSpeedPct == 0
                        ? Component.literal("§7None§r")
                        : Component.literal((swimSpeedPct > 0 ? "§a+" : "§c") + (int) swimSpeedPct + "%"));

        MutableComponent additionalHealthInfo = Component.translatable("text.changed_addon.additionalHealth",
                        extraHp == 0
                                ? Component.literal("§7None§r")
                                : Component.literal((extraHp > 0 ? "§a+" : "§c") + extraHp + "§r"))
                .append(Component.translatable("text.changed_addon.additionalHealth.Hearts"));

        Component miningStrengthInfo = Component.translatable("text.changed_addon.miningStrength", miningStrength);

        MutableComponent jumpStrengthInfo = Component.translatable("text.changed_addon.jumpStrength",
                jumpStrengthPct == 0
                        ? Component.literal("§7None§r")
                        : Component.literal((jumpStrengthPct > 0 ? "§a+" : "§c") + (int) jumpStrengthPct + "%"));

        MutableComponent canGlideInfo = Component.translatable("text.changed_addon.canGlide/Fly")
                .append("")
                .append(canFlyOrGlide
                        ? Component.literal("§aTrue§r")
                        : Component.literal("§cFalse§r"));


        // Verifica se o mouse está sobre cada ícone e exibe a tooltip correspondente
        if (mouseX > iconX && mouseX < iconX + iconSize) {
            if (mouseY > iconYHealth && mouseY < iconYHealth + iconSize) {
                pGuiGraphics.renderTooltip(font, additionalHealthInfo, mouseX, mouseY);
            } else if (mouseY > iconYLandSpeed && mouseY < iconYLandSpeed + iconSize) {
                pGuiGraphics.renderTooltip(font, landSpeedInfo, mouseX, mouseY);
            } else if (mouseY > iconYSwimSpeed && mouseY < iconYSwimSpeed + iconSize) {
                pGuiGraphics.renderTooltip(font, List.of(swimSpeedInfo), Optional.empty(), mouseX, mouseY);
            } else if (mouseY > iconYJump && mouseY < iconYJump + iconSize) {
                pGuiGraphics.renderTooltip(font, jumpStrengthInfo, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float partialTicks, int gx, int gy) {
        pGuiGraphics.setColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, texture);
        pGuiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int keyCode) {
        assert this.minecraft != null;
        if (this.minecraft.player != null) {

            int iconX = leftPos + 9;
            int iconSize = 16; // The Icon Size

            int iconYHealth = topPos + 18;
            int iconYLandSpeed = topPos + 40;
            int iconYSwimSpeed = topPos + 62;
            int iconYJump = topPos + 84;
            //Check if the mouse is inside the "hitbox"
            if (mouseX > iconX && mouseX < iconX + iconSize) {
                if (mouseY > iconYHealth && mouseY < iconYHealth + iconSize) {
                    this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.get(), 1, 1);
                } else if (mouseY > iconYLandSpeed && mouseY < iconYLandSpeed + iconSize) {
                    this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.get(), 1, 1);
                } else if (mouseY > iconYSwimSpeed && mouseY < iconYSwimSpeed + iconSize) {
                    this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.get(), 1, 1);
                } else if (mouseY > iconYJump && mouseY < iconYJump + iconSize) {
                    this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.get(), 1, 1);
                }
            } else if (mouseX > leftPos + (25) && mouseX < leftPos + (34) && mouseY > topPos + (4) && mouseY < topPos + (13)) {
                this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.get(), 1, 1);
            }

            //this.minecraft.player.displayClientMessage(Component.literal("Mouse Position : X =" + mouseX + " and Y =" + mouseY), false);
            //this.minecraft.player.displayClientMessage(Component.literal("Mouse Position2 : X =" + (mouseX - leftPos) + " and Y =" + (mouseY - topPos)), false);
        }

        return super.mouseClicked(mouseX, mouseY, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!filteredSuggestions.isEmpty()) {
            if (keyCode == InputConstants.KEY_UP) {
                suggestionIndex = Math.max(0, suggestionIndex - 1);
                return true;
            } else if (keyCode == InputConstants.KEY_DOWN) {
                suggestionIndex = Math.min(filteredSuggestions.size() - 1, suggestionIndex + 1);
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == InputConstants.KEY_NUMPADENTER) {
                if (suggestionIndex >= 0 && suggestionIndex < filteredSuggestions.size()) {
                    form.setValue(filteredSuggestions.get(suggestionIndex));
                    if (form.isFocused()) {
                        form.setFocused(false);
                    }
                    return true;
                }
            }
//            else if (keyCode == 257 || keyCode == 335) { // ENTER
//                if (suggestionIndex >= 0 && suggestionIndex < filteredSuggestions.size()) {
//                    String chosenName = filteredSuggestions.get(suggestionIndex);
//                    List<TransfurVariant<?>> variants = nameToVariants.get(chosenName);
//                    if (variants != null && !variants.isEmpty()) {
//                        TransfurVariant<?> variant = variants.get(0); // você pode escolher outro critério aqui
//                        form.setValue(variant.getFormId().toString()); // substitui pelo formId
//                    } else {
//                        form.setValue(chosenName); // fallback
//                    }
//
//                    filteredSuggestions.clear();
//                    suggestionIndex = -1;
//                    return true;
//                }
//            }
        }

        if (keyCode == InputConstants.KEY_ESCAPE) {
            assert this.minecraft != null;
            assert this.minecraft.player != null;
            if (!form.isFocused()) {
                this.minecraft.player.closeContainer();
            } else {
                form.setFocused(false);
            }
            return true;
        }

        return form.isFocused() ? form.keyPressed(keyCode, scanCode, modifiers)
                : super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        form.tick();

        InformantBlockEntity blockEntity = menu.blockEntity;
        if (!blockEntity.getText().equals(form.getValue())) form.setValue(blockEntity.getText());
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        // TODO Rewrite this to look more clean AND only display the EXTRA info like (Can Glide,Mining Speed, etc)
        /*String formIdString = form.getValue();
        if (!filteredSuggestions.isEmpty() && suggestionIndex >= 0) {
            String chosenName = filteredSuggestions.get(suggestionIndex);
            List<TransfurVariant<?>> variants = nameToVariants.get(chosenName);
            if (variants != null && !variants.isEmpty()) {
                TransfurVariant<?> variant = variants.get(0);
                formIdString = variant.getFormId().toString();
            }
        } else {
            String chosenName = formIdString;
            List<TransfurVariant<?>> variants = nameToVariants.get(chosenName);
            if (variants != null && !variants.isEmpty()) {
                TransfurVariant<?> variant = variants.get(0);
                formIdString = variant.getFormId().toString();
            }
        }

        if (this.world != null) {
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.position));
            if (blockEntity instanceof InformantBlockEntity informantBlockEntity) {
                ItemStack stack = informantBlockEntity.getItem(0);
                if (!(stack.isEmpty())) {
                    String data = stack.getOrCreateTag().getString("form");
                    if (!data.isEmpty()) {
                        formIdString = data;
                    }
                }
            }
        }

        ResourceLocation formId = ResourceLocation.tryParse(formIdString);
        double hp = TransfurVariantUtils.GetExtraHp(formId, entity);
        double swimSpeed = TransfurVariantUtils.GetSwimSpeed(formId, entity);
        double landSpeed = TransfurVariantUtils.GetLandSpeed(formId, entity);
        double jumpStrength = TransfurVariantUtils.GetJumpStrength(formId);
        boolean canFlyOrGlide = TransfurVariantUtils.CanGlideandFly(formId);
        String miningStrength = TransfurVariantUtils.getMiningStrength(formId);
        double extraHp = (hp) / 2.0;
        double landSpeedPct = landSpeed == 0 ? 0 : (landSpeed - 1) * 100;
        double swimSpeedPct = swimSpeed == 0 ? 0 : (swimSpeed - 1) * 100;
        double jumpStrengthPct = jumpStrength == 0 ? 0 : (jumpStrength - 1) * 100;

        var a = Component.translatable("text.changed_addon.land_speed")
                .append("")
                .append(landSpeedPct == 0
                        ? Component.literal("§7None§r")
                        : Component.literal((landSpeedPct > 0 ? "§a+" : "§c") + (int) landSpeedPct + "%"));

        var b = Component.translatable("text.changed_addon.swim_speed")
                .append("")
                .append(swimSpeedPct == 0
                        ? Component.literal("§7None§r")
                        : Component.literal((swimSpeedPct > 0 ? "§a+" : "§c") + (int) swimSpeedPct + "%"));

        var c = Component.translatable("text.changed_addon.additionalHealth")
                .append("")
                .append(extraHp == 0
                        ? Component.literal("§7None§r")
                        : Component.literal((extraHp > 0 ? "§a+" : "§c") + extraHp + "§r"))
                .append(Component.translatable("text.changed_addon.additionalHealth.Hearts"));

        var d = Component.translatable("text.changed_addon.miningStrength", miningStrength);

        var e = Component.translatable("text.changed_addon.jumpStrength")
                .append("")
                .append(jumpStrengthPct == 0
                        ? Component.literal("§7None§r")
                        : Component.literal((jumpStrengthPct > 0 ? "§a+" : "§c") + (int) jumpStrengthPct + "%"));

        var f = Component.translatable("text.changed_addon.canGlide/Fly")
                .append("")
                .append(canFlyOrGlide
                        ? Component.literal("§aTrue§r")
                        : Component.literal("§cFalse§r"));

        this.font.draw(poseStack, a, 5, 44, -12829636);

        this.font.draw(poseStack, b, 5, 57, -12829636);

        this.font.draw(poseStack, c, 5, 31, -12829636);

        this.font.draw(poseStack, d, 5, 94, -12829636);

        this.font.draw(poseStack, e, 5, 69, -12829636);

        this.font.draw(poseStack, f, 5, 82, -12829636);*/

        pGuiGraphics.drawString(font, Component.translatable("gui.changed_addon.informant_gui.label_empty"), 27, 5, -12829636);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void init() {
        super.init();
        form.setX(this.leftPos + 44);
        form.setY(this.topPos + 13);
        addWidget(form);
    }

    public void updateSuggestions(String input) {
        filteredSuggestions.clear();

        if (input.isEmpty()) {
            suggestionIndex = -1;
            form.setSuggestion(Component.translatable("gui.changed_addon.informant_gui.form").getString());
            return;
        }

        allSuggestions.stream()
                .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                .distinct()
                .limit(6).forEach(filteredSuggestions::add);

        if (filteredSuggestions.isEmpty()) {
            suggestionIndex = -1;
            form.setSuggestion(null);
            return;
        }

        suggestionIndex = Mth.clamp(suggestionIndex, 0, filteredSuggestions.size() - 1);

        String suggestion = filteredSuggestions.get(suggestionIndex);
        form.setSuggestion(input.equalsIgnoreCase(suggestion) ? null : suggestion.substring(input.length()));
    }
}
