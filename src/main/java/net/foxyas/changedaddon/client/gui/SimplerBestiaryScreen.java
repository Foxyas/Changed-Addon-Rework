package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import net.foxyas.changedaddon.entity.api.IBestiaryEntityData;
import net.foxyas.changedaddon.util.ChangedEntityUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;

/**
 * Vanilla Minecraft styled BestiaryScreen with responsive container layout,
 * interactive 3D model viewport, attribute bar chart (replacing the radial pie),
 * click-and-drag interactive scrollbars, and vanilla container styling.
 *
 * <p>Mappings: Official Mojang Mappings (Minecraft 1.20.1 Forge)
 * <p>Author / Contributor: ParkaBird
 */
public class SimplerBestiaryScreen extends AbstractBestiaryScreen {
    // Base Target Dialog Dimensions
    private static final int BASE_W = 440;
    private static final int BASE_H = 224;

    // Computed Dialog Dimensions for current window
    private int dialogW;
    private int dialogH;
    private int left;
    private int top;

    // Panel Widths
    private int listW;
    private int vpW;
    private int detW;

    // Left List
    private final List<TransfurVariant<?>> allVariants = new ArrayList<>();
    private final List<TransfurVariant<?>> filteredVariants = new ArrayList<>();
    private EditBox searchBox;
    private int listScrollOffset = 0;

    // Scrollbar Drag State
    private boolean isDraggingListScroll = false;
    private double listScrollGrabOffset = 0.0;

    private boolean isDraggingDetailsScroll = false;
    private double detailsScrollGrabOffset = 0.0;

    // Selected Variant & Cached Entity
    protected TransfurVariant<?> selected;
    private ChangedEntity currentEntity;
    private boolean isUnlocked = true;

    // Middle Viewport (3D Model)
    private float modelYaw = 25.0f;
    private float modelPitch = 0.0f;
    private float modelZoom = 46.0f;
    private float modelOffsetX = 0.0f;
    private float modelOffsetY = 0.0f;
    private boolean isDraggingModel = false;
    private boolean isDraggingModelOffset = false;

    // Right Details & Attributes Bar Chart
    private int detailsScrollOffset = 0;
    private int maxDetailsScroll = 0;
    private final List<AttributeBarItem> attributeBars = new ArrayList<>();
    private final List<FormattedCharSequence> loreLines = new ArrayList<>();
    private String classificationText = "";

    public SimplerBestiaryScreen() {
        super(Component.translatable("gui.changed_addon.bestiary.title"));
    }

    @Override
    protected void init() {
        super.init();

        // Responsive Dialog Sizing (fits comfortably on all GUI scales)
        this.dialogW = Math.min(BASE_W, this.width - 12);
        this.dialogH = Math.min(BASE_H, this.height - 12);
        this.left = (this.width - this.dialogW) / 2;
        this.top = (this.height - this.dialogH) / 2;

        // Proportional Panels
        int availableInnerW = this.dialogW - 28;
        this.listW = Math.max(90, Math.min(115, availableInnerW * 27 / 100));
        this.vpW = Math.max(105, Math.min(135, availableInnerW * 33 / 100));
        this.detW = availableInnerW - this.listW - this.vpW;

        // Populate variants once
        if (allVariants.isEmpty()) {
            List<TransfurVariant<?>> variants = TransfurVariant.getPublicTransfurVariants()
                    .sorted(Comparator.comparing(var -> var.getFormId().toString()))
                    .toList();
            allVariants.addAll(variants);
        }

        // Initialize Search Box
        String previousQuery = (searchBox != null) ? searchBox.getValue() : "";
        int searchX = this.left + 8;
        int searchY = this.top + 22;
        int searchH = 16;
        searchBox = new EditBox(this.font, searchX, searchY, this.listW, searchH, Component.translatable("gui.changed_addon.bestiary.search"));
        searchBox.setHint(Component.translatable("gui.changed_addon.bestiary.search.hint"));
        searchBox.setResponder(this::onSearchQueryChanged);
        if (!previousQuery.isEmpty()) {
            searchBox.setValue(previousQuery);
        }
        this.addRenderableWidget(searchBox);

        updateFilteredVariants();

        // Default selection
        if (this.selected == null && !filteredVariants.isEmpty()) {
            selectTf(ChangedTransfurVariants.GAS_WOLF_MALE.get());
        } else if (this.selected != null) {
            refreshSelectedEntity();
        }
    }

    private void onSearchQueryChanged(String query) {
        updateFilteredVariants();
        listScrollOffset = 0;
    }

    private void updateFilteredVariants() {
        filteredVariants.clear();
        String query = searchBox != null ? searchBox.getValue().trim().toLowerCase(Locale.ROOT) : "";
        for (TransfurVariant<?> tf : allVariants) {
            String name = getVariantDisplayName(tf).toLowerCase(Locale.ROOT);
            String id = tf.getFormId().toString().toLowerCase(Locale.ROOT);
            if (query.isEmpty() || name.contains(query) || id.contains(query)) {
                filteredVariants.add(tf);
            }
        }
    }

    private String getVariantDisplayName(TransfurVariant<?> tf) {
        if (tf == null) return Component.translatable("gui.changed_addon.bestiary.unknown").getString();
        EntityType<?> type = tf.getEntityType();
        if (type != null) {
            return Component.translatable(type.getDescriptionId()).getString();
        }
        return tf.getFormId().getPath();
    }

    @Override
    public void tick() {
        super.tick();
        if (searchBox != null) {
            searchBox.tick();
        }
        if (currentEntity != null && this.minecraft != null && this.minecraft.player != null) {
            currentEntity.tickCount = this.minecraft.player.tickCount;
        }
    }

    protected void selectTf(TransfurVariant<?> tf) {
        if (tf == null) return;
        this.selected = tf;
        refreshSelectedEntity();
    }

    private void refreshSelectedEntity() {
        if (this.selected == null || this.minecraft == null || this.minecraft.level == null) {
            return;
        }

        Level level = this.minecraft.level;
        Player player = this.minecraft.player;
        ChangedEntity entity = (ChangedEntity) ChangedEntities.getCachedEntity(level, this.selected.getEntityType());

        // Respect ReferencedEntityType from IBestiaryEntityData
        if (entity instanceof IBestiaryEntityData data) {
            EntityType<?> refType = data.getReferencedEntityType();
            if (refType != null && refType != entity.getType()) {
                Entity cached = ChangedEntities.getCachedEntity(level, refType);
                if (cached instanceof ChangedEntity ce) {
                    entity = ce;
                }
            }
            this.isUnlocked = player == null || data.isUnlocked(player);
        } else {
            this.isUnlocked = true;
        }

        this.currentEntity = entity;
        this.detailsScrollOffset = 0;
        this.buildDetailsAndBarChart(entity);
    }

    /**
     * Builds lore lines and attribute bars (bar chart).
     */
    private void buildDetailsAndBarChart(ChangedEntity entity) {
        loreLines.clear();
        attributeBars.clear();
        classificationText = "";

        if (entity == null) {
            return;
        }

        int textWrapWidth = Math.max(100, this.detW - 16);
        String attrTitleKey = Component.translatable("gui.changed_addon.bestiary.attributes").getString();
        String classTitleKey = Component.translatable("gui.changed_addon.bestiary.classification").getString();

        // 1. Lore & Classification
        if (entity instanceof IBestiaryEntityData data) {
            List<IBestiaryEntityData.BestiaryInfo> infos = data.getBestiaryInfo().stream()
                    .sorted(Comparator.comparingInt(IBestiaryEntityData.BestiaryInfo::order))
                    .toList();
            for (IBestiaryEntityData.BestiaryInfo info : infos) {
                String titleStr = info.title().getString();
                // Skip duplicate text attribute dump; our bar chart handles it!
                if (titleStr.equalsIgnoreCase(attrTitleKey) || titleStr.equalsIgnoreCase("Attributes")) {
                    continue;
                }
                if (titleStr.equalsIgnoreCase(classTitleKey) || titleStr.equalsIgnoreCase("Classification") || titleStr.toLowerCase().contains("class")) {
                    classificationText = info.description().getString();
                } else {
                    loreLines.addAll(this.font.split(
                            Component.literal("§e" + titleStr + ": ").append(info.description()), textWrapWidth));
                }
            }
        }

        List<Component> subtitles = ChangedEntityUtil.getEntitySubtitle(entity);
        if (!subtitles.isEmpty()) {
            for (int i = 0; i < subtitles.size(); i++) {
                Component sub = subtitles.get(i);
                String subStr = sub.getString();
                if (subStr.contains("Classification:")) {
                    if (classificationText.isEmpty() && i + 1 < subtitles.size()) {
                        classificationText = subtitles.get(i + 1).getString().replace("§f", "").trim();
                        i++;
                        continue;
                    }
                }
                loreLines.addAll(this.font.split(sub, textWrapWidth));
            }
        }

        if (loreLines.isEmpty()) {
            loreLines.addAll(this.font.split(Component.translatable("gui.changed_addon.bestiary.lore.fallback"), textWrapWidth));
        }

        // 2. Bar Chart Attributes
        AttributeSupplier playerDefaults = DefaultAttributes.getSupplier(EntityType.PLAYER);
        AttributeMap transformedMap = entity.getAttributes();

        // Standard attributes to inspect
        checkAndAddBar(transformedMap, playerDefaults, Attributes.MAX_HEALTH, Component.translatable(Attributes.MAX_HEALTH.getDescriptionId()), 60.0f, 0xFFFF4444, 0xFFFF7777);
        checkAndAddBar(transformedMap, playerDefaults, Attributes.ARMOR, Component.translatable(Attributes.ARMOR.getDescriptionId()), 20.0f, 0xFF55FFFF, 0xFFAAFFFF);
        checkAndAddBar(transformedMap, playerDefaults, Attributes.MOVEMENT_SPEED, Component.translatable(Attributes.MOVEMENT_SPEED.getDescriptionId()), 0.20f, 0xFF55FF55, 0xFFAAFFAA);
        checkAndAddBar(transformedMap, playerDefaults, Attributes.ATTACK_DAMAGE, Component.translatable(Attributes.ATTACK_DAMAGE.getDescriptionId()), 16.0f, 0xFFFFAA00, 0xFFFFDD55);
        checkAndAddBar(transformedMap, playerDefaults, Attributes.ARMOR_TOUGHNESS, Component.translatable(Attributes.ARMOR_TOUGHNESS.getDescriptionId()), 12.0f, 0xFF88AAFF, 0xFFBBDDFF);
        checkAndAddBar(transformedMap, playerDefaults, Attributes.KNOCKBACK_RESISTANCE, Component.translatable(Attributes.KNOCKBACK_RESISTANCE.getDescriptionId()), 1.0f, 0xFFCC66FF, 0xFFEE99FF);
        checkAndAddBar(transformedMap, playerDefaults, Attributes.FOLLOW_RANGE, Component.translatable(Attributes.FOLLOW_RANGE.getDescriptionId()), 48.0f, 0xFFBBAA88, 0xFFDDCCAA);

        // Calculate maximum scroll height
        int totalContentHeight = 24 + (loreLines.size() * 10) + 16 + (attributeBars.size() * 20);
        this.maxDetailsScroll = Math.max(0, totalContentHeight - (this.dialogH - 40));
    }

    private void checkAndAddBar(AttributeMap map, AttributeSupplier playerDefaults,
                                Attribute attr, Component label, float maxScale, int fillColor, int highlightColor) {
        if (!map.hasAttribute(attr)) return;

        double value = map.getValue(attr);
        double base = playerDefaults.hasAttribute(attr) ? playerDefaults.getBaseValue(attr) : 0.0;

        // Speed adjustment in MC attribute system
        if (attr == Attributes.MOVEMENT_SPEED) {
            if (value > 0.5) {
                value *= 0.1;
            }
        }

        double diff = value - base;
        String valText;
        if (attr == Attributes.MOVEMENT_SPEED) {
            double percent = (base > 0) ? (value / base - 1.0) * 100.0 : 0.0;
            valText = String.format(Locale.ROOT, "%+.0f%%", percent);
        } else if (diff != 0.0) {
            valText = String.format(Locale.ROOT, "%.1f (%+.1f)", value, diff);
        } else {
            valText = String.format(Locale.ROOT, "%.1f", value);
        }

        float ratio = clamp((float) (value / maxScale), 0.05f, 1.0f);
        attributeBars.add(new AttributeBarItem(label, valText, ratio, fillColor, highlightColor));
    }

    // -------------------------------------------------------------
    // RENDER PIPELINE (Vanilla Minecraft UI Style)
    // -------------------------------------------------------------
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Translucent background dimming (transparent to world, no dirt)
        graphics.fill(0, 0, this.width, this.height, 0x70000000);

        // 1. Draw Main Vanilla Frame (Dark slate container with 3D bevels)
        renderVanillaContainerFrame(graphics, this.left, this.top, this.dialogW, this.dialogH);

        // Title Header
        graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.header"), this.left + 10, this.top + 7, 0xFFFFFF);

        // Close Hint (hoverable)
        String escHint = Component.translatable("gui.changed_addon.bestiary.esc_close").getString();
        int escW = this.font.width(escHint);
        int escX = this.left + this.dialogW - escW - 10;
        int escY = this.top + 7;
        boolean isEscHovered = mouseX >= escX - 2 && mouseX <= escX + escW + 2 && mouseY >= escY - 2 && mouseY <= escY + 11;
        graphics.drawString(this.font, escHint, escX, escY, isEscHovered ? 0xFFFFAA : 0x888888, false);

        // Calculate panel coordinates
        int leftListX = this.left + 8;
        int leftListY = this.top + 42;
        int leftListH = this.dialogH - 50;

        int vpX = leftListX + this.listW + 6;
        int vpY = this.top + 22;
        int vpH = this.dialogH - 30;

        int detX = vpX + this.vpW + 6;
        int detY = this.top + 22;
        int detH = this.dialogH - 30;

        // 2. Left Panel: Variant List
        renderVariantList(graphics, leftListX, leftListY, this.listW, leftListH, mouseX, mouseY);

        // 3. Middle Panel: 3D Viewport
        renderModelViewport(graphics, vpX, vpY, this.vpW, vpH, mouseX, mouseY, partialTick);

        // 4. Right Panel: Lore & Bar Chart Attributes
        renderDetailsAndBarChart(graphics, detX, detY, this.detW, detH, mouseX, mouseY);

        // Render widgets (Search Box)
        super.render(graphics, mouseX, mouseY, partialTick);

        // Watermark: bottom-right corner outside panel
        String screenWm = "§8© ParkaBird 2026";
        int swmW = this.font.width("© ParkaBird 2026");
        graphics.drawString(this.font, screenWm, this.width - swmW - 8, this.height - 13, 0x888888, false);
    }

    /**
     * Renders a classic Minecraft container frame with 3D bevel borders.
     */
    private void renderVanillaContainerFrame(GuiGraphics graphics, int x, int y, int w, int h) {
        // Main container dark slate fill
        graphics.fill(x, y, x + w, y + h, 0xF0141416);

        // 3D Bevel Outline
        // Top & Left Highlight
        graphics.fill(x, y, x + w, y + 1, 0xFF4C4C54);
        graphics.fill(x, y, x + 1, y + h, 0xFF4C4C54);
        // Bottom & Right Shadow
        graphics.fill(x, y + h - 1, x + w, y + h, 0xFF18181A);
        graphics.fill(x + w - 1, y, x + w, y + h, 0xFF18181A);

        // Inner border line
        graphics.renderOutline(x + 1, y + 1, w - 2, h - 2, 0xFF2A2A30);

        // Header accent divider line
        graphics.fill(x + 4, y + 19, x + w - 4, y + 20, 0xFF3E3E48);
    }

    /**
     * Renders an inset panel slot (e.g. for viewport or list box).
     */
    private void renderInsetBox(GuiGraphics graphics, int x, int y, int w, int h) {
        // Inset fill
        graphics.fill(x, y, x + w, y + h, 0xFF0D0D10);
        // Inset bevel shadow (top/left)
        graphics.fill(x, y, x + w, y + 1, 0xFF16161A);
        graphics.fill(x, y, x + 1, y + h, 0xFF16161A);
        // Inset bevel highlight (bottom/right)
        graphics.fill(x, y + h - 1, x + w, y + h, 0xFF35353D);
        graphics.fill(x + w - 1, y, x + w, y + h, 0xFF35353D);
    }

    /**
     * Left side variant list with search filtering.
     */
    private void renderVariantList(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        renderInsetBox(graphics, x, y, w, h);

        int itemHeight = 18;
        int visibleCount = (h - 4) / itemHeight;
        int totalCount = filteredVariants.size();

        // Clamp scroll offset
        int maxScroll = Math.max(0, totalCount - visibleCount);
        listScrollOffset = clamp(listScrollOffset, 0, maxScroll);

        boolean scissored = false;
        if (w > 4 && h > 4) {
            graphics.enableScissor(x + 2, y + 2, x + w - 2, y + h - 2);
            scissored = true;
        }

        try {
            int renderY = y + 3;
            for (int i = listScrollOffset; i < totalCount && i < listScrollOffset + visibleCount + 1; i++) {
                TransfurVariant<?> tf = filteredVariants.get(i);
                boolean isSelected = (this.selected == tf);
                boolean isHovered = mouseX >= x + 2 && mouseX <= x + w - 8 && mouseY >= renderY && mouseY < renderY + itemHeight;

                int btnBg = isSelected ? 0xFF384458 : (isHovered ? 0xFF282830 : 0xFF1A1A20);
                int btnBorder = isSelected ? 0xFFFFAA00 : (isHovered ? 0xFF585868 : 0xFF2A2A32);
                int textColor = isSelected ? 0xFFFFAA : (isHovered ? 0xFFFFFF : 0xAAAAAA);

                graphics.fill(x + 3, renderY, x + w - 9, renderY + itemHeight - 2, btnBg);
                graphics.renderOutline(x + 3, renderY, w - 12, itemHeight - 2, btnBorder);

                String displayName = getVariantDisplayName(tf);
                renderScrollingString(graphics, this.font, displayName, x + 6, renderY + 3, x + w - 12, renderY + itemHeight - 2, textColor);

                renderY += itemHeight;
            }
        } finally {
            if (scissored) {
                graphics.disableScissor();
            }
        }

        // Scrollbar with click & drag support + hover state
        if (totalCount > visibleCount && maxScroll > 0) {
            int barX = x + w - 7;
            int trackY = y + 3;
            int trackH = h - 6;
            int trackW = 4;
            int thumbH = Math.max(14, (visibleCount * trackH) / totalCount);
            int travel = trackH - thumbH;
            int thumbY = trackY + (int) ((float) listScrollOffset / maxScroll * travel);

            // Track slot
            graphics.fill(barX, trackY, barX + trackW, trackY + trackH, 0xFF101014);

            // Thumb visual state
            boolean isHovered = mouseX >= barX - 2 && mouseX <= barX + trackW + 2 && mouseY >= thumbY && mouseY <= thumbY + thumbH;
            int thumbBg = isDraggingListScroll ? 0xFF8E8EA0 : (isHovered ? 0xFF767688 : 0xFF4E4E5C);
            int thumbBorder = isDraggingListScroll ? 0xFFBCBCCE : (isHovered ? 0xFF9E9EB2 : 0xFF646476);

            graphics.fill(barX, thumbY, barX + trackW, thumbY + thumbH, thumbBg);
            graphics.fill(barX, thumbY, barX + trackW, thumbY + 1, thumbBorder);
            graphics.fill(barX, thumbY, barX + 1, thumbY + thumbH, thumbBorder);
        }
    }

    /**
     * Middle 3D Viewport with interactive mouse drag rotation & zooming.
     */
    private void renderModelViewport(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY, float partialTick) {
        renderInsetBox(graphics, x, y, w, h);

        if (this.currentEntity != null) {
            int centerX = x + w / 2;
            int centerY = y + h - 26;

            // Oval foot shadow
            graphics.fill(centerX - 24, centerY - 2, centerX + 24, centerY + 3, 0x44000000);
            graphics.fill(centerX - 18, centerY - 3, centerX + 18, centerY + 4, 0x33000000);

            // Silhouette rendering / locked banner
            if (!this.isUnlocked) {
                graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.locked").getString(), centerX - 24, y + 8, 0xFF5555, false);
            }

            // Save old rotations
            float oldYBodyRot = this.currentEntity.yBodyRot;
            float oldYRot = this.currentEntity.getYRot();
            float oldXRot = this.currentEntity.getXRot();
            float oldYHeadRot = this.currentEntity.yHeadRot;
            float oldYHeadRotO = this.currentEntity.yHeadRotO;

            // Align entity forward
            this.currentEntity.yBodyRot = 180.0f;
            this.currentEntity.setYRot(180.0f);
            this.currentEntity.setXRot(0.0f);
            this.currentEntity.yHeadRot = 180.0f;
            this.currentEntity.yHeadRotO = 180.0f;

            // Upright Quaternion: rotateZ by PI flips GUI inverted Y right-side up!
            Quaternionf pose = new Quaternionf()
                    .rotateZ((float) Math.PI)
                    .rotateX(modelPitch * ((float) Math.PI / 180.0f))
                    .rotateY(modelYaw * ((float) Math.PI / 180.0f));

            boolean scissored = false;
            if (w > 4 && h > 4) {
                graphics.enableScissor(x + 2, y + 2, x + w - 2, y + h - 2);
                scissored = true;
            }

            try {
                int modelCenterX = centerX + (int) modelOffsetX;
                int modelCenterY = centerY + (int) modelOffsetY;
                graphics.pose().pushPose();
                graphics.pose().translate(0, 0, 50);
                InventoryScreen.renderEntityInInventory(graphics, modelCenterX, modelCenterY, (int) modelZoom, pose, null, this.currentEntity);
                graphics.pose().popPose();
            } catch (Exception e) {
                graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.render_error").getString(), centerX - 28, centerY - 10, 0xFF5555, false);
            } finally {
                if (scissored) {
                    graphics.disableScissor();
                }
                // Restore old rotations
                this.currentEntity.yBodyRot = oldYBodyRot;
                this.currentEntity.setYRot(oldYRot);
                this.currentEntity.setXRot(oldXRot);
                this.currentEntity.yHeadRot = oldYHeadRot;
                this.currentEntity.yHeadRotO = oldYHeadRotO;
            }
        } else {
            graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.no_model").getString(), x + w / 2 - 24, y + h / 2 - 4, 0x777777, false);
        }

        // Viewport bottom interaction hint (scaled 0.75x to prevent overflow)
        String hint = Component.translatable("gui.changed_addon.bestiary.model_hint").getString();
        int hintW = this.font.width(hint);
        float hintScale = 0.75f;
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x + (w - (int)(hintW * hintScale)) / 2.0, y + h - 9, 0.0);
        pose.scale(hintScale, hintScale, 1.0f);
        graphics.drawString(this.font, hint, 0, 0, 0x888888, false);
        pose.popPose();
    }

    /**
     * Right Details Panel: Lore + Attribute Bar Chart.
     */
    private void renderDetailsAndBarChart(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        renderInsetBox(graphics, x, y, w, h);

        if (this.selected == null) return;

        boolean scissored = false;
        if (w > 6 && h > 6) {
            graphics.enableScissor(x + 3, y + 3, x + w - 3, y + h - 3);
            scissored = true;
        }

        try {
            int curY = y + 5 - detailsScrollOffset;

            // 1. Header: Name & Classification
            String name = getVariantDisplayName(this.selected);
            graphics.drawString(this.font, "§e§l" + name, x + 6, curY, 0xFFFF55, false);
            curY += 12;

            if (!classificationText.isEmpty()) {
                String rawClass = classificationText.startsWith("Classification:")
                        ? classificationText.substring("Classification:".length()).trim()
                        : classificationText;
                String classDisplay = Component.translatable("gui.changed_addon.bestiary.lore.classification", rawClass).getString();
                graphics.drawString(this.font, classDisplay, x + 6, curY, 0xAAAAAA, false);
                curY += 11;
            }

            // Divider
            graphics.fill(x + 5, curY, x + w - 5, curY + 1, 0xFF353540);
            curY += 5;

            // 2. Lore Section
            graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.lore.dossier_header"), x + 6, curY, 0xFFAA00, false);
            curY += 11;

            for (FormattedCharSequence line : loreLines) {
                graphics.drawString(this.font, line, x + 6, curY, 0xD0D0D0, false);
                curY += 10;
            }
            curY += 4;

            // Divider
            graphics.fill(x + 5, curY, x + w - 5, curY + 1, 0xFF353540);
            curY += 5;

            // 3. Attribute Bar Chart - Replaces the old overlapping pie widget
            graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.attribute_sheet_header"), x + 6, curY, 0x55FF55, false);
            curY += 12;

            int barW = w - 16;
            int barH = 5;

            for (AttributeBarItem bar : attributeBars) {
                // Label & Value
                graphics.drawString(this.font, bar.label, x + 6, curY, 0xE0E0E0, false);
                int valW = this.font.width(bar.valueText);
                graphics.drawString(this.font, bar.valueText, x + 6 + barW - valW, curY, 0xFFFFAA, false);
                curY += 10;

                // Bar background slot (inset)
                graphics.fill(x + 6, curY, x + 6 + barW, curY + barH, 0xFF141416);
                graphics.renderOutline(x + 6, curY, barW, barH, 0xFF303038);

                // Filled bar (colored proportional bar)
                int fillW = Math.max(2, Math.round((barW - 2) * bar.ratio));
                graphics.fill(x + 7, curY + 1, x + 7 + fillW, curY + barH - 1, bar.fillColor);
                // 1px bevel highlight on top edge
                graphics.fill(x + 7, curY + 1, x + 7 + fillW, curY + 2, bar.highlightColor);

                curY += barH + 6;
            }
        } finally {
            if (scissored) {
                graphics.disableScissor();
            }
        }

        // Vertical scrollbar for details if needed (interactive click & drag)
        if (maxDetailsScroll > 0) {
            int barX = x + w - 7;
            int trackY = y + 3;
            int trackH = h - 6;
            int trackW = 4;
            int thumbH = Math.max(14, (trackH * trackH) / (trackH + maxDetailsScroll));
            int travel = trackH - thumbH;
            int thumbY = trackY + (int) ((float) detailsScrollOffset / maxDetailsScroll * travel);

            // Track slot
            graphics.fill(barX, trackY, barX + trackW, trackY + trackH, 0xFF101014);

            // Thumb visual state
            boolean isHovered = mouseX >= barX - 2 && mouseX <= barX + trackW + 2 && mouseY >= thumbY && mouseY <= thumbY + thumbH;
            int thumbBg = isDraggingDetailsScroll ? 0xFF8E8EA0 : (isHovered ? 0xFF767688 : 0xFF4E4E5C);
            int thumbBorder = isDraggingDetailsScroll ? 0xFFBCBCCE : (isHovered ? 0xFF9E9EB2 : 0xFF646476);

            graphics.fill(barX, thumbY, barX + trackW, thumbY + thumbH, thumbBg);
            graphics.fill(barX, thumbY, barX + trackW, thumbY + 1, thumbBorder);
            graphics.fill(barX, thumbY, barX + 1, thumbY + thumbH, thumbBorder);
        }
    }

    // -------------------------------------------------------------
    // INPUT HANDLING (Mouse Drag, Wheel Zoom, Clicks)
    // -------------------------------------------------------------
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int leftListX = this.left + 8;
        int leftListY = this.top + 42;
        int leftListH = this.dialogH - 50;

        int vpX = leftListX + this.listW + 6;
        int vpY = this.top + 22;
        int vpH = this.dialogH - 30;

        int detX = vpX + this.vpW + 6;
        int detY = this.top + 22;
        int detH = this.dialogH - 30;

        // Close button click ([ESC] Close in header)
        String escHint = Component.translatable("gui.changed_addon.bestiary.esc_close").getString();
        int escW = this.font.width(escHint);
        int escX = this.left + this.dialogW - escW - 10;
        int escY = this.top + 7;
        if (button == 0 && mouseX >= escX - 4 && mouseX <= escX + escW + 4 && mouseY >= escY - 3 && mouseY <= escY + 12) {
            this.onClose();
            return true;
        }

        // Left List Scrollbar Click & Drag
        int visibleCount = (leftListH - 4) / 18;
        int totalCount = filteredVariants.size();
        int maxListScroll = Math.max(0, totalCount - visibleCount);
        if (button == 0 && maxListScroll > 0) {
            int trackX = leftListX + this.listW - 7;
            int trackY = leftListY + 3;
            int trackH = leftListH - 6;
            int thumbH = Math.max(14, (visibleCount * trackH) / totalCount);
            int travel = trackH - thumbH;
            int thumbY = trackY + (travel > 0 ? (int) ((float) listScrollOffset / maxListScroll * travel) : 0);

            if (mouseX >= trackX - 3 && mouseX <= trackX + 7 && mouseY >= trackY && mouseY <= trackY + trackH) {
                isDraggingListScroll = true;
                if (mouseY >= thumbY && mouseY <= thumbY + thumbH) {
                    listScrollGrabOffset = mouseY - thumbY;
                } else {
                    listScrollGrabOffset = thumbH / 2.0;
                    double targetThumbY = mouseY - listScrollGrabOffset;
                    if (travel > 0) {
                        float progress = (float) (targetThumbY - trackY) / (float) travel;
                        listScrollOffset = clamp(Math.round(progress * maxListScroll), 0, maxListScroll);
                    }
                }
                return true;
            }
        }

        // Right Details Scrollbar Click & Drag
        if (button == 0 && maxDetailsScroll > 0) {
            int trackX = detX + this.detW - 7;
            int trackY = detY + 3;
            int trackH = detH - 6;
            int thumbH = Math.max(14, (trackH * trackH) / (trackH + maxDetailsScroll));
            int travel = trackH - thumbH;
            int thumbY = trackY + (travel > 0 ? (int) ((float) detailsScrollOffset / maxDetailsScroll * travel) : 0);

            if (mouseX >= trackX - 3 && mouseX <= trackX + 7 && mouseY >= trackY && mouseY <= trackY + trackH) {
                isDraggingDetailsScroll = true;
                if (mouseY >= thumbY && mouseY <= thumbY + thumbH) {
                    detailsScrollGrabOffset = mouseY - thumbY;
                } else {
                    detailsScrollGrabOffset = thumbH / 2.0;
                    double targetThumbY = mouseY - detailsScrollGrabOffset;
                    if (travel > 0) {
                        float progress = (float) (targetThumbY - trackY) / (float) travel;
                        detailsScrollOffset = clamp(Math.round(progress * maxDetailsScroll), 0, maxDetailsScroll);
                    }
                }
                return true;
            }
        }

        // Middle Viewport click -> Toggle pose or start drag
        if (mouseX >= vpX && mouseX <= vpX + this.vpW && mouseY >= vpY && mouseY <= vpY + vpH) {
            if (button == 0) {
                isDraggingModel = true;
                return true;
            } else if (button == 1 && currentEntity != null) {
                // Right click model toggles pose
                currentEntity.setPose(currentEntity.getPose() == Pose.STANDING ? Pose.CROUCHING : Pose.STANDING);
                return true;
            }
        }

        // Left List Entries click (excluding scrollbar zone)
        int entryMaxX = leftListX + this.listW - 8;
        if (mouseX >= leftListX && mouseX <= entryMaxX && mouseY >= leftListY && mouseY <= leftListY + leftListH) {
            int itemHeight = 18;
            int clickedIdx = listScrollOffset + (int) ((mouseY - (leftListY + 3)) / itemHeight);
            if (clickedIdx >= 0 && clickedIdx < filteredVariants.size()) {
                selectTf(filteredVariants.get(clickedIdx));
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isDraggingModel = false;
        isDraggingModelOffset = false;
        isDraggingListScroll = false;
        isDraggingDetailsScroll = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDraggingListScroll) {
            int leftListY = this.top + 42;
            int leftListH = this.dialogH - 50;
            int visibleCount = (leftListH - 4) / 18;
            int totalCount = filteredVariants.size();
            int maxListScroll = Math.max(0, totalCount - visibleCount);
            if (maxListScroll > 0) {
                int trackY = leftListY + 3;
                int trackH = leftListH - 6;
                int thumbH = Math.max(14, (visibleCount * trackH) / totalCount);
                int travel = trackH - thumbH;
                if (travel > 0) {
                    double targetThumbY = mouseY - listScrollGrabOffset;
                    float progress = (float) (targetThumbY - trackY) / (float) travel;
                    listScrollOffset = clamp(Math.round(progress * maxListScroll), 0, maxListScroll);
                }
            }
            return true;
        }

        if (isDraggingDetailsScroll) {
            int detY = this.top + 22;
            int detH = this.dialogH - 30;
            if (maxDetailsScroll > 0) {
                int trackY = detY + 3;
                int trackH = detH - 6;
                int thumbH = Math.max(14, (trackH * trackH) / (trackH + maxDetailsScroll));
                int travel = trackH - thumbH;
                if (travel > 0) {
                    double targetThumbY = mouseY - detailsScrollGrabOffset;
                    float progress = (float) (targetThumbY - trackY) / (float) travel;
                    detailsScrollOffset = clamp(Math.round(progress * maxDetailsScroll), 0, maxDetailsScroll);
                }
            }
            return true;
        }

        if (isDraggingModel) {
            modelYaw += (float) (dragX * 1.5);
            modelPitch = clamp(modelPitch - (float) (dragY * 1.5), -45.0f, 45.0f);
            return true;
        }

        if (isDraggingModelOffset) {
            int vpW = this.vpW;
            int vpH = this.dialogH - 30;
            float maxOffsetX = vpW / 2.0f;
            float maxOffsetY = vpH / 2.0f;
            modelOffsetX = clamp(modelOffsetX + (float) dragX, -maxOffsetX, maxOffsetX);
            modelOffsetY = clamp(modelOffsetY + (float) dragY, -maxOffsetY, maxOffsetY);
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int leftListX = this.left + 8;
        int leftListY = this.top + 42;
        int leftListH = this.dialogH - 50;

        int vpX = leftListX + this.listW + 6;
        int vpY = this.top + 22;
        int vpH = this.dialogH - 30;

        int detX = vpX + this.vpW + 6;
        int detY = this.top + 22;
        int detH = this.dialogH - 30;

        // Wheel on 3D Viewport -> Zoom
        if (mouseX >= vpX && mouseX <= vpX + this.vpW && mouseY >= vpY && mouseY <= vpY + vpH) {
            modelZoom = clamp(modelZoom + (float) (delta * 3.0), 20.0f, 85.0f);
            return true;
        }

        // Wheel on Left List -> Scroll List
        if (mouseX >= leftListX && mouseX <= leftListX + this.listW && mouseY >= leftListY && mouseY <= leftListY + leftListH) {
            int visibleCount = (leftListH - 4) / 18;
            int maxScroll = Math.max(0, filteredVariants.size() - visibleCount);
            listScrollOffset = clamp(listScrollOffset - (int) Math.signum(delta) * 2, 0, maxScroll);
            return true;
        }

        // Wheel on Right Details -> Scroll details
        if (mouseX >= detX && mouseX <= detX + this.detW && mouseY >= detY && mouseY <= detY + detH) {
            if (maxDetailsScroll > 0) {
                detailsScrollOffset = clamp(detailsScrollOffset - (int) Math.signum(delta) * 12, 0, maxDetailsScroll);
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // -------------------------------------------------------------
    // Helper Data Class for Attribute Bar Item
    // -------------------------------------------------------------
    private static class AttributeBarItem {
        final Component label;
        final String valueText;
        final float ratio;
        final int fillColor;
        final int highlightColor;

        AttributeBarItem(Component label, String valueText, float ratio, int fillColor, int highlightColor) {
            this.label = label;
            this.valueText = valueText;
            this.ratio = ratio;
            this.fillColor = fillColor;
            this.highlightColor = highlightColor;
        }
    }

    private static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Renders a string. If the string exceeds maxW, it applies a smooth left-right ping-pong scrolling effect.
     */
    private void renderScrollingString(GuiGraphics graphics, Font font, String text, int minX, int minY, int maxX, int maxY, int color) {
        int textW = font.width(text);
        int maxW = maxX - minX;

        if (maxW <= 0) {
            return;
        }

        if (textW <= maxW) {
            graphics.drawString(font, text, minX, minY, color, false);
            return;
        }

        int diff = textW - maxW;
        double speed = 25.0; // pixels per second
        double travelTime = Math.max(1.0, diff / speed);
        double pauseTime = 1.0; // 1 second pause at start and end
        double oneWay = travelTime + pauseTime;
        double totalPeriod = oneWay * 2.0;

        long cycleMs = (long) (totalPeriod * 1000.0);
        double t = ((double) Math.floorMod(System.currentTimeMillis(), cycleMs)) / 1000.0;
        double offset;
        if (t < pauseTime) {
            offset = 0;
        } else if (t < pauseTime + travelTime) {
            double progress = (t - pauseTime) / travelTime;
            double eased = (1.0 - Math.cos(progress * Math.PI)) / 2.0;
            offset = -diff * eased;
        } else if (t < pauseTime + travelTime + pauseTime) {
            offset = -diff;
        } else {
            double progress = (t - pauseTime - travelTime - pauseTime) / travelTime;
            double eased = (1.0 - Math.cos(progress * Math.PI)) / 2.0;
            offset = -diff * (1.0 - eased);
        }

        graphics.enableScissor(minX, minY - 1, maxX, maxY + 1);
        try {
            graphics.drawString(font, text, (int) Math.round(minX + offset), minY, color, false);
        } finally {
            graphics.disableScissor();
        }
    }
}