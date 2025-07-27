
package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.entity.InformantBlockEntity;
import net.foxyas.changedaddon.network.InformantBlockGuiKeyMessage;
import net.foxyas.changedaddon.procedures.IfisEmptyProcedure;
import net.foxyas.changedaddon.process.util.TransfurVariantUtils;
import net.foxyas.changedaddon.world.inventory.InformantGuiMenu;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;
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
        if (mouseX > leftPos + (25) && mouseX < leftPos + (34) && mouseY > topPos + (4) && mouseY < topPos + (13)) {
            this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.informant_gui.tooltip_type_the_form"), mouseX, mouseY);
        }
        if (IfisEmptyProcedure.execute(entity)) {
            if (mouseX > leftPos + 151 && mouseX < leftPos + 168 && mouseY > topPos + 88 && mouseY < topPos + 105) {
                this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.informant_gui.tooltip_put_a_syringe_with_a_form"), mouseX, mouseY);
            }
        }
        if (!filteredSuggestions.isEmpty()) {
            int x = form.x;
            int y = form.y + form.getHeight() + 2;
            int width = form.getWidth();
            int height = 12;

            for (int i = 0; i < filteredSuggestions.size(); i++) {
                String suggestion = filteredSuggestions.get(i);
                Color color = new Color(0x8840BEFF, true);
                Color Bgcolor = new Color(0x881DAAEC, true);
                int bgColor = i == suggestionIndex ? Bgcolor.getRGB() : color.getRGB();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                fill(ms, x, y + i * height, x + width, y + (i + 1) * height, bgColor);
                this.font.draw(ms, suggestion, x + 2, y + i * height + 2, 0xFFFFFF);
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

        String formIdString = form.getValue();
        if (!filteredSuggestions.isEmpty() && suggestionIndex >= 0) {
            String chosenName = filteredSuggestions.get(suggestionIndex);
            List<TransfurVariant<?>> variants = nameToVariants.get(chosenName);
            if (variants != null && !variants.isEmpty()) {
                TransfurVariant<?> variant = variants.get(0); // você pode escolher outro critério aqui
                formIdString = variant.getFormId().toString();
            }
        } else {
            String chosenName = formIdString;
            List<TransfurVariant<?>> variants = nameToVariants.get(chosenName);
            if (variants != null && !variants.isEmpty()) {
                TransfurVariant<?> variant = variants.get(0); // você pode escolher outro critério aqui
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

        MutableComponent landSpeedInfo = new TranslatableComponent("text.changed_addon.land_speed")
                .append("")
                .append(landSpeedPct == 0
                        ? new TextComponent("§7None§r")
                        : new TextComponent((landSpeedPct > 0 ? "§a+" : "§c") + (int) landSpeedPct + "%"));

        MutableComponent swimSpeedInfo = new TranslatableComponent("text.changed_addon.swim_speed")
                .append("")
                .append(swimSpeedPct == 0
                        ? new TextComponent("§7None§r")
                        : new TextComponent((swimSpeedPct > 0 ? "§a+" : "§c") + (int) swimSpeedPct + "%"));

        MutableComponent additionalHealthInfo = new TranslatableComponent("text.changed_addon.additionalHealth")
                .append("")
                .append(extraHp == 0
                        ? new TextComponent("§7None§r")
                        : new TextComponent((extraHp > 0 ? "§a+" : "§c") + extraHp + "§r"))
                .append(new TranslatableComponent("text.changed_addon.additionalHealth.Hearts"));

        TranslatableComponent miningStrengthInfo = new TranslatableComponent("text.changed_addon.miningStrength", miningStrength);

        MutableComponent jumpStrengthInfo = new TranslatableComponent("text.changed_addon.jumpStrength")
                .append("")
                .append(jumpStrengthPct == 0
                        ? new TextComponent("§7None§r")
                        : new TextComponent((jumpStrengthPct > 0 ? "§a+" : "§c") + (int) jumpStrengthPct + "%"));

        MutableComponent canGlideInfo = new TranslatableComponent("text.changed_addon.canGlide/Fly")
                .append("")
                .append(canFlyOrGlide
                        ? new TextComponent("§aTrue§r")
                        : new TextComponent("§cFalse§r"));


        // Verifica se o mouse está sobre cada ícone e exibe a tooltip correspondente
        if (mouseX > iconX && mouseX < iconX + iconSize) {
            if (mouseY > iconYHealth && mouseY < iconYHealth + iconSize) {
                renderTooltip(ms, additionalHealthInfo, mouseX, mouseY);
            } else if (mouseY > iconYLandSpeed && mouseY < iconYLandSpeed + iconSize) {
                renderTooltip(ms, landSpeedInfo, mouseX, mouseY);
            } else if (mouseY > iconYSwimSpeed && mouseY < iconYSwimSpeed + iconSize) {
                renderTooltip(ms, List.of(swimSpeedInfo), Optional.empty(), mouseX, mouseY);
            } else if (mouseY > iconYJump && mouseY < iconYJump + iconSize) {
                renderTooltip(ms, jumpStrengthInfo, mouseX, mouseY);
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
                    this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1);
                } else if (mouseY > iconYLandSpeed && mouseY < iconYLandSpeed + iconSize) {
                    this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1);
                } else if (mouseY > iconYSwimSpeed && mouseY < iconYSwimSpeed + iconSize) {
                    this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1);
                } else if (mouseY > iconYJump && mouseY < iconYJump + iconSize) {
                    this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1);
                }
            } else if (mouseX > leftPos + (25) && mouseX < leftPos + (34) && mouseY > topPos + (4) && mouseY < topPos + (13)) {
                this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1);
            }

            this.minecraft.player.displayClientMessage(new TextComponent("Mouse Position : X =" + mouseX + " and Y =" + mouseY), false);
            this.minecraft.player.displayClientMessage(new TextComponent("Mouse Position2 : X =" + (mouseX - leftPos) + " and Y =" + (mouseY - topPos)), false);

        }


        return super.mouseClicked(mouseX, mouseY, keyCode);
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
                    if (form.isFocused()) {
                        form.setFocus(false);
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

        if (keyCode == 256) {
            assert this.minecraft != null;
            assert this.minecraft.player != null;
            if (!form.isFocused()) {
                this.minecraft.player.closeContainer();
            } else {
                form.setValue("");
                form.setFocus(false);
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

        if (!form.getValue().isEmpty()) {
            List<TransfurVariant<?>> variants = nameToVariants.get(form.getValue());
            if (variants != null && !variants.isEmpty()) {
                TransfurVariant<?> variant = variants.get(0);
                if (variant != null) {
                    InformantBlockGuiKeyMessage message = new InformantBlockGuiKeyMessage(variant.getFormId().toString(), this.getPos());
                    ChangedAddonMod.PACKET_HANDLER.sendToServer(message);
                }
            }
        } else {
            if (form.isFocused()) {
                InformantBlockGuiKeyMessage message = new InformantBlockGuiKeyMessage("", this.getPos());
                ChangedAddonMod.PACKET_HANDLER.sendToServer(message);
            }
        }
    }

    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
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

        var a = new TranslatableComponent("text.changed_addon.land_speed")
                .append("")
                .append(landSpeedPct == 0
                        ? new TextComponent("§7None§r")
                        : new TextComponent((landSpeedPct > 0 ? "§a+" : "§c") + (int) landSpeedPct + "%"));

        var b = new TranslatableComponent("text.changed_addon.swim_speed")
                .append("")
                .append(swimSpeedPct == 0
                        ? new TextComponent("§7None§r")
                        : new TextComponent((swimSpeedPct > 0 ? "§a+" : "§c") + (int) swimSpeedPct + "%"));

        var c = new TranslatableComponent("text.changed_addon.additionalHealth")
                .append("")
                .append(extraHp == 0
                        ? new TextComponent("§7None§r")
                        : new TextComponent((extraHp > 0 ? "§a+" : "§c") + extraHp + "§r"))
                .append(new TranslatableComponent("text.changed_addon.additionalHealth.Hearts"));

        var d = new TranslatableComponent("text.changed_addon.miningStrength", miningStrength);

        var e = new TranslatableComponent("text.changed_addon.jumpStrength")
                .append("")
                .append(jumpStrengthPct == 0
                        ? new TextComponent("§7None§r")
                        : new TextComponent((jumpStrengthPct > 0 ? "§a+" : "§c") + (int) jumpStrengthPct + "%"));

        var f = new TranslatableComponent("text.changed_addon.canGlide/Fly")
                .append("")
                .append(canFlyOrGlide
                        ? new TextComponent("§aTrue§r")
                        : new TextComponent("§cFalse§r"));

        this.font.draw(poseStack, a, 5, 44, -12829636);

        this.font.draw(poseStack, b, 5, 57, -12829636);

        this.font.draw(poseStack, c, 5, 31, -12829636);

        this.font.draw(poseStack, d, 5, 94, -12829636);

        this.font.draw(poseStack, e, 5, 69, -12829636);

        this.font.draw(poseStack, f, 5, 82, -12829636);*/

        this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.informant_gui.label_empty"), 27.5f, 5.5f, -12829636);
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

    private final Map<ResourceLocation, String> variantToName =
            TransfurVariant.getPublicTransfurVariants()
                    .collect(Collectors.toMap(
                            TransfurVariant::getFormId, // chave: a própria variante
                            v -> v.getEntityType().getDescription().getString(), // valor: nome legível
                            (a, b) -> a
                    ));


    private List<String> filteredSuggestions = new ArrayList<>();
    private int suggestionIndex = -1;


    @Override
    public void init() {
        super.init();
        assert this.minecraft != null;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        form = new EditBox(this.font, this.leftPos + 44, this.topPos + 13, 90, 10,
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

            @Override
            public void renderButton(PoseStack p_94160_, int p_94161_, int p_94162_, float p_94163_) {
                super.renderButton(p_94160_, p_94161_, p_94162_, p_94163_);
            }

            @Override
            protected void renderBg(PoseStack p_93661_, Minecraft p_93662_, int p_93663_, int p_93664_) {
                super.renderBg(p_93661_, p_93662_, p_93663_, p_93664_);
            }
        };
        form.setTextColor(new Color(0, 205, 255).getRGB());
        //form.setBordered(false);
        //form.setAlpha(0.25f);

        if (this.world != null) {
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.position));
            if (blockEntity instanceof InformantBlockEntity informantBlockEntity) {
                String selectedForm = informantBlockEntity.getSelectedForm();
                if (!selectedForm.isEmpty()) {
                    ResourceLocation res = ResourceLocation.tryParse(selectedForm);
                    if (res != null) {
                        String data = variantToName.get(res);
                        form.setValue(data);
                    }
                }
            }
        }


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

    public BlockPos getPos() {
        return new BlockPos(this.getPosition());
    }

    public Level getWorld() {
        return world;
    }
}
