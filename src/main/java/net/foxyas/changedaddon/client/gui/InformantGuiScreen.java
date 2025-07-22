
package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.procedures.IfisEmptyProcedure;
import net.foxyas.changedaddon.process.util.TransfurVariantUtils;
import net.foxyas.changedaddon.world.inventory.InformantGuiMenu;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InformantGuiScreen extends AbstractContainerScreen<InformantGuiMenu> {
    private final static HashMap<String, Object> guistate = InformantGuiMenu.guistate;
    private final Level world;
    private final Vec3i position;
    private final Player entity;
    public EditBox form;

    public InformantGuiScreen(InformantGuiMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.world = container.world;
        this.position = new Vec3i(container.x, container.y, container.z);
        this.entity = container.entity;
        this.imageWidth = 176;
        this.imageHeight = 195;
    }

    private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/informant_gui.png");

    @Override
    public void render(@NotNull PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        form.render(ms, mouseX, mouseY, partialTicks);
        this.renderTooltip(ms, mouseX, mouseY);
        if (mouseX > leftPos + 4 && mouseX < leftPos + 28 && mouseY > topPos + 4 && mouseY < topPos + 28)
            this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.informant_gui.tooltip_type_the_form"), mouseX, mouseY);
        if (IfisEmptyProcedure.execute(entity))
            if (mouseX > leftPos + 147 && mouseX < leftPos + 171 && mouseY > topPos + 4 && mouseY < topPos + 28)
                this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.informant_gui.tooltip_put_a_syringe_with_a_form"), mouseX, mouseY);
        if (!filteredSuggestions.isEmpty()) {
            int x = form.x;
            int y = form.y + form.getHeight() + 2;
            int width = form.getWidth();
            int height = 12;

            for (int i = 0; i < filteredSuggestions.size(); i++) {
                String suggestion = filteredSuggestions.get(i);
                int bgColor = i == suggestionIndex ? 0x88AAAAAA : 0x88000000;
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                fill(ms, x, y + i * height, x + width, y + (i + 1) * height, bgColor);
                this.font.draw(ms, suggestion, x + 2, y + i * height + 2, 0xFFFFFF);
                RenderSystem.disableBlend();
            }
        }

    }

    @Override
    protected void renderBg(@NotNull PoseStack ms, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, texture);
        blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!filteredSuggestions.isEmpty()) {
            if (keyCode == 265) { // UP
                suggestionIndex = Math.max(0, suggestionIndex - 1);
                return true;
            } else if (keyCode == 264) { // DOWN
                suggestionIndex = Math.min(filteredSuggestions.size() - 1, suggestionIndex + 1);
                return true;
            } else if (keyCode == 257 || keyCode == 335) { // ENTER
                if (suggestionIndex >= 0 && suggestionIndex < filteredSuggestions.size()) {
                    form.setValue(filteredSuggestions.get(suggestionIndex));
                    filteredSuggestions.clear();
                    suggestionIndex = -1;
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

        if (keyCode == 256) {
            assert this.minecraft != null;
            assert this.minecraft.player != null;
            this.minecraft.player.closeContainer();
            return true;
        }

        return form.isFocused() ? form.keyPressed(keyCode, scanCode, modifiers)
                : super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        form.tick();
    }

    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
        String formId = form.getValue();
        if (!filteredSuggestions.isEmpty() && suggestionIndex >= 0) {
            String chosenName = filteredSuggestions.get(suggestionIndex);
            List<TransfurVariant<?>> variants = nameToVariants.get(chosenName);
            if (variants != null && !variants.isEmpty()) {
                TransfurVariant<?> variant = variants.get(0); // você pode escolher outro critério aqui
                formId = variant.getFormId().toString();
            }
        } else {
            String chosenName = formId;
            List<TransfurVariant<?>> variants = nameToVariants.get(chosenName);
            if (variants != null && !variants.isEmpty()) {
                TransfurVariant<?> variant = variants.get(0); // você pode escolher outro critério aqui
                formId = variant.getFormId().toString();
            }
        }


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

        this.font.draw(poseStack,
                new TranslatableComponent("text.changed_addon.land_speed")
                        .append("")
                        .append(landSpeedPct == 0
                                ? new TextComponent("§7None§r")
                                : new TextComponent((landSpeedPct > 0 ? "§a+" : "§c") + (int) landSpeedPct + "%")), 5, 44, -12829636);

        this.font.draw(poseStack,
                new TranslatableComponent("text.changed_addon.swim_speed")
                        .append("")
                        .append(swimSpeedPct == 0
                                ? new TextComponent("§7None§r")
                                : new TextComponent((swimSpeedPct > 0 ? "§a+" : "§c") + (int) swimSpeedPct + "%")), 5, 57, -12829636);

        this.font.draw(poseStack,
                new TranslatableComponent("text.changed_addon.additionalHealth")
                        .append("")
                        .append(extraHp == 0
                                ? new TextComponent("§7None§r")
                                : new TextComponent((extraHp > 0 ? "§a+" : "§c") + extraHp + "§r"))
                        .append(new TranslatableComponent("text.changed_addon.additionalHealth.Hearts")), 5, 31, -12829636);

        this.font.draw(poseStack, new TranslatableComponent("text.changed_addon.miningStrength", miningStrength), 5, 94, -12829636);

        this.font.draw(poseStack,
                new TranslatableComponent("text.changed_addon.jumpStrength")
                        .append("")
                        .append(jumpStrengthPct == 0
                                ? new TextComponent("§7None§r")
                                : new TextComponent((jumpStrengthPct > 0 ? "§a+" : "§c") + (int) jumpStrengthPct + "%")), 5, 69, -12829636);

        this.font.draw(poseStack,
                new TranslatableComponent("text.changed_addon.canGlide/Fly")
                        .append("")
                        .append(canFlyOrGlide
                                ? new TextComponent("§aTrue§r")
                                : new TextComponent("§cFalse§r")), 5, 82, -12829636);

        this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.informant_gui.label_empty"), 13, 10, -12829636);
    }

    @Override
    public void onClose() {
        super.onClose();
        Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
    }

    private final List<String> allTransfurVariantsSuggestions = TransfurVariant.getPublicTransfurVariants()
            .map(v -> v.getFormId().toString())
            .toList();

    private final List<String> allSuggestions = TransfurVariant.getPublicTransfurVariants()
            .map(v -> v.getEntityType().getDescription().getString())
            .toList();

    private final Map<String, List<TransfurVariant<?>>> nameToVariants = TransfurVariant.getPublicTransfurVariants()
            .collect(Collectors.groupingBy(
                    v -> v.getEntityType().getDescription().getString()
            ));

    private List<String> filteredSuggestions = new ArrayList<>();
    private int suggestionIndex = -1;


    @Override
    public void init() {
        super.init();
        assert this.minecraft != null;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        form = new EditBox(this.font, this.leftPos + 27, this.topPos + 5, 120, 20,
                new TranslatableComponent("gui.changed_addon.informant_gui.form")) {
            {
                setSuggestion(new TranslatableComponent("gui.changed_addon.informant_gui.form").getString());
            }

            @Override
            public void insertText(@NotNull String text) {
                super.insertText(text);
                updateSuggestions(getValue());
            }

            @Override
            public void moveCursorTo(int pos) {
                super.moveCursorTo(pos);
                updateSuggestions(getValue());
            }
        };

        form.setMaxLength(32767);
        guistate.put("text:form", form);
        this.addWidget(this.form);

        form.setResponder(this::updateSuggestions); // sempre que o valor mudar, atualiza sugestões
    }

    public void updateSuggestions(String input) {
        if (input.isEmpty()) {
            filteredSuggestions.clear();
            suggestionIndex = -1;
        } else {
            filteredSuggestions = new ArrayList<>(allSuggestions.stream()
                    .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                    .distinct()
                    .limit(6)
                    .toList());

            suggestionIndex = filteredSuggestions.isEmpty() ? -1 : 0;
        }

        if (filteredSuggestions.size() == 1 && !input.equalsIgnoreCase(filteredSuggestions.get(0))) {
            String suggestion = filteredSuggestions.get(0);
            if (suggestion.toLowerCase().startsWith(input.toLowerCase())) {
                String remaining = suggestion.substring(input.length()); // remove o que já foi escrito
                form.setSuggestion(remaining);
            } else {
                form.setSuggestion(null);
            }
        } else {
            form.setSuggestion(null);
        }

    }


    public Vec3i getPosition() {
        return position;
    }

    public Level getWorld() {
        return world;
    }
}
